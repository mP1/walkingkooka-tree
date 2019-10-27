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
import walkingkooka.Either;
import walkingkooka.visit.Visiting;

import java.math.MathContext;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ExpressionReferenceNodeTest extends ExpressionLeafNodeTestCase<ExpressionReferenceNode, ExpressionReference> {

    @Override
    public void testPropertiesNeverReturnNull() {
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final ExpressionReferenceNode node = this.createExpressionNode();

        new FakeExpressionNodeVisitor() {
            @Override
            protected Visiting startVisit(final ExpressionNode n) {
                assertSame(node, n);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionNode n) {
                assertSame(node, n);
                b.append("2");
            }

            @Override
            protected void visit(final ExpressionReferenceNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        assertEquals("132", b.toString());
    }

    // Evaluation ...................................................................................................

    @Test
    public void testToBooleanFalse() {
        this.evaluateAndCheckBoolean(this.createExpressionNode(), this.context("false"), false);
    }

    @Test
    public void testToBooleanTrue() {
        this.evaluateAndCheckBoolean(this.createExpressionNode(), this.context("true"), true);
    }

    @Test
    public void testToBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(), this.context("123"), 123);
    }

    @Test
    public void testToBigInteger() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(), this.context("123"), 123);
    }

    @Test
    public void testToDouble() {
        this.evaluateAndCheckDouble(this.createExpressionNode(), this.context("123"), 123);
    }

    @Test
    public void testToLong() {
        this.evaluateAndCheckLong(this.createExpressionNode(), this.context("123"), 123);
    }

    @Test
    public void testToNumber() {
        this.evaluateAndCheckNumberBigDecimal(this.createExpressionNode(), this.context("123"), 123);
    }

    @Test
    public void testToText() {
        this.evaluateAndCheckText(this.createExpressionNode(), this.context("123"), "123");
    }

    @Test
    public void testToValue() {
        this.evaluateAndCheckValue(this.createExpressionNode(), this.context("123"), "123");
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createExpressionNode(), "a1");
    }

    @Override
    ExpressionReferenceNode createExpressionNode(final ExpressionReference value) {
        return ExpressionReferenceNode.with(value);
    }

    @Override
    ExpressionReference value() {
        return new TestExpressionReference("a1");
    }

    @Override
    ExpressionReference differentValue() {
        return new TestExpressionReference("different-function");
    }

    private static class TestExpressionReference implements ExpressionReference {

        TestExpressionReference(final String name) {
            super();
            this.name = name;
        }

        private final String name;

        @Override
        public int hashCode() {
            return this.name.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            return other instanceof TestExpressionReference && this.name.equals(other.toString());
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    final ExpressionEvaluationContext context(final String referenceText) {
        final ExpressionEvaluationContext context = context();
        final ExpressionReference value = this.value();

        return new FakeExpressionEvaluationContext() {

            @Override
            public Optional<ExpressionNode> reference(final ExpressionReference reference) {
                assertEquals(value, reference, "reference");
                return Optional.of(ExpressionNode.text(referenceText));
            }

            @Override
            public MathContext mathContext() {
                return context.mathContext();
            }

            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> target) {
                return context.convert(value, target);
            }
        };
    }

    @Override
    Class<ExpressionReferenceNode> expressionNodeType() {
        return ExpressionReferenceNode.class;
    }
}
