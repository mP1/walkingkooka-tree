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

package walkingkooka.tree.expression;

import walkingkooka.Either;
import walkingkooka.collect.set.Sets;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionContext;

import java.math.MathContext;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Wraps another {@link ExpressionEvaluationContext} delegating all methods except for a guard within
 * {@link #reference(ExpressionReference)} to detect cycles between resolving a {@link ExpressionReference} to a
 * {@link Expression}, even indirectly.<br>
 */
final class CycleDetectingExpressionEvaluationContext implements ExpressionEvaluationContext {

    /**
     * Factory that creates a new {@link CycleDetectingExpressionEvaluationContext}.
     */
    static CycleDetectingExpressionEvaluationContext with(final ExpressionEvaluationContext context) {
        Objects.requireNonNull(context, "context");

        return new CycleDetectingExpressionEvaluationContext(context);
    }

    /**
     * Private ctor use factory.
     */
    private CycleDetectingExpressionEvaluationContext(final ExpressionEvaluationContext context) {
        this.context = context;
        this.cycles = Sets.ordered();
    }

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        return this.context.expressionNumberKind();
    }

    @Override
    public int defaultYear() {
        return this.context.defaultYear();
    }

    @Override
    public boolean isPure(final FunctionExpressionName name) {
        return this.context.isPure(name);
    }

    @Override
    public int twoToFourDigitYear(final int year) {
        return this.context.twoToFourDigitYear(year);
    }

    @Override
    public int twoDigitYear() {
        return this.context.twoDigitYear();
    }

    @Override
    public ExpressionFunction<?, ExpressionFunctionContext> function(final FunctionExpressionName name) {
        return this.context.function(name);
    }

    @Override
    public Object evaluate(final Expression expression) {
        return this.context.evaluate(expression);
    }

    @Override
    public Object evaluate(final FunctionExpressionName functionName, final List<Object> parameters) {
        return this.context.evaluate(functionName, parameters);
    }

    @Override
    public Optional<Expression> reference(final ExpressionReference reference) {
        final Set<ExpressionReference> cycles = this.cycles;

        this.cycleCheck(reference, cycles);

        try {
            cycles.add(reference);
            final Expression result = this.context.referenceOrFail(reference);

            if (result.isReference()) {
                final ReferenceExpression referenceNode = result.cast();
                this.cycleCheck(referenceNode.value(), cycles);
            }

            return Optional.of(result);
        } finally {
            cycles.remove(reference);
        }
    }

    /**
     * If the reference is in {@link #cycles} then there must be a cycle of some sort.
     */
    private void cycleCheck(final ExpressionReference reference, final Set<ExpressionReference> cycles) {
        if (cycles.contains(reference)) {
            this.reportCycle(reference);
        }
    }

    /**
     * Used to keep track of references and to detect cycles.
     */
    private final Set<ExpressionReference> cycles;

    /**
     * Reports a cycle for a given {@link ExpressionReference}
     */
    private void reportCycle(final ExpressionReference reference) {
        throw new CycleDetectedExpressionEvaluationConversionException("Cycle detected to " + reference, reference);
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
    public boolean canConvert(final Object from,
                              final Class<?> type) {
        return this.context.canConvert(from, type);
    }

    @Override
    public <T> Either<T, String> convert(final Object from,
                                         final Class<T> type) {
        return this.context.convert(from, type);
    }

    private final ExpressionEvaluationContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
