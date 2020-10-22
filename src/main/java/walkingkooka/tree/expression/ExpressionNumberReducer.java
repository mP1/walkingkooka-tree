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

package walkingkooka.tree.expression;

import java.util.Objects;

/**
 * A mutable builder that supports performing operations to produce a final result. After each operation the value is updated
 * ready for further operations. Non {@link Number} values must be converted prior to use as parameters to any operation.
 */
public final class ExpressionNumberReducer {

    /**
     * Creates a new {@link ExpressionNumberReducer} with the given value.
     */
    public static ExpressionNumberReducer with(final Number value,
                                               final ExpressionNumberReducerContext context) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(context, "context");

        return new ExpressionNumberReducer(value, context);
    }

    private ExpressionNumberReducer(final Number value,
                                    final ExpressionNumberReducerContext context) {
        super();
        this.value = value;
        this.context = context;
    }

    /**
     * Adds the given value, storing the result.
     */
    public ExpressionNumberReducer add(final Number value) {
        this.value = ExpressionNumberReducerBinaryExpressionNumberVisitor.add(this.value, value, this.context);
        return this;
    }

    /**
     * Subtract the given value, storing the result.
     */
    public ExpressionNumberReducer subtract(final Number value) {
        this.value = ExpressionNumberReducerBinaryExpressionNumberVisitor.subtract(this.value, value, this.context);
        return this;
    }

    /**
     * The context used to convert numbers to a requested target type.
     */
    private final ExpressionNumberReducerContext context;

    /**
     * Value getter, the result of the last operation.
     */
    public Number value() {
        return this.value;
    }

    /**
     * The work in progress result.
     */
    private Number value;

    @Override
    public String toString() {
        return this.value.toString();
    }
}
