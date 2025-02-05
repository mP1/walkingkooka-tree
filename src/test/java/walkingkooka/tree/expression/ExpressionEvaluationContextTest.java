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
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.tree.expression.function.ExpressionFunctions;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
            ExpressionEvaluationContexts.fake(),
            input,
            output
        );
    }

    @Test
    public void testEvaluateIfNecessaryReference() {
        final ExpressionReference input = new FakeExpressionReference() {
            @Override
            public String toString() {
                return "Variable123";
            }
        };
        final Object output = "Xyz456";
        this.evaluateIfNecessary(
            new FakeExpressionEvaluationContext() {
                @Override
                public Optional<Optional<Object>> reference(final ExpressionReference reference) {
                    checkEquals(
                        input,
                        reference,
                        "reference"
                    );
                    return Optional.of(
                        Optional.of(output)
                    );
                }
            },
            input,
            output
        );
    }

    @Test
    public void testEvaluateIfNecessaryReferenceToExpressionToValue() {
        final ExpressionReference input = new FakeExpressionReference() {
            @Override
            public String toString() {
                return "Variable123";
            }
        };
        final Object output = "Xyz456";
        this.evaluateIfNecessary(
            new FakeExpressionEvaluationContext() {
                @Override
                public Optional<Optional<Object>> reference(final ExpressionReference reference) {
                    checkEquals(
                        input,
                        reference,
                        "reference"
                    );
                    return Optional.of(
                        Optional.of(
                            Expression.value(output)
                        )
                    );
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

    // lambdaFunction..................................................................................................

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.empty();

    private final static Class<Void> RETURN_TYPE = Void.class;

    private final static Expression EXPRESSION = Expression.value(123);

    @Test
    public void testLambdaFunctionNullParametersFails() {
        this.lambdaFunctionFails(
            null,
            RETURN_TYPE,
            EXPRESSION
        );
    }

    @Test
    public void testLambdaFunctionNullReturnTypeFails() {
        this.lambdaFunctionFails(
            PARAMETERS,
            null,
            EXPRESSION
        );
    }

    @Test
    public void testLambdaFunctionNullExpressionFails() {
        this.lambdaFunctionFails(
            PARAMETERS,
            RETURN_TYPE,
            null
        );
    }

    private void lambdaFunctionFails(final List<ExpressionFunctionParameter<?>> parameters,
                                     final Class<?> returnType,
                                     final Expression expression) {
        assertThrows(
            NullPointerException.class,
            () -> new FakeExpressionEvaluationContext()
                .lambdaFunction(
                    parameters,
                    returnType,
                    expression
                )
        );
    }

    @Test
    public void testLambdaFunction() {
        final FakeExpressionEvaluationContext context = new FakeExpressionEvaluationContext() {
            @Override
            public ExpressionEvaluationContext enterScope(final Function<ExpressionReference, Optional<Optional<Object>>> scoped) {
                return new FakeExpressionEvaluationContext() {

                    @Override
                    public <T> Either<T, String> convert(final Object value,
                                                         final Class<T> target) {
                        return this.successfulConversion(
                            ExpressionNumberKind.DOUBLE.create((Number) value),
                            target
                        );
                    }

                    @Override
                    public boolean isText(final Object value) {
                        return false;
                    }

                    @Override
                    public Object referenceOrFail(final ExpressionReference reference) {
                        switch (reference.toString()) {
                            case "x":
                                return 10;
                            case "y":
                                return 20;
                            default:
                                throw new UnsupportedOperationException("Unknown reference " + reference);
                        }
                    }
                };
            }

            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> target) {
                return this.successfulConversion(
                    target.cast(value),
                    target
                );
            }
        };
        final ExpressionFunction<ExpressionNumber, FakeExpressionEvaluationContext> function = context.lambdaFunction(
            Lists.of(
                ExpressionFunctionParameterName.with("x")
                    .required(ExpressionNumber.class),
                ExpressionFunctionParameterName.with("y")
                    .required(ExpressionNumber.class)
            ),
            ExpressionNumber.class,
            Expression.add(
                Expression.reference(
                    new FakeExpressionReference() {
                        @Override
                        public boolean testParameterName(final ExpressionFunctionParameterName parameterName) {
                            return "x".equals(parameterName.value());
                        }

                        @Override
                        public String toString() {
                            return "x";
                        }
                    }
                ),
                Expression.reference(
                    new FakeExpressionReference() {
                        @Override
                        public boolean testParameterName(final ExpressionFunctionParameterName parameterName) {
                            return "y".equals(parameterName.value());
                        }

                        @Override
                        public String toString() {
                            return "y";
                        }
                    }
                )
            )
        );

        this.checkEquals(
            ExpressionNumberKind.DOUBLE.create(30),
            function.apply(
                Lists.of(
                    10,
                    20
                ),
                context
            ),
            () -> "10+20"
        );
    }

    // prepareParameters...............................................................................................

    @Test
    public void testPrepareParameters() {
        final ExpressionFunction<Void, FakeExpressionEvaluationContext> function = ExpressionFunctions.fake();

        assertThrows(
            UnsupportedOperationException.class,
            () -> ExpressionEvaluationContexts.fake()
                .prepareParameters(
                    function,
                    Lists.empty()
                )
        );
    }

    // reference........................................................................................................

    @Test
    public void testReferenceNotFoundValueDifferentReferenceNullValue() {
        this.checkNotEquals(
            ExpressionEvaluationContext.REFERENCE_NOT_FOUND_VALUE,
            ExpressionEvaluationContext.REFERENCE_NULL_VALUE
        );
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionEvaluationContext> type() {
        return ExpressionEvaluationContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
