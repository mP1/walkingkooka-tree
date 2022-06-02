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

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Wraps an {@link ExpressionFunction} and applies a {@link java.util.function.Function} on the paraneters before calling the wrapped.
 */
final class ExpressionFunctionParametersMapper<T, C extends Context & ConverterContext & ExpressionNumberContext> extends ExpressionFunctionParameters<T, C> {

    static <T, C extends Context & ConverterContext & ExpressionNumberContext> ExpressionFunctionParametersMapper<T, C> with(final BiFunction<List<Object>, C, List<Object>> mapper,
                                                                                                                             final ExpressionFunction<T, C> function) {
        Objects.requireNonNull(mapper, "mapper");
        checkFunction(function);

        return new ExpressionFunctionParametersMapper<>(mapper, function);
    }

    private ExpressionFunctionParametersMapper(final BiFunction<List<Object>, C, List<Object>> mapper,
                                               final ExpressionFunction<T, C> function) {
        super(function);
        this.mapper = mapper;
    }

    @Override
    public T apply(final List<Object> parameters,
                   final C context) {
        return this.function.apply(
                this.mapper.apply(
                        parameters,
                        context
                ),
                context
        );
    }

    /**
     * Special cases that handles if the new mapper is equal to the current.
     */
    @Override
    public ExpressionFunction<T, C> mapParameters(final BiFunction<List<Object>, C, List<Object>> mapper) {
        return this.mapper.equals(mapper) ?
                this :
                ExpressionFunctionParametersMapper.with(mapper, this);
    }

    /**
     * The function that preprocesses parameters before calling the wrapped function
     */
    private final BiFunction<List<Object>, C, List<Object>> mapper;

    @Override
    public String toString() {
        return this.function + "(" + this.mapper + ")";
    }
}
