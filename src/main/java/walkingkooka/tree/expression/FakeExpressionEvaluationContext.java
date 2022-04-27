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

import walkingkooka.tree.expression.function.FakeExpressionFunctionContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FakeExpressionEvaluationContext extends FakeExpressionFunctionContext implements ExpressionEvaluationContext {

    public FakeExpressionEvaluationContext() {
        super();
    }

    @Override
    public Object evaluate(final Expression expression) {
        Objects.requireNonNull(expression, "expression");
        throw new UnsupportedOperationException();
    }

    @Override
    public Object evaluate(final FunctionExpressionName name, final List<Object> parameters) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(parameters, "parameters");
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPure(final FunctionExpressionName name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Object> reference(final ExpressionReference reference) {
        Objects.requireNonNull(reference, "reference");
        throw new UnsupportedOperationException();
    }
}
