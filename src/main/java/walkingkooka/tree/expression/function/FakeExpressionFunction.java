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

import walkingkooka.Context;
import walkingkooka.convert.ConverterContext;
import walkingkooka.tree.expression.ExpressionNumberContext;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;
import java.util.Set;

public class FakeExpressionFunction<T, C extends Context & ConverterContext & ExpressionNumberContext> implements ExpressionFunction<T, C> {
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
    public FunctionExpressionName name() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<T> returnType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<ExpressionFunctionKind> kinds() {
        throw new UnsupportedOperationException();
    }
}
