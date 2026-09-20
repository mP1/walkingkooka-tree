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
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.BinaryNumberConverterFunctions;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.ConverterException;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverterContext;
import walkingkooka.currency.CurrencyLocaleContexts;
import walkingkooka.datetime.DateTimeContextTesting;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentContextTesting;
import walkingkooka.logging.CanLog;
import walkingkooka.logging.CanLogs;
import walkingkooka.logging.LoggingLevel;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.ThrowableTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.TextPrinting;
import walkingkooka.text.printer.Printers;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.tree.expression.function.FakeExpressionFunction;

import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionEvaluationContextBasicTest implements ClassTesting2<ExpressionEvaluationContextBasic>,
    ExpressionEvaluationContextTesting2<ExpressionEvaluationContextBasic>,
    ToStringTesting<ExpressionEvaluationContextBasic>,
    DateTimeContextTesting,
    DecimalNumberContextDelegator,
    EnvironmentContextTesting,
    HasExpressionNumberKindTesting,
    ThrowableTesting {

    private final static BiFunction<String, ExpressionEvaluationContext, Object> EVALUATOR = new BiFunction<>() {
        @Override
        public Object apply(final String expression,
                            final ExpressionEvaluationContext context) {
            Objects.requireNonNull(expression, "expression");
            return expression + expression;
        }

        @Override
        public String toString() {
            return "EVALUATOR";
        }
    };

    private static Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions(final boolean pure) {
        return new Function<>() {

            @Override
            public ExpressionFunction<?, ExpressionEvaluationContext> apply(final ExpressionFunctionName functionName) {
                Objects.requireNonNull(functionName, "functionName");

                if (false == FUNCTION_NAME.equals(functionName)) {
                    throw functionName.unknownExpressionFunctionException();
                }

                return new FakeExpressionFunction<>() {
                    @Override
                    public Object apply(final List<Object> parameters,
                                        final ExpressionEvaluationContext context) {
                        Objects.requireNonNull(parameters, "parameters");
                        Objects.requireNonNull(context, "context");

                        return FUNCTION_VALUE;
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
            }

            public String toString() {
                return "FUNCTIONS";
            }
        };
    }

    private final static ExpressionFunctionName FUNCTION_NAME = ExpressionFunctionName.with("sum");

    private final static List<Object> FUNCTION_PARAMETERS = Lists.of(
        "parameter-1",
        2
    );

    private final static Object FUNCTION_VALUE = "function-value-234";

    private final static Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> FUNCTIONS = functions(true);

    private final static ExpressionReference REFERENCE = new FakeExpressionReference() {
    };

    private final static Object REFERENCE_VALUE = "*123*";

    private final static String REFERENCE_NOT_FOUND_MESSAGE = "CustomMessage123";

    private final static Function<ExpressionReference, ExpressionEvaluationException> REFERENCE_NOT_FOUND = new Function<>() {
        @Override
        public ExpressionEvaluationException apply(ExpressionReference reference) {
            return new ExpressionEvaluationReferenceException(
                REFERENCE_NOT_FOUND_MESSAGE,
                reference
            );
        }

        @Override
        public String toString() {
            return "REFERENCE_NOT_FOUND";
        }
    };

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;

    private final static Function<RuntimeException, Object> EXCEPTION_HANDLER = new Function<>() {
        @Override
        public Object apply(final RuntimeException caught) {
            throw caught;
        }

        @Override
        public String toString() {
            return "EXCEPTION_HANDLER";
        }
    };

    private final static ConverterContext CONVERTER_CONTEXT = ConverterContexts.basic(
        false, // canNumbersHaveGroupSeparator
        Converters.JAVA_EPOCH_OFFSET, // dateOffset
        ',', // valueSeparator
        Converters.collection(
            Lists.of(
                Converters.numberToNumber(),
                Converters.simple()
            )
        ),
        BinaryNumberConverterFunctions.fake(), // multiplier
        TextPrinting.with(
            DIFFERENT_INDENTATION,
            DIFFERENT_LINE_ENDING
        ).setCharset(CHARSET),
        CurrencyLocaleContexts.fake(),
        DATE_TIME_CONTEXT,
        DECIMAL_NUMBER_CONTEXT
    );

    private final static Function<ExpressionReference, Optional<Optional<Object>>> REFERENCES = new Function<>() {

        @Override
        public Optional<Optional<Object>> apply(final ExpressionReference reference) {
            Objects.requireNonNull(reference, "references");
            if (false == REFERENCE.equals(reference)) {
                throw new IllegalArgumentException("Invalid reference " + reference);
            }

            return Optional.of(
                Optional.of(REFERENCE_VALUE)
            );
        }

        @Override
        public String toString() {
            return "REFERENCES";
        }
    };

    @Test
    public void testConverterContextDoesntImplementHasExpressionNumberKind() {
        this.checkNotEquals(
            true,
            CONVERTER_CONTEXT instanceof HasExpressionNumberKind
        );
    }

    @Test
    public void testDecimalNumberContextDoesntImplementHasExpressionNumberKind() {
        this.checkNotEquals(
            true,
            DECIMAL_NUMBER_CONTEXT instanceof HasExpressionNumberKind
        );
    }

    @Test
    public void testEnvironmentContextDoesntImplementHasExpressionNumberKind() {
        this.checkNotEquals(
            true,
            ENVIRONMENT_CONTEXT instanceof HasExpressionNumberKind
        );
    }

    // with..............................................................................................................

    @Test
    public void testWithNullExpressionNumberKindFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionEvaluationContextBasic.with(
                null,
                EVALUATOR,
                FUNCTIONS,
                EXCEPTION_HANDLER,
                REFERENCES,
                ExpressionEvaluationContexts.referenceNotFound(),
                CASE_SENSITIVITY,
                CONVERTER_CONTEXT,
                ENVIRONMENT_CONTEXT,
                LOCALE_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullFunctionsFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionEvaluationContextBasic.with(
                EXPRESSION_NUMBER_KIND,
                EVALUATOR,
                null,
                EXCEPTION_HANDLER,
                REFERENCES,
                ExpressionEvaluationContexts.referenceNotFound(),
                CASE_SENSITIVITY,
                CONVERTER_CONTEXT,
                ENVIRONMENT_CONTEXT,
                LOCALE_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullExceptionHandlerFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionEvaluationContextBasic.with(
                EXPRESSION_NUMBER_KIND,
                EVALUATOR,
                FUNCTIONS,
                null,
                REFERENCES,
                ExpressionEvaluationContexts.referenceNotFound(),
                CASE_SENSITIVITY,
                CONVERTER_CONTEXT,
                ENVIRONMENT_CONTEXT,
                LOCALE_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullReferencesFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionEvaluationContextBasic.with(
                EXPRESSION_NUMBER_KIND,
                EVALUATOR,
                FUNCTIONS,
                EXCEPTION_HANDLER,
                null,
                ExpressionEvaluationContexts.referenceNotFound(),
                CASE_SENSITIVITY,
                CONVERTER_CONTEXT,
                ENVIRONMENT_CONTEXT,
                LOCALE_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullReferenceNotFoundFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionEvaluationContextBasic.with(
                EXPRESSION_NUMBER_KIND,
                EVALUATOR,
                FUNCTIONS,
                EXCEPTION_HANDLER,
                REFERENCES,
                null,
                CASE_SENSITIVITY,
                CONVERTER_CONTEXT,
                ENVIRONMENT_CONTEXT,
                LOCALE_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullCaseSensitivityFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionEvaluationContextBasic.with(
                EXPRESSION_NUMBER_KIND,
                EVALUATOR,
                FUNCTIONS,
                EXCEPTION_HANDLER,
                REFERENCES,
                ExpressionEvaluationContexts.referenceNotFound(),
                null,
                CONVERTER_CONTEXT,
                ENVIRONMENT_CONTEXT,
                LOCALE_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullConverterContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionEvaluationContextBasic.with(
                EXPRESSION_NUMBER_KIND,
                EVALUATOR,
                FUNCTIONS,
                EXCEPTION_HANDLER,
                REFERENCES,
                ExpressionEvaluationContexts.referenceNotFound(),
                CASE_SENSITIVITY,
                null,
                ENVIRONMENT_CONTEXT,
                LOCALE_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullEnvironmentContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionEvaluationContextBasic.with(
                EXPRESSION_NUMBER_KIND,
                EVALUATOR,
                FUNCTIONS,
                EXCEPTION_HANDLER,
                REFERENCES,
                ExpressionEvaluationContexts.referenceNotFound(),
                CASE_SENSITIVITY,
                CONVERTER_CONTEXT,
                null,
                LOCALE_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullLocaleContextContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionEvaluationContextBasic.with(
                EXPRESSION_NUMBER_KIND,
                EVALUATOR,
                FUNCTIONS,
                EXCEPTION_HANDLER,
                REFERENCES,
                ExpressionEvaluationContexts.referenceNotFound(),
                CASE_SENSITIVITY,
                CONVERTER_CONTEXT,
                ENVIRONMENT_CONTEXT,
                null
            )
        );
    }

    // currency......................................................................................................

    @Test
    public void testCurrency() {
        this.currencyAndCheck(
            this.createContext(),
            ENVIRONMENT_CONTEXT.currency()
        );
    }
    
    // indentation......................................................................................................

    @Test
    public void testIndentation() {
        this.checkNotEquals(
            ENVIRONMENT_CONTEXT.indentation(),
            CONVERTER_CONTEXT.indentation()
        );

        this.indentationAndCheck(
            this.createContext(),
            ENVIRONMENT_CONTEXT.indentation()
        );
    }
    
    // lineEnding.......................................................................................................

    @Test
    public void testLineEnding() {
        this.checkNotEquals(
            ENVIRONMENT_CONTEXT.lineEnding(),
            CONVERTER_CONTEXT.lineEnding()
        );

        this.lineEndingAndCheck(
            this.createContext(),
            ENVIRONMENT_CONTEXT.lineEnding()
        );
    }

    // evaluateFunction.................................................................................................

    @Test
    public void testEvaluateFunction() {
        final ExpressionFunction<Object, ExpressionEvaluationContextBasic> function = new FakeExpressionFunction<>() {

            @Override
            public Optional<ExpressionFunctionName> name() {
                return ExpressionFunction.ANONYMOUS_NAME;
            }

            @Override
            public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                return ExpressionFunctionParameter.EMPTY;
            }

            @Override
            public Object apply(final List<Object> objects,
                                final ExpressionEvaluationContextBasic context) {
                return FUNCTION_VALUE;
            }
        };

        this.evaluateFunctionAndCheck(
            function,
            FUNCTION_PARAMETERS,
            FUNCTION_VALUE
        );
    }

    @Test
    public void testEvaluateFunctionThrownHandled() {
        final String error = "**ERROR**";

        final ExpressionFunction<String, ExpressionEvaluationContextBasic> function = new FakeExpressionFunction<>() {

            @Override
            public Optional<ExpressionFunctionName> name() {
                return ExpressionFunction.ANONYMOUS_NAME;
            }

            @Override
            public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                return ExpressionFunctionParameter.EMPTY;
            }

            @Override
            public String apply(final List<Object> objects,
                                final ExpressionEvaluationContextBasic context) {
                throw new RuntimeException(error);
            }
        };

        this.evaluateFunctionAndCheck(
            this.createContext(
                (n) -> {
                    throw new RuntimeException();
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
    public void testEvaluateExpressionConversionFails() {
        final ExpressionNumberKind kind = ExpressionNumberKind.DOUBLE;

        this.evaluateExpressionAndCheck(
            ExpressionEvaluationContextBasic.with(
                kind,
                EVALUATOR,
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
                        return this.failConversion(
                            value,
                            target
                        );
                    }
                },
                ENVIRONMENT_CONTEXT,
                LOCALE_CONTEXT
            ),
            Expression.divide(
                Expression.value(kind.one()),
                Expression.value(kind.zero())
            ),
            "@@@Failed to convert 1 (walkingkooka.tree.expression.ExpressionNumberDouble) to walkingkooka.tree.expression.ExpressionNumber"
        );
    }

    @Test
    public void testEvaluateExpressionFunctionConversionFails() {
        final ExpressionFunctionName functionName = ExpressionFunctionName.with("HelloFunction");

        final ConverterException thrown = assertThrows(
            ConverterException.class,
            () -> ExpressionEvaluationContextBasic.with(
                ExpressionNumberKind.BIG_DECIMAL,
                EVALUATOR,
                (n) -> new FakeExpressionFunction<>() {
                    @Override
                    public Optional<ExpressionFunctionName> name() {
                        return Optional.of(functionName);
                    }

                    @Override
                    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                        return Lists.of(
                            ExpressionFunctionParameter.NUMBER.setKinds(
                                ExpressionFunctionParameterKind.CONVERT_EVALUATE
                            )
                        );
                    }

                    @Override
                    public Object apply(final List<Object> values,
                                        final ExpressionEvaluationContext context) {
                        ExpressionFunctionParameter.NUMBER.getOrFail(values, 0);
                        throw new UnsupportedOperationException();
                    }
                },
                (r) -> {
                    throw r;
                },
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
                        return this.failConversion(
                            value,
                            target
                        );
                    }
                },
                ENVIRONMENT_CONTEXT,
                LOCALE_CONTEXT
            ).evaluateExpression(
                Expression.call(
                    Expression.namedFunction(functionName),
                    Lists.of(
                        Expression.value("String1")
                    )
                )
            )
        );

        this.getMessageAndCheck(
            thrown,
            "HelloFunction: number: Failed to convert \"String1\" (java.lang.String) to walkingkooka.tree.expression.ExpressionNumber"
        );
    }

    @Test
    public void testEvaluateExpressionThrowsExceptionTranslated() {
        final ExpressionNumberKind kind = ExpressionNumberKind.DOUBLE;

        this.evaluateExpressionAndCheck(
            ExpressionEvaluationContextBasic.with(
                kind,
                EVALUATOR,
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
                ENVIRONMENT_CONTEXT,
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

    // evaluateFunction.................................................................................................

    @Test
    public void testEvaluateFunctionConverterFails() {
        final ExpressionNumberKind kind = ExpressionNumberKind.DOUBLE;

        final ExpressionFunction<String, ExpressionEvaluationContextBasic> function = new FakeExpressionFunction<>() {

            @Override
            public Optional<ExpressionFunctionName> name() {
                return ExpressionFunction.ANONYMOUS_NAME;
            }

            @Override
            public String apply(final List<Object> objects,
                                final ExpressionEvaluationContextBasic context) {
                throw new RuntimeException("Thrown123");
            }

            @Override
            public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                return ExpressionFunctionParameter.EMPTY;
            }
        };

        this.evaluateFunctionAndCheck(
            ExpressionEvaluationContextBasic.with(
                kind,
                EVALUATOR,
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
                        return this.failConversion(
                            value,
                            target
                        );
                    }
                },
                ENVIRONMENT_CONTEXT,
                LOCALE_CONTEXT
            ),
            function,
            Lists.of(999),
            "@@@Thrown123"
        );
    }

    @Test
    public void testEvaluateFunctionThrowsExceptionTranslated() {
        final ExpressionNumberKind kind = ExpressionNumberKind.DOUBLE;

        final ExpressionFunction<String, ExpressionEvaluationContextBasic> function = new FakeExpressionFunction<>() {

            @Override
            public Optional<ExpressionFunctionName> name() {
                return ExpressionFunction.ANONYMOUS_NAME;
            }

            @Override
            public String apply(final List<Object> objects,
                                final ExpressionEvaluationContextBasic context) {
                throw new RuntimeException("Thrown123");
            }

            @Override
            public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                return ExpressionFunctionParameter.EMPTY;
            }
        };

        this.evaluateFunctionAndCheck(
            ExpressionEvaluationContextBasic.with(
                kind,
                EVALUATOR,
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
                ENVIRONMENT_CONTEXT,
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
            FUNCTION_NAME,
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
        this.getMessageAndCheck(
            thrown,
            REFERENCE_NOT_FOUND_MESSAGE
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

    // log..............................................................................................................

    private final static String MESSAGE1 = "Message111";
    private final static String MESSAGE2 = "Message222";
    private final static String MESSAGE3 = "Message333";
    private final static String MESSAGE4 = "Message444";

    @Test
    public void testLogDisabled() {
        final ExpressionEvaluationContextBasic context = this.createContext(
            CanLogs.fake()
        );
        context.setLoggingLevel(LoggingLevel.NONE);
        context.log(
            LoggingLevel.DEBUG,
            MESSAGE1
        );
    }

    @Test
    public void testDebugDisabled() {
        final ExpressionEvaluationContextBasic context = this.createContext(
            CanLogs.fake()
        );
        context.setLoggingLevel(LoggingLevel.NONE);
        context.debug(
            MESSAGE1
        );
    }

    @Test
    public void testInfoDisabled() {
        final ExpressionEvaluationContextBasic context = this.createContext(
            CanLogs.fake()
        );
        context.setLoggingLevel(LoggingLevel.NONE);
        context.info(
            MESSAGE1
        );
    }

    @Test
    public void testWarnDisabled() {
        final ExpressionEvaluationContextBasic context = this.createContext(
            CanLogs.fake()
        );
        context.setLoggingLevel(LoggingLevel.NONE);
        context.warn(
            MESSAGE1
        );
    }

    @Test
    public void testErrorDisabled() {
        final ExpressionEvaluationContextBasic context = this.createContext(
            CanLogs.fake()
        );
        context.setLoggingLevel(LoggingLevel.NONE);
        context.error(
            MESSAGE1
        );
    }

    @Test
    public void testDebugEnabled() {
        final StringBuilder b = new StringBuilder();

        final ExpressionEvaluationContextBasic context = this.createContext(b);
        context.setLoggingLevel(LoggingLevel.DEBUG);
        context.debug(MESSAGE1);

        this.checkEquals(
            "DEBUG " + MESSAGE1 + LINE_ENDING,
            b.toString()
        );
    }

    @Test
    public void testInfoEnabled() {
        final StringBuilder b = new StringBuilder();

        final ExpressionEvaluationContextBasic context = this.createContext(b);
        context.setLoggingLevel(LoggingLevel.INFO);
        context.info(MESSAGE1);

        this.checkEquals(
            "INFO " + MESSAGE1 + LINE_ENDING,
            b.toString()
        );
    }

    @Test
    public void testWarnEnabled() {
        final StringBuilder b = new StringBuilder();

        final ExpressionEvaluationContextBasic context = this.createContext(b);
        context.setLoggingLevel(LoggingLevel.WARN);
        context.warn(MESSAGE1);

        this.checkEquals(
            "WARN " + MESSAGE1 + LINE_ENDING,
            b.toString()
        );
    }

    @Test
    public void testErrorEnabled() {
        final StringBuilder b = new StringBuilder();

        final ExpressionEvaluationContextBasic context = this.createContext(b);
        context.setLoggingLevel(LoggingLevel.ERROR);
        context.error(MESSAGE1);

        this.checkEquals(
            "ERROR " + MESSAGE1 + LINE_ENDING,
            b.toString()
        );
    }

    @Test
    public void testLogEnabled() {
        final StringBuilder b = new StringBuilder();

        final ExpressionEvaluationContextBasic context = this.createContext(b);
        context.setLoggingLevel(LoggingLevel.INFO);
        context.log(
            LoggingLevel.DEBUG,
            MESSAGE1
        );
        context.log(
            LoggingLevel.INFO,
            MESSAGE2
        );
        context.log(
            LoggingLevel.WARN,
            MESSAGE3
        );
        context.log(
            LoggingLevel.ERROR,
            MESSAGE4
        );

        this.checkEquals(
            "INFO " + MESSAGE2 + LINE_ENDING +
                "WARN " + MESSAGE3 + LINE_ENDING +
                "ERROR " + MESSAGE4 + LINE_ENDING,
            b.toString()
        );
    }

    @Test
    public void testLogLevelChanged() {
        final StringBuilder b = new StringBuilder();

        final ExpressionEvaluationContextBasic context = this.createContext(b);
        context.setLoggingLevel(LoggingLevel.ERROR);

        context.debug(MESSAGE1);

        context.setLoggingLevel(LoggingLevel.INFO);
        context.debug(MESSAGE2);
        context.warn(MESSAGE3);

        context.setLoggingLevel(LoggingLevel.NONE);
        context.log(
            LoggingLevel.ERROR,
            MESSAGE4
        );

        this.checkEquals(
            "WARN " + MESSAGE3 + LINE_ENDING,
            b.toString()
        );
    }

    private ExpressionEvaluationContextBasic createContext(final StringBuilder b) {
        return this.createContext(
            CanLogs.printer(
                Printers.stringBuilder(
                    b,
                    LINE_ENDING // yes doesnt sync with EnvironmentContext#lineEnding
                )
            )
        );
    }

    private ExpressionEvaluationContextBasic createContext(final CanLog canLog) {
        return ExpressionEvaluationContextBasic.with(
            EXPRESSION_NUMBER_KIND,
            EVALUATOR,
            (n) -> {
                throw new UnsupportedOperationException();
            },
            EXCEPTION_HANDLER,
            REFERENCES,
            REFERENCE_NOT_FOUND,
            CASE_SENSITIVITY,
            CONVERTER_CONTEXT,
            ENVIRONMENT_CONTEXT.environment()
                .setCanLog(canLog)
                .environmentContext()
                .cloneEnvironment(),
            LOCALE_CONTEXT
        );
    }

    // stringEqualsCaseSensitivity......................................................................................

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
        this.toStringAndCheck(
            ExpressionEvaluationContextBasic.with(
                EXPRESSION_NUMBER_KIND,
                EVALUATOR,
                FUNCTIONS,
                EXCEPTION_HANDLER,
                REFERENCES,
                REFERENCE_NOT_FOUND,
                CASE_SENSITIVITY,
                CONVERTER_CONTEXT,
                ENVIRONMENT_CONTEXT,
                LOCALE_CONTEXT
            ),
            "expressionNumberKind=BIG_DECIMAL evaluator=EVALUATOR functions=FUNCTIONS exceptionHandler=EXCEPTION_HANDLER references=REFERENCES referenceNotFound=REFERENCE_NOT_FOUND stringEqualityCaseSensitivity=SENSITIVE converterContext=binaryTextContext=charset=\"UTF-8\" indentation=\"    \" lineEnding=\"\\r\\n\" dateTimeContext=symbols=ampms=\"am\", \"pm\" monthNames=\"January\", \"February\", \"March\", \"April\", \"May\", \"June\", \"July\", \"August\", \"September\", \"October\", \"November\", \"December\" monthNameAbbreviations=\"Jan.\", \"Feb.\", \"Mar.\", \"Apr.\", \"May\", \"Jun.\", \"Jul.\", \"Aug.\", \"Sep.\", \"Oct.\", \"Nov.\", \"Dec.\" weekDayNames=\"Sunday\", \"Monday\", \"Tuesday\", \"Wednesday\", \"Thursday\", \"Friday\", \"Saturday\" weekDayNameAbbreviations=\"Sun.\", \"Mon.\", \"Tue.\", \"Wed.\", \"Thu.\", \"Fri.\", \"Sat.\" locale=\"en-AU\" twoDigitYear=50 decimalNumberContext=locale=en_US \"mathContext\" precision=7 roundingMode=HALF_EVEN \"decimalNumberSymbols\" negativeSign='-' positiveSign='+' zeroDigit='0' currencySymbol=\"$\" decimalSeparator='.' exponentSymbol=\"E\" "
        );
    }

    // helpers.........................................................................................................

    @Override
    public ExpressionEvaluationContextBasic createContext() {
        return this.createContext(true);
    }

    private ExpressionEvaluationContextBasic createContext(final boolean pure) {
        return this.createContext(
            pure,
            CASE_SENSITIVITY
        );
    }

    private ExpressionEvaluationContextBasic createContext(final CaseSensitivity caseSensitivity) {
        return this.createContext(
            true,
            caseSensitivity
        );
    }

    private ExpressionEvaluationContextBasic createContext(final boolean pure,
                                                           final CaseSensitivity caseSensitivity) {
        return ExpressionEvaluationContextBasic.with(
            EXPRESSION_NUMBER_KIND,
            EVALUATOR,
            functions(pure),
            EXCEPTION_HANDLER,
            REFERENCES,
            REFERENCE_NOT_FOUND,
            caseSensitivity,
            CONVERTER_CONTEXT,
            ENVIRONMENT_CONTEXT.cloneEnvironment(),
            LOCALE_CONTEXT
        );
    }

    private ExpressionEvaluationContextBasic createContext(final Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions,
                                                           final Function<RuntimeException, Object> exceptionHandler) {
        return ExpressionEvaluationContextBasic.with(
            EXPRESSION_NUMBER_KIND,
            EVALUATOR,
            functions,
            exceptionHandler,
            REFERENCES,
            (r) -> new ExpressionEvaluationReferenceException(REFERENCE_NOT_FOUND_MESSAGE, r),
            CASE_SENSITIVITY,
            CONVERTER_CONTEXT,
            ENVIRONMENT_CONTEXT.cloneEnvironment(),
            LOCALE_CONTEXT
        );
    }

    private ExpressionEvaluationContextBasic createContext(final Function<ExpressionReference, Optional<Optional<Object>>> references) {
        return ExpressionEvaluationContextBasic.with(
            EXPRESSION_NUMBER_KIND,
            EVALUATOR,
            (n) -> {
                throw new UnsupportedOperationException();
            },
            EXCEPTION_HANDLER,
            references,
            REFERENCE_NOT_FOUND,
            CASE_SENSITIVITY,
            CONVERTER_CONTEXT,
            ENVIRONMENT_CONTEXT.cloneEnvironment(),
            LOCALE_CONTEXT
        );
    }

    // DecimalNumberContextDelegator....................................................................................

    @Override
    public int decimalNumberDigitCount() {
        return DECIMAL_NUMBER_CONTEXT.decimalNumberDigitCount();
    }

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return DECIMAL_NUMBER_CONTEXT;
    }

    @Override
    public MathContext mathContext() {
        return DECIMAL_NUMBER_CONTEXT.mathContext();
    }

    // HasEnvironmentContext............................................................................................

    @Test
    @Override
    public void testEnvironmentContext() {
        final EnvironmentContext context = ENVIRONMENT_CONTEXT;

        this.environmentContextAndCheck(
            ExpressionEvaluationContextBasic.with(
                EXPRESSION_NUMBER_KIND,
                EVALUATOR,
                (n) -> {
                    throw new UnsupportedOperationException();
                },
                EXCEPTION_HANDLER,
                REFERENCES,
                REFERENCE_NOT_FOUND,
                CASE_SENSITIVITY,
                CONVERTER_CONTEXT,
                context,
                LOCALE_CONTEXT
            ),
            context
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ExpressionEvaluationContextBasic> type() {
        return Cast.to(ExpressionEvaluationContextBasic.class);
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
