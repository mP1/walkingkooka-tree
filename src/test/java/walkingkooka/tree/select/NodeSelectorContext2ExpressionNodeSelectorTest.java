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

package walkingkooka.tree.select;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.ExpressionNumberConverterContexts;
import walkingkooka.tree.expression.ExpressionReference;

import java.math.MathContext;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class NodeSelectorContext2ExpressionNodeSelectorTest extends NodeSelectorContext2TestCase<NodeSelectorContext2ExpressionNodeSelector<TestNode, StringName, StringName, Object>,
        TestNode, StringName, StringName, Object> {

    private final static int INDEX = 5;

    @Test
    public void testAll() {
        final NodeSelectorContext2ExpressionNodeSelector<TestNode, StringName, StringName, Object> context = this.createContext();
        this.checkType(context.all(), NodeSelectorContext2All.class);
    }

    @Test
    public void testExpression() {
        final NodeSelectorContext2ExpressionNodeSelector<TestNode, StringName, StringName, Object> context = this.createContext();
        final NodeSelectorContext2<TestNode, StringName, StringName, Object> expression = context.expression();
        assertNotSame(context, expression);
        this.checkType(expression, NodeSelectorContext2ExpressionNodeSelector.class);
    }

    @Test
    public void testExpressionCreateIfNecessary() {
        final NodeSelectorContext2ExpressionNodeSelector<TestNode, StringName, StringName, Object> context = this.createContext();
        assertSame(context, context.expressionCreateIfNecessary());
    }

    @Test
    public void testIsNodeSelectedBooleanFalse() {
        this.isNodeSelectedAndCheck(Expression.booleanExpression(false), false);
    }

    @Test
    public void testIsNodeSelectedBooleanTrue() {
        this.isNodeSelectedAndCheck(Expression.booleanExpression(true), true);
    }

    @Test
    public void testIsNodeSelectedNumberDifferent() {
        this.isNodeSelectedAndCheck(Expression.expressionNumber(EXPRESSION_NUMBER_KIND.create(INDEX + 99L)), false);
    }

    @Test
    public void testIsNodeSelectedNumberEqual() {
        this.isNodeSelectedAndCheck(Expression.expressionNumber(EXPRESSION_NUMBER_KIND.create(INDEX)), true);
    }

    @Test
    public void testIsNodeSelectedStringDifferent() {
        this.isNodeSelectedAndCheck(Expression.string(String.valueOf(INDEX + 99)), false);
    }

    private void isNodeSelectedAndCheck(final Expression expression,
                                          final boolean expected) {
        assertEquals(expected,
                this.createContext().isNodeSelected(expression),
                () -> "expression: " + CharSequences.quoteIfChars(expression));
    }

    @Test
    public void testIsNodeSelected() {
        final NodeSelectorContext2ExpressionNodeSelector<TestNode, StringName, StringName, Object> context = this.createContext();
        assertEquals(INDEX,
                context.nodePosition(),
                () -> "nodePosition in " + context);
    }

    @Override
    public NodeSelectorContext2ExpressionNodeSelector<TestNode, StringName, StringName, Object> createContext() {
        final NodeSelectorContext2ExpressionNodeSelector<TestNode, StringName, StringName, Object> context = NodeSelectorContext2ExpressionNodeSelector.with(
                new FakeNodeSelectorContext<TestNode, StringName, StringName, Object>() {

                    @Override
                    public Object evaluate(final Expression expression) {
                        Objects.requireNonNull(expression, "expression");

                        return expression.toValue(
                                ExpressionEvaluationContexts.basic(EXPRESSION_NUMBER_KIND,
                                        (e, parameters) -> {
                                            throw new UnsupportedOperationException();
                                        },
                                        new Function<ExpressionReference, Optional<Expression>>() {
                                            @Override
                                            public Optional<Expression> apply(final ExpressionReference reference) {
                                                throw new UnsupportedOperationException();
                                            }
                                        },
                                        this.converter(),
                                        ExpressionNumberConverterContexts.basic(this.converterContext(), EXPRESSION_NUMBER_KIND)));
                    }

                    private Converter<ExpressionNumberConverterContext> converter() {
                        return  Converters.collection(Lists.of(
                                ExpressionNumber.toConverter(Converters.truthyNumberBoolean()),
                                ExpressionNumber.fromConverter(Converters.numberNumber()),
                                Converters.<String, Integer>function(v -> v instanceof String, Predicates.is(Integer.class), Integer::parseInt)));
                    }

                    private ExpressionNumberConverterContext converterContext() {
                        return ExpressionNumberConverterContexts.basic(ConverterContexts.basic(ConverterContexts.fake(), DecimalNumberContexts.american(MathContext.DECIMAL32)), EXPRESSION_NUMBER_KIND);
                    }
                });
        context.position = INDEX;
        return context;
    }

    @Test
    public void testToString() {
        final NodeSelectorContext2ExpressionNodeSelector<TestNode, StringName, StringName, Object> context = NodeSelectorContext2ExpressionNodeSelector.with(this.contextWithToString("Context123"));
        context.position = 45;
        this.toStringAndCheck(context, "position: 45 Context123");
    }

    @Override
    public Class<NodeSelectorContext2ExpressionNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(NodeSelectorContext2ExpressionNodeSelector.class);
    }
}
