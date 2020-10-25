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
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContexts;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ExpressionNumberConverterToNumberExpressionNumberTestCase<C extends ExpressionNumberConverterToNumberExpressionNumber, N extends ExpressionNumber> implements ConverterTesting2<C> {

    ExpressionNumberConverterToNumberExpressionNumberTestCase() {
        super();
    }

    @Test
    public final void testWithNullConverterFails() {
        assertThrows(NullPointerException.class, () -> this.createConverter(null));
    }

    // ??? -> Number....................................................................................................

    @Test
    public final void testByte() {
        this.convertAndCheck("123", Byte.class, (byte) 123);
    }

    @Test
    public final void testShort() {
        this.convertAndCheck("123", Short.class, (short) 123);
    }

    @Test
    public final void testInteger() {
        this.convertAndCheck("123", Integer.class, 123);
    }

    @Test
    public final void testLong() {
        this.convertAndCheck("123", Long.class, 123L);
    }

    @Test
    public final void testFloat() {
        this.convertAndCheck("128.5", Float.class, 128.5f);
    }

    @Test
    public final void testDouble() {
        this.convertAndCheck("128.5", Double.class, 128.5);
    }

    @Test
    public final void testBigInteger() {
        this.convertAndCheck("123", BigInteger.class, BigInteger.valueOf(123));
    }

    @Test
    public final void testBigDecimal() {
        this.convertAndCheck("128.5", BigDecimal.class, BigDecimal.valueOf(128.5));
    }

    // String -> ExpressionNumber..........................................................................................

    @Test
    public final void testStringExpressionNumber() {
        this.convertAndCheck("128", ExpressionNumber.class, this.expressionNumber(128));
    }

    @Test
    public final void testStringExpressionNumber2() {
        this.convertAndCheck("128.5", ExpressionNumber.class, this.expressionNumber(128.5));
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
        this.convertAndCheck(this.createConverter(Converters.numberNumber()),
                value,
                ExpressionNumber.class,
                this.expressionNumber(value.doubleValue()));
    }

    // factories & helpers..............................................................................................

    @Override
    public final C createConverter() {
        return this.createConverter(Converters.stringNumber((d) -> (DecimalFormat) DecimalFormat.getInstance()));
    }

    abstract C createConverter(final Converter converter);

    @Override
    public final ConverterContext createContext() {
        return ConverterContexts.basic(DateTimeContexts.fake(), DecimalNumberContexts.american(MathContext.DECIMAL32));
    }

    abstract N expressionNumber(final double value);

    @Override
    public final String typeNamePrefix() {
        return ExpressionNumberConverterToNumberExpressionNumber.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
