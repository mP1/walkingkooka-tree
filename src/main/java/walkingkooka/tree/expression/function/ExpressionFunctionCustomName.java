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

import walkingkooka.Cast;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An {@link ExpressionFunction} that returns a different {@link ExpressionFunctionName}.
 */
final class ExpressionFunctionCustomName<T, C extends ExpressionEvaluationContext> implements ExpressionFunction<T, C> {

    /**
     * Factory called by {@link ExpressionFunction#setName}
     */
    static <T, C extends ExpressionEvaluationContext> ExpressionFunction<T, C> with(final ExpressionFunction<T, C> function,
                                                                                    final Optional<ExpressionFunctionName> name) {
        Objects.requireNonNull(function, "namedFunction");
        Objects.requireNonNull(name, "name");

        return function.name().equals(name) ?
            function :
            function instanceof ExpressionFunctionCustomName ?
                unwrap(Cast.to(function), name) :
                new ExpressionFunctionCustomName<>(function, name);
    }

    /**
     * Handles the special case not preventing double wrapping a {@link ExpressionFunctionCustomName}.
     */
    static <T, C extends ExpressionEvaluationContext> ExpressionFunction<T, C> unwrap(final ExpressionFunctionCustomName<T, C> function,
                                                                                      final Optional<ExpressionFunctionName> name) {
        return new ExpressionFunctionCustomName<>(function.function, name);
    }

    /**
     * Private ctor use factory.
     */
    private ExpressionFunctionCustomName(final ExpressionFunction<T, C> function,
                                         final Optional<ExpressionFunctionName> name) {
        super();
        this.function = function;
        this.name = name;
    }

    @Override
    public T apply(final List<Object> parameters,
                   final C context) {
        return this.function.apply(parameters, context);
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return this.function.isPure(context);
    }

    private final ExpressionFunction<T, C> function;

    @Override
    public Optional<ExpressionFunctionName> name() {
        return this.name;
    }

    private final Optional<ExpressionFunctionName> name;

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return this.function.parameters(count);
    }

    @Override
    public Class<T> returnType() {
        return this.function.returnType();
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.name,
            this.function
        );
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) ||
            other instanceof ExpressionFunctionCustomName && this.equals0((ExpressionFunctionCustomName) other);
    }

    private boolean equals0(final ExpressionFunctionCustomName other) {
        return this.name.equals(other.name) &&
            this.function.equals(other.function);
    }

    @Override
    public String toString() {
        return this.name()
            .map(ExpressionFunctionName::value)
            .orElse(ANONYMOUS);
    }
}
