/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

import walkingkooka.naming.HasName;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Basic contract for a function within a {@link ExpressionFunctionContext}
 */
public interface ExpressionFunction<T, C extends ExpressionFunctionContext> extends BiFunction<List<Object>, C, T>,
        HasName<FunctionExpressionName> {

    /**
     * Gives this {@link ExpressionFunction} a new name.
     */
    default ExpressionFunction<T, C> setName(final FunctionExpressionName name) {
        Objects.requireNonNull(name, "name");
        return CustomNameExpressionFunction.with(this, name);
    }

    /**
     * When <code>true</code> parameters that are{@link walkingkooka.tree.expression.ExpressionReference are resolved to
     * their actual non {@link walkingkooka.tree.expression.Expression} value.
     * This is only honoured when {@link ExpressionFunctionContext#evaluate(FunctionExpressionName, List)} is used.
     */
    boolean resolveReferences();

    /**
     * Returns a ne {@link ExpressionFunction} that adds the parameter mapper before this function.
     */
    default ExpressionFunction<T, C> parameters(final BiFunction<List<Object>, C, List<Object>> mapper) {
        return ParametersMapperExpressionFunction.with(mapper, this);
    }
}
