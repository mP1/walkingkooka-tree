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

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Represents the separator between elements or key/value pairs belonging to arrays and objects.
 */
public final class NodeSelectorParameterSeparatorSymbolParserToken extends NodeSelectorNonBinaryOperandSymbolParserToken {

    static NodeSelectorParameterSeparatorSymbolParserToken with(final String value, final String text) {
        checkValue(value);
        checkTextNullOrWhitespace(text);

        return new NodeSelectorParameterSeparatorSymbolParserToken(value, text);
    }

    private NodeSelectorParameterSeparatorSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    // removeFirstIf....................................................................................................

    @Override
    public Optional<NodeSelectorParameterSeparatorSymbolParserToken> removeFirstIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeFirstIfLeaf(
                this,
                predicate,
                NodeSelectorParameterSeparatorSymbolParserToken.class
        );
    }

    // removeIf....................................................................................................

    @Override
    public Optional<NodeSelectorParameterSeparatorSymbolParserToken> removeIf(final Predicate<ParserToken> predicate) {
        return ParserToken.removeIfLeaf(
                this,
                predicate,
                NodeSelectorParameterSeparatorSymbolParserToken.class
        );
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorParameterSeparatorSymbolParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                                          final ParserToken token) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                token,
                NodeSelectorParameterSeparatorSymbolParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorParameterSeparatorSymbolParserToken replaceIf(final Predicate<ParserToken> predicate,
                                                                     final ParserToken token) {
        return ParserToken.replaceIf(
                this,
                predicate,
                token,
                NodeSelectorParameterSeparatorSymbolParserToken.class
        );
    }

    // Visitor.........................................................................................................

    @Override
    public void accept(final NodeSelectorParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodeSelectorParameterSeparatorSymbolParserToken;
    }
}
