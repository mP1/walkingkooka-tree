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

import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.math.DecimalNumberSymbols;

import java.util.Locale;
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
    public ExpressionEvaluationContext enterScope(final Function<ExpressionReference, Optional<Optional<Object>>> scoped) {
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

    // ExpressionEvaluationContextDelegator.............................................................................

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
            new ScopedExpressionEvaluationContext(
                this.referenceToValue,
                after
            );
    }

    @Override
    public ExpressionEvaluationContext expressionEvaluationContext() {
        return this.context;
    }

    @Override
    public Optional<DateTimeSymbols> dateTimeSymbolsForLocale(final Locale locale) {
        return this.context.dateTimeSymbolsForLocale(locale);
    }

    @Override
    public Optional<DecimalNumberSymbols> decimalNumberSymbolsForLocale(final Locale locale) {
        return this.context.decimalNumberSymbolsForLocale(locale);
    }

    @Override
    public Optional<Locale> localeForLanguageTag(final String languageTag) {
        return this.context.localeForLanguageTag(languageTag);
    }

    private final ExpressionEvaluationContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
