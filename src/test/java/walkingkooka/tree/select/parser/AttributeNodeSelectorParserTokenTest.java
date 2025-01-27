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

public final class AttributeNodeSelectorParserTokenTest extends ParentNodeSelectorParserTokenTestCase<AttributeNodeSelectorParserToken> {

    // @id

    @Test
    public void testAttributeName() {
        this.checkAttributeName(this.createToken());
    }

    private void checkAttributeName(final AttributeNodeSelectorParserToken token) {
        this.checkEquals(attributeName().value(), token.attributeName(), "attributeName");
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final AttributeNodeSelectorParserToken attribute = this.createToken();
        final AtSignSymbolNodeSelectorParserToken atSign = attribute.value().get(0).cast(AtSignSymbolNodeSelectorParserToken.class);
        final AttributeNameNodeSelectorParserToken name = attribute.value().get(1).cast(AttributeNameNodeSelectorParserToken.class);

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
            protected Visiting startVisit(final AttributeNodeSelectorParserToken t) {
                assertSame(attribute, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final AttributeNodeSelectorParserToken t) {
                assertSame(attribute, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final AtSignSymbolNodeSelectorParserToken t) {
                assertSame(atSign, t);
                b.append("5");
                visited.add(t);
            }

            @Override
            protected void visit(final AttributeNameNodeSelectorParserToken t) {
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
    AttributeNodeSelectorParserToken createToken(final String text, final List<ParserToken> tokens) {
        return AttributeNodeSelectorParserToken.with(tokens, text);
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
    public AttributeNodeSelectorParserToken createDifferentToken() {
        return attribute(atSign(), attributeName2());
    }

    @Override
    public Class<AttributeNodeSelectorParserToken> type() {
        return AttributeNodeSelectorParserToken.class;
    }
}
