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

public final class AddExpressionTest extends BinaryArithmeticExpressionTestCase<AddExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final AddExpression addition = this.createExpression();
        final Expression text1 = addition.children().get(0);
        final Expression text2 = addition.children().get(1);

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
            protected Visiting startVisit(final AddExpression t) {
                assertSame(addition, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final AddExpression t) {
                assertSame(addition, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final StringExpression t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(addition);
        assertEquals("1315215242", b.toString());
        assertEquals(Lists.of(addition, addition,
                text1, text1, text1,
                text2, text2, text2,
                addition, addition),
                visited,
                "visited");
    }

    // toBoolean.....................................................................................

    @Test
    public void testEvaluateToBooleanTrue() {
        // left + right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(expressionNumber(12), expressionNumber(34)), true);
    }

    @Test
    public void testEvaluateToBooleanFalse() {
        // left + right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(expressionNumber(12), expressionNumber(-12)), false);
    }

    // toExpressionNumber.....................................................................................

    @Test
    public void testEvaluateToExpressionNumberExpressionNumber() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(expressionNumber(12), expressionNumber(34)), 12 + 34);
    }

    @Test
    public void testEvaluateToExpressionNumberLocalDate() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(expressionNumber(12), localDate(34)), 12 + 34);
    }

    @Test
    public void testEvaluateToExpressionNumberLocalDateTime() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(expressionNumber(12), localDateTime(34)), 12 + 34);
    }

    @Test
    public void testEvaluateToExpressionNumberLocalTime() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(expressionNumber(12), localTime(34)), 12 + 34);
    }

    @Test
    public void testEvaluateToExpressionNumberText() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(expressionNumber(12), text("34")), 12 + 34);
    }

    // toLocalDate..............................................................................................................

    @Test
    public void testEvaluateToLocalDateExpressionNumber() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localDate(12), expressionNumber(34.5)), 12L + 34.5);
    }

    @Test
    public void testEvaluateToLocalDateLocalDate() {
        this.evaluateAndCheckLocalDate(this.createExpression(localDate(12), localDate(34)), 12L + 34);
    }

    @Test
    public void testEvaluateToLocalDateLocalDateTime() {
        this.evaluateAndCheckLocalDateTime(this.createExpression(localDate(12), localDateTime(34)), 12L + 34.0);
    }

    @Test
    public void testEvaluateToLocalDateLocalTime() {
        this.evaluateAndCheckLocalTime(this.createExpression(localDate(12), localTime(34)), 12L + 34);
    }

    @Test
    public void testEvaluateToLocalDateText() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localDate(12), text(34)), 12L + 34L);
    }

    // toLocalDateTime..............................................................................................................

    @Test
    public void testEvaluateToLocalDateTimeExpressionNumber() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localDateTime(12), expressionNumber(34.5)), 12L + 34.5);
    }

    @Test
    public void testEvaluateToLocalDateTimeLocalDate() {
        this.evaluateAndCheckLocalDate(this.createExpression(localDateTime(12), localDate(34)), 12L + 34);
    }

    @Test
    public void testEvaluateToLocalDateTimeLocalDateTime() {
        this.evaluateAndCheckLocalDateTime(this.createExpression(localDateTime(12), localDateTime(34)), 12L + 34.0);
    }

    @Test
    public void testEvaluateToLocalDateTimeLocalTime() {
        this.evaluateAndCheckLocalTime(this.createExpression(localDateTime(12), localDateTime(34)), 12L + 34);
    }

    @Test
    public void testEvaluateToLocalDateTimeText() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localDateTime(12), text(34)), 12L + 34L);
    }

    // toLocalTime..............................................................................................................

    @Test
    public void testEvaluateToLocalTimeExpressionNumber() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localTime(12), expressionNumber(34.5)), 12L + 34.5);
    }

    @Test
    public void testEvaluateToLocalTimeLocalDate() {
        this.evaluateAndCheckLocalDate(this.createExpression(localTime(12), localDate(34)), 12L + 34);
    }

    @Test
    public void testEvaluateToLocalTimeLocalDateTime() {
        this.evaluateAndCheckLocalDateTime(this.createExpression(localTime(12), localDateTime(34)), 12L + 34.0);
    }

    @Test
    public void testEvaluateToLocalTimeLocalTime() {
        this.evaluateAndCheckLocalTime(this.createExpression(localTime(12), localTime(34)), 12L + 34);
    }

    @Test
    public void testEvaluateToLocalTimeText() {
        this.evaluateAndCheckText(this.createExpression(localTime(12), text(34)), "46");
    }

    // toText.....................................................................................

    @Test
    public void testEvaluateToTextExpressionNumber() {
        this.evaluateAndCheckText(this.createExpression(text(12), expressionNumber(34)), "1234");
    }

    @Test
    public void testEvaluateToTextLocalDate() {
        this.evaluateAndCheckText(this.createExpression(text(12), localDate(34)), "12" + textText(localDate(34)));
    }

    @Test
    public void testEvaluateToTextLocalDateTime() {
        this.evaluateAndCheckText(this.createExpression(text(12), localDateTime(34)), "12" + textText(localDateTime(34)));
    }

    @Test
    public void testEvaluateToTextLocalTime() {
        this.evaluateAndCheckText(this.createExpression(text(12), localTime(34)), "12" + textText(localTime(34)));
    }

    @Test
    public void testEvaluateToTextText() {
        this.evaluateAndCheckText(this.createExpression(text(12), text(34)), "1234");
    }

    // helpers.........................................................................................................

    @Override
    AddExpression createExpression(final Expression left, final Expression right) {
        return AddExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + "+" + RIGHT_TO_STRING;
    }

    @Override
    Class<AddExpression> expressionType() {
        return AddExpression.class;
    }
}
