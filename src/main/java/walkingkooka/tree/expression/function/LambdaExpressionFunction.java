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

import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link ExpressionFunction} that supports binding given parameter values using the parameter names to a {@link ExpressionEvaluationContext} before invoking a {@link Function}.
 */
final class LambdaExpressionFunction<T, C extends ExpressionEvaluationContext> implements ExpressionFunction<T, C> {

    static <T, C extends ExpressionEvaluationContext> LambdaExpressionFunction<T, C> with(final List<ExpressionFunctionParameter<?>> parameters,
                                                                                          final Class<T> returnType,
                                                                                          final Expression expression) {
        Objects.requireNonNull(parameters, "parameters");
        Objects.requireNonNull(returnType, "returnType");
        Objects.requireNonNull(expression, "expression");

        return new LambdaExpressionFunction<>(
                Lists.immutable(parameters),
                returnType,
                expression
        );
    }

    public LambdaExpressionFunction(final List<ExpressionFunctionParameter<?>> parameters,
                                    final Class<T> returnType,
                                    final Expression expression) {
        this.parameters = parameters;
        this.returnType = returnType;
        this.expression = expression;
    }

    @Override
    public Optional<FunctionExpressionName> name() {
        return ANONYMOUS_NAME;
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return this.expression.isPure(context);
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return this.parameters;
    }

    /**
     * The parameter definitions which are transformed into scoped local variables when the {@link #expression} is executed.
     */
    private final List<ExpressionFunctionParameter<?>> parameters;

    @Override
    public Class<T> returnType() {
        return this.returnType;
    }

    private final Class<T> returnType;

    @Override
    public T apply(final List<Object> values,
                   final C context) {
        this.checkParameterCount(values);

        return context.convertOrFail(
                context.context(
                        LambdaExpressionFunctionExpressionEvaluationContextContextFunction.with(
                                this.parameters,
                                values
                        )
                ).evaluate(this.expression),
                this.returnType
        );
    }

    /**
     * The {@link Function} that will be executed.
     */
    private final Expression expression;

    @Override
    public String toString() {
        return ANONYMOUS;
    }
}
