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
import walkingkooka.Context;
import walkingkooka.convert.ConverterContext;
import walkingkooka.tree.expression.ExpressionNumberContext;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * An {@link ExpressionFunction} that returns a different {@link FunctionExpressionName}.
 */
final class ExpressionFunctionCustomName<T, C extends Context & ConverterContext & ExpressionNumberContext> implements ExpressionFunction<T, C> {

    /**
     * Factory called by {@link ExpressionFunction#setName}
     */
    static <T, C extends Context & ConverterContext & ExpressionNumberContext> ExpressionFunction<T, C> with(final ExpressionFunction<T, C> function,
                                                                                                             final FunctionExpressionName name) {
        Objects.requireNonNull(function, "function");
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
    static <T, C extends Context & ConverterContext & ExpressionNumberContext> ExpressionFunction<T, C> unwrap(final ExpressionFunctionCustomName<T, C> function,
                                                                                                               final FunctionExpressionName name) {
        return new ExpressionFunctionCustomName<>(function.function, name);
    }

    /**
     * Private ctor use factory.
     */
    private ExpressionFunctionCustomName(final ExpressionFunction<T, C> function,
                                         final FunctionExpressionName name) {
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
    public FunctionExpressionName name() {
        return this.name;
    }

    private final FunctionExpressionName name;

    @Override
    public List<ExpressionFunctionParameter<?>> parameters() {
        return this.function.parameters();
    }

    @Override
    public Class<T> returnType() {
        return this.function.returnType();
    }

    @Override
    public Set<ExpressionFunctionKind> kinds() {
        return this.function.kinds();
    }

    @Override
    public String toString() {
        return this.name().toString();
    }
}
