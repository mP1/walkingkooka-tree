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
import walkingkooka.convert.ConverterContext;
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

public final class ExpressionNumberKindConverterTest implements ConverterTesting2<ExpressionNumberKindConverter<ConverterContext>, ConverterContext> {

    @Test
    public final void testWithNullConverterFails() {
        assertThrows(NullPointerException.class, () -> ExpressionNumberKindConverter.with(ExpressionNumberKind.BIG_DECIMAL, null));
    }

    @Test
    public final void testConvertByteFails() {
        this.convertFails2("123", Byte.class);
    }

    @Test
    public final void testConvertShortFails() {
        this.convertFails2("123", Short.class);
    }

    @Test
    public final void testConvertIntegerFails() {
        this.convertFails2("123", Integer.class);
    }

    @Test
    public final void testConvertLongFails() {
        this.convertFails2("123", Long.class);
    }

    @Test
    public final void testConvertFloatFails() {
        this.convertFails2("123", Float.class);
    }

    @Test
    public final void testConvertDoubleFails() {
        this.convertFails2("123", Double.class);
    }

    @Test
    public final void testConvertBigIntegerFails() {
        this.convertFails2("123", BigInteger.class);
    }

    @Test
    public final void testConvertBigDecimalFails() {
        this.convertFails2("123", BigDecimal.class);
    }

    @Test
    public final void testConvertExpressionNumberFails() {
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
        final Converter<ConverterContext> converter = kind.toConverter(new FakeConverter<ConverterContext>() {
            @Override
            public boolean canConvert(final Object value,
                                      final Class<?> type,
                                      final ConverterContext context) {
                return false;
            }

            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> type,
                                                 final ConverterContext context) {
                return this.failConversion(value, type);
            }
        });
        final ConverterContext context = this.createContext();
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
        this.convertAndCheck(kind.toConverter(Converters.stringNumber((d) -> (DecimalFormat) DecimalFormat.getInstance())), from, target, number);
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
        this.convertAndCheck(kind.toConverter(Converters.numberNumber()), from, target, number);
    }

    // factories & helpers..............................................................................................

    @Override
    public final ExpressionNumberKindConverter<ConverterContext> createConverter() {
        return ExpressionNumberKindConverter.with(ExpressionNumberKind.BIG_DECIMAL, Converters.fake());
    }

    @Override
    public final ConverterContext createContext() {
        return ConverterContexts.basic(DateTimeContexts.fake(), DecimalNumberContexts.american(MathContext.DECIMAL32));
    }

    @Override
    public Class<ExpressionNumberKindConverter<ConverterContext>> type() {
        return Cast.to(ExpressionNumberKindConverter.class);
    }
}
