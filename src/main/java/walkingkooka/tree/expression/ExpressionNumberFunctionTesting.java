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

import walkingkooka.test.Testing;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface ExpressionNumberFunctionTesting<F extends ExpressionNumberFunction> extends Testing {

    default void mapBigDecimalAndCheck(final BigDecimal value,
                                       final RoundingMode roundingMode,
                                       final BigDecimal expected) {
        this.mapBigDecimalAndCheck(
                value,
                roundingMode,
                expected
        );
    }

    default void mapBigDecimalAndCheck(final ExpressionNumberFunction function,
                                       final BigDecimal value,
                                       final RoundingMode roundingMode,
                                       final BigDecimal expected) {
        this.checkEquals(
                expected,
                function.mapBigDecimal(value, roundingMode),
                () -> "mapBigDecimal " + value
        );
    }

    default void mapDoubleAndCheck(final double value,
                                   final double expected) {
        this.mapDoubleAndCheck(
                this.createExpressionNumberFunction(),
                value,
                expected
        );
    }

    default void mapDoubleAndCheck(final ExpressionNumberFunction function,
                                   final double value,
                                   final double expected) {
        this.checkEquals(
                expected,
                function.mapDouble(value),
                () -> "mapDouble " + value
        );
    }

    F createExpressionNumberFunction();
}
