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

import java.util.Objects;

/**
 * Captures an individual parameter to a @link ExpressionFunction}.
 */
public final class ExpressionFunctionParameter {

    public static ExpressionFunctionParameter with(final ExpressionFunctionParameterName name,
                                                   final Class<?> type) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(type, "type");

        return new ExpressionFunctionParameter(name, type);
    }

    private ExpressionFunctionParameter(final ExpressionFunctionParameterName name,
                                        final Class<?> type) {
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

    private final Class<?> type;

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