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
import walkingkooka.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class BooleanExpressionTest extends LeafExpressionTestCase<BooleanExpression, Boolean> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final BooleanExpression node = this.createExpression();

        new FakeExpressionVisitor() {
            @Override
            protected Visiting startVisit(final Expression n) {
                assertSame(node, n);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Expression n) {
                assertSame(node, n);
                b.append("2");
            }

            @Override
            protected void visit(final BooleanExpression n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        assertEquals("132", b.toString());
    }

    // Evaluation ...................................................................................................

    @Test
    public void testToBooleanFalse() {
        this.evaluateAndCheckBoolean(BooleanExpression.with(false), false);
    }

    @Test
    public void testToBooleanTrue() {
        this.evaluateAndCheckBoolean(BooleanExpression.with(true), true);
    }

    @Test
    public void testTrueToBigDecimal() {
        this.evaluateAndCheckBigDecimal(BooleanExpression.with(true), 1);
    }

    @Test
    public void testFalseToBigDecimal() {
        this.evaluateAndCheckBigDecimal(BooleanExpression.with(false), 0);
    }

    @Test
    public void testTrueToBigInteger() {
        this.evaluateAndCheckBigInteger(BooleanExpression.with(true), 1);
    }

    @Test
    public void testFalseToBigInteger() {
        this.evaluateAndCheckBigInteger(BooleanExpression.with(false), 0);
    }

    @Test
    public void testTrueToDouble() {
        this.evaluateAndCheckDouble(BooleanExpression.with(true), 1);
    }

    @Test
    public void testFalseToDouble() {
        this.evaluateAndCheckDouble(BooleanExpression.with(false), 0);
    }

    @Test
    public void testTrueToLong() {
        this.evaluateAndCheckLong(BooleanExpression.with(true), 1);
    }

    @Test
    public void testFalseToLong() {
        this.evaluateAndCheckLong(BooleanExpression.with(false), 0);
    }

    @Test
    public void testTrueToText() {
        this.evaluateAndCheckText(BooleanExpression.with(true), "true");
    }

    @Test
    public void testFalseToText() {
        this.evaluateAndCheckText(BooleanExpression.with(false), "false");
    }

    @Test
    public void testToStringTrue() {
        this.toStringAndCheck(this.createExpression(true), "true");
    }

    @Test
    public void testToStringFalse() {
        this.toStringAndCheck(this.createExpression(false), "false");
    }

    @Override
    BooleanExpression createExpression(final Boolean value) {
        return BooleanExpression.with(value);
    }

    @Override
    Boolean value() {
        return true;
    }

    @Override
    Boolean differentValue() {
        return false;
    }

    @Override
    Class<BooleanExpression> expressionType() {
        return BooleanExpression.class;
    }
}
