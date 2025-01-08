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
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionVisitorTesting;
import walkingkooka.tree.expression.ValueExpression;

public final class NodeSelectorExpressionExpressionVisitorTest implements ExpressionVisitorTesting<NodeSelectorExpressionExpressionVisitor<TestNode, StringName, StringName, Object>> {

    @Test
    public void testAcceptExpressionIgnored() {
        this.acceptExpressionAndCheck(
            Expression.add(
                number(1),
                number(2)
            )
        );
    }

    private ValueExpression<ExpressionNumber> number(final int value) {
        return Expression.value(ExpressionNumberKind.DEFAULT.create(value));
    }

    @Test
    public void testAcceptExpressionBooleanFalse() {
        this.acceptExpressionAndCheck(Expression.value(false));
    }

    @Test
    public void testAcceptExpressionBooleanTrue() {
        final NodeSelector<TestNode, StringName, StringName, Object> selector = this.selector();
        this.acceptExpressionAndCheck(selector,
            Expression.value(true),
            selector.setToString(selector + "[true()]"));
    }

    private void acceptExpressionAndCheck(final Expression expression) {
        final NodeSelector<TestNode, StringName, StringName, Object> selector = this.selector();

        this.acceptExpressionAndCheck(selector,
            expression,
            selector.append(ExpressionNodeSelector.with(expression)));
    }

    private void acceptExpressionAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                          final Expression expression,
                                          final NodeSelector<TestNode, StringName, StringName, Object> expected) {
        this.checkEquals(expected,
            NodeSelectorExpressionExpressionVisitor.acceptExpression(expression, selector),
            () -> "expression=" + expression + " selector: " + selector);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createVisitor(), this.selector().toString());
    }

    @Override
    public NodeSelectorExpressionExpressionVisitor<TestNode, StringName, StringName, Object> createVisitor() {
        return new NodeSelectorExpressionExpressionVisitor<>(this.selector());
    }

    private NodeSelector<TestNode, StringName, StringName, Object> selector() {
        return TestNode.relativeNodeSelector()
            .named(Names.string("abc123"));
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return NodeSelector.class.getSimpleName() + "Expression";
    }

    @Override
    public Class<NodeSelectorExpressionExpressionVisitor<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(NodeSelectorExpressionExpressionVisitor.class);
    }
}
