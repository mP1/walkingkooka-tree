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

/**
 * Container for an attribute reference holding its component.
 */
public final class AttributeNodeSelectorParserToken extends ParentNodeSelectorParserToken<AttributeNodeSelectorParserToken> {

    static AttributeNodeSelectorParserToken with(final List<ParserToken> value, final String text) {
        return new AttributeNodeSelectorParserToken(copyAndCheckTokens(value),
            checkTextNullOrWhitespace(text));
    }

    private AttributeNodeSelectorParserToken(final List<ParserToken> value,
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
        this.attributeName = ((AttributeNameNodeSelectorParserToken) name).value();
    }

    public NodeSelectorAttributeName attributeName() {
        return this.attributeName;
    }

    private final NodeSelectorAttributeName attributeName;

    // children.........................................................................................................

    @Override
    public AttributeNodeSelectorParserToken setChildren(final List<ParserToken> children) {
        return ParserToken.parentSetChildren(
            this,
            children,
            AttributeNodeSelectorParserToken::with
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
}
