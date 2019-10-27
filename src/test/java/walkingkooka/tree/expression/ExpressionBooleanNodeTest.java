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
import walkingkooka.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ExpressionBooleanNodeTest extends ExpressionLeafNodeTestCase<ExpressionBooleanNode, Boolean> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final ExpressionBooleanNode node = this.createExpressionNode();

        new FakeExpressionNodeVisitor() {
            @Override
            protected Visiting startVisit(final ExpressionNode n) {
                assertSame(node, n);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionNode n) {
                assertSame(node, n);
                b.append("2");
            }

            @Override
            protected void visit(final ExpressionBooleanNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        assertEquals("132", b.toString());
    }

    // Evaluation ...................................................................................................

    @Test
    public void testToBooleanFalse() {
        this.evaluateAndCheckBoolean(ExpressionBooleanNode.with(false), false);
    }

    @Test
    public void testToBooleanTrue() {
        this.evaluateAndCheckBoolean(ExpressionBooleanNode.with(true), true);
    }

    @Test
    public void testTrueToBigDecimal() {
        this.evaluateAndCheckBigDecimal(ExpressionBooleanNode.with(true), 1);
    }

    @Test
    public void testFalseToBigDecimal() {
        this.evaluateAndCheckBigDecimal(ExpressionBooleanNode.with(false), 0);
    }

    @Test
    public void testTrueToBigInteger() {
        this.evaluateAndCheckBigInteger(ExpressionBooleanNode.with(true), 1);
    }

    @Test
    public void testFalseToBigInteger() {
        this.evaluateAndCheckBigInteger(ExpressionBooleanNode.with(false), 0);
    }

    @Test
    public void testTrueToDouble() {
        this.evaluateAndCheckDouble(ExpressionBooleanNode.with(true), 1);
    }

    @Test
    public void testFalseToDouble() {
        this.evaluateAndCheckDouble(ExpressionBooleanNode.with(false), 0);
    }

    @Test
    public void testTrueToLong() {
        this.evaluateAndCheckLong(ExpressionBooleanNode.with(true), 1);
    }

    @Test
    public void testFalseToLong() {
        this.evaluateAndCheckLong(ExpressionBooleanNode.with(false), 0);
    }

    @Test
    public void testTrueToText() {
        this.evaluateAndCheckText(ExpressionBooleanNode.with(true), "true");
    }

    @Test
    public void testFalseToText() {
        this.evaluateAndCheckText(ExpressionBooleanNode.with(false), "false");
    }

    @Test
    public void testToStringTrue() {
        this.toStringAndCheck(this.createExpressionNode(true), "true");
    }

    @Test
    public void testToStringFalse() {
        this.toStringAndCheck(this.createExpressionNode(false), "false");
    }

    @Override
    ExpressionBooleanNode createExpressionNode(final Boolean value) {
        return ExpressionBooleanNode.with(value);
    }

    @Override
    Boolean value() {
        return true;
    }

    @Override
    Boolean differentValue() {
        return false;
    }

    @Override
    Class<ExpressionBooleanNode> expressionNodeType() {
        return ExpressionBooleanNode.class;
    }
}
