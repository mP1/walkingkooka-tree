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
import walkingkooka.Cast;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class ValueExpressionTest extends LeafExpressionTestCase<ValueExpression<ExpressionNumber>, ExpressionNumber> implements TreePrintableTesting {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final ValueExpression<ExpressionNumber> node = this.createExpression();

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
            protected void visit(final ValueExpression<?> n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        this.checkEquals("132", b.toString());
    }

    // Evaluation ...................................................................................................

    @Test
    public void testToBooleanFalse() {
        this.toBooleanAndCheck(
            this.createExpression(0),
            false
        );
    }

    @Test
    public void testToBooleanTrue() {
        this.toBooleanAndCheck(
            this.createExpression(1),
            true
        );
    }

    @Test
    public void testToExpressionNumber() {
        final ExpressionNumber value = this.expressionNumberValue(123);
        this.toExpressionNumberAndCheck(
            this.createExpression(value),
            value
        );
    }

    @Test
    public void testToText() {
        this.toTextAndCheck(
            this.createExpression(123),
            "123"
        );
    }

    // printTree... ...................................................................................................

    @Test
    public void testPrintTreeNonNull() {
        this.treePrintAndCheck(
            ValueExpression.with(1),
            "ValueExpression 1 (java.lang.Integer)\n"
        );
    }

    @Test
    public void testPrintTreeNonNullString() {
        this.treePrintAndCheck(
            ValueExpression.with("abc"),
            "ValueExpression \"abc\" (java.lang.String)\n"
        );
    }

    @Test
    public void testPrintTreeNull() {
        this.treePrintAndCheck(
            ValueExpression.with(null),
            "ValueExpression null\n"
        );
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

    private ValueExpression<ExpressionNumber> createExpression(final double value) {
        return this.createExpression(
            this.expressionNumberValue(value)
        );
    }

    @Override
    ValueExpression<ExpressionNumber> createExpression(final ExpressionNumber value) {
        return ValueExpression.with(value);
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
    Class<ValueExpression<ExpressionNumber>> expressionType() {
        return Cast.to(ValueExpression.class);
    }
}
