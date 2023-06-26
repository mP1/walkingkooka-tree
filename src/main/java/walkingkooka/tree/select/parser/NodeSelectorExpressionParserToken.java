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
 * A parser token that holds an entire expression.
 */
public final class NodeSelectorExpressionParserToken extends NodeSelectorParentParserToken<NodeSelectorExpressionParserToken> {

    static NodeSelectorExpressionParserToken with(final List<ParserToken> value,
                                                  final String text) {
        return new NodeSelectorExpressionParserToken(copyAndCheckTokens(value),
                checkTextNullOrWhitespace(text));
    }

    private NodeSelectorExpressionParserToken(final List<ParserToken> value,
                                              final String text) {
        super(value, text);
    }

    // children.........................................................................................................

    @Override
    public NodeSelectorExpressionParserToken setChildren(final List<ParserToken> children) {
        return ParserToken.parentSetChildren(
                this,
                children,
                NodeSelectorExpressionParserToken::new
        );
    }

    // removeFirstIf....................................................................................................

    @Override
    public NodeSelectorExpressionParserToken removeFirstIf(final Predicate<ParserToken> predicate) {
        return ParserToken.parentRemoveFirstIf(
                this,
                predicate,
                NodeSelectorExpressionParserToken.class
        );
    }

    // removeIf.........................................................................................................

    @Override
    public NodeSelectorExpressionParserToken removeIf(final Predicate<ParserToken> predicate) {
        return ParserToken.parentRemoveIf(
                this,
                predicate,
                NodeSelectorExpressionParserToken.class
        );
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorExpressionParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                            final ParserToken token) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                token,
                NodeSelectorExpressionParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorExpressionParserToken replaceIf(final Predicate<ParserToken> predicate,
                                                       final ParserToken token) {
        return ParserToken.replaceIf(
                this,
                predicate,
                token,
                NodeSelectorExpressionParserToken.class
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
        return other instanceof NodeSelectorExpressionParserToken;
    }
}
