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

public final class AddExpressionTest extends ArithmeticExpressionTestCase<AddExpression> {

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
            protected void visit(final ValueExpression<?> t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(addition);
        this.checkEquals("1315215242", b.toString());
        this.checkEquals(Lists.of(addition, addition,
                text1, text1, text1,
                text2, text2, text2,
                addition, addition),
            visited,
            "visited");
    }

    // toBoolean.....................................................................................

    @Test
    public void testToBooleanAndTrue() {
        // left + right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                expressionNumber(12),
                expressionNumber(34)
            ),
            true
        );
    }

    @Test
    public void testToBooleanAndFalse() {
        // left + right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                expressionNumber(12),
                expressionNumber(-12)
            ), false
        );
    }

    // toExpressionNumber.....................................................................................

    @Test
    public void testToExpressionNumberWithExpressionNumber() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                expressionNumber(12),
                expressionNumber(34)
            ), 12 + 34
        );
    }

    @Test
    public void testToExpressionNumberWithLocalDate() {
        this.toExpressionNumberAndCheck(
            this.createExpression(expressionNumber(12),
                localDate(34)
            ), 12 + 34
        );
    }

    @Test
    public void testToExpressionNumberWithLocalDateTime() {
        this.toExpressionNumberAndCheck(
            this.createExpression(expressionNumber(12),
                localDateTime(34)
            ), 12 + 34
        );
    }

    @Test
    public void testToExpressionNumberWithLocalTime() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                expressionNumber(12),
                localTime(34)
            ), 12 + 34
        );
    }

    @Test
    public void testToExpressionNumberText() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                expressionNumber(12),
                text("34")
            ), 12 + 34
        );
    }

    // toLocalDate......................................................................................................

    @Test
    public void testToExpressionNumberLocalDateExpressionNumber() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDate(12),
                expressionNumber(34.5)
            ), 12L + 34.5
        );
    }

    @Test
    public void testToExpressionNumberLocalDateLocalDate() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDate(12),
                localDate(34)
            ), 12L + 34
        );
    }

    @Test
    public void testToExpressionNumberLocalDateLocalDateTime() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDate(12),
                localDateTime(34)
            ), 12L + 34.0
        );
    }

    @Test
    public void testToExpressionNumberLocalDateLocalTime() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDate(12),
                localTime(34)
            ), 12L + 34
        );
    }

    @Test
    public void testToExpressionNumberLocalDateText() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDate(12),
                text(34)
            ), 12L + 34L
        );
    }

    // toLocalDateTime..................................................................................................

    @Test
    public void testToExpressionNumberWithAddLocalDateTimeAndExpressionNumber() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDateTime(12),
                expressionNumber(34.5)
            ),
            12L + 34.5
        );
    }

    @Test
    public void testToExpressionNumberWithAddLocalDateTimeAndLocalDate() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDateTime(12),
                localDate(34)
            ), 12L + 34
        );
    }

    @Test
    public void testToExpressionNumberAddLocalDateTimeAndLocalDateTime() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDateTime(12),
                localDateTime(34)
            ), 12L + 34.0
        );
    }

    @Test
    public void testToExpressionNumberWithAddLocalDateTimeAndLocalTime() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDateTime(12),
                localDateTime(34)
            ), 12L + 34
        );
    }

    @Test
    public void testToExpressionNumberAddLocalDateTimeAndText() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDateTime(12),
                text(34)
            ), 12L + 34L
        );
    }

    // toLocalTime......................................................................................................

    @Test
    public void testToExpressionNumberAddLocalTimeExpressionNumber() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localTime(12),
                expressionNumber(34.5)
            ), 12L + 34.5
        );
    }

    @Test
    public void testToExpressionNumberAddLocalTimeAndLocalDate() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localTime(12),
                localDate(34)
            ), 12L + 34
        );
    }

    @Test
    public void testToExpressionNumberAddLocalTimeAndLocalDateTime() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localTime(12),
                localDateTime(34)
            ), 12L + 34.0
        );
    }

    @Test
    public void testToExpressionNumberAddLocalTimeAndLocalTime() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localTime(12),
                localTime(34)
            ), 12L + 34
        );
    }

    // toText...........................................................................................................

    @Test
    public void testToTextWithLocalTimeText() {
        this.toTextAndCheck(
            this.createExpression(
                localTime(12),
                text(34)
            ),
            "46"
        );
    }

    @Test
    public void testToTextExpressionNumber() {
        this.toTextAndCheck(
            this.createExpression(
                text(12),
                expressionNumber(34)
            ), "1234"
        );
    }

    @Test
    public void testToTextLocalDate() {
        this.toTextAndCheck(
            this.createExpression(
                text(12),
                localDate(34)
            ), "12" + textText(localDate(34))
        );
    }

    @Test
    public void testToTextLocalDateTime() {
        this.toTextAndCheck(
            this.createExpression(
                text(12),
                localDateTime(34)
            ), "12" + textText(
                localDateTime(34)
            )
        );
    }

    @Test
    public void testToTextWithLocalTimeAndText() {
        this.toTextAndCheck(
            this.createExpression(
                text(12),
                localTime(34)
            ),
            "12" + textText(
                localTime(34)
            )
        );
    }

    @Test
    public void testToTextWithTextAndText() {
        this.toTextAndCheck(
            this.createExpression(
                text(12),
                text(34)
            ), "1234"
        );
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
