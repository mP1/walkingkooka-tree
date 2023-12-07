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

import java.util.Objects;
import java.util.Optional;

/**
 * A base class for several {@link ExpressionFunction} sub classes that delegates most methods but leaves a few abstract.
 */
abstract class ExpressionFunctionParameterValues<T, C extends ExpressionEvaluationContext> implements ExpressionFunction<T, C> {

    static void checkFunction(final ExpressionFunction<?, ?> function) {
        Objects.requireNonNull(function, "function");
    }

    ExpressionFunctionParameterValues(final ExpressionFunction<T, C> function) {
        this.function = function;
    }

    @Override
    public final boolean isPure(final ExpressionPurityContext context) {
        return this.function.isPure(context);
    }

    @Override
    public final Optional<FunctionExpressionName> name() {
        return this.function.name();
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
    public final String toString() {
        return this.name()
                .map(FunctionExpressionName::toString)
                .orElseGet(this.function::toString);
    }
}
