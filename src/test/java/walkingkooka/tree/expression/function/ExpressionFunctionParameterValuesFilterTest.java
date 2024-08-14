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

package walkingkooka.tree.expression.function;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionParameterValuesFilterTest extends ExpressionFunctionTestCase<ExpressionFunctionParameterValuesFilter<Object, ExpressionEvaluationContext>, Object> {

    private final static BiPredicate<Object, ExpressionEvaluationContext> FILTER = (p, c) -> p instanceof String;

    private final static ExpressionFunction<Object, ExpressionEvaluationContext> FUNCTION = new FakeExpressionFunction<>() {

        @Override
        public Object apply(final List<Object> parameters,
                            final ExpressionEvaluationContext context) {
            return parameters.stream()
                    .map(p -> p.toString().toUpperCase())
                    .collect(Collectors.joining(","));
        }

        @Override
        public boolean isPure(final ExpressionPurityContext context) {
            return true;
        }

        @Override
        public Optional<ExpressionFunctionName> name() {
            return NAME;
        }

        @Override
        public List<ExpressionFunctionParameter<?>> parameters(final int count) {
            return Lists.of(
                    ExpressionFunctionParameterName.with("parameter1")
                            .required(String.class)
            );
        }
    };

    private final static Optional<ExpressionFunctionName> NAME = Optional.of(
            ExpressionFunctionName.with("custom-namedFunction")
    );

    @Test
    public void testWithNullFilterFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctionParameterValuesFilter.with(null, FUNCTION)
        );
    }

    @Test
    public void testWithNullFunctionFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctionParameterValuesFilter.with(FILTER, null)
        );
    }

    @Test
    public void testApply() {
        this.applyAndCheck(
                Lists.of(
                        "aaa", // toUpperCase()
                        "bbb",
                        3, // removed!
                        "ddd"
                ),
                "AAA,BBB,DDD"
        );
    }

    @Test
    public void testName() {
        this.checkEquals(
                NAME,
                this.createBiFunction().name()
        );
    }

    @Test
    public void testFilterParameterValuesSamePredicate() {
        final ExpressionFunctionParameterValuesFilter<Object, ExpressionEvaluationContext> function = this.createBiFunction();

        assertSame(function, function.filterParameterValues(FILTER));
    }

    // helpers..........................................................................................................

    @Override
    public ExpressionFunctionParameterValuesFilter<Object, ExpressionEvaluationContext> createBiFunction() {
        return ExpressionFunctionParameterValuesFilter.with(
                FILTER,
                FUNCTION
        );
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    @Override
    public Class<ExpressionFunctionParameterValuesFilter<Object, ExpressionEvaluationContext>> type() {
        return Cast.to(ExpressionFunctionParameterValuesFilter.class);
    }
}
