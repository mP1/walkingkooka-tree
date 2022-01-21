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

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

/**
 * Wraps the {@link List} of parameters, lazily resolving each parameter to a value required by the function,
 * and lazily resolving references as well.
 */
final class ExpressionEvaluationContextParametersList extends AbstractList<Object> {

    static ExpressionEvaluationContextParametersList with(final List<Object> parameters,
                                                          final ExpressionFunction<?, ExpressionFunctionContext> function,
                                                          final ExpressionEvaluationContext context) {
        return new ExpressionEvaluationContextParametersList(
                parameters,
                function,
                context
        );
    }

    private ExpressionEvaluationContextParametersList(final List<Object> parameters,
                                                      final ExpressionFunction<?, ExpressionFunctionContext> function,
                                                      final ExpressionEvaluationContext context) {
        this.parameters = parameters.toArray(new Object[parameters.size()]);

        this.function = function;
        this.context = context;
    }

    @Override
    public Object get(final int index) {
        final Object[] parameters = this.parameters;

        Object parameter = parameters[index];

        if (parameter instanceof Expression) {
            if (this.function.requiresEvaluatedParameters()) {
                parameter = toReferenceOrValue(parameter);
                parameters[index] = parameter;
            }
        }
            if (parameter instanceof ExpressionReference) {
                if (this.function.resolveReferences()) {
                    parameter = this.context.referenceOrFail((ExpressionReference) parameter);

                    if (parameter instanceof Expression && this.function.requiresEvaluatedParameters()) {
                        parameter = toReferenceOrValue(parameter);
                    }
                    parameters[index] = parameter;
                }
            }

        return parameter;
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
