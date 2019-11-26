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

import java.math.BigDecimal;
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
        assertThrows(IllegalArgumentException.class, () -> Expression.valueOrFail(this));
    }

    @Test
    public void testValueOrFailBigInteger() {
        this.valueOrFailAndCheck(BigInteger.valueOf(123), BigIntegerExpression.class);
    }

    @Test
    public void testValueOrFailBigDecimal() {
        this.valueOrFailAndCheck(BigDecimal.valueOf(123.5), BigDecimalExpression.class);
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
        this.valueOrFailAndCheck(123.5f, DoubleExpression.class, 123.5);
    }

    @Test
    public void testValueOrFailDouble() {
        this.valueOrFailAndCheck(123.5, DoubleExpression.class);
    }

    @Test
    public void testValueOrFailByte() {
        this.valueOrFailAndCheck((byte) 123, LongExpression.class, 123L);
    }

    @Test
    public void testValueOrFailShort() {
        this.valueOrFailAndCheck((short) 123, LongExpression.class, 123L);
    }

    @Test
    public void testValueOrFailInteger() {
        this.valueOrFailAndCheck(123, LongExpression.class, 123L);
    }

    @Test
    public void testValueOrFailLong() {
        this.valueOrFailAndCheck(123L, LongExpression.class);
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

    private void valueOrFailAndCheck(final Object value, final Class<? extends ValueExpression> type) {
        valueOrFailAndCheck(value, type, value);
    }

    private void valueOrFailAndCheck(final Object value, final Class<? extends ValueExpression> type, final Object expected) {
        final Expression node = Expression.valueOrFail(value);
        assertEquals(type, node.getClass(), "node reflect of " + value);
        assertEquals(expected, type.cast(node).value(), "value");
    }

    @Override
    public Class<Expression> type() {
        return Expression.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
