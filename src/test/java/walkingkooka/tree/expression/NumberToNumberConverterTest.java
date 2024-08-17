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
import walkingkooka.convert.ConverterTesting2;

public final class NumberToNumberConverterTest implements ConverterTesting2<NumberToNumberConverter<FakeExpressionNumberConverterContext>, FakeExpressionNumberConverterContext>,
        ToStringTesting<NumberToNumberConverter<FakeExpressionNumberConverterContext>> {

    private final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.BIG_DECIMAL;

    @Test
    public void testExpressionNumberBigDecimalToNonNumber() {
        this.convertFails(
                ExpressionNumberKind.BIG_DECIMAL.create(123),
                String.class
        );
    }

    @Test
    public void testExpressionNumberDoubleToNonNumber() {
        this.convertFails(
                ExpressionNumberKind.DOUBLE.create(123),
                String.class
        );
    }

    @Test
    public void testNumberToNonNumber() {
        this.convertFails(
                123,
                String.class
        );
    }

    @Test
    public void testExpressionNumberBigDecimalToExpressionNumberBigDecimal() {
        this.convertAndCheck(
                ExpressionNumberKind.BIG_DECIMAL.create(123)
        );
    }

    @Test
    public void testExpressionNumberBigDecimalToExpressionNumberDouble() {
        this.convertAndCheck(
                ExpressionNumberKind.BIG_DECIMAL.create(123),
                ExpressionNumber.class,
                this.createContext(ExpressionNumberKind.DOUBLE),
                ExpressionNumberKind.DOUBLE.create(123)
        );
    }

    @Test
    public void testExpressionNumberBigDecimalToInteger() {
        this.convertAndCheck(
                ExpressionNumberKind.BIG_DECIMAL.create(123),
                123
        );
    }

    @Test
    public void testExpressionNumberBigDecimalToNumber() {
        this.convertAndCheck(
                ExpressionNumberKind.BIG_DECIMAL.create(123),
                Number.class
        );
    }

    @Test
    public void testExpressionNumberDoubleToExpressionNumberBigDecimal() {
        this.convertAndCheck(
                ExpressionNumberKind.DOUBLE.create(123),
                ExpressionNumber.class,
                this.createContext(ExpressionNumberKind.BIG_DECIMAL),
                ExpressionNumberKind.BIG_DECIMAL.create(123)
        );
    }

    @Test
    public void testExpressionNumberDoubleToExpressionNumberDouble() {
        this.convertAndCheck(
                ExpressionNumberKind.DOUBLE.create(123)
        );
    }

    @Test
    public void testExpressionNumberDoubleToInteger() {
        this.convertAndCheck(
                ExpressionNumberKind.DOUBLE.create(123),
                123
        );
    }

    @Test
    public void testExpressionNumberDoubleToNumber() {
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
                ExpressionNumberKind.BIG_DECIMAL.create(123)
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
                "number-to-number"
        );
    }

    // class............................................................................................................

    @Override
    public Class<NumberToNumberConverter<FakeExpressionNumberConverterContext>> type() {
        return Cast.to(NumberToNumberConverter.class);
    }
}
