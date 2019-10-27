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
import walkingkooka.collect.list.Lists;
import walkingkooka.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ExpressionGreaterThanNodeTest extends ExpressionComparisonBinaryNodeTestCase<ExpressionGreaterThanNode> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ExpressionNode> visited = Lists.array();

        final ExpressionGreaterThanNode gt = this.createExpressionNode();
        final ExpressionNode text1 = gt.children().get(0);
        final ExpressionNode text2 = gt.children().get(1);

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
            protected Visiting startVisit(final ExpressionGreaterThanNode t) {
                assertSame(gt, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionGreaterThanNode t) {
                assertSame(gt, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ExpressionTextNode t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(gt);
        assertEquals("1315215242", b.toString());
        assertEquals(Lists.of(gt, gt,
                text1, text1, text1,
                text2, text2, text2,
                gt, gt),
                visited,
                "visited");
    }

    // BigDecimal ................................................................................................

    @Test
    public void testEvaluateToBooleanBigDecimalBigDecimal() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(12), bigDecimal(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigDecimal2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(12), bigDecimal(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigDecimal3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(12), bigDecimal(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigInteger() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(12), bigInteger(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigInteger2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(12), bigInteger(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigInteger3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(12), bigInteger(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalDouble() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(12), doubleValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalDouble2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(12), doubleValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalDouble3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(12), doubleValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalLong() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(12), longValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalLong2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(12), longValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalLong3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(12), longValue(-99)), true);
    }

    // BigInteger................................................................................................

    @Test
    public void testEvaluateToBooleanBigIntegerBigDecimal() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigInteger(12), bigDecimal(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigDecimal2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigInteger(12), bigDecimal(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigDecimal3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigInteger(12), bigDecimal(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigInteger() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigInteger(12), bigInteger(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigInteger2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigInteger(12), bigInteger(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigInteger3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigInteger(12), bigInteger(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerDouble() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigInteger(12), doubleValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerDouble2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigInteger(12), doubleValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerDouble3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigInteger(12), doubleValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerLong() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigInteger(12), longValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerLong2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigInteger(12), longValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerLong3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigInteger(12), longValue(-99)), true);
    }

    // Double ................................................................................................

    @Test
    public void testEvaluateToBooleanDoubleBigDecimal() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(doubleValue(12), bigDecimal(34)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigDecimal2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(doubleValue(12), bigDecimal(12)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigDecimal3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(doubleValue(12), bigDecimal(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigInteger() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(doubleValue(12), bigInteger(34)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigInteger2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(doubleValue(12), bigInteger(12)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigInteger3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(doubleValue(12), bigInteger(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleDouble() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(doubleValue(12), doubleValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleDouble2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(doubleValue(12), doubleValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleDouble3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(doubleValue(12), doubleValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleLong() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(doubleValue(12), longValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleLong2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(doubleValue(12), longValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleLong3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(doubleValue(12), longValue(-99)), true);
    }

    // Long................................................................................................

    @Test
    public void testEvaluateToBooleanLongBigDecimal() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(12), bigDecimal(34)), false);
    }

    @Test
    public void testEvaluateToBooleanLongBigDecimal2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(12), bigDecimal(12)), false);
    }

    @Test
    public void testEvaluateToBooleanLongBigDecimal3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(12), bigDecimal(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanLongBigInteger() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(12), bigInteger(34)), false);
    }

    @Test
    public void testEvaluateToBooleanLongBigInteger2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(12), bigInteger(12)), false);
    }

    @Test
    public void testEvaluateToBooleanLongBigInteger3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(12), bigInteger(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanLongDouble() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(12), doubleValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanLongDouble2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(12), doubleValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanLongDouble3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(12), doubleValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanLongLong() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(12), longValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanLongLong2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(12), longValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanLongLong3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(12), longValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanLongText() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(12), text(34)), false);
    }

    @Test
    public void testEvaluateToBooleanLongText2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(12), text(12)), false);
    }

    @Test
    public void testEvaluateToBooleanLongText3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(12), text(-99)), true);
    }

    // Long................................................................................................

    @Test
    public void testEvaluateToBooleanTextBigDecimal() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(text(12), bigDecimal(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextBigDecimal2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(text(12), bigDecimal(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextBigDecimal3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(text(12), bigDecimal(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanTextBigInteger() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(text(12), bigInteger(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextBigInteger2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(text(12), bigInteger(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextBigInteger3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(text(12), bigInteger(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanTextDouble() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(text(12), doubleValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextDouble2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(text(12), doubleValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextDouble3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(text(12), doubleValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanTextLong() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(text(12), longValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextLong2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(text(12), longValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextLong3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(text(12), longValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanTextText() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(text(12), text(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextText2() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(text(12), text(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextText3() {
        // left gt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(text(12), text(-99)), true);
    }

    @Override
    ExpressionGreaterThanNode createExpressionNode(final ExpressionNode left, final ExpressionNode right) {
        return ExpressionGreaterThanNode.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + ">" + RIGHT_TO_STRING;
    }

    @Override
    Class<ExpressionGreaterThanNode> expressionNodeType() {
        return ExpressionGreaterThanNode.class;
    }
}
