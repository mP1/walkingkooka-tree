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
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ExpressionNumberDoubleTest extends ExpressionNumberTestCase<ExpressionNumberDouble> {

    @Override
    @Test
    public void setKindDifferent() {
        final double value = 1.5;
        final ExpressionNumberDouble number = this.create(value);
        final ExpressionNumberBigDecimal different = (ExpressionNumberBigDecimal) number.setKind(ExpressionNumberKind.BIG_DECIMAL);
        assertNotSame(
            number,
            value
        );
        this.checkEquals(
            value,
            different.doubleValue()
        );
    }

    // exp...............................................................................................................

    @Test
    public void testExp() {
        final double value = 123;

        this.checkEquals(
            ExpressionNumberKind.DOUBLE.create(value)
                .exp(CONTEXT),
            ExpressionNumberKind.DOUBLE.create(
                Math.exp(value)
            )
        );
    }

    // ln...............................................................................................................

    @Test
    public void testLn() {
        final double value = 100;

        this.checkEquals(
            ExpressionNumberKind.DOUBLE.create(value)
                .ln(CONTEXT),
            ExpressionNumberKind.DOUBLE.create(Math.log(value))
        );
    }

    // map.............................................................................................................

    @Override
    @Test
    public void testMap() {
        for (final RoundingMode roundingMode : RoundingMode.values()) {
            this.checkEquals(
                ExpressionNumber.with(10.0 * 2),
                ExpressionNumber.with(10)
                    .map(
                        new FakeExpressionNumberFunction() {
                            @Override
                            public double mapDouble(final double value) {
                                return value * 2;
                            }
                        },
                        new MathContext(32, roundingMode)
                    )
            );
        }
    }

    @Override
    @Test
    public void testMapSame() {
        for (final RoundingMode roundingMode : RoundingMode.values()) {
            final ExpressionNumber number = ExpressionNumber.with(10);

            assertSame(
                number,
                number.map(
                    new FakeExpressionNumberFunction() {
                        @Override
                        public double mapDouble(final double value) {
                            return value;
                        }
                    },
                    new MathContext(32, roundingMode)
                )
            );
        }
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

    @Override
    void checkValue0(final double value, final ExpressionNumberDouble number) {
        this.checkEquals(value, number.doubleValue());
    }

    @Override
    void toStringAndCheck2(final double value) {
        this.toStringAndCheck2(value, Double.toString(value));
    }

    @Override
    public Class<ExpressionNumberDouble> type() {
        return ExpressionNumberDouble.class;
    }
}
