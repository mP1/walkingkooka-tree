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

package walkingkooka.tree.select;

import walkingkooka.Either;
import walkingkooka.convert.Converter;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionException;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionContext;

import java.math.MathContext;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

/**
 * The {@link ExpressionFunctionContext} that accompanies each function execution.
 */
final class BasicNodeSelectorContextExpressionEvalutionContext implements ExpressionEvaluationContext {

    static BasicNodeSelectorContextExpressionEvalutionContext with(final Function<FunctionExpressionName, Optional<ExpressionFunction<?>>> functions,
                                                                   final Converter<ExpressionNumberConverterContext> converter,
                                                                   final ExpressionNumberConverterContext context) {
        return new BasicNodeSelectorContextExpressionEvalutionContext(functions,
                converter,
                context);
    }

    private BasicNodeSelectorContextExpressionEvalutionContext(final Function<FunctionExpressionName, Optional<ExpressionFunction<?>>> functions,
                                                               final Converter<ExpressionNumberConverterContext> converter,
                                                               final ExpressionNumberConverterContext context) {
        super();
        this.functions = functions;
        this.converter = converter;
        this.context = context;
    }

    @Override
    public Object function(final FunctionExpressionName name, final List<Object> parameters) {
        final Optional<ExpressionFunction<?>> function = this.functions.apply(name);
        if (!function.isPresent()) {
            throw new ExpressionException("Unknown function " + name);
        }
        return function(name, parameters);
    }

    @Override public Optional<Expression> reference(ExpressionReference reference) {
        return Optional.empty();
    }

    private final Function<FunctionExpressionName, Optional<ExpressionFunction<?>>> functions;

    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> target) {
        return this.converter.convert(value, target, this.context);
    }

    private final Converter<ExpressionNumberConverterContext> converter;

    @Override
    public Locale locale() {
        return this.context.locale();
    }

    @Override
    public MathContext mathContext() {
        return this.context.mathContext();
    }

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        return this.context.expressionNumberKind();
    }

    @Override
    public String currencySymbol() {
        return this.currencySymbol();
    }

    @Override
    public char decimalSeparator() {
        return this.decimalSeparator();
    }

    @Override
    public String exponentSymbol() {
        return this.exponentSymbol();
    }

    @Override
    public char groupingSeparator() {
        return this.groupingSeparator();
    }

    @Override
    public char percentageSymbol() {
        return this.context.percentageSymbol();
    }

    @Override
    public char negativeSign() {
        return this.context.negativeSign();
    }

    @Override
    public char positiveSign() {
        return this.context.positiveSign();
    }

    private final ExpressionNumberConverterContext context;

    @Override
    public String toString() {
        return this.functions + " " + this.converter + " " + this.context;
    }
}
