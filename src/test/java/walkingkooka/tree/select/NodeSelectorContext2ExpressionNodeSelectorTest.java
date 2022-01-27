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
import walkingkooka.convert.Converters;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.function.ExpressionFunctionContexts;

import java.util.Objects;

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
        this.isNodeSelectedAndCheck(Expression.value(false), false);
    }

    @Test
    public void testIsNodeSelectedBooleanTrue() {
        this.isNodeSelectedAndCheck(Expression.value(true), true);
    }

    @Test
    public void testIsNodeSelectedNumberDifferent() {
        this.isNodeSelectedAndCheck(Expression.value(EXPRESSION_NUMBER_KIND.create(INDEX + 99L)), false);
    }

    @Test
    public void testIsNodeSelectedNumberEqual() {
        this.isNodeSelectedAndCheck(Expression.value(EXPRESSION_NUMBER_KIND.create(INDEX)), true);
    }

    @Test
    public void testIsNodeSelectedStringDifferent() {
        this.isNodeSelectedAndCheck(Expression.value(String.valueOf(INDEX + 99)), false);
    }

    private void isNodeSelectedAndCheck(final Expression expression,
                                          final boolean expected) {
        this.checkEquals(expected,
                this.createContext().isNodeSelected(expression),
                () -> "expression: " + CharSequences.quoteIfChars(expression));
    }

    @Test
    public void testIsNodeSelected() {
        final NodeSelectorContext2ExpressionNodeSelector<TestNode, StringName, StringName, Object> context = this.createContext();
        this.checkEquals(INDEX,
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
                                ExpressionEvaluationContexts.basic(
                                        (f) -> {
                                            throw new UnsupportedOperationException();
                                        },
                                        ExpressionFunctionContexts.fake()
                                )
                        );
                    }

                    private Converter<ExpressionNumberConverterContext> converter() {
                        return Converters.collection(
                                Lists.of(
                                        ExpressionNumber.toConverter(Converters.truthyNumberBoolean()),
                                        ExpressionNumber.fromConverter(Converters.numberNumber()),
                                        Converters.<String, Integer, ExpressionNumberConverterContext>mapper(v -> v instanceof String, Predicates.is(Integer.class), Integer::parseInt)
                                )
                        );
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
