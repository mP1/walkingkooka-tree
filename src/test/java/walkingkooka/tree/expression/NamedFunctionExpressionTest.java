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
import walkingkooka.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class NamedFunctionExpressionTest extends LeafExpressionTestCase<NamedFunctionExpression, ExpressionFunctionName> {

    private final static String NAME = "test-function-123";

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final NamedFunctionExpression node = this.createExpression();

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
            protected void visit(final NamedFunctionExpression n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        this.checkEquals("132", b.toString());
    }

    // Evaluation ...................................................................................................

    @Test
    public void testToBooleanTrue() {
        this.evaluateAndCheckBoolean(
                this.createExpression(),
                true
        );
    }

    @Test
    public void testToText() {
        this.evaluateAndCheckText(
                this.createExpression(),
                NAME
        );
    }

    // printTree... ...................................................................................................

    @Test
    public void testPrintTree() {
        this.treePrintAndCheck(
                this.createExpression(),
                "NamedFunctionExpression test-function-123\n"
        );
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createExpression(),
                NAME
        );
    }

    @Override
    NamedFunctionExpression createExpression(final ExpressionFunctionName value) {
        return NamedFunctionExpression.with(value);
    }

    @Override
    ExpressionFunctionName value() {
        return ExpressionFunctionName.with(NAME);
    }

    @Override
    ExpressionFunctionName differentValue() {
        return ExpressionFunctionName.with("test-different");
    }

    @Override
    Class<NamedFunctionExpression> expressionType() {
        return Cast.to(NamedFunctionExpression.class);
    }
}
