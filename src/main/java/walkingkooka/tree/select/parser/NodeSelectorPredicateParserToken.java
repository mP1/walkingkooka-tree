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
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.function.Predicate;

/**
 * A predicate parser token.
 */
public final class NodeSelectorPredicateParserToken extends NodeSelectorParentParserToken<NodeSelectorPredicateParserToken> {

    static NodeSelectorPredicateParserToken with(final List<ParserToken> value,
                                                 final String text) {
        return new NodeSelectorPredicateParserToken(copyAndCheckTokens(value),
                checkTextNullOrWhitespace(text));
    }

    private NodeSelectorPredicateParserToken(final List<ParserToken> value,
                                             final String text) {
        super(value, text);
    }

    /**
     * Converts this token into an {@link Expression}
     */
    public Expression toExpression(final Predicate<FunctionExpressionName> functions) {
        return NodeSelectorPredicateParserTokenNodeSelectorParserTokenVisitor.toExpression(this, functions);
    }

    // children.........................................................................................................

    @Override
    public NodeSelectorPredicateParserToken setChildren(final List<ParserToken> children) {
        return ParserToken.parentSetChildren(
                this,
                children,
                NodeSelectorPredicateParserToken::with
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
        return other instanceof NodeSelectorPredicateParserToken;
    }
}
