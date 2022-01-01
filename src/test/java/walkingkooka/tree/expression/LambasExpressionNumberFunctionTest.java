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
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;
import java.util.function.DoubleUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class LambasExpressionNumberFunctionTest implements ExpressionNumberFunctionTesting<LambasExpressionNumberFunction>,
        ClassTesting<LambasExpressionNumberFunction>,
        ToStringTesting<LambasExpressionNumberFunction> {

    private final static BiFunction<BigDecimal, MathContext, BigDecimal> BIG_DECIMAL = (b, c) -> b.multiply(BigDecimal.TEN);
    private final static DoubleUnaryOperator DOUBLE = (d) -> d * 3;

    @Test
    public void testWithNullBigDecimalFails() {
        assertThrows(
                NullPointerException.class,
                () -> LambasExpressionNumberFunction.with(null, DOUBLE)
        );
    }

    @Test
    public void testWithNullDoubleFails() {
        assertThrows(
                NullPointerException.class,
                () -> LambasExpressionNumberFunction.with(BIG_DECIMAL, null)
        );
    }

    @Test
    public void testMapBigDecimal() {
        this.mapBigDecimalAndCheck(
                BigDecimal.valueOf(3),
                MathContext.DECIMAL32,
                BigDecimal.valueOf(3 * 10)
        );
    }

    @Test
    public void testMapDouble() {
        this.mapDoubleAndCheck(
                5,
                5 * 3
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createExpressionNumberFunction(), BIG_DECIMAL + " " + DOUBLE);
    }

    @Override
    public LambasExpressionNumberFunction createExpressionNumberFunction() {
        return LambasExpressionNumberFunction.with(BIG_DECIMAL, DOUBLE);
    }

    @Override
    public Class<LambasExpressionNumberFunction> type() {
        return LambasExpressionNumberFunction.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
