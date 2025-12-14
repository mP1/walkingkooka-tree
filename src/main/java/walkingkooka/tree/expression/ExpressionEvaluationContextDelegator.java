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

import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContextDelegator;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentContextDelegator;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.environment.EnvironmentValueWatcher;
import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContextDelegator;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

/**
 * Delegates all {@link ExpressionEvaluationContext} except for {@link #reference(ExpressionReference)} and
 * {@link #enterScope(Function)}.
 */
public interface ExpressionEvaluationContextDelegator extends ExpressionEvaluationContext,
    ConverterContextDelegator,
    EnvironmentContextDelegator,
    LocaleContextDelegator {

    @Override
    default ConverterContext converterContext() {
        return this.expressionEvaluationContext();
    }

    // LocaleContext....................................................................................................

    @Override
    default LocaleContext localeContext() {
        return this.expressionEvaluationContext();
    }

    @Override
    default Locale locale() {
        return this.environmentContext()
            .locale();
    }

    // EnvironmentContext...............................................................................................

    @Override
    default <T> ExpressionEvaluationContext setEnvironmentValue(final EnvironmentValueName<T> name,
                                                                final T value) {
        this.environmentContext()
            .setEnvironmentValue(
                name,
                value
            );
        return this;
    }

    @Override
    default ExpressionEvaluationContext removeEnvironmentValue(final EnvironmentValueName<?> name) {
        this.environmentContext()
            .removeEnvironmentValue(name);
        return this;
    }

    @Override
    default ExpressionEvaluationContext setLineEnding(final LineEnding lineEnding) {
        this.environmentContext()
            .setLineEnding(lineEnding);
        return this;
    }

    @Override
    default ExpressionEvaluationContext setLocale(final Locale locale) {
        this.environmentContext()
            .setLocale(locale);
        return this;
    }

    @Override
    default ExpressionEvaluationContext setUser(final Optional<EmailAddress> user) {
        this.environmentContext()
            .setUser(user);
        return this;
    }

    @Override
    default LocalDateTime now() {
        return this.environmentContext()
            .now();
    }

    @Override
    default Runnable addEventValueWatcher(final EnvironmentValueWatcher watcher) {
        return this.environmentContext()
            .addEventValueWatcher(watcher);
    }

    @Override
    default Runnable addEventValueWatcherOnce(final EnvironmentValueWatcher watcher) {
        return this.environmentContext()
            .addEventValueWatcherOnce(watcher);
    }

    @Override
    default EnvironmentContext environmentContext() {
        return this.expressionEvaluationContext();
    }

    // ExpressionEvaluationContext......................................................................................

    @Override
    default Object evaluate(final String expression) {
        return this.expressionEvaluationContext()
            .evaluate(expression);
    }

    @Override
    default boolean isPure(final ExpressionFunctionName name) {
        return this.expressionEvaluationContext().isPure(name);
    }

    @Override
    default ExpressionNumberKind expressionNumberKind() {
        return this.expressionEvaluationContext()
            .expressionNumberKind();
    }

    @Override
    default ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name) {
        return this.expressionEvaluationContext()
            .expressionFunction(name);
    }

    @Override
    default <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                   final Object value) {
        return this.expressionEvaluationContext()
            .prepareParameter(
                parameter,
                value
            );
    }

    @Override
    default Object handleException(final RuntimeException exception) {
        return this.expressionEvaluationContext()
            .handleException(exception);
    }

    @Override
    default boolean isText(final Object value) {
        return this.expressionEvaluationContext()
            .isText(value);
    }

    @Override
    default CaseSensitivity stringEqualsCaseSensitivity() {
        return this.expressionEvaluationContext()
            .stringEqualsCaseSensitivity();
    }

    ExpressionEvaluationContext expressionEvaluationContext();
}
