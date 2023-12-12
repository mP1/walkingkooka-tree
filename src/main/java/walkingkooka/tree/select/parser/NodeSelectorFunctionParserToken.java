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
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A parser token that holds an entire namedFunction.
 */
public final class NodeSelectorFunctionParserToken extends NodeSelectorParentParserToken<NodeSelectorFunctionParserToken> {

    static NodeSelectorFunctionParserToken with(final List<ParserToken> value,
                                                final String text) {
        return new NodeSelectorFunctionParserToken(copyAndCheckTokens(value),
                checkTextNullOrWhitespace(text));
    }

    private NodeSelectorFunctionParserToken(final List<ParserToken> value,
                                            final String text) {
        super(value, text);

        final List<ParserToken> without = ParserToken.filterWithoutNoise(value);
        final int count = without.size();
        if (count < 1) {
            throw new IllegalArgumentException("Expected at least 1 tokens but got " + count + "=" + without);
        }
        final NodeSelectorParserToken name = without.get(0).cast(NodeSelectorParserToken.class);
        if (!name.isFunctionName()) {
            throw new IllegalArgumentException("Function name missing from " + value);
        }

        this.functionName = ((NodeSelectorFunctionNameParserToken) name).value();
        this.parameters = without.subList(1, without.size());
    }

    public NodeSelectorFunctionName functionName() {
        return this.functionName;
    }

    private final NodeSelectorFunctionName functionName;

    public List<ParserToken> parameters() {
        return this.parameters;
    }

    private final List<ParserToken> parameters;

    // children.........................................................................................................

    @Override
    public NodeSelectorFunctionParserToken setChildren(final List<ParserToken> children) {
        return ParserToken.parentSetChildren(
                this,
                children,
                NodeSelectorFunctionParserToken::with
        );
    }

    // replaceFirstIf...................................................................................................

    @Override
    public NodeSelectorFunctionParserToken replaceFirstIf(final Predicate<ParserToken> predicate,
                                                          final Function<ParserToken, ParserToken> mapper) {
        return ParserToken.replaceFirstIf(
                this,
                predicate,
                mapper,
                NodeSelectorFunctionParserToken.class
        );
    }

    // replaceIf........................................................................................................

    @Override
    public NodeSelectorFunctionParserToken replaceIf(final Predicate<ParserToken> predicate,
                                                     final Function<ParserToken, ParserToken> mapper) {
        return ParserToken.replaceIf(
                this,
                predicate,
                mapper,
                NodeSelectorFunctionParserToken.class
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
        return other instanceof NodeSelectorFunctionParserToken;
    }
}
