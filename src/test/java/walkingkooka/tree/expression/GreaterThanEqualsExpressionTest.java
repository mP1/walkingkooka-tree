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

public final class GreaterThanEqualsExpressionTest extends BinaryComparisonExpressionTestCase<GreaterThanEqualsExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final GreaterThanEqualsExpression gte = this.createExpression();
        final Expression text1 = gte.children().get(0);
        final Expression text2 = gte.children().get(1);

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
            protected Visiting startVisit(final GreaterThanEqualsExpression t) {
                assertSame(gte, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final GreaterThanEqualsExpression t) {
                assertSame(gte, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final StringExpression t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(gte);
        assertEquals("1315215242", b.toString());
        assertEquals(Lists.of(gte, gte,
                text1, text1, text1,
                text2, text2, text2,
                gte, gte),
                visited);
    }

    // BigDecimal ................................................................................................

    @Test
    public void testEvaluateToBooleanBigDecimalBigDecimal() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigDecimal(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigDecimal2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigDecimal(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigDecimal3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigDecimal(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigInteger() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigInteger(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigInteger2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigInteger(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigInteger3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigInteger(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalDouble() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), doubleValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalDouble2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), doubleValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalDouble3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), doubleValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalLong() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), longValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalLong2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), longValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalLong3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), longValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalText() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), text(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalText2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), text(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalText3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), text(-99)), true);
    }

    // BigInteger................................................................................................

    @Test
    public void testEvaluateToBooleanBigIntegerBigDecimal() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigDecimal(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigDecimal2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigDecimal(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigDecimal3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigDecimal(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigInteger() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigInteger(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigInteger2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigInteger(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigInteger3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigInteger(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerDouble() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), doubleValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerDouble2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), doubleValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerDouble3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), doubleValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerLong() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), longValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerLong2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), longValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerLong3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), longValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerText() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), text(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerText2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), text(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerText3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), text(-99)), true);
    }

    // Double ................................................................................................

    @Test
    public void testEvaluateToBooleanDoubleBigDecimal() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigDecimal(34)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigDecimal2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigDecimal(12)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigDecimal3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigDecimal(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigInteger() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigInteger(34)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigInteger2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigInteger(12)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigInteger3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigInteger(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleDouble() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), doubleValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleDouble2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), doubleValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleDouble3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), doubleValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleLong() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), longValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleLong2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), longValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleLong3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), longValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleText() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), text(34)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleText2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), text(12)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleText3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), text(-99)), true);
    }

    // Long................................................................................................

    @Test
    public void testEvaluateToBooleanLongBigDecimal() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigDecimal(34)), false);
    }

    @Test
    public void testEvaluateToBooleanLongBigDecimal2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigDecimal(12)), true);
    }

    @Test
    public void testEvaluateToBooleanLongBigDecimal3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigDecimal(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanLongBigInteger() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigInteger(34)), false);
    }

    @Test
    public void testEvaluateToBooleanLongBigInteger2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigInteger(12)), true);
    }

    @Test
    public void testEvaluateToBooleanLongBigInteger3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigInteger(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanLongDouble() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), doubleValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanLongDouble2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), doubleValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanLongDouble3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), doubleValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanLongLong() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), longValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanLongLong2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), longValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanLongLong3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), longValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanLongText() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), text(34)), false);
    }

    @Test
    public void testEvaluateToBooleanLongText2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), text(12)), true);
    }

    @Test
    public void testEvaluateToBooleanLongText3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), text(-99)), true);
    }

    // Text................................................................................................

    @Test
    public void testEvaluateToBooleanTextBigDecimal() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigDecimal(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextBigDecimal2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigDecimal(12)), true);
    }

    @Test
    public void testEvaluateToBooleanTextBigDecimal3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigDecimal(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanTextBigInteger() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigInteger(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextBigInteger2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigInteger(12)), true);
    }

    @Test
    public void testEvaluateToBooleanTextBigInteger3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigInteger(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanTextDouble() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), doubleValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextDouble2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), doubleValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanTextDouble3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), doubleValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanTextLong() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), longValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextLong2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), longValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanTextLong3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), longValue(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanTextText() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), text(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextText2() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), text(12)), true);
    }

    @Test
    public void testEvaluateToBooleanTextText3() {
        // left gte right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), text(-99)), true);
    }

    @Override
    GreaterThanEqualsExpression createExpression(final Expression left, final Expression right) {
        return GreaterThanEqualsExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + ">=" + RIGHT_TO_STRING;
    }

    @Override
    Class<GreaterThanEqualsExpression> expressionType() {
        return GreaterThanEqualsExpression.class;
    }
}
