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
import walkingkooka.tree.expression.ExpressionPurity;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Basic contract for a function within a {@link ExpressionFunctionContext}
 */
public interface ExpressionFunction<T, C extends ExpressionFunctionContext> extends BiFunction<List<Object>, C, T>,
        ExpressionPurity,
        HasName<FunctionExpressionName> {

    /**
     * Gives this {@link ExpressionFunction} a new name.
     */
    default ExpressionFunction<T, C> setName(final FunctionExpressionName name) {
        Objects.requireNonNull(name, "name");
        return CustomNameExpressionFunction.with(this, name);
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
     * When true indicates that any parameters in {@link walkingkooka.tree.expression.Expression} form be evaluated into
     * java objects prior to invoking invoking {@link #apply(Object, Object)}. For the vast majority of cases all
     * functions will return true, but for cases such as Excels isError it may be desired to catch any thrown exceptions
     * and substitute an error object of some kind.
     */
    boolean requiresEvaluatedParameters();

    /**
     * When <code>true</code> parameters that implement {@link walkingkooka.tree.expression.ExpressionReference} are resolved to
     * their actual non {@link walkingkooka.tree.expression.Expression} value.
     * This is only honoured when {@link ExpressionFunctionContext#evaluate(FunctionExpressionName, List)} is used.
     */
    boolean resolveReferences();

    /**
     * {@see ParametersMapperExpressionFunctionBiFunctionFlatten}
     */
    default ExpressionFunction<T, C> flatten() {
        return this.mapParameters(ParametersMapperExpressionFunctionBiFunctionFlatten.instance());
    }

    /**
     * Returns a ne {@link ExpressionFunction} that adds the parameter mapper before this function.
     */
    default ExpressionFunction<T, C> mapParameters(final BiFunction<List<Object>, C, List<Object>> mapper) {
        return ParametersMapperExpressionFunction.with(mapper, this);
    }
}
