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
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;
import java.util.Optional;

public final class ExpressionEvaluationContextPrepareParametersListFlattenedTest extends ExpressionEvaluationContextPrepareParametersListTestCase2<ExpressionEvaluationContextPrepareParametersListFlattened> {

    @Test
    public void testFlatten() {
        this.flattenAndCheck(
                Lists.of(
                        FLATTEN
                ),
                Lists.of(
                        10
                ),
                10
        );
    }

    @Test
    public void testFlatten2() {
        this.flattenAndCheck(
                Lists.of(
                        REQUIRED,
                        FLATTEN
                ),
                Lists.of(
                        10,
                        20
                ),
                20
        );
    }

    @Test
    public void testFlattenIncludesList() {
        this.flattenAndCheck(
                Lists.of(
                        REQUIRED,
                        FLATTEN
                ),
                Lists.of(
                        10,
                        Lists.of(20)
                ),
                20
        );
    }

    @Test
    public void testFlattenIncludesList2() {
        this.flattenAndCheck(
                Lists.of(
                        REQUIRED,
                        FLATTEN
                ),
                Lists.of(
                        10,
                        Lists.of(20, 30)
                ),
                20,
                30
        );
    }

    @Test
    public void testFlattenIncludesNestedLists() {
        this.flattenAndCheck(
                Lists.of(
                        REQUIRED,
                        FLATTEN
                ),
                Lists.of(
                        10,
                        Lists.of(
                                20,
                                Lists.of(30, 40)
                        )
                ),
                20,
                30,
                40
        );
    }

    @Test
    public void testFlattenConvertIgnored() {
        this.flattenAndCheck(
                Lists.of(
                        REQUIRED,
                        FLATTEN.setKinds(
                                Sets.of(
                                        ExpressionFunctionParameterKind.CONVERT
                                )
                        )
                ),
                Lists.of(
                        10,
                        20
                ),
                20
        );
    }

    @Test
    public void testFlattenReferences() {
        this.flattenAndCheck(
                Lists.of(
                        REQUIRED,
                        FLATTEN
                ),
                Lists.of(
                        10,
                        REFERENCE
                ),

                REFERENCE
        );
    }

    @Test
    public void testFlattenReferencesResolved() {
        this.flattenAndCheck(
                Lists.of(
                        REQUIRED,
                        FLATTEN.setKinds(Sets.of(ExpressionFunctionParameterKind.RESOLVE_REFERENCES))
                ),
                Lists.of(
                        10,
                        REFERENCE
                ),
                Lists.of(
                        20
                ),
                this.createContextWithReference(
                        REFERENCE,
                        20
                )
        );
    }

    @Test
    public void testFlattenReferencesResolved2() {
        this.flattenAndCheck(
                Lists.of(
                        REQUIRED,
                        FLATTEN.setKinds(Sets.of(ExpressionFunctionParameterKind.RESOLVE_REFERENCES))
                ),
                Lists.of(
                        10,
                        REFERENCE
                ),
                Lists.of(
                        20,
                        30
                ),
                this.createContextWithReference(
                        REFERENCE,
                        Lists.of(20, 30)
                )
        );
    }

    @Test
    public void testFlattenReferencesResolved3() {
        this.flattenAndCheck(
                Lists.of(
                        REQUIRED,
                        FLATTEN.setKinds(Sets.of(ExpressionFunctionParameterKind.RESOLVE_REFERENCES))
                ),
                Lists.of(
                        10,
                        REFERENCE
                ),
                Lists.of(
                        20,
                        30,
                        40
                ),
                this.createContextWithReference(
                        REFERENCE,
                        Lists.of(20, 30, 40)
                )
        );
    }

    @Test
    public void testFlattenExpression() {
        this.flattenAndCheck(
                Lists.of(
                        REQUIRED,
                        FLATTEN
                ),
                Lists.of(
                        10,
                        EXPRESSION
                ),
                EXPRESSION
        );
    }

    @Test
    public void testFlattenExpressionEvaluated() {
        this.flattenAndCheck(
                Lists.of(
                        REQUIRED,
                        FLATTEN.setKinds(Sets.of(ExpressionFunctionParameterKind.EVALUATE))
                ),
                Lists.of(
                        10,
                        EXPRESSION
                ),
                EXPRESSION_NUMBER
        );
    }

