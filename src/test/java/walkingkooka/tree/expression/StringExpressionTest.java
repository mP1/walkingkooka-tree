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

public final class StringExpressionTest extends LeafExpressionTestCase<StringExpression, String> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final StringExpression node = this.createExpression();

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
            protected void visit(final StringExpression n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        assertEquals("132", b.toString());
    }

    // Evaluation ...................................................................................................

    @Test
    public void testToBooleanFalse() {
        this.evaluateAndCheckBoolean(this.createExpression("false"), false);
    }

    @Test
    public void testToBooleanTrue() {
        this.evaluateAndCheckBoolean(this.createExpression("true"), true);
    }

    @Test
    public void testToExpressionNumber() {
        this.evaluateAndCheckExpressionNumber(this.createExpression("123"), expressionNumberValue(123));
    }

    @Test
    public void testToText() {
        this.evaluateAndCheckText(this.createExpression("123"), "123");
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createExpression("abc123"), "\"abc123\"");
    }

    @Test
    public void testToStringRequiresEscaping() {
        this.toStringAndCheck(this.createExpression("abc\t123"), "\"abc\\t123\"");
    }

    @Override
    StringExpression createExpression(final String value) {
        return StringExpression.with(value);
    }

    @Override
    String value() {
        return "A";
    }

    @Override
    String differentValue() {
        return "Different";
    }

    @Override
    Class<StringExpression> expressionType() {
        return StringExpression.class;
    }
}
