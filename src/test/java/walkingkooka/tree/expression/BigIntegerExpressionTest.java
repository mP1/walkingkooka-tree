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

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class BigIntegerExpressionTest extends LeafExpressionTestCase<BigIntegerExpression, BigInteger> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final BigIntegerExpression node = this.createExpression();

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
            protected void visit(final BigIntegerExpression n) {
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
        this.evaluateAndCheckBigDecimal(this.createExpression(123), 123);
    }

    @Test
    public void testToBigInteger() {
        this.evaluateAndCheckBigInteger(this.createExpression(123), 123);
    }

    @Test
    public void testToDouble() {
        this.evaluateAndCheckDouble(this.createExpression(123), 123);
    }

    @Test
    public void testToLong() {
        this.evaluateAndCheckLong(this.createExpression(123), 123);
    }

    @Test
    public void testToNumberBigInteger() {
        this.evaluateAndCheckNumberBigInteger(this.createExpression(123), 123);
    }

    @Test
    public void testToText() {
        this.evaluateAndCheckText(this.createExpression(123), "123");
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createExpression(BigInteger.ONE), "1");
    }

    @Test
    public void testToString2() {
        this.toStringAndCheck(this.createExpression(BigInteger.valueOf(234)), "234");
    }

    private BigIntegerExpression createExpression(final long value) {
        return this.createExpression(BigInteger.valueOf(value));
    }

    @Override
    BigIntegerExpression createExpression(final BigInteger value) {
        return BigIntegerExpression.with(value);
    }

    @Override
    BigInteger value() {
        return BigInteger.valueOf(1);
    }

    @Override
    BigInteger differentValue() {
        return BigInteger.valueOf(999);
    }

    @Override
    Class<BigIntegerExpression> expressionType() {
        return BigIntegerExpression.class;
    }
}
