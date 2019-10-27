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
import walkingkooka.visit.Visiting;

import java.math.MathContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionNodeTest extends ExpressionVariableNodeTestCase<ExpressionFunctionNode> {

    @Test
    public void testWithNullNameFails() {
        assertThrows(NullPointerException.class, () -> ExpressionFunctionNode.with(null, this.children()));
    }

    @Test
    public void testSetNameNullFails() {
        assertThrows(NullPointerException.class, () -> this.createExpressionNode().setName(null));
    }

    @Test
    public void testSetNameSame() {
        final ExpressionFunctionNode node = this.createExpressionNode();
        assertSame(node, node.setName(node.name()));
    }

    @Test
    public void testSetNameDifferent() {
        final ExpressionFunctionNode node = this.createExpressionNode();
        final ExpressionNodeName differentName = name("different-name");
        final ExpressionFunctionNode different = node.setName(differentName);
        assertEquals(differentName, different.name(), "name");
        this.checkChildren(different, node.children());
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ExpressionNode> visited = Lists.array();

        final ExpressionFunctionNode function = this.createExpressionNode();
        final ExpressionNode text1 = function.children().get(0);
        final ExpressionNode text2 = function.children().get(1);
        final ExpressionNode text3 = function.children().get(2);

        new FakeExpressionNodeVisitor() {
            @Override
            protected Visiting startVisit(final ExpressionNode n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionNode n) {
                b.append("2");
                visited.add(n);
            }

            @Override
            protected Visiting startVisit(final ExpressionFunctionNode t) {
                assertSame(function, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionFunctionNode t) {
                assertSame(function, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ExpressionTextNode t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(function);
        assertEquals("1315215215242", b.toString());
        assertEquals(Lists.of(function, function,
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
        this.evaluateAndCheckBoolean(this.createExpressionNode(), this.context("false"), false);
    }

    @Test
    public void testToBooleanTrue() {
        this.evaluateAndCheckBoolean(this.createExpressionNode(), this.context("true"), true);
    }

    @Test
    public void testToBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(), this.context("123"), 123);
    }

    @Test
    public void testToBigInteger() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(), this.context("123"), 123);
    }

    @Test
    public void testToDouble() {
        this.evaluateAndCheckDouble(this.createExpressionNode(), this.context("123"), 123);
    }

    @Test
    public void testToLong() {
        this.evaluateAndCheckLong(this.createExpressionNode(), this.context("123"), 123);
    }

    @Test
    public void testToNumber() {
        this.evaluateAndCheckNumberBigDecimal(this.createExpressionNode(), this.context("123"), 123);
    }

    @Test
    public void testToText() {
        this.evaluateAndCheckText(this.createExpressionNode(), this.context("123"), "123");
    }

    final ExpressionEvaluationContext context(final String functionValue) {
        final ExpressionEvaluationContext context = context();

        return new FakeExpressionEvaluationContext() {

            @Override
            public Object function(final ExpressionNodeName name, final List<Object> parameters) {
                assertEquals(name("fx"), name, "function name");
                assertEquals(Lists.of("child-111", "child-222", "child-333"), parameters, "parameter values");
                return functionValue;
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
        assertEquals("fx(\"child-111\",\"child-222\",\"child-333\")", this.createExpressionNode().toString());
    }

    @Override
    ExpressionFunctionNode createExpressionNode(final List<ExpressionNode> children) {
        return ExpressionFunctionNode.with(name("fx"), children);
    }

    private ExpressionNodeName name(final String name) {
        return ExpressionNodeName.with(name);
    }

    @Override
    Class<ExpressionFunctionNode> expressionNodeType() {
        return ExpressionFunctionNode.class;
    }
}
