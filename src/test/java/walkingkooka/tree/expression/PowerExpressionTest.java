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

public final class PowerExpressionTest extends ArithmeticExpressionTestCase2<PowerExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final PowerExpression power = this.createExpression();
        final Expression text1 = power.children().get(0);
        final Expression text2 = power.children().get(1);

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
            protected Visiting startVisit(final PowerExpression t) {
                assertSame(power, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final PowerExpression t) {
                assertSame(power, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ValueExpression<?> t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(power);
        this.checkEquals("1315215242", b.toString());
        this.checkEquals(Lists.of(power, power,
                text1, text1, text1,
                text2, text2, text2,
                power, power),
            visited,
            "visited");
    }

    // toBoolean...............................................................................................

    @Test
    public void testEvaluateToBooleanTrue() {
        // left ^^ right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(expressionNumber(2), expressionNumber(3)), true);
    }

    @Test
    public void testEvaluateToBooleanFalse() {
        // left ^^ right == truthy expressionNumber
        this.evaluateAndCheckBoolean(this.createExpression(expressionNumber(0), expressionNumber(3)), false);
    }

    // toBigDecimal...............................................................................................

    @Test
    public void testEvaluateToExpressionNumber() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(expressionNumber(100), expressionNumber(0.5)), Math.pow(100, 0.5));
    }

    @Test
    public void testEvaluateToExpressionNumberText() {
        this.evaluateAndCheckExpressionNumber(this.createExpression(expressionNumber(100), text(2)), (int) Math.pow(100, 2));
    }

    @Override
    PowerExpression createExpression(final Expression left, final Expression right) {
        return PowerExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + "^^" + RIGHT_TO_STRING;
    }

    @Override
    Class<PowerExpression> expressionType() {
        return PowerExpression.class;
    }
}