    @Test
    public void testFlattenExpressionEvaluatedFails() {
        this.flattenAndCheck(
                Lists.of(
                        REQUIRED,
                        FLATTEN.setKinds(Sets.of(ExpressionFunctionParameterKind.EVALUATE))
                ),
                Lists.of(
                        10,
                        Expression.divide(
                                Expression.value(EXPRESSION_NUMBER),
                                Expression.value(EXPRESSION_NUMBER_KIND.zero())
                        )
                ),
                List.of("@@@Division by zero"),
                new FakeExpressionEvaluationContext() {

                    @Override
                    public boolean isText(final Object value) {
                        return false;
                    }

                    @Override
                    public <T> T convertOrFail(final Object value,
                                               final Class<T> target) {
                        return target.cast(value);
                    }

                    @Override
                    public Object handleException(final RuntimeException exception) {
                        return "@@@" + exception.getMessage();
                    }
                }
        );
    }

    @Test
    public void testFlattenExpressionEvaluatedReferencesResolved() {
        this.flattenAndCheck(
                Lists.of(
                        REQUIRED,
                        FLATTEN.setKinds(
                                Sets.of(
                                        ExpressionFunctionParameterKind.EVALUATE,
                                        ExpressionFunctionParameterKind.RESOLVE_REFERENCES
                                )
                        )
                ),
                Lists.of(
                        10,
                        EXPRESSION
                ),
                EXPRESSION_NUMBER
        );
    }

    private void flattenAndCheck(final List<ExpressionFunctionParameter<?>> parameters,
                                 final List<Object> values,
                                 final Object... flatten) {
        this.flattenAndCheck(
                parameters,
                values,
                Lists.of(flatten),
                ExpressionEvaluationContexts.fake()
        );
    }

    private void flattenAndCheck(final List<ExpressionFunctionParameter<?>> parameters,
                                 final List<Object> values,
                                 final List<Object> flatten,
                                 final ExpressionEvaluationContext context) {
        final ExpressionEvaluationContextPrepareParametersListFlattened list = this.createList2(
                parameters,
                values,
                context
        );

        this.checkEquals(
                flatten,
                list.flattenIfNecessary()
        );
    }

    // get.............................................................................................................

