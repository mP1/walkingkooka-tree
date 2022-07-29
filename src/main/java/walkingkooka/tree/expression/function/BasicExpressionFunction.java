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

import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.IntFunction;

/**
 * A {@link ExpressionFunction} that accepts as parameters all of its properties and operations supporting the
 * assembly of dynamic functions.
 */
final class BasicExpressionFunction<T, C extends ExpressionEvaluationContext> implements ExpressionFunction<T, C> {

    static <T, C extends ExpressionEvaluationContext> BasicExpressionFunction<T, C> with(final Optional<FunctionExpressionName> name,
                                                                                         final boolean pure,
                                                                                         final IntFunction<List<ExpressionFunctionParameter<?>>> parameters,
                                                                                         final Class<T> returnType,
                                                                                         final BiFunction<List<Object>, C, T> biFunction) {
        return new BasicExpressionFunction<>(
                Objects.requireNonNull(name, "name"),
                pure,
                Objects.requireNonNull(parameters, "parameters"),
                Objects.requireNonNull(returnType, "returnType"),
                Objects.requireNonNull(biFunction, "biFunction")
        );
    }

    private BasicExpressionFunction(final Optional<FunctionExpressionName> name,
                                    final boolean pure,
                                    final IntFunction<List<ExpressionFunctionParameter<?>>> parameters,
                                    final Class<T> returnType,
                                    final BiFunction<List<Object>, C, T> biFunction) {
        this.name = name;
        this.pure = pure;
        this.parameters = parameters;
        this.returnType = returnType;
        this.biFunction = biFunction;
    }

    @Override
    public Optional<FunctionExpressionName> name() {
        return this.name;
    }

    private final Optional<FunctionExpressionName> name;

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return this.pure;
    }

    private boolean pure;

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return this.parameters.apply(count);
    }

    private final IntFunction<List<ExpressionFunctionParameter<?>>> parameters;

    @Override
    public Class<T> returnType() {
        return this.returnType;
    }

    private final Class<T> returnType;

    @Override
    public T apply(final List<Object> objects,
                   final C context) {
        return this.biFunction.apply(objects, context);
    }

    private final BiFunction<List<Object>, C, T> biFunction;

    @Override
    public int hashCode() {
        return Objects.hash(
                this.name,
                this.pure,
                this.parameters,
                this.returnType,
                this.biFunction
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof BasicExpressionFunction && this.equals1((BasicExpressionFunction<?, ?>) other);
    }

    private boolean equals1(final BasicExpressionFunction<?, ?> other) {
        return this.name.equals(other.name) &&
                this.pure == other.pure &&
                this.parameters.equals(other.parameters) &&
                this.returnType.equals(other.returnType) &&
                this.biFunction.equals(other.biFunction);
    }

    @Override
    public String toString() {
        return this.name()
                .map(FunctionExpressionName::value)
                .orElse(ANONYMOUS);
    }
}
