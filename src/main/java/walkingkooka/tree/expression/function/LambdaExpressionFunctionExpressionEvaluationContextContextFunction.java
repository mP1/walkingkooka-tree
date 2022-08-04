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
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * A {@link Function} that handles translating {@link ExpressionReference} into possible values and is given to {@link walkingkooka.tree.expression.ExpressionEvaluationContext#context(Function)}
 * for that purpose.
 */
final class LambdaExpressionFunctionExpressionEvaluationContextContextFunction implements Function<ExpressionReference, Optional<Object>> {

    static LambdaExpressionFunctionExpressionEvaluationContextContextFunction with(final List<ExpressionFunctionParameter<?>> parameters,
                                                                                   final List<Object> values,
                                                                                   final BiPredicate<ExpressionFunctionParameterName, ExpressionReference> parameterMatcher) {
        return new LambdaExpressionFunctionExpressionEvaluationContextContextFunction(
                parameters,
                values,
                parameterMatcher
        );
    }

    private LambdaExpressionFunctionExpressionEvaluationContextContextFunction(final List<ExpressionFunctionParameter<?>> parameters,
                                                                               final List<Object> values,
                                                                               final BiPredicate<ExpressionFunctionParameterName, ExpressionReference> parameterMatcher) {
        this.parameters = parameters;
        this.values = values;
        this.parameterMatcher = parameterMatcher;
    }

    @Override
    public Optional<Object> apply(final ExpressionReference reference) {
        Object value = null;

        final BiPredicate<ExpressionFunctionParameterName, ExpressionReference> parameterMatcher = this.parameterMatcher;

        int i = 0;

        for (final ExpressionFunctionParameter<?> parameter : this.parameters) {
            if (parameterMatcher.test(
                    parameter.name(),
                    reference
            )) {
                value = this.values.get(i); // TODO need to special case null being returned here.
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

    /**
     * This is used to match {@link ExpressionReference} with a {@link ExpressionFunctionParameterName},
     * eg matching only a spreadsheet label with a parameter name.
     */
    private final BiPredicate<ExpressionFunctionParameterName, ExpressionReference> parameterMatcher;

    @Override
    public String toString() {
        return this.parameters.toString();
    }
}
