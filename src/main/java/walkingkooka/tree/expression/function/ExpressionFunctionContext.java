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

import walkingkooka.Context;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.CanConvert;
import walkingkooka.datetime.YearContext;
import walkingkooka.locale.HasLocale;
import walkingkooka.math.HasMathContext;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationException;
import walkingkooka.tree.expression.ExpressionEvaluationReferenceException;
import walkingkooka.tree.expression.ExpressionNumberContext;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;
import java.util.Optional;

/**
 * Context that accompanies a {@link ExpressionFunction}.
 */
public interface ExpressionFunctionContext extends Context,
        CanConvert,
        ExpressionNumberContext,
        HasLocale,
        HasMathContext,
        YearContext {

    /**
     * Returns the {@link ExpressionFunction} with the given {@link FunctionExpressionName}.
     */
    ExpressionFunction<?, ExpressionFunctionContext> function(final FunctionExpressionName name);

    /**
     * This method is called with each parameter and value pair, prior to invoking the function.
     * <br>
     * This provides an opportunity to convert the value to the required parameter type if the language requires such
     * semantics.
     */
    <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                           final Object value);

    /**
     * Constant for functions without any parameters.
     */
    List<Object> NO_PARAMETERS = Lists.empty();

    /**
     * Locates a function with the given name and then executes it with the provided parameter values.
     */
    Object evaluate(final FunctionExpressionName name, final List<Object> parameters);

    /**
     * Locates the value or a {@link Expression} for the given {@link ExpressionReference}
     */
    Optional<Object> reference(final ExpressionReference reference);

    /**
     * Locates the value for the given {@link ExpressionReference} or throws a
     * {@link ExpressionEvaluationReferenceException}.
     */
    default Object referenceOrFail(final ExpressionReference reference) {
        return this.reference(reference)
                .orElseThrow(() -> this.referenceNotFound(reference));
    }

    /**
     * Returns a {@link ExpressionEvaluationException} that captures the given {@link ExpressionReference} was not found.
     */
    default ExpressionEvaluationException referenceNotFound(final ExpressionReference reference) {
        return ExpressionFunctionContexts.referenceNotFound().apply(reference);
    }
}
