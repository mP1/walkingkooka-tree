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
import walkingkooka.collect.set.Sets;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;

import java.util.List;

public abstract class ExpressionEvaluationContextPrepareParametersListTestCase2<T extends ExpressionEvaluationContextPrepareParametersList> extends ExpressionEvaluationContextPrepareParametersListTestCase<T> {

    ExpressionEvaluationContextPrepareParametersListTestCase2() {
        super();
    }

    // Tests...........................................................................................................

    @Test
    public final void testGetRequired() {
        final T list = this.createList(
                REQUIRED,
                123
        );
        this.getAndCheck(list, 0, 123);
        this.sizeAndCheck2(list, 1);
    }

    @Test
    public final void testGetRequiredExpression() {
        final T list = this.createList(
                REQUIRED,
                EXPRESSION
        );
        this.getAndCheck(list, 0, EXPRESSION);
        this.sizeAndCheck2(list, 1);
    }

    @Test
    public final void testGetRequiredExpressionEvaluated() {
        final T list = this.createList(
                REQUIRED.setKinds(
                        Sets.of(ExpressionFunctionParameterKind.EVALUATE)
                ),
                Expression.value(EXPRESSION_NUMBER)
        );
        this.getAndCheck(list, 0, EXPRESSION_NUMBER);
        this.sizeAndCheck2(list, 1);
    }

    @Test
    public final void testGetRequiredReference() {
        final T list = this.createList(
                REQUIRED.setKinds(
                        Sets.of(ExpressionFunctionParameterKind.RESOLVE_REFERENCES)
                ),
                REFERENCE,
                this.createContextWithReference(
                        REFERENCE,
                        EXPRESSION_NUMBER
                )
        );
        this.getAndCheck(list, 0, EXPRESSION_NUMBER);
        this.sizeAndCheck2(list, 1);
    }

    @Test
    public final void testGetRequiredReferenceFails() {
        final T list = this.createList(
                REQUIRED.setKinds(
                        Sets.of(ExpressionFunctionParameterKind.RESOLVE_REFERENCES)
                ),
                REFERENCE,
                this.createContextWithReference(
                        REFERENCE,
                        null
                )
        );
        this.getAndCheck(list, 0, "@@@Reference not found: *reference*");
        this.sizeAndCheck2(list, 1);
    }

    @Test
    public final void testGetRequiredConverts() {
        final T list = this.createList(
                REQUIRED.setKinds(
                        Sets.of(ExpressionFunctionParameterKind.CONVERT)
                ),
                "123",
                this.createContextWhichConverts("123", 123)
        );
        this.getAndCheck(list, 0, 123);
        this.sizeAndCheck2(list, 1);
    }

    @Test
    public final void testGetRequiredConvertFails() {
        final T list = this.createList(
                REQUIRED.setKinds(
                        Sets.of(ExpressionFunctionParameterKind.CONVERT)
                ),
                "Fails!",
                this.createContextWhichConvertFails()
        );
        this.getAndCheck(list, 0, "@@@Unable to convert Fails! to Integer");
        this.getAndCheck(list, 0, "@@@Unable to convert Fails! to Integer");
    }

    @Test
    public final void testGetOptionalPresent() {
        final T list = this.createList(
                OPTIONAL,
                123
        );
        this.getAndCheck(list, 0, 123);
        this.sizeAndCheck2(list, 1);
    }

    @Test
    public final void testGetOptionalPresentExpression() {
        final T list = this.createList(
                OPTIONAL,
                EXPRESSION
        );
        this.getAndCheck(list, 0, EXPRESSION);
        this.sizeAndCheck2(list, 1);
    }

    @Test
    public final void testGetOptionalPresentExpressionEvaluated() {
        final T list = this.createList(
                OPTIONAL.setKinds(
                        Sets.of(
                                ExpressionFunctionParameterKind.EVALUATE
                        )
                ),
                EXPRESSION
        );
        this.getAndCheck(list, 0, EXPRESSION_NUMBER);
        this.sizeAndCheck2(list, 1);
    }

    @Test
    public final void testGetOptionalPresentExpressionEvaluatedFails() {
        final T list = this.createList(
                OPTIONAL.setKinds(
                        Sets.of(
                                ExpressionFunctionParameterKind.EVALUATE
                        )
                ),
                Expression.divide(
                        Expression.value(EXPRESSION_NUMBER),
                        Expression.value(EXPRESSION_NUMBER_KIND.zero())
                ),
                new FakeExpressionEvaluationContext() {

                    @Override
                    public boolean isText(final Object value) {
                        return false;
                    }

                    @Override
                    public <TT> TT convertOrFail(final Object value,
                                                 final Class<TT> target) {
                        return target.cast(value);
                    }

                    @Override
                    public Object handleException(final RuntimeException exception) {
                        return "@@@" + exception.getMessage();
                    }
                }
        );
        this.getAndCheck(list, 0, "@@@Division by zero");
        this.sizeAndCheck2(list, 1);
    }

    @Test
    public final void testGetOptionalAbsent() {
        final T list = this.createList(
                OPTIONAL
        );
        this.getFails(list, 0);
        this.sizeAndCheck2(list, 0);
    }

