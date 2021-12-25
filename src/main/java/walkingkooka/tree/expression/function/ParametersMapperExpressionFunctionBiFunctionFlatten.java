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
import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;

final class ParametersMapperExpressionFunctionBiFunctionFlatten<C extends ExpressionFunctionContext> extends ParametersMapperExpressionFunctionBiFunction<C> {

    static <C extends ExpressionFunctionContext> ParametersMapperExpressionFunctionBiFunctionFlatten<C> instance() {
        return Cast.to(INSTANCE);
    }

    private final static ParametersMapperExpressionFunctionBiFunctionFlatten<?> INSTANCE = new ParametersMapperExpressionFunctionBiFunctionFlatten<>();

    private ParametersMapperExpressionFunctionBiFunctionFlatten() {
        super();
    }

    @Override
    public List<Object> apply(final List<Object> parameters, final C context) {
        Objects.requireNonNull(parameters, "parameters");

        final List<Object> flatten = Lists.array();
        flatten(parameters, flatten);
        return Lists.readOnly(flatten);
    }

    private static void flatten(final List<Object> parameters,
                                final List<Object> flatten) {
        for (final Object parameter : parameters) {
            if (parameter instanceof List) {
                flatten(Cast.to(parameter), flatten);
                continue;
            }
            flatten.add(parameter);
        }
    }

    @Override
    public String toString() {
        return "flatten";
    }
}
