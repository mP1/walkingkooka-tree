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

public final class AndExpressionTest extends BinaryLogicalExpressionTestCase<AndExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final AndExpression and = this.createExpression();
        final Expression text1 = and.children().get(0);
        final Expression text2 = and.children().get(1);

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
            protected Visiting startVisit(final AndExpression t) {
                assertSame(and, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final AndExpression t) {
                assertSame(and, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final StringExpression t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(and);
        assertEquals("1315215242", b.toString());
        assertEquals(Lists.of(and, and,
                text1, text1, text1,
                text2, text2, text2,
                and, and),
                visited,
                "visited");
    }

    // toBoolean.....................................................................................

    @Test
    public void testEvaluateToBooleanBooleanBooleanTrue() {
        this.evaluateAndCheckBoolean(this.createExpression(booleanValue(true), booleanValue(true)), true);
    }

    @Test
    public void testEvaluateToBooleanBooleanBooleanFalse() {
        this.evaluateAndCheckBoolean(this.createExpression(booleanValue(true), booleanValue(false)), false);
    }

    @Test
    public void testEvaluateToBooleanLongLongTrue() {
        // left & right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(7), longValue(3)), true);
    }

    @Test
    public void testEvaluateToBooleanLongLongFalse() {
        // left & right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(8), longValue(3)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigIntegerTrue() {
        // left & right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(7), bigInteger(3)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigIntegerFalse() {
        // left & right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(8), bigInteger(3)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigDecimalTrue() {
        // left & right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(7), bigDecimal(3)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigDecimalFalse() {
        // left & right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(8), bigDecimal(3)), false);
    }

    // toBigDecimal.....................................................................................

    @Test
    public void testEvaluateToBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigDecimal(6), bigDecimal(3)), 6 & 3);
    }

    @Test
    public void testEvaluateToBigDecimal2() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigDecimal(6), bigInteger(3)), 6 & 3);
    }

    @Test
    public void testEvaluateToBigDecimal3() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigDecimal(6), doubleValue(3)), 6 & 3);
    }

    @Test
    public void testEvaluateToBigDecimal4() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigDecimal(6), longValue(3)), 6 & 3);
    }

    // toBigInteger.....................................................................................

    @Test
    public void testEvaluateToBigInteger() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigInteger(6), bigDecimal(3)), 6 & 3);
    }

    @Test
    public void testEvaluateToBigInteger2() {
        this.evaluateAndCheckBigInteger(this.createExpression(bigInteger(6), bigInteger(3)), 6 & 3);
    }

    @Test
    public void testEvaluateToBigInteger3() {
        this.evaluateAndCheckBigDecimal(this.createExpression(bigInteger(6), doubleValue(3)), 6 & 3);
    }

    @Test
    public void testEvaluateToBigInteger4() {
        this.evaluateAndCheckBigInteger(this.createExpression(bigInteger(6), longValue(3)), 6 & 3);
    }

    // toDouble.....................................................................................

    @Test
    public void testEvaluateToDouble() {
        this.evaluateAndCheckBigDecimal(this.createExpression(doubleValue(6), bigDecimal(3)), 6 & 3);
    }

    @Test
    public void testEvaluateToDouble2() {
        this.evaluateAndCheckBigDecimal(this.createExpression(doubleValue(6), bigInteger(3)), 6 & 3);
    }

    @Test
    public void testEvaluateToDouble3() {
        this.evaluateAndCheckLong(this.createExpression(doubleValue(6), doubleValue(3)), 6 & 3);
    }

    @Test
    public void testEvaluateToDouble4() {
        this.evaluateAndCheckLong(this.createExpression(doubleValue(6), longValue(3)), 6 & 3);
    }

    // toLong...............................................................................................

    @Test
    public void testEvaluateToLong() {
        this.evaluateAndCheckBigDecimal(this.createExpression(longValue(6), bigDecimal(3)), 6L & 3L);
    }

    @Test
    public void testEvaluateToLong2() {
        this.evaluateAndCheckBigInteger(this.createExpression(longValue(6), bigInteger(3)), 6L & 3L);
    }

    @Test
    public void testEvaluateToLong3() {
        this.evaluateAndCheckLong(this.createExpression(longValue(6), doubleValue(3)), 6L & 3);
    }

    @Test
    public void testEvaluateToLong4() {
        this.evaluateAndCheckLong(this.createExpression(longValue(6), longValue(3)), 6L & 3L);
    }

    // toNumber...............................................................................................

    @Test
    public void testEvaluateToNumber() {
        this.evaluateAndCheckNumberBigInteger(this.createExpression(bigDecimal(6), bigDecimal(3)), 6 & 3);
    }

    @Test
    public void testEvaluateToNumber2() {
        this.evaluateAndCheckNumberBigInteger(this.createExpression(bigInteger(6), bigInteger(3)), 6 & 3);
    }

    @Test
    public void testEvaluateToNumber3() {
        this.evaluateAndCheckNumberBigInteger(this.createExpression(doubleValue(6), doubleValue(3)), 6 & 3);
    }

    @Test
    public void testEvaluateToNumber4() {
        this.evaluateAndCheckNumberLong(this.createExpression(longValue(6), longValue(3)), 6 & 3);
    }

    // toValue...............................................................................................

    @Test
    public void testEvaluateToValueBooleanBooleanTrue() {
        this.evaluateAndCheckValue(this.createExpression(booleanValue(true), booleanValue(true)), true);
    }

    @Test
    public void testEvaluateToValueBooleanBooleanFalse() {
        this.evaluateAndCheckValue(this.createExpression(booleanValue(true), booleanValue(false)), false);
    }

    @Test
    public void testEvaluateToValueLongLong() {
        this.evaluateAndCheckValue(this.createExpression(longValue(6), longValue(3)), 6L & 3L);
    }

    @Test
    public void testEvaluateToValueBigIntegerBigInteger() {
        this.evaluateAndCheckValue(this.createExpression(bigInteger(6), bigInteger(3)), BigInteger.valueOf(6 & 3));
    }

    @Override
    AndExpression createExpression(final Expression left, final Expression right) {
        return AndExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + "&" + RIGHT_TO_STRING;
    }

    @Override
    Class<AndExpression> expressionType() {
        return AndExpression.class;
    }
}
