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

import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.ConverterContext;
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
import java.util.stream.Collectors;

/**
 * An {@link ExpressionFunctionContext} delegates to helpers or constants for each method.
 */
final class BasicExpressionFunctionContext implements ExpressionFunctionContext {

    /**
     * Factory that creates a {@link BasicExpressionFunctionContext}
     */
    static BasicExpressionFunctionContext with(final ExpressionNumberKind expressionNumberKind,
                                               final Function<FunctionExpressionName, ExpressionFunction<?, ExpressionFunctionContext>> functions,
                                               final Function<ExpressionReference, Optional<Object>> references,
                                               final Function<ExpressionReference, ExpressionEvaluationException> referenceNotFound,
                                               final ConverterContext converterContext) {
        Objects.requireNonNull(expressionNumberKind, "expressionNumberKind");
        Objects.requireNonNull(functions, "functions");
        Objects.requireNonNull(references, "references");
        Objects.requireNonNull(referenceNotFound, "referenceNotFound");
        Objects.requireNonNull(converterContext, "converterContext");

        return new BasicExpressionFunctionContext(
                expressionNumberKind,
                functions,
                references,
                referenceNotFound,
                converterContext
        );
    }

    /**
     * Private ctor use factory
     */
    private BasicExpressionFunctionContext(final ExpressionNumberKind expressionNumberKind,
                                           final Function<FunctionExpressionName, ExpressionFunction<?, ExpressionFunctionContext>> functions,
                                           final Function<ExpressionReference, Optional<Object>> references,
                                           final Function<ExpressionReference, ExpressionEvaluationException> referenceNotFound,
                                           final ConverterContext converterContext) {
        super();
        this.expressionNumberKind = expressionNumberKind;
        this.functions = functions;
        this.references = references;
        this.referenceNotFound = referenceNotFound;
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

    @Override
    public int defaultYear() {
        return this.converterContext.defaultYear();
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
    public ExpressionFunction<?, ExpressionFunctionContext> function(final FunctionExpressionName name) {
        return this.functions.apply(name);
    }

    private final Function<FunctionExpressionName, ExpressionFunction<?, ExpressionFunctionContext>> functions;

    @Override
    public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                  final Object value) {
        Objects.requireNonNull(parameter, "parameter");

        final Class<T> type = parameter.type();
        final boolean isList = type.equals(List.class);
        return isList ?
                Cast.to(this.convertList(parameter, type, value)) :
                this.convertOrFail(value, type);
    }

    private <T> List<T> convertList(final ExpressionFunctionParameter<T> parameter,
                                    final Class<T> listType,
                                    final Object value) {
        if (value instanceof List) {
            throw new IllegalArgumentException("Parameter " + parameter.name() + " is not a List but " + listType);
        }

        final List<?> list = Cast.to(value);
        return Lists.immutable(
                list.stream()
                        .map(v -> this.convertOrFail(v, listType))
                        .collect(Collectors.toList()));
    }

    @Override
    public Object evaluate(final FunctionExpressionName name,
                           final List<Object> parameters) {
        return this.function(name)
                .apply(parameters, this);
    }

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
        return this.functions + " " + this.references + " " + this.referenceNotFound + " " + this.converterContext;
    }
}
