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
 * A function that returns a substring of another string.<br>
 * <a href="https://developer.mozilla.org/en-US/docs/Web/XPath/Functions/substring"></a>
 */
final class ExpressionSubstringFunction extends ExpressionFunction2<String> {

    /**
     * Factory that returns a matching {@link ExpressionSubstringFunction}
     */
    static ExpressionSubstringFunction with(final int indexBase) {
        ExpressionSubstringFunction result;
        switch (indexBase) {
            case 0:
                result = ZERO;
                break;
            case 1:
                result = ONE;
                break;
            default:
                throw new IllegalArgumentException("Invalid indexBase " + indexBase + " expected either 0 or 1");
        }
        return result;
    }

    /**
     * Singleton
     */
    private static final ExpressionSubstringFunction ZERO = new ExpressionSubstringFunction(0);

    /**
     * Singleton
     */
    private static final ExpressionSubstringFunction ONE = new ExpressionSubstringFunction(1);

    /**
     * Private ctor
     */
    private ExpressionSubstringFunction(final int indexBase) {
        super();
        this.indexBase = indexBase;
    }

    @Override
    public String apply(final List<Object> parameters,
                        final ExpressionFunctionContext context) {
        final int parameterCount = parameters.size();
        switch (parameterCount) {
            case 2:
            case 3:
                break;
            default:
                throw new IllegalArgumentException("Expected 2 or 3 parameters (String, offset, [length])=" + parameters.subList(1, parameterCount));
        }

        final String string = this.string(parameters, 0, context);

        final int offset = this.integer(parameters, 1, context);
        final int zeroOffset = offset - this.indexBase;

        final int length = parameterCount == 3 ?
                this.integer(parameters, 2, context) :
                string.length() - offset + this.indexBase;

        return string.substring(zeroOffset, length + zeroOffset);
    }

    private final int indexBase;


    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    private final static ExpressionNodeName NAME = ExpressionNodeName.with("substring");

    @Override
    public String toString() {
        return this.name().toString();
    }
}
