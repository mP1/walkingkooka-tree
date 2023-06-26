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
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.function.Predicate;

/**
 * Holds an multiply
 */
public final class NodeSelectorMultiplicationParserToken extends NodeSelectorBinaryParserToken<NodeSelectorMultiplicationParserToken> {

    static NodeSelectorMultiplicationParserToken with(final List<ParserToken> value,
                                                      final String text) {
        return new NodeSelectorMultiplicationParserToken(copyAndCheckTokens(value),
                checkTextNullOrWhitespace(text));
    }

    private NodeSelectorMultiplicationParserToken(final List<ParserToken> value,
                                                  final String text) {
        super(value, text);
    }

    // children.........................................................................................................

    @Override
    public NodeSelectorMultiplicationParserToken setChildren(final List<ParserToken> children) {
        return ParserToken.parentSetChildren(
                this,
                children,
                NodeSelectorMultiplicationParserToken::with
        );
    }

    // removeFirstIf....................................................................................................

    @Override
    public NodeSelectorMultiplicationParserToken removeFirstIf(final Predicate<ParserToken> predicate) {
        return ParserToken.parentRemoveFirstIf(
                this,
                predicate,
                NodeSelectorMultiplicationParserToken.class
        );
    }

    // removeIf.........................................................................................................

    @Override
    public NodeSelectorMultiplicationParserToken removeIf(final Predicate<ParserToken> predicate) {
        return ParserToken.parentRemoveIf(
                this,
                predicate,
                NodeSelectorMultiplicationParserToken.class
        );
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorMultiplicationParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                                final ParserToken token) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                token,
                NodeSelectorMultiplicationParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorMultiplicationParserToken replaceIf(final Predicate<ParserToken> predicate,
                                                           final ParserToken token) {
        return ParserToken.replaceIf(
                this,
                predicate,
                token,
                NodeSelectorMultiplicationParserToken.class
        );
    }

    // Visitor..........................................................................................................

    @Override
    public void accept(final NodeSelectorParserTokenVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // Object........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodeSelectorMultiplicationParserToken;
    }
}
