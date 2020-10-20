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
import walkingkooka.naming.Name;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;
import java.util.Objects;

/**
 * An {@link ExpressionFunction} that returns a different {@link FunctionExpressionName}.
 */
final class CustomNameExpressionFunction<T> implements ExpressionFunction<T> {

    /**
     * Factory called by {@link ExpressionFunction#setName}
     */
    static <T> ExpressionFunction<T> with(final ExpressionFunction<T> function,
                                          final FunctionExpressionName name) {
        Objects.requireNonNull(function, "function");
        Objects.requireNonNull(name, "name");

        return function.name().equals(name) ?
                function :
                function instanceof CustomNameExpressionFunction ?
                        unwrap(Cast.to(function), name) :
                        new CustomNameExpressionFunction<>(function, name);
    }

    /**
     * Handles the special case not preventing double wrapping a {@link CustomNameExpressionFunction}.
     */
    static <T> ExpressionFunction<T> unwrap(final CustomNameExpressionFunction<T> function,
                                            final FunctionExpressionName name) {
        return new CustomNameExpressionFunction<>(function.function, name);
    }

    /**
     * Private ctor use factory.
     */
    private CustomNameExpressionFunction(final ExpressionFunction<T> function,
                                         final FunctionExpressionName name) {
        super();
        this.function = function;
        this.name = name;
    }

    @Override
    public T apply(final List<Object> parameters,
                   final ExpressionFunctionContext context) {
        return this.function.apply(parameters, context);
    }

    private final ExpressionFunction<T> function;

    @Override
    public FunctionExpressionName name() {
        return this.name;
    }

    private FunctionExpressionName name;

    @Override
    public String toString() {
        return this.name().toString();
    }
}
