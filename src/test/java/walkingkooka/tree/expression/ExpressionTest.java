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
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CharSequences;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.Printers;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionTest implements ClassTesting2<Expression> {

    @Test
    public void testValueOrFailNullFails() {
        assertThrows(NullPointerException.class, () -> Expression.valueOrFail(null));
    }

    @Test
    public void testValueOrFailUnknownValueTypeFails() {
        valueOrFailFails(this);
    }

    @Test
    public void testValueOrFailBigIntegerFails() {
        valueOrFailFails(BigInteger.ONE);
    }

    @Test
    public void testValueOrFailBigDecimalFails() {
        valueOrFailFails(BigInteger.TEN);
    }

    @Test
    public void testValueOrFailBooleanTrue() {
        this.valueOrFailAndCheck(true, BooleanExpression.class);
    }

    @Test
    public void testValueOrFailBooleanFalse() {
        this.valueOrFailAndCheck(false, BooleanExpression.class);
    }

    @Test
    public void testValueOrFailFloat() {
        this.valueOrFailFails(123.5f);
    }

    @Test
    public void testValueOrFailDouble() {
        this.valueOrFailFails(123.5);
    }

    @Test
    public void testValueOrFailByte() {
        this.valueOrFailFails((byte) 123);
    }

    @Test
    public void testValueOrFailShort() {
        this.valueOrFailFails((short) 123);
    }

    @Test
    public void testValueOrFailInteger() {
        this.valueOrFailFails(123);
    }

    @Test
    public void testValueOrFailLong() {
        this.valueOrFailFails(123L);
    }

    @Test
    public void testValueOrFailExpressionNumber() {
        this.valueOrFailAndCheck(ExpressionNumberKind.DEFAULT.create(123));
    }

    @Test
    public void testValueOrFailLocalDate() {
        this.valueOrFailAndCheck(LocalDate.of(2000, 12, 31), LocalDateExpression.class);
    }

    @Test
    public void testValueOrFailLocalDateTime() {
        this.valueOrFailAndCheck(LocalDateTime.of(2000, 12, 31, 12, 58, 59),
                LocalDateTimeExpression.class);
    }

    @Test
    public void testValueOrFailLocalTime() {
        this.valueOrFailAndCheck(LocalTime.of(12, 58, 59), LocalTimeExpression.class);
    }

    @Test
    public void testValueOrFailText() {
        this.valueOrFailAndCheck("abc123", StringExpression.class);
    }

    private void valueOrFailFails(final Object value) {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> Expression.valueOrFail(value));
        assertEquals("Unknown value " + CharSequences.quoteIfChars(value) + "(" + value.getClass().getName() + ")",
                thrown.getMessage(),
                "message");
    }

    private void valueOrFailAndCheck(final Object value,
                                     final Class<? extends ValueExpression> type) {
        valueOrFailAndCheck(value, type, value);
    }

    private void valueOrFailAndCheck(final Number value) {
        valueOrFailAndCheck(value,
                ExpressionNumberExpression.class,
                ExpressionNumberKind.DEFAULT.create(value));
    }

    private void valueOrFailAndCheck(final Object value,
                                     final Class<? extends ValueExpression> type,
                                     final Object expected) {
        final Expression node = Expression.valueOrFail(value);
        assertEquals(type, node.getClass(), "node reflect of " + value);
        assertEquals(expected, type.cast(node).value(), "value");
    }

    // TreePrinting......................................................................................................

    @Test
    public void testTreePrint() {
        final ExpressionNumberKind kind = ExpressionNumberKind.DOUBLE;
        final Expression expression = Expression.add(
                Expression.expressionNumber(kind.create(1.5)),
                Expression.multiply(
                        Expression.string("20"),
                        Expression.booleanExpression(true)
                )
        );

        final StringBuilder printed = new StringBuilder();

        try (final IndentingPrinter printer = Printers.stringBuilder(printed, LineEnding.NL).indenting(Indentation.with("  "))) {
            expression.printTree(printer);

            printer.flush();
            assertEquals(
                    "AddExpression\n" +
                            "  ExpressionNumberExpression 1.5\n" +
                            "  MultiplyExpression\n" +
                            "    StringExpression \"20\"\n" +
                            "    BooleanExpression true\n",
                    printed.toString()
            );
        }
    }

    // ClassTesting......................................................................................................

    @Override
    public Class<Expression> type() {
        return Expression.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
