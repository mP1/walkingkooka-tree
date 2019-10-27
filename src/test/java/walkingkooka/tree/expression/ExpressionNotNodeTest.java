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

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNotNodeTest extends ExpressionUnaryNodeTestCase<ExpressionNotNode> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ExpressionNode> visited = Lists.array();

        final ExpressionNotNode not = this.createExpressionNode();
        final ExpressionNode child = not.children().get(0);

        new FakeExpressionNodeVisitor() {
            @Override
            protected Visiting startVisit(final ExpressionNode n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionNode n) {
                b.append("2");
                visited.add(n);
            }

            @Override
            protected Visiting startVisit(final ExpressionNotNode t) {
                assertSame(not, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionNotNode t) {
                assertSame(not, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ExpressionTextNode t) {
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
    public void testEvaluateToBigDecimal() {
        final long value = 123;
        this.evaluateAndCheckBigInteger(this.createExpressionNode(bigDecimal(value)), BigInteger.valueOf(value).not());
    }

    @Test
    public void testEvaluateToBigDecimalWithDecimalsFails() {
        assertThrows(ExpressionEvaluationConversionException.class, () -> this.createExpressionNode(bigDecimal(1.5)).toNumber(context()));
    }

    @Test
    public void testEvaluateToBigInteger() {
        final BigInteger value = BigInteger.valueOf(123);
        this.evaluateAndCheckBigInteger(this.createExpressionNode(ExpressionNode.bigInteger(value)), value.not());
    }

    @Test
    public void testEvaluateToDouble() {
        final long value = 123;
        this.evaluateAndCheckLong(this.createExpressionNode(ExpressionNode.doubleNode(value)), ~value);
    }

    @Test
    public void testEvaluateToDoubleWithDecimalsFails() {
        assertThrows(ExpressionEvaluationConversionException.class, () -> this.createExpressionNode(doubleValue(1.5)).toNumber(context()));
    }

    @Test
    public void testEvaluateToLong() {
        final long value = 123L;
        this.evaluateAndCheckLong(this.createExpressionNode(ExpressionNode.longNode(value)), ~value);
    }

    @Test
    public void testEvaluateToNumberBigDecimal() {
        final long value = 123;
        this.evaluateAndCheckNumberBigInteger(this.createExpressionNode(bigDecimal(value)), BigInteger.valueOf(value).not());
    }

    @Test
    public void testEvaluateToNumberBigInteger() {
        final BigInteger value = BigInteger.valueOf(123);
        this.evaluateAndCheckNumberBigInteger(this.createExpressionNode(ExpressionNode.bigInteger(value)), value.not());
    }

    @Test
    public void testEvaluateToNumberDouble() {
        final long value = 123;
        this.evaluateAndCheckLong(this.createExpressionNode(ExpressionNode.doubleNode(value)), ~value);
    }

    @Test
    public void testEvaluateToNumberLong() {
        final long value = 123L;
        this.evaluateAndCheckNumberLong(this.createExpressionNode(ExpressionNode.longNode(value)), ~value);
    }

    @Override
    ExpressionNotNode createExpressionNode(final ExpressionNode child) {
        return ExpressionNotNode.with(child);
    }

    @Override
    String expectedToString() {
        return "!" + CHILD_TO_STRING;
    }

    @Override
    Class<ExpressionNotNode> expressionNodeType() {
        return ExpressionNotNode.class;
    }
}
