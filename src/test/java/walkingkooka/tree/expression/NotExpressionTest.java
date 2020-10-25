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

package walkingkooka.tree.expression;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NotExpressionTest extends UnaryExpressionTestCase<NotExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final NotExpression not = this.createExpression();
        final Expression child = not.children().get(0);

        new FakeExpressionVisitor() {
            @Override
            protected Visiting startVisit(final Expression n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Expression n) {
                b.append("2");
                visited.add(n);
            }

            @Override
            protected Visiting startVisit(final NotExpression t) {
                assertSame(not, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NotExpression t) {
                assertSame(not, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final StringExpression t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(not);
        assertEquals("1315242", b.toString());
        assertEquals(Lists.of(not, not,
                child, child, child,
                not, not),
                visited,
                "visited");
    }

    // evaluate.....................................................................................

    @Test
    public void testEvaluateToExpressionNumber() {
        final long value = 123;
        this.evaluateAndCheckExpressionNumber(this.createExpression(expressionNumber(value)), ~value);
    }

    @Test
    public void testEvaluateToExpressionNumberWithDecimalsFails() {
        assertThrows(ArithmeticException.class, () -> this.createExpression(expressionNumber(1.5)).toExpressionNumber(context()));
    }

    @Override
    NotExpression createExpression(final Expression child) {
        return NotExpression.with(child);
    }

    @Override
    String expectedToString() {
        return "!" + CHILD_TO_STRING;
    }

    @Override
    Class<NotExpression> expressionType() {
        return NotExpression.class;
    }
}
