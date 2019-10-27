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
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.expression.ExpressionNodeVisitorTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class NodeSelectorExpressionExpressionNodeVisitorTest implements ExpressionNodeVisitorTesting<NodeSelectorExpressionExpressionNodeVisitor<TestNode, StringName, StringName, Object>>{

    @Test
    public void testAcceptExpressionIgnored() {
        this.acceptExpressionAndCheck(ExpressionNode.addition(ExpressionNode.longNode(1), ExpressionNode.longNode(2)));
    }

    @Test
    public void testAcceptExpressionBooleanFalse() {
        this.acceptExpressionAndCheck(ExpressionNode.booleanNode(false));
    }

    @Test
    public void testAcceptExpressionBooleanTrue() {
        final NodeSelector<TestNode, StringName, StringName, Object> selector = this.selector();
        this.acceptExpressionAndCheck(selector,
                ExpressionNode.booleanNode(true),
                selector.setToString(selector + "[true()]"));
    }

    private void acceptExpressionAndCheck(final ExpressionNode expression) {
        final NodeSelector<TestNode, StringName, StringName, Object> selector = this.selector();

        this.acceptExpressionAndCheck(selector,
                expression,
                selector.append(ExpressionNodeSelector.with(expression)));
    }

    private void acceptExpressionAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                          final ExpressionNode expression,
                                          final NodeSelector<TestNode, StringName, StringName, Object> expected) {
        assertEquals(expected,
                NodeSelectorExpressionExpressionNodeVisitor.acceptExpression(expression, selector),
                () -> "expression=" + expression + " selector: " + selector);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createVisitor(), this.selector().toString());
    }

    @Override
    public NodeSelectorExpressionExpressionNodeVisitor<TestNode, StringName, StringName, Object> createVisitor() {
        return new NodeSelectorExpressionExpressionNodeVisitor<>(this.selector());
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
    public Class<NodeSelectorExpressionExpressionNodeVisitor<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(NodeSelectorExpressionExpressionNodeVisitor.class);
    }
}
