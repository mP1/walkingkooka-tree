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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.function.ExpressionFunctionKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionEvaluationContextPrepareParametersListNonFlattenedTest extends ExpressionEvaluationContextPrepareParametersListTestCase<ExpressionEvaluationContextPrepareParametersListNonFlattened> {

    private final static ExpressionNumberKind KIND = ExpressionNumberKind.DOUBLE;

    @Test
    public void testSize() {
        final Object element1 = 111;
        final Object element2 = Expression.value("222");

        final List<Object> parameters = Lists.of(
                element1,
                element2
        );
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        this.function(
                                ExpressionFunctionKind.CONVERT_PARAMETERS
                        ),
                        CONTEXT
                )
        );

        this.sizeAndCheck(list, 2);
    }

    @Test
    public void testNotConverted() {
        final Object element1 = 111;
        final Object element2 = "222";

        final List<Object> parameters = Lists.of(
                element1,
                element2
        );
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        this.function(ExpressionFunctionKind.EVALUATE_PARAMETERS),
                        ExpressionEvaluationContexts.fake()
                )
        );

        this.sizeAndCheck(list, 2);
        this.getAndCheck(list, 0, element1);
        this.getAndCheck(list, 1, element2);

        this.getAndCheck(list, 0, element1);
        this.getAndCheck(list, 1, element2);
    }

    @Test
    public void testObjectRequiresEvaluatedParametersFalse() {
        final Object element1 = 111;
        final Object element2 = "222";

        final List<Object> parameters = Lists.of(
                element1,
                element2
        );
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        this.function(
                                ExpressionFunctionKind.CONVERT_PARAMETERS
                        ),
                        CONTEXT
                )
        );

        this.sizeAndCheck(list, 2);
        this.getAndCheck(list, 0, element1);
        this.getAndCheck(list, 1, element2);
    }

    @Test
    public void testExpressionRequiresEvaluatedParametersFalse() {
        final Expression element1 = Expression.value(111);
        final Expression element2 = Expression.value("222");

        final List<Object> parameters = Lists.of(
                element1,
                element2
        );
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        this.function(
                                ExpressionFunctionKind.CONVERT_PARAMETERS
                        ),
                        CONTEXT
                )
        );

        this.sizeAndCheck(list, 2);
        this.getAndCheck(list, 0, element1);
        this.getAndCheck(list, 1, element2);
    }

    @Test
    public void testObjectRequiresEvaluatedParametersTrue() {
        final Object element1 = 111;
        final Object element2 = "222";

        final List<Object> parameters = Lists.of(
                element1,
                element2
        );
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        this.function(
                                ExpressionFunctionKind.CONVERT_PARAMETERS,
                                ExpressionFunctionKind.EVALUATE_PARAMETERS
                        ),
                        CONTEXT
                )
        );

        this.sizeAndCheck(list, 2);
        this.getAndCheck(list, 0, element1);
        this.getAndCheck(list, 1, element2);
    }

    @Test
    public void testExpressionRequiresEvaluatedParametersTrue() {
        final ValueExpression<?> element1 = Expression.value(111);
        final ValueExpression<?> element2 = Expression.value("222");

        final List<Object> parameters = Lists.of(
                element1,
                element2
        );
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        this.function(
                                ExpressionFunctionKind.CONVERT_PARAMETERS,
                                ExpressionFunctionKind.EVALUATE_PARAMETERS
                        ),
                        CONTEXT
                )
        );

        this.sizeAndCheck(list, 2);
        this.getAndCheck(list, 0, element1.value());
        this.getAndCheck(list, 1, element2.value());
    }

    @Test
    public void testObjectResolveReferencesTrue() {
        final Object element1 = 111;
        final Object element2 = "222";

        final List<Object> parameters = Lists.of(
                element1,
                element2
        );
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        this.function(
                                ExpressionFunctionKind.CONVERT_PARAMETERS,
                                ExpressionFunctionKind.EVALUATE_PARAMETERS
                        ),
                        CONTEXT
                )
        );

        this.sizeAndCheck(list, 2);
        this.getAndCheck(list, 0, element1);
        this.getAndCheck(list, 1, element2);
    }

    @Test
    public void testObjectResolveReferencesTrueCached() {
        final Object element1 = 111;
        final Object element2 = "222";

        final List<Object> parameters = Lists.of(
                element1,
                element2
        );
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        this.function(
                                ExpressionFunctionKind.CONVERT_PARAMETERS,
                                ExpressionFunctionKind.EVALUATE_PARAMETERS
                        ),
                        CONTEXT
                )
        );

        this.sizeAndCheck(list, 2);
        this.getAndCheck(list, 0, element1);
        this.getAndCheck(list, 1, element2);

        this.getAndCheck(list, 0, element1);
        this.getAndCheck(list, 1, element2);
    }

    @Test
    public void testExpressionReferenceResolveReferencesTrue() {
        final ExpressionReference element1 = REFERENCE;
        final ValueExpression<?> value1 = Expression.value(111);
        final ValueExpression<?> element2 = Expression.value("222");

        final List<Object> parameters = Lists.of(
                element1,
                element2
        );
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        this.function(
                                ExpressionFunctionKind.CONVERT_PARAMETERS,
                                ExpressionFunctionKind.RESOLVE_REFERENCES
                        ),
                        new FakeExpressionEvaluationContext() {

                            @Override
                            public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                                          final Object value) {
                                return Cast.to(value);
                            }

                            @Override
                            public Expression referenceOrFail(final ExpressionReference r) {
                                assertSame(REFERENCE, r);
                                return value1;
                            }
                        }
                )
        );

        this.sizeAndCheck(list, 2);
        this.getAndCheck(list, 0, value1);
        this.getAndCheck(list, 1, element2);
    }

    @Test
    public void testExpressionReferenceResolveReferencesTrueCached() {
        final ExpressionReference element1 = REFERENCE;
        final ValueExpression<?> value1 = Expression.value(111);
        final ValueExpression<?> element2 = Expression.value("222");

        final List<Object> parameters = Lists.of(
                element1,
                element2
        );
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        this.function(
                                ExpressionFunctionKind.CONVERT_PARAMETERS,
                                ExpressionFunctionKind.RESOLVE_REFERENCES
                        ),
                        new FakeExpressionEvaluationContext() {

                            @Override
                            public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                                          final Object value) {
                                return Cast.to(value);
                            }

                            @Override
                            public Expression referenceOrFail(final ExpressionReference r) {
                        if (this.invoked) {
                            throw new IllegalStateException("Value must not have been cached");
                        }
                        this.invoked = true;
                        assertSame(REFERENCE, r);
                        return value1;
                    }

                    private boolean invoked;
                }
                )
        );

        this.sizeAndCheck(list, 2);
        this.getAndCheck(list, 0, value1);
        this.getAndCheck(list, 1, element2);

        this.getAndCheck(list, 0, value1);
        this.getAndCheck(list, 1, element2);
    }

    @Test
    public void testExpressionReferenceResolveReferencesTrueRequireParametersEvaluated() {
        final ExpressionReference element1 = REFERENCE;
        final ValueExpression<?> value1 = Expression.value(111);
        final ValueExpression<?> element2 = Expression.value("222");

        final List<Object> parameters = Lists.of(
                element1,
                element2
        );
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        this.function(
                                ExpressionFunctionKind.CONVERT_PARAMETERS,
                                ExpressionFunctionKind.EVALUATE_PARAMETERS,
                                ExpressionFunctionKind.RESOLVE_REFERENCES
                        ),
                        new FakeExpressionEvaluationContext() {

                            @Override
                            public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                                          final Object value) {
                                return Cast.to(value);
                            }

                            @Override
                            public Expression referenceOrFail(final ExpressionReference r) {
                                assertSame(REFERENCE, r);
                                return value1;
                            }
                        }
                )
        );

        this.sizeAndCheck(list, 2);
        this.getAndCheck(list, 0, value1.value());
        this.getAndCheck(list, 1, element2.value());
    }

    @Test
    public void testExpressionReferenceResolveReferencesTrueRequireParametersEvaluatedCached() {
        final ExpressionReference element1 = REFERENCE;
        final ValueExpression<?> value1 = Expression.value(111);
        final ValueExpression<?> element2 = Expression.value("222");

        final List<Object> parameters = Lists.of(
                element1,
                element2
        );
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        this.function(
                                ExpressionFunctionKind.CONVERT_PARAMETERS,
                                ExpressionFunctionKind.EVALUATE_PARAMETERS,
                                ExpressionFunctionKind.RESOLVE_REFERENCES
                        ),
                        new FakeExpressionEvaluationContext() {

                            @Override
                            public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                                          final Object value) {
                                return Cast.to(value);
                            }

                            @Override
                            public Expression referenceOrFail(final ExpressionReference r) {
                                assertSame(REFERENCE, r);
                                return value1;
                            }
                        }
                )
        );

        this.sizeAndCheck(list, 2);
        this.getAndCheck(list, 0, value1.value());
        this.getAndCheck(list, 1, element2.value());

        this.getAndCheck(list, 0, value1.value());
        this.getAndCheck(list, 1, element2.value());
    }

    @Test
    public void testParameterValueNotConverted() {
        final List<Object> parameters = Lists.of(
                "111",
                "222"
        );
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        this.function(ExpressionFunctionKind.EVALUATE_PARAMETERS),
                        ExpressionEvaluationContexts.fake()
                )
        );

        this.sizeAndCheck(list, 2);
        this.getAndCheck(list, 0, "111");
        this.getAndCheck(list, 1, "222");
    }

    @Test
    public void testParameterValueConverted() {
        final List<Object> parameters = Lists.of(
                "111",
                "222"
        );
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        this.function(
                                ExpressionFunctionKind.CONVERT_PARAMETERS
                        ),
                        new FakeExpressionEvaluationContext() {

                            @Override
                            public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                                          final Object value) {
                                return parameter.type().cast(Integer.parseInt((String) value));
                            }
                        }
                )
        );

        this.sizeAndCheck(list, 2);
        this.getAndCheck(list, 0, 111);
        this.getAndCheck(list, 1, 222);
    }

    @Test
    public void testParameterValueConvertedCached() {
        final List<Object> parameters = Lists.of(
                "111",
                "222"
        );
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        this.function(
                                ExpressionFunctionKind.CONVERT_PARAMETERS
                        ),
                        new FakeExpressionEvaluationContext() {

                            @Override
                            public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                                          final Object value) {
                                return parameter.type().cast(Integer.parseInt((String) value));
                            }
                        }
                )
        );

        this.sizeAndCheck(list, 2);
        this.getAndCheck(list, 0, 111);
        this.getAndCheck(list, 0, 111);
        this.getAndCheck(list, 1, 222);
        this.getAndCheck(list, 1, 222);
    }

    @Test
    public void testGetFailedExpressionEvaluationExceptionTranslated() {
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        Lists.of(
                                Expression.divide(
                                        Expression.value(KIND.one()),
                                        Expression.value(KIND.zero())
                                )
                        ),
                        function(
                                Lists.of(
                                        ExpressionFunctionParameterName.with("expression")
                                                .variable(Object.class)
                                ),
                                ExpressionFunctionKind.CONVERT_PARAMETERS,
                                ExpressionFunctionKind.EVALUATE_PARAMETERS
                        ),
                        new FakeExpressionEvaluationContext() {

                            @Override
                            public <T> T convertOrFail(final Object value,
                                                       final Class<T> target) {
                                return target.cast(value);
                            }

                            @Override
                            public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                                          final Object value) {
                                return Cast.to(value);
                            }

                            @Override
                            public Object handleException(final RuntimeException exception) {
                                return "@@@" + exception.getMessage();
                            }
                        }
                )
        );

        this.getAndCheck(list, 0, "@@@Division by zero");
    }

    @Test
    public void testGetFailedReferenceNotFoundExceptionTranslated() {
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        Lists.of(
                                new ExpressionReference() {
                                    @Override
                                    public String toString() {
                                        return "TestReference";
                                    }
                                }
                        ),
                        function(
                                Lists.of(
                                        ExpressionFunctionParameterName.with("reference")
                                                .variable(Object.class)
                                ),
                                ExpressionFunctionKind.CONVERT_PARAMETERS,
                                ExpressionFunctionKind.RESOLVE_REFERENCES
                        ),
                        new FakeExpressionEvaluationContext() {

                            @Override
                            public Optional<Object> reference(final ExpressionReference reference) {
                                return Optional.empty();
                            }

                            @Override
                            public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                                          final Object value) {
                                return Cast.to(value);
                            }

                            @Override
                            public Object handleException(final RuntimeException exception) {
                                return "@@@" + exception.getMessage();
                            }
                        }
                )
        );

        this.getAndCheck(list, 0, "@@@Reference not found: TestReference");
    }

    @Test
    public void testGetFailedConvertExceptionTranslated() {
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        Lists.of(
                                "this parameter convert will throw",
                                "222"
                        ),
                        function(
                                Lists.of(VARIABLE),
                                ExpressionFunctionKind.CONVERT_PARAMETERS,
                                ExpressionFunctionKind.EVALUATE_PARAMETERS,
                                ExpressionFunctionKind.RESOLVE_REFERENCES
                        ),
                        CONTEXT_PARSE_INT
                )
        );

        this.getAndCheck(list, 0, "@@@For input string: \"this parameter convert will throw\"");
        this.getAndCheck(list, 1, 222);
    }

    @Test
    public void testGetFailedConvert() {
        final String message = "Message 123";

        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        Lists.of(
                                "this value is never returned"
                        ),
                        function(
                                Lists.of(VARIABLE),
                                ExpressionFunctionKind.CONVERT_PARAMETERS,
                                ExpressionFunctionKind.EVALUATE_PARAMETERS,
                                ExpressionFunctionKind.RESOLVE_REFERENCES
                        ),
                        new FakeExpressionEvaluationContext() {

                            @Override
                            public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                                          final Object value) {
                                throw new NumberFormatException(message);
                            }

                            @Override
                            public Object handleException(final RuntimeException exception) {
                                throw exception;
                            }
                        }
                )
        );

        final NumberFormatException thrown = assertThrows(
                NumberFormatException.class,
                () -> {
                    list.get(0);
                }
        );
        this.checkEquals(message, thrown.getMessage(), "message");
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<ExpressionEvaluationContextPrepareParametersListNonFlattened> type() {
        return ExpressionEvaluationContextPrepareParametersListNonFlattened.class;
    }
}