    @Test
    public void testGetFlattenRequiredConvertFails() {
        final ExpressionEvaluationContextPrepareParametersListFlattened list = this.createList(
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
    public void testGetFlatten() {
        final ExpressionEvaluationContextPrepareParametersListFlattened list = this.createList2(
                Lists.of(
                        FLATTEN
                ),
                Lists.of(
                        10
                )
        );
        this.getAndCheck(list, 0, 10);
        this.sizeAndCheck(list, 1);
    }

    @Test
    public void testGetFlattenTwice() {
        final ExpressionEvaluationContextPrepareParametersListFlattened list = this.createList2(
                Lists.of(
                        FLATTEN
                ),
                Lists.of(
                        10
                )
        );
        this.getAndCheck(list, 0, 10);
        this.getAndCheck(list, 0, 10);
        this.sizeAndCheck(list, 1);
    }

    @Test
    public void testGetFlatten2() {
        final ExpressionEvaluationContextPrepareParametersListFlattened list = this.createList2(
                Lists.of(
                        REQUIRED,
                        FLATTEN
                ),
                Lists.of(
                        10,
                        20
                )
        );
        this.getAndCheck(list, 0, 10);
        this.getAndCheck(list, 1, 20);
        this.sizeAndCheck(list, 2);
    }

    @Test
    public void testGetFlatten2Twice() {
        final ExpressionEvaluationContextPrepareParametersListFlattened list = this.createList2(
                Lists.of(
                        REQUIRED,
                        FLATTEN
                ),
                Lists.of(
                        10,
                        20
                )
        );
        this.getAndCheck(list, 0, 10);
        this.getAndCheck(list, 1, 20);

        this.getAndCheck(list, 0, 10);
        this.getAndCheck(list, 1, 20);

        this.sizeAndCheck(list, 2);
    }

    @Test
    public void testGetFlattenList() {
        final ExpressionEvaluationContextPrepareParametersListFlattened list = this.createList2(
                Lists.of(
                        REQUIRED,
                        FLATTEN
                ),
                Lists.of(
                        10,
                        Lists.of(20)
                )
        );
        this.getAndCheck(list, 0, 10);
        this.getAndCheck(list, 1, 20);
        this.sizeAndCheck(list, 2);
    }

    @Test
    public void testGetFlattenList2() {
        final ExpressionEvaluationContextPrepareParametersListFlattened list = this.createList2(
                Lists.of(
                        REQUIRED,
                        FLATTEN
                ),
                Lists.of(
                        10,
                        Lists.of(20, 30)
                )
        );
        this.getAndCheck(list, 0, 10);
        this.getAndCheck(list, 1, 20);
        this.getAndCheck(list, 2, 30);
        this.sizeAndCheck(list, 3);
    }

    @Test
    public void testGetFlattenConverted() {
        final ExpressionEvaluationContextPrepareParametersListFlattened list = this.createList2(
                Lists.of(
                        FLATTEN.setKinds(
                                Sets.of(
                                        ExpressionFunctionParameterKind.CONVERT,
                                        ExpressionFunctionParameterKind.FLATTEN
                                )
                        )
                ),
                Lists.of(
                        "Ten"
                ),
                this.createContextWhichConverts("Ten", 10)
        );
        this.getAndCheck(list, 0, 10);
        this.sizeAndCheck(list, 1);
    }

    @Test
    public void testGetFlattenConverted2() {
        final ExpressionEvaluationContextPrepareParametersListFlattened list = this.createList2(
                Lists.of(
                        REQUIRED,
                        FLATTEN.setKinds(
                                Sets.of(
                                        ExpressionFunctionParameterKind.CONVERT,
                                        ExpressionFunctionParameterKind.FLATTEN
                                )
                        )
                ),
                Lists.of(
                        10,
                        "Twenty"
                ),
                this.createContextWhichConverts("Twenty", 20)
        );
        this.getAndCheck(list, 0, 10);
        this.getAndCheck(list, 1, 20);
        this.sizeAndCheck(list, 2);
    }

    @Test
    public void testGetFlattenReferenceResolvedExpressionEvaluatedConverted() {
        final ExpressionEvaluationContextPrepareParametersListFlattened list = this.createList2(
                Lists.of(
                        REQUIRED,
                        FLATTEN.setKinds(
                                ExpressionFunctionParameterKind.CONVERT_EVALUATE_FLATTEN_RESOLVE_REFERENCES
                        )
                ),
                Lists.of(
                        10,
                        REFERENCE
                ),
                new FakeExpressionEvaluationContext() {

                    @Override
                    public Optional<Optional<Object>> reference(final ExpressionReference reference) {
                        checkEquals(REFERENCE, reference);

                        return Optional.of(
                                Optional.of(
                                        Expression.value("Twenty"
                                        )
                                )
                        );
                    }

                    @Override
                    public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                                  final Object value) {
                        return this.convertOrFail(value, parameter.type());
                    }

                    @Override
                    public <T> T convertOrFail(final Object value,
                                               final Class<T> target) {
                        if ("Twenty".equals(value)) {
                            return Cast.to(20);
                        }
                        throw new UnsupportedOperationException("Unable to convert " + value + " to " + target.getName());
                    }
                }
        );
        this.getAndCheck(list, 0, 10);
        this.getAndCheck(list, 1, 20);
        this.sizeAndCheck(list, 2);
    }

    @Override
    ExpressionEvaluationContextPrepareParametersListFlattened createList(final List<ExpressionFunctionParameter<?>> parameters,
                                                                         final List<Object> values,
                                                                         final ExpressionEvaluationContext context) {
        final List<ExpressionFunctionParameter<?>> parameters1 = Lists.array();
        parameters1.addAll(parameters);
        parameters1.add(
                ExpressionFunctionParameterName.with("flatten")
                        .variable(Object.class)
                        .setKinds(Sets.of(ExpressionFunctionParameterKind.FLATTEN)
                        )
        );

        return ExpressionEvaluationContextPrepareParametersListFlattened.withFlattened(
                parameters,
                values,
                values.size(),
                parameters.get(parameters.size() - 1),
                context
        );
    }

    private ExpressionEvaluationContextPrepareParametersListFlattened createList2(final List<ExpressionFunctionParameter<?>> parameters,
                                                                                  final List<Object> values) {
        return this.createList2(
                parameters,
                values,
                ExpressionEvaluationContexts.fake()
        );
    }

    private ExpressionEvaluationContextPrepareParametersListFlattened createList2(final List<ExpressionFunctionParameter<?>> parameters,
                                                                                  final List<Object> values,
                                                                                  final ExpressionEvaluationContext context) {
        return ExpressionEvaluationContextPrepareParametersListFlattened.withFlattened(
                parameters,
                values,
                parameters.size() - 1,
                parameters.get(parameters.size() - 1),
                context
        );
    }

    @Override
    void sizeAndCheck2(final List list, final int size) {
        // nop
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<ExpressionEvaluationContextPrepareParametersListFlattened> type() {
        return ExpressionEvaluationContextPrepareParametersListFlattened.class;
    }
}
