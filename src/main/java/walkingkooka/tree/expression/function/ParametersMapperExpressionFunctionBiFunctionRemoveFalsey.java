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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Attempts to convert each parameter to {@link Boolean}, removing those with false values.
 */
final class ParametersMapperExpressionFunctionBiFunctionRemoveFalsey<C extends ExpressionFunctionContext> extends ParametersMapperExpressionFunctionBiFunction<C> {

    static <C extends ExpressionFunctionContext> ParametersMapperExpressionFunctionBiFunctionRemoveFalsey<C> instance() {
        return Cast.to(INSTANCE);
    }

    private final static ParametersMapperExpressionFunctionBiFunctionRemoveFalsey<?> INSTANCE = new ParametersMapperExpressionFunctionBiFunctionRemoveFalsey<>();


    private ParametersMapperExpressionFunctionBiFunctionRemoveFalsey() {
        super();
    }

    @Override
    public List<Object> apply(final List<Object> parameters, final C context) {
        Objects.requireNonNull(parameters, "parameters");

        return parameters.stream()
                .filter(p -> context.convertOrFail(p, Boolean.class))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "remove falsey";
    }
}
