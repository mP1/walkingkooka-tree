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
import walkingkooka.Either;
import walkingkooka.convert.Converter;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverter;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberConverterToNumberOrExpressionNumberTest extends ExpressionNumberConverterTestCase<ExpressionNumberConverterToNumberOrExpressionNumber<ExpressionNumberConverterContext>> {

    @Test
    public void testWithNullConverterFails() {
        assertThrows(NullPointerException.class, () -> ExpressionNumberConverterToNumberOrExpressionNumber.with(null));
    }

    @Test
    public void testWithExpressionNumberToConverter() {
        final ExpressionNumberConverterToNumberOrExpressionNumber<ExpressionNumberConverterContext> converter = this.createConverter();
        assertSame(
                converter,
                ExpressionNumberConverterToNumberOrExpressionNumber.with(converter)
        );
    }

    // fails............................................................................................................

    @Test
    public void testConvertStringToByteFails() {
        this.convertFails2(
                "123",
                Byte.class
        );
    }

    @Test
    public void testConvertStringToShortFails() {
        this.convertFails2(
                "123",
                Short.class
        );
    }

    @Test
    public void testConvertStringToIntegerFails() {
        this.convertFails2(
                "123",
                Integer.class
        );
    }

    @Test
    public void testConvertStringToLongFails() {
        this.convertFails2(
                "123",
                Long.class
        );
    }

    @Test
    public void testConvertStringToFloatFails() {
        this.convertFails2(
                "123",
                Float.class
        );
    }

    @Test
    public void testConvertStringToDoubleFails() {
        this.convertFails2(
                "123",
                Double.class
        );
    }

    @Test
    public void testConvertStringToBigIntegerFails() {
        this.convertFails2(
                "123",
                BigInteger.class
        );
    }

    @Test
    public void testConvertStringToBigDecimalFails() {
        this.convertFails2(
                "123",
                BigDecimal.class
        );
    }

    private void convertFails2(final String value,
                               final Class<?> type) {
        this.convertFails3(ExpressionNumberKind.BIG_DECIMAL, value, type);
        this.convertFails3(ExpressionNumberKind.DOUBLE, value, type);
    }

    private void convertFails3(final ExpressionNumberKind kind,
                               final String value,
                               final Class<?> type) {
        final Converter<ExpressionNumberConverterContext> converter = ExpressionNumberConverters.toNumberOrExpressionNumber(
                new FakeConverter<>() {
                    @Override
                    public boolean canConvert(final Object value,
                                              final Class<?> type,
                                              final ExpressionNumberConverterContext context) {
                        return false;
                    }

                    @Override
                    public <T> Either<T, String> convert(final Object value,
                                                         final Class<T> type,
                                                         final ExpressionNumberConverterContext context) {
                        return this.failConversion(value, type);
                    }
                });
        final ExpressionNumberConverterContext context = this.createContext(kind);
        this.checkEquals(
                false,
                converter.canConvert(value, type, context)
        );

        this.convertFails(
                converter,
                value,
                type,
                context
        );
    }

    // ??? -> Number....................................................................................................

    @Test
    public void testConvertByteToExpressionNumber() {
        this.convertToExpressionNumberAndCheck((byte) 123);
    }

    @Test
    public void testConvertShortToExpressionNumber() {
        this.convertToExpressionNumberAndCheck((short) 123);
    }

    @Test
    public void testConvertIntegerToExpressionNumber() {
        this.convertToExpressionNumberAndCheck(123);
    }

    @Test
    public void testConvertLongToExpressionNumber() {
        this.convertToExpressionNumberAndCheck(123L);
    }

    @Test
    public void testConvertFloatToExpressionNumber() {
        this.convertToExpressionNumberAndCheck(128.5f);
    }

    @Test
    public void testConvertDoubleToExpressionNumber() {
        this.convertToExpressionNumberAndCheck(128.5);
    }

    @Test
    public void testConvertBigIntegerToExpressionNumber() {
        this.convertToExpressionNumberAndCheck(BigInteger.valueOf(123));
    }

    @Test
    public void testConvertBigDecimalToExpressionNumber() {
        this.convertToExpressionNumberAndCheck(BigDecimal.valueOf(128.5));
    }

    @Test
    public void testConvertBigDecimalToExpressionNumber2() {
        final ExpressionNumberKind kind = ExpressionNumberKind.BIG_DECIMAL;
        final Number number = BigDecimal.valueOf(128.5);

        this.convertAndCheck(
                number,
                ExpressionNumber.class,
                this.createContext(kind),
                kind.create(number)
        );
    }

    @Test
    public void testConvertBigDecimalToExpressionNumber3() {
        final ExpressionNumberKind kind = ExpressionNumberKind.DOUBLE;
        final Number number = BigDecimal.valueOf(128.5);

        this.convertAndCheck(
                number,
                ExpressionNumber.class,
                this.createContext(kind),
                kind.create(number)
        );
    }

    @Test
    public void testConvertWithExpressionNumberConvertFromWithBigDecimalToExpressionNumber() {
        this.convertToExpressionNumberAndCheck(
                ExpressionNumberConverterToNumberOrExpressionNumber.with(
                        ExpressionNumberConverterNumberOrExpressionNumberToNumber.instance()
                                .to(
                                        Number.class,
                                        Converters.numberToNumber()
                                )
                ),
                BigDecimal.valueOf(123)
        );
    }

    private void convertToExpressionNumberAndCheck(final Number number) {
        this.convertToExpressionNumberAndCheck(
                this.createConverter(),
                number
        );
    }

    private void convertToExpressionNumberAndCheck(final ExpressionNumberConverterToNumberOrExpressionNumber<ExpressionNumberConverterContext> converter,
                                                   final Number number) {
        this.convertAndCheck2(
                converter,
                ExpressionNumberKind.BIG_DECIMAL,
                number,
                ExpressionNumber.class,
                ExpressionNumberKind.BIG_DECIMAL.create(number)
        );

        this.convertAndCheck2(
                converter,
                ExpressionNumberKind.DOUBLE,
                number,
                ExpressionNumber.class,
                ExpressionNumberKind.DOUBLE.create(number)
        );
    }

    // toBigDecimal.........................................................................................................

    @Test
    public void testConvertByteToBigDecimal() {
        this.convertToBigDecimalAndCheck((byte) 123);
    }

    @Test
    public void testConvertShortToBigDecimal() {
        this.convertToBigDecimalAndCheck((short) 123);
    }

    @Test
    public void testConvertIntegerToBigDecimal() {
        this.convertToBigDecimalAndCheck(123);
    }

    @Test
    public void testConvertLongToBigDecimal() {
        this.convertToBigDecimalAndCheck(123L);
    }

    @Test
    public void testConvertFloatToBigDecimal() {
        this.convertToBigDecimalAndCheck(128.5f);
    }

    @Test
    public void testConvertDoubleToBigDecimal() {
        this.convertToBigDecimalAndCheck(128.5);
    }

    @Test
    public void testConvertBigIntegerToBigDecimal() {
        this.convertToBigDecimalAndCheck(BigInteger.valueOf(123));
    }

    @Test
    public void testConvertBigDecimalToBigDecimal() {
        this.convertToBigDecimalAndCheck(BigDecimal.valueOf(128.5));
    }

    private void convertToBigDecimalAndCheck(final Number number) {
        this.convertToBigDecimalAndCheck(
                this.createConverter(),
                number
        );
    }

    private void convertToBigDecimalAndCheck(final ExpressionNumberConverterToNumberOrExpressionNumber<ExpressionNumberConverterContext> converter,
                                             final Number number) {
        final BigDecimal expected = ExpressionNumberKind.BIG_DECIMAL.create(number)
                .bigDecimal();

        this.convertAndCheck2(
                converter,
                ExpressionNumberKind.BIG_DECIMAL,
                number,
                BigDecimal.class,
                expected
        );

        this.convertAndCheck2(
                converter,
                ExpressionNumberKind.DOUBLE,
                number,
                BigDecimal.class,
                expected
        );
    }

    // toDouble.........................................................................................................

    @Test
    public void testConvertByteToDouble() {
        this.convertToDoubleAndCheck((byte) 123);
    }

    @Test
    public void testConvertShortToDouble() {
        this.convertToDoubleAndCheck((short) 123);
    }

    @Test
    public void testConvertIntegerToDouble() {
        this.convertToDoubleAndCheck(123);
    }

    @Test
    public void testConvertLongToDouble() {
        this.convertToDoubleAndCheck(123L);
    }

    @Test
    public void testConvertFloatToDouble() {
        this.convertToDoubleAndCheck(128.5f);
    }

    @Test
    public void testConvertDoubleToDouble() {
        this.convertToDoubleAndCheck(128.5);
    }

    @Test
    public void testConvertBigIntegerToDouble() {
        this.convertToDoubleAndCheck(BigInteger.valueOf(123));
    }

    @Test
    public void testConvertBigDecimalToDouble() {
        this.convertToDoubleAndCheck(BigDecimal.valueOf(128.5));
    }

    private void convertToDoubleAndCheck(final Number number) {
        this.convertToDoubleAndCheck(
                this.createConverter(),
                number
        );
    }

    private void convertToDoubleAndCheck(final ExpressionNumberConverterToNumberOrExpressionNumber<ExpressionNumberConverterContext> converter,
                                         final Number number) {
        final double expected = ExpressionNumberKind.DOUBLE.create(number)
                .doubleValue();

        this.convertAndCheck2(
                converter,
                ExpressionNumberKind.BIG_DECIMAL,
                number,
                Double.class,
                expected
        );

        this.convertAndCheck2(
                converter,
                ExpressionNumberKind.DOUBLE,
                number,
                Double.class,
                expected
        );
    }

    private <N> void convertAndCheck2(final Converter<ExpressionNumberConverterContext> converter,
                                      final ExpressionNumberKind kind,
                                      final Object from,
                                      final Class<N> target,
                                      final N number) {
        this.convertAndCheck(
                converter,
                from,
                target,
                this.createContext(kind),
                number
        );
    }

    // convertExpressionNumberToExpressionNumber........................................................................

    @Test
    public void testConvertExpressionNumberToExpressionNumberBigDecimal() {
        this.convertAndCheck(
                ExpressionNumberKind.BIG_DECIMAL.create(123.5)
        );
    }

    @Test
    public void testConvertExpressionNumberToExpressionNumberDouble() {
        this.convertAndCheck(
                ExpressionNumberKind.DOUBLE.create(123.5)
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                ExpressionNumberConverterToNumberOrExpressionNumber.with(Converters.fake()
                        .setToString("Custom")
                        .cast(ExpressionNumberConverterContext.class)),
                "Custom | " + ExpressionNumber.class.getSimpleName()
        );
    }

    // helpers..........................................................................................................

    @Override
    public ExpressionNumberConverterToNumberOrExpressionNumber<ExpressionNumberConverterContext> createConverter() {
        return ExpressionNumberConverterToNumberOrExpressionNumber.with(
                Converters.numberToNumber()
        );
    }

    @Override
    public Class<ExpressionNumberConverterToNumberOrExpressionNumber<ExpressionNumberConverterContext>> type() {
        return Cast.to(ExpressionNumberConverterToNumberOrExpressionNumber.class);
    }
}
