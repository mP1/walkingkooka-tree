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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NodeSelectorFunctionParserTokenTest extends NodeSelectorParentParserTokenTestCase<NodeSelectorFunctionParserToken> {

    // [ends-with(@href, '/')]

    @Test
    public void testWithMissingFunctionNameFails() {
        assertThrows(IllegalArgumentException.class, () -> NodeSelectorFunctionParserToken.with(Lists.of(number()), number().text()));
    }

    @Test
    public void testWithNoParameters() {
        final NodeSelectorFunctionNameParserToken functionName = functionName();
        final List<ParserToken> tokens = Lists.of(functionName, parenthesisOpen(), parenthesisClose());

        final NodeSelectorFunctionParserToken function = NodeSelectorFunctionParserToken.with(tokens, ParserToken.text(tokens));
        this.check(function, functionName.value());
    }

    @Test
    public void testWithOneParameter() {
        final NodeSelectorFunctionParserToken function = this.createToken();
        this.check(function, functionName().value(), quotedText());
    }

    @Test
    public void testWithManyParameters() {
        final NodeSelectorFunctionNameParserToken functionName = functionName();
        final NodeSelectorParserToken parameter0 = number();
        final NodeSelectorParserToken parameter1 = number2();
        final NodeSelectorParserToken parameter2 = number(3);

        final List<ParserToken> tokens = Lists.of(functionName, parenthesisOpen(), parameter0, comma(), parameter1, comma(), parameter2, parenthesisClose());

        final NodeSelectorFunctionParserToken function = NodeSelectorFunctionParserToken.with(tokens, ParserToken.text(tokens));
        this.check(function, functionName.value(), parameter0, parameter1, parameter2);
    }

    private void check(final NodeSelectorFunctionParserToken function,
                       final NodeSelectorFunctionName functionName,
                       final ParserToken... parameters) {
        this.checkEquals(functionName, function.functionName(), "functionName");
        this.checkEquals(Lists.of(parameters), function.parameters(), "parameters");
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final NodeSelectorFunctionParserToken function = this.createToken();

        final NodeSelectorFunctionNameParserToken functionName = function.value().get(0).cast(NodeSelectorFunctionNameParserToken.class);
        final NodeSelectorParenthesisOpenSymbolParserToken parenOpen = function.value().get(1).cast(NodeSelectorParenthesisOpenSymbolParserToken.class);
        final NodeSelectorQuotedTextParserToken quotedText = function.value().get(2).cast(NodeSelectorQuotedTextParserToken.class);
        final NodeSelectorParenthesisCloseSymbolParserToken parenClose = function.value().get(3).cast(NodeSelectorParenthesisCloseSymbolParserToken.class);

        new FakeNodeSelectorParserTokenVisitor() {
            @Override
            protected Visiting startVisit(final NodeSelectorParserToken n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodeSelectorParserToken n) {
                b.append("2");
                visited.add(n);
            }

            @Override
            protected Visiting startVisit(final NodeSelectorFunctionParserToken t) {
                assertSame(function, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodeSelectorFunctionParserToken t) {
                assertSame(function, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorFunctionNameParserToken t) {
                assertSame(functionName, t);
                b.append("5");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorParenthesisOpenSymbolParserToken t) {
                assertSame(parenOpen, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorQuotedTextParserToken t) {
                assertSame(quotedText, t);
                b.append("7");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorParenthesisCloseSymbolParserToken t) {
                assertSame(parenClose, t);
                b.append("8");
                visited.add(t);
            }
        }.accept(function);
        this.checkEquals("1315216217218242", b.toString());
        this.checkEquals(Lists.<Object>of(function, function,
                functionName, functionName, functionName,
                parenOpen, parenOpen, parenOpen,
                quotedText, quotedText, quotedText,
                parenClose, parenClose, parenClose,
                function, function),
            visited,
            "visited");
    }

    @Override
    NodeSelectorFunctionParserToken createToken(final String text, final List<ParserToken> tokens) {
        return NodeSelectorFunctionParserToken.with(tokens, text);
    }

    @Override
    public String text() {
        return "contains(\"xyz\")";
    }

    @Override
    List<ParserToken> tokens() {
        return Lists.of(functionName(), parenthesisOpen(), quotedText(), parenthesisClose());
    }

    @Override
    public NodeSelectorFunctionParserToken createDifferentToken() {
        return function(functionName(), parenthesisOpen(), parenthesisClose());
    }

    @Override
    public Class<NodeSelectorFunctionParserToken> type() {
        return NodeSelectorFunctionParserToken.class;
    }
}
