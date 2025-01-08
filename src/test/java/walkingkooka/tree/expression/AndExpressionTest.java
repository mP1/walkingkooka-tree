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

import static org.junit.jupiter.api.Assertions.assertSame;

public final class AndExpressionTest extends LogicalExpressionTestCase<AndExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final AndExpression and = this.createExpression();
        final Expression text1 = and.children().get(0);
        final Expression text2 = and.children().get(1);

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
            protected Visiting startVisit(final AndExpression t) {
                assertSame(and, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final AndExpression t) {
                assertSame(and, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ValueExpression<?> t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(and);
        this.checkEquals("1315215242", b.toString());
        this.checkEquals(Lists.of(and, and,
                text1, text1, text1,
                text2, text2, text2,
                and, and),
            visited,
            "visited");
    }

    // toBoolean.....................................................................................

    @Test
    public void testEvaluateToBooleanBooleanBooleanTrue() {
        this.evaluateAndCheckBoolean(this.createExpression(booleanValue(true), booleanValue(true)), true);
    }

    @Test
    public void testEvaluateToBooleanBooleanBooleanFalse() {
        this.evaluateAndCheckBoolean(this.createExpression(booleanValue(true), booleanValue(false)), false);
    }

    @Test
    public void testEvaluateToBooleanExpressionNumberExpressionNumberTrue() {
        // left & right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(expressionNumber(7), expressionNumber(3)), true);
    }

    @Test
    public void testEvaluateToBooleanExpressionNumberExpressionNumberFalse() {
        // left & right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(expressionNumber(8), expressionNumber(3)), false);
    }

    // toExpressionNumber.....................................................................................

    @Test
    public void testEvaluateToExpressionNumber() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(expressionNumber(6), expressionNumber(3)), 6 & 3);
    }

    // toValue...............................................................................................

    @Test
    public void testEvaluateToValueBooleanBooleanTrue() {
        this.evaluateAndCheckValue(this.createExpression(booleanValue(true), booleanValue(true)), true);
    }

    @Test
    public void testEvaluateToValueBooleanBooleanFalse() {
        this.evaluateAndCheckValue(this.createExpression(booleanValue(true), booleanValue(false)), false);
    }

    @Test
    public void testEvaluateToValueExpressionNumberExpressionNumber() {
        this.evaluateAndCheckValue(this.createExpression(expressionNumber(6), expressionNumber(3)), expressionNumberValue(6 & 3));
    }

    @Override
    AndExpression createExpression(final Expression left, final Expression right) {
        return AndExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + "&" + RIGHT_TO_STRING;
    }

    @Override
    Class<AndExpression> expressionType() {
        return AndExpression.class;
    }
}
