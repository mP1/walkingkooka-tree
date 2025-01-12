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

public final class LessThanEqualsExpressionTest extends CompareExpressionTestCase<LessThanEqualsExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final LessThanEqualsExpression lte = this.createExpression();
        final Expression text1 = lte.children().get(0);
        final Expression text2 = lte.children().get(1);

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
            protected Visiting startVisit(final LessThanEqualsExpression t) {
                assertSame(lte, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final LessThanEqualsExpression t) {
                assertSame(lte, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ValueExpression<?> t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(lte);
        this.checkEquals("1315215242", b.toString());
        this.checkEquals(Lists.of(lte, lte,
                text1, text1, text1,
                text2, text2, text2,
                lte, lte),
            visited,
            "visited");
    }

    // ExpressionNumber ................................................................................................

    @Test
    public void testToBooleanExpressionNumberExpressionNumber() {
        // left lte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                expressionNumber(12),
                expressionNumber(34)
            ), true
        );
    }

    @Test
    public void testToBooleanExpressionNumberExpressionNumber2() {
        // left lte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                expressionNumber(12),
                expressionNumber(12)
            ), true
        );
    }

    @Test
    public void testToBooleanExpressionNumberExpressionNumber3() {
        // left lte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                expressionNumber(12),
                expressionNumber(-99)
            ), false
        );
    }

    // Text ................................................................................................

    @Test
    public void testToBooleanTextExpressionNumber() {
        // left lte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                text(12),
                expressionNumber(34)
            ), true
        );
    }

    @Test
    public void testToBooleanTextExpressionNumber2() {
        // left lte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                text(12),
                expressionNumber(12)
            ), true
        );
    }

    @Test
    public void testToBooleanTextExpressionNumber3() {
        // left lte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                text(12),
                expressionNumber(-99)
            ), false
        );
    }

    @Test
    public void testToBooleanTextText() {
        // left lte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                text(12),
                text(34)
            ), true
        );
    }

    @Test
    public void testToBooleanTextText2() {
        // left lte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                text(12),
                text(12)
            ), true
        );
    }

    @Test
    public void testToBooleanTextText3() {
        // left lte right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                text(12),
                text(-99)
            ), false
        );
    }

    @Override
    LessThanEqualsExpression createExpression(final Expression left, final Expression right) {
        return LessThanEqualsExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + "<=" + RIGHT_TO_STRING;
    }

    @Override
    Class<LessThanEqualsExpression> expressionType() {
        return LessThanEqualsExpression.class;
    }
}
