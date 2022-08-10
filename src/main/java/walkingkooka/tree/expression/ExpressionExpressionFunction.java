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

package walkingkooka.tree.expression;

import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;
import java.util.Optional;

/**
 * This is the {@link ExpressionFunction} returned by {@link Expression#function(ExpressionEvaluationContext)} for all sub classes except
 * {@link CallExpression}.
 */
final class ExpressionExpressionFunction implements ExpressionFunction<Object, ExpressionEvaluationContext> {

    static ExpressionExpressionFunction with(final Expression expression) {
        return new ExpressionExpressionFunction(expression);
    }

    private ExpressionExpressionFunction(final Expression expression) {
        this.expression = expression;
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return ExpressionFunctionParameter.EMPTY;
    }

    @Override
    public Class<Object> returnType() {
        return Object.class;
    }

    public Object apply(final List<Object> parameterValues,
                        final ExpressionEvaluationContext context) {
        this.parameters(parameterValues.size());

        return this.expression.toValue(context);
    }

    @Override
    public Optional<FunctionExpressionName> name() {
        return Optional.empty();
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return this.expression.isPure(context);
    }

    @Override
    public String toString() {
        return this.expression + "()";
    }

    private final Expression expression;
}