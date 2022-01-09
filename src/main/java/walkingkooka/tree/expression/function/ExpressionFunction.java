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

import walkingkooka.collect.list.Lists;
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
     * When true indicates the var args.
     */
    boolean lsLastParameterVariable();

    /**
     * Checks the given parameter values match the expected count exactly. Less or more parameters will result in a
     * {@link IllegalArgumentException} being thrown.
     */
    default void checkOnlyRequiredParameters(final List<Object> parameters) {
        final int actualCount = parameters.size();
        final int expectedCount = this.parameters().size();
        if (actualCount != expectedCount) {
            this.failParameterCount(expectedCount, actualCount);
        }
    }

    /**
     * Checks the given parameter values match the expected count. Extra parameters will result in an {@link IllegalArgumentException}.
     * Less parameters will not result in an exception and it is assumed default values will be used for missing.
     */
    default void checkWithoutExtraParameters(final List<Object> parameters) {
        final int actualCount = parameters.size();
        final int expectedCount = this.parameters().size();
        if (actualCount > expectedCount) {
            this.failParameterCount(expectedCount, actualCount);
        }
    }

    /**
     * Helper that handles failed parameter count checks.
     */
    default void failParameterCount(final int expected, final int actual) {
        throw new IllegalArgumentException("Expected only " + expected + " but got " + actual + " parameters");
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
     * Converts all parameter values using the {@link ExpressionFunctionParameter} for each parameter.
     */
    default List<Object> convertParameters(final List<Object> parameters, final ExpressionFunctionContext context) {
        final List<Object> after = Lists.array();

        final List<ExpressionFunctionParameter<?>> parameterInfos = this.parameters();
        final int parameterInfoCounts = parameterInfos.size();

        int i = 0;
        ExpressionFunctionParameter<?> parameterInfo = null;

        while (i < parameterInfoCounts) {
            parameterInfo = parameterInfos.get(i);

            after.add(parameterInfo.convertOrFail(parameters.get(i), context));
            i++;
        }

        final int count = parameters.size();
        if (this.lsLastParameterVariable() && null != parameterInfo) {
            while (i < count) {
                after.add(parameterInfo.convertOrFail(parameters.get(i), context));
                i++;
            }
        } else {
            while (i < count) {
                after.add(parameters.get(i));
                i++;
            }
        }

        return Lists.readOnly(after);
    }

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

    /**
     * {@see ParametersMapperExpressionFunctionBiFunctionRemoveFalsey}
     */
    default ExpressionFunction<T, C> removeFalsey() {
        return this.mapParameters(ParametersMapperExpressionFunctionBiFunctionRemoveFalsey.instance());
    }
}
