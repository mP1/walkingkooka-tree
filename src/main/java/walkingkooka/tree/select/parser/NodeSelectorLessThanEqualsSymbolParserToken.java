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

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Represents a less than equals sign in a comparison parser token.
 */
public final class NodeSelectorLessThanEqualsSymbolParserToken extends NodeSelectorSymbolParserToken {

    static NodeSelectorLessThanEqualsSymbolParserToken with(final String value, final String text) {
        checkValue(value);
        checkTextNullOrWhitespace(text);

        return new NodeSelectorLessThanEqualsSymbolParserToken(value, text);
    }

    private NodeSelectorLessThanEqualsSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    // operator priority................................................................................................

    @Override
    int operatorPriority() {
        return LESS_GREATER_PRIORITY;
    }

    @Override
    NodeSelectorBinaryParserToken<?> binaryOperand(final List<ParserToken> tokens, final String text) {
        return lessThanEquals(tokens, text);
    }

    // removeFirstIf....................................................................................................

    @Override
    public Optional<NodeSelectorLessThanEqualsSymbolParserToken> removeFirstIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeFirstIfLeaf(
                this,
                predicate,
                NodeSelectorLessThanEqualsSymbolParserToken.class
        );
    }

    // removeIf.........................................................................................................

    @Override
    public Optional<NodeSelectorLessThanEqualsSymbolParserToken> removeIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeIfLeaf(
                this,
                predicate,
                NodeSelectorLessThanEqualsSymbolParserToken.class
        );
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorLessThanEqualsSymbolParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                                      final ParserToken token) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                token,
                NodeSelectorLessThanEqualsSymbolParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorLessThanEqualsSymbolParserToken replaceIf(final Predicate<ParserToken> predicate,
                                                                 final ParserToken token) {
        return ParserToken.replaceIf(
                this,
                predicate,
                token,
                NodeSelectorLessThanEqualsSymbolParserToken.class
        );
    }

    // Visitor................................................................................................

    @Override
    public void accept(final NodeSelectorParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodeSelectorLessThanEqualsSymbolParserToken;
    }
}
