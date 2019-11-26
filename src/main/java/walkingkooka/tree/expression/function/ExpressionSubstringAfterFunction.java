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
 * A function that returns the part of string1 after the first occurrence of string2
 */
final class ExpressionSubstringAfterFunction extends ExpressionFunction2<String> {

    /**
     * Singleton
     */
    static final ExpressionSubstringAfterFunction INSTANCE = new ExpressionSubstringAfterFunction();

    /**
     * Private ctor
     */
    private ExpressionSubstringAfterFunction() {
        super();
    }

    @Override
    public String apply(final List<Object> parameters,
                        final ExpressionFunctionContext context) {
        this.checkParameterCount(parameters, 2);

        final String string = this.string(parameters, 0, context);
        final String find = this.string(parameters, 1, context);
        final int offset = string.indexOf(find);

        return -1 != offset ?
                string.substring(offset + find.length()) :
                "";
    }


    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    private final static FunctionExpressionName NAME = FunctionExpressionName.with("substring-after");

    @Override
    public String toString() {
        return this.name().toString();
    }
}
