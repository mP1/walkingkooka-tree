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

import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberFunction;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * An {@link ExpressionFunction} that delegates all calls to a {@link ExpressionNumberFunction}. A single {@link ExpressionNumber}
 * parameter is required and only accepted.
 */
final class ExpressionNumberFunctionExpressionFunction<C extends ExpressionFunctionContext> implements ExpressionFunction<ExpressionNumber, C> {

    static <C extends ExpressionFunctionContext> ExpressionNumberFunctionExpressionFunction<C> with(final FunctionExpressionName name,
                                                                                                    final ExpressionNumberFunction function) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(function, "function");

        return new ExpressionNumberFunctionExpressionFunction<>(name, function);
    }

    private ExpressionNumberFunctionExpressionFunction(final FunctionExpressionName name,
                                                       final ExpressionNumberFunction function) {
        super();
        this.name = name;
        this.function = function;
    }

    @Override
    public FunctionExpressionName name() {
        return this.name;
    }

    private final FunctionExpressionName name;

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
    public List<ExpressionFunctionParameter<?>> parameters() {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<ExpressionNumber> NUMBER = ExpressionFunctionParameterName.with("number")
            .required(ExpressionNumber.class);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(NUMBER);

    public boolean lsLastParameterVariable() {
        return false;
    }

    @Override
    public Class<ExpressionNumber> returnType() {
        return ExpressionNumber.class;
    }

    @Override
    public Set<ExpressionFunctionKind> kinds() {
        return KINDS;
    }

    private final Set<ExpressionFunctionKind> KINDS = EnumSet.of(
            ExpressionFunctionKind.EVALUATE_PARAMETERS,
            ExpressionFunctionKind.RESOLVE_REFERENCES
    );

    @Override
    public boolean isPure(ExpressionPurityContext context) {
        return true;
    }

    @Override
    public String toString() {
        return this.name().toString();
    }
}
