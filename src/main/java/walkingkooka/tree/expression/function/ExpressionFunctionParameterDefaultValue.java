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

import walkingkooka.ToStringBuilder;
import walkingkooka.UsesToStringBuilder;
import walkingkooka.tree.expression.ExpressionEvaluationContext;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link Function} that holds a fixed value for a {@link ExpressionFunctionParameter#defaultValue()}
 */
final class ExpressionFunctionParameterDefaultValue<T> implements Function<ExpressionEvaluationContext, Optional<T>>,
    UsesToStringBuilder {

    static <T> ExpressionFunctionParameterDefaultValue<T> with(final Optional<T> value) {
        return new ExpressionFunctionParameterDefaultValue<>(
            Objects.requireNonNull(value, "value")
        );
    }

    private ExpressionFunctionParameterDefaultValue(final Optional<T> value) {
        this.value = value;
    }

    @Override
    public Optional<T> apply(final ExpressionEvaluationContext context) {
        Objects.requireNonNull(context, "context");
        return this.value;
    }

    private final Optional<T> value;

    @Override
    public String toString() {
        return String.valueOf(
            value.orElse(null)
        );
    }

    @Override
    public void buildToString(final ToStringBuilder b) {
        b.value(this.value);
    }
}
