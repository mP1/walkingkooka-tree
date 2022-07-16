/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CharSequences;

import java.util.Optional;

public final class ExpressionEvaluationContextTest implements ClassTesting<ExpressionEvaluationContext> {

    @Test
    public void testEvaluateIfNecessaryNull() {
        this.evaluateIfNecessary(null);
    }

    @Test
    public void testEvaluateIfNecessaryBoolean() {
        this.evaluateIfNecessary(true);
    }

    @Test
    public void testEvaluateIfNecessaryExpressionNumber() {
        this.evaluateIfNecessary(
                ExpressionNumberKind.BIG_DECIMAL.one()
        );
    }

    @Test
    public void testEvaluateIfNecessaryString() {
        this.evaluateIfNecessary("Apple");
    }

    private void evaluateIfNecessary(final Object value) {
        this.evaluateIfNecessary(
                value,
                value
        );
    }

    private void evaluateIfNecessary(final Object value,
                                     final Object expected) {
        this.evaluateIfNecessary(
                ExpressionEvaluationContexts.fake(),
                value,
                expected
        );
    }


    @Test
    public void testEvaluateIfNecessaryExpression() {
        final Object output = "Xyz456";
        final Expression input = Expression.value(output);

        this.evaluateIfNecessary(
                new FakeExpressionEvaluationContext() {
                    @Override
                    public Object evaluate(final Expression expression) {
                        return ValueExpression.class.cast(expression).value();
                    }
                },
                input,
                output
        );
    }

    @Test
    public void testEvaluateIfNecessaryReference() {
        final ExpressionReference input = new ExpressionReference() {
            @Override
            public String toString() {
                return "Variable123";
            }
        };
        final Object output = "Xyz456";
        this.evaluateIfNecessary(
                new FakeExpressionEvaluationContext() {
                    @Override
                    public Optional<Object> reference(final ExpressionReference reference) {
                        checkEquals(
                                input,
                                reference,
                                "reference"
                        );
                        return Optional.of(output);
                    }
                },
                input,
                output
        );
    }

    @Test
    public void testEvaluateIfNecessaryReferenceToExpressionToValue() {
        final ExpressionReference input = new ExpressionReference() {
            @Override
            public String toString() {
                return "Variable123";
            }
        };
        final Object output = "Xyz456";
        this.evaluateIfNecessary(
                new FakeExpressionEvaluationContext() {
                    @Override
                    public Optional<Object> reference(final ExpressionReference reference) {
                        checkEquals(
                                input,
                                reference,
                                "reference"
                        );
                        return Optional.of(
                                Expression.value(output)
                        );
                    }

                    @Override
                    public Object evaluate(final Expression expression) {
                        return ValueExpression.class.cast(expression).value();
                    }
                },
                input,
                output
        );
    }

    private void evaluateIfNecessary(final ExpressionEvaluationContext context,
                                     final Object value,
                                     final Object expected) {
        this.checkEquals(
                expected,
                context.evaluateIfNecessary(value),
                () -> "evaluateIfNecessary " + CharSequences.quoteIfChars(value)
        );
    }

    @Override
    public Class<ExpressionEvaluationContext> type() {
        return ExpressionEvaluationContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
