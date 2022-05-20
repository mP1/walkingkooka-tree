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

package walkingkooka.tree.expression.function;

import walkingkooka.Either;
import walkingkooka.convert.ConverterContext;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionEvaluationException;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.math.MathContext;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * An {@link ExpressionFunctionContext} delegates to helpers or constants for each method.
 */
final class BasicExpressionFunctionContext implements ExpressionFunctionContext {

    /**
     * Factory that creates a {@link BasicExpressionFunctionContext}
     */
    static BasicExpressionFunctionContext with(final ExpressionNumberKind expressionNumberKind,
                                               final Function<FunctionExpressionName, ExpressionFunction<?, ExpressionFunctionContext>> functions,
                                               final Function<RuntimeException, Object> exceptionHandler,
                                               final Function<ExpressionReference, Optional<Object>> references,
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

        return new BasicExpressionFunctionContext(
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
    private BasicExpressionFunctionContext(final ExpressionNumberKind expressionNumberKind,
                                           final Function<FunctionExpressionName, ExpressionFunction<?, ExpressionFunctionContext>> functions,
                                           final Function<RuntimeException, Object> exceptionHandler,
                                           final Function<ExpressionReference, Optional<Object>> references,
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
    public char groupingSeparator() {
        return this.converterContext.groupingSeparator();
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

    // functions........................................................................................................

    @Override
    public ExpressionFunction<?, ExpressionFunctionContext> function(final FunctionExpressionName name) {
        return this.functions.apply(name);
    }

    private final Function<FunctionExpressionName, ExpressionFunction<?, ExpressionFunctionContext>> functions;

    @Override
    public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                  final Object value) {
        return parameter.convertOrFail(value, this);
    }

    @Override
    public Object evaluate(final FunctionExpressionName name,
                           final List<Object> parameters) {
        Object result;
        try {
            result = this.function(name)
                    .apply(parameters, this);
        } catch (final RuntimeException cause) {
            result = this.exceptionHandler.apply(cause);
        }
        return result;
    }

    @Override
    public Object handleException(final RuntimeException exception) {
        return this.exceptionHandler.apply(exception);
    }

    private final Function<RuntimeException, Object> exceptionHandler;

    @Override
    public Optional<Object> reference(final ExpressionReference reference) {
        return this.references.apply(reference);
    }

    private final Function<ExpressionReference, Optional<Object>> references;

    @Override
    public ExpressionEvaluationException referenceNotFound(final ExpressionReference reference) {
        return this.referenceNotFound.apply(reference);
    }

    private final Function<ExpressionReference, ExpressionEvaluationException> referenceNotFound;

    @Override
    public CaseSensitivity caseSensitivity() {
        return this.caseSensitivity;
    }

    private final CaseSensitivity caseSensitivity;

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
