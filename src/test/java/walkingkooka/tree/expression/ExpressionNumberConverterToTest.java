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

public final class ExpressionNumberConverterToTest extends ExpressionNumberConverterTestCase<ExpressionNumberConverterTo<ExpressionNumberConverterContext>> {

    @Test
    public void testWithNullConverterFails() {
        assertThrows(NullPointerException.class, () -> ExpressionNumberConverterTo.with(null));
    }

    @Test
    public void testWithExpressionNumberToConverter() {
        final ExpressionNumberConverterTo<ExpressionNumberConverterContext> converter = this.createConverter();
        assertSame(
                converter,
                ExpressionNumberConverterTo.with(converter)
        );
    }

    // fails............................................................................................................

    @Test
    public void testConvertByteFails() {
        this.convertFails2("123", Byte.class);
    }

    @Test
    public void testConvertShortFails() {
        this.convertFails2("123", Short.class);
    }

    @Test
    public void testConvertIntegerFails() {
        this.convertFails2("123", Integer.class);
    }

    @Test
    public void testConvertLongFails() {
        this.convertFails2("123", Long.class);
    }

    @Test
    public void testConvertFloatFails() {
        this.convertFails2("123", Float.class);
    }

    @Test
    public void testConvertDoubleFails() {
        this.convertFails2("123", Double.class);
    }

    @Test
    public void testConvertBigIntegerFails() {
        this.convertFails2("123", BigInteger.class);
    }

    @Test
    public void testConvertBigDecimalFails() {
        this.convertFails2("123", BigDecimal.class);
    }

    private void convertFails2(final String value,
                               final Class<?> type) {
        this.convertFails3(ExpressionNumberKind.BIG_DECIMAL, value, type);
        this.convertFails3(ExpressionNumberKind.DOUBLE, value, type);
    }

    private void convertFails3(final ExpressionNumberKind kind,
                               final String value,
                               final Class<?> type) {
        final Converter<ExpressionNumberConverterContext> converter = ExpressionNumber.toConverter(
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
    public void testConvertByte() {
        this.convertAndCheck2((byte) 123);
    }

    @Test
    public void testConvertShort() {
        this.convertAndCheck2((short) 123);
    }

    @Test
    public void testConvertInteger() {
        this.convertAndCheck2(123);
    }

    @Test
    public void testConvertLong() {
        this.convertAndCheck2(123L);
    }

    @Test
    public void testConvertFloat() {
        this.convertAndCheck2(128.5f);
    }

    @Test
    public void testConvertDouble() {
        this.convertAndCheck2(128.5);
    }

    @Test
    public void testConvertBigInteger() {
        this.convertAndCheck2(BigInteger.valueOf(123));
    }

    @Test
    public void testConvertBigDecimal() {
        this.convertAndCheck2(BigDecimal.valueOf(128.5));
    }

    @Test
    public void testConvertBigDecimal2() {
        this.convertAndCheck2(
                BigDecimal.valueOf(128.5)
        );
    }

    @Test
    public void testConvertBigDecimalToExpressionNumber() {
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
    public void testConvertBigDecimalToExpressionNumber2() {
        final ExpressionNumberKind kind = ExpressionNumberKind.DOUBLE;
        final Number number = BigDecimal.valueOf(128.5);

        this.convertAndCheck(
                number,
                ExpressionNumber.class,
                this.createContext(kind),
                kind.create(number)
        );
    }

    private void convertAndCheck2(final Number number) {
        this.convertAndCheck2(
                this.createConverter(),
                number
        );
    }

    private void convertAndCheck2(final ExpressionNumberConverterTo<ExpressionNumberConverterContext> converter,
                                  final Number number) {
        this.convertAndCheck3(
                converter,
                ExpressionNumberKind.BIG_DECIMAL,
                number,
                ExpressionNumber.class,
                ExpressionNumberKind.BIG_DECIMAL.create(number)
        );

        this.convertAndCheck3(
                converter,
                ExpressionNumberKind.DOUBLE,
                number,
                ExpressionNumber.class,
                ExpressionNumberKind.DOUBLE.create(number)
        );
    }

    private <N> void convertAndCheck3(final Converter<ExpressionNumberConverterContext> converter,
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

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                ExpressionNumberConverterTo.with(Converters.fake()
                        .setToString("Custom")
                        .cast(ExpressionNumberConverterContext.class)),
                "Custom? " + ExpressionNumber.class.getSimpleName()
        );
    }

    // helpers..........................................................................................................

    @Override
    public ExpressionNumberConverterTo<ExpressionNumberConverterContext> createConverter() {
        return ExpressionNumberConverterTo.with(
                Converters.numberNumber()
        );
    }

    @Override
    public Class<ExpressionNumberConverterTo<ExpressionNumberConverterContext>> type() {
        return Cast.to(ExpressionNumberConverterTo.class);
    }
}
