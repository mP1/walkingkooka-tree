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
import walkingkooka.ToStringTesting;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberFromExpressionNumberNumberConverterTest implements ConverterTesting2<ExpressionNumberFromExpressionNumberNumberConverter<ExpressionNumberConverterContext>, ExpressionNumberConverterContext>,
        ToStringTesting<ExpressionNumberFromExpressionNumberNumberConverter<ExpressionNumberConverterContext>> {

    @Test
    public void testWithNullConverterFails() {
        assertThrows(NullPointerException.class, () -> ExpressionNumberFromExpressionNumberNumberConverter.with(null));
    }

    @Test
    public void testConvertFails() {
        this.convertFails(true, String.class);
    }

    @Test
    public void testByte() {
        this.convertAndCheck2((byte) 123, "123");
    }

    @Test
    public void testShort() {
        this.convertAndCheck2((short) 123, "123");
    }

    @Test
    public void testInteger() {
        this.convertAndCheck2(123, "123");
    }

    @Test
    public void testLong() {
        this.convertAndCheck2(123L, "123");
    }

    @Test
    public void testFloat() {
        this.convertAndCheck2(128.5f, "128.5");
    }

    @Test
    public void testDouble() {
        this.convertAndCheck2(128.5, "128.5");
    }

    @Test
    public void testBigInteger() {
        this.convertAndCheck2(BigInteger.valueOf(123), "123");
    }

    @Test
    public void testBigDecimal() {
        this.convertAndCheck2(BigDecimal.valueOf(128.5), "128.5");
    }

    @Test
    public void testExpressionNumberBigDecimal() {
        this.convertAndCheck2(ExpressionNumberKind.BIG_DECIMAL.create(BigDecimal.valueOf(128.5)), "128.5");
    }

    @Test
    public void testExpressionNumberDouble() {
        this.convertAndCheck2(ExpressionNumberKind.DOUBLE.create(128.5), "128.5");
    }

    private void convertAndCheck2(final Object value,
                                  final String expected) {
        this.convertAndCheck(value,
                String.class,
                expected);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createConverter(), "ExpressionNumber|DecimalFormat.getInstance");
    }

    @Override
    public ExpressionNumberFromExpressionNumberNumberConverter<ExpressionNumberConverterContext> createConverter() {
        return ExpressionNumberFromExpressionNumberNumberConverter.with(Converters.numberString(new Function<>() {
            @Override
            public DecimalFormat apply(final DecimalNumberContext decimalNumberContext) {
                return (DecimalFormat) DecimalFormat.getInstance();
            }

            // want to override toString
            @Override
            public String toString() {
                return "DecimalFormat.getInstance";
            }
        }));
    }

    @Override
    public ExpressionNumberConverterContext createContext() {
        return this.createContext(ExpressionNumberKind.DEFAULT);
    }

    private ExpressionNumberConverterContext createContext(final ExpressionNumberKind kind) {
        return ExpressionNumberConverterContexts.basic(ConverterContexts.basic(DateTimeContexts.fake(), DecimalNumberContexts.american(MathContext.DECIMAL32)), kind);
    }

    @Override
    public Class<ExpressionNumberFromExpressionNumberNumberConverter<ExpressionNumberConverterContext>> type() {
        return Cast.to(ExpressionNumberFromExpressionNumberNumberConverter.class);
    }

    @Override
    public String typeNamePrefix() {
        return ExpressionNumberFromExpressionNumberNumberConverter.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return "";
    }
}
