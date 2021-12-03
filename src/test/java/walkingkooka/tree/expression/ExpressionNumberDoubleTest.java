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

public final class ExpressionNumberDoubleTest extends ExpressionNumberTestCase<ExpressionNumberDouble> {

    @Override
    @Test
    public void setKindDifferent() {
        final double value = 1.5;
        final ExpressionNumberDouble number = this.create(value);
        final ExpressionNumberBigDecimal different = (ExpressionNumberBigDecimal)number.setKind(ExpressionNumberKind.BIG_DECIMAL);
        assertNotSame(number, different);
        this.checkEquals(value, different.doubleValue());
    }

    // comparable......................................................................................................

    @Test
    public void testCompareBigDecimalEquals() {
        this.compareToAndCheckEquals(ExpressionNumberDouble.withDouble(1),
                ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.ONE));
    }

    @Test
    public void testCompareDoubleEquals() {
        this.compareToAndCheckEquals(ExpressionNumberDouble.withDouble(1),
                ExpressionNumberDouble.withDouble(1));
    }

    @Test
    public void testCompareBigDecimalLess() {
        this.compareToAndCheckLess(ExpressionNumberDouble.withDouble(1),
                ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.valueOf(2)));
    }

    @Test
    public void testCompareDoubleLess() {
        this.compareToAndCheckLess(ExpressionNumberDouble.withDouble(1),
                ExpressionNumberDouble.withDouble(2));
    }

    // toString.........................................................................................................

    @Test
    public void testToStringNaN() {
        this.toStringAndCheck2(Double.NaN);
    }

    @Test
    public void testToStringPositiveInfinity() {
        this.toStringAndCheck2(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testToStringNegativeInfinity() {
        this.toStringAndCheck2(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testToStringIncludesDecimals() {
        this.toStringAndCheck2(1.2345);
    }

    // helpers..........................................................................................................

    @Override
    ExpressionNumberDouble create(final double value) {
        return ExpressionNumberDouble.withDouble(value);
    }

    @Override final void checkValue0(final double value, final ExpressionNumberDouble number) {
        this.checkEquals(value, number.doubleValue());
    }

    @Override
    final void toStringAndCheck2(final double value) {
        this.toStringAndCheck2(value, Double.toString(value));
    }

    @Override
    public Class<ExpressionNumberDouble> type() {
        return ExpressionNumberDouble.class;
    }
}
