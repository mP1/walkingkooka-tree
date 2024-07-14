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
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.math.MathContext;
import java.time.LocalDateTime;
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
final class BasicExpressionEvaluationContext implements ExpressionEvaluationContext {

    /**
     * Factory that creates a {@link BasicExpressionEvaluationContext}
     */
    static BasicExpressionEvaluationContext with(final ExpressionNumberKind expressionNumberKind,
                                                 final Function<FunctionExpressionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions,
                                                 final Function<RuntimeException, Object> exceptionHandler,
                                                 final Function<ExpressionReference, Optional<Optional<Object>>> references,
                                                 final Function<ExpressionReference, ExpressionEvaluationException> referenceNotFound,
                                                 final CaseSensitivity caseSensitivity,
                                                 final ConverterContext converterContext) {
        Objects.requireNonNull(expressionNumberKind, "expressionNumberKind");
        Objects.requireNonNull(functions, "functions");
        Objects.requireNonNull(exceptionHandler, "exceptionHandler");
        Objects.requireNonNull(references, "references");
        Objects.requireNonNull(referenceNotFound, "referenceNotFound");
        Objects.requireNonNull(caseSensitivity, "caseSensitivity");
        Objects.requireNonNull(converterContext, "converterContext");

        return new BasicExpressionEvaluationContext(
                expressionNumberKind,
                functions,
                exceptionHandler,
                references,
                referenceNotFound,
                caseSensitivity,
                converterContext
        );
    }

    /**
     * Private ctor use factory
     */
    private BasicExpressionEvaluationContext(final ExpressionNumberKind expressionNumberKind,
                                             final Function<FunctionExpressionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions,
                                             final Function<RuntimeException, Object> exceptionHandler,
                                             final Function<ExpressionReference, Optional<Optional<Object>>> references,
                                             final Function<ExpressionReference, ExpressionEvaluationException> referenceNotFound,
                                             final CaseSensitivity caseSensitivity,
                                             final ConverterContext converterContext) {
        super();

        this.expressionNumberKind = expressionNumberKind;
        this.exceptionHandler = exceptionHandler;
        this.functions = functions;
        this.references = references;
        this.referenceNotFound = referenceNotFound;
        this.caseSensitivity = caseSensitivity;
        this.converterContext = converterContext;
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return this.caseSensitivity;
    }

    private final CaseSensitivity caseSensitivity;

    @Override
    public boolean isText(final Object value) {
        return value instanceof Character || value instanceof CharSequence;
    }

    // DateTimeContext.................................................................................................

    @Override
    public List<String> ampms() {
        return this.converterContext.ampms();
    }

    @Override
    public int defaultYear() {
        return this.converterContext.defaultYear();
    }

    @Override
    public List<String> monthNames() {
        return this.converterContext.monthNames();
    }

    @Override
    public List<String> monthNameAbbreviations() {
        return this.converterContext.monthNameAbbreviations();
    }

    @Override
    public LocalDateTime now() {
        return this.converterContext.now();
    }

    @Override
    public int twoToFourDigitYear(final int year) {
        return this.converterContext.twoToFourDigitYear(year);
    }

    @Override
    public int twoDigitYear() {
        return this.converterContext.twoDigitYear();
    }

    @Override
    public List<String> weekDayNames() {
        return this.converterContext.weekDayNames();
    }

    @Override
    public List<String> weekDayNameAbbreviations() {
        return this.converterContext.weekDayNameAbbreviations();
    }

    // DecimalNumberContext............................................................................................

    @Override
    public String currencySymbol() {
        return this.converterContext.currencySymbol();
    }

    @Override
    public char decimalSeparator() {
        return this.converterContext.decimalSeparator();
    }

    @Override
    public String exponentSymbol() {
        return this.converterContext.exponentSymbol();
    }

    @Override
    public char groupSeparator() {
        return this.converterContext.groupSeparator();
    }

    @Override
    public char percentageSymbol() {
        return this.converterContext.percentageSymbol();
    }

    @Override
    public char negativeSign() {
        return this.converterContext.negativeSign();
    }

    @Override
    public char positiveSign() {
        return this.converterContext.positiveSign();
    }


    @Override
    public Locale locale() {
        return this.converterContext.locale();
    }

    @Override
    public MathContext mathContext() {
        return this.converterContext.mathContext();
    }

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        return this.expressionNumberKind;
    }

    private final ExpressionNumberKind expressionNumberKind;

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

    // eval.............................................................................................................

    @Override
    public Object evaluate(final Expression expression) {
        Object result;

        try {
            result = expression.toValue(this);
        } catch (final RuntimeException exception) {
            result = this.handleException(exception);
        }

        return result;
    }

    // functions........................................................................................................

    @Override
    public ExpressionEvaluationContext context(final Function<ExpressionReference, Optional<Optional<Object>>> resolver) {
        throw new UnsupportedOperationException("https://github.com/mP1/walkingkooka-tree/issues/599");
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final FunctionExpressionName name) {
        return this.functions.apply(name);
    }

    private final Function<FunctionExpressionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions;

    @Override
    public boolean isPure(final FunctionExpressionName name) {
        return this.expressionFunction(name).isPure(this);
    }

    @Override
    public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                  final Object value) {
        return parameter.convertOrFail(value, this);
    }

    @Override
    public Object evaluateFunction(final ExpressionFunction<?, ? extends ExpressionEvaluationContext> function,
                                   final List<Object> parameters) {
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
                this.caseSensitivity +
                " " +
                this.converterContext;
    }
}
