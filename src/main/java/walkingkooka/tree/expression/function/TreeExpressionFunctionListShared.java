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

import walkingkooka.Cast;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.List;

/**
 * A {@link ExpressionFunction} which accepts values and returns a {@link List}.
 */
abstract class TreeExpressionFunctionListShared<C extends ExpressionEvaluationContext> extends TreeExpressionFunction<List<?>, C> {

    TreeExpressionFunctionListShared(final String name) {
        super(name);
    }

    @Override
    public final boolean isPure(final ExpressionPurityContext context) {
        return true;
    }

    @Override
    public final List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    @Override
    public final Class<List<?>> returnType() {
        return LIST;
    }

    private final static Class<List<?>> LIST = Cast.to(List.class);

    final static ExpressionFunctionParameter<Object> ELEMENTS = ExpressionFunctionParameterName.with("elements")
        .variable(Object.class)
        .setKinds(
            ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES
        );

    final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        ELEMENTS
    );
}
