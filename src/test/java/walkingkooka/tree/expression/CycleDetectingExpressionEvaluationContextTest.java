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
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.Parsers;

import java.math.BigInteger;
import java.math.MathContext;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public final class CycleDetectingExpressionEvaluationContextTest implements ClassTesting2<CycleDetectingExpressionEvaluationContext>,
        ExpressionEvaluationContextTesting<CycleDetectingExpressionEvaluationContext> {

    private final static String VALUE = "text123";

    private final static ExpressionReference A1 = reference("A1");
    private final static ExpressionReference B2 = reference("B2");
    private final static ExpressionReference C3 = reference("C3");

    private static ExpressionReference reference(final String label) {
        return new ExpressionReference() {
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

    public void testFunction() {
        final ExpressionNodeName name = ExpressionNodeName.with("sum");
        final List<Object> parameters = Lists.of("param-1", "param-2");

        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {
            @Override
            public Object function(final ExpressionNodeName n, final List<Object> p) {
                assertSame(name, n, "name");
                assertSame(parameters, p, "parameters");

                return VALUE;
            }
        });
        assertSame(VALUE, context.function(name, parameters));
    }

    @Test
    public void testReference() {
        final ExpressionNode target = this.text();

        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public Optional<ExpressionNode> reference(final ExpressionReference reference) {
                assertSame(A1, reference, "reference");
                return Optional.of(target);
            }
        });
        assertSame(target, context.reference(A1).orElse(null));
    }

    @Test
    public void testReferenceToReference() {
        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public Optional<ExpressionNode> reference(final ExpressionReference reference) {
                assertSame(A1, reference, "reference");
                return Optional.of(text());
            }
        });
        final ExpressionNode expression = ExpressionNode.reference(A1);

        this.toValueAndCheck(expression, context, VALUE);
        this.toValueAndCheck(expression, context, VALUE);
        this.toValueAndCheck(expression, context, VALUE);
    }

    @Test
    public void testReferenceToReferenceToReference() {
        // B2 -> A1 -> C3
        final ExpressionNode b2 = ExpressionNode.reference(A1);
        final ExpressionNode a1 = ExpressionNode.reference(C3);
        final ExpressionNode c3 = this.text();

        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public Optional<ExpressionNode> reference(final ExpressionReference reference) {
                return Optional.of(this.reference0(reference));
            }

            private ExpressionNode reference0(final ExpressionReference reference) {
                if (B2 == reference) {
                    return b2;
                }
                if (A1 == reference) {
                    return a1;
                }
                if (C3 == reference) {
                    return c3;
                }
                return this.unknownReference(reference);
            }

            private <T> T unknownReference(final ExpressionReference reference) {
                fail("Unknown reference=" + reference);
                return null;
            }
        });

        this.toValueAndCheck(c3, context, VALUE);
        this.toValueAndCheck(a1, context, VALUE);
        this.toValueAndCheck(b2, context, VALUE);
    }

    @Test
    public void testReferenceToSelfCycleFails() {
        // A1 -> target
        final ExpressionNode target = ExpressionNode.reference(A1);

        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public Optional<ExpressionNode> reference(final ExpressionReference reference) {
                if (A1 == reference) {
                    return Optional.of(target);
                }
                return this.unknownReference(reference);
            }

            private <T> T unknownReference(final ExpressionReference reference) {
                fail("Unknown reference=" + reference);
                return null;
            }
        });

        assertThrows(CycleDetectedExpressionEvaluationConversionException.class, () -> target.toValue(context));
    }

    @Test
    public void testReferenceWithCycleFails() {
        // B2 -> A1 -> target
        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public Optional<ExpressionNode> reference(final ExpressionReference reference) {
                return Optional.of(this.reference0(reference));
            }

            private ExpressionNode reference0(final ExpressionReference reference) {
                if (B2 == reference) {
                    return ExpressionNode.reference(B2);
                }
                if (A1 == reference) {
                    return ExpressionNode.reference(A1);
                }
                return this.unknownReference(reference);
            }

            private <T> T unknownReference(final ExpressionReference reference) {
                fail("Unknown reference=" + reference);
                return null;
            }
        });
        assertThrows(CycleDetectedExpressionEvaluationConversionException.class, () -> ExpressionNode.reference(A1).toValue(context));
    }

    @Test
    public void testReferenceWithCycleFails2() {
        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public Optional<ExpressionNode> reference(final ExpressionReference reference) {
                return Optional.of(this.reference0(reference));
            }

            private ExpressionNode reference0(final ExpressionReference reference) {
                if (B2 == reference || C3 == reference) {
                    return ExpressionNode.reference(B2);
                }
                if (A1 == reference) {
                    return ExpressionNode.reference(A1);
                }
                return this.unknownReference(reference);
            }

            private <T> T unknownReference(final ExpressionReference reference) {
                fail("Unknown reference=" + reference);
                return null;
            }
        });

        assertThrows(CycleDetectedExpressionEvaluationConversionException.class, () -> {
            ExpressionNode.reference(B2).toValue(context); // --> B2 --> A1 --> B2 cycle!!!
        });
    }

    @Test
    public void testReferenceAfterCycleDetected() {
        // B2 -> A1 -> expression
        final ExpressionReference a1 = reference("A1");
        final ExpressionReference b2 = reference("B2");

        final ExpressionNode b2Expression = ExpressionNode.reference(a1);
        final ExpressionNode a1Expression = ExpressionNode.reference(b2);
        final ExpressionNode expression = this.text();

        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public Optional<ExpressionNode> reference(final ExpressionReference reference) {
                return Optional.of(this.reference0(reference));
            }

            private ExpressionNode reference0(final ExpressionReference reference) {
                if (b2 == reference) {
                    return a1Expression;
                }
                if (a1 == reference) {
                    return b2Expression;
                }
                return this.unknownReference(reference);
            }

            private <T> T unknownReference(final ExpressionReference reference) {
                fail("Unknown reference=" + reference);
                return null;
            }
        });

        assertThrows(CycleDetectedExpressionEvaluationConversionException.class, () -> b2Expression.toValue(context));
        this.toValueAndCheck(expression, context, VALUE);
    }

    @Test
    public void testMathContext() {
        final MathContext mathContext = MathContext.DECIMAL32;

        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {
            @Override
            public MathContext mathContext() {
                return mathContext;
            }
        });
        assertSame(mathContext, context.mathContext());
    }

    @Test
    public void testConvert() {
        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public char negativeSign() {
                return '-';
            }

            @Override
            public char positiveSign() {
                return '+';
            }

            @Override
            public <T> Either<T, String> convert(final Object value, final Class<T> target) {
                return Converters.parser(BigInteger.class, Parsers.bigInteger(10), (c) -> ParserContexts.basic(c, c))
                        .convert(value, target, ConverterContexts.basic(DateTimeContexts.fake(), this));
            }
        });
        assertEquals(Either.left(BigInteger.valueOf(123)), context.convert("123", BigInteger.class));
    }

    @Override
    public CycleDetectingExpressionEvaluationContext createContext() {
        final DecimalNumberContext decimalNumberContext = this.decimalNumberContext();

        return this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public Locale locale() {
                return decimalNumberContext.locale();
            }

            @Override
            public MathContext mathContext() {
                return decimalNumberContext.mathContext();
            }

            @Override
            public String currencySymbol() {
                return decimalNumberContext.currencySymbol();
            }

            @Override
            public char decimalSeparator() {
                return decimalNumberContext.decimalSeparator();
            }

            @Override
            public char exponentSymbol() {
                return decimalNumberContext.exponentSymbol();
            }

            @Override
            public char groupingSeparator() {
                return decimalNumberContext.groupingSeparator();
            }

            @Override
            public char negativeSign() {
                return decimalNumberContext.negativeSign();
            }

            @Override
            public char percentageSymbol() {
                return decimalNumberContext.percentageSymbol();
            }

            @Override
            public char positiveSign() {
                return decimalNumberContext.positiveSign();
            }
        });
    }

    private DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.american(this.mathContext());
    }

    private CycleDetectingExpressionEvaluationContext createContext(final ExpressionEvaluationContext context) {
        return CycleDetectingExpressionEvaluationContext.with(context);
    }

    private ExpressionNode text() {
        return ExpressionNode.text(VALUE);
    }

    @Override
    public String currencySymbol() {
        return this.decimalNumberContext().currencySymbol();
    }

    @Override
    public char decimalSeparator() {
        return this.decimalNumberContext().decimalSeparator();
    }

    @Override
    public char exponentSymbol() {
        return this.decimalNumberContext().exponentSymbol();
    }

    @Override
    public char groupingSeparator() {
        return this.decimalNumberContext().groupingSeparator();
    }

    @Override
    public MathContext mathContext() {
        return MathContext.DECIMAL32;
    }

    @Override
    public char negativeSign() {
        return this.decimalNumberContext().negativeSign();
    }

    @Override
    public char percentageSymbol() {
        return this.decimalNumberContext().percentageSymbol();
    }

    @Override
    public char positiveSign() {
        return this.decimalNumberContext().positiveSign();
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
