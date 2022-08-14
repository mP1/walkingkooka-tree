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
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class LambdaFunctionExpressionTest extends UnaryExpressionTestCase<LambdaFunctionExpression> {

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
            ExpressionFunctionParameterName.with("x").required(Object.class),
            ExpressionFunctionParameterName.with("y").required(Object.class)
    );

    @Test
    public void testWithNullParameterFails() {
        assertThrows(
                NullPointerException.class,
                () -> LambdaFunctionExpression.with(
                        null,
                        Expression.value(123)
                )
        );
    }

    // setParameters................................................................................................

    @Test
    public void testSetParametersWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createExpression().setParameters(null)
        );
    }

    @Test
    public void testSetParametersSame() {
        final LambdaFunctionExpression expression = this.createExpression();
        assertSame(expression, expression.setParameters(PARAMETERS));
    }

    @Test
    public void testSetParametersDifferent() {
        final LambdaFunctionExpression expression = this.createExpression();

        final List<ExpressionFunctionParameter<?>> differentParameter = Lists.of(
                ExpressionFunctionParameterName.with("a").required(Object.class),
                ExpressionFunctionParameterName.with("b").required(Object.class)
        );

        final LambdaFunctionExpression different = expression.setParameters(differentParameter);
        assertNotSame(expression, different);

        this.checkEquals(
                differentParameter,
                different.parameters(),
                "parameters"
        );
    }

    // visitor.........................................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();

        final LambdaFunctionExpression node = this.createExpression();
        final Expression body = node.children().get(0);

        new FakeExpressionVisitor() {
            @Override
            protected Visiting startVisit(final Expression n) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Expression n) {
                b.append("2");
            }

            @Override
            protected Visiting startVisit(final LambdaFunctionExpression n) {
                assertSame(node, n);
                b.append("3");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final LambdaFunctionExpression n) {
                assertSame(node, n);
                b.append("4");
            }

            @Override
            protected void visit(final ValueExpression<?> n) {
                assertSame(body, n);
                b.append("5");
            }
        }.accept(node);

        this.checkEquals(
                "1315242",
                b.toString()
        );
    }

    // Evaluation .....................................................................................................

    @Test
    public void testToValue() {
        this.evaluateAndCheckValue(
                LambdaFunctionExpression.with(
                        PARAMETERS,
                        Expression.add(
                                Expression.reference(
                                        new FakeExpressionReference() {
                                            @Override
                                            public String toString() {
                                                return "x";
                                            }
                                        }),
                                Expression.reference(
                                        new FakeExpressionReference() {
                                            @Override
                                            public String toString() {
                                                return "y";
                                            }
                                        })
                        )
                ),
                new FakeExpressionEvaluationContext() {

                    @Override
                    public <T> Either<T, String> convert(final Object value,
                                                         final Class<T> target) {
                        return this.successfulConversion(
                                target.cast(
                                        EXPRESSION_NUMBER_KIND.create((Number) value)
                                ),
                                target
                        );
                    }

                    @Override
                    public Object evaluate(final Expression expression) {
                        return expression.toValue(this);
                    }

                    @Override
                    public boolean isText(final Object value) {
                        return false;
                    }

                    @Override
                    public Optional<Optional<Object>> reference(final ExpressionReference reference) {
                        switch (reference.toString()) {
                            case "x":
                                return Optional.of(
                                        Optional.of(10)
                                );
                            case "y":
                                return Optional.of(
                                        Optional.of(20)
                                );
                            default:
                                throw new IllegalArgumentException("Unknown reference: " + reference);
                        }
                    }
                },
                EXPRESSION_NUMBER_KIND.create(30)
        );
    }

    @Test
    public void testToText() {
        final String text = "Hello123";

        this.evaluateAndCheckText(
                this.createExpression(
                        Expression.value(text)
                ),
                text
        );
    }


    // printTree.......................................................................................................

    @Test
    public void testPrintTree() {
        this.treePrintAndCheck(
                this.createExpression(),
                "LambdaFunctionExpression\n" +
                        "  parameters\n" +
                        "    java.lang.Object x\n" +
                        "    java.lang.Object y\n" +
                        "  body\n" +
                        "    ValueExpression \"child123\"\n"
        );
    }

    // equals.. ........................................................................................................

    @Test
    public void testEqualsDifferentParameters() {
        this.checkNotEquals(
                LambdaFunctionExpression.with(
                        Lists.of(
                                ExpressionFunctionParameterName.with("a").required(Object.class),
                                ExpressionFunctionParameterName.with("b").required(Object.class)
                        ),
                        this.child()
                )
        );
    }

    // ToString .......................................................................................................

    @Override
    String expectedToString() {
        return "(x, y)->{" + this.child() + "}";
    }

    @Override
    LambdaFunctionExpression createExpression(final Expression value) {
        return LambdaFunctionExpression.with(
                PARAMETERS,
                value
        );
    }

    @Override
    Class<LambdaFunctionExpression> expressionType() {
        return LambdaFunctionExpression.class;
    }
}
