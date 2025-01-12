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

import static org.junit.jupiter.api.Assertions.assertSame;

public final class ReferenceExpressionTest extends LeafExpressionTestCase<ReferenceExpression, ExpressionReference> {

    @Override
    public void testPropertiesNeverReturnNull() {
    }

    @Test
    public void testToReferenceOrValue() {
        final ExpressionReference reference = new TestExpressionReference("reference!");
        final ReferenceExpression expression = ReferenceExpression.with(reference);
        this.checkEquals(reference, expression.toReferenceOrValue(ExpressionEvaluationContexts.fake()));
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final ReferenceExpression node = this.createExpression();

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
            protected void visit(final ReferenceExpression n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        this.checkEquals("132", b.toString());
    }

    // ExpressionPurity.................................................................................................

    @Test
    public void testIsPureFalse() {
        this.isPureAndCheck(
            this.createExpression(),
            this.context(),
            false
        );
    }

    // Evaluation ...................................................................................................

    @Test
    public void testToBooleanFalse() {
        this.toBooleanAndCheck(
            this.createExpression(),
            this.context("false"),
            false
        );
    }

    @Test
    public void testToBooleanTrue() {
        this.toBooleanAndCheck(
            this.createExpression(),
            this.context("true"),
            true
        );
    }

    @Test
    public void testToExpressionNumber() {
        this.toExpressionNumberAndCheck(
            this.createExpression(),
            this.context("123"),
            expressionNumberValue(123)
        );
    }

    @Test
    public void testToText() {
        this.toTextAndCheck(
            this.createExpression(),
            this.context("123"),
            "123"
        );
    }

    @Test
    public void testToValue() {
        this.toTextAndCheck(
            this.createExpression(),
            this.context("123"),
            "123"
        );
    }

    // printTree... ...................................................................................................

    @Test
    public void testPrintTree() {
        this.treePrintAndCheck(
            this.createExpression(),
            "ReferenceExpression a1 (walkingkooka.tree.expression.ReferenceExpressionTest$TestExpressionReference)\n"
        );
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createExpression(), "a1");
    }

    @Override
    ReferenceExpression createExpression(final ExpressionReference value) {
        return ReferenceExpression.with(value);
    }

    @Override
    ExpressionReference value() {
        return new TestExpressionReference("a1");
    }

    @Override
    ExpressionReference differentValue() {
        return new TestExpressionReference("different-namedFunction");
    }

    private static class TestExpressionReference extends FakeExpressionReference {

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

    private ExpressionEvaluationContext context(final String referenceText) {
        final ExpressionEvaluationContext context = context();
        final ExpressionReference value = this.value();

        return new FakeExpressionEvaluationContext() {

            @Override
            public Optional<Optional<Object>> reference(final ExpressionReference reference) {
                checkEquals(value, reference, "reference");
                return Optional.of(
                    Optional.of(
                        referenceText
                    )
                );
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
    Class<ReferenceExpression> expressionType() {
        return ReferenceExpression.class;
    }
}
