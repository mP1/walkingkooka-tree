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

import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.List;
import java.util.Optional;

/**
 * A {@link ExpressionFunction} that always returns a null value.
 */
final class ExpressionFunctionNullValue<C extends ExpressionEvaluationContext> implements ExpressionFunction<Object, C> {

    static <C extends ExpressionEvaluationContext> ExpressionFunctionNullValue<C> instance() {
        return INSTANCE;
    }

    /**
     * Singleton
     */
    private final static ExpressionFunctionNullValue INSTANCE = new ExpressionFunctionNullValue();

    private ExpressionFunctionNullValue() {
    }

    @Override
    public Optional<ExpressionFunctionName> name() {
        return NAME;
    }

    private final static Optional<ExpressionFunctionName> NAME = Optional.of(
        ExpressionFunctionName.with("nullValue")
    );

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

    @Override
    public String toString() {
        return NAME.get()
            .toString();
    }
}
