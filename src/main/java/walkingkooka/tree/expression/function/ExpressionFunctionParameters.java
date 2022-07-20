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

import walkingkooka.Context;
import walkingkooka.convert.ConverterContext;
import walkingkooka.tree.expression.ExpressionNumberContext;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;
import java.util.Objects;

/**
 * Wraps an {@link ExpressionFunction} only the apply method remains to implemented by sub-classes.
 */
abstract class ExpressionFunctionParameters<T, C extends Context & ConverterContext & ExpressionNumberContext> implements ExpressionFunction<T, C> {

    static void checkFunction(final ExpressionFunction<?, ?> function) {
        Objects.requireNonNull(function, "function");
    }

    ExpressionFunctionParameters(final ExpressionFunction<T, C> function) {
        this.function = function;
    }

    @Override
    public final boolean isPure(final ExpressionPurityContext context) {
        return this.function.isPure(context);
    }

    @Override
    public final FunctionExpressionName name() {
        return this.function.name();
    }

    @Override
    public final List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return this.function.parameters(count);
    }

    @Override
    public final Class<T> returnType() {
        return this.function.returnType();
    }

    /**
     * The wrapped function
     */
    final ExpressionFunction<T, C> function;

    @Override
    public abstract String toString();
}
