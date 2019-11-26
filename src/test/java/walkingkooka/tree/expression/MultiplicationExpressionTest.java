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

public final class MultiplicationExpressionTest extends BinaryArithmeticExpressionTestCase2<MultiplicationExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final MultiplicationExpression mul = this.createExpression();
        final Expression text1 = mul.children().get(0);
        final Expression text2 = mul.children().get(1);

        new FakeExpressionVisitor() {
            @Override
            protected Visiting startVisit(final Expression n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Expression n) {
                b.append("2");
                visited.add(n);
            }

            @Override
            protected Visiting startVisit(final MultiplicationExpression t) {
                assertSame(mul, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final MultiplicationExpression t) {
                assertSame(mul, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final StringExpression t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(mul);
        assertEquals("1315215242", b.toString());
        assertEquals(Lists.of(mul, mul,
                text1, text1, text1,
                text2, text2, text2,
                mul, mul),
                visited,
                "visited");
    }

    // toBoolean...............................................................................................

    @Test
    public void testEvaluateToBooleanTrue() {
        // left * right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigDecimal(34)), true);
    }

    @Test
    public void testEvaluateToBooleanFalse() {
        // left * right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigDecimal(0)), false);
    }

    // toBigDecimal...............................................................................................

    @Test
    public void testEvaluateToBigDecimalBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigDecimal(12), bigDecimal(34)), 12 * 34);
    }

    @Test
    public void testEvaluateToBigDecimalBigInteger() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigDecimal(12), bigInteger(34)), 12 * 34);
    }

    @Test
    public void testEvaluateToBigDecimalDouble() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigDecimal(12), doubleValue(34)), 12 * 34);
    }

    @Test
    public void testEvaluateToBigDecimalLong() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigDecimal(12), longValue(34)), 12 * 34);
    }

    @Test
    public void testEvaluateToBigDecimalText() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigDecimal(12), text(34)), 12 * 34);
    }

    // toBigInteger...............................................................................................

    @Test
    public void testEvaluateToBigIntegerBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigInteger(12), bigDecimal(34)), 12 * 34);
    }

    @Test
    public void testEvaluateToBigIntegerBigInteger() {
        this.evaluateAndCheckBigInteger(this.createExpression(bigInteger(12), bigInteger(34)), 12 * 34);
    }

    @Test
    public void testEvaluateToBigIntegerDouble() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigInteger(12), doubleValue(34)), 12 * 34);
    }

    @Test
    public void testEvaluateToBigIntegerLong() {
        this.evaluateAndCheckBigInteger(this.createExpression(bigInteger(12), longValue(34)), 12 * 34);
    }

    @Test
    public void testEvaluateToBigIntegerText() {
        this.evaluateAndCheckBigInteger(this.createExpression(bigInteger(12), text(34)), 12 * 34);
    }

    // toDouble...............................................................................................

    @Test
    public void testEvaluateToDoubleBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpression(doubleValue(12), bigDecimal(34)), 12 * 34);
    }

    @Test
    public void testEvaluateToDoubleBigInteger() {
        this.evaluateAndCheckBigDecimal(this.createExpression(doubleValue(12), bigInteger(34)), 12 * 34);
    }

    @Test
    public void testEvaluateToDoubleDouble() {
        this.evaluateAndCheckDouble(this.createExpression(doubleValue(12), doubleValue(34)), 12.0 * 34.0);
    }

    @Test
    public void testEvaluateToDoubleLong() {
        this.evaluateAndCheckDouble(this.createExpression(doubleValue(12), longValue(34)), 12.0 * 34.0);
    }

    @Test
    public void testEvaluateToDoubleText() {
        this.evaluateAndCheckDouble(this.createExpression(doubleValue(12), text(34)), 12.0 * 34.0);
    }

    // toLong...............................................................................................

    @Test
    public void testEvaluateToLongBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpression(longValue(12), bigDecimal(34)), 12 * 34);
    }

    @Test
    public void testEvaluateToLongBigInteger() {
        this.evaluateAndCheckBigInteger(this.createExpression(longValue(12), bigInteger(34)), 12 * 34);
    }

    @Test
    public void testEvaluateToLongDouble() {
        this.evaluateAndCheckDouble(this.createExpression(longValue(12), doubleValue(34)), 12L * 34.0);
    }

    @Test
    public void testEvaluateToLongLong() {
        this.evaluateAndCheckLong(this.createExpression(longValue(12), longValue(34)), 12L * 34L);
    }

    @Test
    public void testEvaluateToLongText() {
        this.evaluateAndCheckLong(this.createExpression(longValue(12), text(34)), 12L * 34L);
    }

    // toNumber.....................................................................................

    @Test
    public void testEvaluateToNumberBigDecimal() {
        this.evaluateAndCheckNumberBigDecimal(this.createExpression(bigDecimal(12), bigDecimal(34)), 12 * 34);
    }

    @Test
    public void testEvaluateToNumberBigInteger() {
        this.evaluateAndCheckNumberBigInteger(this.createExpression(bigInteger(12), bigInteger(34)), 12 * 34);
    }

    @Test
    public void testEvaluateToNumberDouble() {
        this.evaluateAndCheckNumberDouble(this.createExpression(doubleValue(12), doubleValue(34)), 12 * 34);
    }

    @Test
    public void testEvaluateToNumberLong() {
        this.evaluateAndCheckNumberLong(this.createExpression(longValue(12), longValue(34)), 12 * 34);
    }

    @Override
    MultiplicationExpression createExpression(final Expression left, final Expression right) {
        return MultiplicationExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + "*" + RIGHT_TO_STRING;
    }

    @Override
    Class<MultiplicationExpression> expressionType() {
        return MultiplicationExpression.class;
    }
}
