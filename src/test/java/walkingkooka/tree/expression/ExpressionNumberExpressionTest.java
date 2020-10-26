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

public final class ExpressionNumberExpressionTest extends LeafExpressionTestCase<ExpressionNumberExpression, ExpressionNumber> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final ExpressionNumberExpression node = this.createExpression();

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
            protected void visit(final ExpressionNumberExpression n) {
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
    public void testToExpressionNumber() {
        final ExpressionNumber value = this.expressionNumberValue(123);
        this.evaluateAndCheckExpressionNumber(this.createExpression(value), value);
    }

    @Test
    public void testToText() {
        this.evaluateAndCheckText(this.createExpression(123), "123");
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createExpression(this.value()), "1");
    }

    @Test
    public void testToString2() {
        this.toStringAndCheck(this.createExpression(this.expressionNumberValue(234)), "234");
    }

    private ExpressionNumberExpression createExpression(final double value) {
        return this.createExpression(this.expressionNumberValue(value));
    }

    @Override
    ExpressionNumberExpression createExpression(final ExpressionNumber value) {
        return ExpressionNumberExpression.with(value);
    }

    @Override
    ExpressionNumber value() {
        return this.expressionNumberValue(1);
    }

    @Override
    ExpressionNumber differentValue() {
        return this.expressionNumberValue(999);
    }

    @Override
    Class<ExpressionNumberExpression> expressionType() {
        return ExpressionNumberExpression.class;
    }
}
