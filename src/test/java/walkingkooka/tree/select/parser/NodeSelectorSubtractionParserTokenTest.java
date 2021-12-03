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

public final class NodeSelectorSubtractionParserTokenTest extends NodeSelectorBinaryParserTokenTestCase<NodeSelectorSubtractionParserToken> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final NodeSelectorSubtractionParserToken and = this.createToken();

        final NodeSelectorNodeNameParserToken nodeName = and.value().get(0).cast(NodeSelectorNodeNameParserToken.class);
        final NodeSelectorMinusSymbolParserToken minusSymbol = and.value().get(1).cast(NodeSelectorMinusSymbolParserToken.class);
        final NodeSelectorWildcardParserToken wildcard = and.value().get(2).cast(NodeSelectorWildcardParserToken.class);

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
            protected Visiting startVisit(final NodeSelectorSubtractionParserToken t) {
                assertSame(and, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodeSelectorSubtractionParserToken t) {
                assertSame(and, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorNodeNameParserToken t) {
                assertSame(nodeName, t);
                b.append("5");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorMinusSymbolParserToken t) {
                assertSame(minusSymbol, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorWildcardParserToken t) {
                assertSame(wildcard, t);
                b.append("7");
                visited.add(t);
            }

        }.accept(and);
        this.checkEquals("1315216217242", b.toString());
        this.checkEquals(Lists.<Object>of(and, and,
                        nodeName, nodeName, nodeName,
                        minusSymbol, minusSymbol, minusSymbol,
                        wildcard, wildcard, wildcard,
                        and, and),
                visited,
                "visited");
    }

    @Override
    NodeSelectorSubtractionParserToken createToken(final String text, final List<ParserToken> tokens) {
        return NodeSelectorSubtractionParserToken.with(tokens, text);
    }

    @Override
    NodeSelectorParserToken operatorSymbol() {
        return minusSymbol();
    }

    @Override
    public Class<NodeSelectorSubtractionParserToken> type() {
        return NodeSelectorSubtractionParserToken.class;
    }
}
