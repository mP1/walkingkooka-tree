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
import walkingkooka.tree.expression.ExpressionException;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.ExpressionNumberKind;
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
final class BasicNodeSelectorContextExpressionFunctionContext implements ExpressionFunctionContext {

    static BasicNodeSelectorContextExpressionFunctionContext with(final Function<FunctionExpressionName, Optional<ExpressionFunction<?>>> functions,
                                                                  final Converter<ExpressionNumberConverterContext> converter,
                                                                  final ExpressionNumberConverterContext context) {
        return new BasicNodeSelectorContextExpressionFunctionContext(functions,
                converter,
                context);
    }

    private BasicNodeSelectorContextExpressionFunctionContext(final Function<FunctionExpressionName, Optional<ExpressionFunction<?>>> functions,
                                                              final Converter<ExpressionNumberConverterContext> converter,
                                                              final ExpressionNumberConverterContext context) {
        super();
        this.functions = functions;
        this.converter = converter;
        this.context = context;
    }

    @Override
    public Object function(final FunctionExpressionName name, final List<Object> parameters) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(parameters, "parameters");

        final Optional<ExpressionFunction<?>> function = this.functions.apply(name);
        if(false == function.isPresent()) {
            throw new IllegalArgumentException("Unknown function " + name);
        }
        return function.get().apply(parameters, this);
    }

    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> target) {
        return this.converter.convert(value, target, this.context);
    }

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
    public String toString() {
        return this.functions + " " + this.converter + " " + this.context;
    }

    private final Function<FunctionExpressionName, Optional<ExpressionFunction<?>>> functions;
    private final Converter<ExpressionNumberConverterContext> converter;
    final ExpressionNumberConverterContext context;
}
