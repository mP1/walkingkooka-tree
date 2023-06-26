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

import walkingkooka.text.cursor.parser.ParserToken;

import java.util.function.Predicate;

/**
 * Holds the combination any whitespace that may appear within a selector.
 */
public final class NodeSelectorWhitespaceParserToken extends NodeSelectorNonBinaryOperandSymbolParserToken {

    static NodeSelectorWhitespaceParserToken with(final String value, final String text) {
        return new NodeSelectorWhitespaceParserToken(
                checkValue(value),
                checkTextNullOrEmpty(text)
        );
    }

    private NodeSelectorWhitespaceParserToken(final String value, final String text) {
        super(value, text);
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorWhitespaceParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                            final ParserToken token) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                token,
                NodeSelectorWhitespaceParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorWhitespaceParserToken replaceIf(final Predicate<ParserToken> predicate,
                                                       final ParserToken token) {
        return ParserToken.replaceIf(
                this,
                predicate,
                token,
                NodeSelectorWhitespaceParserToken.class
        );
    }

    // Visitor.........................................................................................................

    @Override
    public void accept(final NodeSelectorParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodeSelectorWhitespaceParserToken;
    }
}
