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
import walkingkooka.convert.Converter;
import walkingkooka.convert.Converters;
import walkingkooka.math.DecimalNumberContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberConverterFromTest extends ExpressionNumberConverterTestCase<ExpressionNumberConverterFrom<ExpressionNumberConverterContext>> {

    @Test
    public void testWithNullConverterFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionNumberConverterFrom.with(null)
        );
    }

    @Test
    public void testWithExpressionNumberFromConverter() {
        final ExpressionNumberConverterFrom<ExpressionNumberConverterContext> converter = this.createConverter();
        assertSame(
                converter,
                ExpressionNumberConverterFrom.with(converter)
        );
    }

    // convert..........................................................................................................

    @Test
    public void testConvertTrueToStringFails() {
        this.convertFails(
                true,
                String.class
        );
    }

    @Test
    public void testConvertNullToString() {
        this.convertToStringAndCheck(
                null,
                null
        );
    }

    @Test
    public void testConvertByteToString() {
        this.convertToStringAndCheck(
                (byte) 123,
                "123"
        );
    }

    @Test
    public void testConvertShortToString() {
        this.convertToStringAndCheck(
                (short) 123,
                "123"
        );
    }

    @Test
    public void testConvertIntegerToString() {
        this.convertToStringAndCheck(
                123,
                "123"
        );
    }

    @Test
    public void testConvertLongToString() {
        this.convertToStringAndCheck(
                123L,
                "123"
        );
    }

    @Test
    public void testConvertFloatToString() {
        this.convertToStringAndCheck(
                128.5f,
                "128.5"
        );
    }

    @Test
    public void testConvertDoubleToString() {
        this.convertToStringAndCheck(
                128.5,
                "128.5"
        );
    }

    @Test
    public void testConvertBigIntegerToString() {
        this.convertToStringAndCheck(
                BigInteger.valueOf(123),
                "123"
        );
    }

    @Test
    public void testConvertBigDecimalToString() {
        this.convertToStringAndCheck(
                BigDecimal.valueOf(128.5),
                "128.5"
        );
    }

    @Test
    public void testConvertExpressionNumberBigDecimalToString() {
        this.convertToStringAndCheck(
                ExpressionNumberKind.BIG_DECIMAL.create(BigDecimal.valueOf(128.5)),
                "128.5"
        );
    }

    @Test
    public void testConvertExpressionNumberDouble() {
        this.convertToStringAndCheck(
                ExpressionNumberKind.DOUBLE.create(128.5
                ),
                "128.5");
    }

    private void convertToStringAndCheck(final Object value,
                                         final String expected) {
        this.convertAndCheck(

                value,
                String.class,
                expected
        );
    }

    // convert to same...................................................................................................

    @Test
    public void testConvertDoubleToDoubleDelegates() {
        final Converter<ExpressionNumberConverterContext> converter = Converters.numberNumber();
        this.convertAndCheck(
                converter,
                1.0,
                Double.class,
                1.0
        );

        this.convertAndCheck(
                ExpressionNumberConverterFrom.with(
                        converter
                ),
                1.0,
                Double.class,
                1.0
        );
    }

    @Test
    public void testConvertDoubleToDoubleDelegates2() {
        final Converter<ExpressionNumberConverterContext> converter = Converters.never();
        this.convertFails(
                converter,
                1.0,
                Double.class
        );

        this.convertFails(
                ExpressionNumberConverterFrom.with(
                        converter
                ),
                1.0,
                Double.class
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createConverter(),
                "ExpressionNumber? DecimalFormat.getInstance"
        );
    }

    @Override
    public ExpressionNumberConverterFrom<ExpressionNumberConverterContext> createConverter() {
        return ExpressionNumberConverterFrom.with(
                Converters.numberString(
                        new Function<>() {
                            @Override
                            public DecimalFormat apply(final DecimalNumberContext decimalNumberContext) {
                                return (DecimalFormat) DecimalFormat.getInstance();
                            }

                            // want to override toString
                            @Override
                            public String toString() {
                                return "DecimalFormat.getInstance";
                            }
                        }
                )
        );
    }

    @Override
    public Class<ExpressionNumberConverterFrom<ExpressionNumberConverterContext>> type() {
        return Cast.to(ExpressionNumberConverterFrom.class);
    }
}