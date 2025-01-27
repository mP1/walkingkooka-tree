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

public final class ModuloNodeSelectorParserTokenTest extends BinaryNodeSelectorParserTokenTestCase<ModuloNodeSelectorParserToken> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final ModuloNodeSelectorParserToken modulo = this.createToken();

        final NodeNameNodeSelectorParserToken nodeName = modulo.value().get(0).cast(NodeNameNodeSelectorParserToken.class);
        final ModuloSymbolNodeSelectorParserToken moduloSymbol = modulo.value().get(1).cast(ModuloSymbolNodeSelectorParserToken.class);
        final WildcardNodeSelectorParserToken wildcard = modulo.value().get(2).cast(WildcardNodeSelectorParserToken.class);

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
            protected Visiting startVisit(final ModuloNodeSelectorParserToken t) {
                assertSame(modulo, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ModuloNodeSelectorParserToken t) {
                assertSame(modulo, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeNameNodeSelectorParserToken t) {
                assertSame(nodeName, t);
                b.append("5");
                visited.add(t);
            }

            @Override
            protected void visit(final ModuloSymbolNodeSelectorParserToken t) {
                assertSame(moduloSymbol, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final WildcardNodeSelectorParserToken t) {
                assertSame(wildcard, t);
                b.append("7");
                visited.add(t);
            }

        }.accept(modulo);
        this.checkEquals("1315216217242", b.toString());
        this.checkEquals(Lists.<Object>of(modulo, modulo,
                nodeName, nodeName, nodeName,
                moduloSymbol, moduloSymbol, moduloSymbol,
                wildcard, wildcard, wildcard,
                modulo, modulo),
            visited,
            "visited");
    }

    @Override
    ModuloNodeSelectorParserToken createToken(final String text, final List<ParserToken> tokens) {
        return ModuloNodeSelectorParserToken.with(tokens, text);
    }

    @Override
    NodeSelectorParserToken operatorSymbol() {
        return moduloSymbol();
    }

    @Override
    public Class<ModuloNodeSelectorParserToken> type() {
        return ModuloNodeSelectorParserToken.class;
    }
}
