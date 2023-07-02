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
import walkingkooka.tree.expression.ExpressionNumber;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Holds a single decimal number.
 */
public final class NodeSelectorExpressionNumberParserToken extends NodeSelectorNonSymbolParserToken<ExpressionNumber> {

    static NodeSelectorExpressionNumberParserToken with(final ExpressionNumber value, final String text) {
        checkTextNullOrWhitespace(text);

        return new NodeSelectorExpressionNumberParserToken(value, text);
    }

    private NodeSelectorExpressionNumberParserToken(final ExpressionNumber value, final String text) {
        super(value, text);
    }

    // removeFirstIf....................................................................................................

    @Override
    public Optional<NodeSelectorExpressionNumberParserToken> removeFirstIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeFirstIfLeaf(
                this,
                predicate,
                NodeSelectorExpressionNumberParserToken.class
        );
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorExpressionNumberParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                                  final ParserToken token) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                token,
                NodeSelectorExpressionNumberParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorExpressionNumberParserToken replaceIf(final Predicate<ParserToken> predicate,
                                                             final ParserToken token) {
        return ParserToken.replaceIf(
                this,
                predicate,
                token,
                NodeSelectorExpressionNumberParserToken.class
        );
    }

    // Visitor..........................................................................................................

    @Override
    public void accept(final NodeSelectorParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodeSelectorExpressionNumberParserToken;
    }
}
