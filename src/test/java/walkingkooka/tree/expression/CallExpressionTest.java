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
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.tree.expression.function.FakeExpressionFunction;
import walkingkooka.tree.select.parser.NodeSelectorAttributeName;
import walkingkooka.visit.Visiting;

import java.math.MathContext;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CallExpressionTest extends VariableExpressionTestCase<CallExpression> {

    @Test
    public void testWithNullFunctionFails() {
        assertThrows(
                NullPointerException.class,
                () -> CallExpression.with(
                        null,
                        this.children()
                )
        );
    }

    @Test
    public void testSetCallableNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createExpression().setCallable(null)
        );
    }

    @Test
    public void testSetCallableSame() {
        final CallExpression expression = this.createExpression();
        assertSame(
                expression,
                expression.setCallable(this.namedFunction())
        );
    }

    @Test
    public void testSetCallableDifferent() {
        final CallExpression expression = this.createExpression();
        final NamedFunctionExpression differentNamedFunction = this.namedFunction("different-name");
        final CallExpression different = expression.setCallable(differentNamedFunction);

        this.checkEquals(differentNamedFunction, different.callable(), "callable()");
        this.checkChildren(different, expression.children());
    }

    // ExpressionPurity.................................................................................................

    @Test
    public void testIsPureTrue() {
        this.isPureAndCheck2(true);
    }

    @Test
    public void testIsPureFalse() {
        this.isPureAndCheck2(false);
    }

    private void isPureAndCheck2(final boolean pure) {
        final NamedFunctionExpression namedFunction = this.namedFunction();

        this.isPureAndCheck(
                CallExpression.with(
                        namedFunction,
                        Expression.NO_CHILDREN
                ),
                new ExpressionPurityContext() {
                    @Override
                    public boolean isPure(final FunctionExpressionName n) {
                        checkEquals(namedFunction.value(), n, "name");
                        return pure;
                    }
                },
                pure
        );
    }

    // toValue.........................................................................................................

    private final static FunctionExpressionName FUNCTION_NAME = FunctionExpressionName.with("test-function");

    @Test
    public void testToValue() {
        final StringName attribute = Names.string("attribute123");
        final ExpressionReference reference = NodeSelectorAttributeName.with(attribute.value());

        final CallExpression call = Expression.call(
                Expression.namedFunction(FUNCTION_NAME),
                Lists.of(
                        Expression.value("1"),
                        Expression.reference(reference)
                )
        );
        final List<Expression> parameters = call.value();

        this.checkEquals(
                parameters,
                call.toValue(
                        new FakeExpressionEvaluationContext() {
                            @Override
                            public ExpressionFunction<?, ExpressionEvaluationContext> function(final FunctionExpressionName name) {
                                checkEquals(FUNCTION_NAME, name, "namedFunction name");
                                return new FakeExpressionFunction<>() {

                                    @Override
                                    public Object apply(final List<Object> p,
                                                        final ExpressionEvaluationContext context) {
                                        checkEquals(parameters, p);
                                        return parameters;
                                    }
                                };
                            }

                            @Override
                            public Object evaluateFunction(final ExpressionFunction<?, ? extends ExpressionEvaluationContext> function,
                                                           final List<Object> parameters) {
                                return function.apply(
                                        parameters,
                                        Cast.to(this)
                                );
                            }
                        }
                )
        );
    }

    // accept..........................................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final CallExpression call = this.createExpression();
        final Expression namedFunction = call.callable();
        final Expression text1 = call.children().get(0);
        final Expression text2 = call.children().get(1);
        final Expression text3 = call.children().get(2);

        new FakeExpressionVisitor() {
            @Override
            protected Visiting startVisit(final Expression n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Expression n) {
                b.append("2");
                visited.add(n);
            }

            @Override
            protected Visiting startVisit(final CallExpression c) {
                assertSame(call, c);
                b.append("3");
                visited.add(c);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final CallExpression c) {
                assertSame(call, c);
                b.append("4");
                visited.add(c);
            }

            @Override
            protected void visit(final NamedFunctionExpression n) {
                assertSame(namedFunction, n);
                b.append("5");
                visited.add(n);
            }

            @Override
            protected void visit(final ValueExpression<?> v) {
                b.append("6");
                visited.add(v);
            }
        }.accept(call);

        this.checkEquals("13516216216242", b.toString());
        this.checkEquals(
                Lists.of(
                        call, call,
                        namedFunction,
                        text1, text1, text1,
                        text2, text2, text2,
                        text3, text3, text3,
                        call, call),
                visited,
                "visited"
        );
    }

    // Evaluation ...................................................................................................

    @Test
    public void testEvaluateAddExpressionWithoutParameters() {
        this.evaluateAndCheckExpressionNumber(
                CallExpression.with(
                        Expression.add(
                                Expression.value(100),
                                Expression.value(200)
                        ),
                        Expression.NO_CHILDREN
                ),
                new FakeExpressionEvaluationContext() {

                    @Override
                    public <T> Either<T, String> convert(final Object value,
                                                         final Class<T> target) {
                        return this.successfulConversion(
                                target.cast(
                                        EXPRESSION_NUMBER_KIND.create((Number) value)
                                ),
                                target
                        );
                    }

                    @Override
                    public Object evaluate(final Expression expression) {
                        return expression.toValue(this);
                    }

                    @Override
                    public Object evaluateFunction(final ExpressionFunction<?, ? extends ExpressionEvaluationContext> function,
                                                   final List<Object> parameters) {
                        return function.apply(
                                this.prepareParameters(function, parameters),
                                Cast.to(this)
                        );
                    }

                    @Override
                    public boolean isText(final Object value) {
                        return false;
                    }
                },
                expressionNumberValue(300)
        );
    }

    @Test
    public void testEvaluateLambdaExpressionNumber() {
        this.evaluateAndCheckExpressionNumber(
                CallExpression.with(
                        this.lambdaFunction(),
                        Lists.of(
                                Expression.value(10),
                                Expression.value(20)
                        )
                ),
                new FakeExpressionEvaluationContext() {

                    @Override
                    public ExpressionEvaluationContext context(final Function<ExpressionReference, Optional<Optional<Object>>> scoped) {
                        return new FakeExpressionEvaluationContext() {

                            @Override
                            public <T> Either<T, String> convert(final Object value,
                                                                 final Class<T> target) {
                                return this.successfulConversion(
                                        target.cast(
                                                EXPRESSION_NUMBER_KIND.create((Number) value)
                                        ),
                                        target
                                );
                            }

                            @Override
                            public Object evaluate(final Expression expression) {
                                return expression.toValue(this);
                            }

                            @Override
                            public boolean isText(final Object value) {
                                return false;
                            }

                            @Override
                            public Optional<Optional<Object>> reference(final ExpressionReference reference) {
                                return scoped.apply(reference);
                            }
                        };
                    }

                    @Override
                    public <T> Either<T, String> convert(final Object value,
                                                         final Class<T> target) {
                        return this.successfulConversion(
                                target.cast(
                                        EXPRESSION_NUMBER_KIND.create((Number) value)
                                ),
                                target
                        );
                    }

                    @Override
                    public Object evaluate(final Expression expression) {
                        return expression.toValue(this);
                    }

                    @Override
                    public Object evaluateFunction(final ExpressionFunction<?, ? extends ExpressionEvaluationContext> function,
                                                   final List<Object> parameters) {
                        Object result;

                        try {
                            result = function.apply(
                                    this.prepareParameters(function, parameters),
                                    Cast.to(this)
                            );
                        } catch (final RuntimeException exception) {
                            result = this.handleException(exception);
                        }

                        return result;
                    }

                    @Override
                    public boolean isText(final Object value) {
                        return false;
                    }
                },
                expressionNumberValue(30)
        );
    }

    @Test
    public void testEvaluateNamedFunctionBooleanFalse() {
        this.evaluateAndCheckBoolean(
                this.createExpression(),
                this.context("false"), false
        );
    }

    @Test
    public void testEvaluateNamedFunctionBooleanTrue() {
        this.evaluateAndCheckBoolean(
                this.createExpression(),
                this.context("true"), true
        );
    }

    @Test
    public void testEvaluateNamedFunctionExpressionNumber() {
        this.evaluateAndCheckExpressionNumber(
                this.createExpression(),
                this.context("123"),
                expressionNumberValue(123));
    }

    @Test
    public void testEvaluateNamedFunctionText() {
        this.evaluateAndCheckText(
                this.createExpression(),
                this.context("123"),
                "123"
        );
    }

    @Test
    public void testEvaluateNamedFunctionReturnExpressionFunctionAndCall() {
        final int namedFunctionParameterValue = 999;

        final Expression e = CallExpression.with(
                CallExpression.with(
                        this.namedFunction(),
                        Lists.of(
                                Expression.value(namedFunctionParameterValue)
                        )
                ),
                Lists.of(
                        Expression.value(expressionNumberValue(10)),
                        Expression.value(expressionNumberValue(20))
                )
        );

        this.evaluateAndCheckValue(
                e,
                new FakeExpressionEvaluationContext() {

                    @Override
                    public <T> Either<T, String> convert(final Object value,
                                                         final Class<T> target) {
                        return this.successfulConversion(
                                target.cast(
                                        EXPRESSION_NUMBER_KIND.create((Number) value)
                                ),
                                target
                        );
                    }

                    @Override
                    public Object evaluate(final Expression expression) {
                        return expression.toValue(this);
                    }

                    @Override
                    public Object evaluateFunction(final ExpressionFunction<?, ? extends ExpressionEvaluationContext> function,
                                                   final List<Object> parametersValues) {
                        return function.apply(
                                this.prepareParameters(
                                        function,
                                        parametersValues
                                ),
                                Cast.to(this)
                        );
                    }

                    @Override
                    public ExpressionFunction<?, ExpressionEvaluationContext> function(final FunctionExpressionName name) {
                        checkEquals(FUNCTION_NAME, name, "namedFunction name");

                        return new FakeExpressionFunction<>() {

                            @Override
                            public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                                return Lists.of(a);
                            }

                            private final ExpressionFunctionParameter<ExpressionNumber> a = parameter("a");

                            @Override
                            public Object apply(final List<Object> parameters,
                                                final ExpressionEvaluationContext context) {
                                this.checkParameterCount(parameters);

                                checkEquals(
                                        namedFunctionParameterValue,
                                        a.getOrFail(parameters, 0),
                                        "a"
                                );
                                return new FakeExpressionFunction<>() {

                                    @Override
                                    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                                        return Lists.of(
                                                x,
                                                y
                                        );
                                    }

                                    private final ExpressionFunctionParameter<ExpressionNumber> x = parameter("x");
                                    private final ExpressionFunctionParameter<ExpressionNumber> y = parameter("y");

                                    @Override
                                    public Object apply(final List<Object> parameters,
                                                        final ExpressionEvaluationContext context) {
                                        this.checkParameterCount(parameters);

                                        final ExpressionNumber x = this.x.getOrFail(parameters, 0);
                                        final ExpressionNumber y = this.y.getOrFail(parameters, 1);
                                        return x.add(y, context);
                                    }

                                    @Override
                                    public String toString() {
                                        return "FUNCTION(x,y){return x+y;)";
                                    }
                                };
                            }

                            @Override
                            public String toString() {
                                return FUNCTION_NAME.toString();
                            }
                        };
                    }
                },
                expressionNumberValue(30)
        );
    }

    private ExpressionEvaluationContext context(final String functionValue) {
        final ExpressionEvaluationContext context = context();

        return new FakeExpressionEvaluationContext() {
            @Override
            public ExpressionFunction<?, ExpressionEvaluationContext> function(final FunctionExpressionName name) {
                checkEquals(FUNCTION_NAME, name, "namedFunction name");

                return new FakeExpressionFunction<>() {
                    @Override
                    public Object apply(final List<Object> parameters,
                                        final ExpressionEvaluationContext context) {
                        return functionValue;
                    }

                    @Override
                    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public Object evaluateFunction(final ExpressionFunction<?, ? extends ExpressionEvaluationContext> function,
                                           final List<Object> parameters) {
                return function.apply(
                        parameters,
                        Cast.to(this)
                );
            }

            @Override
            public MathContext mathContext() {
                return context.mathContext();
            }

            @Override
            public <T> Either<T, String> convert(final Object value, final Class<T> target) {
                return context.convert(value, target);
            }
        };
    }

    // printTree........................................................................................................

    @Test
    public void testPrintTree() {
        this.treePrintAndCheck(
                this.createExpression(),
                "CallExpression\n" +
                        "  callable\n" +
                        "    NamedFunctionExpression test-function\n" +
                        "  parameters\n" +
                        "      ValueExpression \"child-111\" (java.lang.String)\n" +
                        "      ValueExpression \"child-222\" (java.lang.String)\n" +
                        "      ValueExpression \"child-333\" (java.lang.String)\n"
        );
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createExpression(),
                "test-function(\"child-111\",\"child-222\",\"child-333\")"
        );
    }

    @Override
    CallExpression createExpression(final List<Expression> parameterValues) {
        return CallExpression.with(
                this.namedFunction(),
                parameterValues
        );
    }

    private LambdaFunctionExpression lambdaFunction() {
        final String x = "x";
        final String y = "y";

        return Expression.lambdaFunction(
                Lists.of(
                        parameter(x),
                        parameter(y)
                ),
                Expression.add(
                        referenceExpression("x"),
                        referenceExpression("y")
                )
        );
    }

    private NamedFunctionExpression namedFunction() {
        return this.namedFunction(FUNCTION_NAME.value());
    }

    private NamedFunctionExpression namedFunction(final String name) {
        return Expression.namedFunction(
                FunctionExpressionName.with(name)
        );
    }

    private static ExpressionFunctionParameter<ExpressionNumber> parameter(final String name) {
        return ExpressionFunctionParameterName.with(name)
                .required(ExpressionNumber.class)
                .setKinds(ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES);
    }

    private static ReferenceExpression referenceExpression(final String name) {
        return Expression.reference(
                new FakeExpressionReference() {
                    @Override
                    public boolean testParameterName(final ExpressionFunctionParameterName parameterName) {
                        return this.toString().equals(parameterName.value());
                    }

                    @Override
                    public String toString() {
                        return name;
                    }
                }
        );
    }

    @Override
    Class<CallExpression> expressionType() {
        return CallExpression.class;
    }
}
