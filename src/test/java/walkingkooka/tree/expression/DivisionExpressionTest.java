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

public final class DivisionExpressionTest extends BinaryArithmeticExpressionTestCase2<DivisionExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final DivisionExpression division = this.createExpression();
        final Expression text1 = division.children().get(0);
        final Expression text2 = division.children().get(1);

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
            protected Visiting startVisit(final DivisionExpression t) {
                assertSame(division, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final DivisionExpression t) {
                assertSame(division, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final StringExpression t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(division);
        assertEquals("1315215242", b.toString());
        assertEquals(Lists.of(division, division,
                text1, text1, text1,
                text2, text2, text2,
                division, division),
                visited,
                "visited");
    }

    // toBoolean...............................................................................................

    @Test
    public void testEvaluateToBooleanTrue() {
        // left / right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigDecimal(34)), true);
    }

    @Test
    public void testEvaluateToBooleanFalse() {
        // left / right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(0), bigDecimal(-12)), false);
    }

    // toBigDecimal...............................................................................................

    @Test
    public void testEvaluateToBigDecimalBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigDecimal(50), bigDecimal(0.5)), 50 / 0.5);
    }

    @Test
    public void testEvaluateToBigDecimalBigInteger() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigDecimal(50), bigInteger(10)), 50.0 / 10);
    }

    @Test
    public void testEvaluateToBigDecimalDouble() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigDecimal(50), doubleValue(0.5)), 50 / 0.5);
    }

    @Test
    public void testEvaluateToBigDecimalLong() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigDecimal(50), longValue(5)), 50.0 / 5);
    }


    @Test
    public void testEvaluateToBigDecimalText() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigDecimal(50), text(5)), 50.0 / 5);
    }

    // toBigInteger...............................................................................................

    @Test
    public void testEvaluateToBigIntegerBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigInteger(50), bigDecimal(0.5)), 50 / 0.5);
    }

    @Test
    public void testEvaluateToBigIntegerBigInteger() {
        this.evaluateAndCheckBigInteger(this.createExpression(bigInteger(50), bigInteger(10)), 50 / 10);
    }

    @Test
    public void testEvaluateToBigIntegerDouble() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigInteger(50), doubleValue(0.5)), 50 / 0.5);
    }

    @Test
    public void testEvaluateToBigIntegerLong() {
        this.evaluateAndCheckBigInteger(this.createExpression(bigInteger(50), longValue(10)), 50 / 10);
    }

    @Test
    public void testEvaluateToBigIntegerText() {
        this.evaluateAndCheckBigInteger(this.createExpression(bigInteger(50), text(10)), 50 / 10);
    }

    // toDouble...............................................................................................

    @Test
    public void testEvaluateToDoubleBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpression(doubleValue(50), bigDecimal(0.5)), 50.0 / 0.5);
    }

    @Test
    public void testEvaluateToDoubleBigInteger() {
        this.evaluateAndCheckBigDecimal(this.createExpression(doubleValue(50), bigInteger(5)), 50.0 / 5);
    }

    @Test
    public void testEvaluateToDoubleDouble() {
        this.evaluateAndCheckDouble(this.createExpression(doubleValue(50), doubleValue(0.5)), 50.0 / 0.5);
    }

    @Test
    public void testEvaluateToDoubleLong() {
        this.evaluateAndCheckDouble(this.createExpression(doubleValue(50), longValue(5)), 50.0 / 5L);
    }

    @Test
    public void testEvaluateToDoubleText() {
        this.evaluateAndCheckDouble(this.createExpression(doubleValue(50), text(5)), 50.0 / 5L);
    }

    // toLong...............................................................................................

    @Test
    public void testEvaluateToLongBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpression(longValue(60), bigDecimal(12)), 60.0 / 12);
    }

    @Test
    public void testEvaluateToLongBigInteger() {
        this.evaluateAndCheckBigInteger(this.createExpression(longValue(60), bigInteger(12)), 60 / 12L);
    }

    @Test
    public void testEvaluateToLongDouble() {
        this.evaluateAndCheckDouble(this.createExpression(longValue(60), doubleValue(12)), 60L / 12.0);
    }

    @Test
    public void testEvaluateToLongLong() {
        this.evaluateAndCheckLong(this.createExpression(longValue(60), longValue(12)), 60L / 12L);
    }

    @Test
    public void testEvaluateToLongText() {
        this.evaluateAndCheckLong(this.createExpression(longValue(60), text(12)), 60L / 12L);
    }

    // toNumber.....................................................................................

    @Test
    public void testEvaluateToNumberBigDecimal() {
        this.evaluateAndCheckNumberBigDecimal(this.createExpression(bigDecimal(60), bigDecimal(5)), 60.0 / 5);
    }

    @Test
    public void testEvaluateToNumberBigInteger() {
        this.evaluateAndCheckNumberBigInteger(this.createExpression(bigInteger(60), bigInteger(5)), 60 / 5);
    }

    @Test
    public void testEvaluateToNumberDouble() {
        this.evaluateAndCheckNumberDouble(this.createExpression(doubleValue(60), doubleValue(5)), 60.0 / 5);
    }

    @Test
    public void testEvaluateToNumberLong() {
        this.evaluateAndCheckNumberLong(this.createExpression(longValue(60), longValue(5)), 60 / 5);
    }

    @Override
    DivisionExpression createExpression(final Expression left, final Expression right) {
        return DivisionExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + "/" + RIGHT_TO_STRING;
    }

    @Override
    Class<DivisionExpression> expressionType() {
        return DivisionExpression.class;
    }
}
