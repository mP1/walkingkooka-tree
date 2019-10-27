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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ExpressionBigDecimalNodeTest extends ExpressionLeafNodeTestCase<ExpressionBigDecimalNode, BigDecimal> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final ExpressionBigDecimalNode node = this.createExpressionNode();

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
            protected void visit(final ExpressionBigDecimalNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        assertEquals("132", b.toString());
    }

    // Evaluation ...................................................................................................

    @Test
    public void testToBooleanFalse() {
        this.evaluateAndCheckBoolean(this.createExpressionNode(0), false);
    }

    @Test
    public void testToBooleanTrue() {
        this.evaluateAndCheckBoolean(this.createExpressionNode(1), true);
    }

    @Test
    public void testToBigDecimal() {
        final BigDecimal value = BigDecimal.valueOf(123);
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(value), value);
    }

    @Test
    public void testToBigInteger() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(123), 123);
    }

    @Test
    public void testToDouble() {
        this.evaluateAndCheckDouble(this.createExpressionNode(123.5), 123.5);
    }

    @Test
    public void testToLong() {
        this.evaluateAndCheckLong(this.createExpressionNode(123), 123);
    }

    @Test
    public void testToNumberBigDecimal() {
        this.evaluateAndCheckNumberBigDecimal(this.createExpressionNode(123), 123);
    }

    @Test
    public void testToText() {
        this.evaluateAndCheckText(this.createExpressionNode(123), "123");
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createExpressionNode(BigDecimal.valueOf(1)), "1");
    }

    @Test
    public void testToString2() {
        this.toStringAndCheck(this.createExpressionNode(BigDecimal.valueOf(234)), "234");
    }

    private ExpressionBigDecimalNode createExpressionNode(final double value) {
        return this.createExpressionNode(BigDecimal.valueOf(value));
    }

    @Override
    ExpressionBigDecimalNode createExpressionNode(final BigDecimal value) {
        return ExpressionBigDecimalNode.with(value);
    }

    @Override
    BigDecimal value() {
        return BigDecimal.ONE;
    }

    @Override
    BigDecimal differentValue() {
        return BigDecimal.valueOf(999);
    }

    @Override
    Class<ExpressionBigDecimalNode> expressionNodeType() {
        return ExpressionBigDecimalNode.class;
    }
}
