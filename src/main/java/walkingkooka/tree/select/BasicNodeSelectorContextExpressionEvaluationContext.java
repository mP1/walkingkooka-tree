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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * The {@link ExpressionFunctionContext} that accompanies each function execution.
 */
final class BasicNodeSelectorContextExpressionEvaluationContext implements ExpressionEvaluationContext {

    static BasicNodeSelectorContextExpressionEvaluationContext with(final BasicNodeSelectorContextExpressionFunctionContext context) {
        return new BasicNodeSelectorContextExpressionEvaluationContext(context);
    }

    private BasicNodeSelectorContextExpressionEvaluationContext(final BasicNodeSelectorContextExpressionFunctionContext context) {
        super();
        this.context = context;
    }

    @Override
    public Object function(final FunctionExpressionName name, final List<Object> parameters) {
        return this.context.function(name, parameters);
    }

    @Override
    public Optional<Expression> reference(final ExpressionReference reference) {
        Objects.requireNonNull(reference, "reference");
        return Optional.empty();
    }

    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> target) {
        return this.context.convert(value, target);
    }

    @Override
    public Locale locale() {
        return this.context.context.locale();
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
        return this.context.context.currencySymbol();
    }

    @Override
    public char decimalSeparator() {
        return this.context.context.decimalSeparator();
    }

    @Override
    public String exponentSymbol() {
        return this.context.context.exponentSymbol();
    }

    @Override
    public char groupingSeparator() {
        return this.context.context.groupingSeparator();
    }

    @Override
    public char percentageSymbol() {
        return this.context.context.percentageSymbol();
    }

    @Override
    public char negativeSign() {
        return this.context.context.negativeSign();
    }

    @Override
    public char positiveSign() {
        return this.context.context.positiveSign();
    }

    private final BasicNodeSelectorContextExpressionFunctionContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
