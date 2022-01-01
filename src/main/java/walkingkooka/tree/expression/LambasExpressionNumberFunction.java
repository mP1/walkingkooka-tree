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

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.DoubleUnaryOperator;

/**
 * A @link ExpressionNumberFunction} that is typically created using two lambdas and delegates to the right at runtime.
 */
final class LambasExpressionNumberFunction implements ExpressionNumberFunction {

    static LambasExpressionNumberFunction with(final BiFunction<BigDecimal, MathContext, BigDecimal> bigDecimal,
                                               final DoubleUnaryOperator doubleFunction) {
        Objects.requireNonNull(bigDecimal, "bigDecimal");
        Objects.requireNonNull(doubleFunction, "doubleFunction");

        return new LambasExpressionNumberFunction(bigDecimal, doubleFunction);
    }

    public LambasExpressionNumberFunction(final BiFunction<BigDecimal, MathContext, BigDecimal> bigDecimal,
                                          final DoubleUnaryOperator doubleFunction) {
        this.bigDecimal = bigDecimal;
        this.doubleFunction = doubleFunction;
    }

    @Override
    public BigDecimal mapBigDecimal(final BigDecimal value,
                                    final MathContext context) {
        return this.bigDecimal.apply(value, context);
    }

    private final BiFunction<BigDecimal, MathContext, BigDecimal> bigDecimal;

    @Override
    public double mapDouble(final double value) {
        return this.doubleFunction.applyAsDouble(value);
    }

    private final DoubleUnaryOperator doubleFunction;

    @Override
    public String toString() {
        return this.bigDecimal + " " + this.doubleFunction;
    }
}
