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

import walkingkooka.text.cursor.parser.ParentParserToken;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.visit.Visiting;

import java.util.List;

/**
 * Holds a unary negative token with an argument such as a number or function.
 */
public final class NodeSelectorNegativeParserToken extends NodeSelectorParentParserToken<NodeSelectorNegativeParserToken> {

    static NodeSelectorNegativeParserToken with(final List<ParserToken> value,
                                                final String text) {
        return new NodeSelectorNegativeParserToken(copyAndCheckTokens(value),
                checkTextNullOrWhitespace(text));
    }

    private NodeSelectorNegativeParserToken(final List<ParserToken> value,
                                            final String text) {
        super(value, text);

        final List<ParserToken> without = ParentParserToken.filterWithoutNoise(value);
        final int count = without.size();
        if (1 != count) {
            throw new IllegalArgumentException("Expected 1 token but got " + count + "=" + without);
        }

        this.parameter = without.get(0).cast(NodeSelectorParserToken.class);
    }

    public NodeSelectorParserToken parameter() {
        return this.parameter;
    }

    private final NodeSelectorParserToken parameter;

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
        return other instanceof NodeSelectorNegativeParserToken;
    }
}
