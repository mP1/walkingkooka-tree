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

package walkingkooka.tree.select.parser;

import walkingkooka.text.cursor.parser.BinaryOperatorTransformer;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;

final class NodeSelectorEbnfParserCombinatorGrammarTransformerBinaryOperatorTransformer implements BinaryOperatorTransformer {

    /**
     * Singleton
     */
    final static NodeSelectorEbnfParserCombinatorGrammarTransformerBinaryOperatorTransformer INSTANCE = new NodeSelectorEbnfParserCombinatorGrammarTransformerBinaryOperatorTransformer();

    /**
     * Private ctor use singleton
     */
    private NodeSelectorEbnfParserCombinatorGrammarTransformerBinaryOperatorTransformer() {
        super();
    }

    @Override
    public int highestPriority() {
        return NodeSelectorParserToken.HIGHEST_PRIORITY;
    }

    @Override
    public int lowestPriority() {
        return NodeSelectorParserToken.LOWEST_PRIORITY;
    }

    @Override
    public int priority(final ParserToken token) {
        return ((NodeSelectorParserToken) token).operatorPriority();
    }

    @Override
    public ParserToken binaryOperand(final List<ParserToken> tokens,
                                     final String text,
                                     final ParserToken parent) {
        return ((NodeSelectorParserToken) parent).binaryOperand(tokens, text);
    }

    @Override
    public String toString() {
        return NodeSelectorEbnfParserCombinatorGrammarTransformer.class.getSimpleName();
    }
}
