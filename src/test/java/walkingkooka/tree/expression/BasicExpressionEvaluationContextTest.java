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
import walkingkooka.convert.ConverterException;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverterContext;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.datetime.HasNow;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentContexts;
import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.predicate.Predicates;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.tree.expression.function.FakeExpressionFunction;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.math.MathContext;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicExpressionEvaluationContextTest implements ClassTesting2<BasicExpressionEvaluationContext>,
    ExpressionEvaluationContextTesting<BasicExpressionEvaluationContext>,
    ToStringTesting<BasicExpressionEvaluationContext>,
    DecimalNumberContextDelegator {

    private final static ExpressionNumberKind KIND = ExpressionNumberKind.DEFAULT;

    private final static BiFunction<String, ExpressionEvaluationContext, Object> EVALUATOR = (e, c) -> {
        Objects.requireNonNull(e, "expression");
        return e + e;
    };

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

    private final static Currency CURRENCY = Currency.getInstance("AUD");

    private final static HasNow HAS_NOW = () -> LocalDateTime.MIN;

    private final static Locale LOCALE = Locale.forLanguageTag("en-AU");

    private final static EnvironmentContext ENVIRONMENT_CONTEXT = EnvironmentContexts.readOnly(
        Predicates.always(), // all values are read only
        EnvironmentContexts.map(
            EnvironmentContexts.empty(
                CURRENCY,
                INDENTATION,
                LineEnding.NL,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            )
        )
    );

    private final static LocaleContext LOCALE_CONTEXT = LocaleContexts.jre(
        ENVIRONMENT_CONTEXT.locale()
    );

    private final static DecimalNumberContext DECIMAL_NUMBER_CONTEXT = DecimalNumberContexts.american(MathContext.DECIMAL32);

    private final static ConverterContext CONVERTER_CONTEXT = ConverterContexts.basic(
        (l) -> {
            throw new UnsupportedOperationException();
        }, // canDateTimeSymbolsForLocale
        (l) -> {
            throw new UnsupportedOperationException();
        }, // canDecimalNumberSymbolsForLocale
        false, // canNumbersHaveGroupSeparator
        Converters.JAVA_EPOCH_OFFSET, // dateOffset
        Indentation.SPACES4,
        LineEnding.CRNL,
        ',', // valueSeparator
        Converters.collection(
            Lists.of(
                Converters.numberToNumber(),
                Converters.simple()
            )
        ),
        DateTimeContexts.basic(
            DateTimeSymbols.fromDateFormatSymbols(
                new DateFormatSymbols(LOCALE)
            ),
            LOCALE,
            1950, // defaultYear
            50, // twoDigitYear
            HAS_NOW
        ),
        DECIMAL_NUMBER_CONTEXT
    );

    private final static Function<ExpressionReference, Optional<Optional<Object>>> REFERENCES = (r -> {
        Objects.requireNonNull(r, "references");
        if (false == REFERENCE.equals(r)) {
            throw new IllegalArgumentException("Invalid reference " + r);
        }

        return Optional.of(
            Optional.of(REFERENCE_VALUE)
        );
    }
    );

    @Test
    public void testWithNullExpressionNumberKindFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionEvaluationContext.with(
                null,
                EVALUATOR,
                this.functions(),
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
            () -> BasicExpressionEvaluationContext.with(
                KIND,
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
            () -> BasicExpressionEvaluationContext.with(
                KIND,
                EVALUATOR,
                this.functions(),
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
            () -> BasicExpressionEvaluationContext.with(
                KIND,
                EVALUATOR,
                this.functions(),
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
            () -> BasicExpressionEvaluationContext.with(
                KIND,
                EVALUATOR,
                this.functions(),
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
            () -> BasicExpressionEvaluationContext.with(
                KIND,
                EVALUATOR,
                this.functions(),
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
            () -> BasicExpressionEvaluationContext.with(
                KIND,
                EVALUATOR,
                this.functions(),
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
            () -> BasicExpressionEvaluationContext.with(
                KIND,
                EVALUATOR,
                this.functions(),
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
            () -> BasicExpressionEvaluationContext.with(
                KIND,
                EVALUATOR,
                this.functions(),
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
        final Object value = this.functionValue();

        final ExpressionFunction<Object, BasicExpressionEvaluationContext> function = new FakeExpressionFunction<>() {

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
            public Optional<ExpressionFunctionName> name() {
                return ExpressionFunction.ANONYMOUS_NAME;
            }

            @Override
            public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                return ExpressionFunctionParameter.EMPTY;
            }

            @Override
            public String apply(final List<Object> objects,
                                final BasicExpressionEvaluationContext context) {
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
            BasicExpressionEvaluationContext.with(
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
            () -> BasicExpressionEvaluationContext.with(
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

        this.checkEquals(
            "HelloFunction: number: Failed to convert \"String1\" (java.lang.String) to walkingkooka.tree.expression.ExpressionNumber",
            thrown.getMessage()
        );
    }

    @Test
    public void testEvaluateExpressionThrowsExceptionTranslated() {
        final ExpressionNumberKind kind = ExpressionNumberKind.DOUBLE;

        this.evaluateExpressionAndCheck(
            BasicExpressionEvaluationContext.with(
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

    // namedFunction........................................................................................................

    @Test
    public void testEvaluateFunctionConverterFails() {
        final ExpressionNumberKind kind = ExpressionNumberKind.DOUBLE;

        final ExpressionFunction<String, BasicExpressionEvaluationContext> function = new FakeExpressionFunction<>() {

            @Override
            public Optional<ExpressionFunctionName> name() {
                return ExpressionFunction.ANONYMOUS_NAME;
            }

            @Override
            public String apply(final List<Object> objects,
                                final BasicExpressionEvaluationContext context) {
                throw new RuntimeException("Thrown123");
            }

            @Override
            public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                return ExpressionFunctionParameter.EMPTY;
            }
        };

        this.evaluateFunctionAndCheck(
            BasicExpressionEvaluationContext.with(
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

        final ExpressionFunction<String, BasicExpressionEvaluationContext> function = new FakeExpressionFunction<>() {

            @Override
            public Optional<ExpressionFunctionName> name() {
                return ExpressionFunction.ANONYMOUS_NAME;
            }

            @Override
            public String apply(final List<Object> objects,
                                final BasicExpressionEvaluationContext context) {
                throw new RuntimeException("Thrown123");
            }

            @Override
            public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                return ExpressionFunctionParameter.EMPTY;
            }
        };

        this.evaluateFunctionAndCheck(
            BasicExpressionEvaluationContext.with(
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
        final Function<ExpressionReference, Optional<Optional<Object>>> references = REFERENCES;
        final Function<ExpressionReference, ExpressionEvaluationException> referenceNotFound = ExpressionEvaluationContexts.referenceNotFound();

        this.toStringAndCheck(
            BasicExpressionEvaluationContext.with(
                KIND,
                EVALUATOR,
                functions,
                EXCEPTION_HANDLER,
                references,
                referenceNotFound,
                CASE_SENSITIVITY,
                CONVERTER_CONTEXT,
                ENVIRONMENT_CONTEXT,
                LOCALE_CONTEXT
            ),
            KIND +
                " " +
                EVALUATOR +
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
                CONVERTER_CONTEXT +
                " " +
                ENVIRONMENT_CONTEXT +
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
            EVALUATOR,
            this.functions(pure),
            EXCEPTION_HANDLER,
            REFERENCES,
            REFERENCE_NOT_FOUND,
            caseSensitivity,
            CONVERTER_CONTEXT,
            ENVIRONMENT_CONTEXT.cloneEnvironment(),
            LOCALE_CONTEXT
        );
    }

    private BasicExpressionEvaluationContext createContext(final Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions,
                                                           final Function<RuntimeException, Object> exceptionHandler) {
        return BasicExpressionEvaluationContext.with(
            KIND,
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

    private BasicExpressionEvaluationContext createContext(final Function<ExpressionReference, Optional<Optional<Object>>> references) {
        return BasicExpressionEvaluationContext.with(
            KIND,
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
