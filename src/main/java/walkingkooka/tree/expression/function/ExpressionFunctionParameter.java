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
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;

/**
 * Captures an individual parameter to a @link ExpressionFunction}.
 */
public final class ExpressionFunctionParameter<T> {

    /**
     * Helper that creates a read only list of the given parameters.
     */
    public static List<ExpressionFunctionParameter<?>> list(final ExpressionFunctionParameter<?>... parameters) {
        return Lists.of(parameters);
    }

    public static <T> ExpressionFunctionParameter<T> with(final ExpressionFunctionParameterName name,
                                                          final Class<T> type) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(type, "type");

        return new ExpressionFunctionParameter<>(name, type);
    }

    private ExpressionFunctionParameter(final ExpressionFunctionParameterName name,
                                        final Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public ExpressionFunctionParameterName name() {
        return this.name;
    }

    private final ExpressionFunctionParameterName name;

    public Class<?> type() {
        return this.type;
    }

    private final Class<T> type;

    /**
     * Gets the parameter at index or uses the default
     */
    public T getOrDefault(final List<Object> parameters,
                          final int index,
                          final T defaultValue) {
        return index >= parameters.size() ?
                defaultValue :
                this.getOrFail(parameters, index);
    }

    /**
     * Gets the parameter at index or fails and also complains if it is the wrong type.
     */
    public T getOrFail(final List<Object> parameters,
                       final int index) {
        if (index >= parameters.size()) {
            throw new IndexOutOfBoundsException("Required parameter " + this.name() + " missing");
        }

        final Object value = parameters.get(index);
        try {
            // https://github.com/mP1/walkingkooka-tree/issues/307
            // Emulate Class.cast
            return Cast.to(value);
        } catch (final ClassCastException cast) {
            throw new ClassCastException("Parameter " + this.name() + " of wrong type " + value.getClass().getName() + " expected " + this.type());
        }
    }

    /**
     * Converts the given parameter value to match the required type of this parameter.
     */
    public Either<T, String> convert(final Object value,
                                     final ExpressionFunctionContext context) {
        return context.convert(value, this.type);
    }

    /**
     * Converts the given parameter value to match the required type of this parameter or fails.
     */
    public T convertOrFail(final Object value,
                           final ExpressionFunctionContext context) {
        return context.convertOrFail(value, this.type);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.type);
    }

    public boolean equals(final Object other) {
        return this == other || other instanceof ExpressionFunctionParameter && this.equals0((ExpressionFunctionParameter) other);
    }

    private boolean equals0(final ExpressionFunctionParameter other) {
        return this.name.equals(other.name) && this.type.equals(other.type);
    }

    @Override
    public String toString() {
        return this.type.getName() + " " + this.name;
    }
}
