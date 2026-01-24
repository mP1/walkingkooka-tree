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

import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.environment.EnvironmentValueWatcher;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.expression.convert.FakeExpressionNumberConverterContext;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class FakeExpressionEvaluationContext extends FakeExpressionNumberConverterContext implements ExpressionEvaluationContext {

    public FakeExpressionEvaluationContext() {
        super();
    }

    @Override
    public CaseSensitivity stringEqualsCaseSensitivity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionEvaluationContext enterScope(final Function<ExpressionReference, Optional<Optional<Object>>> scoped) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object evaluate(final String expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name) {
        Objects.requireNonNull(name, "name");
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isText(final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                  final Object value) {
        Objects.requireNonNull(parameter, "parameter");

        throw new UnsupportedOperationException();
    }

    @Override
    public Object handleException(final RuntimeException exception) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPure(final ExpressionFunctionName name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Optional<Object>> reference(final ExpressionReference reference) {
        Objects.requireNonNull(reference, "reference");

        throw new UnsupportedOperationException();
    }

    // LocaleContext....................................................................................................

    @Override
    public Set<Locale> availableLocales() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<DateTimeSymbols> dateTimeSymbolsForLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<DecimalNumberSymbols> decimalNumberSymbolsForLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Locale> findByLocaleText(final String text,
                                        final int offset,
                                        final int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale locale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> localeText(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    // FakeEnvironmentContext...........................................................................................

    @Override
    public Indentation indentation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIndentation(final Indentation indentation) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public LineEnding lineEnding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLineEnding(final LineEnding lineEnding) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionEvaluationContext cloneEnvironment() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionEvaluationContext setEnvironmentContext(final EnvironmentContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void setEnvironmentValue(final EnvironmentValueName<T> name,
                                        final T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeEnvironmentValue(final EnvironmentValueName<?> name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LocalDateTime now() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<EmailAddress> user() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setUser(final Optional<EmailAddress> user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addEventValueWatcher(final EnvironmentValueWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addEventValueWatcherOnce(final EnvironmentValueWatcher watcher) {
        throw new UnsupportedOperationException();
    }
}
