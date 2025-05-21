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

package walkingkooka.tree.expression.convert;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;

public final class NumberToNumberConverterTest implements ConverterTesting2<NumberToNumberConverter<FakeExpressionNumberConverterContext>, FakeExpressionNumberConverterContext>,
    ToStringTesting<NumberToNumberConverter<FakeExpressionNumberConverterContext>> {

    private final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.BIG_DECIMAL;

    @Test
    public void testConvertStringToStringFails() {
        this.convertFails(
            "Hello",
            String.class
        );
    }

    @Test
    public void testConvertExpressionNumberBigDecimalToNonNumber() {
        this.convertFails(
            ExpressionNumberKind.BIG_DECIMAL.create(123),
            String.class
        );
    }

    @Test
    public void testConvertExpressionNumberDoubleToNonNumber() {
        this.convertFails(
            ExpressionNumberKind.DOUBLE.create(123),
            String.class
        );
    }

    @Test
    public void testConvertNumberToNonNumber() {
        this.convertFails(
            123,
            String.class
        );
    }

    @Test
    public void testConvertNullToExpressionNumber() {
        this.convertAndCheck(
            (Object) null,
            ExpressionNumber.class,
            null
        );
    }

    @Test
    public void testConvertNullToExpressionNumberBigDecimal() {
        this.convertAndCheck(
            (Object) null,
            ExpressionNumberKind.BIG_DECIMAL.numberType(),
            null
        );
    }

    @Test
    public void testConvertNullToExpressionNumberDouble() {
        this.convertAndCheck(
            (Object) null,
            ExpressionNumberKind.DOUBLE.numberType(),
            null
        );
    }

    @Test
    public void testConvertExpressionNumberBigDecimalToExpressionNumberBigDecimal() {
        this.convertAndCheck(
            ExpressionNumberKind.BIG_DECIMAL.create(123)
        );
    }

    @Test
    public void testConvertExpressionNumberBigDecimalToExpressionNumberDouble() {
        final ExpressionNumber number = ExpressionNumberKind.BIG_DECIMAL.create(123);
        this.convertAndCheck(
            number,
            ExpressionNumber.class,
            this.createContext(ExpressionNumberKind.DOUBLE),
            number
        );
    }

    @Test
    public void testConvertExpressionNumberBigDecimalToInteger() {
        this.convertAndCheck(
            ExpressionNumberKind.BIG_DECIMAL.create(123),
            123
        );
    }

    @Test
    public void testConvertExpressionNumberBigDecimalToNumber() {
        this.convertAndCheck(
            ExpressionNumberKind.BIG_DECIMAL.create(123),
            Number.class
        );
    }

    @Test
    public void testConvertExpressionNumberDoubleToExpressionNumberBigDecimal() {
        final ExpressionNumber number = ExpressionNumberKind.DOUBLE.create(123);

        this.convertAndCheck(
            number,
            ExpressionNumber.class,
            this.createContext(ExpressionNumberKind.BIG_DECIMAL),
            number
        );
    }

    @Test
    public void testConvertExpressionNumberDoubleToExpressionNumberDouble() {
        this.convertAndCheck(
            ExpressionNumberKind.DOUBLE.create(123)
        );
    }

    @Test
    public void testConvertExpressionNumberDoubleToInteger() {
        this.convertAndCheck(
            ExpressionNumberKind.DOUBLE.create(123),
            123
        );
    }

    @Test
    public void testConvertExpressionNumberDoubleToNumber() {
        this.convertAndCheck(
            ExpressionNumberKind.DOUBLE.create(123),
            Number.class
        );
    }

    @Test
    public void testConvertIntegerToExpressionNumberBigDecimal() {
        this.convertAndCheck(
            123,
            ExpressionNumber.class,
            this.createContext(ExpressionNumberKind.BIG_DECIMAL),
            ExpressionNumberKind.DOUBLE.create(123)
        );
    }

    @Test
    public void testConvertIntegerToExpressionNumberDouble() {
        this.convertAndCheck(
            123,
            ExpressionNumber.class,
            this.createContext(ExpressionNumberKind.DOUBLE),
            ExpressionNumberKind.DOUBLE.create(123)
        );
    }

    @Test
    public void testConvertIntegerToInteger() {
        this.convertAndCheck(
            123
        );
    }

    @Test
    public void testConvertIntegerToLong() {
        this.convertAndCheck(
            123,
            123L
        );
    }

    @Test
    public void testConvertIntegerToNumber() {
        this.convertAndCheck(
            123,
            Number.class,
            123
        );
    }

    @Override
    public NumberToNumberConverter<FakeExpressionNumberConverterContext> createConverter() {
        return NumberToNumberConverter.instance();
    }

    @Override
    public FakeExpressionNumberConverterContext createContext() {
        return this.createContext(EXPRESSION_NUMBER_KIND);
    }

    private FakeExpressionNumberConverterContext createContext(final ExpressionNumberKind kind) {
        return new FakeExpressionNumberConverterContext() {
            @Override
            public ExpressionNumberKind expressionNumberKind() {
                return kind;
            }
        };
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            NumberToNumberConverter.instance(),
            "number to number"
        );
    }

    // class............................................................................................................

    @Override
    public Class<NumberToNumberConverter<FakeExpressionNumberConverterContext>> type() {
        return Cast.to(NumberToNumberConverter.class);
    }
}
