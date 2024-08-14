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

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;
import java.util.Optional;

/**
 * This is the {@link ExpressionFunction} returned by {@link CallExpression#function(ExpressionEvaluationContext)}.
 */
final class CallExpressionExpressionFunction implements ExpressionFunction<Object, ExpressionEvaluationContext> {

    static CallExpressionExpressionFunction with(final CallExpression expression) {
        return new CallExpressionExpressionFunction(expression);
    }

    private CallExpressionExpressionFunction(final CallExpression expression) {
        this.expression = expression;
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return VALUES; // all objects
    }

    private final static List<ExpressionFunctionParameter<?>> VALUES = Lists.of(
            ExpressionFunctionParameterName.VALUE.variable(Object.class)
    );

    @Override
    public Class<Object> returnType() {
        return Object.class;
    }

    public Object apply(final List<Object> parameterValues,
                        final ExpressionEvaluationContext context) {
        final Object tryEvaluateFunction = this.expression.toValue(context);

        if (!(tryEvaluateFunction instanceof ExpressionFunction)) {
            throw new IllegalArgumentException("Call requires function but got " + CharSequences.quoteIfChars(tryEvaluateFunction));
        }

        return context.evaluateFunction(
                Cast.to(tryEvaluateFunction),
                Cast.to(parameterValues)
        );
    }

    @Override
    public Optional<ExpressionFunctionName> name() {
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

    private final CallExpression expression;
}