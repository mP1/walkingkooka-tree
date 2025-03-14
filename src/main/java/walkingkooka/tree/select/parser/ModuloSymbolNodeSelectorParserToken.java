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

/**
 * Represents a MOD symbol token.
 */
public final class ModuloSymbolNodeSelectorParserToken extends SymbolNodeSelectorParserToken {

    static ModuloSymbolNodeSelectorParserToken with(final String value, final String text) {
        checkValue(value);
        checkTextNullOrWhitespace(text);

        return new ModuloSymbolNodeSelectorParserToken(value, text);
    }

    private ModuloSymbolNodeSelectorParserToken(final String value, final String text) {
        super(value, text);
    }

    // operator priority..................................................................................................

    @Override
    int operatorPriority() {
        return MOD_PRIORITY;
    }

    @Override
    BinaryNodeSelectorParserToken<?> binaryOperand(final List<ParserToken> tokens, final String text) {
        return NodeSelectorParserToken.modulo(tokens, text);
    }

    // Visitor................................................................................................

    @Override
    public void accept(final NodeSelectorParserTokenVisitor visitor) {
        visitor.visit(this);
    }
}
