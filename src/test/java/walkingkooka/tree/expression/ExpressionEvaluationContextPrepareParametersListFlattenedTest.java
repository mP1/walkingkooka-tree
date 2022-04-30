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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionEvaluationContextPrepareParametersListFlattenedTest extends ExpressionEvaluationContextPrepareParametersListTestCase<ExpressionEvaluationContextPrepareParametersListFlattened> {

    private final static ExpressionFunctionParameter<Integer> VARIABLE = ExpressionFunctionParameterName.with("integer")
            .variable(Integer.class);

    private final static ExpressionEvaluationContext CONTEXT_PARSE_INT = new FakeExpressionEvaluationContext() {

        @Override
        public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                      final Object value) {
            return Cast.to(Integer.parseInt((String) value));
        }
    };

    // flatten..........................................................................................................

    @Test
    public void testNoListValues() {
        this.flattenAndCheck(
                List.of(1, 2),
                CONTEXT,
                List.of(1, 2)
        );
    }

    @Test
    public void testListValues() {
        this.flattenAndCheck(
                List.of(
                        1,
                        Lists.of(
                                2,
                                3
                        )
                ),
                CONTEXT,
                List.of(1, 2, 3)
        );
    }

    @Test
    public void testNestedListValues() {
        this.flattenAndCheck(
                List.of(
                        1,
                        Lists.of(
                                2,
                                Lists.of(
                                        3,
                                        4
                                )
                        )
                ),
                CONTEXT,
                List.of(1, 2, 3, 4)
        );
    }

    @Test
    public void testListValuesAlsoConverted() {
        this.flattenAndCheck(
                List.of(
                        "1",
                        Lists.of(
                                "2",
                                "3"
                        )
                ),
                CONTEXT_PARSE_INT,
                List.of(1, 2, 3)
        );
    }

    private void flattenAndCheck(final List<Object> parameters,
                                 final ExpressionEvaluationContext context,
                                 final List<Object> expected) {
        final ExpressionEvaluationContextPrepareParametersListFlattened flatten = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        parameters,
                        function(
                                Lists.of(VARIABLE),
                                ExpressionFunctionKind.REQUIRES_EVALUATED_PARAMETERS,
                                ExpressionFunctionKind.RESOLVE_REFERENCES,
                                ExpressionFunctionKind.FLATTEN
                        ),
                        context
                ));
        this.checkNotEquals(
                ExpressionEvaluationContextPrepareParametersList.class,
                flatten.getClass()
        );

        this.sizeAndCheck(flatten, expected.size());

        this.checkEquals(
                expected,
                flatten
        );
    }

    @Test
    public void testGetFailedConvert() {
        final ExpressionEvaluationContextPrepareParametersListFlattened list = Cast.to(
                ExpressionEvaluationContextPrepareParametersList.with(
                        Lists.of(
                                "this parameter convert will throw",
                                "222"
                        ),
                        function(
                                Lists.of(VARIABLE),
                                ExpressionFunctionKind.REQUIRES_EVALUATED_PARAMETERS,
                                ExpressionFunctionKind.RESOLVE_REFERENCES,
                                ExpressionFunctionKind.FLATTEN
                        ),
                        CONTEXT_PARSE_INT
                )
        );

        assertThrows(
                NumberFormatException.class,
                () -> {
                    list.get(0);
                }
        );
        this.getAndCheck(list, 1, 222);
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<ExpressionEvaluationContextPrepareParametersListFlattened> type() {
        return ExpressionEvaluationContextPrepareParametersListFlattened.class;
    }
}
