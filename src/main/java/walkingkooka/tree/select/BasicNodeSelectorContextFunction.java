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

package walkingkooka.tree.select;

import walkingkooka.collect.map.Maps;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctions;

import java.util.Map;
import java.util.function.Function;

/**
 * A {@link Function} that knows all xpath functions present in {@link walkingkooka.tree.expression.function.ExpressionFunctions}.
 */
final class BasicNodeSelectorContextFunction implements Function<FunctionExpressionName, ExpressionFunction<?, ?>> {

    /**
     * Singleton
     */
    final static BasicNodeSelectorContextFunction INSTANCE = new BasicNodeSelectorContextFunction();

    /**
     * Private ctor use singleton
     */
    private BasicNodeSelectorContextFunction() {
        super();

        // must be init before register calls to avoid NPE.
        this.nameToFunction = Maps.sorted();

        ExpressionFunctions.visit(this::register);
    }

    private void register(final ExpressionFunction<?, ?> function) {
        this.nameToFunction.put(
                function.name()
                        .orElseThrow(() -> new IllegalStateException("Missing namedFunction name")),
                function
        );
    }

    @Override
    public ExpressionFunction<?, ?> apply(final FunctionExpressionName name) {
        final ExpressionFunction<?, ?> function = this.nameToFunction.get(name);
        if (null == function) {
            throw new IllegalArgumentException("Unknown function " + name);
        }
        return function;
    }

    /**
     * Provides a lookup by {@link FunctionExpressionName namedFunction name} to the actual namedFunction.
     */
    private final Map<FunctionExpressionName, ExpressionFunction<?, ?>> nameToFunction;

    @Override
    public String toString() {
        return this.nameToFunction.toString();
    }
}
