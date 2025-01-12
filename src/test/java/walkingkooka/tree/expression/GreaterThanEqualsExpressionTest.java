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

public final class GreaterThanEqualsExpressionTest extends CompareExpressionTestCase<GreaterThanEqualsExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final GreaterThanEqualsExpression gte = this.createExpression();
        final Expression text1 = gte.children().get(0);
        final Expression text2 = gte.children().get(1);

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
            protected Visiting startVisit(final GreaterThanEqualsExpression t) {
                assertSame(gte, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final GreaterThanEqualsExpression t) {
                assertSame(gte, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ValueExpression<?> t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(gte);
        this.checkEquals("1315215242", b.toString());
        this.checkEquals(Lists.of(gte, gte,
                text1, text1, text1,
                text2, text2, text2,
                gte, gte),
            visited);
    }

    // evaluate.........................................................................................................

    @Test
    public void testToBooleanExpressionNumberExpressionNumber() {
        // left gte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                expressionNumber(12),
                expressionNumber(34)
            ), false
        );
    }

    @Test
    public void testToBooleanExpressionNumberExpressionNumber2() {
        // left gte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                expressionNumber(12),
                expressionNumber(12)
            ), true
        );
    }

    @Test
    public void testToBooleanExpressionNumberExpressionNumber3() {
        // left gte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                expressionNumber(12),
                expressionNumber(-99)
            ), true
        );
    }

    // Text.............................................................................................................

    @Test
    public void testToBooleanTextExpressionNumber() {
        // left gte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                text(12),
                expressionNumber(34)
            ), false
        );
    }

    @Test
    public void testToBooleanTextExpressionNumber2() {
        // left gte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                text(12),
                expressionNumber(12)
            ), true
        );
    }

    @Test
    public void testToBooleanTextExpressionNumber3() {
        // left gte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                text(12),
                expressionNumber(-99)
            ), true
        );
    }

    @Test
    public void testToBooleanTextText() {
        // left gte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                text(12),
                text(34)
            ), false
        );
    }

    @Test
    public void testToBooleanTextText2() {
        // left gte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                text(12),
                text(12)
            ), true
        );
    }

    @Test
    public void testToBooleanTextText3() {
        // left gte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                text(12),
                text(-99)
            ), true
        );
    }

    @Override
    GreaterThanEqualsExpression createExpression(final Expression left, final Expression right) {
        return GreaterThanEqualsExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + ">=" + RIGHT_TO_STRING;
    }

    @Override
    Class<GreaterThanEqualsExpression> expressionType() {
        return GreaterThanEqualsExpression.class;
    }
}
