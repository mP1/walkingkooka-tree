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

import walkingkooka.tree.expression.ExpressionReference;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link Function} that handles translating {@link ExpressionReference} into possible values and is given to {@link walkingkooka.tree.expression.ExpressionEvaluationContext#context(Function)}
 * for that purpose.
 */
final class ExpressionFunctionLambdaExpressionEvaluationContextFunction implements Function<ExpressionReference, Optional<Optional<Object>>> {

    static ExpressionFunctionLambdaExpressionEvaluationContextFunction with(final List<ExpressionFunctionParameter<?>> parameters,
                                                                            final List<Object> values) {
        return new ExpressionFunctionLambdaExpressionEvaluationContextFunction(
                parameters,
                values
        );
    }

    private ExpressionFunctionLambdaExpressionEvaluationContextFunction(final List<ExpressionFunctionParameter<?>> parameters,
                                                                        final List<Object> values) {
        this.parameters = parameters;
        this.values = values;
    }

    @Override
    public Optional<Optional<Object>> apply(final ExpressionReference reference) {
        Optional<Object> value = null;

        int i = 0;

        for (final ExpressionFunctionParameter<?> parameter : this.parameters) {
            if (reference.testParameterName(
                    parameter.name()
            )) {
                value = Optional.ofNullable(
                        this.values.get(i)
                );
                break;
            }
            i++;
        }

        return Optional.ofNullable(value);
    }

    /**
     * Provides the names for each parameter, and the type
     */
    private final List<ExpressionFunctionParameter<?>> parameters;

    /**
     * The values to the lambda function when executed.
     */
    private final List<Object> values;

    @Override
    public String toString() {
        return this.parameters.toString();
    }
}
