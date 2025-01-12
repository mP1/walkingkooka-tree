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

import walkingkooka.Cast;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * An {@link ExpressionEvaluationContext} that wraps another {@link ExpressionEvaluationContext} delegating all
 * methods with special handling for scoped references in {@link #reference(ExpressionReference)}.
 */
final class ScopedExpressionEvaluationContext implements ExpressionEvaluationContext,
    ExpressionEvaluationContextDelegator {

    static ScopedExpressionEvaluationContext with(final Function<ExpressionReference, Optional<Optional<Object>>> referenceToValue,
                                                  final ExpressionEvaluationContext context) {
        return new ScopedExpressionEvaluationContext(
            Objects.requireNonNull(referenceToValue, "referenceToValue"),
            Objects.requireNonNull(context, "context")
        );
    }

    private ScopedExpressionEvaluationContext(final Function<ExpressionReference, Optional<Optional<Object>>> referenceToValue,
                                              final ExpressionEvaluationContext context) {
        this.referenceToValue = referenceToValue;
        this.context = context;
    }

    // ExpressionEvaluationContext......................................................................................

    @Override
    public ExpressionEvaluationContext context(final Function<ExpressionReference, Optional<Optional<Object>>> scoped) {
        return ScopedExpressionEvaluationContext.with(
            scoped,
            this
        );
    }

    @Override
    public Optional<Optional<Object>> reference(final ExpressionReference reference) {
        Optional<Optional<Object>> value = this.referenceToValue.apply(reference);
        if (false == value.isPresent()) {
            value = this.context.reference(reference);
        }

        return value;
    }

    private final Function<ExpressionReference, Optional<Optional<Object>>> referenceToValue;

    // copied from BasicExpressionEvaluationContext
    @Override
    public Object evaluateExpression(final Expression expression) {
        Objects.requireNonNull(expression, "expression");

        Object result;

        try {
            result = expression.toValue(this);
        } catch (final RuntimeException exception) {
            result = this.handleException(exception);
        }

        return result;
    }

    // copied from BasicExpressionEvaluationContext
    @Override
    public Object evaluateFunction(final ExpressionFunction<?, ? extends ExpressionEvaluationContext> function,
                                   final List<Object> parameters) {
        Objects.requireNonNull(function, "function");
        Objects.requireNonNull(parameters, "parameters");

        Object result;

        try {
            result = function.apply(
                this.prepareParameters(function, parameters),
                Cast.to(this)
            );
        } catch (final RuntimeException exception) {
            result = this.handleException(exception);
        }

        return result;
    }

    // ExpressionEvaluationContextDelegator.............................................................................

    @Override
    public ExpressionEvaluationContext expressionEvaluationContext() {
        return this.context;
    }

    private final ExpressionEvaluationContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
