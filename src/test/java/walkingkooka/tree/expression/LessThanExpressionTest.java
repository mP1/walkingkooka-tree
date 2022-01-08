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

public final class LessThanExpressionTest extends BinaryComparisonExpressionTestCase<LessThanExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final LessThanExpression lt = this.createExpression();
        final Expression text1 = lt.children().get(0);
        final Expression text2 = lt.children().get(1);

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
            protected Visiting startVisit(final LessThanExpression t) {
                assertSame(lt, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final LessThanExpression t) {
                assertSame(lt, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ValueExpression<?> t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(lt);
        this.checkEquals("1315215242", b.toString());
        this.checkEquals(Lists.of(lt, lt,
                        text1, text1, text1,
                        text2, text2, text2,
                        lt, lt),
                visited,
                "visited");
    }

    // evaluate........ ................................................................................................

    @Test
    public void testEvaluateToBooleanExpressionNumberExpressionNumber() {
        // left lt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(expressionNumber(12), expressionNumber(34)), true);
    }

    @Test
    public void testEvaluateToBooleanExpressionNumberExpressionNumber2() {
        // left lt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(expressionNumber(12), expressionNumber(12)), false);
    }

    @Test
    public void testEvaluateToBooleanExpressionNumberExpressionNumber3() {
        // left lt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(expressionNumber(12), expressionNumber(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanTextExpressionNumber() {
        // left lt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(text(12), expressionNumber(34)), true);
    }

    @Test
    public void testEvaluateToBooleanTextExpressionNumber2() {
        // left lt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(text(12), expressionNumber(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextExpressionNumber3() {
        // left lt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(text(12), expressionNumber(-99)), false);
    }

    @Test
    public void testEvaluateToBooleanTextText() {
        // left lt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(text(12), text(34)), true);
    }

    @Test
    public void testEvaluateToBooleanTextText2() {
        // left lt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(text(12), text(12)), false);
    }

    @Test
    public void testEvaluateToBooleanTextText3() {
        // left lt right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(text(12), text(-99)), false);
    }

    @Override
    LessThanExpression createExpression(final Expression left, final Expression right) {
        return LessThanExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + "<" + RIGHT_TO_STRING;
    }

    @Override
    Class<LessThanExpression> expressionType() {
        return LessThanExpression.class;
    }
}
