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
 * Holds an following-sibling axis
 */
public final class NodeSelectorFollowingSiblingParserToken extends NodeSelectorNonSymbolParserToken<String> {

    static NodeSelectorFollowingSiblingParserToken with(final String value, final String text) {
        checkValue(value);
        checkTextNullOrWhitespace(text);

        return new NodeSelectorFollowingSiblingParserToken(value, text);
    }

    private NodeSelectorFollowingSiblingParserToken(final String value, final String text) {
        super(value, text);
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorFollowingSiblingParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                                  final ParserToken token) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                token,
                NodeSelectorFollowingSiblingParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorFollowingSiblingParserToken replaceIf(final Predicate<ParserToken> predicate,
                                                             final ParserToken token) {
        return ParserToken.replaceIf(
                this,
                predicate,
                token,
                NodeSelectorFollowingSiblingParserToken.class
        );
    }

    // Visitor..........................................................................................................

    @Override
    public void accept(final NodeSelectorParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodeSelectorFollowingSiblingParserToken;
    }
}
