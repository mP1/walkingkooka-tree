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

import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.convert.ConverterContext;
import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeContextDelegator;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * An {@link ExpressionEvaluationContext} delegates to helpers or constants for each method.
 * Note that namedFunction parameters have any {@link Expression} & {@link ExpressionReference} evaluated to values and
 * values are also converted to the parameter's type.
 * This is useful for languages or environments that have auto converting of value semantics, think Javascript.
 */
final class BasicExpressionEvaluationContext implements ExpressionEvaluationContext,
    DateTimeContextDelegator,
    DecimalNumberContextDelegator {

    /**
     * Factory that creates a {@link BasicExpressionEvaluationContext}
     */
    static BasicExpressionEvaluationContext with(final ExpressionNumberKind expressionNumberKind,
                                                 final Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions,
                                                 final Function<RuntimeException, Object> exceptionHandler,
                                                 final Function<ExpressionReference, Optional<Optional<Object>>> references,
                                                 final Function<ExpressionReference, ExpressionEvaluationException> referenceNotFound,
                                                 final CaseSensitivity stringEqualityCaseSensitivity,
                                                 final ConverterContext converterContext) {
        Objects.requireNonNull(expressionNumberKind, "expressionNumberKind");
        Objects.requireNonNull(functions, "functions");
        Objects.requireNonNull(exceptionHandler, "exceptionHandler");
        Objects.requireNonNull(references, "references");
        Objects.requireNonNull(referenceNotFound, "referenceNotFound");
        Objects.requireNonNull(stringEqualityCaseSensitivity, "stringEqualsCaseSensitivity");
        Objects.requireNonNull(converterContext, "converterContext");

        return new BasicExpressionEvaluationContext(
            expressionNumberKind,
            functions,
            exceptionHandler,
            references,
            referenceNotFound,
            stringEqualityCaseSensitivity,
            converterContext
        );
    }

    /**
     * Private ctor use factory
     */
    private BasicExpressionEvaluationContext(final ExpressionNumberKind expressionNumberKind,
                                             final Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions,
                                             final Function<RuntimeException, Object> exceptionHandler,
                                             final Function<ExpressionReference, Optional<Optional<Object>>> references,
                                             final Function<ExpressionReference, ExpressionEvaluationException> referenceNotFound,
                                             final CaseSensitivity stringEqualityCaseSensitivity,
                                             final ConverterContext converterContext) {
        super();

        this.expressionNumberKind = expressionNumberKind;
        this.exceptionHandler = exceptionHandler;
        this.functions = functions;
        this.references = references;
        this.referenceNotFound = referenceNotFound;
        this.stringEqualityCaseSensitivity = stringEqualityCaseSensitivity;
        this.converterContext = converterContext;
    }

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

    @Override
    public Locale locale() {
        return this.converterContext.locale();
    }

    // DateTimeContext.................................................................................................

    @Override
    public DateTimeContext dateTimeContext() {
        return this.converterContext;
    }

    // DecimalNumberContext............................................................................................

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return this.converterContext;
    }

    // Convert..........................................................................................................

    @Override
    public long dateOffset() {
        return this.converterContext.dateOffset();
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type) {
        return this.converterContext.canConvert(value, type);
    }

    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> target) {
        return this.converterContext.convert(value, target);
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

    // if changed copy to ScopedExpressionEvaluationContext#evaluateFunction
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

    // Object..........................................................................................................
    @Override
    public String toString() {
        return this.expressionNumberKind +
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
            this.converterContext;
    }
}
