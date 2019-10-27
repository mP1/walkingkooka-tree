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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class NodeSelectorNegativeParserTokenTest extends NodeSelectorParentParserTokenTestCase<NodeSelectorNegativeParserToken> {

    // -123

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final NodeSelectorNegativeParserToken negative = this.createToken();
        final NodeSelectorMinusSymbolParserToken minus = negative.value().get(0).cast(NodeSelectorMinusSymbolParserToken.class);
        final NodeSelectorNumberParserToken number = negative.value().get(1).cast(NodeSelectorNumberParserToken.class);

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
            protected Visiting startVisit(final NodeSelectorNegativeParserToken t) {
                assertSame(negative, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodeSelectorNegativeParserToken t) {
                assertSame(negative, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorMinusSymbolParserToken t) {
                assertSame(minus, t);
                b.append("5");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorNumberParserToken t) {
                assertSame(number, t);
                b.append("6");
                visited.add(t);
            }
        }.accept(negative);

        assertEquals("1315216242", b.toString());
        assertEquals(Lists.<Object>of(negative, negative,
                minus, minus, minus,
                number, number, number,
                negative, negative),
                visited,
                "visited");
    }

    @Override
    NodeSelectorNegativeParserToken createToken(final String text, final List<ParserToken> tokens) {
        return NodeSelectorNegativeParserToken.with(tokens, text);
    }

    @Override
    public String text() {
        return "-1.0";
    }

    @Override
    List<ParserToken> tokens() {
        return Lists.of(minusSymbol(), number());
    }

    @Override
    public NodeSelectorNegativeParserToken createDifferentToken() {
        return negative(minusSymbol(), number2());
    }

    @Override
    public Class<NodeSelectorNegativeParserToken> type() {
        return NodeSelectorNegativeParserToken.class;
    }
}
