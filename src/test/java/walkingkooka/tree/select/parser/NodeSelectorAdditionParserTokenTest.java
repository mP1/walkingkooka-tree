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

public final class NodeSelectorAdditionParserTokenTest extends NodeSelectorBinaryParserTokenTestCase<NodeSelectorAdditionParserToken> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final NodeSelectorAdditionParserToken add = this.createToken();

        final NodeSelectorNodeNameParserToken nodeName = add.value().get(0).cast(NodeSelectorNodeNameParserToken.class);
        final NodeSelectorPlusSymbolParserToken addSymbol = add.value().get(1).cast(NodeSelectorPlusSymbolParserToken.class);
        final NodeSelectorWildcardParserToken wildcard = add.value().get(2).cast(NodeSelectorWildcardParserToken.class);

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
            protected Visiting startVisit(final NodeSelectorAdditionParserToken t) {
                assertSame(add, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodeSelectorAdditionParserToken t) {
                assertSame(add, t);
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
            protected void visit(final NodeSelectorPlusSymbolParserToken t) {
                assertSame(addSymbol, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorWildcardParserToken t) {
                assertSame(wildcard, t);
                b.append("7");
                visited.add(t);
            }

        }.accept(add);
        this.checkEquals("1315216217242", b.toString());
        this.checkEquals(Lists.<Object>of(add, add,
                        nodeName, nodeName, nodeName,
                        addSymbol, addSymbol, addSymbol,
                        wildcard, wildcard, wildcard,
                        add, add),
                visited,
                "visited");
    }

    @Override
    NodeSelectorAdditionParserToken createToken(final String text, final List<ParserToken> tokens) {
        return NodeSelectorAdditionParserToken.with(tokens, text);
    }

    @Override
    NodeSelectorParserToken operatorSymbol() {
        return plusSymbol();
    }

    @Override
    public Class<NodeSelectorAdditionParserToken> type() {
        return NodeSelectorAdditionParserToken.class;
    }
}
