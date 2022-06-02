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

/**
 * Basic contract for a function within a {@link walkingkooka.tree.expression.ExpressionEvaluationContext}
 */
public interface ExpressionFunction<T, C extends Context & ConverterContext & ExpressionNumberContext> extends BiFunction<List<Object>, C, T>,
        ExpressionPurity,
        HasName<FunctionExpressionName> {

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
    List<ExpressionFunctionParameter<?>> parameters();

    /**
     * Gets the {@link ExpressionFunctionParameter} for the given parameter. This method is helpful because the last
     * parameter may be {@link ExpressionFunctionParameterCardinality#VARIABLE}.
     */
    default ExpressionFunctionParameter<?> parameter(final int index) {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index " + index + " < 0");
        }

        final List<ExpressionFunctionParameter<?>> parameters = this.parameters();
        final int count = parameters.size();

        final ExpressionFunctionParameter<?> parameter;

        if (index < count) {
            parameter = parameters.get(index);
        } else {
            parameter = parameters.get(count - 1);
            if (parameter.cardinality() != ExpressionFunctionParameterCardinality.VARIABLE) {
                throw new ArrayIndexOutOfBoundsException("Unknown parameter " + index + " expected only " + count);
            }
        }

        return parameter;
    }

    /**
     * Checks the given parameter values match the expected count exactly. Less or more parameters will result in a
     * {@link IllegalArgumentException} being thrown.
     */
    default void checkParameterCount(final List<Object> parameters) {
        int min = 0;
        int max = 0;

        ExpressionFunctionParameterCardinality last = null;

        for (final ExpressionFunctionParameter<?> functionParameter : this.parameters()) {
            final ExpressionFunctionParameterCardinality cardinality = functionParameter.cardinality();
            min += cardinality.min;
            max += cardinality.max;
            last = cardinality;
        }

        if (ExpressionFunctionParameterCardinality.VARIABLE == last) {
            max = Integer.MAX_VALUE;
        }

        final int count = parameters.size();
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
     * Meta customisations for this {@link ExpressionFunction}
     */
    Set<ExpressionFunctionKind> kinds();

    /**
     * Wraps this function if necessary so that it returns the given {@link ExpressionFunctionKind}.
     */
    default ExpressionFunction<T, C> setKinds(final Set<ExpressionFunctionKind> kinds) {
        return ExpressionFunctionCustomKinds.with(this, kinds);
    }

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
