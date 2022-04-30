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
import java.util.Objects;
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
abstract class ExpressionEvaluationContextPrepareParametersList extends AbstractList<Object> {

    static ExpressionEvaluationContextPrepareParametersList with(final List<Object> parameters,
                                                                 final ExpressionFunction<?, ExpressionFunctionContext> function,
                                                                 final ExpressionEvaluationContext context) {
        Objects.requireNonNull(parameters, "parameters");
        Objects.requireNonNull(function, "function");
        Objects.requireNonNull(context, "context");

        return function.kinds().contains(ExpressionFunctionKind.FLATTEN) ?
                ExpressionEvaluationContextPrepareParametersListFlattened.with(
                        parameters,
                        function,
                        context
                ) :
                ExpressionEvaluationContextPrepareParametersListNonFlattened.with(
                        parameters,
                        function,
                        context
                );
    }

    static Object prepareParameter(final Object parameterValue,
                                   final ExpressionFunction<?, ExpressionFunctionContext> function,
                                   final ExpressionEvaluationContext context) {
        final Set<ExpressionFunctionKind> kinds = function.kinds();
        Object result = parameterValue;

        if (result instanceof Expression) {
            if (kinds.contains(ExpressionFunctionKind.REQUIRES_EVALUATED_PARAMETERS)) {
                result = toReferenceOrValue(result, context);
            }
        }
        if (result instanceof ExpressionReference) {
            if (kinds.contains(ExpressionFunctionKind.RESOLVE_REFERENCES)) {
                result = context.referenceOrFail((ExpressionReference) result);

                if (result instanceof Expression && kinds.contains(ExpressionFunctionKind.REQUIRES_EVALUATED_PARAMETERS)) {
                    result = toReferenceOrValue(result, context);
                }
            }
        }

        return result;
    }

    private static Object toReferenceOrValue(final Object parameter,
                                             final ExpressionEvaluationContext context) {
        final Expression expression = (Expression) parameter;
        return expression.toReferenceOrValue(context);
    }

    /**
     * Private ctor
     */
    ExpressionEvaluationContextPrepareParametersList(final List<Object> parameters,
                                                     final ExpressionFunction<?, ExpressionFunctionContext> function,
                                                     final ExpressionEvaluationContext context) {
        this.parametersList = parameters;
        this.function = function;
        this.context = context;

        this.parameters = new Object[parameters.size()];
        Arrays.fill(this.parameters, MISSING);
    }

    private final static Object MISSING = new Object();

    /**
     * Lazily with and convert the requested parameter to the {@link ExpressionFunctionParameter#type()}
     */
    @Override
    public final Object get(final int index) {
        if (MISSING == this.parameters[index]) {
            this.parameters[index] = prepareAndConvert(index);
        }
        return this.parameters[index];
    }

    abstract Object prepareAndConvert(final int index);

    /**
     * The function these parameters belong too. The function will provide numerous parameters about how to with the
     * parameters if at all.
     */
    final ExpressionFunction<?, ExpressionFunctionContext> function;

    /**
     * {@link ExpressionEvaluationContext context} used to resolve references and evaluate parameters to values.
     */
    final ExpressionEvaluationContext context;

    @Override
    public final int size() {
        return this.parameters.length;
    }

    /**
     * The original unprepared parameter values.
     */
    final List<Object> parametersList;

    /**
     * A copy of the original parameters, as an array, where elements are overwritten as values are evaluated or
     * references resolved.
     */
    private final Object[] parameters;

    @Override
    public final String toString() {
        return this.parametersList.toString();
    }
}
