/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberTest implements ClassTesting<ExpressionNumber> {

    // isByteShortIntegerLong...........................................................................................

    @Test
    public void testIsByteShortIntegerLongByte() {
        isByteShortIntegerLongAndCheck(Byte.MAX_VALUE, true);
    }

    @Test
    public void testIsByteShortIntegerLongShort() {
        isByteShortIntegerLongAndCheck(Short.MAX_VALUE, true);
    }

    @Test
    public void testIsByteShortIntegerLongInteger() {
        isByteShortIntegerLongAndCheck(Integer.MAX_VALUE, true);
    }

    @Test
    public void testIsByteShortIntegerLongLong() {
        isByteShortIntegerLongAndCheck(Long.MAX_VALUE, true);
    }

    @Test
    public void testIsByteShortIntegerLongFloat() {
        isByteShortIntegerLongAndCheck(Float.MAX_VALUE, false);
    }

    @Test
    public void testIsByteShortIntegerLongDouble() {
        isByteShortIntegerLongAndCheck(Double.MAX_VALUE, false);
    }

    @Test
    public void testIsByteShortIntegerLongBigInteger() {
        isByteShortIntegerLongAndCheck(BigInteger.ONE, false);
    }

    @Test
    public void testIsByteShortIntegerLongBigDecimal() {
        isByteShortIntegerLongAndCheck(BigDecimal.ONE, false);
    }

    @Test
    public void testIsByteShortIntegerLongUnknown() {
        isByteShortIntegerLongAndCheck(new TestNumber(), false);
    }

    private void isByteShortIntegerLongAndCheck(final Object value,
                                                final boolean expected) {
        assertEquals(expected,
                ExpressionNumber.isByteShortIntegerLong(value),
                () -> "isByteShortIntegerLongAndCheck " + value.getClass().getSimpleName() + " " + value);
    }

    // isFloatDouble...........................................................................................

    @Test
    public void testIsFloatDoubleByte() {
        isFloatDoubleAndCheck(Byte.MAX_VALUE, false);
    }

    @Test
    public void testIsFloatDoubleShort() {
        isFloatDoubleAndCheck(Short.MAX_VALUE, false);
    }

    @Test
    public void testIsFloatDoubleInteger() {
        isFloatDoubleAndCheck(Integer.MAX_VALUE, false);
    }

    @Test
    public void testIsFloatDoubleLong() {
        isFloatDoubleAndCheck(Long.MAX_VALUE, false);
    }

    @Test
    public void testIsFloatDoubleFloat() {
        isFloatDoubleAndCheck(Float.MAX_VALUE, true);
    }

    @Test
    public void testIsFloatDoubleDouble() {
        isFloatDoubleAndCheck(Double.MAX_VALUE, true);
    }

    @Test
    public void testIsFloatDoubleBigInteger() {
        isFloatDoubleAndCheck(BigInteger.ONE, false);
    }

    @Test
    public void testIsFloatDoubleBigDecimal() {
        isFloatDoubleAndCheck(BigDecimal.ONE, false);
    }

    @Test
    public void testIsFloatDoubleUnknown() {
        isFloatDoubleAndCheck(new TestNumber(), false);
    }

    private void isFloatDoubleAndCheck(final Object value,
                                       final boolean expected) {
        assertEquals(expected,
                ExpressionNumber.isFloatDouble(value),
                () -> "isFloatDoubleAndCheck " + value.getClass().getSimpleName() + " " + value);
    }

    // is...........................................................................................

    @Test
    public void testIsByte() {
        isAndCheck(Byte.MAX_VALUE, true);
    }

    @Test
    public void testIsShort() {
        isAndCheck(Short.MAX_VALUE, true);
    }

    @Test
    public void testIsInteger() {
        isAndCheck(Integer.MAX_VALUE, true);
    }

    @Test
    public void testIsLong() {
        isAndCheck(Long.MAX_VALUE, true);
    }

    @Test
    public void testIsFloat() {
        isAndCheck(Float.MAX_VALUE, true);
    }

    @Test
    public void testIsDouble() {
        isAndCheck(Double.MAX_VALUE, true);
    }

    @Test
    public void testIsBigInteger() {
        isAndCheck(BigInteger.ONE, true);
    }

    @Test
    public void testIsBigDecimal() {
        isAndCheck(BigDecimal.ONE, true);
    }

    @Test
    public void testIsExpressionNumber() {
        isAndCheck(ExpressionNumber.with(1, ExpressionNumberContexts.fake()), true);
    }

    @Test
    public void testIsUnknown() {
        isAndCheck(new TestNumber(), false);
    }

    private void isAndCheck(final Object value,
                            final boolean expected) {
        assertEquals(expected,
                ExpressionNumber.is(value),
                () -> "isAndCheck " + value.getClass().getSimpleName() + " " + value);
    }

    // wider...............................................................................................................

    @Test
    public void testWidenByte() {
        widenAndCheck(Byte.MAX_VALUE, Long.class);
    }

    @Test
    public void testWidenShort() {
        widenAndCheck(Short.MAX_VALUE, Long.class);
    }

    @Test
    public void testWidenInteger() {
        widenAndCheck(Integer.MAX_VALUE, Long.class);
    }

    @Test
    public void testWidenLong() {
        widenAndCheck(Long.MAX_VALUE, Long.class);
    }

    @Test
    public void testWidenFloat() {
        widenAndCheck(Float.MAX_VALUE, Double.class);
    }

    @Test
    public void testWidenDouble() {
        widenAndCheck(Double.MAX_VALUE, Double.class);
    }

    @Test
    public void testWidenBigInteger() {
        widenAndCheck(BigInteger.ONE, BigInteger.class);
    }

    @Test
    public void testWidenBigDecimal() {
        widenAndCheck(BigDecimal.ONE, BigDecimal.class);
    }

    private void widenAndCheck(final Object value,
                               final Class<? extends Number> expected) {
        assertEquals(expected,
                ExpressionNumber.wider(value),
                () -> "widenAndCheck " + value.getClass().getSimpleName() + " " + value);
    }

    @Test
    public void testWidenUnknownFails() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionNumber.wider(new TestNumber()));
    }

    final static class TestNumber extends Number {
        private static final long serialVersionUID = 1;

        @Override
        public int intValue() {
            return 0;
        }

        @Override
        public long longValue() {
            return 0;
        }

        @Override
        public float floatValue() {
            return 0;
        }

        @Override
        public double doubleValue() {
            return 0;
        }
    }

    // helpers..........................................................................................................

    @Override
    public Class<ExpressionNumber> type() {
        return ExpressionNumber.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
