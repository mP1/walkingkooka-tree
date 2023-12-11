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
 * Represents and symbol token.
 */
public final class NodeSelectorAndSymbolParserToken extends NodeSelectorSymbolParserToken {

    static NodeSelectorAndSymbolParserToken with(final String value, final String text) {
        checkValue(value);
        checkTextNullOrWhitespace(text);

        return new NodeSelectorAndSymbolParserToken(value, text);
    }

    private NodeSelectorAndSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    // operator priority..................................................................................................

    @Override
    int operatorPriority() {
        return AND_PRIORITY;
    }

    @Override
    NodeSelectorBinaryParserToken<?> binaryOperand(final List<ParserToken> tokens, final String text) {
        return and(tokens, text);
    }

    // removeFirstIf....................................................................................................

    @Override
    public Optional<NodeSelectorAndSymbolParserToken> removeFirstIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeFirstIfLeaf(
                this,
                predicate,
                NodeSelectorAndSymbolParserToken.class
        );
    }

    // removeIf.........................................................................................................

    @Override
    public Optional<NodeSelectorAndSymbolParserToken> removeIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeIfLeaf(
                this,
                predicate,
                NodeSelectorAndSymbolParserToken.class
        );
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorAndSymbolParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                           final Function<ParserToken, ParserToken> mapper) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                mapper,
                NodeSelectorAndSymbolParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorAndSymbolParserToken replaceIf(final Predicate<ParserToken> predicate,
                                                      final Function<ParserToken, ParserToken> mapper) {
        return ParserToken.replaceIf(
                this,
                predicate,
                mapper,
                NodeSelectorAndSymbolParserToken.class
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
        return other instanceof NodeSelectorAndSymbolParserToken;
    }
}
