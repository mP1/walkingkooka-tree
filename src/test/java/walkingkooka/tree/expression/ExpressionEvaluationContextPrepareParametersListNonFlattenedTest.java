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

import java.util.List;
import java.util.Optional;

public final class ExpressionEvaluationContextPrepareParametersListNonFlattenedTest extends ExpressionEvaluationContextPrepareParametersListTestCase2<ExpressionEvaluationContextPrepareParametersListNonFlattened> {

    @Override
    ExpressionEvaluationContextPrepareParametersListNonFlattened createList(final List<ExpressionFunctionParameter<?>> parameters,
                                                                            final List<Object> values,
                                                                            final Optional<ExpressionFunctionName> functionName,
                                                                            final ExpressionEvaluationContext context) {
        return ExpressionEvaluationContextPrepareParametersListNonFlattened.withNonFlattened(
            parameters,
            values,
            values.size(),
            functionName,
            context
        );
    }

    @Override
    void sizeAndCheck2(final List<?> list,
                       final int size) {
        this.sizeAndCheck(list, size);
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<ExpressionEvaluationContextPrepareParametersListNonFlattened> type() {
        return ExpressionEvaluationContextPrepareParametersListNonFlattened.class;
    }
}
