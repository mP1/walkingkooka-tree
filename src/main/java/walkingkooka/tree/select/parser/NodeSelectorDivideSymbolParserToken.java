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
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a DIV symbol token.
 */
public final class NodeSelectorDivideSymbolParserToken extends NodeSelectorSymbolParserToken {

    static NodeSelectorDivideSymbolParserToken with(final String value, final String text) {
        checkValue(value);
        checkTextNullOrWhitespace(text);

        return new NodeSelectorDivideSymbolParserToken(value, text);
    }

    private NodeSelectorDivideSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    // operator priority..................................................................................................

    @Override
    int operatorPriority() {
        return MULTIPLY_DIVISION_PRIORITY;
    }

    @Override
    NodeSelectorBinaryParserToken<?> binaryOperand(final List<ParserToken> tokens, final String text) {
        return division(tokens, text);
    }

    // removeFirstIf....................................................................................................

    @Override
    public Optional<NodeSelectorDivideSymbolParserToken> removeFirstIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeFirstIfLeaf(
                this,
                predicate,
                NodeSelectorDivideSymbolParserToken.class
        );
    }

    // removeIf.........................................................................................................

    @Override
    public Optional<NodeSelectorDivideSymbolParserToken> removeIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeIfLeaf(
                this,
                predicate,
                NodeSelectorDivideSymbolParserToken.class
        );
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorDivideSymbolParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                              final Function<ParserToken, ParserToken> mapper) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                mapper,
                NodeSelectorDivideSymbolParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorDivideSymbolParserToken replaceIf(final Predicate<ParserToken> predicate,
                                                         final Function<ParserToken, ParserToken> mapper) {
        return ParserToken.replaceIf(
                this,
                predicate,
                mapper,
                NodeSelectorDivideSymbolParserToken.class
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
        return other instanceof NodeSelectorDivideSymbolParserToken;
    }
}
