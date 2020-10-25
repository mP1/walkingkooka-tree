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

public final class GreaterThanExpressionTest extends BinaryComparisonExpressionTestCase<GreaterThanExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final GreaterThanExpression gt = this.createExpression();
        final Expression text1 = gt.children().get(0);
        final Expression text2 = gt.children().get(1);

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
            protected Visiting startVisit(final GreaterThanExpression t) {
                assertSame(gt, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final GreaterThanExpression t) {
                assertSame(gt, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final StringExpression t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(gt);
        assertEquals("1315215242", b.toString());
        assertEquals(Lists.of(gt, gt,
                text1, text1, text1,
                text2, text2, text2,
                gt, gt),
                visited,
                "visited");
    }

    // Number ..........................................................................................................

    @Test
    public void testEvaluateToBooleanNumberNumber() {
        // left gt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(expressionNumber(12), expressionNumber(34)), false);
    }

    @Test
    public void testEvaluateToBooleanNumberNumber2() {
        // left gt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(expressionNumber(12), expressionNumber(12)), false);
    }

    @Test
    public void testEvaluateToBooleanNumberNumber3() {
        // left gt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(expressionNumber(12), expressionNumber(-99)), true);
    }

    // Text............................................................................................................

    @Test
    public void testEvaluateToBooleanTextNumber() {
        // left gt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(text(12), expressionNumber(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextNumber2() {
        // left gt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(text(12), expressionNumber(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextNumber3() {
        // left gt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(text(12), expressionNumber(-99)), true);
    }

    @Test
    public void testEvaluateToBooleanTextText() {
        // left gt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(text(12), text(34)), false);
    }

    @Test
    public void testEvaluateToBooleanTextText2() {
        // left gt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(text(12), text(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextText3() {
        // left gt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(text(12), text(-99)), true);
    }

    @Override
    GreaterThanExpression createExpression(final Expression left, final Expression right) {
        return GreaterThanExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + ">" + RIGHT_TO_STRING;
    }

    @Override
    Class<GreaterThanExpression> expressionType() {
        return GreaterThanExpression.class;
    }
}
