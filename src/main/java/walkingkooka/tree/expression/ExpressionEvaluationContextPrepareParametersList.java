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

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Wraps the {@link List} of parameters values and performs several actions lazily for each parameter.
 * <ul>
 * <li>Evaluate {@link Expression} if {@link ExpressionFunctionKind#REQUIRES_EVALUATED_PARAMETERS}</li>
 * <li>Resolve {@link ReferenceExpression} if {@link ExpressionFunctionKind#RESOLVE_REFERENCES}</li>
 * <li>Convert values to the {@link ExpressionFunctionParameter#type()}</li>
 * </ul>
 * The above list is only performed once for each parameter and cached for future fetches.
 */
final class ExpressionEvaluationContextPrepareParametersList extends AbstractList<Object> {

    static ExpressionEvaluationContextPrepareParametersList with(final List<Object> parameters,
                                                                 final ExpressionFunction<?, ExpressionFunctionContext> function,
                                                                 final ExpressionEvaluationContext context) {
        return new ExpressionEvaluationContextPrepareParametersList(
                parameters,
                function,
                context
        );
    }

    private ExpressionEvaluationContextPrepareParametersList(final List<Object> parameters,
                                                             final ExpressionFunction<?, ExpressionFunctionContext> function,
                                                             final ExpressionEvaluationContext context) {
        this.parameters = parameters.toArray(new Object[parameters.size()]);

        this.function = function;
        this.context = context;
    }

    @Override
    public Object get(final int index) {
        final Object[] parametersValues = this.parameters;
        final ExpressionFunction<?, ExpressionFunctionContext> function = this.function;
        final ExpressionEvaluationContext context = this.context;

        Object parameterValue = parametersValues[index];

        final Set<ExpressionFunctionKind> kinds = function.kinds();

        if (parameterValue instanceof Expression) {
            if (kinds.contains(ExpressionFunctionKind.REQUIRES_EVALUATED_PARAMETERS)) {
                parameterValue = this.toReferenceOrValue(parameterValue);
                parametersValues[index] = parameterValue;
            }
        }
        if (parameterValue instanceof ExpressionReference) {
            if (kinds.contains(ExpressionFunctionKind.RESOLVE_REFERENCES)) {
                parameterValue = context.referenceOrFail((ExpressionReference) parameterValue);

                if (parameterValue instanceof Expression && kinds.contains(ExpressionFunctionKind.REQUIRES_EVALUATED_PARAMETERS)) {
                    parameterValue = this.toReferenceOrValue(parameterValue);
                }
                parametersValues[index] = parameterValue;
            }
        }

        // should ExpressionFunction include a shouldConvertToParametersType ???
        return context.prepareParameter(
                function.parameter(index),
                parameterValue
        );
    }

    private Object toReferenceOrValue(final Object parameter) {
        final Expression expression = (Expression) parameter;
        return expression.toReferenceOrValue(this.context);
    }

    /**
     * The function these parameters belong too. The function will provide numerous parameters about how to prepare the
     * parameters if at all.
     */
    private final ExpressionFunction<?, ExpressionFunctionContext> function;

    /**
     * {@link ExpressionEvaluationContext context} used to resolve references and evaluate parameters to values.
     */
    private final ExpressionEvaluationContext context;

    @Override
    public int size() {
        return this.parameters.length;
    }

    /**
     * A copy of the original parameters, as an array, where elements are overwritten as values are evaluated or
     * references resolved.
     */
    private final Object[] parameters;

    @Override
    public String toString() {
        return Arrays.toString(this.parameters);
    }
}
