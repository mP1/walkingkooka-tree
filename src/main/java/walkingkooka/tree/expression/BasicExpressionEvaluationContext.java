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
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;

import java.math.MathContext;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * An {@link ExpressionEvaluationContext} delegates to helpers or constants for each method.
 */
final class BasicExpressionEvaluationContext implements ExpressionEvaluationContext {

    /**
     * Factory that creates a {@link BasicExpressionEvaluationContext}
     */
    static BasicExpressionEvaluationContext with(final BiFunction<FunctionExpressionName, List<Object>, Object> functions,
                                                 final Function<ExpressionReference, Optional<Expression>> references,
                                                 final Converter converter,
                                                 final ConverterContext converterContext) {
        Objects.requireNonNull(functions, "functions");
        Objects.requireNonNull(references, "references");
        Objects.requireNonNull(converter, "converter");
        Objects.requireNonNull(converterContext, "converterContext");

        return new BasicExpressionEvaluationContext(functions,
                references,
                converter,
                converterContext);
    }

    /**
     * Private ctor use factory
     */
    private BasicExpressionEvaluationContext(final BiFunction<FunctionExpressionName, List<Object>, Object> functions,
                                             final Function<ExpressionReference, Optional<Expression>> references,
                                             final Converter converter,
                                             final ConverterContext converterContext) {
        super();
        this.functions = functions;
        this.references = references;
        this.converter = converter;
        this.converterContext = converterContext;
    }

    @Override
    public String currencySymbol() {
        return this.converterContext.currencySymbol();
    }

    @Override
    public char decimalSeparator() {
        return this.converterContext.decimalSeparator();
    }

    @Override
    public char exponentSymbol() {
        return this.converterContext.exponentSymbol();
    }

    @Override
    public char groupingSeparator() {
        return this.converterContext.groupingSeparator();
    }

    @Override
    public char negativeSign() {
        return this.converterContext.negativeSign();
    }

    @Override
    public char percentageSymbol() {
        return this.converterContext.percentageSymbol();
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
    public Object function(final FunctionExpressionName name, final List<Object> parameters) {
        return this.functions.apply(name, parameters);
    }

    private final BiFunction<FunctionExpressionName, List<Object>, Object> functions;

    @Override
    public Optional<Expression> reference(final ExpressionReference reference) {
        final Optional<Expression> node = this.references.apply(reference);
        if (!node.isPresent()) {
            throw new ExpressionEvaluationReferenceException("Missing reference: " + reference);
        }
        return node;
    }

    private final Function<ExpressionReference, Optional<Expression>> references;

    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> target) {
        return this.converter.convert(value, target, this.converterContext);
    }

    private final Converter converter;
    private final ConverterContext converterContext;

    @Override
    public String toString() {
        return this.converterContext + " " + this.functions + " " + this.references + " " + this.converter;
    }
}
