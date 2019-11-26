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

/**
 * Returns the length of the provided string.
 * <a href="https://developer.mozilla.org/en-US/docs/Web/XPath/Functions/string-length"></a>
 * Unlike the Mozilla documentation, if the argument is missing an exception is thrown.
 */
final class ExpressionStringLengthFunction extends ExpressionFunction2<Number> {
    /**
     * Singleton
     */
    static final ExpressionStringLengthFunction INSTANCE = new ExpressionStringLengthFunction();

    /**
     * Private ctor
     */
    private ExpressionStringLengthFunction() {
        super();
    }

    @Override
    public Number apply(final List<Object> parameters,
                        final ExpressionFunctionContext context) {
        this.checkParameterCount(parameters, 1);

        return (long) this.string(parameters, 0, context).length();
    }


    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    private final static FunctionExpressionName NAME = FunctionExpressionName.with("string-length");

    @Override
    public String toString() {
        return this.name().toString();
    }
}
