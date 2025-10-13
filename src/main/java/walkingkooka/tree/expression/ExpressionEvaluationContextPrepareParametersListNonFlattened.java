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

import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;

import java.util.List;
import java.util.Optional;

/**
 * A lazy {@link List} of the original parameter values, performing the following if they are enabled for this namedFunction.
 * <ul>
 * <li>Evaluate {@link Expression} if {@link ExpressionFunctionParameterKind#EVALUATE}</li>
 * <li>Resolve {@link ReferenceExpression} if {@link ExpressionFunctionParameterKind#RESOLVE_REFERENCES}</li>
 * <li>Convert values to the {@link ExpressionFunctionParameter#type()}</li>
 * </ul>
 * The above list is only performed once for each parameter and cached for future fetches.
 */
final class ExpressionEvaluationContextPrepareParametersListNonFlattened extends ExpressionEvaluationContextPrepareParametersList {

    static ExpressionEvaluationContextPrepareParametersListNonFlattened withNonFlattened(final List<ExpressionFunctionParameter<?>> parameters,
                                                                                         final List<Object> values,
                                                                                         final int preparedValuesCount,
                                                                                         final Optional<ExpressionFunctionName> functionName,
                                                                                         final ExpressionEvaluationContext context) {
        return new ExpressionEvaluationContextPrepareParametersListNonFlattened(
            parameters,
            values,
            preparedValuesCount,
            functionName,
            context
        );
    }

    /**
     * Private ctor
     */
    private ExpressionEvaluationContextPrepareParametersListNonFlattened(final List<ExpressionFunctionParameter<?>> parameters,
                                                                         final List<Object> values,
                                                                         final int preparedValuesCount,
                                                                         final Optional<ExpressionFunctionName> functionName,
                                                                         final ExpressionEvaluationContext context) {
        super(
            parameters,
            values,
            preparedValuesCount,
            functionName,
            context
        );
    }

    @Override
    public Object get(final int index) {
        return this.getPrepareIfNecessary(index);
    }

    @Override
    public int size() {
        return this.preparedValues.length;
    }
}
