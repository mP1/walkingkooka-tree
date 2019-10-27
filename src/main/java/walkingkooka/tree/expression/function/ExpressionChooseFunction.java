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

/**
 * A choose function.
 * <a href="https://developer.mozilla.org/en-US/docs/Web/XPath/Functions/choose"></a>
 * <pre>
 * The choose function returns one of the specified objects based on a boolean parameter.
 *
 * Note: This method should be used instead of if(), which has been deprecated.
 * SyntaxSection
 * choose( boolean , object1, object2 )
 * ArgumentsSection
 * boolean
 * The boolean operation to use when determining which object to return.
 * object1
 * The first object to consider returning.
 * object2
 * The second object to consider returning.
 * </pre>
 */
final class ExpressionChooseFunction extends ExpressionFunction2<Object> {

    /**
     * Singleton
     */
    static final ExpressionChooseFunction INSTANCE = new ExpressionChooseFunction();

    /**
     * Private ctor
     */
    private ExpressionChooseFunction() {
        super();
    }

    @Override
    public Object apply(final List<Object> parameters,
                        final ExpressionFunctionContext context) {
        this.checkParameterCount(parameters, 3);

        return this.parameter(parameters,
                this.booleanValue(parameters, 0, context) ? 1 : 2);
    }


    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    private final static ExpressionNodeName NAME = ExpressionNodeName.with("choose");

    @Override
    public String toString() {
        return this.name().toString();
    }
}
