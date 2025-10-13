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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionEvaluationContextPrepareParametersListTest extends ExpressionEvaluationContextPrepareParametersListTestCase<ExpressionEvaluationContextPrepareParametersList> {

    private final static Set<ExpressionFunctionParameterKind> CONVERT = Sets.of(
        ExpressionFunctionParameterKind.CONVERT
    );

    @Test
    public void testWithEmptyValues() {
        final List<Object> values = Lists.empty();

        assertSame(
            values,
            ExpressionEvaluationContextPrepareParametersList.with(
                Lists.empty(),
                values,
                FUNCTION_NAME,
                ExpressionEvaluationContexts.fake()
            )
        );
    }

    @Test
    public void testGetInvalidParameterFails() {
        final ArrayIndexOutOfBoundsException thrown = assertThrows(
            ArrayIndexOutOfBoundsException.class,
            () -> ExpressionEvaluationContextPrepareParametersList.with(
                Lists.of(
                    REQUIRED
                ),
                Lists.of(
                    100
                ),
                FUNCTION_NAME,
                ExpressionEvaluationContexts.fake()
            ).get(1)
        );

        this.checkEquals(
            "TestFunction: Unknown parameter 1 expected only 1",
            thrown.getMessage()

        );
    }

    @Test
    public void testGetVariable() {
        final List<Object> list = ExpressionEvaluationContextPrepareParametersList.with(
            Lists.of(
                VARIABLE
            ),
            Lists.of(
                100,
                200
            ),
            FUNCTION_NAME,
            ExpressionEvaluationContexts.fake()
        );
        this.getAndCheck(list, 0, 100);
        this.getAndCheck(list, 1, 200);
        this.sizeAndCheck(list, 2);
    }

    @Test
    public void testNotEmptyNotFlattenSomeConverted() {
        final List<Object> list = ExpressionEvaluationContextPrepareParametersList.with(
            Lists.of(
                REQUIRED.setKinds(CONVERT),
                OPTIONAL
            ),
            Lists.of(
                "Ten",
                "Twenty"
            ),
            FUNCTION_NAME,
            this.createContextWhichConverts(
                "Ten", 10
            )
        );
        this.getAndCheck(list, 0, 10);
        this.getAndCheck(list, 1, "Twenty");
        this.sizeAndCheck(list, 2);
    }

    @Test
    public void testNotEmptyNotFlatten() {
        final List<Object> list = ExpressionEvaluationContextPrepareParametersList.with(
            Lists.of(
                REQUIRED.setKinds(CONVERT),
                OPTIONAL.setKinds(CONVERT)
            ),
            Lists.of(
                "Ten",
                "Twenty"
            ),
            FUNCTION_NAME,
            this.createContextWhichConverts(
                Maps.of(
                    "Ten", 10,
                    "Twenty", 20L
                )
            )
        );
        this.getAndCheck(list, 0, 10);
        this.getAndCheck(list, 1, 20L);
        this.sizeAndCheck(list, 2);
    }

    @Test
    public void testNotEmptyFlatten() {
        final List<Object> list = ExpressionEvaluationContextPrepareParametersList.with(
            Lists.of(
                FLATTEN.setKinds(CONVERT)
            ),
            Lists.of(
                "Ten",
                "Twenty"
            ),
            FUNCTION_NAME,
            this.createContextWhichConverts(
                Maps.of(
                    "Ten", 10,
                    "Twenty", 20
                )
            )
        );
        this.getAndCheck(list, 0, 10);
        this.getAndCheck(list, 1, 20);
        this.sizeAndCheck(list, 2);
    }

    @Test
    public void testNotEmptyFlatten2() {
        final List<Object> list = ExpressionEvaluationContextPrepareParametersList.with(
            Lists.of(
                REQUIRED.setKinds(CONVERT),
                FLATTEN.setKinds(CONVERT)
            ),
            Lists.of(
                "Ten",
                "Twenty"
            ),
            FUNCTION_NAME,
            this.createContextWhichConverts(
                Maps.of(
                    "Ten", 10,
                    "Twenty", 20
                )
            )
        );
        this.getAndCheck(list, 0, 10);
        this.getAndCheck(list, 1, 20);
        this.sizeAndCheck(list, 2);
    }

    @Test
    public void testUnwrapNotEmpty() {
        final List<Object> list = ExpressionEvaluationContextPrepareParametersList.with(
            Lists.of(
                REQUIRED.setKinds(CONVERT)
            ),
            ExpressionEvaluationContextPrepareParametersList.with(
                Lists.of(
                    REQUIRED.setKinds(CONVERT)
                ),
                Lists.of(
                    "Ten"
                ),
                FUNCTION_NAME,
                ExpressionEvaluationContexts.fake()
            ),
            FUNCTION_NAME,
            this.createContextWhichConverts(
                Maps.of(
                    "Ten", 10
                )
            )
        );
        this.getAndCheck(list, 0, 10);
        this.sizeAndCheck(list, 1);
    }

    @Override
    public Class<ExpressionEvaluationContextPrepareParametersList> type() {
        return ExpressionEvaluationContextPrepareParametersList.class;
    }
}
