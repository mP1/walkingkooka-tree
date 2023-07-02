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
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Holds two selectors joined by a logical OR.
 */
public final class NodeSelectorOrParserToken extends NodeSelectorBinaryParserToken<NodeSelectorOrParserToken> {

    static NodeSelectorOrParserToken with(final List<ParserToken> value,
                                          final String text) {
        return new NodeSelectorOrParserToken(copyAndCheckTokens(value),
                checkTextNullOrWhitespace(text));
    }

    private NodeSelectorOrParserToken(final List<ParserToken> value,
                                      final String text) {
        super(value, text);
    }

    // children.........................................................................................................

    @Override
    public NodeSelectorOrParserToken setChildren(final List<ParserToken> children) {
        return ParserToken.parentSetChildren(
                this,
                children,
                NodeSelectorOrParserToken::with
        );
    }

    // removeFirstIf....................................................................................................

    @Override
    public Optional<NodeSelectorOrParserToken> removeFirstIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeFirstIfParent(
                this,
                predicate,
                NodeSelectorOrParserToken.class
        );
    }

    // removeIf.........................................................................................................

    @Override
    public NodeSelectorOrParserToken removeIf(final Predicate<ParserToken> predicate) {
        return ParserToken.parentRemoveIf(
                this,
                predicate,
                NodeSelectorOrParserToken.class
        );
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorOrParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                    final ParserToken token) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                token,
                NodeSelectorOrParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorOrParserToken replaceIf(final Predicate<ParserToken> predicate,
                                               final ParserToken token) {
        return ParserToken.replaceIf(
                this,
                predicate,
                token,
                NodeSelectorOrParserToken.class
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
        return other instanceof NodeSelectorOrParserToken;
    }
}
