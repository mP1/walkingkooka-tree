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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ExpressionNegativeNodeTest extends ExpressionUnaryNodeTestCase<ExpressionNegativeNode> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ExpressionNode> visited = Lists.array();

        final ExpressionNegativeNode negative = this.createExpressionNode();
        final ExpressionNode child = negative.children().get(0);

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
            protected Visiting startVisit(final ExpressionNegativeNode t) {
                assertSame(negative, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionNegativeNode t) {
                assertSame(negative, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ExpressionTextNode t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(negative);
        assertEquals("1315242", b.toString());
        assertEquals(Lists.of(negative, negative,
                child, child, child,
                negative, negative),
                visited,
                "visited");
    }

    // evaluate.....................................................................................

    @Test
    public void testEvaluateToBigDecimal() {
        final BigDecimal value = BigDecimal.valueOf(123);
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(ExpressionNode.bigDecimal(value)), value.negate());
    }

    @Test
    public void testEvaluateToBigInteger() {
        final BigInteger value = BigInteger.valueOf(123);
        this.evaluateAndCheckBigInteger(this.createExpressionNode(ExpressionNode.bigInteger(value)), value.negate());
    }

    @Test
    public void testEvaluateToDouble() {
        final double value = 123;
        this.evaluateAndCheckDouble(this.createExpressionNode(ExpressionNode.doubleNode(value)), -value);
    }

    @Test
    public void testEvaluateToLong() {
        final long value = 123L;
        this.evaluateAndCheckLong(this.createExpressionNode(ExpressionNode.longNode(value)), -value);
    }

    @Test
    public void testEvaluateToNumberBigDecimal() {
        final BigDecimal value = BigDecimal.valueOf(123);
        this.evaluateAndCheckNumberBigDecimal(this.createExpressionNode(ExpressionNode.bigDecimal(value)), value.negate());
    }

    @Test
    public void testEvaluateToNumberBigInteger() {
        final BigInteger value = BigInteger.valueOf(123);
        this.evaluateAndCheckNumberBigInteger(this.createExpressionNode(ExpressionNode.bigInteger(value)), value.negate());
    }

    @Test
    public void testEvaluateToNumberDouble() {
        final double value = 123;
        this.evaluateAndCheckDouble(this.createExpressionNode(ExpressionNode.doubleNode(value)), -value);
    }

    @Test
    public void testEvaluateToNumberLong() {
        final long value = 123L;
        this.evaluateAndCheckNumberLong(this.createExpressionNode(ExpressionNode.longNode(value)), -value);
    }

    @Override
    ExpressionNegativeNode createExpressionNode(final ExpressionNode child) {
        return ExpressionNegativeNode.with(child);
    }

    @Override
    String expectedToString() {
        return "-" + CHILD_TO_STRING;
    }

    @Override
    Class<ExpressionNegativeNode> expressionNodeType() {
        return ExpressionNegativeNode.class;
    }
}
