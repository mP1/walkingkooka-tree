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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class BigDecimalExpressionTest extends LeafExpressionTestCase<BigDecimalExpression, BigDecimal> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final BigDecimalExpression node = this.createExpression();

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
            protected void visit(final BigDecimalExpression n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        assertEquals("132", b.toString());
    }

    // Evaluation ...................................................................................................

    @Test
    public void testToBooleanFalse() {
        this.evaluateAndCheckBoolean(this.createExpression(0), false);
    }

    @Test
    public void testToBooleanTrue() {
        this.evaluateAndCheckBoolean(this.createExpression(1), true);
    }

    @Test
    public void testToBigDecimal() {
        final BigDecimal value = BigDecimal.valueOf(123);
        this.evaluateAndCheckBigDecimal(this.createExpression(value), value);
    }

    @Test
    public void testToBigInteger() {
        this.evaluateAndCheckBigInteger(this.createExpression(123), 123);
    }

    @Test
    public void testToDouble() {
        this.evaluateAndCheckDouble(this.createExpression(123.5), 123.5);
    }

    @Test
    public void testToLong() {
        this.evaluateAndCheckLong(this.createExpression(123), 123);
    }

    @Test
    public void testToNumberBigDecimal() {
        this.evaluateAndCheckNumberBigDecimal(this.createExpression(123), 123);
    }

    @Test
    public void testToText() {
        this.evaluateAndCheckText(this.createExpression(123), "123");
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createExpression(BigDecimal.valueOf(1)), "1");
    }

    @Test
    public void testToString2() {
        this.toStringAndCheck(this.createExpression(BigDecimal.valueOf(234)), "234");
    }

    private BigDecimalExpression createExpression(final double value) {
        return this.createExpression(BigDecimal.valueOf(value));
    }

    @Override
    BigDecimalExpression createExpression(final BigDecimal value) {
        return BigDecimalExpression.with(value);
    }

    @Override
    BigDecimal value() {
        return BigDecimal.ONE;
    }

    @Override
    BigDecimal differentValue() {
        return BigDecimal.valueOf(999);
    }

    @Override
    Class<BigDecimalExpression> expressionType() {
        return BigDecimalExpression.class;
    }
}
