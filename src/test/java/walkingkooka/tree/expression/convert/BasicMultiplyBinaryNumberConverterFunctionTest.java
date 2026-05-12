/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.Either;
import walkingkooka.ToStringTesting;
import walkingkooka.convert.BinaryNumberConverterFunctionTesting2;
import walkingkooka.convert.Converter;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.math.BigInteger;
import java.math.MathContext;

public final class BasicMultiplyBinaryNumberConverterFunctionTest implements BinaryNumberConverterFunctionTesting2<BasicMultiplyBinaryNumberConverterFunction<FakeExpressionNumberConverterContext>, FakeExpressionNumberConverterContext>,
    ClassTesting<BasicMultiplyBinaryNumberConverterFunction<FakeExpressionNumberConverterContext>>,
    ToStringTesting<BasicMultiplyBinaryNumberConverterFunction<FakeExpressionNumberConverterContext>> {

    private final static ExpressionNumberKind KIND = ExpressionNumberKind.BIG_DECIMAL;

    @Test
    public void testApplyExpressionNumberExpressionNumberToExpressionNumber() {
        this.applyAndCheck(
            this.createBinaryNumberConverterFunction(),
            KIND.create(2),
            KIND.create(3),
            ExpressionNumber.class,
            this.createContext(),
            KIND.create(6)
        );
    }

    @Test
    public void testApplyExpressionNumberExpressionNumberToNumber() {
        this.applyAndCheck(
            this.createBinaryNumberConverterFunction(),
            KIND.create(2),
            KIND.create(3),
            Number.class,
            this.createContext(),
            KIND.create(6)
        );
    }

    @Test
    public void testApplyFloatDoubleToInteger() {
        this.applyAndCheck(
            this.createBinaryNumberConverterFunction(),
            2f,
            3.0,
            Integer.class,
            this.createContext(),
            6
        );
    }

    @Test
    public void testApplyIntegerIntegerToInteger() {
        this.applyAndCheck(
            this.createBinaryNumberConverterFunction(),
            2,
            3,
            Integer.class,
            this.createContext(),
            6
        );
    }

    @Test
    public void testApplyIntegerIntegerToLong() {
        this.applyAndCheck(
            this.createBinaryNumberConverterFunction(),
            2,
            3,
            Long.class,
            this.createContext(),
            6L
        );
    }

    @Test
    public void testApplyIntegerIntegerToBigInteger() {
        this.applyAndCheck(
            this.createBinaryNumberConverterFunction(),
            2,
            3,
            BigInteger.class,
            this.createContext(),
            new BigInteger("6")
        );
    }

    @Test
    public void testApplyIntegerIntegerToExpressionNumber() {
        this.applyAndCheck(
            this.createBinaryNumberConverterFunction(),
            2,
            3,
            ExpressionNumber.class,
            this.createContext(),
            KIND.create(6)
        );
    }

    @Override
    public BasicMultiplyBinaryNumberConverterFunction<FakeExpressionNumberConverterContext> createBinaryNumberConverterFunction() {
        return BasicMultiplyBinaryNumberConverterFunction.instance();
    }

    @Override
    public FakeExpressionNumberConverterContext createContext() {
        return new FakeExpressionNumberConverterContext() {

            @Override
            public ExpressionNumberKind expressionNumberKind() {
                return KIND;
            }

            @Override
            public MathContext mathContext() {
                return MathContext.UNLIMITED;
            }

            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> target) {
                return this.converter.convert(
                    value,
                    target,
                    this
                );
            }

            private final Converter<FakeExpressionNumberConverterContext> converter = ExpressionNumberConverters.numberToNumber();
        };
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBinaryNumberConverterFunction(),
            BasicMultiplyBinaryNumberConverterFunction.class.getName()
        );
    }

    // class............................................................................................................

    @Override
    public Class<BasicMultiplyBinaryNumberConverterFunction<FakeExpressionNumberConverterContext>> type() {
        return Cast.to(BasicMultiplyBinaryNumberConverterFunction.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
