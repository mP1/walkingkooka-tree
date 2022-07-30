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

package walkingkooka.tree.expression.function;

import walkingkooka.Context;
import walkingkooka.convert.ConverterContext;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberContext;
import walkingkooka.tree.expression.ExpressionNumberFunction;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An {@link ExpressionFunction} that handles unwrapping a single {@link ExpressionNumber} parameter.
 * The {@link ExpressionNumber} value type ({@link java.math.BigDecimal} or {@link double} is then used to call one of the two {@link ExpressionNumberFunction} overloads.
 * The {@link Number} result is then rewrapped back into an {@link ExpressionNumber} and becomes the result of this {@link ExpressionFunction}.
 */
final class ExpressionNumberFunctionExpressionFunction<C extends Context & ConverterContext & ExpressionNumberContext> implements ExpressionFunction<ExpressionNumber, C> {

    static <C extends Context & ConverterContext & ExpressionNumberContext> ExpressionNumberFunctionExpressionFunction<C> with(final Optional<FunctionExpressionName> name,
                                                                                                                               final ExpressionNumberFunction function) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(function, "function");

        return new ExpressionNumberFunctionExpressionFunction<>(name, function);
    }

    private ExpressionNumberFunctionExpressionFunction(final Optional<FunctionExpressionName> name,
                                                       final ExpressionNumberFunction function) {
        super();
        this.name = name;
        this.function = function;
    }

    @Override
    public Optional<FunctionExpressionName> name() {
        return this.name;
    }

    private final Optional<FunctionExpressionName> name;

    @Override
    public ExpressionNumber apply(final List<Object> parameters,
                                  final C context) {
        this.checkParameterCount(parameters);

        return NUMBER.getOrFail(parameters, 0)
                .map(
                        this.function,
                        context.mathContext()
                );
    }

    private final ExpressionNumberFunction function;

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<ExpressionNumber> NUMBER = ExpressionFunctionParameterName.with("number")
            .required(ExpressionNumber.class)
            .setKinds(ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(NUMBER);

    @Override
    public Class<ExpressionNumber> returnType() {
        return ExpressionNumber.class;
    }

    @Override
    public boolean isPure(ExpressionPurityContext context) {
        return true;
    }

    @Override
    public String toString() {
        return this.name()
                .map(FunctionExpressionName::value)
                .orElse(ANONYMOUS);
    }
}
