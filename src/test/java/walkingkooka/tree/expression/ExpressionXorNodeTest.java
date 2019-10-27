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

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ExpressionXorNodeTest extends ExpressionLogicalBinaryNodeTestCase<ExpressionXorNode> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ExpressionNode> visited = Lists.array();

        final ExpressionXorNode xor = this.createExpressionNode();
        final ExpressionNode text1 = xor.children().get(0);
        final ExpressionNode text2 = xor.children().get(1);

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
            protected Visiting startVisit(final ExpressionXorNode t) {
                assertSame(xor, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionXorNode t) {
                assertSame(xor, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ExpressionTextNode t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(xor);
        assertEquals("1315215242", b.toString());
        assertEquals(Lists.of(xor, xor,
                text1, text1, text1,
                text2, text2, text2,
                xor, xor),
                visited,
                "visited");
    }

    // toBoolean.....................................................................................

    @Test
    public void testEvaluateToBooleanBooleanBooleanTrue() {
        this.evaluateAndCheckBoolean(this.createExpressionNode(booleanValue(false), booleanValue(true)), true);
    }

    @Test
    public void testEvaluateToBooleanBooleanBooleanTrue2() {
        this.evaluateAndCheckBoolean(this.createExpressionNode(booleanValue(true), booleanValue(false)), true);
    }

    @Test
    public void testEvaluateToBooleanBooleanBooleanFalse() {
        this.evaluateAndCheckBoolean(this.createExpressionNode(booleanValue(false), booleanValue(false)), false);
    }

    @Test
    public void testEvaluateToBooleanBooleanBooleanFalse2() {
        this.evaluateAndCheckBoolean(this.createExpressionNode(booleanValue(true), booleanValue(true)), false);
    }

    @Test
    public void testEvaluateToBooleanLongLongTrue() {
        // left ^ right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(7), longValue(3)), true);
    }

    @Test
    public void testEvaluateToBooleanLongLongFalse() {
        // left ^ right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(longValue(8), longValue(8)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigIntegerTrue() {
        // left ^ right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigInteger(7), bigInteger(3)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigIntegerFalse() {
        // left ^ right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigInteger(8), bigInteger(8)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalTrue() {
        // left ^ right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(7), bigDecimal(3)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigDecimalFalse() {
        // left ^ right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(8), bigDecimal(8)), false);
    }

    // toBigDecimal.....................................................................................

    @Test
    public void testEvaluateToBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(6), bigDecimal(3)), 6 ^ 3);
    }

    @Test
    public void testEvaluateToBigDecimal2() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(6), bigInteger(3)), 6 ^ 3);
    }

    @Test
    public void testEvaluateToBigDecimal3() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(6), doubleValue(3)), 6 ^ 3);
    }

    @Test
    public void testEvaluateToBigDecimal4() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(6), longValue(3)), 6 ^ 3);
    }

    // toBigInteger.....................................................................................

    @Test
    public void testEvaluateToBigInteger() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigInteger(6), bigDecimal(3)), 6 ^ 3);
    }

    @Test
    public void testEvaluateToBigInteger2() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(bigInteger(6), bigInteger(3)), 6 ^ 3);
    }

    @Test
    public void testEvaluateToBigInteger3() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigInteger(6), doubleValue(3)), 6 ^ 3);
    }

    @Test
    public void testEvaluateToBigInteger4() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(bigInteger(6), longValue(3)), 6 ^ 3);
    }

    // toDouble.....................................................................................

    @Test
    public void testEvaluateToDouble() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(doubleValue(6), bigDecimal(3)), 6 ^ 3);
    }

    @Test
    public void testEvaluateToDouble2() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(doubleValue(6), bigInteger(3)), 6 ^ 3);
    }

    @Test
    public void testEvaluateToDouble3() {
        this.evaluateAndCheckLong(this.createExpressionNode(doubleValue(6), doubleValue(3)), 6 ^ 3);
    }

    @Test
    public void testEvaluateToDouble4() {
        this.evaluateAndCheckLong(this.createExpressionNode(doubleValue(6), longValue(3)), 6 ^ 3);
    }

    // toLong...............................................................................................

    @Test
    public void testEvaluateToLong() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(longValue(6), bigDecimal(3)), 6L ^ 3L);
    }

    @Test
    public void testEvaluateToLong2() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(longValue(6), bigInteger(3)), 6L ^ 3L);
    }

    @Test
    public void testEvaluateToLong3() {
        this.evaluateAndCheckLong(this.createExpressionNode(longValue(6), doubleValue(3)), 6L ^ 3);
    }

    @Test
    public void testEvaluateToLong4() {
        this.evaluateAndCheckLong(this.createExpressionNode(longValue(6), longValue(3)), 6L ^ 3L);
    }

    // toNumber...............................................................................................

    @Test
    public void testEvaluateToNumber() {
        this.evaluateAndCheckNumberBigInteger(this.createExpressionNode(longValue(6), bigDecimal(3)), 6 ^ 3);
    }

    @Test
    public void testEvaluateToNumber2() {
        this.evaluateAndCheckNumberBigInteger(this.createExpressionNode(longValue(6), bigInteger(3)), 6 ^ 3);
    }

    @Test
    public void testEvaluateToNumber3() {
        this.evaluateAndCheckNumberBigInteger(this.createExpressionNode(longValue(6), doubleValue(3)), 6 ^ 3);
    }

    @Test
    public void testEvaluateToNumber4() {
        this.evaluateAndCheckNumberLong(this.createExpressionNode(longValue(6), longValue(3)), 6 ^ 3);
    }

    // toValue.....................................................................................

    @Test
    public void testEvaluateToValueBooleanBooleanTrue() {
        this.evaluateAndCheckValue(this.createExpressionNode(booleanValue(false), booleanValue(true)), true);
    }

    @Test
    public void testEvaluateToValueBooleanBooleanTrue2() {
        this.evaluateAndCheckValue(this.createExpressionNode(booleanValue(true), booleanValue(false)), true);
    }

    @Test
    public void testEvaluateToValueBooleanBooleanFalse() {
        this.evaluateAndCheckValue(this.createExpressionNode(booleanValue(false), booleanValue(false)), false);
    }

    @Test
    public void testEvaluateToValueBooleanBooleanFalse2() {
        this.evaluateAndCheckValue(this.createExpressionNode(booleanValue(true), booleanValue(true)), false);
    }

    @Test
    public void testEvaluateToValueLongLong() {
        this.evaluateAndCheckValue(this.createExpressionNode(longValue(6), longValue(3)), 6L ^ 3L);
    }

    @Test
    public void testEvaluateToValueBigIntegerBigInteger() {
        this.evaluateAndCheckValue(this.createExpressionNode(bigInteger(6), bigInteger(3)), BigInteger.valueOf(6 ^ 3));
    }

    @Override
    ExpressionXorNode createExpressionNode(final ExpressionNode left, final ExpressionNode right) {
        return ExpressionXorNode.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + "^" + RIGHT_TO_STRING;
    }

    @Override
    Class<ExpressionXorNode> expressionNodeType() {
        return ExpressionXorNode.class;
    }
}
