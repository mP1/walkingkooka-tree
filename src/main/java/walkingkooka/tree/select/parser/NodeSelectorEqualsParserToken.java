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
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Parser token that represents an equals condition including parameters.
 */
public final class NodeSelectorEqualsParserToken extends NodeSelectorBinaryParserToken<NodeSelectorEqualsParserToken> {

    static NodeSelectorEqualsParserToken with(final List<ParserToken> value,
                                              final String text) {
        return new NodeSelectorEqualsParserToken(copyAndCheckTokens(value),
                checkTextNullOrWhitespace(text));
    }

    private NodeSelectorEqualsParserToken(final List<ParserToken> value,
                                          final String text) {
        super(value, text);
    }

    // children.........................................................................................................

    @Override
    public NodeSelectorEqualsParserToken setChildren(final List<ParserToken> children) {
        return ParserToken.parentSetChildren(
                this,
                children,
                NodeSelectorEqualsParserToken::with
        );
    }

    // removeFirstIf....................................................................................................

    @Override
    public Optional<NodeSelectorEqualsParserToken> removeFirstIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeFirstIfParent(
                this,
                predicate,
                NodeSelectorEqualsParserToken.class
        );
    }

    // removeIf.........................................................................................................

    @Override
    public Optional<NodeSelectorEqualsParserToken> removeIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeIfParent(
                this,
                predicate,
                NodeSelectorEqualsParserToken.class
        );
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorEqualsParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                        final Function<ParserToken, ParserToken> mapper) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                mapper,
                NodeSelectorEqualsParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorEqualsParserToken replaceIf(final Predicate<ParserToken> predicate,
                                                   final Function<ParserToken, ParserToken> mapper) {
        return ParserToken.replaceIf(
                this,
                predicate,
                mapper,
                NodeSelectorEqualsParserToken.class
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
        return other instanceof NodeSelectorEqualsParserToken;
    }
}
