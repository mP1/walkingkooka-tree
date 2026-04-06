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
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.List;

/**
 * A {@link ExpressionFunction} that always returns a null value.
 */
final class TreeExpressionFunctionNull<C extends ExpressionEvaluationContext> extends TreeExpressionFunction<Object, C> {

    static <C extends ExpressionEvaluationContext> TreeExpressionFunctionNull<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeExpressionFunctionNull<?> INSTANCE = new TreeExpressionFunctionNull<>();

    private TreeExpressionFunctionNull() {
        super("null");
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return true;
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return Lists.empty();
    }

    @Override
    public Class<Object> returnType() {
        return Object.class;
    }

    @Override
    public Object apply(final List<Object> objects,
                        final C c) {
        this.checkParameterCount(objects);
        return null;
    }
}
