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

package walkingkooka.tree.expression;

import walkingkooka.Context;
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.ConvertOrFailFunction;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.HasMathContext;

import java.util.List;
import java.util.Optional;

/**
 * Context that travels during any expression evaluation.
 */
public interface ExpressionEvaluationContext extends Context, ConvertOrFailFunction, DecimalNumberContext, ExpressionNumberReducerContext, HasMathContext {

    /**
     * Constant for functions without any parameters.
     */
    List<Object> NO_PARAMETERS = Lists.empty();

    /**
     * Locates a function with the given name and then executes it with the provided parameter values (not {@link Expression}
     */
    Object function(final FunctionExpressionName name, final List<Object> parameters);

    /**
     * Locates the value or a {@link Expression} for the given {@link ExpressionReference}
     */
    Optional<Expression> reference(final ExpressionReference reference);

    /**
     * Locates the value or a {@link Expression} for the given {@link ExpressionReference} or throws a
     * {@link ExpressionEvaluationReferenceException}.
     */
    default Expression referenceOrFail(final ExpressionReference reference) {
        return this.reference(reference).orElseThrow(() -> new ExpressionEvaluationReferenceException("Unable to find " + reference));
    }

    /**
     * Handles converting the given value to the requested {@link Class target type}.
     */
    <T> Either<T, String> convert(final Object value, final Class<T> target);

    /**
     * Converts the given value to the {@link Class target type} or throws a {@link ExpressionEvaluationConversionException}
     */
    default <T> T convertOrFail(final Object value,
                                final Class<T> target) {
        final Either<T, String> converted = this.convert(value, target);
        if (converted.isRight()) {
            throw new ExpressionEvaluationConversionException(converted.rightValue());
        }

        return converted.leftValue();
    }
}
