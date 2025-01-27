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

public final class ExpressionNodeSelectorParserTokenTest extends ParentNodeSelectorParserTokenTestCase<ExpressionNodeSelectorParserToken> {

    // [ends-with(@href, '/')]

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final ExpressionNodeSelectorParserToken expression = this.createToken();

        final WildcardNodeSelectorParserToken wildcard = expression.value().get(0).cast(WildcardNodeSelectorParserToken.class);

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
            protected Visiting startVisit(final ExpressionNodeSelectorParserToken t) {
                assertSame(expression, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionNodeSelectorParserToken t) {
                assertSame(expression, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final WildcardNodeSelectorParserToken t) {
                assertSame(wildcard, t);
                b.append("5");
                visited.add(t);
            }
        }.accept(expression);
        this.checkEquals("1315242", b.toString());
        this.checkEquals(Lists.of(expression, expression,
                wildcard, wildcard, wildcard,
                expression, expression),
            visited,
            "visited");
    }

    @Override
    ExpressionNodeSelectorParserToken createToken(final String text, final List<ParserToken> tokens) {
        return ExpressionNodeSelectorParserToken.with(tokens, text);
    }

    @Override
    public String text() {
        return "*";
    }

    @Override
    List<ParserToken> tokens() {
        return Lists.of(wildcard());
    }

    @Override
    public ExpressionNodeSelectorParserToken createDifferentToken() {
        return expression(descendantOrSelfSlashSlash());
    }

    @Override
    public Class<ExpressionNodeSelectorParserToken> type() {
        return ExpressionNodeSelectorParserToken.class;
    }
}
