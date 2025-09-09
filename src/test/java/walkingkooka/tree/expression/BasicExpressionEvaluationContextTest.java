/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverterContext;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.tree.expression.function.FakeExpressionFunction;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.math.MathContext;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicExpressionEvaluationContextTest implements ClassTesting2<BasicExpressionEvaluationContext>,
    ExpressionEvaluationContextTesting<BasicExpressionEvaluationContext>,
    ToStringTesting<BasicExpressionEvaluationContext>,
    DecimalNumberContextDelegator {

    private final static ExpressionNumberKind KIND = ExpressionNumberKind.DEFAULT;

    private final static ExpressionReference REFERENCE = new FakeExpressionReference() {
    };

    private final static Object REFERENCE_VALUE = "*123*";

    private final static String REFERENCE_NOT_FOUND_MESSAGE = "CustomMessage123";

    private final static Function<ExpressionReference, ExpressionEvaluationException> REFERENCE_NOT_FOUND =
        (r) ->
            new ExpressionEvaluationReferenceException(
                REFERENCE_NOT_FOUND_MESSAGE,
                r
            );

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;

    private final static Function<RuntimeException, Object> EXCEPTION_HANDLER = (r) -> {
        throw r;
    };

    private final static LocaleContext LOCALE_CONTEXT = LocaleContexts.jre(
        Locale.forLanguageTag("EN-AU")
    );

    @Test
    public void testWithNullExpressionNumberKindFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionEvaluationContext.with(
                null,
                this.functions(),
                EXCEPTION_HANDLER,
                this.references(),
                ExpressionEvaluationContexts.referenceNotFound(),
                CASE_SENSITIVITY,
                this.converterContext(),
                LOCALE_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullFunctionsFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionEvaluationContext.with(
                KIND,
                null,
                EXCEPTION_HANDLER,
                this.references(),
                ExpressionEvaluationContexts.referenceNotFound(),
                CASE_SENSITIVITY,
                this.converterContext(),
                LOCALE_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullExceptionHandlerFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionEvaluationContext.with(
                KIND,
                this.functions(),
                null,
                this.references(),
                ExpressionEvaluationContexts.referenceNotFound(),
                CASE_SENSITIVITY,
                this.converterContext(),
                LOCALE_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullReferencesFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionEvaluationContext.with(
                KIND,
                this.functions(),
                EXCEPTION_HANDLER,
                null,
                ExpressionEvaluationContexts.referenceNotFound(),
                CASE_SENSITIVITY,
                this.converterContext(),
                LOCALE_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullReferenceNotFoundFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionEvaluationContext.with(
                KIND,
                this.functions(),
                EXCEPTION_HANDLER,
                this.references(),
                null,
                CASE_SENSITIVITY,
                this.converterContext(),
                LOCALE_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullCaseSensitivityFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionEvaluationContext.with(
                KIND,
                this.functions(),
                EXCEPTION_HANDLER,
                this.references(),
                ExpressionEvaluationContexts.referenceNotFound(),
                null,
                this.converterContext(),
                LOCALE_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullConverterContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionEvaluationContext.with(
                KIND,
                this.functions(),
                EXCEPTION_HANDLER,
                this.references(),
                ExpressionEvaluationContexts.referenceNotFound(),
                CASE_SENSITIVITY,
                null,
                LOCALE_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullLocaleContextContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionEvaluationContext.with(
                KIND,
                this.functions(),
                EXCEPTION_HANDLER,
                this.references(),
                ExpressionEvaluationContexts.referenceNotFound(),
                CASE_SENSITIVITY,
                this.converterContext(),
                null
            )
        );
    }

    // evaluateFunction.................................................................................................

    @Test
    public void testEvaluateFunction() {
        final Object value = this.functionValue();

        final ExpressionFunction<Object, BasicExpressionEvaluationContext> function = new FakeExpressionFunction<>() {

            @Override
            public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                return ExpressionFunctionParameter.EMPTY;
            }

            @Override
            public Object apply(final List<Object> objects,
                                final BasicExpressionEvaluationContext context) {
                return value;
            }
        };

        this.evaluateFunctionAndCheck(
            function,
            this.parameters(),
            value
        );
    }

    @Test
    public void testEvaluateFunctionThrownHandled() {
        final String error = "**ERROR**";

        final ExpressionFunction<String, BasicExpressionEvaluationContext> function = new FakeExpressionFunction<>() {

            @Override
            public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                return ExpressionFunctionParameter.EMPTY;
            }

            @Override
            public String apply(final List<Object> objects,
                                final BasicExpressionEvaluationContext context) {
                throw new UnsupportedOperationException(error);
            }
        };

        this.evaluateFunctionAndCheck(
            this.createContext(
                (n) -> {
                    throw new UnsupportedOperationException();
                },
                Throwable::getMessage
            ),
            function,
            ExpressionEvaluationContext.NO_PARAMETERS,
            error
        );
    }

    // evaluateExpression...............................................................................................

    @Test
    public void testEvaluateExpressionTrue() {
        this.evaluateExpressionAndCheck2(true);
    }

    @Test
    public void testEvaluateExpressionFalse() {
        this.evaluateExpressionAndCheck2(false);
    }

    private void evaluateExpressionAndCheck2(final boolean value) {
        this.evaluateExpressionAndCheck(
            Expression.value(value),
            value
        );
    }

    @Test
    public void testEvaluateExpressionThrowsExceptionTranslated() {
        final ExpressionNumberKind kind = ExpressionNumberKind.DOUBLE;

        this.evaluateExpressionAndCheck(
            BasicExpressionEvaluationContext.with(
                kind,
                (n) -> {
                    throw new UnsupportedOperationException();
                },
                (r) -> "@@@" + r.getMessage(),
                (r) -> {
                    throw new UnsupportedOperationException();
                },
                (r) -> {
                    throw new UnsupportedOperationException();
                },
                CASE_SENSITIVITY,
                new FakeConverterContext() {
                    @Override
                    public <T> Either<T, String> convert(final Object value,
                                                         final Class<T> target) {
                        return Cast.to(
                            Either.left(value)
                        );
                    }
                },
                LOCALE_CONTEXT
            ),
            Expression.divide(
                Expression.value(kind.one()),
                Expression.value(kind.zero())
            ),
            "@@@Division by zero"
        );
    }

    @Test
    public void testEvaluateExpressionString() {
        final String value = "abc123";
        this.evaluateExpressionAndCheck(
            Expression.value(value),
            value
        );
    }

    // string equals....................................................................................................

    @Test
    public void testEvaluateExpressionStringEqualsCaseSensitive() {
        this.evaluateExpressionStringEqualsAndCheck(
            "abc",
            "abc",
            CaseSensitivity.SENSITIVE,
            true
        );
    }

    @Test
    public void testEvaluateExpressionStringEqualsCaseSensitiveCaseDifferent() {
        this.evaluateExpressionStringEqualsAndCheck(
            "abc",
            "ABC",
            CaseSensitivity.SENSITIVE,
            false
        );
    }

    @Test
    public void testEvaluateExpressionStringEqualsCaseInsensitiveCaseDifferent() {
        this.evaluateExpressionStringEqualsAndCheck(
            "abc",
            "ABC",
            CaseSensitivity.INSENSITIVE,
            true
        );
    }

    @Test
    public void testEvaluateExpressionStringEqualsCaseSensitiveDifferent() {
        this.evaluateExpressionStringEqualsAndCheck(
            "abc",
            "different",
            CaseSensitivity.SENSITIVE,
            false
        );
    }

    private void evaluateExpressionStringEqualsAndCheck(final String left,
                                                        final String right,
                                                        final CaseSensitivity caseSensitivity,
                                                        final boolean expected) {
        this.evaluateExpressionAndCheck(
            this.createContext(true, caseSensitivity),
            Expression.equalsExpression(
                Expression.value(left),
                Expression.value(right)
            ),
            expected
        );
    }

    // string notEquals.................................................................................................

    @Test
    public void testEvaluateExpressionStringNotEqualsCaseSensitive() {
        this.evaluateExpressionStringNotEqualsAndCheck(
            "abc",
            "abc",
            CaseSensitivity.SENSITIVE,
            false
        );
    }

    @Test
    public void testEvaluateExpressionStringNotEqualsCaseSensitiveCaseDifferent() {
        this.evaluateExpressionStringNotEqualsAndCheck(
            "abc",
            "ABC",
            CaseSensitivity.SENSITIVE,
            true
        );
    }

    @Test
    public void testEvaluateExpressionStringNotEqualsCaseInsensitiveCaseDifferent() {
        this.evaluateExpressionStringNotEqualsAndCheck(
            "abc",
            "ABC",
            CaseSensitivity.INSENSITIVE,
            false
        );
    }

    @Test
    public void testEvaluateExpressionStringNotEqualsCaseSensitiveDifferent() {
        this.evaluateExpressionStringNotEqualsAndCheck(
            "abc",
            "different",
            CaseSensitivity.SENSITIVE,
            true
        );
    }

    private void evaluateExpressionStringNotEqualsAndCheck(final String left,
                                                           final String right,
                                                           final CaseSensitivity stringNotEqualsCaseSensitivity,
                                                           final boolean expected) {
        this.evaluateExpressionAndCheck(
            this.createContext(true, stringNotEqualsCaseSensitivity),
            Expression.notEquals(
                Expression.value(left),
                Expression.value(right)
            ),
            expected
        );
    }

    // namedFunction........................................................................................................

    @Test
    public void testEvaluateFunctionThrowsExceptionTranslated() {
        final ExpressionNumberKind kind = ExpressionNumberKind.DOUBLE;

        final ExpressionFunction<String, BasicExpressionEvaluationContext> function = new FakeExpressionFunction<>() {
            @Override
            public String apply(final List<Object> objects,
                                final BasicExpressionEvaluationContext context) {
                throw new UnsupportedOperationException("Thrown123");
            }

            @Override
            public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                return ExpressionFunctionParameter.EMPTY;
            }
        };

        this.evaluateFunctionAndCheck(
            BasicExpressionEvaluationContext.with(
                kind,
                (n) -> {
                    throw new UnsupportedOperationException();
                },
                (r) -> "@@@" + r.getMessage(),
                (r) -> {
                    throw new UnsupportedOperationException();
                },
                (r) -> {
                    throw new UnsupportedOperationException();
                },
                CASE_SENSITIVITY,
                new FakeConverterContext() {
                    @Override
                    public <T> Either<T, String> convert(final Object value,
                                                         final Class<T> target) {
                        return Cast.to(
                            Either.left(value)
                        );
                    }
                },
                LOCALE_CONTEXT
            ),
            function,
            Lists.empty(),
            "@@@Thrown123"
        );
    }

    // isPure..........................................................................................................

    @Test
    public void testIsPureTrue() {
        this.isPureAndCheck2(true);
    }

    @Test
    public void testIsPureFalse() {
        this.isPureAndCheck2(false);
    }

    private void isPureAndCheck2(final boolean pure) {
        this.isPureAndCheck(
            this.createContext(pure, CASE_SENSITIVITY),
            this.functionName(),
            pure
        );
    }

    // referencesNotFound...............................................................................................

    @Test
    public void testReferenceNotFound() {
        final ExpressionEvaluationReferenceException thrown = (ExpressionEvaluationReferenceException) this.createContext()
            .referenceNotFound(REFERENCE);
        this.checkEquals(
            REFERENCE,
            thrown.expressionReference()
        );
        this.checkEquals(
            REFERENCE_NOT_FOUND_MESSAGE,
            thrown.getMessage()
        );
    }

    // reference........................................................................................................

    @Test
    public void testReference() {
        this.referenceAndCheck(
            this.createContext(),
            REFERENCE,
            REFERENCE_VALUE
        );
    }

    @Test
    public void testReferenceCycle() {
        this.referenceAndCheck(
            this.createContext(
                (r) -> Optional.of(
                    Optional.of(r)
                )
            ),
            REFERENCE,
            REFERENCE
        );
    }

    // enterScope.......................................................................................................

    @Test
    public void testEnterScopeWithGlobalReference() {
        this.referenceAndCheck(
            this.createContext()
                .enterScope(
                    r -> Optional.empty() // no locals
                ),
            REFERENCE,
            REFERENCE_VALUE
        );
    }

    @Test
    public void testEnterScopeWithLocalReference() {
        final ExpressionReference reference = new FakeExpressionReference();
        final String value = "*reference value*";

        this.referenceAndCheck(
            this.createContext()
                .enterScope(
                    r -> r.equals(reference) ?
                        Optional.of(
                            Optional.of(value)
                        ) :
                        Optional.empty()
                ),
            reference,
            value
        );
    }

    // convert..........................................................................................................

    @Test
    public void testConvert() {
        this.convertAndCheck(123.0, Long.class, 123L);
    }

    @Test
    public void testStringEqualsCaseSensitivity() {
        this.checkEquals(
            CaseSensitivity.SENSITIVE,
            this.createContext(CaseSensitivity.SENSITIVE).stringEqualsCaseSensitivity()
        );
    }

    @Test
    public void testStringEqualsCaseSensitivity2() {
        this.checkEquals(
            CaseSensitivity.INSENSITIVE,
            this.createContext(CaseSensitivity.INSENSITIVE).stringEqualsCaseSensitivity()
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions = this.functions();
        final Function<ExpressionReference, Optional<Optional<Object>>> references = this.references();
        final Function<ExpressionReference, ExpressionEvaluationException> referenceNotFound = ExpressionEvaluationContexts.referenceNotFound();
        final ConverterContext converterContext = this.converterContext();

        this.toStringAndCheck(
            BasicExpressionEvaluationContext.with(
                KIND,
                functions,
                EXCEPTION_HANDLER,
                references,
                referenceNotFound,
                CASE_SENSITIVITY,
                converterContext,
                LOCALE_CONTEXT
            ),
            KIND +
                " " +
                functions +
                " " +
                EXCEPTION_HANDLER +
                " " +
                references +
                " " +
                referenceNotFound +
                " " +
                CASE_SENSITIVITY +
                " " +
                converterContext +
                " " +
                LOCALE_CONTEXT
        );
    }

    // helpers.........................................................................................................

    @Override
    public BasicExpressionEvaluationContext createContext() {
        return this.createContext(true);
    }

    private BasicExpressionEvaluationContext createContext(final boolean pure) {
        return this.createContext(
            pure,
            CASE_SENSITIVITY
        );
    }

    private BasicExpressionEvaluationContext createContext(final CaseSensitivity caseSensitivity) {
        return this.createContext(
            true,
            caseSensitivity
        );
    }

    private BasicExpressionEvaluationContext createContext(final boolean pure,
                                                           final CaseSensitivity caseSensitivity) {
        return BasicExpressionEvaluationContext.with(
            KIND,
            this.functions(pure),
            EXCEPTION_HANDLER,
            this.references(),
            REFERENCE_NOT_FOUND,
            caseSensitivity,
            this.converterContext(),
            LOCALE_CONTEXT
        );
    }

    private BasicExpressionEvaluationContext createContext(final Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions,
                                                           final Function<RuntimeException, Object> exceptionHandler) {
        return BasicExpressionEvaluationContext.with(
            KIND,
            functions,
            exceptionHandler,
            this.references(),
            (r) -> new ExpressionEvaluationReferenceException(REFERENCE_NOT_FOUND_MESSAGE, r),
            CASE_SENSITIVITY,
            this.converterContext(),
            LOCALE_CONTEXT
        );
    }

    private Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions() {
        return this.functions(true);
    }

    private Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions(final boolean pure) {
        return (functionName) -> {
            Objects.requireNonNull(functionName, "functionName");

            if (false == this.functionName().equals(functionName)) {
                throw new UnknownExpressionFunctionException(functionName);
            }

            return new FakeExpressionFunction<>() {
                @Override
                public Object apply(final List<Object> parameters,
                                    final ExpressionEvaluationContext context) {
                    Objects.requireNonNull(parameters, "parameters");
                    Objects.requireNonNull(context, "context");

                    return BasicExpressionEvaluationContextTest.this.functionValue();
                }

                @Override
                public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                    return Lists.of(
                        ExpressionFunctionParameterName.VALUE.required(Object.class)
                    );
                }

                @Override
                public boolean isPure(final ExpressionPurityContext context) {
                    return pure;
                }
            };
        };
    }

    private ExpressionFunctionName functionName() {
        return ExpressionFunctionName.with("sum");
    }

    private List<Object> parameters() {
        return Lists.of("parameter-1", 2);
    }

    private Object functionValue() {
        return "namedFunction-value-234";
    }

    private Function<ExpressionReference, Optional<Optional<Object>>> references() {
        return (r -> {
            Objects.requireNonNull(r, "references");
            this.checkEquals(REFERENCE, r, "reference");

            return Optional.of(
                Optional.of(REFERENCE_VALUE)
            );
        });
    }

    private BasicExpressionEvaluationContext createContext(final Function<ExpressionReference, Optional<Optional<Object>>> references) {
        return BasicExpressionEvaluationContext.with(
            KIND,
            (n) -> {
                throw new UnsupportedOperationException();
            },
            EXCEPTION_HANDLER,
            references,
            REFERENCE_NOT_FOUND,
            CASE_SENSITIVITY,
            ConverterContexts.fake(),
            LOCALE_CONTEXT
        );
    }

    private ConverterContext converterContext() {
        return ConverterContexts.basic(
            false, // canNumbersHaveGroupSeparator
            Converters.JAVA_EPOCH_OFFSET, // dateOffset
            ',', // valueSeparator
            Converters.collection(
                Lists.of(
                    Converters.numberToNumber(),
                    Converters.simple()
                )
            ),
            DateTimeContexts.fake(),
            this.decimalNumberContext()
        );
    }

    // DecimalNumberContextDelegator....................................................................................

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.american(this.mathContext());
    }

    @Override
    public MathContext mathContext() {
        return MathContext.DECIMAL32;
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<BasicExpressionEvaluationContext> type() {
        return Cast.to(BasicExpressionEvaluationContext.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
