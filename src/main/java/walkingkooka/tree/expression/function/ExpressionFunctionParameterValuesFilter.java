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
import walkingkooka.tree.expression.ExpressionEvaluationContext;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * Wraps an {@link ExpressionFunction} and applies a {@link java.util.function.Function} on the paraneters before calling the wrapped.
 */
final class ExpressionFunctionParameterValuesFilter<T, C extends ExpressionEvaluationContext> extends ExpressionFunctionParameterValues<T, C> {

    static <T, C extends ExpressionEvaluationContext> ExpressionFunctionParameterValuesFilter<T, C> with(final BiPredicate<Object, C> filter,
                                                                                                         final ExpressionFunction<T, C> function) {
        checkFilter(filter);
        checkFunction(function);

        return new ExpressionFunctionParameterValuesFilter<>(filter, function);
    }

    private static void checkFilter(final BiPredicate<Object, ? extends Context> filter) {
        Objects.requireNonNull(filter, "filter");
    }

    private ExpressionFunctionParameterValuesFilter(final BiPredicate<Object, C> filter,
                                                    final ExpressionFunction<T, C> function) {
        super(function);
        this.filter = filter;
    }

    @Override
    public T apply(final List<Object> parameters,
                   final C context) {
        return this.function.apply(
                parameters.stream()
                        .filter(p -> this.filter.test(p, context))
                        .collect(Collectors.toList()),
                context
        );
    }

    /**
     * Special cases that handles if the new filter is equal to the current.
     */
    @Override
    public ExpressionFunction<T, C> filterParameterValues(final BiPredicate<Object, C> filter) {
        checkFilter(filter);

        return this.filter.equals(filter) ?
                this :
                ExpressionFunctionParameterValuesFilter.with(filter, this);
    }

    /**
     * The namedFunction that preprocesses parameters before calling the wrapped namedFunction
     */
    private final BiPredicate<Object, C> filter;

    @Override
    public String toString() {
        return this.function + "(" + this.filter + ")";
    }
}
