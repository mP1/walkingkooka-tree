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

public final class EqualsExpressionTest extends BinaryComparisonExpressionTestCase<EqualsExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final EqualsExpression equals = this.createExpression();
        final Expression text1 = equals.children().get(0);
        final Expression text2 = equals.children().get(1);

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
            protected Visiting startVisit(final EqualsExpression t) {
                assertSame(equals, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final EqualsExpression t) {
                assertSame(equals, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final StringExpression t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(equals);
        assertEquals("1315215242", b.toString());
        assertEquals(Lists.of(equals, equals,
                text1, text1, text1,
                text2, text2, text2,
                equals, equals),
                visited,
                "visited");
    }

    // BigDecimal.................................................................................................

    @Test
    public void testEvaluateToBooleanBigDecimalBigDecimal() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigDecimal(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigDecimal2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigDecimal(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigInteger() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigInteger(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalBigInteger2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), bigInteger(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalDouble() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), doubleValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalDouble2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), doubleValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalLong() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), longValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalLong2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), longValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalText() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), text(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigDecimalText2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigDecimal(12), text(12)), true);
    }

    // BigInteger................................................................................................

    @Test
    public void testEvaluateToBooleanBigIntegerBigDecimal() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigDecimal(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigDecimal2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigDecimal(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigInteger() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigInteger(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerBigInteger2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), bigInteger(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerDouble() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), doubleValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerDouble2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), doubleValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerLong() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), longValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerLong2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), longValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerText() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), text(34)), false);
    }

    @Test
    public void testEvaluateToBooleanBigIntegerText2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(bigInteger(12), text(12)), true);
    }

    // Double ................................................................................................

    @Test
    public void testEvaluateToBooleanDoubleBigDecimal() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigDecimal(34)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigDecimal2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigDecimal(12)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigInteger() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigInteger(34)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleBigInteger2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), bigInteger(12)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleDouble() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), doubleValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleDouble2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), doubleValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleLong() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), longValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleLong2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), longValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanDoubleText() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), text(34)), false);
    }

    @Test
    public void testEvaluateToBooleanDoubleText2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(doubleValue(12), text(12)), true);
    }

    // Long................................................................................................

    @Test
    public void testEvaluateToBooleanLongBigDecimal() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigDecimal(34)), false);
    }

    @Test
    public void testEvaluateToBooleanLongBigDecimal2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigDecimal(12)), true);
    }

    @Test
    public void testEvaluateToBooleanLongBigInteger() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigInteger(34)), false);
    }

    @Test
    public void testEvaluateToBooleanLongBigInteger2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), bigInteger(12)), true);
    }

    @Test
    public void testEvaluateToBooleanLongDouble() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), doubleValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanLongDouble2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), doubleValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanLongLong() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), longValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanLongLong2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), longValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanLongText() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), text(34)), false);
    }

    @Test
    public void testEvaluateToBooleanLongText2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(longValue(12), text(12)), true);
    }


    // Long................................................................................................

    @Test
    public void testEvaluateToBooleanTextBigDecimal() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigDecimal(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextBigDecimal2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigDecimal(12)), true);
    }

    @Test
    public void testEvaluateToBooleanTextBigInteger() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigInteger(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextBigInteger2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), bigInteger(12)), true);
    }

    @Test
    public void testEvaluateToBooleanTextDouble() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), doubleValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextDouble2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), doubleValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanTextLong() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), longValue(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextLong2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), longValue(12)), true);
    }

    @Test
    public void testEvaluateToBooleanTextText() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), text(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextText2() {
        // left eq right == truthy number
        this.evaluateAndCheckBoolean(this.createExpression(text(12), text(12)), true);
    }

    @Override
    EqualsExpression createExpression(final Expression left, final Expression right) {
        return EqualsExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + "=" + RIGHT_TO_STRING;
    }

    @Override
    Class<EqualsExpression> expressionType() {
        return EqualsExpression.class;
    }
}
