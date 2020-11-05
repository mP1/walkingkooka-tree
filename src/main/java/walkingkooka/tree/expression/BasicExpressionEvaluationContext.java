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
    static BasicExpressionEvaluationContext with(final ExpressionNumberKind expressionNumberKind,
                                                 final BiFunction<FunctionExpressionName, List<Object>, Object> functions,
                                                 final Function<ExpressionReference, Optional<Expression>> references,
                                                 final ConverterContext converterContext) {
        Objects.requireNonNull(expressionNumberKind, "expressionNumberKind");
        Objects.requireNonNull(functions, "functions");
        Objects.requireNonNull(references, "references");
        Objects.requireNonNull(converterContext, "converterContext");

        return new BasicExpressionEvaluationContext(expressionNumberKind,
                functions,
                references,
                converterContext);
    }

    /**
     * Private ctor use factory
     */
    private BasicExpressionEvaluationContext(final ExpressionNumberKind expressionNumberKind,
                                             final BiFunction<FunctionExpressionName, List<Object>, Object> functions,
                                             final Function<ExpressionReference, Optional<Expression>> references,
                                             final ConverterContext converterContext) {
        super();
        this.expressionNumberKind = expressionNumberKind;
        this.functions = functions;
        this.references = references;
        this.converterContext = converterContext;
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
    public Object evaluate(final Expression expression) {
        return expression.toValue(this);
    }

    @Override
    public Object evaluate(final FunctionExpressionName name, final List<Object> parameters) {
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
        return this.functions + " " + this.references + " " + this.converterContext;
    }
}
