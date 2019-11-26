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

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class LocalDateExpressionTest extends LeafExpressionTestCase<LocalDateExpression, LocalDate> {

    private final static int VALUE = 123;
    private final String DATE_STRING = "2000-01-02";
    private final String DIFFERENT_DATE_STRING = "1999-12-31";

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final LocalDateExpression node = this.createExpression();

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
            protected void visit(final LocalDateExpression n) {
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
        this.evaluateAndCheckLocalDate(this.createExpression(), this.value());
    }

    @Test
    public void testToLocalDateTime() {
        this.evaluateAndCheckLocalDateTime(this.createExpression(),
                Converters.localDateLocalDateTime()
                        .convertOrFail(this.value(), LocalDateTime.class, ConverterContexts.fake()));
    }

    @Test
    public void testToLocalTime() {
        assertThrows(ExpressionEvaluationException.class, () -> this.createExpression().toLocalTime(context()));
    }

    @Test
    public void testToLong() {
        this.evaluateAndCheckLong(this.createExpression(), VALUE);
    }

    @Test
    public void testToText() {
        this.evaluateAndCheckText(this.createExpression(DATE_STRING), DATE_STRING);
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createExpression(DATE_STRING), DATE_STRING);
    }

    @Test
    public void testToString2() {
        this.toStringAndCheck(this.createExpression(LocalDate.parse(DIFFERENT_DATE_STRING)), DIFFERENT_DATE_STRING);
    }

    private LocalDateExpression createExpression(final String value) {
        return this.createExpression(LocalDate.parse(value));
    }

    private LocalDateExpression createExpression(final long value) {
        return this.localDate(value);
    }

    @Override
    LocalDateExpression createExpression(final LocalDate value) {
        return LocalDateExpression.with(value);
    }

    @Override
    LocalDate value() {
        return LocalDate.ofEpochDay(VALUE);
    }

    @Override
    LocalDate differentValue() {
        return LocalDate.parse(DIFFERENT_DATE_STRING);
    }

    @Override
    Class<LocalDateExpression> expressionType() {
        return LocalDateExpression.class;
    }
}
