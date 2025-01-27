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
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.function.Predicate;

/**
 * A predicate parser token.
 */
public final class PredicateNodeSelectorParserToken extends ParentNodeSelectorParserToken<PredicateNodeSelectorParserToken> {

    static PredicateNodeSelectorParserToken with(final List<ParserToken> value,
                                                 final String text) {
        return new PredicateNodeSelectorParserToken(copyAndCheckTokens(value),
            checkTextNullOrWhitespace(text));
    }

    private PredicateNodeSelectorParserToken(final List<ParserToken> value,
                                             final String text) {
        super(value, text);
    }

    /**
     * Converts this token into an {@link Expression}
     */
    public Expression toExpression(final Predicate<ExpressionFunctionName> functions) {
        return PredicateNodeSelectorParserTokenNodeSelectorParserTokenVisitor.toExpression(this, functions);
    }

    // children.........................................................................................................

    @Override
    public PredicateNodeSelectorParserToken setChildren(final List<ParserToken> children) {
        return ParserToken.parentSetChildren(
            this,
            children,
            PredicateNodeSelectorParserToken::with
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
