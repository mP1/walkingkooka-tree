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

package walkingkooka.tree.expression.function;

import walkingkooka.tree.expression.ExpressionEvaluationContext;

import java.util.List;
import java.util.Objects;

/**
 * Wraps an {@link ExpressionFunction} and has {@link ExpressionFunctionParameter} definitions different from those of the wrapped {@link ExpressionFunction}.
 * This will result in a different preparation of parameter value before the wrapped {@link ExpressionFunction} is executed.
 */
final class ExpressionFunctionParameterValuesParameters<T, C extends ExpressionEvaluationContext> extends ExpressionFunctionParameterValues<T, C> {

    static <T, C extends ExpressionEvaluationContext> ExpressionFunctionParameterValuesParameters<T, C> with(final List<ExpressionFunctionParameter<?>> parameters,
                                                                                                             final ExpressionFunction<T, C> function) {
        return new ExpressionFunctionParameterValuesParameters<>(
            Objects.requireNonNull(parameters, "parameters"),
            function
        );
    }

    private ExpressionFunctionParameterValuesParameters(final List<ExpressionFunctionParameter<?>> parameters,
                                                        final ExpressionFunction<T, C> function) {
        super(function);
        this.parameters = parameters;
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return this.parameters;
    }

    @Override
    public T apply(final List<Object> parameters,
                   final C context) {
        return this.function.apply(
            parameters,
            context
        );
    }

    /**
     * The different/custom parameters definitions that will be used to preprocess parameter values before the wrapped {@link #function} is executed.
     */
    private final List<ExpressionFunctionParameter<?>> parameters;
}
