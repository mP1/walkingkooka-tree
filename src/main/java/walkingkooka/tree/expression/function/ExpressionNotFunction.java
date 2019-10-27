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

import walkingkooka.tree.expression.ExpressionNodeName;

import java.util.List;
import java.util.Objects;

/**
 * A function that inverts the result of another function.
 */
final class ExpressionNotFunction extends ExpressionFunction2<Boolean> {

    /**
     * Factory
     */
    static ExpressionNotFunction with(final ExpressionFunction<?> function) {
        Objects.requireNonNull(function, "function");
        return new ExpressionNotFunction(function);
    }

    /**
     * Private ctor
     */
    private ExpressionNotFunction(final ExpressionFunction<?> function) {
        super();
        this.function = function;
    }

    @Override
    public Boolean apply(final List<Object> parameters,
                         final ExpressionFunctionContext context) {
        this.checkParameterCount(parameters, 2);

        return !context.convertOrFail(this.function.apply(parameters, context), Boolean.class);
    }

    private final ExpressionFunction<?> function;

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    private final static ExpressionNodeName NAME = ExpressionNodeName.with("not");

    @Override
    public String toString() {
        return "not(" + this.function + ")";
    }
}
