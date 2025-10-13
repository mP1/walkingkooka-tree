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

import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.ExpressionFunctionName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This exception should be thrown when a {@link ExpressionFunction} receives too few or too many parameters when invoked.
 * To update the {@link ExpressionFunctionName} invoke {@link #setFunctionName(Optional)}, which will update the name
 * mentioned in {@link #getMessage()}.
 */
public final class InvalidExpressionFunctionParameterCountException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public static void checkParameters(final List<Object> parameters,
                                       final ExpressionFunction<?, ?> function) {
        Objects.requireNonNull(parameters, "parameters");
        Objects.requireNonNull(function, "function");

        int min = 0;
        int max = 0;

        ExpressionFunctionParameterCardinality last = null;

        final int count = parameters.size();

        final List<ExpressionFunctionParameter<?>> functionParameters = function.parameters(count);

        for (final ExpressionFunctionParameter<?> functionParameter : functionParameters) {
            final ExpressionFunctionParameterCardinality cardinality = functionParameter.cardinality();
            min += cardinality.min;
            max += cardinality.max;
            last = cardinality;
        }

        if (ExpressionFunctionParameterCardinality.VARIABLE == last) {
            max = Integer.MAX_VALUE;
        }

        if (count < min) {
            throw new InvalidExpressionFunctionParameterCountException(
                count,
                min,
                max,
                Lists.immutable(functionParameters),
                function.name()
            );
        }
        if (count > max) {
            throw new InvalidExpressionFunctionParameterCountException(
                count,
                min,
                max,
                Lists.immutable(functionParameters),
                function.name()
            );
        }


        if (count < min || count > max) {
            new InvalidExpressionFunctionParameterCountException(
                count,
                min,
                max,
                Lists.immutable(functionParameters),
                function.name()
            );
        }
    }

    private InvalidExpressionFunctionParameterCountException(final int count,
                                                             final int min,
                                                             final int max,
                                                             final List<ExpressionFunctionParameter<?>> parameters,
                                                             final Optional<ExpressionFunctionName> functionName) {
        this.count = count;
        this.min = min;
        this.max = max;
        this.parameters = parameters;

        this.functionName = functionName;
    }

    // getMessage.......................................................................................................

    @Override
    public String getMessage() {
        final StringBuilder b = new StringBuilder()
            .append(
                this.functionName
                    .map(ExpressionFunctionName::value)
                    .orElse("Anonymous")
            ).append(": ");

        final int count = this.count;
        final int min = this.min;
        final int max = this.max;

        if (count < min) {
            // Missing parameter(s) X, Y, Z
            b.append("Missing parameter(s): ");

            final List<ExpressionFunctionParameter<?>> parameters = this.parameters;

            String separator = "";
            for (int i = count; i < min; i++) {
                b.append(separator);
                b.append(
                    parameters.get(i)
                        .name()
                );

                separator = ", ";
            }

        }
        if (count > max) {
            // 3 extra parameters
            b.append(count - max);
            b.append(" extra parameter values");
        }

        return b.toString();
    }

    // properties.......................................................................................................

    /**
     * The actual parameter value count.
     */
    public int count() {
        return this.count;
    }


    private final int count;

    /**
     * The minimum parameter count required
     */
    public int min() {
        return this.min;
    }

    private final int min;

    /**
     * The maximum parameter count accepted
     */
    public int max() {
        return this.max;
    }

    private final int max;

    /**
     * The parameters returned by the {@link ExpressionFunction} for the given arguments.
     */
    public List<ExpressionFunctionParameter<?>> parameters() {
        return this.parameters;
    }

    private final List<ExpressionFunctionParameter<?>> parameters;

    /**
     * The name of the function receiving the parameters. This may be replaced by {@link #setFunctionName(Optional)}.
     */
    public Optional<ExpressionFunctionName> functionName() {
        return this.functionName;
    }

    public InvalidExpressionFunctionParameterCountException setFunctionName(final Optional<ExpressionFunctionName> functionName) {
        this.functionName = Objects.requireNonNull(functionName, "functionName");
        return this;
    }

    private Optional<ExpressionFunctionName> functionName;
}
