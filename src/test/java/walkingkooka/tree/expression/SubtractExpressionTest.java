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
    public void testToBooleanTrue() {
        // left - right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                expressionNumber(12),
                expressionNumber(34)
            ), true
        );
    }

    @Test
    public void testToBooleanFalse() {
        // left - right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                expressionNumber(12),
                expressionNumber(12)
            ), false
        );
    }

    // toExpressionNumber....................................................................................................

    @Test
    public void testToExpressionNumberExpressionNumber() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                expressionNumber(12),
                expressionNumber(34.5)
            ), 12 - 34.5
        );
    }

    @Test
    public void testToExpressionNumberLocalDate() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                expressionNumber(12),
                localDate(34)
            ), 12 - 34
        );
    }

    @Test
    public void testToExpressionNumberLocalDateTime() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                expressionNumber(12),
                localDateTime(34)
            ), 12 - 34
        );
    }

    @Test
    public void testToExpressionNumberLocalTime() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                expressionNumber(12),
                localTime(0.5)
            ), 12 - 0.5
        );
    }

    @Test
    public void testToExpressionNumberText() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                expressionNumber(12),
                text(34)
            ), 12 - 34
        );
    }

    // toLocalDate.......................................................................................................

    @Test
    public void testExpressionNumberWithLocalDate() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDate(12),
                expressionNumber(34)
            ), 12L - 34L
        );
    }

    @Test
    public void testToExpressionNumberLocalDateLocalDate() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDate(12),
                localDate(34)
            ), 12L - 34L
        );
    }

    @Test
    public void testToExpressionNumberLocalDateLocalDateTime() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDate(12),
                localDateTime(34)
            ), 12L - 34L
        );
    }

    @Test
    public void testToExpressionNumberLocalDateLocalTime() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDate(12)
                , localTime(0.5)
            ), 12L - 0.5
        );
    }

    @Test
    public void testToExpressionNumberLocalDateText() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDate(12),
                text(34)
            ), 12L - 34L
        );
    }

    // toLocalDateTime..................................................................................................

    @Test
    public void testToExpressionNumberLocalDateTimeExpressionNumber() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDateTime(12),
                expressionNumber(34)
            ), 12L - 34L
        );
    }

    @Test
    public void testToExpressionNumberLocalDateTimeLocalDate() {
        this.toExpressionNumberAndCheck(this.createExpression(localDateTime(12), localDate(34)), 12L - 34L);
    }

    @Test
    public void testToExpressionNumberLocalDateTimeLocalDateTime() {
        this.toExpressionNumberAndCheck(this.createExpression(localDateTime(12), localDateTime(34)), 12L - 34L);
    }

    @Test
    public void testToExpressionNumberLocalDateTimeLocalTime() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDateTime(12),
                localTime(0.5)
            ), 12L - 0.5
        );
    }

    @Test
    public void testToExpressionNumberLocalDateTimeText() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localDateTime(12),
                text(34)
            ), 12L - 34L
        );
    }

    // toLocalTime......................................................................................................

    @Test
    public void testToExpressionNumberLocalTimeExpressionNumber() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localTime(0.5),
                expressionNumber(34)
            ), 0.5 - 34L
        );
    }

    @Test
    public void testToExpressionNumberLocalTimeLocalDate() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localTime(0.5),
                localDate(34)
            ), 0.5 - 34L
        );
    }

    @Test
    public void testToExpressionNumberLocalTimeLocalDateTime() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localTime(0.5),
                localDateTime(34)
            ), 0.5 - 34L
        );
    }

    @Test
    public void testToExpressionNumberLocalTimeLocalTime() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localTime(0.5),
                localTime(0.25)
            ), 0.5 - 0.25
        );
    }

    @Test
    public void testToExpressionNumberLocalTimeText() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                localTime(0.5),
                text(34)
            ), 0.5 - 34L
        );
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
