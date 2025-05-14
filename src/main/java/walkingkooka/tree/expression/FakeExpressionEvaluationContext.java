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

import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.convert.FakeExpressionNumberConverterContext;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class FakeExpressionEvaluationContext extends FakeExpressionNumberConverterContext implements ExpressionEvaluationContext {

    public FakeExpressionEvaluationContext() {
        super();
    }

    @Override
    public CaseSensitivity stringEqualsCaseSensitivity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionEvaluationContext enterScope(final Function<ExpressionReference, Optional<Optional<Object>>> scoped) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name) {
        Objects.requireNonNull(name, "name");
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isText(final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                  final Object value) {
        Objects.requireNonNull(parameter, "parameter");

        throw new UnsupportedOperationException();
    }

    @Override
    public Object handleException(final RuntimeException exception) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPure(final ExpressionFunctionName name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Optional<Object>> reference(final ExpressionReference reference) {
        Objects.requireNonNull(reference, "reference");

        throw new UnsupportedOperationException();
    }
}
