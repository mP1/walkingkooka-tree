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

import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterCardinality;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Wraps the {@link List} of parameters values and performs several actions lazily for each parameter.
 * <ul>
 * <li>Evaluate {@link Expression} if {@link ExpressionFunctionParameterKind#EVALUATE}</li>
 * <li>Resolve {@link ReferenceExpression} if {@link ExpressionFunctionParameterKind#RESOLVE_REFERENCES}</li>
 * <li>Convert values to the {@link ExpressionFunctionParameter#type()}</li>
 * </ul>
 * The above list is only performed once for each parameter and cached for future fetches.
 */
abstract class ExpressionEvaluationContextPrepareParametersList extends AbstractList<Object> {

    static List<Object> with(final List<ExpressionFunctionParameter<?>> parameters,
                             final List<Object> values,
                             final ExpressionEvaluationContext context) {
        Objects.requireNonNull(parameters, "parameters");
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(context, "context");

        return parameters.isEmpty() ?
                values :
                withNotEmpty(
                        parameters,
                        unwrap(values),
                        context
                );
    }

    @SuppressWarnings("lgtm[java/abstract-to-concrete-cast]")
    private static List<Object> unwrap(final List<Object> values) {
        return values instanceof ExpressionEvaluationContextPrepareParametersList ?
                ((ExpressionEvaluationContextPrepareParametersList) values).values :
                values;
    }

    /**
     * Factory that tests if the last parameter is {@link ExpressionFunctionParameterKind#FLATTEN} and creates
     * the appropriate sub-class.
     */
    private static List<Object> withNotEmpty(final List<ExpressionFunctionParameter<?>> parameters,
                                             final List<Object> values,
                                             final ExpressionEvaluationContext context) {
        final int count = parameters.size();
        final ExpressionFunctionParameter<?> last = parameters.get(count - 1);

        return last.kinds().contains(ExpressionFunctionParameterKind.FLATTEN) ?
                ExpressionEvaluationContextPrepareParametersListFlattened.withFlattened(
                        parameters,
                        values,
                        count - 1,
                        last,
                        context
                ) :
                ExpressionEvaluationContextPrepareParametersListNonFlattened.withNonFlattened(
                        parameters,
                        values,
                        values.size(),
                        context
                );
    }

    /**
     * Private package ctor to limit sub classing.
     */
    ExpressionEvaluationContextPrepareParametersList(final List<ExpressionFunctionParameter<?>> parameters,
                                                     final List<Object> values,
                                                     final int preparedValuesCount,
                                                     final ExpressionEvaluationContext context) {
        this.parameters = parameters;
        this.values = values;
        this.context = context;

        this.preparedValues = preparedValues(preparedValuesCount);
    }

    static Object[] preparedValues(final int count) {
        final Object[] prepared = new Object[count];
        Arrays.fill(prepared, MISSING);
        return prepared;
    }

    final Object getPrepareIfNecessary(final int index) {
        Object preparedValue = this.preparedValues[index];

        if (MISSING == preparedValue) {
            final List<ExpressionFunctionParameter<?>> parameters = this.parameters;
            final int count = parameters.size();

            final ExpressionFunctionParameter<?> parameter;

            if (index < count) {
                parameter = parameters.get(index);
            } else {
                parameter = parameters.get(count - 1);
                if (parameter.cardinality() != ExpressionFunctionParameterCardinality.VARIABLE) {
                    throw new ArrayIndexOutOfBoundsException("Unknown parameter " + index + " expected only " + count);
                }
            }

            preparedValue = prepareAndConvert(
                    parameter,
                    this.values.get(index)
            );
            this.preparedValues[index] = preparedValue;
        }
        return preparedValue;
    }

    final Object prepareValue(final ExpressionFunctionParameter<?> parameter,
                              final Object value) {
        final ExpressionEvaluationContext context = this.context;
        final Set<ExpressionFunctionParameterKind> kinds = parameter.kinds();
        Object result = value;

        int i = 0;
        int j;
        do {
            j = i;

            if (result instanceof Expression && kinds.contains(ExpressionFunctionParameterKind.EVALUATE)) {
                final Expression expression = (Expression) result;

                Object result1;
                try {
                    result1 = expression.toReferenceOrValue(context);
                } catch (final RuntimeException cause) {
                    result1 = context.handleException(cause);
                }
                result = result1;
                i++;
            }
            if (result instanceof ExpressionReference && kinds.contains(ExpressionFunctionParameterKind.RESOLVE_REFERENCES)) {
                result = context.referenceOrFail((ExpressionReference) result);
                i++;
            }
        } while (i != j);

        return result;
    }

    final Object prepareAndConvert(final ExpressionFunctionParameter<?> parameter,
                                   final Object value) {
        Object result;

        final ExpressionEvaluationContext context = this.context;
        try {
            final Object prepared = prepareValue(
                    parameter,
                    value
            );

            result = parameter.kinds()
                    .contains(ExpressionFunctionParameterKind.CONVERT) ?
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

    /**
     * Dummy placeholder that appears in {@link #preparedValues} and indicates the value has not be prepared and cached.
     */
    final static Object MISSING = new Object();

    /**
     * The original unprepared parameter values.
     */
    final List<Object> values;

    /**
     * A cache of all the values after any additional processing due to {@link ExpressionFunctionParameterKind}.
     */
    final Object[] preparedValues;

    /**
     * The parameters for this namedFunction.
     */
    final List<ExpressionFunctionParameter<?>> parameters;

    /**
     * {@link ExpressionEvaluationContext context} used to resolve references and evaluate parameters to values.
     */
    final ExpressionEvaluationContext context;
}
