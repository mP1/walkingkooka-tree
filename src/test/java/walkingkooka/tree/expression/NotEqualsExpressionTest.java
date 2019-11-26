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

public final class NotEqualsExpressionTest extends BinaryComparisonExpressionTestCase<NotEqualsExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final NotEqualsExpression ne = this.createExpression();
        final Expression text1 = ne.children().get(0);
        final Expression text2 = ne.children().get(1);

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
            protected Visiting startVisit(final NotEqualsExpression t) {
                assertSame(ne, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NotEqualsExpression t) {
                assertSame(ne, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final StringExpression t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(ne);
        assertEquals("1315215242", b.toString());
        assertEquals(Lists.of(ne, ne,
                text1, text1, text1,
                text2, text2, text2,
                ne, ne),
                visited,
                "visited");
    }

    // BigDecimal ................................................................................................

    @Test
    public void testEvaluateToBooleanBigDecimalBigDecimal() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigDecimal(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigDecimal2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigDecimal(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigInteger() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigInteger(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigInteger2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigInteger(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalDouble() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), doubleValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalDouble2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), doubleValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalLong() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), longValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalLong2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), longValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalText() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), text(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalText2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), text(12)), false);
    }

    // BigInteger ................................................................................................

    @Test
    public void testEvaluateToBooleanBigIntegerBigDecimal() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigDecimal(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigDecimal2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigDecimal(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigInteger() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigInteger(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigInteger2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigInteger(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerDouble() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), doubleValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerDouble2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), doubleValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerLong() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), longValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerLong2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), longValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerText() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), text(34)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerText2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), text(12)), false);
    }
    // Double  ................................................................................................

    @Test
    public void testEvaluateToBooleanDoubleBigDecimal() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigDecimal(34)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigDecimal2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigDecimal(12)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigInteger() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigInteger(34)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigInteger2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigInteger(12)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleDouble() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), doubleValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleDouble2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), doubleValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleLong() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), longValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleLong2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), longValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleText() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), text(34)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleText2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), text(12)), false);
    }
    // Long ....................................................................................................

    @Test
    public void testEvaluateToBooleanLongBigDecimal() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigDecimal(34)), true);
    }

    @Test
    public void testEvaluateToBooleanLongBigDecimal2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigDecimal(12)), false);
    }

    @Test
    public void testEvaluateToBooleanLongBigInteger() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigInteger(34)), true);
    }

    @Test
    public void testEvaluateToBooleanLongBigInteger2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigInteger(12)), false);
    }

    @Test
    public void testEvaluateToBooleanLongDouble() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), doubleValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanLongDouble2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), doubleValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanLongLong() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), longValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanLongLong2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), longValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanLongText() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), text(34)), true);
    }

    @Test
    public void testEvaluateToBooleanLongText2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), text(12)), false);
    }

    // Text ....................................................................................................

    @Test
    public void testEvaluateToBooleanTextBigDecimal() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigDecimal(34)), true);
    }

    @Test
    public void testEvaluateToBooleanTextBigDecimal2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigDecimal(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextBigInteger() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigInteger(34)), true);
    }

    @Test
    public void testEvaluateToBooleanTextBigInteger2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigInteger(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextDouble() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), doubleValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanTextDouble2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), doubleValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextLong() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), longValue(34)), true);
    }

    @Test
    public void testEvaluateToBooleanTextLong2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), longValue(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextText() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), text(34)), true);
    }

    @Test
    public void testEvaluateToBooleanTextText2() {
        // left ne right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), text(12)), false);
    }

    @Override
    NotEqualsExpression createExpression(final Expression left, final Expression right) {
        return NotEqualsExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + "!=" + RIGHT_TO_STRING;
    }

    @Override
    Class<NotEqualsExpression> expressionType() {
        return NotEqualsExpression.class;
    }
}
