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

import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link ExpressionFunction} which evaluates the given {@link Expression}. The expression ignores any given parameters.
 */
final class ExpressionFunctionEval<C extends ExpressionEvaluationContext> implements ExpressionFunction<Object, C> {

    static <C extends ExpressionEvaluationContext> ExpressionFunctionEval<C> with(final Expression expression) {
        return new ExpressionFunctionEval<>(
            Objects.requireNonNull(expression, "expression")
        );
    }

    private ExpressionFunctionEval(final Expression expression) {
        this.expression = expression;
    }

    @Override
    public Optional<ExpressionFunctionName> name() {
        return NAME;
    }

    private final static Optional<ExpressionFunctionName> NAME = Optional.of(
        ExpressionFunctionName.with("eval")
    );

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return this.expression.isPure(context);
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of();

    @Override
    public Class<Object> returnType() {
        return Object.class; // return type of Expression is unknown so return Object
    }

    @Override
    public Object apply(final List<Object> parameters,
                        final C context) {
        // ignore parameters,
        return context.evaluateExpression(this.expression);
    }

    private final Expression expression;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return NAME.get()
            .toString();
    }
}
