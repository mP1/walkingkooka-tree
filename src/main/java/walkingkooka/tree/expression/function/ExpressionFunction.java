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

import walkingkooka.Context;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.ConverterContext;
import walkingkooka.naming.HasName;
import walkingkooka.tree.expression.ExpressionNumberContext;
import walkingkooka.tree.expression.ExpressionPurity;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * Basic contract for a function within a {@link walkingkooka.tree.expression.ExpressionEvaluationContext}
 */
public interface ExpressionFunction<T, C extends Context & ConverterContext & ExpressionNumberContext> extends BiFunction<List<Object>, C, T>,
        ExpressionPurity,
        HasName<FunctionExpressionName> {

    /**
     * Updates ALL parameters with the given {@link ExpressionFunctionParameterKind}
     */
    static List<ExEpressionFunctionParameter<?>> setKinds(final List<ExpressionFunctionParameter<?>> parameters,
                                                          final Set<ExpressionFunctionParameterKind> kinds) {
        Objects.requireNonNull(parameters, "parameters");
        Objects.requireNonNull(kinds, "kinds");

        return Lists.immutable(
                parameters.stream()
                        .map(p -> p.setKinds(kinds))
                        .collect(Collectors.toList())
        );
    }

    /**
     * Gives this {@link ExpressionFunction} a new name.
     */
    default ExpressionFunction<T, C> setName(final FunctionExpressionName name) {
        Objects.requireNonNull(name, "name");
        return ExpressionFunctionCustomName.with(this, name);
    }

    /**
     * Returns meta info about the parameters for this function.
     */
    List<ExpressionFunctionParameter<?>> parameters(int count);

    /**
     * Checks the given parameter values match the expected count exactly. Less or more parameters will result in a
     * {@link IllegalArgumentException} being thrown.
     */
    default void checkParameterCount(final List<Object> parameters) {
        int min = 0;
        int max = 0;

        ExpressionFunctionParameterCardinality last = null;

        final int count = parameters.size();
        for (final ExpressionFunctionParameter<?> functionParameter : this.parameters(count)) {
            final ExpressionFunctionParameterCardinality cardinality = functionParameter.cardinality();
            min += cardinality.min;
            max += cardinality.max;
            last = cardinality;
        }

        if (ExpressionFunctionParameterCardinality.VARIABLE == last) {
            max = Integer.MAX_VALUE;
        }

        if (count < min) {
            throw new IllegalArgumentException("Missing parameters, got " + count + " expected " + min);
        }
        if (count > max) {
            throw new IllegalArgumentException("Too many parameters got " + count + " expected " + max);
        }
    }

    /**
     * The return type of this function
     *
     * @return The return type of this function
     */
    Class<T> returnType();

    /**
     * Returns a ne {@link ExpressionFunction} that adds the parameter mapper before this function.
     */
    default ExpressionFunction<T, C> mapParameters(final BiFunction<List<Object>, C, List<Object>> mapper) {
        return ExpressionFunctionParametersMapper.with(mapper, this);
    }

    /**
     * Returns a new {@link ExpressionFunction} that filters parameters using the given {@link BiPredicate}.
     */
    default ExpressionFunction<T, C> filterParameters(final BiPredicate<Object, C> filter) {
        return ExpressionFunctionParametersFilter.with(filter, this);
    }
}
