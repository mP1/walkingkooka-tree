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
import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberToExpressionNumberConverterTest implements ConverterTesting2<ExpressionNumberToExpressionNumberConverter<ExpressionNumberConverterContext>, ExpressionNumberConverterContext>,
        ToStringTesting<ExpressionNumberToExpressionNumberConverter<ExpressionNumberConverterContext>> {

    @Test
    public void testWithNullConverterFails() {
        assertThrows(NullPointerException.class, () -> ExpressionNumberToExpressionNumberConverter.with(null));
    }

    @Test
    public void testConvertersSimpleFails() {
        this.convertFails(Converters.simple(), ExpressionNumberKind.DEFAULT.create(1), ExpressionNumber.class);
    }

    // fails............................................................................................................

    @Test
    public final void testByteFails() {
        this.convertFails2("123", Byte.class);
    }

    @Test
    public final void testShortFails() {
        this.convertFails2("123", Short.class);
    }

    @Test
    public final void testIntegerFails() {
        this.convertFails2("123", Integer.class);
    }

    @Test
    public final void testLongFails() {
        this.convertFails2("123", Long.class);
    }

    @Test
    public final void testFloatFails() {
        this.convertFails2("123", Float.class);
    }

    @Test
    public final void testDoubleFails() {
        this.convertFails2("123", Double.class);
    }

    @Test
    public final void testBigIntegerFails() {
        this.convertFails2("123", BigInteger.class);
    }

    @Test
    public final void testBigDecimalFails() {
        this.convertFails2("123", BigDecimal.class);
    }

    @Test
    public final void testExpressionNumberFails() {
        this.convertFails2("123", ExpressionNumber.class);
    }

    private void convertFails2(final String value,
                               final Class<?> type) {
        this.convertFails3(ExpressionNumberKind.BIG_DECIMAL, value, type);
        this.convertFails3(ExpressionNumberKind.DOUBLE, value, type);
    }

    private void convertFails3(final ExpressionNumberKind kind,
                               final String value,
                               final Class<?> type) {
        final Converter<ExpressionNumberConverterContext> converter = ExpressionNumber.toExpressionNumberConverter(new FakeConverter<ExpressionNumberConverterContext>() {
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
        assertEquals(false, converter.canConvert(value, type, context));
        this.convertFails(converter,
                value,
                type,
                context);
    }

    // ??? -> Number....................................................................................................

    @Test
    public final void testByte() {
        this.convertAndCheck2("123", Byte.class, (byte) 123);
    }

    @Test
    public final void testShort() {
        this.convertAndCheck2("123", Short.class, (short) 123);
    }

    @Test
    public final void testInteger() {
        this.convertAndCheck2("123", Integer.class, 123);
    }

    @Test
    public final void testLong() {
        this.convertAndCheck2("123", Long.class, 123L);
    }

    @Test
    public final void testFloat() {
        this.convertAndCheck2("128.5", Float.class, 128.5f);
    }

    @Test
    public final void testDouble() {
        this.convertAndCheck2("128.5", Double.class, 128.5);
    }

    @Test
    public final void testBigInteger() {
        this.convertAndCheck2("123", BigInteger.class, BigInteger.valueOf(123));
    }

    @Test
    public final void testBigDecimal() {
        this.convertAndCheck2("128.5", BigDecimal.class, BigDecimal.valueOf(128.5));
    }

    private <N> void convertAndCheck2(final String string,
                                      final Class<N> target,
                                      final N number) {
        this.convertAndCheck4(ExpressionNumberKind.BIG_DECIMAL,
                string,
                ExpressionNumber.class,
                ExpressionNumberKind.BIG_DECIMAL.create(new BigDecimal(string)));

        this.convertAndCheck4(ExpressionNumberKind.DOUBLE,
                string,
                ExpressionNumber.class,
                ExpressionNumberKind.DOUBLE.create(Double.parseDouble(string)));
    }

    // String -> ExpressionNumber..........................................................................................

    @Test
    public final void testStringExpressionNumber() {
        this.convertAndCheck3("128");
    }

    @Test
    public final void testStringExpressionNumber2() {
        this.convertAndCheck3("128.5");
    }

    private void convertAndCheck3(final String string) {
        this.convertAndCheck4(ExpressionNumberKind.BIG_DECIMAL,
                string,
                ExpressionNumber.class,
                ExpressionNumberKind.BIG_DECIMAL.create(new BigDecimal(string)));

        this.convertAndCheck4(ExpressionNumberKind.DOUBLE,
                string,
                ExpressionNumber.class,
                ExpressionNumberKind.DOUBLE.create(Double.parseDouble(string)));
    }

    private <N> void convertAndCheck4(final ExpressionNumberKind kind,
                                      final Object from,
                                      final Class<N> target,
                                      final N number) {
        this.convertAndCheck(ExpressionNumber.toExpressionNumberConverter(Converters.stringNumber((d) -> (DecimalFormat) DecimalFormat.getInstance())),
                from,
                target,
                this.createContext(kind),
                number);
    }

    // Number -> ExpressionNumber..........................................................................................

    @Test
    public final void testByteExpressionNumber() {
        this.convertNumberAndCheck((byte) 123);
    }

    @Test
    public final void testShortNumberExpressionNumber() {
        this.convertNumberAndCheck((short) 123);
    }

    @Test
    public final void testIntegerNumberExpressionNumber() {
        this.convertNumberAndCheck(123);
    }

    @Test
    public final void testFloatNumberExpressionNumber() {
        this.convertNumberAndCheck(123f);
    }

    @Test
    public final void testDoubleNumberExpressionNumber() {
        this.convertNumberAndCheck(123.0);
    }

    @Test
    public final void testBigIntegerNumberExpressionNumber() {
        this.convertNumberAndCheck(BigInteger.valueOf(123));
    }

    @Test
    public final void testBigDecimalNumberExpressionNumber() {
        this.convertNumberAndCheck(BigDecimal.valueOf(128.5));
    }

    private void convertNumberAndCheck(final Number value) {
        this.convertNumberAndCheck2(ExpressionNumberKind.BIG_DECIMAL,
                value,
                ExpressionNumber.class,
                ExpressionNumberKind.BIG_DECIMAL.create(value));

        this.convertNumberAndCheck2(ExpressionNumberKind.DOUBLE,
                value,
                ExpressionNumber.class,
                ExpressionNumberKind.DOUBLE.create(value));
    }

    private <N> void convertNumberAndCheck2(final ExpressionNumberKind kind,
                                            final Object from,
                                            final Class<N> target,
                                            final N number) {
        this.convertAndCheck(ExpressionNumber.toExpressionNumberConverter(Converters.numberNumber()),
                from,
                target,
                ExpressionNumberConverterContexts.basic(ConverterContexts.fake(), kind),
                number);
    }

    // ExpressionNumber.................................................................................................

    @Test
    public void testExpressionNumberBigDecimalBigDecimal() {
        this.convertExpressionNumberAndCheck(ExpressionNumberKind.BIG_DECIMAL.create(1.0), ExpressionNumberKind.BIG_DECIMAL);
    }

    @Test
    public void testExpressionNumberBigDecimalDouble() {
        this.convertExpressionNumberAndCheck(ExpressionNumberKind.BIG_DECIMAL.create(1.0), ExpressionNumberKind.DOUBLE);
    }

    @Test
    public void testExpressionNumberDoubleBigDecimal() {
        this.convertExpressionNumberAndCheck(ExpressionNumberKind.DOUBLE.create(1.0), ExpressionNumberKind.BIG_DECIMAL);
    }

    @Test
    public void testExpressionNumberDoubleDouble() {
        this.convertExpressionNumberAndCheck(ExpressionNumberKind.DOUBLE.create(1.0), ExpressionNumberKind.DOUBLE);
    }

    private void convertExpressionNumberAndCheck(final ExpressionNumber number,
                                                 final ExpressionNumberKind kind) {
        this.convertAndCheck(ExpressionNumberToExpressionNumberConverter.with(Converters.numberNumber()),
                number,
                ExpressionNumber.class,
                this.createContext(kind),
                kind.create(number));
    }

    @Test
    public void testExpressionNumberValueDifferentType() {
        this.convertFails(ExpressionNumberToExpressionNumberConverter.with(Converters.simple()),
                ExpressionNumberKind.DEFAULT.create(1),
                this.getClass());
    }

    @Test
    public void testNotExpressionNumber() {
        this.convertFails(ExpressionNumberToExpressionNumberConverter.with(Converters.simple()),
                this,
                ExpressionNumber.class);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(ExpressionNumberToExpressionNumberConverter.with(Converters.fake()
                        .setToString("Custom")
                        .cast(ExpressionNumberConverterContext.class)),
                "Custom|" + ExpressionNumber.class.getSimpleName());
    }

    // helpers..........................................................................................................

    @Override
    public ExpressionNumberToExpressionNumberConverter<ExpressionNumberConverterContext> createConverter() {
        return ExpressionNumberToExpressionNumberConverter.with(Converters.fake());
    }

    @Override
    public ExpressionNumberConverterContext createContext() {
        return this.createContext(ExpressionNumberKind.DEFAULT);
    }

    private ExpressionNumberConverterContext createContext(final ExpressionNumberKind kind) {
        return ExpressionNumberConverterContexts.basic(ConverterContexts.basic(DateTimeContexts.fake(), DecimalNumberContexts.american(MathContext.DECIMAL32)), kind);
    }

    @Override
    public Class<ExpressionNumberToExpressionNumberConverter<ExpressionNumberConverterContext>> type() {
        return Cast.to(ExpressionNumberToExpressionNumberConverter.class);
    }

    @Override
    public String typeNamePrefix() {
        return ExpressionNumber.class.getSimpleName();
    }
}
