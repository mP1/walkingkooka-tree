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
import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeContextDelegator;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentContextDelegator;
import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContextDelegator;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.Indentation;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Wraps another {@link ExpressionEvaluationContext} delegating all methods except for a guard within
 * {@link #reference(ExpressionReference)} to detect cycles between resolving a {@link ExpressionReference} to a
 * {@link Expression}, even indirectly.<br>
 */
final class CycleDetectingExpressionEvaluationContext implements ExpressionEvaluationContext,
    DateTimeContextDelegator,
    DecimalNumberContextDelegator,
    EnvironmentContextDelegator,
    LocaleContextDelegator {

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
    public Object evaluate(final String expression) {
        return this.context.evaluate(expression);
    }

    @Override
    public boolean isPure(final ExpressionFunctionName name) {
        return this.context.isPure(name);
    }

    @Override
    public ExpressionEvaluationContext enterScope(final Function<ExpressionReference, Optional<Optional<Object>>> resolver) {
        return this.context.enterScope(resolver);
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name) {
        return this.context.expressionFunction(name);
    }

    @Override
    public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                  final Object value) {
        return this.context.prepareParameter(parameter, value);
    }

    @Override
    public Object handleException(final RuntimeException exception) {
        return this.context.handleException(exception);
    }

    @Override
    public Optional<Optional<Object>> reference(final ExpressionReference reference) {
        final Set<ExpressionReference> cycles = this.cycles;

        this.cycleCheck(reference, cycles);

        try {
            cycles.add(reference);

            final Optional<Optional<Object>> possibleValue = this.context.reference(reference);
            if (possibleValue.isPresent()) {
                final Object value = possibleValue.get()
                    .orElse(null);

                if (value instanceof ExpressionReference) {
                    this.cycleCheck((ExpressionReference) value, cycles);
                }
            }

            return possibleValue;
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

    // EnvironmentContextDelegator......................................................................................

    @Override
    public ExpressionEvaluationContext cloneEnvironment() {
        return this.setEnvironmentContext(
            this.context.cloneEnvironment()
        );
    }

    @Override
    public ExpressionEvaluationContext setEnvironmentContext(final EnvironmentContext environmentContext) {
        final ExpressionEvaluationContext before = this.context;
        final ExpressionEvaluationContext after = before.setEnvironmentContext(environmentContext);

        return before == after ?
            this :
            new CycleDetectingExpressionEvaluationContext(after);
    }

    @Override
    public LocalDateTime now() {
        return this.context.now();
    }

    @Override
    public Locale locale() {
        return this.context.locale();
    }

    @Override
    public void setLocale(final Locale locale) {
        this.context.setLocale(locale);
    }

    @Override
    public EnvironmentContext environmentContext() {
        return this.context;
    }

    // LocaleContext....................................................................................................

    @Override
    public LocaleContext localeContext() {
        return this.context;
    }

    // DateTimeContext..................................................................................................

    @Override
    public DateTimeContext dateTimeContext() {
        return this.context;
    }

    // ExpressionNumberContext..........................................................................................

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        return this.context.expressionNumberKind();
    }

    // DecimalNumberContextDelegator....................................................................................

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return this.context;
    }

    // CanConvert.......................................................................................................

    @Override
    public boolean canNumbersHaveGroupSeparator() {
        return this.context.canNumbersHaveGroupSeparator();
    }

    @Override
    public long dateOffset() {
        return this.context.dateOffset();
    }

    @Override
    public Indentation indentation() {
        return this.context.indentation();
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

    @Override
    public char valueSeparator() {
        return this.context.valueSeparator();
    }

    // Strings..........................................................................................................

    @Override
    public CaseSensitivity stringEqualsCaseSensitivity() {
        return this.context.stringEqualsCaseSensitivity();
    }

    @Override
    public boolean isText(final Object value) {
        return this.context.isText(value);
    }

    private final ExpressionEvaluationContext context;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