    @Test
    public final void testGetVariableEmpty() {
        final T list = this.createList(
                VARIABLE
        );
        this.getFails(list, 0);
        this.sizeAndCheck2(list, 0);
    }

    @Test
    public final void testGetVariable1() {
        final T list = this.createList(
                VARIABLE,
                100
        );
        this.getAndCheck(list, 0, 100);
        this.sizeAndCheck2(list, 1);
    }

    @Test
    public final void testGetVariable2() {
        final T list = this.createList(
                VARIABLE,
                100,
                200
        );
        this.getAndCheck(list, 0, 100);
        this.getAndCheck(list, 1, 200);
        this.sizeAndCheck2(list, 2);
    }

    @Test
    public final void testGetVariable3() {
        final T list = this.createList(
                VARIABLE,
                100,
                200,
                300
        );
        this.getAndCheck(list, 0, 100);
        this.getAndCheck(list, 1, 200);
        this.getAndCheck(list, 2, 300);
        this.sizeAndCheck2(list, 3);
    }

    @Test
    public final void testGetRequiredOptionalAndVariable() {
        final T list = this.createList(
                Lists.of(
                        REQUIRED,
                        OPTIONAL,
                        VARIABLE
                ),
                Lists.of(
                        100,
                        200,
                        300
                )
        );
        this.getAndCheck(list, 0, 100);
        this.getAndCheck(list, 1, 200);
        this.getAndCheck(list, 2, 300);
        this.sizeAndCheck2(list, 3);
    }

    @Test
    public final void testGetVariableExpression() {
        final T list = this.createList(
                VARIABLE,
                EXPRESSION
        );
        this.getAndCheck(list, 0, EXPRESSION);
        this.sizeAndCheck2(list, 1);
    }

    @Test
    public final void testGetVariableExpressionEvaluated() {
        final T list = this.createList(
                VARIABLE.setKinds(
                        Sets.of(
                                ExpressionFunctionParameterKind.EVALUATE
                        )
                ),
                EXPRESSION
        );
        this.getAndCheck(list, 0, EXPRESSION_NUMBER);
        this.sizeAndCheck2(list, 1);
    }

    @Test
    public final void testGetVariableExpressionEvaluated2() {
        final T list = this.createList(
                VARIABLE.setKinds(
                        Sets.of(
                                ExpressionFunctionParameterKind.EVALUATE
                        )
                ),
                EXPRESSION,
                Expression.value(222)
        );
        this.getAndCheck(list, 0, EXPRESSION_NUMBER);
        this.getAndCheck(list, 1, 222);
        this.sizeAndCheck2(list, 2);
    }

    @Test
    public final void testGetVariableExpressionEvaluatedAndConverted() {
        final T list = this.createList(
                VARIABLE.setKinds(
                        Sets.of(
                                ExpressionFunctionParameterKind.CONVERT,
                                ExpressionFunctionParameterKind.EVALUATE
                        )
                ),
                Lists.of(
                        Expression.value(111.0),
                        Expression.value("222"),
                        "333"
                ),
                new FakeExpressionEvaluationContext() {

                    @Override
                    public <TT> TT prepareParameter(final ExpressionFunctionParameter<TT> parameter,
                                                    final Object value) {
                        return this.convertOrFail(value, parameter.type());
                    }

                    @Override
                    public <TT> TT convertOrFail(final Object value,
                                                 final Class<TT> target) {
                        if (Double.valueOf(111.0).equals(value)) {
                            return Cast.to(value);
                        }
                        if ("222".equals(value)) {
                            return Cast.to(222.0);
                        }
                        if ("333".equals(value)) {
                            return Cast.to(333.0);
                        }
                        throw new UnsupportedOperationException("Unable to convert " + value + " to " + target.getName());
                    }
                }
        );
        this.getAndCheck(list, 0, 111.0);
        this.getAndCheck(list, 1, 222.0);
        this.getAndCheck(list, 2, 333.0);
        this.sizeAndCheck2(list, 3);
    }

    // helpers..........................................................................................................

    final T createList(
            final ExpressionFunctionParameter<?> parameter,
            final Object... values) {
        return this.createList(
                Lists.of(parameter),
                Lists.of(values)
        );
    }

    final T createList(
            final ExpressionFunctionParameter<?> parameter,
            final Object value,
            final ExpressionEvaluationContext context) {
        return this.createList(
                Lists.of(parameter),
                Lists.of(value),
                context
        );
    }

    final T createList(
            final List<ExpressionFunctionParameter<?>> parameters,
            final List<Object> values) {
        return this.createList(
                parameters,
                values,
                ExpressionEvaluationContexts.fake()
        );
    }

    final T createList(
            final ExpressionFunctionParameter<?> parameter,
            final List<Object> values,
            final ExpressionEvaluationContext context) {
        return this.createList(
                Lists.of(parameter),
                values,
                context
        );
    }

    abstract T createList(
            final List<ExpressionFunctionParameter<?>> parameters,
            final List<Object> values,
            final ExpressionEvaluationContext context
    );

    abstract void sizeAndCheck2(final List<?> list,
                                final int size);
}
