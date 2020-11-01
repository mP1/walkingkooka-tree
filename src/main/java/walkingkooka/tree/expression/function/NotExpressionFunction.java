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

import walkingkooka.Cast;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;

/**
 * A function that inverts the a boolean value.
 */
final class NotExpressionFunction<C extends ExpressionFunctionContext> extends ExpressionFunction2<Boolean, C> {

    /**
     * Instance getter.
     */
    static <C extends ExpressionFunctionContext> NotExpressionFunction<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private static final NotExpressionFunction INSTANCE = new NotExpressionFunction();

    /**
     * Private ctor
     */
    private NotExpressionFunction() {
        super();
    }

    @Override
    public Boolean apply(final List<Object> parameters,
                         final C context) {
        this.checkParameterCount(parameters, 1);

        return !this.booleanValue(parameters, 0, context);
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    private final static FunctionExpressionName NAME = FunctionExpressionName.with("not");

    @Override
    public String toString() {
        return this.name().toString();
    }
}
