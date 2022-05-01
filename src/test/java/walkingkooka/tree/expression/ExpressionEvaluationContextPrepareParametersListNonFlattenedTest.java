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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class ExpressionEvaluationContextPrepareParametersListNonFlattenedTest extends ExpressionEvaluationContextPrepareParametersListTestCase<ExpressionEvaluationContextPrepareParametersListNonFlattened> {

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
                        this.function(),
                        CONTEXT
                )
        );

        this.sizeAndCheck(list, 2);
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
                        this.function(),
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
                        this.function(),
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
                        this.function(ExpressionFunctionKind.REQUIRES_EVALUATED_PARAMETERS),
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
                        this.function(ExpressionFunctionKind.REQUIRES_EVALUATED_PARAMETERS),
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
                        this.function(ExpressionFunctionKind.REQUIRES_EVALUATED_PARAMETERS),
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
                        this.function(ExpressionFunctionKind.REQUIRES_EVALUATED_PARAMETERS),
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
                        this.function(ExpressionFunctionKind.RESOLVE_REFERENCES),
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
                        this.function(ExpressionFunctionKind.RESOLVE_REFERENCES),
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
                                ExpressionFunctionKind.REQUIRES_EVALUATED_PARAMETERS,
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
                                ExpressionFunctionKind.REQUIRES_EVALUATED_PARAMETERS,
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
    public void testParameterValueConverted() {
        final List<Object> parameters = Lists.of(
                "111",
                "222"
        );
        final ExpressionEvaluationContextPrepareParametersListNonFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        this.function(),
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
                        this.function(),
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

    // ClassTesting....................................................................................................

    @Override
    public Class<ExpressionEvaluationContextPrepareParametersListNonFlattened> type() {
        return ExpressionEvaluationContextPrepareParametersListNonFlattened.class;
    }
}
