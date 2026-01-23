/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.tree.expression;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentContexts;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterCardinality;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.tree.expression.function.FakeExpressionFunction;

import java.math.MathContext;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ScopedExpressionEvaluationContextTest implements ExpressionEvaluationContextTesting<ScopedExpressionEvaluationContext>,
    DecimalNumberContextDelegator {

    private final static ExpressionReference LOCAL_REFERENCE = new ExpressionReference() {
        @Override
        public boolean testParameterName(final ExpressionFunctionParameterName parameterName) {
            return false;
        }
    };

    private final static String LOCAL_REFERENCE_VALUE = "local reference value";

    private final static Function<ExpressionReference, Optional<Optional<Object>>> REFERENCE_TO_VALUE = (r) -> r.equals(LOCAL_REFERENCE) ?
        Optional.of(Optional.of(LOCAL_REFERENCE_VALUE)) :
        Optional.empty();


    private final static ExpressionReference GLOBAL_REFERENCE = new ExpressionReference() {

        @Override
        public boolean testParameterName(final ExpressionFunctionParameterName parameterName) {
            return false;
        }
    };

    private final static String GLOBAL_REFERENCE_VALUE = "global reference value";

    // with.............................................................................................................

    @Test
    public void testWithNullScopeFails() {
        assertThrows(
            NullPointerException.class,
            () -> ScopedExpressionEvaluationContext.with(
                null,
                ExpressionEvaluationContexts.fake()
            )
        );
    }

    @Test
    public void testWithNullExpressionEvaluationContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> ScopedExpressionEvaluationContext.with(
                REFERENCE_TO_VALUE,
                null
            )
        );
    }

    // reference........................................................................................................

    @Test
    public void testReferenceWithUnknownReference() {
        this.referenceAndCheck(
            this.createContext(),
            new FakeExpressionReference()
        );
    }

    @Test
    public void testReferenceWithGlobalReference() {
        this.referenceAndCheck(
            this.createContext(),
            GLOBAL_REFERENCE,
            GLOBAL_REFERENCE_VALUE
        );
    }

    @Test
    public void testReferenceWithLocalReference() {
        this.referenceAndCheck(
            this.createContext(),
            LOCAL_REFERENCE,
            LOCAL_REFERENCE_VALUE
        );
    }

    @Test
    public void testReferenceWithDoubleLocalReference() {
        final String value = "222";
        final ExpressionReference reference = new FakeExpressionReference();

        this.referenceAndCheck(
            this.createContext()
                .enterScope(r ->
                    Optional.of(
                        Optional.ofNullable(
                            r.equals(reference) ?
                                value :
                                null
                        )
                    )
                ),
            reference,
            value
        );
    }

    // evaluateExpression...............................................................................................

    @Test
    public void testEvaluateExpressionWithGlobalReference() {
        this.evaluateExpressionAndCheck(
            this.createContext(),
            Expression.reference(GLOBAL_REFERENCE),
            GLOBAL_REFERENCE_VALUE
        );
    }

    @Test
    public void testEvaluateExpressionWithLocalReference() {
        this.evaluateExpressionAndCheck(
            this.createContext(),
            Expression.reference(LOCAL_REFERENCE),
            LOCAL_REFERENCE_VALUE
        );
    }

    @Test
    public void testEvaluateExpressionWithUnknownReference() {
        assertThrows(
            ExpressionEvaluationReferenceException.class,
            () -> this.createContext()
                .evaluateExpression(
                    Expression.reference(new FakeExpressionReference())
                )
        );
    }

    // evaluateFunction.................................................................................................

    @Test
    public void testEvaluateFunctionWithGlobalReference() {
        this.evaluateFunctionAndCheck(
            this.createContext(),
            new TestFunction() {

                @Override
                public Object apply(final List<Object> objects,
                                    final ScopedExpressionEvaluationContext context) {
                    return context.referenceOrFail(
                        (ExpressionReference) objects.get(0)
                    );
                }
            },
            Lists.of(GLOBAL_REFERENCE),
            GLOBAL_REFERENCE_VALUE
        );
    }

    @Test
    public void testEvaluateFunctionWithLocalReference() {
        this.evaluateFunctionAndCheck(
            this.createContext(),
            new TestFunction() {

                @Override
                public Object apply(final List<Object> objects,
                                    final ScopedExpressionEvaluationContext context) {
                    return context.referenceOrFail(
                        (ExpressionReference) objects.get(0)
                    );
                }
            },
            Lists.of(LOCAL_REFERENCE),
            LOCAL_REFERENCE_VALUE
        );
    }

    @Test
    public void testEvaluateFunctionWithUnknownReference() {
        final Object unknownDefault = "***UnknownDefault***";

        this.evaluateFunctionAndCheck(
            this.createContext(),
            new TestFunction() {

                @Override
                public Object apply(final List<Object> objects,
                                    final ScopedExpressionEvaluationContext context) {
                    return context.reference(
                            (ExpressionReference) objects.get(0)
                        ).map(Optional::get)
                        .orElse(
                            unknownDefault
                        );
                }
            },
            Lists.of(new FakeExpressionReference()),
            unknownDefault
        );
    }

    abstract class TestFunction extends FakeExpressionFunction<Object, ScopedExpressionEvaluationContext> {

        @Override
        public final Optional<ExpressionFunctionName> name() {
            return Optional.of(
                ExpressionFunctionName.with("TestFunction")
            );
        }

        @Override
        public abstract Object apply(final List<Object> objects, final ScopedExpressionEvaluationContext context);

        @Override
        public final List<ExpressionFunctionParameter<?>> parameters(final int count) {
            checkEquals(1, count, "parameterCount");

            return Lists.of(
                ExpressionFunctionParameter.with(
                    ExpressionFunctionParameterName.with("reference-parameter"),
                    Object.class,
                    ExpressionFunctionParameterCardinality.REQUIRED,
                    Optional.empty(), // defaultValue
                    ExpressionFunctionParameter.NO_KINDS
                )
            );
        }
    }

    // ExpressionEvaluationContext......................................................................................

    @Override
    public void testEvaluateExpressionUnknownFunctionNameFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ScopedExpressionEvaluationContext createContext() {
        final Locale locale = Locale.ENGLISH;

        return ScopedExpressionEvaluationContext.with(
            REFERENCE_TO_VALUE,
            ExpressionEvaluationContexts.basic(
                ExpressionNumberKind.BIG_DECIMAL,
                (e, c) -> {
                    Objects.requireNonNull(e, "expression");
                    throw new UnsupportedOperationException();
                },
                (fn) -> {
                    Objects.requireNonNull(fn, "fn");
                    throw new UnsupportedOperationException();
                },
                (rr) -> {
                    Objects.requireNonNull(rr, "rr");
                    throw rr;
                },
                (ref) -> {
                    Objects.requireNonNull(ref, "ref");

                    return ref.equals(GLOBAL_REFERENCE) ?
                        Optional.of(Optional.of(GLOBAL_REFERENCE_VALUE)) :
                        Optional.empty();
                },
                ExpressionEvaluationContexts.referenceNotFound(),
                CaseSensitivity.SENSITIVE,
                ConverterContexts.basic(
                    false, // canNumbersHaveGroupSeparator
                    0,
                    Indentation.SPACES2,
                    LineEnding.NL,
                    ',', // valueSeparator
                    Converters.fake(),
                    DateTimeContexts.basic(
                        DateTimeSymbols.fromDateFormatSymbols(
                            new DateFormatSymbols(locale)
                        ),
                        locale,
                        1900,
                        50,
                        () -> LocalDateTime.MIN
                    ),
                    DecimalNumberContexts.american(MathContext.DECIMAL32)
                ),
                EnvironmentContexts.map(
                    EnvironmentContexts.empty(
                        LineEnding.NL,
                        locale,
                        () -> LocalDateTime.MIN,
                        EnvironmentContext.ANONYMOUS
                    )
                ),
                LocaleContexts.jre(locale)
            )
        );
    }

    // DecimalNumberContextDelegator....................................................................................

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return DECIMAL_NUMBER_CONTEXT;
    }

    @Override
    public int decimalNumberDigitCount() {
        return DECIMAL_NUMBER_CONTEXT.decimalNumberDigitCount();
    }

    @Override
    public MathContext mathContext() {
        return DECIMAL_NUMBER_CONTEXT.mathContext();
    }


    private final static DecimalNumberContext DECIMAL_NUMBER_CONTEXT = DecimalNumberContexts.american(MathContext.DECIMAL32);

    // class............................................................................................................

    @Override
    public Class<ScopedExpressionEvaluationContext> type() {
        return ScopedExpressionEvaluationContext.class;
    }
}
