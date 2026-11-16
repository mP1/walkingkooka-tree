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

import walkingkooka.ToStringBuilder;
import walkingkooka.UsesToStringBuilder;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContextDelegator;
import walkingkooka.currency.CurrencyCode;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContextDelegator;
import walkingkooka.locale.LocaleLanguageTag;
import walkingkooka.logging.LoggingContext;
import walkingkooka.logging.LoggingContextDelegator;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * An {@link ExpressionEvaluationContext} delegates to helpers or constants for each method.
 * Note that {@link ExpressionFunction} parameters have any {@link Expression} & {@link ExpressionReference} evaluated to values and
 * values are also converted to the parameter's type.
 * This is useful for languages or environments that have auto converting of value semantics, think Javascript.
 */
final class ExpressionEvaluationContextBasic implements ExpressionEvaluationContext,
    ConverterContextDelegator,
    LocaleContextDelegator,
    LoggingContextDelegator,
    UsesToStringBuilder {

    /**
     * Factory that creates a {@link ExpressionEvaluationContextBasic}
     */
    static ExpressionEvaluationContextBasic with(final ExpressionNumberKind expressionNumberKind,
                                                 final BiFunction<String, ExpressionEvaluationContext, Object> evaluator,
                                                 final Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions,
                                                 final Function<RuntimeException, Object> exceptionHandler,
                                                 final Function<ExpressionReference, Optional<Optional<Object>>> references,
                                                 final Function<ExpressionReference, ExpressionEvaluationException> referenceNotFound,
                                                 final CaseSensitivity stringEqualityCaseSensitivity,
                                                 final ConverterContext converterContext,
                                                 final LocaleContext localeContext,
                                                 final LoggingContext loggingContext) {
        Objects.requireNonNull(expressionNumberKind, "expressionNumberKind");
        Objects.requireNonNull(evaluator, "evaluator");
        Objects.requireNonNull(functions, "functions");
        Objects.requireNonNull(exceptionHandler, "exceptionHandler");
        Objects.requireNonNull(references, "references");
        Objects.requireNonNull(referenceNotFound, "referenceNotFound");
        Objects.requireNonNull(stringEqualityCaseSensitivity, "stringEqualsCaseSensitivity");
        Objects.requireNonNull(converterContext, "converterContext");
        Objects.requireNonNull(localeContext, "localeContext");
        Objects.requireNonNull(loggingContext, "loggingContext");

        return new ExpressionEvaluationContextBasic(
            expressionNumberKind,
            evaluator,
            functions,
            exceptionHandler,
            references,
            referenceNotFound,
            stringEqualityCaseSensitivity,
            converterContext,
            localeContext,
            loggingContext
        );
    }

    /**
     * Private ctor use factory
     */
    private ExpressionEvaluationContextBasic(final ExpressionNumberKind expressionNumberKind,
                                             final BiFunction<String, ExpressionEvaluationContext, Object> evaluator,
                                             final Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions,
                                             final Function<RuntimeException, Object> exceptionHandler,
                                             final Function<ExpressionReference, Optional<Optional<Object>>> references,
                                             final Function<ExpressionReference, ExpressionEvaluationException> referenceNotFound,
                                             final CaseSensitivity stringEqualityCaseSensitivity,
                                             final ConverterContext converterContext,
                                             final LocaleContext localeContext,
                                             final LoggingContext loggingContext) {
        super();

        this.expressionNumberKind = expressionNumberKind;
        this.evaluator = evaluator;
        this.exceptionHandler = exceptionHandler;
        this.functions = functions;
        this.references = references;
        this.referenceNotFound = referenceNotFound;
        this.stringEqualityCaseSensitivity = stringEqualityCaseSensitivity;
        this.converterContext = converterContext;
        this.localeContext = localeContext;
        this.loggingContext = loggingContext;
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

    @Override
    public CurrencyCode currencyCode() {
        return this.converterContext.currencyCode();
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

    // LocaleContextDelegator...........................................................................................

    @Override
    public LocaleContext localeContext() {
        return this.localeContext;
    }

    private final LocaleContext localeContext;

    @Override
    public Locale locale() {
        return this.localeContext.locale();
    }

    @Override
    public Optional<DateTimeSymbols> dateTimeSymbolsForLocale(final Locale locale) {
        return this.localeContext.dateTimeSymbolsForLocale(locale);
    }

    @Override
    public Optional<DecimalNumberSymbols> decimalNumberSymbolsForLocale(final Locale locale) {
        return this.localeContext.decimalNumberSymbolsForLocale(locale);
    }

    @Override
    public Optional<Locale> localeForLanguageTag(final LocaleLanguageTag languageTag) {
        return this.localeContext.localeForLanguageTag(languageTag);
    }

    // LoggingContextDelegator..........................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.loggingContext;
    }

    private final LoggingContext loggingContext;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    public void buildToString(final ToStringBuilder b) {
        b.label("expressionNumberKind")
            .value(this.expressionNumberKind)
            .label("evaluator")
            .value(this.evaluator)
            .label("functions")
            .value(this.functions)
            .label("exceptionHandler")
            .value(this.exceptionHandler)
            .label("references")
            .value(this.references)
            .label("referenceNotFound")
            .value(this.referenceNotFound)
            .label("stringEqualityCaseSensitivity")
            .value(this.stringEqualityCaseSensitivity)
            .label("converterContext")
            .value(this.converterContext)
            .label("localeContext")
            .value(this.localeContext)
            .label("loggingContext")
            .value(this.loggingContext);
    }
}
