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

public final class PredicateNodeSelectorParserTokenTest extends ParentNodeSelectorParserTokenTestCase<PredicateNodeSelectorParserToken> {

    // [ends-with(@href, '/')]

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final PredicateNodeSelectorParserToken predicate = this.createToken();
        final FunctionNodeSelectorParserToken function = predicate.value().get(0).cast(FunctionNodeSelectorParserToken.class);

        final FunctionNameNodeSelectorParserToken functionName = function.value().get(0).cast(FunctionNameNodeSelectorParserToken.class);
        final ParenthesisOpenSymbolNodeSelectorParserToken parenOpen = function.value().get(1).cast(ParenthesisOpenSymbolNodeSelectorParserToken.class);
        final AtSignSymbolNodeSelectorParserToken atSign = function.value().get(2).cast(AtSignSymbolNodeSelectorParserToken.class);
        final AttributeNameNodeSelectorParserToken attributeName = function.value().get(3).cast(AttributeNameNodeSelectorParserToken.class);
        final ParameterSeparatorSymbolNodeSelectorParserToken comma = function.value().get(4).cast(ParameterSeparatorSymbolNodeSelectorParserToken.class);
        final QuotedTextNodeSelectorParserToken quotedText = function.value().get(5).cast(QuotedTextNodeSelectorParserToken.class);
        final ParenthesisCloseSymbolNodeSelectorParserToken parenClose = function.value().get(6).cast(ParenthesisCloseSymbolNodeSelectorParserToken.class);

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
            protected Visiting startVisit(final PredicateNodeSelectorParserToken t) {
                assertSame(predicate, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final PredicateNodeSelectorParserToken t) {
                assertSame(predicate, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected Visiting startVisit(final FunctionNodeSelectorParserToken t) {
                assertSame(function, t);
                b.append("5");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final FunctionNodeSelectorParserToken t) {
                assertSame(function, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final FunctionNameNodeSelectorParserToken t) {
                assertSame(functionName, t);
                b.append("7");
                visited.add(t);
            }

            @Override
            protected void visit(final ParenthesisOpenSymbolNodeSelectorParserToken t) {
                assertSame(parenOpen, t);
                b.append("8");
                visited.add(t);
            }

            @Override
            protected void visit(final AtSignSymbolNodeSelectorParserToken t) {
                assertSame(atSign, t);
                b.append("9");
                visited.add(t);
            }

            @Override
            protected void visit(final AttributeNameNodeSelectorParserToken t) {
                assertSame(attributeName, t);
                b.append("A");
                visited.add(t);
            }

            @Override
            protected void visit(final ParameterSeparatorSymbolNodeSelectorParserToken t) {
                assertSame(comma, t);
                b.append("B");
                visited.add(t);
            }

            @Override
            protected void visit(final QuotedTextNodeSelectorParserToken t) {
                assertSame(quotedText, t);
                b.append("C");
                visited.add(t);
            }

            @Override
            protected void visit(final ParenthesisCloseSymbolNodeSelectorParserToken t) {
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
    PredicateNodeSelectorParserToken createToken(final String text, final List<ParserToken> tokens) {
        return PredicateNodeSelectorParserToken.with(tokens, text);
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
    public PredicateNodeSelectorParserToken createDifferentToken() {
        return predicate(function(functionName(), parenthesisOpen(), attributeName(), comma(), quotedText("different"), parenthesisClose()));
    }

    @Override
    public Class<PredicateNodeSelectorParserToken> type() {
        return PredicateNodeSelectorParserToken.class;
    }
}
