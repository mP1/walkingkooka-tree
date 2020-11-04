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

import walkingkooka.Either;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.math.MathContext;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A {@link ExpressionFunctionContext} that delegates all methods to a {@link ExpressionEvaluationContext}.
 */
final class ExpressionEvaluationContextExpressionFunctionContext implements ExpressionFunctionContext {

    static ExpressionEvaluationContextExpressionFunctionContext with(final ExpressionEvaluationContext context) {
        Objects.requireNonNull(context, "context");

        return new ExpressionEvaluationContextExpressionFunctionContext(context);
    }

    private ExpressionEvaluationContextExpressionFunctionContext(final ExpressionEvaluationContext context) {
        super();
        this.context = context;
    }

    @Override
    public Object function(final FunctionExpressionName name,
                           final List<Object> parameters) {
        return this.context.function(name, parameters);
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type) {
        return this.context.canConvert(value, type);
    }

    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> type) {
        return this.context.convert(value, type);
    }

    @Override
    public Locale locale() {
        return this.context.locale();
    }

    @Override
    public MathContext mathContext() {
        return this.context.mathContext();
    }

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        return this.context.expressionNumberKind();
    }

    private final ExpressionEvaluationContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
