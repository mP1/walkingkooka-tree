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
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.List;
import java.util.Optional;

public class FakeExpressionFunction<T, C extends ExpressionEvaluationContext> implements ExpressionFunction<T, C> {
    @Override
    public T apply(final List<Object> objects,
                   final C expressionFunctionContext) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<ExpressionFunctionName> name() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<T> returnType() {
        throw new UnsupportedOperationException();
    }
}
