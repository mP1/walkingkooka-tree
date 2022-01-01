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

import walkingkooka.reflect.PublicStaticHelper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;
import java.util.function.DoubleUnaryOperator;

public final class ExpressionNumberFunctions implements PublicStaticHelper {

    /**
     * {@see FakeExpressionNumberFunction}
     */
    public static ExpressionNumberFunction fake() {
        return new FakeExpressionNumberFunction();
    }

    /**
     * {@see LambasExpressionNumberFunction}
     */
    public static ExpressionNumberFunction lambdas(final BiFunction<BigDecimal, MathContext, BigDecimal> bigDecimal,
                                                   final DoubleUnaryOperator doubleFunction) {
        return LambasExpressionNumberFunction.with(bigDecimal, doubleFunction);
    }

    /**
     * Stop creation
     */
    private ExpressionNumberFunctions() {
        throw new UnsupportedOperationException();
    }
}
