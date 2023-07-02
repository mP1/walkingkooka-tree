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
 * Holds an subtract operation.
 */
public final class NodeSelectorSubtractionParserToken extends NodeSelectorBinaryParserToken<NodeSelectorSubtractionParserToken> {

    static NodeSelectorSubtractionParserToken with(final List<ParserToken> value,
                                                   final String text) {
        return new NodeSelectorSubtractionParserToken(copyAndCheckTokens(value),
                checkTextNullOrWhitespace(text));
    }

    private NodeSelectorSubtractionParserToken(final List<ParserToken> value,
                                               final String text) {
        super(value, text);
    }

    // children.........................................................................................................

    @Override
    public NodeSelectorSubtractionParserToken setChildren(final List<ParserToken> children) {
        return ParserToken.parentSetChildren(
                this,
                children,
                NodeSelectorSubtractionParserToken::with
        );
    }

    // removeFirstIf....................................................................................................

    @Override
    public Optional<NodeSelectorSubtractionParserToken> removeFirstIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeFirstIfParent(
                this,
                predicate,
                NodeSelectorSubtractionParserToken.class
        );
    }

    // removeIf.........................................................................................................

    @Override
    public NodeSelectorSubtractionParserToken removeIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeIfParent(
                this,
                predicate,
                NodeSelectorSubtractionParserToken.class
        );
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorSubtractionParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                             final ParserToken token) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                token,
                NodeSelectorSubtractionParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorSubtractionParserToken replaceIf(final Predicate<ParserToken> predicate,
                                                        final ParserToken token) {
        return ParserToken.replaceIf(
                this,
                predicate,
                token,
                NodeSelectorSubtractionParserToken.class
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
        return other instanceof NodeSelectorSubtractionParserToken;
    }
}
