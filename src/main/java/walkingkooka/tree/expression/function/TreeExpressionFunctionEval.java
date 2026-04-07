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

import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.List;

/**
 * A {@link ExpressionFunction} which evaluates the given {@link Expression} given as a parameter.
 */
final class TreeExpressionFunctionEval<C extends ExpressionEvaluationContext> extends TreeExpressionFunction<Object, C> {

    static <C extends ExpressionEvaluationContext> TreeExpressionFunctionEval<C> instance() {
        return INSTANCE;
    }

    private final static TreeExpressionFunctionEval INSTANCE = new TreeExpressionFunctionEval<>();

    private TreeExpressionFunctionEval() {
        super("eval");
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return true;
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    @Override
    public Class<Object> returnType() {
        return Object.class;
    }

    @Override
    public Object apply(final List<Object> parameters,
                        final C context) {
        return context.evaluateExpression(
            EXPRESSION.getOrFail(
                parameters,
                0
            )
        );
    }

    private final static ExpressionFunctionParameter<Expression> EXPRESSION = ExpressionFunctionParameterName.with("expression")
        .required(Expression.class)
        .setKinds(
            ExpressionFunctionParameterKind.CONVERT_EVALUATE
        );

    final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        EXPRESSION
    );
}
