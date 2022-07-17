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
import walkingkooka.tree.expression.function.ExpressionFunctionKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;

/**
 * A lazy {@link List} of the original parameter values, performing the following if they are enabled for this function.
 * <ul>
 * <li>Evaluate {@link Expression} if {@link ExpressionFunctionKind#EVALUATE_PARAMETERS}</li>
 * <li>Resolve {@link ReferenceExpression} if {@link ExpressionFunctionKind#RESOLVE_REFERENCES}</li>
 * <li>Convert values to the {@link ExpressionFunctionParameter#type()}</li>
 * </ul>
 * The above list is only performed once for each parameter and cached for future fetches.
 */
final class ExpressionEvaluationContextPrepareParametersListNonFlattened extends ExpressionEvaluationContextPrepareParametersList {

    static ExpressionEvaluationContextPrepareParametersListNonFlattened with(final List<Object> values,
                                                                             final ExpressionFunction<?, ExpressionEvaluationContext> function,
                                                                             final ExpressionEvaluationContext context) {
        return new ExpressionEvaluationContextPrepareParametersListNonFlattened(
                values,
                function,
                context
        );
    }

    /**
     * Private ctor
     */
    private ExpressionEvaluationContextPrepareParametersListNonFlattened(final List<Object> values,
                                                                         final ExpressionFunction<?, ExpressionEvaluationContext> function,
                                                                         final ExpressionEvaluationContext context) {
        super(
                values,
                function,
                context
        );
    }

    @Override
    Object prepareAndConvert(final Object value,
                             final ExpressionFunctionParameter<?> parameter) {
        final ExpressionEvaluationContext context = this.context;

        Object result;

        try {
            final Object prepared = prepareValue(
                    value,
                    this.function,
                    context
            );

            result = this.convert ?
                    context.prepareParameter(
                            parameter,
                            prepared
                    ) :
                    prepared;
        } catch (final RuntimeException exception) {
            result = context.handleException(exception);
        }

        return result;
    }
}
