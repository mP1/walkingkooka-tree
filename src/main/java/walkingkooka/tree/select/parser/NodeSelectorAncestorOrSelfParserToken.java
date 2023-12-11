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

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Holds an ancestor-or-self:: axis
 */
public final class NodeSelectorAncestorOrSelfParserToken extends NodeSelectorNonSymbolParserToken<String> {

    static NodeSelectorAncestorOrSelfParserToken with(final String value, final String text) {
        checkValue(value);
        checkTextNullOrWhitespace(text);

        return new NodeSelectorAncestorOrSelfParserToken(value, text);
    }

    private NodeSelectorAncestorOrSelfParserToken(final String value, final String text) {
        super(value, text);
    }

    // removeFirstIf....................................................................................................

    @Override
    public Optional<NodeSelectorAncestorOrSelfParserToken> removeFirstIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeFirstIfLeaf(
                this,
                predicate,
                NodeSelectorAncestorOrSelfParserToken.class
        );
    }

    // removeIf.........................................................................................................

    @Override
    public Optional<NodeSelectorAncestorOrSelfParserToken> removeIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeIfLeaf(
                this,
                predicate,
                NodeSelectorAncestorOrSelfParserToken.class
        );
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorAncestorOrSelfParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                                final Function<ParserToken, ParserToken> mapper) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                mapper,
                NodeSelectorAncestorOrSelfParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorAncestorOrSelfParserToken replaceIf(final Predicate<ParserToken> predicate,
                                                           final Function<ParserToken, ParserToken> mapper) {
        return ParserToken.replaceIf(
                this,
                predicate,
                mapper,
                NodeSelectorAncestorOrSelfParserToken.class
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
        return other instanceof NodeSelectorAncestorOrSelfParserToken;
    }
}
