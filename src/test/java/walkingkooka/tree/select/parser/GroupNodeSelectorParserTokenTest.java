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

public final class GroupNodeSelectorParserTokenTest extends ParentNodeSelectorParserTokenTestCase<GroupNodeSelectorParserToken> {

    // (1)

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final GroupNodeSelectorParserToken expression = this.createToken();

        final ParenthesisOpenSymbolNodeSelectorParserToken open = expression.value().get(0).cast(ParenthesisOpenSymbolNodeSelectorParserToken.class);
        final ExpressionNumberNodeSelectorParserToken number = expression.value().get(1).cast(ExpressionNumberNodeSelectorParserToken.class);
        final ParenthesisCloseSymbolNodeSelectorParserToken close = expression.value().get(2).cast(ParenthesisCloseSymbolNodeSelectorParserToken.class);

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
            protected Visiting startVisit(final GroupNodeSelectorParserToken t) {
                assertSame(expression, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final GroupNodeSelectorParserToken t) {
                assertSame(expression, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ParenthesisOpenSymbolNodeSelectorParserToken t) {
                assertSame(open, t);
                b.append("5");
                visited.add(t);
            }

            @Override
            protected void visit(final ExpressionNumberNodeSelectorParserToken t) {
                assertSame(number, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final ParenthesisCloseSymbolNodeSelectorParserToken t) {
                assertSame(close, t);
                b.append("7");
                visited.add(t);
            }
        }.accept(expression);
        this.checkEquals("1315216217242", b.toString());
        this.checkEquals(Lists.of(expression, expression,
                open, open, open, number, number, number, close, close, close,
                expression, expression),
            visited,
            "visited");
    }

    @Override
    GroupNodeSelectorParserToken createToken(final String text, final List<ParserToken> tokens) {
        return GroupNodeSelectorParserToken.with(tokens, text);
    }

    @Override
    public String text() {
        return "(1.0)";
    }

    @Override
    List<ParserToken> tokens() {
        return Lists.of(parenthesisOpen(), number(), parenthesisClose());
    }

    @Override
    public GroupNodeSelectorParserToken createDifferentToken() {
        return group(parenthesisOpen(), this.number(), this.plusSymbol(), this.number2(), parenthesisClose());
    }

    @Override
    public Class<GroupNodeSelectorParserToken> type() {
        return GroupNodeSelectorParserToken.class;
    }
}
