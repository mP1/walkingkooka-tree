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
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionContext;
import walkingkooka.tree.expression.function.FakeExpressionFunction;
import walkingkooka.tree.select.parser.NodeSelectorAttributeName;
import walkingkooka.visit.Visiting;

import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FunctionExpressionTest extends VariableExpressionTestCase<FunctionExpression> {

    @Test
    public void testWithNullNameFails() {
        assertThrows(NullPointerException.class, () -> FunctionExpression.with(null, this.children()));
    }

    @Test
    public void testSetNameNullFails() {
        assertThrows(NullPointerException.class, () -> this.createExpression().setName(null));
    }

    @Test
    public void testSetNameSame() {
        final FunctionExpression node = this.createExpression();
        assertSame(node, node.setName(node.name()));
    }

    @Test
    public void testSetNameDifferent() {
        final FunctionExpression node = this.createExpression();
        final FunctionExpressionName differentName = name("different-name");
        final FunctionExpression different = node.setName(differentName);
        this.checkEquals(differentName, different.name(), "name");
        this.checkChildren(different, node.children());
    }

    // ExpressionPurity.................................................................................................

    @Test
    public void testIsPureTrue() {
        this.isPureAndCheck2(true);
    }

    @Test
    public void testIsPureFalse() {
        this.isPureAndCheck2(false);
    }

    private void isPureAndCheck2(final boolean pure) {
        final FunctionExpressionName name = this.name();

        this.isPureAndCheck(
                this.createExpression(),
                new ExpressionPurityContext() {
                    @Override
                    public boolean isPure(final FunctionExpressionName n) {
                        checkEquals(name, n, "name");
                        return pure;
                    }
                },
                pure
        );
    }

    @Test
    public void testToValueFunctionResolveReferencesTrue() {
        final StringName attribute = Names.string("attribute123");
        final String value = "value-345";
        final ExpressionReference reference = NodeSelectorAttributeName.with(attribute.value());

        final FunctionExpressionName function = FunctionExpressionName.with("custom-function");
        final List<Expression> parameters = Lists.of(
                Expression.string("1"),
                Expression.reference(reference)
        );
        final Expression expression = Expression.function(function, parameters);

        this.checkEquals(
                Lists.of("1", value),
                expression.toValue(new FakeExpressionEvaluationContext() {
                    @Override
                    public ExpressionFunction<?, ExpressionFunctionContext> function(final FunctionExpressionName name) {
                        checkEquals(function, name, "function name");
                        return new FakeExpressionFunction<>() {
                            @Override
                            public Object apply(final List<Object> parameters,
                                                final ExpressionFunctionContext contet) {
                                return parameters;
                            }

                            @Override
                            public boolean resolveReferences() {
                                return true;
                            }
                        };
                    }

                    @Override
                    public Optional<Expression> reference(final ExpressionReference r) {
                        assertSame(reference, r, "reference");
                        return Optional.of(Expression.string(value));
                    }
                }));
    }

    @Test
    public void testToValueFunctionResolveReferencesFalse() {
        final StringName attribute = Names.string("attribute123");
        final String value = "value-345";
        final ExpressionReference reference = NodeSelectorAttributeName.with(attribute.value());

        final FunctionExpressionName function = FunctionExpressionName.with("custom-function");
        final List<Expression> parameters = Lists.of(
                Expression.string("1"),
                Expression.reference(reference)
        );
        final Expression expression = Expression.function(function, parameters);

        this.checkEquals(
                Lists.of("1", reference),
                expression.toValue(
                        new FakeExpressionEvaluationContext() {
                            @Override
                            public ExpressionFunction<?, ExpressionFunctionContext> function(final FunctionExpressionName name) {
                                checkEquals(function, name, "function name");
                                return new FakeExpressionFunction<>() {

                                    @Override
                                    public Object apply(final List<Object> parameters,
                                                        final ExpressionFunctionContext context) {
                                        return parameters;
                                    }

                                    @Override
                                    public boolean resolveReferences() {
                                        return false;
                                    }
                                };
                            }

                            public Object evaluate(final FunctionExpressionName name, final List<Object> parameters) {
                                Objects.requireNonNull(name, "name");
                                Objects.requireNonNull(parameters, "parameters");
                                throw new UnsupportedOperationException();
                            }
                        })
        );
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final FunctionExpression function = this.createExpression();
        final Expression text1 = function.children().get(0);
        final Expression text2 = function.children().get(1);
        final Expression text3 = function.children().get(2);

        new FakeExpressionVisitor() {
            @Override
            protected Visiting startVisit(final Expression n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Expression n) {
                b.append("2");
                visited.add(n);
            }

            @Override
            protected Visiting startVisit(final FunctionExpression t) {
                assertSame(function, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final FunctionExpression t) {
                assertSame(function, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final StringExpression t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(function);
        this.checkEquals("1315215215242", b.toString());
        this.checkEquals(Lists.of(function, function,
                        text1, text1, text1,
                        text2, text2, text2,
                        text3, text3, text3,
                        function, function),
                visited,
                "visited");
    }

    // Evaluation ...................................................................................................

    @Test
    public void testToBooleanFalse() {
        this.evaluateAndCheckBoolean(this.createExpression(), this.context("false"), false);
    }

    @Test
    public void testToBooleanTrue() {
        this.evaluateAndCheckBoolean(this.createExpression(), this.context("true"), true);
    }

    @Test
    public void testToExpressionNumber() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(),
                this.context("123"),
                expressionNumberValue(123));
    }

    @Test
    public void testToText() {
        this.evaluateAndCheckText(this.createExpression(), this.context("123"), "123");
    }

    private ExpressionEvaluationContext context(final String functionValue) {
        final ExpressionEvaluationContext context = context();

        return new FakeExpressionEvaluationContext() {
            @Override
            public ExpressionFunction<?, ExpressionFunctionContext> function(final FunctionExpressionName name) {
                checkEquals(name("fx"), name, "function name");

                return new FakeExpressionFunction<>() {
                    @Override
                    public Object apply(final List<Object> parameters,
                                        final ExpressionFunctionContext context) {
                        checkEquals(Lists.of("child-111", "child-222", "child-333"), parameters, "parameter values");

                        return functionValue;
                    }

                    @Override
                    public boolean resolveReferences() {
                        return true;
                    }
                };
            }

            @Override
            public MathContext mathContext() {
                return context.mathContext();
            }

            @Override
            public <T> Either<T, String> convert(final Object value, final Class<T> target) {
                return context.convert(value, target);
            }
        };
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        this.checkEquals("fx(\"child-111\",\"child-222\",\"child-333\")", this.createExpression().toString());
    }

    @Override
    FunctionExpression createExpression(final List<Expression> children) {
        return FunctionExpression.with(this.name(), children);
    }

    private FunctionExpressionName name() {
        return this.name("fx");
    }

    private FunctionExpressionName name(final String name) {
        return FunctionExpressionName.with(name);
    }

    @Override
    Class<FunctionExpression> expressionType() {
        return FunctionExpression.class;
    }
}
