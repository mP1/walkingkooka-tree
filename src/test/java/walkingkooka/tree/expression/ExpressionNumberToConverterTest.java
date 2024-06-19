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
import walkingkooka.ToStringTesting;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverter;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContexts;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberToConverterTest implements ConverterTesting2<ExpressionNumberToConverter<ExpressionNumberConverterContext>, ExpressionNumberConverterContext>,
        ToStringTesting<ExpressionNumberToConverter<ExpressionNumberConverterContext>> {

    @Test
    public void testWithNullConverterFails() {
        assertThrows(NullPointerException.class, () -> ExpressionNumberToConverter.with(null));
    }

    @Test
    public void testWithExpressionNumberToConverter() {
        final ExpressionNumberToConverter<ExpressionNumberConverterContext> converter = this.createConverter();
        assertSame(
                converter,
                ExpressionNumberToConverter.with(converter)
        );
    }

    // convert..........................................................................................................

    @Test
    public void testConvertersSimpleFails() {
        this.convertFails(
                Converters.simple(),
                ExpressionNumberKind.DEFAULT.one(),
                ExpressionNumber.class
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
        final ExpressionNumberConverterContext context = this.createContext();
        this.checkEquals(false, converter.canConvert(value, type, context));
        this.convertFails(converter,
                value,
                type,
                context);
    }

    // ??? -> Number....................................................................................................

    @Test
    public void testConvertNull() {
        this.convertAndCheck(
                null,
                ExpressionNumber.class,
                null
        );
    }

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
                ExpressionNumberToConverter.with(
                        ExpressionNumberFromConverter.with(Converters.fake())
                ),
                BigDecimal.valueOf(128.5)
        );
    }

    @Test
    public void testConvertBigDecimalToExpressionNumber() {
        final ExpressionNumberKind kind = ExpressionNumberKind.BIG_DECIMAL;
        final Number number = BigDecimal.valueOf(128.5);

        this.convertAndCheck(
                ExpressionNumberToConverter.with(
                        ExpressionNumberFromConverter.with(Converters.fake())
                ),
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
                ExpressionNumberToConverter.with(
                        ExpressionNumberFromConverter.with(Converters.fake())
                ),
                number,
                ExpressionNumber.class,
                this.createContext(kind),
                kind.create(number)
        );
    }

    @Test
    public void testConvertExpressionNumberWithDouble() {
        this.convertAndCheck2(
                ExpressionNumberKind.DOUBLE.create(1)
        );
    }

    @Test
    public void testConvertExpressionNumberWithDouble2() {
        this.convertAndCheck2(
                ExpressionNumberToConverter.with(
                        ExpressionNumberFromConverter.with(Converters.simple())
                ),
                ExpressionNumberKind.DOUBLE.create(1)
        );
    }

    @Test
    public void testConvertExpressionNumberWithBigDecimal() {
        this.convertAndCheck2(
                ExpressionNumberKind.BIG_DECIMAL.create(1)
        );
    }

    @Test
    public void testConvertExpressionNumberWithBigDecimal2() {
        this.convertAndCheck2(
                ExpressionNumberToConverter.with(
                        ExpressionNumberFromConverter.with(Converters.simple())
                ),
                ExpressionNumberKind.BIG_DECIMAL.create(1)
        );
    }

    private <N extends Number> void convertAndCheck2(final N number) {
        this.convertAndCheck2(
                this.createConverter(),
                number
        );
    }

    private <N extends Number> void convertAndCheck2(final ExpressionNumberToConverter<ExpressionNumberConverterContext> converter,
                                                     final N number) {
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

    // ExpressionNumber.................................................................................................

    @Test
    public void testConvertExpressionNumberBigDecimalBigDecimal() {
        this.convertExpressionNumberAndCheck(ExpressionNumberKind.BIG_DECIMAL.create(1.0), ExpressionNumberKind.BIG_DECIMAL);
    }

    @Test
    public void testConvertExpressionNumberBigDecimalDouble() {
        this.convertExpressionNumberAndCheck(ExpressionNumberKind.BIG_DECIMAL.create(1.0), ExpressionNumberKind.DOUBLE);
    }

    @Test
    public void testConvertExpressionNumberDoubleBigDecimal() {
        this.convertExpressionNumberAndCheck(ExpressionNumberKind.DOUBLE.create(1.0), ExpressionNumberKind.BIG_DECIMAL);
    }

    @Test
    public void testConvertExpressionNumberDoubleDouble() {
        this.convertExpressionNumberAndCheck(ExpressionNumberKind.DOUBLE.create(1.0), ExpressionNumberKind.DOUBLE);
    }

    private void convertExpressionNumberAndCheck(final ExpressionNumber number,
                                                 final ExpressionNumberKind kind) {
        this.convertAndCheck(
                number,
                ExpressionNumber.class,
                this.createContext(kind),
                kind.create(number)
        );
    }

    @Test
    public void testConvertExpressionNumberValueDifferentType() {
        this.convertFails(
                ExpressionNumberToConverter.with(Converters.simple()),
                ExpressionNumberKind.DEFAULT.one(),
                this.getClass()
        );
    }

    @Test
    public void testConvertNotExpressionNumber() {
        this.convertFails(ExpressionNumberToConverter.with(Converters.simple()),
                this,
                ExpressionNumber.class);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                ExpressionNumberToConverter.with(Converters.fake()
                        .setToString("Custom")
                        .cast(ExpressionNumberConverterContext.class)),
                "Custom? " + ExpressionNumber.class.getSimpleName()
        );
    }

    // helpers..........................................................................................................

    @Override
    public ExpressionNumberToConverter<ExpressionNumberConverterContext> createConverter() {
        return ExpressionNumberToConverter.with(
                Converters.numberNumber()
        );
    }

    @Override
    public ExpressionNumberConverterContext createContext() {
        return this.createContext(ExpressionNumberKind.DEFAULT);
    }

    private ExpressionNumberConverterContext createContext(final ExpressionNumberKind kind) {
        return ExpressionNumberConverterContexts.basic(
                Converters.fake(),
                ConverterContexts.basic(
                        Converters.fake(),
                        DateTimeContexts.fake(),
                        DecimalNumberContexts.american(MathContext.DECIMAL32)
                ),
                kind
        );
    }

    @Override
    public Class<ExpressionNumberToConverter<ExpressionNumberConverterContext>> type() {
        return Cast.to(ExpressionNumberToConverter.class);
    }

    @Override
    public String typeNamePrefix() {
        return ExpressionNumber.class.getSimpleName();
    }
}
