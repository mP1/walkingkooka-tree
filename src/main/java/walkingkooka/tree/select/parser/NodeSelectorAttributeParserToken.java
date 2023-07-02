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
 * Container for an attribute reference holding its component.
 */
public final class NodeSelectorAttributeParserToken extends NodeSelectorParentParserToken<NodeSelectorAttributeParserToken> {

    static NodeSelectorAttributeParserToken with(final List<ParserToken> value, final String text) {
        return new NodeSelectorAttributeParserToken(copyAndCheckTokens(value),
                checkTextNullOrWhitespace(text));
    }

    private NodeSelectorAttributeParserToken(final List<ParserToken> value,
                                             final String text) {
        super(value, text);

        final List<ParserToken> without = ParserToken.filterWithoutNoise(value);
        final int count = without.size();
        if (count < 1) {
            throw new IllegalArgumentException("Expected at least 1 tokens but got " + count + "=" + without);
        }
        final NodeSelectorParserToken name = without.get(0).cast(NodeSelectorParserToken.class);
        if (!name.isAttributeName()) {
            throw new IllegalArgumentException("Attribute name missing from " + value);
        }
        this.attributeName = ((NodeSelectorAttributeNameParserToken) name).value();
    }

    public NodeSelectorAttributeName attributeName() {
        return this.attributeName;
    }

    private final NodeSelectorAttributeName attributeName;

    // children.........................................................................................................

    @Override
    public NodeSelectorAttributeParserToken setChildren(final List<ParserToken> children) {
        return ParserToken.parentSetChildren(
                this,
                children,
                NodeSelectorAttributeParserToken::with
        );
    }

    // removeFirstIf....................................................................................................

    @Override
    public Optional<NodeSelectorAttributeParserToken> removeFirstIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeFirstIfParent(
                this,
                predicate,
                NodeSelectorAttributeParserToken.class
        );
    }

    // removeIf.........................................................................................................

    @Override
    public NodeSelectorAttributeParserToken removeIf(final Predicate<ParserToken> predicate) {
        return ParserToken.parentRemoveIf(
                this,
                predicate,
                NodeSelectorAttributeParserToken.class
        );
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorAttributeParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                           final ParserToken token) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                token,
                NodeSelectorAttributeParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorAttributeParserToken replaceIf(final Predicate<ParserToken> predicate,
                                                      final ParserToken token) {
        return ParserToken.replaceIf(
                this,
                predicate,
                token,
                NodeSelectorAttributeParserToken.class
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
        return other instanceof NodeSelectorAttributeParserToken;
    }
}
