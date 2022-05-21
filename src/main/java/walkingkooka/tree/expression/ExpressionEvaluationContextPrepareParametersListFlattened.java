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

import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterCardinality;

import java.util.List;
import java.util.Objects;

/**
 * A partially lazy list, note values are evaluated and references resolved if necessary but the convert to the actual
 * parameter type is performed when the parameter is gotten.
 */
final class ExpressionEvaluationContextPrepareParametersListFlattened extends ExpressionEvaluationContextPrepareParametersList {

    static ExpressionEvaluationContextPrepareParametersListFlattened with(final List<Object> parameters,
                                                                          final ExpressionFunction<?, ExpressionEvaluationContext> function,
                                                                          final ExpressionEvaluationContext context) {
        Objects.requireNonNull(parameters, "parameters");
        Objects.requireNonNull(function, "function");
        Objects.requireNonNull(context, "context");

        return new ExpressionEvaluationContextPrepareParametersListFlattened(
                flatten(
                        parameters,
                        function,
                        context
                ),
                function,
                context
        );
    }

    private static List<Object> flatten(final List<Object> parameters,
                                        final ExpressionFunction<?, ExpressionEvaluationContext> function,
                                        final ExpressionEvaluationContext context) {
        final ExpressionFunctionParameter<?> parameter = function.parameter(0);
        final ExpressionFunctionParameterCardinality cardinality = parameter.cardinality();
        if (ExpressionFunctionParameterCardinality.VARIABLE != cardinality) {
            throw new IllegalStateException("Function " + function + " has " + ExpressionFunctionKind.FLATTEN + " should only have a single parameter");
        }

        final List<Object> parameterValues = Lists.array();

        for (final Object parameterValue : parameters) {
            prepareParameterAndFlatten(
                    parameterValue,
                    parameterValues,
                    parameter,
                    function,
                    context
            );
        }

        return Lists.immutable(parameterValues);
    }

    private static void prepareParameterAndFlatten(final Object parameterValue,
                                                   final List<Object> parametersValues,
                                                   final ExpressionFunctionParameter<?> parameter,
                                                   final ExpressionFunction<?, ExpressionEvaluationContext> function,
                                                   final ExpressionEvaluationContext context) {
        final Object prepared = prepareParameter(
                parameterValue,
                function,
                context
        );
        if (prepared instanceof List) {
            prepareParameterAndFlatten(
                    (List<?>) prepared,
                    parametersValues,
                    parameter,
                    function,
                    context
            );
        } else {
            parametersValues.add(
                    prepared
            );
        }
    }

    private static void prepareParameterAndFlatten(final List<?> unflattenedParameterValues,
                                                   final List<Object> flattenParametersValues,
                                                   final ExpressionFunctionParameter<?> parameter,
                                                   final ExpressionFunction<?, ExpressionEvaluationContext> function,
                                                   final ExpressionEvaluationContext context) {
        for (final Object parameterValue : unflattenedParameterValues) {
            prepareParameterAndFlatten(
                    parameterValue,
                    flattenParametersValues,
                    parameter,
                    function,
                    context
            );
        }
    }

    /**
     * Private ctor
     */
    private ExpressionEvaluationContextPrepareParametersListFlattened(final List<Object> parameters,
                                                                      final ExpressionFunction<?, ExpressionEvaluationContext> function,
                                                                      final ExpressionEvaluationContext context) {
        super(parameters, function, context);
    }

    @Override
    Object prepareAndConvert(final int index) {
        final ExpressionEvaluationContext context = this.context;

        Object result;
        try {
            final Object value = this.parametersList.get(index);

            result = this.convert ?
                    context.prepareParameter(
                            this.function.parameter(index),
                            value
                    ) :
                    value;
        } catch (final RuntimeException cause) {
            result = context.handleException(cause);
        }

        return result;
    }
}
