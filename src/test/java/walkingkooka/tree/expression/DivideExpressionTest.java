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

import java.math.MathContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class DivideExpressionTest extends ArithmeticExpressionTestCase2<DivideExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final DivideExpression division = this.createExpression();
        final Expression text1 = division.children().get(0);
        final Expression text2 = division.children().get(1);

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
            protected Visiting startVisit(final DivideExpression t) {
                assertSame(division, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final DivideExpression t) {
                assertSame(division, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ValueExpression<?> t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(division);
        this.checkEquals("1315215242", b.toString());
        this.checkEquals(Lists.of(division, division,
                text1, text1, text1,
                text2, text2, text2,
                division, division),
            visited,
            "visited");
    }

    @Test
    public void testEvaluateExpressionDivideByZeroBigDecimalFails() {
        this.evaluateExpressionDivideByZeroFails(ExpressionNumberKind.BIG_DECIMAL);
    }

    @Test
    public void testEvaluateExpressionDivideByZeroDoubleFails() {
        this.evaluateExpressionDivideByZeroFails(ExpressionNumberKind.DOUBLE);
    }

    private void evaluateExpressionDivideByZeroFails(final ExpressionNumberKind kind) {
        final ExpressionEvaluationException thrown = assertThrows(
            ExpressionEvaluationException.class,
            () -> kind.one()
                .divide(
                    kind.zero(),
                    new FakeExpressionNumberContext() {
                        @Override
                        public MathContext mathContext() {
                            return MathContext.DECIMAL32;
                        }
                    }
                )
        );

        this.checkEquals(
            "Division by zero",
            thrown.getMessage()
        );
    }

    // toBoolean........................................................................................................

    @Test
    public void testToBooleanTrue() {
        // left / right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                expressionNumber(12),
                expressionNumber(34)
            ), true
        );
    }

    @Test
    public void testToBooleanFalse() {
        // left / right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                expressionNumber(0),
                expressionNumber(-12)
            ), false
        );
    }

    // toExpressionNumber..............................................................................................

    @Test
    public void testToExpressionNumber() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                expressionNumber(60),
                expressionNumber(5)
            ), expressionNumberValue(60.0 / 5)
        );
    }

    @Override
    DivideExpression createExpression(final Expression left, final Expression right) {
        return DivideExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + "/" + RIGHT_TO_STRING;
    }

    @Override
    Class<DivideExpression> expressionType() {
        return DivideExpression.class;
    }
}
