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

package walkingkooka.tree.expression.function;

import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A function that concats all the Strings given to it.
 */
final class ConcatExpressionFunction extends ExpressionFunction2<String> {

    /**
     * Singleton
     */
    static final ConcatExpressionFunction INSTANCE = new ConcatExpressionFunction();

    /**
     * Private ctor
     */
    private ConcatExpressionFunction() {
        super();
    }

    @Override
    public String apply(final List<Object> parameters,
                        final ExpressionFunctionContext context) {
        final int count = parameters.size();
        if (count < 1) {
            throw new IllegalArgumentException("Expected at least 1 parameter but got " + count + "=" + parameters.subList(1, count));
        }

        return parameters.stream()
                .map(p -> this.string(p, context))
                .collect(Collectors.joining());
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    private final static FunctionExpressionName NAME = FunctionExpressionName.with("concat");

    @Override
    public String toString() {
        return this.name().toString();
    }
}
