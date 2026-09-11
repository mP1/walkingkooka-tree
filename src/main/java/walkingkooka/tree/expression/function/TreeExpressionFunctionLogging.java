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

import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.List;

abstract class TreeExpressionFunctionLogging<C extends ExpressionEvaluationContext> extends TreeExpressionFunction<Void, C> {

    TreeExpressionFunctionLogging(final String name) {
        super(name);
    }

    @Override
    public final List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    final static ExpressionFunctionParameter<String> MESSAGE = ExpressionFunctionParameterName.with("message")
        .required(String.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        MESSAGE
    );

    @Override
    public final Class<Void> returnType() {
        return Void.class;
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return false;
    }

    @Override
    public final Void apply(final List<Object> parameters,
                            final C context) {
        final String message = MESSAGE.getOrFail(parameters, 0);
        this.log(
            message,
            context
        );

        return null;
    }

    abstract void log(final String message,
                      final C context);
}
