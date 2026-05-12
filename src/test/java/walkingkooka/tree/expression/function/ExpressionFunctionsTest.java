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

package walkingkooka.tree.expression.function;

import org.junit.jupiter.api.Test;
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.convert.ShortCircuitingConverter;
import walkingkooka.currency.CurrencyCode;
import walkingkooka.currency.CurrencyExchangeRaters;
import walkingkooka.currency.FakeCurrencyLocaleContext;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.environment.EnvironmentContexts;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.locale.LocaleLanguageTag;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;

import java.lang.reflect.Method;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionsTest implements PublicStaticHelperTesting<ExpressionFunctions> {

    private final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.BIG_DECIMAL;

    @Test
    public void testLookupNullFunctionsFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionFunctions.lookup(null, CaseSensitivity.SENSITIVE)
        );
    }

    @Test
    public void testLookupNullCaseSensitiveFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionFunctions.lookup(
                Sets.empty(),
                null
            )
        );
    }

    @Test
    public void testLookupIncludesAnonymousFunctionFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () ->
                ExpressionFunctions.lookup(
                    Sets.of(
                        functionWithName(null)
                    ),
                    CaseSensitivity.SENSITIVE
                )
        );
        this.checkEquals(
            "Anonymous functions are not supported",
            thrown.getMessage(),
            "message"
        );
    }

    @Test
    public void testLookupNullDuplicateFunctionNameFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> ExpressionFunctions.lookup(
                Sets.of(
                    functionWithName("duplicate123"),
                    functionWithName("duplicate123")
                ),
                CaseSensitivity.SENSITIVE
            )
        );
        this.checkEquals(
            "Duplicate f" +
                "unction \"duplicate123\"",
            thrown.getMessage(),
            "message"
        );
    }

    @Test
    public void testLookupNullDuplicateFunctionNameCaseInsensitiveFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> ExpressionFunctions.lookup(
                Sets.of(
                    functionWithName("duplicate123"),
                    functionWithName("DUPLICATE123")
                ),
                CaseSensitivity.INSENSITIVE
            )
        );
        this.checkEquals(
            "Duplicate function \"DUPLICATE123\"",
            thrown.getMessage(),
            "message"
        );
    }

    @Test
    public void testLookupCaseSensitive() {
        final ExpressionFunction<Void, ExpressionEvaluationContext> function = functionWithName("test-1");

        final Function<ExpressionFunctionName, Optional<ExpressionFunction<?, ExpressionEvaluationContext>>> lookup =
            ExpressionFunctions.lookup(
                Sets.of(
                    function,
                    functionWithName("test-functionWithName-2")
                ),
                CaseSensitivity.SENSITIVE
            );
        this.checkEquals(
            Optional.of(
                function
            ),
            lookup.apply(
                ExpressionFunctionName.with("test-1")
            )
        );
    }

    @Test
    public void testLookupCaseSensitiveWrongCaseNotFound() {
        final ExpressionFunction<Void, ExpressionEvaluationContext> function = functionWithName("test-1");

        final Function<ExpressionFunctionName, Optional<ExpressionFunction<?, ExpressionEvaluationContext>>> lookup =
            ExpressionFunctions.lookup(
                Sets.of(
                    function,
                    functionWithName("test-functionWithName-2")
                ),
                CaseSensitivity.SENSITIVE
            );
        this.checkEquals(
            Optional.empty(),
            lookup.apply(
                ExpressionFunctionName.with("TEST-1")
            )
        );
    }

    @Test
    public void testLookupCaseInsensitiveWrongCase() {
        final ExpressionFunction<Void, ExpressionEvaluationContext> function = functionWithName("test-functionWithName-1");

        final Function<ExpressionFunctionName, Optional<ExpressionFunction<?, ExpressionEvaluationContext>>> lookup =
            ExpressionFunctions.lookup(
                Sets.of(
                    function,
                    functionWithName("test-functionWithName-2")
                ),
                CaseSensitivity.INSENSITIVE
            );
        this.checkEquals(
            Optional.of(
                function
            ),
            lookup.apply(
                ExpressionFunctionName.with("TEST-functionWithName-1")
            )
        );
    }

    @Test
    public void testLookupCaseInsensitiveNotFound() {
        final ExpressionFunction<Void, ExpressionEvaluationContext> function = functionWithName("test-functionWithName-1");

        final Function<ExpressionFunctionName, Optional<ExpressionFunction<?, ExpressionEvaluationContext>>> lookup =
            ExpressionFunctions.lookup(
                Sets.of(
                    function,
                    functionWithName("test-functionWithName-2")
                ),
                CaseSensitivity.INSENSITIVE
            );
        this.checkEquals(
            Optional.empty(),
            lookup.apply(
                ExpressionFunctionName.with("TEST-functionWithName-unknown")
            )
        );
    }

    private static ExpressionFunction<Void, ExpressionEvaluationContext> functionWithName(final String name) {
        return new FakeExpressionFunction<>() {
            @Override
            public Optional<ExpressionFunctionName> name() {
                return Optional.ofNullable(name)
                    .map(ExpressionFunctionName::with);
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    @Test
    public void testVisit() {
        final Set<ExpressionFunctionName> names = SortedSets.tree();

        ExpressionFunctions.visit(
            (e) -> names.add(
                e.name()
                    .orElseThrow(() -> new IllegalStateException("ExpressionFunction must have a name"))
            )
        );

        final Set<String> methods = Arrays.stream(ExpressionFunctions.class.getDeclaredMethods())
            .filter(m -> m.getReturnType() == ExpressionFunction.class)
            .filter(m -> m.getParameterCount() == 0)
            .filter(m -> !(m.getName().equals("fake")))
            .map(Method::getName)
            .collect(Collectors.toCollection(SortedSets::tree));

        this.checkEquals(
            methods.size(),
            names.size(),
            methods::toString
        );
        this.checkEquals(
            true,
            names.contains(
                ExpressionFunctions.typeName()
                    .name()
                    .get()
            )
        );
    }

    @Test
    public void testEvaluateCurrencyCode() {
        final String currencyCode = "AUD";

        this.evaluateAndCheck(
            "currencyCode",
            Lists.of(currencyCode),
            CurrencyCode.parse(currencyCode)
        );
    }

    @Test
    public void testEvaluateLocale() {
        final String locale = "en-AU";

        this.evaluateAndCheck(
            "locale",
            Lists.of(locale),
            Locale.forLanguageTag(locale)
        );
    }

    @Test
    public void testEvaluateLocaleLanguageTag() {
        final String localeLanguageTag = "en-AU";

        this.evaluateAndCheck(
            "localeLanguageTag",
            Lists.of(localeLanguageTag),
            LocaleLanguageTag.parse(localeLanguageTag)
        );
    }

    @Test
    public void testEvaluateExpression() {
        this.evaluateAndCheck(
            "expression",
            Lists.empty(),
            123
        );
    }

    private final static String EXPRESSION = "1+2";

    @Test
    public void testEvaluateEval() {
        this.evaluateAndCheck(
            "eval",
            Lists.of(EXPRESSION),
            EXPRESSION_NUMBER_KIND.create(3)
        );
    }

    @Test
    public void testEvaluateNull() {
        this.evaluateAndCheck(
            "null",
            Lists.of(),
            null
        );
    }

    @Test
    public void testEvaluateTypeName() {
        this.evaluateAndCheck(
            "typeName",
            Lists.of(this),
            this.getClass()
                .getName()
        );
    }

    private void evaluateAndCheck(final String functionName,
                                  final List<Object> parameters,
                                  final Object expected) {
        this.checkEquals(
            expected,
            Expression.call(
                Expression.namedFunction(
                    ExpressionFunctionName.with(functionName)
                ),
                parameters.stream()
                    .map(Expression::value)
                    .collect(Collectors.toList())
            ).toValue(
                ExpressionEvaluationContexts.basic(
                    ExpressionNumberKind.BIG_DECIMAL,
                    (e, c) -> {
                        throw new UnsupportedOperationException();
                    },
                    (name) -> {
                        switch (name.value()) {
                            case "currencyCode":
                                return ExpressionFunctions.currencyCode();
                            case "eval":
                                return ExpressionFunctions.eval();
                            case "locale":
                                return ExpressionFunctions.locale();
                            case "localeLanguageTag":
                                return ExpressionFunctions.localeLanguageTag();
                            case "expression":
                                return ExpressionFunctions.expression(
                                    Expression.value(123)
                                );
                            case "null":
                                return ExpressionFunctions.nullFunction();
                            case "typeName":
                                return ExpressionFunctions.typeName();
                            default:
                                throw new UnknownExpressionFunctionException(name);
                        }
                    }, // name -> function
                    (final RuntimeException cause) -> {
                        throw cause;
                    },
                    (ExpressionReference reference) -> {
                        throw new UnsupportedOperationException();
                    },
                    (ExpressionReference reference) -> {
                        throw new UnsupportedOperationException();
                    },
                    CaseSensitivity.SENSITIVE,
                    ConverterContexts.basic(
                        false, // canNumbersHaveGroupSeparator
                        Converters.EXCEL_1900_DATE_SYSTEM_OFFSET, // dateTimeOffset
                        Indentation.SPACES2,
                        LineEnding.NL,
                        ',', // valueSeparator
                        Converters.collection(
                            Lists.of(
                                Converters.characterOrCharSequenceOrHasTextOrStringToCharacterOrCharSequenceOrString(),
                                Converters.simple(),
                                Converters.textToCurrencyCode(),
                                new ShortCircuitingConverter<>() {
                                    @Override
                                    public boolean canConvert(final Object value,
                                                              final Class<?> type,
                                                              final ConverterContext context) {
                                        return EXPRESSION.equals(value) && Expression.class == type;
                                    }

                                    @Override
                                    public <T> Either<T, String> doConvert(final Object value,
                                                                           final Class<T> type,
                                                                           final ConverterContext converterContext) {
                                        return this.successfulConversion(
                                            Expression.add(
                                                Expression.value(
                                                    EXPRESSION_NUMBER_KIND.one()
                                                ),
                                                Expression.value(
                                                    EXPRESSION_NUMBER_KIND.create(2.0)
                                                )
                                            ),
                                            type
                                        );
                                    }
                                },
                                Converters.textToLocale(),
                                Converters.textToLocaleLanguageTag()
                            )
                        ),
                        CurrencyExchangeRaters.fake(),
                        new FakeCurrencyLocaleContext() {
                            @Override
                            public Optional<Locale> localeForLanguageTag(final LocaleLanguageTag languageTag) {
                                return LocaleContexts.jre(
                                    Locale.forLanguageTag("en-AU")
                                ).localeForLanguageTag(languageTag);
                            }
                        },
                        DateTimeContexts.fake(),
                        DecimalNumberContexts.american(MathContext.DECIMAL32)
                    ),
                    EnvironmentContexts.fake(),
                    LocaleContexts.fake()
                )
            )
        );
    }

    @Test
    public void testPublicStaticMethodsWithoutMathContextParameter() {
        this.publicStaticMethodParametersTypeCheck(MathContext.class);
    }

    @Override
    public Class<ExpressionFunctions> type() {
        return ExpressionFunctions.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
