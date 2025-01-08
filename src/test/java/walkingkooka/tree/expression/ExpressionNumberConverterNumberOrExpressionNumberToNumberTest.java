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
import walkingkooka.Cast;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class ExpressionNumberConverterNumberOrExpressionNumberToNumberTest extends ExpressionNumberConverterTestCase<ExpressionNumberConverterNumberOrExpressionNumberToNumber<ExpressionNumberConverterContext>> {


    // convert from ExpressionNumber....................................................................................

    @Test
    public void testConvertExpressionNumberToByteFails() {
        this.convertFromExpressionNumberAndFails(
            (byte) 123
        );
    }

    @Test
    public void testConvertExpressionNumberToShortFails() {
        this.convertFromExpressionNumberAndFails(
            (short) 123
        );
    }

    @Test
    public void testConvertExpressionNumberToIntegerFails() {
        this.convertFromExpressionNumberAndFails(
            123
        );
    }

    @Test
    public void testConvertExpressionNumberToLongFails() {
        this.convertFromExpressionNumberAndFails(
            123L
        );
    }

    @Test
    public void testConvertExpressionNumberToFloatFails() {
        this.convertFromExpressionNumberAndFails(
            123.5f
        );
    }

    @Test
    public void testConvertExpressionNumberToDoubleFails() {
        this.convertFromExpressionNumberAndFails(
            123.0
        );
    }

    @Test
    public void testConvertExpressionNumberToBigIntegerFails() {
        this.convertFromExpressionNumberAndFails(
            BigInteger.valueOf(123)
        );
    }

    @Test
    public void testConvertExpressionNumberToBigDecimalFails() {
        this.convertFromExpressionNumberAndFails(
            BigDecimal.valueOf(123.5)
        );
    }

    @Test
    public void testConvertExpressionNumberToExpressionNumberBigDecimalFails() {
        this.convertFromExpressionNumberAndFails(
            ExpressionNumberKind.BIG_DECIMAL.one()
        );
    }

    @Test
    public void testConvertExpressionNumberToExpressionNumberDoubleFails() {
        this.convertFromExpressionNumberAndFails(
            ExpressionNumberKind.DOUBLE.one()
        );
    }

    private void convertFromExpressionNumberAndFails(final Number value) {
        this.convertFails(
            value,
            value.getClass()
        );
    }

    // convert from Number..............................................................................................

    @Test
    public void testConvertByteToNumber() {
        this.convertFromNumberToNumberAndCheck(
            (byte) 123
        );
    }

    @Test
    public void testConvertShortToNumber() {
        this.convertFromNumberToNumberAndCheck(
            (short) 123
        );
    }

    @Test
    public void testConvertIntegerToNumber() {
        this.convertFromNumberToNumberAndCheck(
            123
        );
    }

    @Test
    public void testConvertLongToNumber() {
        this.convertFromNumberToNumberAndCheck(
            123L
        );
    }

    @Test
    public void testConvertFloatToNumber() {
        this.convertFromNumberToNumberAndCheck(
            123.5f
        );
    }

    @Test
    public void testConvertDoubleToNumber() {
        this.convertFromNumberToNumberAndCheck(
            123.0
        );
    }

    @Test
    public void testConvertBigIntegerToNumber() {
        this.convertFromNumberToNumberAndCheck(
            BigInteger.valueOf(123)
        );
    }

    @Test
    public void testConvertBigDecimalToNumber() {
        this.convertFromNumberToNumberAndCheck(
            BigDecimal.valueOf(123.5)
        );
    }

    private void convertFromNumberToNumberAndCheck(final Number expected) {
        this.convertAndCheck(
            expected,
            Number.class,
            this.createContext(),
            expected
        );
    }

    @Test
    public void testConvertExpressionNumberBigDecimalToNumber() {
        final BigDecimal value = BigDecimal.ONE;

        this.convertAndCheck(
            ExpressionNumberKind.BIG_DECIMAL.create(value),
            Number.class,
            value
        );
    }

    @Test
    public void testConvertExpressionNumberDoubleToNumber() {
        final double value = 1.5;

        this.convertAndCheck(
            ExpressionNumberKind.DOUBLE.create(value),
            Number.class,
            value
        );
    }

    @Override
    public ExpressionNumberConverterNumberOrExpressionNumberToNumber<ExpressionNumberConverterContext> createConverter() {
        return ExpressionNumberConverterNumberOrExpressionNumberToNumber.instance();
    }

    @Override
    public Class<ExpressionNumberConverterNumberOrExpressionNumberToNumber<ExpressionNumberConverterContext>> type() {
        return Cast.to(ExpressionNumberConverterNumberOrExpressionNumberToNumber.class);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createConverter(),
            "ExpressionNumber or Number to Number"
        );
    }
}
