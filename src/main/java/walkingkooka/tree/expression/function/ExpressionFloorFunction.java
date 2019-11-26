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

package walkingkooka.tree.expression.function;

import walkingkooka.tree.expression.FunctionExpressionName;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * A floor function that expects a single number.
 */
final class ExpressionFloorFunction extends ExpressionNumberFunction2 {

    /**
     * Singleton
     */
    static final ExpressionFloorFunction INSTANCE = new ExpressionFloorFunction();

    /**
     * Private ctor
     */
    private ExpressionFloorFunction() {
        super();
    }

    @Override
    Number applyBigDecimal(final BigDecimal number) {
        return number.setScale(0, RoundingMode.FLOOR);
    }

    @Override
    Number applyBigInteger(final BigInteger number) {
        return number;
    }

    @Override
    Number applyDouble(final Double number) {
        return Math.floor(number);
    }

    @Override
    Number applyLong(final Long number) {
        return number;
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    private final static FunctionExpressionName NAME = FunctionExpressionName.with("floor");
}
