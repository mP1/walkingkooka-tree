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

public final class ExpressionModuloNodeTest extends ExpressionArithmeticBinaryNodeTestCase2<ExpressionModuloNode> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ExpressionNode> visited = Lists.array();

        final ExpressionModuloNode modulo = this.createExpressionNode();
        final ExpressionNode text1 = modulo.children().get(0);
        final ExpressionNode text2 = modulo.children().get(1);

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
            protected Visiting startVisit(final ExpressionModuloNode t) {
                assertSame(modulo, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionModuloNode t) {
                assertSame(modulo, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ExpressionTextNode t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(modulo);
        assertEquals("1315215242", b.toString());
        assertEquals(Lists.of(modulo, modulo,
                text1, text1, text1,
                text2, text2, text2,
                modulo, modulo),
                visited,
                "visited");
    }

    // toBoolean...............................................................................................

    @Test
    public void testEvaluateToBooleanTrue() {
        // left % right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(12), bigDecimal(34)), true);
    }

    @Test
    public void testEvaluateToBooleanFalse() {
        // left % right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(0), bigDecimal(9)), false);
    }

    // toBigDecimal...............................................................................................

    @Test
    public void testEvaluateToBigDecimalBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(12), bigDecimal(34)), 12 % 34);
    }

    @Test
    public void testEvaluateToBigDecimalBigInteger() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(12), bigInteger(34)), 12 % 34);
    }

    @Test
    public void testEvaluateToBigDecimalDouble() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(12), doubleValue(34)), 12 % 34);
    }

    @Test
    public void testEvaluateToBigDecimalLong() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(12), longValue(34)), 12 % 34);
    }

    @Test
    public void testEvaluateToBigDecimalText() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(12), text(34)), 12 % 34);
    }

    // toBigInteger...............................................................................................

    @Test
    public void testEvaluateToBigIntegerBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigInteger(12), bigDecimal(34)), 12 % 34);
    }

    @Test
    public void testEvaluateToBigIntegerBigInteger() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(bigInteger(12), bigInteger(34)), 12 % 34);
    }

    @Test
    public void testEvaluateToBigIntegerDouble() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigInteger(12), doubleValue(34)), 12 % 34);
    }

    @Test
    public void testEvaluateToBigIntegerLong() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(bigInteger(12), longValue(34)), 12 % 34);
    }

    @Test
    public void testEvaluateToBigIntegerText() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(bigInteger(12), text(34)), 12 % 34);
    }

    // toDouble...............................................................................................

    @Test
    public void testEvaluateToDoubleBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(doubleValue(12), bigDecimal(34)), 12 % 34);
    }

    @Test
    public void testEvaluateToDoubleBigInteger() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(doubleValue(12), bigInteger(34)), 12 % 34);
    }

    @Test
    public void testEvaluateToDoubleDouble() {
        this.evaluateAndCheckDouble(this.createExpressionNode(doubleValue(12), doubleValue(34)), 12.0 % 34.0);
    }

    @Test
    public void testEvaluateToDoubleLong() {
        this.evaluateAndCheckDouble(this.createExpressionNode(doubleValue(12), longValue(34)), 12.0 % 34.0);
    }

    @Test
    public void testEvaluateToDoubleText() {
        this.evaluateAndCheckDouble(this.createExpressionNode(doubleValue(12), text(34)), 12.0 % 34.0);
    }

    // toLong...............................................................................................

    @Test
    public void testEvaluateToLongBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(longValue(12), bigDecimal(34)), 12L % 34);
    }

    @Test
    public void testEvaluateToLongBigInteger() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(longValue(12), bigInteger(34)), 12L % 34L);
    }

    @Test
    public void testEvaluateToLongDouble() {
        this.evaluateAndCheckDouble(this.createExpressionNode(longValue(12), doubleValue(34)), 12L % 34.0);
    }

    @Test
    public void testEvaluateToLongLong() {
        this.evaluateAndCheckLong(this.createExpressionNode(longValue(12), longValue(34)), 12L % 34L);
    }

    @Test
    public void testEvaluateToLongText() {
        this.evaluateAndCheckLong(this.createExpressionNode(longValue(12), text(34)), 12L % 34L);
    }
    // toNumber.....................................................................................

    @Test
    public void testEvaluateToNumberBigDecimal() {
        this.evaluateAndCheckNumberBigDecimal(this.createExpressionNode(bigDecimal(12), bigDecimal(5)), 12 % 5);
    }

    @Test
    public void testEvaluateToNumberBigInteger() {
        this.evaluateAndCheckNumberBigInteger(this.createExpressionNode(bigInteger(12), bigInteger(5)), 12 % 5);
    }

    @Test
    public void testEvaluateToNumberDouble() {
        this.evaluateAndCheckNumberDouble(this.createExpressionNode(doubleValue(12), doubleValue(5)), 12 % 5);
    }

    @Test
    public void testEvaluateToNumberLong() {
        this.evaluateAndCheckNumberLong(this.createExpressionNode(longValue(12), longValue(5)), 12 % 5);
    }

    @Override
    ExpressionModuloNode createExpressionNode(final ExpressionNode left, final ExpressionNode right) {
        return ExpressionModuloNode.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + "%" + RIGHT_TO_STRING;
    }

    @Override
    Class<ExpressionModuloNode> expressionNodeType() {
        return ExpressionModuloNode.class;
    }
}
