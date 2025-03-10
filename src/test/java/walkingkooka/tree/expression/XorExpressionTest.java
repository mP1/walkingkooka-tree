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

public final class XorExpressionTest extends LogicalExpressionTestCase<XorExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final XorExpression xor = this.createExpression();
        final Expression text1 = xor.children().get(0);
        final Expression text2 = xor.children().get(1);

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
            protected Visiting startVisit(final XorExpression t) {
                assertSame(xor, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final XorExpression t) {
                assertSame(xor, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ValueExpression<?> t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(xor);
        this.checkEquals("1315215242", b.toString());
        this.checkEquals(Lists.of(xor, xor,
                text1, text1, text1,
                text2, text2, text2,
                xor, xor),
            visited,
            "visited");
    }

    // toBoolean........................................................................................................

    @Test
    public void testToBooleanBooleanBooleanTrue() {
        this.toBooleanAndCheck(
            this.createExpression(
                booleanValue(false),
                booleanValue(true)
            ), true
        );
    }

    @Test
    public void testToBooleanBooleanBooleanTrue2() {
        this.toBooleanAndCheck(
            this.createExpression(
                booleanValue(true),
                booleanValue(false)
            ), true
        );
    }

    @Test
    public void testToBooleanBooleanBooleanFalse() {
        this.toBooleanAndCheck(
            this.createExpression(
                booleanValue(false),
                booleanValue(false)
            ), false
        );
    }

    @Test
    public void testToBooleanBooleanBooleanFalse2() {
        this.toBooleanAndCheck(
            this.createExpression(
                booleanValue(true),
                booleanValue(true)
            ), false
        );
    }

    @Test
    public void testToBooleanExpressionNumberTrue() {
        // left ^ right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                expressionNumber(7),
                expressionNumber(3)
            ), true
        );
    }

    @Test
    public void testToBooleanExpressionNumberExpressionNumberFalse() {
        // left ^ right == truthy expressionNumber
        this.toBooleanAndCheck(
            this.createExpression(
                expressionNumber(8),
                expressionNumber(8)
            ), false
        );
    }

    @Test
    public void testToBooleanBooleanFalse() {
        this.toBooleanAndCheck(
            this.createExpression(
                booleanValue(false),
                booleanValue(false)
            ), false
        );
    }

    @Test
    public void testToBooleanBooleanFalse2() {
        this.toBooleanAndCheck(
            this.createExpression(
                booleanValue(true),
                booleanValue(true)
            ), false
        );
    }

    // toExpressionNumber...............................................................................................

    @Test
    public void testToExpressionNumber() {
        this.toExpressionNumberAndCheck(
            this.createExpression(
                expressionNumber(6),
                expressionNumber(3)
            ), 6 ^ 3
        );
    }

    // toValue..........................................................................................................

    @Test
    public void testToValue() {
        this.toValueAndCheck(
            this.createExpression(
                expressionNumber(6),
                expressionNumber(3)
            ), expressionNumberValue(6 ^ 3)
        );
    }

    @Override
    XorExpression createExpression(final Expression left, final Expression right) {
        return XorExpression.with(left, right);
    }

    @Override
    String expectedToString() {
        return LEFT_TO_STRING + "^" + RIGHT_TO_STRING;
    }

    @Override
    Class<XorExpression> expressionType() {
        return XorExpression.class;
    }
}
