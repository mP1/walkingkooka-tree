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
import walkingkooka.collect.list.ListTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionContext;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.tree.expression.function.FakeExpressionFunction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class ExpressionEvaluationContextPrepareParametersListTest implements ClassTesting<ExpressionEvaluationContextPrepareParametersList>,
        ListTesting {

    private final static ExpressionEvaluationContext CONTEXT = new FakeExpressionEvaluationContext() {

        @Override
        public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                      final Object value) {
            return Cast.to(value);
        }
    };

    private final static ExpressionReference REFERENCE = new ExpressionReference() {
        @Override
        public String toString() {
            return "*reference*";
        }
    };

    @Test
    public void testSize() {
        final Object element1 = 111;
        final Object element2 = Expression.value("222");

        final List<Object> parameters = Lists.of(
                element1,
                element2
        );
        final ExpressionEvaluationContextPrepareParametersList list = ExpressionEvaluationContextPrepareParametersList.with(
                parameters,
                this.function(false, false),
                CONTEXT
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
        final ExpressionEvaluationContextPrepareParametersList list = ExpressionEvaluationContextPrepareParametersList.with(
                parameters,
                this.function(false, false),
                CONTEXT
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
        final ExpressionEvaluationContextPrepareParametersList list = ExpressionEvaluationContextPrepareParametersList.with(
                parameters,
                this.function(false, false),
                CONTEXT
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
        final ExpressionEvaluationContextPrepareParametersList list = ExpressionEvaluationContextPrepareParametersList.with(
                parameters,
                this.function(true, false),
                CONTEXT
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
        final ExpressionEvaluationContextPrepareParametersList list = ExpressionEvaluationContextPrepareParametersList.with(
                parameters,
                this.function(true, false),
                CONTEXT
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
        final ExpressionEvaluationContextPrepareParametersList list = ExpressionEvaluationContextPrepareParametersList.with(
                parameters,
                this.function(true, false),
                CONTEXT
        );

        this.sizeAndCheck(list, 2);
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
        final ExpressionEvaluationContextPrepareParametersList list = ExpressionEvaluationContextPrepareParametersList.with(
                parameters,
                this.function(false, true),
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
        );

        this.sizeAndCheck(list, 2);
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
        final ExpressionEvaluationContextPrepareParametersList list = ExpressionEvaluationContextPrepareParametersList.with(
                parameters,
                this.function(true, true),
                new FakeExpressionEvaluationContext() {

                    public List<Object> XXXprepareParameters(final ExpressionFunction<?, ExpressionFunctionContext> function,
                                                             final List<Object> parameters) {
                        return parameters;
                    }

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
        );

        this.sizeAndCheck(list, 2);
        this.getAndCheck(list, 0, value1.value());
        this.getAndCheck(list, 1, element2.value());
    }

    @Test
    public void testParameterValueConverted() {
        final List<Object> parameters = Lists.of(
                "111",
                "222"
        );
        final ExpressionEvaluationContextPrepareParametersList list = ExpressionEvaluationContextPrepareParametersList.with(
                parameters,
                this.function(false, false),
                new FakeExpressionEvaluationContext() {

                    @Override
                    public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                                  final Object value) {
                        return parameter.type().cast(Integer.parseInt((String) value));
                    }
                }
        );

        this.sizeAndCheck(list, 2);
        this.getAndCheck(list, 0, 111);
        this.getAndCheck(list, 1, 222);
    }

    private ExpressionFunction<Void, ExpressionFunctionContext> function(final boolean requiresEvaluatedParameters,
                                                                         final boolean resolveReferences) {
        return this.function(
                Lists.of(ExpressionFunctionParameterName.with("parameters").variable(Object.class)),
                requiresEvaluatedParameters,
                resolveReferences
        );
    }

    private ExpressionFunction<Void, ExpressionFunctionContext> function(final List<ExpressionFunctionParameter<?>> parameters,
                                                                         final boolean requiresEvaluatedParameters,
                                                                         final boolean resolveReferences) {
        return new FakeExpressionFunction<>() {

            @Override
            public List<ExpressionFunctionParameter<?>> parameters() {
                return parameters;
            }

            @Override
            public boolean requiresEvaluatedParameters() {
                return requiresEvaluatedParameters;
            }

            @Override
            public boolean resolveReferences() {
                return resolveReferences;
            }

            @Override
            public String toString() {
                return "parameters: " + parameters + " requiresEvaluatedParameters: " + requiresEvaluatedParameters + " resolveReferences: " + resolveReferences;
            }
        };
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<ExpressionEvaluationContextPrepareParametersList> type() {
        return ExpressionEvaluationContextPrepareParametersList.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
