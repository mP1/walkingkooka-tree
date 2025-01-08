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

public final class NodeSelectorPredicateParserTokenTest extends NodeSelectorParentParserTokenTestCase<NodeSelectorPredicateParserToken> {

    // [ends-with(@href, '/')]

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final NodeSelectorPredicateParserToken predicate = this.createToken();
        final NodeSelectorFunctionParserToken function = predicate.value().get(0).cast(NodeSelectorFunctionParserToken.class);

        final NodeSelectorFunctionNameParserToken functionName = function.value().get(0).cast(NodeSelectorFunctionNameParserToken.class);
        final NodeSelectorParenthesisOpenSymbolParserToken parenOpen = function.value().get(1).cast(NodeSelectorParenthesisOpenSymbolParserToken.class);
        final NodeSelectorAtSignSymbolParserToken atSign = function.value().get(2).cast(NodeSelectorAtSignSymbolParserToken.class);
        final NodeSelectorAttributeNameParserToken attributeName = function.value().get(3).cast(NodeSelectorAttributeNameParserToken.class);
        final NodeSelectorParameterSeparatorSymbolParserToken comma = function.value().get(4).cast(NodeSelectorParameterSeparatorSymbolParserToken.class);
        final NodeSelectorQuotedTextParserToken quotedText = function.value().get(5).cast(NodeSelectorQuotedTextParserToken.class);
        final NodeSelectorParenthesisCloseSymbolParserToken parenClose = function.value().get(6).cast(NodeSelectorParenthesisCloseSymbolParserToken.class);

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
            protected Visiting startVisit(final NodeSelectorPredicateParserToken t) {
                assertSame(predicate, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodeSelectorPredicateParserToken t) {
                assertSame(predicate, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected Visiting startVisit(final NodeSelectorFunctionParserToken t) {
                assertSame(function, t);
                b.append("5");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodeSelectorFunctionParserToken t) {
                assertSame(function, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorFunctionNameParserToken t) {
                assertSame(functionName, t);
                b.append("7");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorParenthesisOpenSymbolParserToken t) {
                assertSame(parenOpen, t);
                b.append("8");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorAtSignSymbolParserToken t) {
                assertSame(atSign, t);
                b.append("9");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorAttributeNameParserToken t) {
                assertSame(attributeName, t);
                b.append("A");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorParameterSeparatorSymbolParserToken t) {
                assertSame(comma, t);
                b.append("B");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorQuotedTextParserToken t) {
                assertSame(quotedText, t);
                b.append("C");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorParenthesisCloseSymbolParserToken t) {
                assertSame(parenClose, t);
                b.append("D");
                visited.add(t);
            }
        }.accept(predicate);
        this.checkEquals("13151721821921A21B21C21D26242", b.toString());
        this.checkEquals(Lists.<Object>of(predicate, predicate,
                function, function,
                functionName, functionName, functionName,
                parenOpen, parenOpen, parenOpen,
                atSign, atSign, atSign,
                attributeName, attributeName, attributeName,
                comma, comma, comma,
                quotedText, quotedText, quotedText,
                parenClose, parenClose, parenClose,
                function, function,
                predicate, predicate),
            visited,
            "visited");
    }

    @Override
    NodeSelectorPredicateParserToken createToken(final String text, final List<ParserToken> tokens) {
        return NodeSelectorPredicateParserToken.with(tokens, text);
    }

    @Override
    public String text() {
        return "contains(@attribute1,\"xyz\")";
    }

    @Override
    List<ParserToken> tokens() {
        return Lists.of(function(functionName(), parenthesisOpen(), atSign(), attributeName(), comma(), quotedText(), parenthesisClose()));
    }

    @Override
    public NodeSelectorPredicateParserToken createDifferentToken() {
        return predicate(function(functionName(), parenthesisOpen(), attributeName(), comma(), quotedText("different"), parenthesisClose()));
    }

    @Override
    public Class<NodeSelectorPredicateParserToken> type() {
        return NodeSelectorPredicateParserToken.class;
    }
}
