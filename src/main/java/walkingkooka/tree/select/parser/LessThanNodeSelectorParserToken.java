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
 * Parser token that represents an less than condition including parameters.
 */
public final class LessThanNodeSelectorParserToken extends BinaryNodeSelectorParserToken<LessThanNodeSelectorParserToken> {

    static LessThanNodeSelectorParserToken with(final List<ParserToken> value,
                                                final String text) {
        return new LessThanNodeSelectorParserToken(copyAndCheckTokens(value),
            checkTextNullOrWhitespace(text));
    }

    private LessThanNodeSelectorParserToken(final List<ParserToken> value,
                                            final String text) {
        super(value, text);
    }

    // children.........................................................................................................

    @Override
    public LessThanNodeSelectorParserToken setChildren(final List<ParserToken> children) {
        return ParserToken.parentSetChildren(
            this,
            children,
            LessThanNodeSelectorParserToken::with
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
