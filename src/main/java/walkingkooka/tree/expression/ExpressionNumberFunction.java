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

import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctions;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Optional;

/**
 * A namedFunction that may be applied to a {@link ExpressionNumber}. The appropriate method is called depending on the
 * {@link ExpressionNumberKind}.
 */
public interface ExpressionNumberFunction {

    BigDecimal mapBigDecimal(final BigDecimal value, final MathContext context);

    double mapDouble(final double value);

    /**
     * Factory that creates a {@link ExpressionFunction} with the provided name that accepts a single ExpressionNumber, maps that
     * with this {@link ExpressionNumberFunction} and returns a {@link ExpressionNumber}.
     * This namedFunction does not handle lists or var args.
     */
    default <C extends ExpressionEvaluationContext> ExpressionFunction<ExpressionNumber, C> function(final Optional<ExpressionFunctionName> name) {
        return ExpressionFunctions.expressionNumberFunction(
            name,
            this
        );
    }
}
