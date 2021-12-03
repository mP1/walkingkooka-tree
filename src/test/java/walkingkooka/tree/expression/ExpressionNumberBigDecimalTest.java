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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberBigDecimalTest extends ExpressionNumberTestCase<ExpressionNumberBigDecimal> {

    @Test
    public void testWithNullBigDecimalFails() {
        assertThrows(NullPointerException.class, () -> ExpressionNumberBigDecimal.withBigDecimal(null));
    }

    @Override
    @Test
    public void setKindDifferent() {
        final double value = 1.5;
        final ExpressionNumberBigDecimal number = this.create(value);
        final ExpressionNumberDouble different = (ExpressionNumberDouble)number.setKind(ExpressionNumberKind.DOUBLE);
        assertNotSame(number, different);
        this.checkEquals(value, different.doubleValue());
    }

    // hashCode.........................................................................................................

    @Test
    public void testHashCodeEqualsLongDouble() {
        final BigDecimal bigDecimalLong = BigDecimal.valueOf(1L);
        final BigDecimal bigDecimalDouble = BigDecimal.valueOf(1.0);
        this.checkNotEquals(bigDecimalLong, bigDecimalDouble, "should not be equal");
        this.checkNotEquals(bigDecimalLong.hashCode(), bigDecimalDouble.hashCode(), "hashCodes should be different because of scale");

        this.checkEquals(ExpressionNumber.with(bigDecimalLong), ExpressionNumber.with(bigDecimalDouble));
    }

    @Test
    public void testHashCodeEqualsDifferentScale() {
        final BigDecimal bigDecimalDouble1 = new BigDecimal("1.0");
        final BigDecimal bigDecimalDouble2 = new BigDecimal("1.00000");
        this.checkNotEquals(bigDecimalDouble1, bigDecimalDouble2, "should not be equal");
        this.checkNotEquals(bigDecimalDouble1.hashCode(), bigDecimalDouble2.hashCode(), "hashCodes should be different because of scale");

        this.checkEquals(ExpressionNumber.with(bigDecimalDouble1), ExpressionNumber.with(bigDecimalDouble2));
    }

    // comparable......................................................................................................

    @Test
    public void testCompareBigDecimalEquals() {
        this.compareToAndCheckEquals(ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.ONE),
                ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.ONE));
    }

    @Test
    public void testCompareDoubleEquals() {
        this.compareToAndCheckEquals(ExpressionNumberDouble.withDouble(1),
                ExpressionNumberDouble.withDouble(1));
    }

    @Test
    public void testCompareBigDecimalLess() {
        this.compareToAndCheckLess(ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.ONE),
                ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.valueOf(2)));
    }

    @Test
    public void testCompareDoubleLess() {
        this.compareToAndCheckLess(ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.ONE),
                ExpressionNumberDouble.withDouble(2));
    }

    // bigDecimal.......................................................................................................

    @Test
    public final void testBigDecimal2() {
        final BigDecimal value = BigDecimal.valueOf(12.5);
        this.checkEquals(value, ExpressionNumberBigDecimal.withBigDecimal(value).bigDecimal());
    }

    // toString.........................................................................................................

    @Test
    public void testToStringManyTrailingZeroes() {
        this.toStringAndCheck2("1.0000", "1");
    }

    @Test
    public void testToStringTrailingZero2() {
        this.toStringAndCheck2("1.50", "1.5");
    }

    @Test
    public void testToStringTrailingZero3() {
        this.toStringAndCheck2("1.500", "1.5");
    }

    @Test
    public void testToStringTrailingZero4() {
        this.toStringAndCheck2("1.560", "1.56");
    }

    @Test
    public void testToStringIncludesTrailingZeroes() {
        this.toStringAndCheck2("1.0001", "1.0001");
    }

    @Test
    public void testToStringIncludesTrailingZeroes2() {
        this.toStringAndCheck2("1.2345", "1.2345");
    }

    private void toStringAndCheck2(final String value, final String expected) {
        this.toStringAndCheck(ExpressionNumber.with(new BigDecimal(value)), expected);
    }

    // helper...........................................................................................................

    @Override
    ExpressionNumberBigDecimal create(final double value) {
        return ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.valueOf(value));
    }

    @Override
    final void checkValue0(final double value, final ExpressionNumberBigDecimal number) {
        final BigDecimal actual = number.bigDecimal();
        this.checkEquals(BigDecimal.valueOf(value).setScale(actual.scale()), actual);
    }

    @Override
    final void toStringAndCheck2(final double value) {
        this.toStringAndCheck2(value, BigDecimal.valueOf(value).toString());
    }

    @Override
    public Class<ExpressionNumberBigDecimal> type() {
        return ExpressionNumberBigDecimal.class;
    }
}
