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

public final class NodeSelectorAttributeParserTokenTest extends NodeSelectorParentParserTokenTestCase<NodeSelectorAttributeParserToken> {

    // @id

    @Test
    public void testAttributeName() {
        this.checkAttributeName(this.createToken());
    }

    private void checkAttributeName(final NodeSelectorAttributeParserToken token) {
        this.checkEquals(attributeName().value(), token.attributeName(), "attributeName");
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final NodeSelectorAttributeParserToken attribute = this.createToken();
        final NodeSelectorAtSignSymbolParserToken atSign = attribute.value().get(0).cast(NodeSelectorAtSignSymbolParserToken.class);
        final NodeSelectorAttributeNameParserToken name = attribute.value().get(1).cast(NodeSelectorAttributeNameParserToken.class);

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
            protected Visiting startVisit(final NodeSelectorAttributeParserToken t) {
                assertSame(attribute, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodeSelectorAttributeParserToken t) {
                assertSame(attribute, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorAtSignSymbolParserToken t) {
                assertSame(atSign, t);
                b.append("5");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorAttributeNameParserToken t) {
                assertSame(name, t);
                b.append("6");
                visited.add(t);
            }
        }.accept(attribute);

        this.checkEquals("1315216242", b.toString());
        this.checkEquals(Lists.<Object>of(attribute, attribute,
                atSign, atSign, atSign,
                name, name, name,
                attribute, attribute),
            visited,
            "visited");
    }

    @Override
    NodeSelectorAttributeParserToken createToken(final String text, final List<ParserToken> tokens) {
        return NodeSelectorAttributeParserToken.with(tokens, text);
    }

    @Override
    public String text() {
        return "@" + attributeName();
    }

    @Override
    List<ParserToken> tokens() {
        return Lists.of(atSign(), attributeName());
    }

    @Override
    public NodeSelectorAttributeParserToken createDifferentToken() {
        return attribute(atSign(), attributeName2());
    }

    @Override
    public Class<NodeSelectorAttributeParserToken> type() {
        return NodeSelectorAttributeParserToken.class;
    }
}
