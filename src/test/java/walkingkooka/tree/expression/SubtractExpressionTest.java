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

import static org.junit.jupiter.api.Assertions.assertSame;

public final class SubtractExpressionTest extends ArithmeticExpressionTestCase2<SubtractExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final SubtractExpression sub = this.createExpression();
        final Expression text1 = sub.children().get(0);
        final Expression text2 = sub.children().get(1);

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
            protected Visiting startVisit(final SubtractExpression t) {
                assertSame(sub, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final SubtractExpression t) {
                assertSame(sub, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ValueExpression<?> t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(sub);
        this.checkEquals("1315215242", b.toString());
        this.checkEquals(Lists.of(sub, sub,
                        text1, text1, text1,
                        text2, text2, text2,
                        sub, sub),
                visited,
                "visited");
    }

    // toBoolean....................................................................................................

    @Test
    public void testEvaluateToBooleanTrue() {
        // left - right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(expressionNumber(12), expressionNumber(34)), true);
    }

    @Test
    public void testEvaluateToBooleanFalse() {
        // left - right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(expressionNumber(12), expressionNumber(12)), false);
    }

    // toExpressionNumber....................................................................................................

    @Test
    public void testEvaluateToExpressionNumberExpressionNumber() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(expressionNumber(12), expressionNumber(34.5)), 12 - 34.5);
    }

    @Test
    public void testEvaluateToExpressionNumberLocalDate() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(expressionNumber(12), localDate(34)), 12 - 34);
    }

    @Test
    public void testEvaluateToExpressionNumberLocalDateTime() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(expressionNumber(12), localDateTime(34)), 12 - 34);
    }

    @Test
    public void testEvaluateToExpressionNumberLocalTime() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(expressionNumber(12), localTime(34)), 12 - 34);
    }

    @Test
    public void testEvaluateToExpressionNumberText() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(expressionNumber(12), text(34)), 12 - 34);
    }

    // toLocalDate....................................................................................................

    @Test
    public void testEvaluateToLocalDateExpressionNumber() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localDate(12), expressionNumber(34)), 12L - 34L);
    }

    @Test
    public void testEvaluateToLocalDateLocalDate() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localDate(12), localDate(34)), 12L - 34L);
    }

    @Test
    public void testEvaluateToLocalDateLocalDateTime() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localDate(12), localDateTime(34)), 12L - 34L);
    }

    @Test
    public void testEvaluateToLocalDateLocalTime() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localDate(12), localTime(34)), 12L - 34L);
    }

    @Test
    public void testEvaluateToLocalDateText() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localDate(12), text(34)), 12L - 34L);
    }

    // toLocalDateTime....................................................................................................

    @Test
    public void testEvaluateToLocalDateTimeExpressionNumber() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localDateTime(12), expressionNumber(34)), 12L - 34L);
    }

    @Test
    public void testEvaluateToLocalDateTimeLocalDate() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localDateTime(12), localDate(34)), 12L - 34L);
    }

    @Test
    public void testEvaluateToLocalDateTimeLocalDateTime() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localDateTime(12), localDateTime(34)), 12L - 34L);
    }

    @Test
    public void testEvaluateToLocalDateTimeLocalTime() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localDateTime(12), localTime(34)), 12L - 34L);
    }

    @Test
    public void testEvaluateToLocalDateTimeText() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localDateTime(12), text(34)), 12L - 34L);
    }

    // toLocalTime....................................................................................................

    @Test
    public void testEvaluateToLocalTimeExpressionNumber() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localTime(12), expressionNumber(34)), 12L - 34L);
    }

    @Test
    public void testEvaluateToLocalTimeLocalDate() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localTime(12), localDate(34)), 12L - 34L);
    }

    @Test
    public void testEvaluateToLocalTimeLocalDateTime() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localTime(12), localDateTime(34)), 12L - 34L);
    }

    @Test
    public void testEvaluateToLocalTimeLocalTime() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localTime(12), localTime(34)), 12L - 34L);
    }

    @Test
    public void testEvaluateToLocalTimeText() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(localTime(12), text(34)), 12L - 34L);
    }

    @Override
    SubtractExpression createExpression(final Expression left, final Expression right) {
        return SubtractExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + "-" + RIGHT_TO_STRING;
    }

    @Override
    Class<SubtractExpression> expressionType() {
        return SubtractExpression.class;
    }
}
