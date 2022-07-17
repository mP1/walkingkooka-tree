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
import walkingkooka.tree.expression.function.ExpressionFunctionParameterCardinality;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Wraps the {@link List} of parameters values and performs several actions lazily for each parameter.
 * <ul>
 * <li>Evaluate {@link Expression} if {@link ExpressionFunctionKind#EVALUATE_PARAMETERS}</li>
 * <li>Resolve {@link ReferenceExpression} if {@link ExpressionFunctionKind#RESOLVE_REFERENCES}</li>
 * <li>Convert values to the {@link ExpressionFunctionParameter#type()}</li>
 * </ul>
 * The above list is only performed once for each parameter and cached for future fetches.
 */
abstract class ExpressionEvaluationContextPrepareParametersList extends AbstractList<Object> {

    static List<Object> with(final List<Object> values,
                             final ExpressionFunction<?, ExpressionEvaluationContext> function,
                             final ExpressionEvaluationContext context) {
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(function, "function");
        Objects.requireNonNull(context, "context");

        final Set<ExpressionFunctionKind> kinds = function.kinds();
        return kinds.isEmpty() ?
                values :
                kinds.contains(ExpressionFunctionKind.FLATTEN) ?
                        ExpressionEvaluationContextPrepareParametersListFlattened.with(
                                values,
                                function,
                                context
                        ) :
                        ExpressionEvaluationContextPrepareParametersListNonFlattened.with(
                                values,
                                function,
                                context
                        );
    }

    // Note the value is not converted to the parameter type.
    static Object prepareValue(final Object value,
                               final ExpressionFunction<?, ExpressionEvaluationContext> function,
                               final ExpressionEvaluationContext context) {
        final Set<ExpressionFunctionKind> kinds = function.kinds();
        Object result = value;

        if (result instanceof Expression) {
            if (kinds.contains(ExpressionFunctionKind.EVALUATE_PARAMETERS)) {
                result = toReferenceOrValue(result, context);
            }
        }
        if (result instanceof ExpressionReference) {
            if (kinds.contains(ExpressionFunctionKind.RESOLVE_REFERENCES)) {
                try {
                    result = context.referenceOrFail((ExpressionReference) result);
                } catch (final RuntimeException cause) {
                    result = context.handleException(cause);
                }

                if (result instanceof Expression && kinds.contains(ExpressionFunctionKind.EVALUATE_PARAMETERS)) {
                    result = toReferenceOrValue(result, context);
                }
            }
        }

        return result;
    }

    private static Object toReferenceOrValue(final Object value,
                                             final ExpressionEvaluationContext context) {
        final Expression expression = (Expression) value;

        Object result;
        try {
            result = expression.toReferenceOrValue(context);
        } catch (final RuntimeException cause) {
            result = context.handleException(cause);
        }
        return result;
    }

    /**
     * Private ctor
     */
    ExpressionEvaluationContextPrepareParametersList(final List<Object> values,
                                                     final ExpressionFunction<?, ExpressionEvaluationContext> function,
                                                     final ExpressionEvaluationContext context) {
        this.valuesList = values;
        this.parameters = function.parameters();

        this.function = function;
        this.convert = function.kinds()
                .contains(ExpressionFunctionKind.CONVERT_PARAMETERS);

        this.context = context;

        this.values = new Object[values.size()];
        Arrays.fill(this.values, MISSING);
    }

    private final static Object MISSING = new Object();

    /**
     * Lazily with and convert the requested parameter to the {@link ExpressionFunctionParameter#type()}
     */
    @Override
    public final Object get(final int index) {
        if (MISSING == this.values[index]) {
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

            this.values[index] = this.prepareAndConvert(
                    this.valuesList.get(index),
                    parameter
            );
        }
        return this.values[index];
    }

    abstract Object prepareAndConvert(final Object value,
                                      final ExpressionFunctionParameter<?> parameter);

    /**
     * A flag will be true when the {@link ExpressionFunctionKind#CONVERT_PARAMETERS} is present.
     */
    final boolean convert;

    /**
     * The function these parameters belong too. The function will provide numerous parameters about how to with the
     * parameters if at all.
     */
    final ExpressionFunction<?, ExpressionEvaluationContext> function;

    /**
     * The parameters for this function.
     */
    final List<ExpressionFunctionParameter<?>> parameters;

    /**
     * {@link ExpressionEvaluationContext context} used to resolve references and evaluate parameters to values.
     */
    final ExpressionEvaluationContext context;

    @Override
    public final int size() {
        return this.values.length;
    }

    /**
     * The original unprepared parameter values.
     */
    final List<Object> valuesList;

    /**
     * A copy of the original parameters, as an array, where elements are overwritten as values are evaluated or
     * references resolved.
     */
    private final Object[] values;

    @Override
    public final String toString() {
        return this.valuesList.toString();
    }
}
