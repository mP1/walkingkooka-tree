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
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.cursor.parser.BigIntegerParserToken;
import walkingkooka.text.cursor.parser.InvalidCharacterExceptionFactory;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.FakeExpressionFunction;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.math.BigInteger;
import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public final class CycleDetectingExpressionEvaluationContextTest implements ClassTesting2<CycleDetectingExpressionEvaluationContext>,
    ExpressionEvaluationContextTesting<CycleDetectingExpressionEvaluationContext>,
    DecimalNumberContextDelegator {

    private final static String VALUE = "text123";

    private final static ExpressionReference A1 = reference("A1");
    private final static ExpressionReference B2 = reference("B2");
    private final static ExpressionReference C3 = reference("C3");

    private static ExpressionReference reference(final String label) {
        return new FakeExpressionReference() {
            @Override
            public String toString() {
                return label;
            }
        };
    }

    @Test
    public void testWithNullContextFails() {
        assertThrows(NullPointerException.class, () -> CycleDetectingExpressionEvaluationContext.with(null));
    }

    @Test
    public void testEvaluateExpressionString() {
        final String value = "abc123";
        this.evaluateExpressionAndCheck(
            Expression.value(value),
            value
        );
    }

    @Test
    public void testEvaluateFunction() {
        final ExpressionFunction<Object, CycleDetectingExpressionEvaluationContext> function = new FakeExpressionFunction<>() {
            @Override
            public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                return Lists.empty();
            }

            @Override
            public Object apply(final List<Object> parameters,
                                final CycleDetectingExpressionEvaluationContext context) {
                return VALUE;
            }
        };
        final List<Object> parameters = Lists.of("param-1", "param-2");

        final CycleDetectingExpressionEvaluationContext context = this.createContext(ExpressionEvaluationContexts.fake());

        this.evaluateFunctionAndCheck(
            context,
            function,
            parameters,
            VALUE
        );
    }

    @Test
    public void testIsPureFalse() {
        this.isPureAndCheck2(false);
    }

    @Test
    public void testIsPureTrue() {
        this.isPureAndCheck2(true);
    }

    private void isPureAndCheck2(final boolean pure) {
        final ExpressionFunctionName functionName = this.functionName();
        this.isPureAndCheck(
            this.createContext(pure),
            functionName,
            pure
        );
    }

    private ExpressionFunctionName functionName() {
        return ExpressionFunctionName.with("function123");
    }

    @Test
    public void testReference() {
        final String target = "Text123";

        this.referenceAndCheck(
            this.createContext(
                new FakeExpressionEvaluationContext() {

                    @Override
                    public Optional<Optional<Object>> reference(final ExpressionReference reference) {
                        assertSame(A1, reference, "reference");

                        return Optional.of(
                            Optional.of(target)
                        );
                    }
                }
            ),
            A1,
            target
        );
    }

    @Test
    public void testReferenceToSelfCycleFails() {
        // A1 -> target
        final ReferenceExpression target = Expression.reference(A1);

        final CycleDetectingExpressionEvaluationContext context = this.createContext(
            new FakeExpressionEvaluationContext() {

                @Override
                public Optional<Optional<Object>> reference(final ExpressionReference reference) {
                    if (A1 == reference) {
                        return Optional.of(
                            Optional.of(
                                target.value()
                            )
                        );
                    }
                    return this.unknownReference(reference);
                }

                private <T> T unknownReference(final ExpressionReference reference) {
                    fail("Unknown reference=" + reference);
                    return null;
                }


                @Override
                public Object handleException(final RuntimeException exception) {
                    throw exception;
                }
            });

        assertThrows(CycleDetectedExpressionEvaluationConversionException.class, () -> target.toValue(context));
    }

    @Test
    public void testReferenceWithCycleFails() {
        // B2 -> A1 -> target
        final CycleDetectingExpressionEvaluationContext context = this.createContext(
            new FakeExpressionEvaluationContext() {

                @Override
                public Optional<Optional<Object>> reference(final ExpressionReference reference) {
                    return Optional.of(
                        Optional.of(
                            this.reference0(reference)
                        )
                    );
                }

                private Object reference0(final ExpressionReference reference) {
                    if (B2 == reference) {
                        return B2;
                    }
                    if (A1 == reference) {
                        return A1;
                    }
                    return this.unknownReference(reference);
                }

                private <T> T unknownReference(final ExpressionReference reference) {
                    fail("Unknown reference=" + reference);
                    return null;
                }


                @Override
                public Object handleException(final RuntimeException exception) {
                    throw exception;
                }
            });
        assertThrows(CycleDetectedExpressionEvaluationConversionException.class, () -> Expression.reference(A1).toValue(context));
    }

    @Test
    public void testReferenceWithCycleFails2() {
        final CycleDetectingExpressionEvaluationContext context = this.createContext(
            new FakeExpressionEvaluationContext() {

                @Override
                public Optional<Optional<Object>> reference(final ExpressionReference reference) {
                    return Optional.of(
                        Optional.of(
                            this.reference0(reference)
                        )
                    );
                }

                private Object reference0(final ExpressionReference reference) {
                    if (B2 == reference || C3 == reference) {
                        return B2;
                    }
                    if (A1 == reference) {
                        return A1;
                    }
                    return this.unknownReference(reference);
                }

                private <T> T unknownReference(final ExpressionReference reference) {
                    fail("Unknown reference=" + reference);
                    return null;
                }


                @Override
                public Object handleException(final RuntimeException exception) {
                    throw exception;
                }
            });

        assertThrows(CycleDetectedExpressionEvaluationConversionException.class, () -> {
            Expression.reference(B2).toValue(context); // --> B2 --> A1 --> B2 cycle!!!
        });
    }

    @Test
    public void testReferenceAfterCycleDetected() {
        // B2 -> A1 -> expression
        final ExpressionReference a1 = reference("A1");
        final ExpressionReference b2 = reference("B2");

        final Expression b2Expression = Expression.reference(a1);
        final Expression expression = this.text();

        final CycleDetectingExpressionEvaluationContext context = this.createContext(
            new FakeExpressionEvaluationContext() {

                @Override
                public Optional<Optional<Object>> reference(final ExpressionReference reference) {
                    return Optional.of(
                        Optional.of(
                            this.reference0(reference)
                        )
                    );
                }

                private Object reference0(final ExpressionReference reference) {
                    if (b2 == reference) {
                        return b2;
                    }
                    if (a1 == reference) {
                        return a1;
                    }
                    return this.unknownReference(reference);
                }

                private <T> T unknownReference(final ExpressionReference reference) {
                    fail("Unknown reference=" + reference);
                    return null;
                }

                @Override
                public Object handleException(final RuntimeException exception) {
                    throw exception;
                }
            });

        assertThrows(
            CycleDetectedExpressionEvaluationConversionException.class,
            () -> b2Expression.toValue(context)
        );
        this.toValueAndCheck(expression, context, VALUE);
    }

    @Test
    @Override
    public void testMathContext() {
        final MathContext mathContext = MathContext.DECIMAL32;

        final CycleDetectingExpressionEvaluationContext context = this.createContext(
            new FakeExpressionEvaluationContext() {
                @Override
                public MathContext mathContext() {
                    return mathContext;
                }
            });
        assertSame(mathContext, context.mathContext());
    }

    @Test
    public void testConvert() {
        final CycleDetectingExpressionEvaluationContext context = this.createContext(
            new FakeExpressionEvaluationContext() {

                @Override
                public <T> Either<T, String> convert(final Object value, final Class<T> target) {
                    return Converters.parser(
                            BigInteger.class,
                            Parsers.bigInteger(10),
                            (c) -> ParserContexts.basic(
                                InvalidCharacterExceptionFactory.POSITION,
                                c,
                                c
                            ),
                            (t, c) -> t.cast(BigIntegerParserToken.class).value()
                        )
                        .convert(
                            value,
                            target,
                            ConverterContexts.basic(
                                Converters.JAVA_EPOCH_OFFSET, // dateOffset
                                Converters.characterOrCharSequenceOrHasTextOrStringToCharacterOrCharSequenceOrString(),
                                DateTimeContexts.fake(),
                                DecimalNumberContexts.american(MathContext.DECIMAL32)
                            )
                        );
                }
            }
        );
        this.checkEquals(
            Either.left(BigInteger.valueOf(123)),
            context.convert("123", BigInteger.class)
        );
    }

    @Override
    public void testEnterScopeGivesDifferentInstance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CycleDetectingExpressionEvaluationContext createContext() {
        return this.createContext(true);
    }

    private CycleDetectingExpressionEvaluationContext createContext(final boolean pure) {
        final DecimalNumberContext decimalNumberContext = this.decimalNumberContext();

        return this.createContext(
            new FakeExpressionEvaluationContext() {

                @Override
                public Object handleException(final RuntimeException exception) {
                    throw exception;
                }

                @Override
                public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name) {
                    Objects.requireNonNull(name, "name");
                    throw new UnknownExpressionFunctionException(name);
                }

                @Override
                public boolean isPure(final ExpressionFunctionName name) {
                    Objects.requireNonNull(name, "name");
                    return pure;
                }

                @Override
                public ExpressionEvaluationContext enterScope(final Function<ExpressionReference, Optional<Optional<Object>>> scoped) {
                    Objects.requireNonNull(scoped, "scoped");

                    throw new UnsupportedOperationException();
                }

                // DecimalNumberContext.................................................................................

                @Override
                public String currencySymbol() {
                    return decimalNumberContext.currencySymbol();
                }

                @Override
                public char decimalSeparator() {
                    return decimalNumberContext.decimalSeparator();
                }

                @Override
                public String exponentSymbol() {
                    return decimalNumberContext.exponentSymbol();
                }

                @Override
                public char groupSeparator() {
                    return decimalNumberContext.groupSeparator();
                }

                @Override
                public String infinitySymbol() {
                    return decimalNumberContext.infinitySymbol();
                }

                @Override
                public char monetaryDecimalSeparator() {
                    return decimalNumberContext.monetaryDecimalSeparator();
                }

                @Override
                public String nanSymbol() {
                    return decimalNumberContext.nanSymbol();
                }

                @Override
                public char negativeSign() {
                    return decimalNumberContext.negativeSign();
                }

                @Override
                public char percentSymbol() {
                    return decimalNumberContext.percentSymbol();
                }

                @Override
                public char permillSymbol() {
                    return decimalNumberContext.permillSymbol();
                }

                @Override
                public char positiveSign() {
                    return decimalNumberContext.positiveSign();
                }

                @Override
                public char zeroDigit() {
                    return decimalNumberContext.zeroDigit();
                }
            }
        );
    }

    private CycleDetectingExpressionEvaluationContext createContext(final ExpressionEvaluationContext context) {
        return CycleDetectingExpressionEvaluationContext.with(context);
    }

    private ValueExpression<String> text() {
        return Expression.value(VALUE);
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
    public Class<CycleDetectingExpressionEvaluationContext> type() {
        return CycleDetectingExpressionEvaluationContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
