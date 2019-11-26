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

public final class LongExpressionTest extends LeafExpressionTestCase<LongExpression, Long> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final LongExpression node = this.createExpression();

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
            protected void visit(final LongExpression n) {
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
    public void testToNumber() {
        this.evaluateAndCheckNumberLong(this.createExpression(123), 123);
    }

    @Test
    public void testToText() {
        this.evaluateAndCheckText(this.createExpression(123), "123");
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createExpression(1L), "1");
    }

    @Test
    public void testToString2() {
        this.toStringAndCheck(this.createExpression(234L), "234");
    }

    private LongExpression createExpression(final long value) {
        return this.createExpression(Long.valueOf(value));
    }

    @Override
    LongExpression createExpression(final Long value) {
        return LongExpression.with(value);
    }

    @Override
    Long value() {
        return 1L;
    }

    @Override
    Long differentValue() {
        return 999L;
    }

    @Override
    Class<LongExpression> expressionType() {
        return LongExpression.class;
    }
}
