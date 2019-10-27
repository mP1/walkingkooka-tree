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
 * Returns a string after removing any leading or trailing spaces and normalizes multiple sequences of spaces into a single space.<br>
 * <a href="https://developer.mozilla.org/en-US/docs/Web/XPath/Functions/normalize-space></a>
 * <br>
 * <pre>
 * The string to be normalized. If omitted, string used will be the same as the context node converted to a string.
 * </pre>
 * Unlike the mention in the mozilla document, if the argument is missing, an exception will be thrown.
 */
final class ExpressionNormalizeSpaceFunction extends ExpressionFunction2<String> {
    /**
     * Singleton
     */
    static final ExpressionNormalizeSpaceFunction INSTANCE = new ExpressionNormalizeSpaceFunction();

    /**
     * Private ctor
     */
    private ExpressionNormalizeSpaceFunction() {
        super();
    }

    @Override
    public String apply(final List<Object> parameters,
                        final ExpressionFunctionContext context) {
        this.checkParameterCount(parameters, 1);

        final String trimmed = this.string(parameters, 0, context)
                .trim();

        final StringBuilder b = new StringBuilder();
        final int length = trimmed.length();
        boolean wasWhitespace = false;

        for (int i = 0; i < length; i++) {
            char c = trimmed.charAt(i);
            final boolean whitespace = Character.isWhitespace(c);
            if (whitespace) {
                if (wasWhitespace) {
                    continue;
                }
                c = ' ';
            }
            wasWhitespace = whitespace;
            b.append(c);
        }

        return b.toString();
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    private final static ExpressionNodeName NAME = ExpressionNodeName.with("normalize-space");

    @Override
    public String toString() {
        return this.name().toString();
    }
}
