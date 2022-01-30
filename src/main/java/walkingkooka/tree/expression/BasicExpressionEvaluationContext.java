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
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionContext;

import java.math.MathContext;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * An {@link ExpressionEvaluationContext} delegates to helpers or constants for each method.
 */
final class BasicExpressionEvaluationContext implements ExpressionEvaluationContext {

    /**
     * Factory that creates a {@link BasicExpressionEvaluationContext}
     */
    static BasicExpressionEvaluationContext with(final Function<FunctionExpressionName, ExpressionFunction<?, ExpressionFunctionContext>> functions,
                                                 final ExpressionFunctionContext functionContext,
                                                 final CaseSensitivity stringEqualityCaseSensitivity) {
        Objects.requireNonNull(functions, "functions");
        Objects.requireNonNull(functionContext, "functionContext");
        Objects.requireNonNull(stringEqualityCaseSensitivity, "stringEqualityCaseSensitivity");

        return new BasicExpressionEvaluationContext(
                functions,
                functionContext,
                stringEqualityCaseSensitivity
        );
    }

    /**
     * Private ctor use factory
     */
    private BasicExpressionEvaluationContext(final Function<FunctionExpressionName, ExpressionFunction<?, ExpressionFunctionContext>> functions,
                                             final ExpressionFunctionContext functionContext,
                                             final CaseSensitivity stringEqualityCaseSensitivity) {
        super();
        this.functions = functions;
        this.functionContext = functionContext;
        this.stringEqualityCaseSensitivity = stringEqualityCaseSensitivity;
    }

    // ExpressionNumberContext.........................................................................................

    @Override
    public String currencySymbol() {
        return this.functionContext.currencySymbol();
    }

    @Override
    public char decimalSeparator() {
        return this.functionContext.decimalSeparator();
    }

    @Override
    public String exponentSymbol() {
        return this.functionContext.exponentSymbol();
    }

    @Override
    public char groupingSeparator() {
        return this.functionContext.groupingSeparator();
    }

    @Override
    public char percentageSymbol() {
        return this.functionContext.percentageSymbol();
    }

    @Override
    public char negativeSign() {
        return this.functionContext.negativeSign();
    }

    @Override
    public char positiveSign() {
        return this.functionContext.positiveSign();
    }

    @Override
    public Locale locale() {
        return this.functionContext.locale();
    }

    @Override
    public MathContext mathContext() {
        return this.functionContext.mathContext();
    }

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        return this.functionContext.expressionNumberKind();
    }

    @Override
    public int defaultYear() {
        return this.functionContext.defaultYear();
    }

    @Override
    public int twoToFourDigitYear(final int year) {
        return this.functionContext.twoToFourDigitYear(year);
    }

    @Override
    public int twoDigitYear() {
        return this.functionContext.twoDigitYear();
    }

    @Override
    public ExpressionFunction<?, ExpressionFunctionContext> function(final FunctionExpressionName name) {
        return this.functions.apply(name);
    }

    private final Function<FunctionExpressionName, ExpressionFunction<?, ExpressionFunctionContext>> functions;

    @Override
    public Object evaluate(final Expression expression) {
        return expression.toValue(this);
    }

    @Override
    public Object evaluate(final FunctionExpressionName name, final List<Object> parameters) {
        final ExpressionFunction<?, ExpressionFunctionContext> function = this.function(name);

        return function.apply(
                this.prepareParameters(function, parameters),
                this.functionContext
        );
    }

    private final ExpressionFunctionContext functionContext;

    @Override
    public boolean isPure(final FunctionExpressionName name) {
        return this.functions.apply(name).isPure(this);
    }

    @Override
    public Optional<Object> reference(final ExpressionReference reference) {
        return this.functionContext.reference(reference);
    }

    @Override
    public ExpressionEvaluationException referenceNotFound(final ExpressionReference reference) {
        return this.functionContext.referenceNotFound(reference);
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type) {
        return this.functionContext.canConvert(value, type);
    }

    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> target) {
        return this.functionContext.convert(value, target);
    }

    @Override
    public CaseSensitivity stringEqualityCaseSensitivity() {
        return this.stringEqualityCaseSensitivity;
    }

    private final CaseSensitivity stringEqualityCaseSensitivity;

    @Override
    public String toString() {
        return this.functions + " " + this.functionContext + " stringEquality: " + this.stringEqualityCaseSensitivity;
    }
}
