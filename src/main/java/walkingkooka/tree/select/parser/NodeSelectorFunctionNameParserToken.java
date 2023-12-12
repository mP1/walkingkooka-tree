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

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Holds a namedFunction name.
 */
public final class NodeSelectorFunctionNameParserToken extends NodeSelectorNonSymbolParserToken<NodeSelectorFunctionName> {

    static NodeSelectorFunctionNameParserToken with(final NodeSelectorFunctionName value, final String text) {
        checkValue(value);
        checkTextNullOrWhitespace(text);

        return new NodeSelectorFunctionNameParserToken(value, text);
    }

    private NodeSelectorFunctionNameParserToken(final NodeSelectorFunctionName value, final String text) {
        super(value, text);
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorFunctionNameParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                              final Function<ParserToken, ParserToken> mapper) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                mapper,
                NodeSelectorFunctionNameParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorFunctionNameParserToken replaceIf(final Predicate<ParserToken> predicate,
                                                         final Function<ParserToken, ParserToken> mapper) {
        return ParserToken.replaceIf(
                this,
                predicate,
                mapper,
                NodeSelectorFunctionNameParserToken.class
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
        return other instanceof NodeSelectorFunctionNameParserToken;
    }
}
