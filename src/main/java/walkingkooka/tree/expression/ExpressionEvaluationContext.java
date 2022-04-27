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

import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionContext;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;

/**
 * Context that travels during any expression evaluation.
 */
public interface ExpressionEvaluationContext extends ExpressionFunctionContext,
        ExpressionPurityContext {

    /**
     * Evaluate the given {@link Expression} returning the result/value.
     */
    Object evaluate(final Expression expression);

    /**
     * Wraps the {@link List} of parameters values and performs several actions lazily for each parameter.
     * <ul>
     * <li>Resolve {@link Expression} if {@link ExpressionFunction#requiresEvaluatedParameters()}</li>
     * <li>Resolve {@link ReferenceExpression} if {@link ExpressionFunction#resolveReferences()}</li>
     * <li>Convert values to the {@link ExpressionFunctionParameter#type()}</li>
     * </ul>
     * The above list is only performed once for each parameter and cached for future fetches.
     */
    default List<Object> prepareParameters(final ExpressionFunction<?, ExpressionFunctionContext> function,
                                           final List<Object> parameters) {
        return ExpressionEvaluationContextPrepareParametersList.with(
                parameters,
                function,
                this
        );
    }
}
