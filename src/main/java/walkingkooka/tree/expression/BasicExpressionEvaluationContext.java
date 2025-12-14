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

import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContextDelegator;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentContextDelegator;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContextDelegator;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * An {@link ExpressionEvaluationContext} delegates to helpers or constants for each method.
 * Note that namedFunction parameters have any {@link Expression} & {@link ExpressionReference} evaluated to values and
 * values are also converted to the parameter's type.
 * This is useful for languages or environments that have auto converting of value semantics, think Javascript.
 */
final class BasicExpressionEvaluationContext implements ExpressionEvaluationContext,
    ConverterContextDelegator,
    EnvironmentContextDelegator,
    LocaleContextDelegator {

    /**
     * Factory that creates a {@link BasicExpressionEvaluationContext}
     */
    static BasicExpressionEvaluationContext with(final ExpressionNumberKind expressionNumberKind,
                                                 final BiFunction<String, ExpressionEvaluationContext, Object> evaluator,
                                                 final Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions,
                                                 final Function<RuntimeException, Object> exceptionHandler,
                                                 final Function<ExpressionReference, Optional<Optional<Object>>> references,
                                                 final Function<ExpressionReference, ExpressionEvaluationException> referenceNotFound,
                                                 final CaseSensitivity stringEqualityCaseSensitivity,
                                                 final ConverterContext converterContext,
                                                 final EnvironmentContext environmentContext,
                                                 final LocaleContext localeContext) {
        Objects.requireNonNull(expressionNumberKind, "expressionNumberKind");
        Objects.requireNonNull(evaluator, "evaluator");
        Objects.requireNonNull(functions, "functions");
        Objects.requireNonNull(exceptionHandler, "exceptionHandler");
        Objects.requireNonNull(references, "references");
        Objects.requireNonNull(referenceNotFound, "referenceNotFound");
        Objects.requireNonNull(stringEqualityCaseSensitivity, "stringEqualsCaseSensitivity");
        Objects.requireNonNull(converterContext, "converterContext");
        Objects.requireNonNull(environmentContext, "environmentContext");
        Objects.requireNonNull(localeContext, "localeContext");

        return new BasicExpressionEvaluationContext(
            expressionNumberKind,
            evaluator,
            functions,
            exceptionHandler,
            references,
            referenceNotFound,
            stringEqualityCaseSensitivity,
            converterContext,
            environmentContext,
            localeContext
        );
    }

    /**
     * Private ctor use factory
     */
    private BasicExpressionEvaluationContext(final ExpressionNumberKind expressionNumberKind,
                                             final BiFunction<String, ExpressionEvaluationContext, Object> evaluator,
                                             final Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions,
                                             final Function<RuntimeException, Object> exceptionHandler,
                                             final Function<ExpressionReference, Optional<Optional<Object>>> references,
                                             final Function<ExpressionReference, ExpressionEvaluationException> referenceNotFound,
                                             final CaseSensitivity stringEqualityCaseSensitivity,
                                             final ConverterContext converterContext,
                                             final EnvironmentContext environmentContext,
                                             final LocaleContext localeContext) {
        super();

        this.expressionNumberKind = expressionNumberKind;
        this.evaluator = evaluator;
        this.exceptionHandler = exceptionHandler;
        this.functions = functions;
        this.references = references;
        this.referenceNotFound = referenceNotFound;
        this.stringEqualityCaseSensitivity = stringEqualityCaseSensitivity;
        this.converterContext = converterContext;
        this.environmentContext = environmentContext;
        this.localeContext = localeContext;
    }

    @Override
    public Object evaluate(final String expression) {
        return this.evaluator.apply(
            expression,
            this
        );
    }

    private final BiFunction<String, ExpressionEvaluationContext, Object> evaluator;

    @Override
    public CaseSensitivity stringEqualsCaseSensitivity() {
        return this.stringEqualityCaseSensitivity;
    }

    private final CaseSensitivity stringEqualityCaseSensitivity;

    @Override
    public boolean isText(final Object value) {
        return value instanceof Character || value instanceof CharSequence;
    }

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        return this.expressionNumberKind;
    }

    private final ExpressionNumberKind expressionNumberKind;

    // ConverterContextDelegator........................................................................................


    @Override
    public ConverterContext converterContext() {
        return this.converterContext;
    }

    private final ConverterContext converterContext;

    // functions........................................................................................................

    @Override
    public ExpressionEvaluationContext enterScope(final Function<ExpressionReference, Optional<Optional<Object>>> resolver) {
        return ExpressionEvaluationContexts.scoped(
            resolver,
            this
        );
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name) {
        return this.functions.apply(name);
    }

    private final Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions;

    @Override
    public boolean isPure(final ExpressionFunctionName name) {
        return this.expressionFunction(name).isPure(this);
    }

    @Override
    public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                  final Object value) {
        return parameter.convertOrFail(value, this);
    }

    @Override
    public Object handleException(final RuntimeException exception) {
        return this.exceptionHandler.apply(exception);
    }

    private final Function<RuntimeException, Object> exceptionHandler;

    @Override
    public Optional<Optional<Object>> reference(final ExpressionReference reference) {
        return this.references.apply(reference);
    }

    private final Function<ExpressionReference, Optional<Optional<Object>>> references;

    @Override
    public ExpressionEvaluationException referenceNotFound(final ExpressionReference reference) {
        return this.referenceNotFound.apply(reference);
    }

    private final Function<ExpressionReference, ExpressionEvaluationException> referenceNotFound;

    // EnvironmentContextDelegator......................................................................................

    @Override
    public ExpressionEvaluationContext cloneEnvironment() {
        return this.setEnvironmentContext(
            this.environmentContext.cloneEnvironment()
        );
    }

    @Override
    public ExpressionEvaluationContext setEnvironmentContext(final EnvironmentContext environmentContext) {
        return this.environmentContext == environmentContext ?
            this :
            new BasicExpressionEvaluationContext(
                this.expressionNumberKind,
                this.evaluator,
                this.functions,
                this.exceptionHandler,
                this.references,
                this.referenceNotFound,
                this.stringEqualityCaseSensitivity,
                this.converterContext,
                Objects.requireNonNull(environmentContext, "environmentContext"),
                this.localeContext
            );
    }

    @Override
    public <T> ExpressionEvaluationContext setEnvironmentValue(final EnvironmentValueName<T> name,
                                                               final T value) {
        this.environmentContext.setEnvironmentValue(
            name,
            value
        );
        return this;
    }

    @Override
    public ExpressionEvaluationContext removeEnvironmentValue(final EnvironmentValueName<?> name) {
        this.environmentContext.removeEnvironmentValue(name);
        return this;
    }

    @Override
    public LocalDateTime now() {
        return this.environmentContext.now();
    }

    @Override
    public ExpressionEvaluationContext setLineEnding(final LineEnding lineEnding) {
        this.environmentContext.setLineEnding(lineEnding);
        return this;
    }

    /**
     * Locale must come from {@link EnvironmentContext} and not {@link LocaleContext}.
     */
    @Override
    public Locale locale() {
        return this.environmentContext.locale();
    }

    @Override
    public ExpressionEvaluationContext setLocale(final Locale locale) {
        this.environmentContext.setLocale(locale);
        return this;
    }

    @Override
    public ExpressionEvaluationContext setUser(final Optional<EmailAddress> user) {
        this.environmentContext.setUser(user);
        return this;
    }

    @Override
    public EnvironmentContext environmentContext() {
        return this.environmentContext;
    }

    private final EnvironmentContext environmentContext;

    // LocaleContext....................................................................................................

    @Override
    public LocaleContext localeContext() {
        return this.localeContext;
    }

    private final LocaleContext localeContext;

    // Object...........................................................................................................
    @Override
    public String toString() {
        return this.expressionNumberKind +
            " " +
            this.evaluator +
            " " +
            this.functions +
            " " +
            this.exceptionHandler +
            " " +
            this.references +
            " " +
            this.referenceNotFound +
            " " +
            this.stringEqualityCaseSensitivity +
            " " +
            this.converterContext +
            " " +
            this.environmentContext +
            " " +
            this.localeContext;
    }
}
