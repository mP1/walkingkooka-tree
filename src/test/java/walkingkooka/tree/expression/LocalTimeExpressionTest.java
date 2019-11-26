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
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.visit.Visiting;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class LocalTimeExpressionTest extends LeafExpressionTestCase<LocalTimeExpression, LocalTime> {

    private final static String TIME_STRING = "12:59:00";
    private final static String DIFFERENT_TIME_STRING = "06:00";
    private final static long VALUE = Converters.localTimeNumber()
            .convertOrFail(LocalTime.parse(TIME_STRING),
                    Long.class,
                    ConverterContexts.fake());

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final LocalTimeExpression node = this.createExpression();

        new FakeExpressionVisitor() {
            @Override
            protected Visiting startVisit(final Expression n) {
                assertSame(node, n);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Expression n) {
                assertSame(node, n);
                b.append("2");
            }

            @Override
            protected void visit(final LocalTimeExpression n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        assertEquals("132", b.toString());
    }

    // Evaluation ...................................................................................................

    @Test
    public void testToBooleanFalse() {
        this.evaluateAndCheckBoolean(this.createExpression(0), false);
    }

    @Test
    public void testToBooleanTrue() {
        this.evaluateAndCheckBoolean(this.createExpression(1), true);
    }

    @Test
    public void testToBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpression(), VALUE);
    }

    @Test
    public void testToBigInteger() {
        this.evaluateAndCheckBigInteger(this.createExpression(), VALUE);
    }

    @Test
    public void testToDouble() {
        this.evaluateAndCheckDouble(this.createExpression(), VALUE);
    }

    @Test
    public void testToLocalDate() {
        assertThrows(ExpressionEvaluationException.class, () -> this.createExpression().toLocalDate(context()));
    }

    @Test
    public void testToLocalDateTime() {
        this.evaluateAndCheckLocalDateTime(this.createExpression(),
                Converters.localTimeLocalDateTime()
                        .convertOrFail(this.value(), LocalDateTime.class, ConverterContexts.fake()));
    }

    @Test
    public void testToLocalTime() {
        this.evaluateAndCheckLocalTime(this.createExpression(), this.value());
    }

    @Test
    public void testToLong() {
        this.evaluateAndCheckLong(this.createExpression(), VALUE);
    }

    @Test
    public void testToText() {
        this.evaluateAndCheckText(this.createExpression(), TIME_STRING);
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createExpression(), "12:59");
    }

    @Test
    public void testToString2() {
        this.toStringAndCheck(this.createExpression(LocalTime.parse(DIFFERENT_TIME_STRING)), DIFFERENT_TIME_STRING);
    }

    private LocalTimeExpression createExpression(final long value) {
        return this.localTime(value);
    }

    @Override
    LocalTimeExpression createExpression(final LocalTime value) {
        return LocalTimeExpression.with(value);
    }

    @Override
    LocalTime value() {
        return LocalTime.parse(TIME_STRING);
    }

    @Override
    LocalTime differentValue() {
        return LocalTime.parse(DIFFERENT_TIME_STRING);
    }

    @Override
    Class<LocalTimeExpression> expressionType() {
        return LocalTimeExpression.class;
    }
}
