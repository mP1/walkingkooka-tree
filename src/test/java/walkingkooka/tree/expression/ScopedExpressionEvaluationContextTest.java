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
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ScopedExpressionEvaluationContextTest implements ExpressionEvaluationContextTesting<ScopedExpressionEvaluationContext> {

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
                .context(r ->
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

    // ExpressionEvaluationContext......................................................................................

    @Override
    public void testEvaluateExpressionUnknownFunctionNameFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ScopedExpressionEvaluationContext createContext() {
        return ScopedExpressionEvaluationContext.with(
            REFERENCE_TO_VALUE,
            ExpressionEvaluationContexts.basic(
                ExpressionNumberKind.BIG_DECIMAL,
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
                    0,
                    Converters.fake(),
                    DateTimeContexts.locale(
                        Locale.ENGLISH,
                        1900,
                        50,
                        LocalDateTime::now
                    ),
                    DecimalNumberContexts.american(MathContext.DECIMAL32)
                )
            )
        );
    }

    @Override
    public String currencySymbol() {
        return DECIMAL_NUMBER_CONTEXT.currencySymbol();
    }

    @Override
    public char decimalSeparator() {
        return DECIMAL_NUMBER_CONTEXT.decimalSeparator();
    }

    @Override
    public String exponentSymbol() {
        return DECIMAL_NUMBER_CONTEXT.exponentSymbol();
    }

    @Override
    public char groupSeparator() {
        return DECIMAL_NUMBER_CONTEXT.groupSeparator();
    }

    @Override
    public MathContext mathContext() {
        return DECIMAL_NUMBER_CONTEXT.mathContext();
    }

    @Override
    public char negativeSign() {
        return DECIMAL_NUMBER_CONTEXT.negativeSign();
    }

    @Override
    public char percentageSymbol() {
        return DECIMAL_NUMBER_CONTEXT.percentageSymbol();
    }

    @Override
    public char positiveSign() {
        return DECIMAL_NUMBER_CONTEXT.positiveSign();
    }

    private final static DecimalNumberContext DECIMAL_NUMBER_CONTEXT = DecimalNumberContexts.american(MathContext.DECIMAL32);

    // class............................................................................................................

    @Override
    public Class<ScopedExpressionEvaluationContext> type() {
        return ScopedExpressionEvaluationContext.class;
    }
}
