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
 * Holds an modulo operation
 */
public final class NodeSelectorModuloParserToken extends NodeSelectorBinaryParserToken<NodeSelectorModuloParserToken> {

    static NodeSelectorModuloParserToken with(final List<ParserToken> value,
                                              final String text) {
        return new NodeSelectorModuloParserToken(copyAndCheckTokens(value),
                checkTextNullOrWhitespace(text));
    }

    private NodeSelectorModuloParserToken(final List<ParserToken> value,
                                          final String text) {
        super(value, text);
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
        return other instanceof NodeSelectorModuloParserToken;
    }
}
