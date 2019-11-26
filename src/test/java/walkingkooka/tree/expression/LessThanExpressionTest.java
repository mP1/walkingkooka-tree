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

public final class LessThanExpressionTest extends BinaryComparisonExpressionTestCase<LessThanExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final LessThanExpression lt = this.createExpression();
        final Expression text1 = lt.children().get(0);
        final Expression text2 = lt.children().get(1);

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
            protected Visiting startVisit(final LessThanExpression t) {
                assertSame(lt, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final LessThanExpression t) {
                assertSame(lt, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final StringExpression t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(lt);
        assertEquals("1315215242", b.toString());
        assertEquals(Lists.of(lt, lt,
                text1, text1, text1,
                text2, text2, text2,
                lt, lt),
                visited,
                "visited");
    }

    // BigDecimal ................................................................................................

    @Test
    public void testEvaluateToBooleanBigDecimalBigDecimal() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigDecimal(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigDecimal2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigDecimal(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigDecimal3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigDecimal(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigInteger() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigInteger(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigInteger2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigInteger(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigInteger3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigInteger(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalDouble() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), doubleValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalDouble2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), doubleValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalDouble3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), doubleValue(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalLong() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), longValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalLong2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), longValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalLong3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), longValue(-99)), false);
    }

    // BigInteger ................................................................................................

    @Test
    public void testEvaluateToBooleanBigIntegerBigDecimal() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigDecimal(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigDecimal2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigDecimal(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigDecimal3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigDecimal(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigInteger() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigInteger(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigInteger2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigInteger(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigInteger3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigInteger(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerDouble() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), doubleValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerDouble2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), doubleValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerDouble3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), doubleValue(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerLong() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), longValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerLong2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), longValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerLong3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), longValue(-99)), false);
    }

    // Double ................................................................................................

    @Test
    public void testEvaluateToBooleanDoubleBigDecimal() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigDecimal(34)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigDecimal2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigDecimal(12)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigDecimal3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigDecimal(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigInteger() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigInteger(34)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigInteger2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigInteger(12)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigInteger3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigInteger(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleDouble() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), doubleValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleDouble2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), doubleValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleDouble3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), doubleValue(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleLong() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), longValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleLong2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), longValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleLong3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), longValue(-99)), false);
    }

    // Long ................................................................................................

    @Test
    public void testEvaluateToBooleanLongBigDecimal() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigDecimal(34)), true);
    }

    @Test
    public void testEvaluateToBooleanLongBigDecimal2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigDecimal(12)), false);
    }

    @Test
    public void testEvaluateToBooleanLongBigDecimal3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigDecimal(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanLongBigInteger() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigInteger(34)), true);
    }

    @Test
    public void testEvaluateToBooleanLongBigInteger2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigInteger(12)), false);
    }

    @Test
    public void testEvaluateToBooleanLongBigInteger3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigInteger(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanLongDouble() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), doubleValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanLongDouble2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), doubleValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanLongDouble3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), doubleValue(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanLongLong() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), longValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanLongLong2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), longValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanLongLong3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), longValue(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanLongText() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), text(34)), true);
    }

    @Test
    public void testEvaluateToBooleanLongText2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), text(12)), false);
    }

    @Test
    public void testEvaluateToBooleanLongText3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), text(-99)), false);
    }

    // Long ................................................................................................

    @Test
    public void testEvaluateToBooleanTextBigDecimal() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigDecimal(34)), true);
    }

    @Test
    public void testEvaluateToBooleanTextBigDecimal2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigDecimal(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextBigDecimal3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigDecimal(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanTextBigInteger() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigInteger(34)), true);
    }

    @Test
    public void testEvaluateToBooleanTextBigInteger2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigInteger(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextBigInteger3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigInteger(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanTextDouble() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), doubleValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanTextDouble2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), doubleValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextDouble3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), doubleValue(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanTextLong() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), longValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanTextLong2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), longValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextLong3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), longValue(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanTextText() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), text(34)), true);
    }

    @Test
    public void testEvaluateToBooleanTextText2() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), text(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextText3() {
        // left lt right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), text(-99)), false);
    }

    @Override
    LessThanExpression createExpression(final Expression left, final Expression right) {
        return LessThanExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + "<" + RIGHT_TO_STRING;
    }

    @Override
    Class<LessThanExpression> expressionType() {
        return LessThanExpression.class;
    }
}
