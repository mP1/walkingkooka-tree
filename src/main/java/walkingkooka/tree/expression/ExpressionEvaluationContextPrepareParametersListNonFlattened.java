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
import walkingkooka.tree.expression.function.ExpressionFunctionContext;
import walkingkooka.tree.expression.function.ExpressionFunctionKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;
import java.util.Objects;

/**
 * A lazy {@link List} of the original parameter values, performing the following if they are enabled for this function.
 * <ul>
 * <li>Evaluate {@link Expression} if {@link ExpressionFunctionKind#EVALUATED_PARAMETERS}</li>
 * <li>Resolve {@link ReferenceExpression} if {@link ExpressionFunctionKind#RESOLVE_REFERENCES}</li>
 * <li>Convert values to the {@link ExpressionFunctionParameter#type()}</li>
 * </ul>
 * The above list is only performed once for each parameter and cached for future fetches.
 */
final class ExpressionEvaluationContextPrepareParametersListNonFlattened extends ExpressionEvaluationContextPrepareParametersList {

    static ExpressionEvaluationContextPrepareParametersListNonFlattened with(final List<Object> parameters,
                                                                             final ExpressionFunction<?, ExpressionFunctionContext> function,
                                                                             final ExpressionEvaluationContext context) {
        Objects.requireNonNull(parameters, "parameters");
        Objects.requireNonNull(function, "function");
        Objects.requireNonNull(context, "context");

        return new ExpressionEvaluationContextPrepareParametersListNonFlattened(
                parameters,
                function,
                context
        );
    }

    /**
     * Private ctor
     */
    private ExpressionEvaluationContextPrepareParametersListNonFlattened(final List<Object> parameters,
                                                                         final ExpressionFunction<?, ExpressionFunctionContext> function,
                                                                         final ExpressionEvaluationContext context) {
        super(
                parameters,
                function,
                context
        );
    }

    @Override
    Object prepareAndConvert(final int index) {
        final ExpressionFunction<?, ExpressionFunctionContext> function = this.function;
        final ExpressionEvaluationContext context = this.context;

        final Object prepared = prepareParameter(
                this.parametersList.get(index),
                function,
                context
        );

        Object result;

        try {
            result = context.prepareParameter(
                    function.parameter(index),
                    prepared
            );
        } catch (final RuntimeException exception) {
            result = context.handleException(exception);
        }

        return result;
    }
}
