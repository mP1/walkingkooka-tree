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
 * Holds an addition
 */
public final class NodeSelectorAdditionParserToken extends NodeSelectorBinaryParserToken<NodeSelectorAdditionParserToken> {

    static NodeSelectorAdditionParserToken with(final List<ParserToken> value,
                                                final String text) {
        return new NodeSelectorAdditionParserToken(copyAndCheckTokens(value),
                checkTextNullOrWhitespace(text));
    }

    private NodeSelectorAdditionParserToken(final List<ParserToken> value,
                                            final String text) {
        super(value, text);
    }

    // children.........................................................................................................

    @Override
    public NodeSelectorAdditionParserToken setChildren(final List<ParserToken> children) {
        return ParserToken.parentSetChildren(
                this,
                children,
                NodeSelectorAdditionParserToken::with
        );
    }

    // removeFirstIf....................................................................................................

    @Override
    public NodeSelectorAdditionParserToken removeFirstIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeFirstIfParent(
                this,
                predicate,
                NodeSelectorAdditionParserToken.class
        );
    }

    // removeIf.........................................................................................................

    @Override
    public NodeSelectorAdditionParserToken removeIf(final Predicate<ParserToken> predicate) {
        return ParserToken.parentRemoveIf(
                this,
                predicate,
                NodeSelectorAdditionParserToken.class
        );
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorAdditionParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                          final ParserToken token) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                token,
                NodeSelectorAdditionParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorAdditionParserToken replaceIf(final Predicate<ParserToken> predicate,
                                                     final ParserToken token) {
        return ParserToken.replaceIf(
                this,
                predicate,
                token,
                NodeSelectorAdditionParserToken.class
        );
    }

    // Visitor........................................................................................................

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
        return other instanceof NodeSelectorAdditionParserToken;
    }
}
