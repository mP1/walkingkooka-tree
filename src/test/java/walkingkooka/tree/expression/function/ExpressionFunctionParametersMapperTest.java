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
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionParametersMapperTest extends ExpressionFunctionTestCase<ExpressionFunctionParametersMapper<Object, ExpressionEvaluationContext>, Object> {

    private final static BiFunction<List<Object>, ExpressionEvaluationContext, List<Object>> MAPPER = (p, c) -> p.stream()
            .map(pp -> pp.toString().toUpperCase())
            .collect(Collectors.toList());

    private final static ExpressionFunction<Object, ExpressionEvaluationContext> FUNCTION = new FakeExpressionFunction<>() {

        @Override
        public Object apply(final List<Object> parameters,
                            final ExpressionEvaluationContext context) {
            return parameters.stream()
                    .map(p -> p.toString() + "-namedFunction")
                    .collect(Collectors.joining(","));
        }

        @Override
        public boolean isPure(final ExpressionPurityContext context) {
            return true;
        }

        @Override
        public Optional<FunctionExpressionName> name() {
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

    private final static Optional<FunctionExpressionName> NAME = Optional.of(
            FunctionExpressionName.with("custom-namedFunction")
    );

    @Test
    public void testWithNullMapperFails() {
        assertThrows(NullPointerException.class, () -> ExpressionFunctionParametersMapper.with(null, FUNCTION));
    }

    @Test
    public void testWithNullFunctionFails() {
        assertThrows(NullPointerException.class, () -> ExpressionFunctionParametersMapper.with(MAPPER, null));
    }

    @Test
    public void testApply() {
        this.applyAndCheck(Lists.of("param-1", "param-2", 3),
                this.createContext(),
                "PARAM-1-namedFunction,PARAM-2-namedFunction,3-namedFunction");
    }

    @Test
    public void testName() {
        this.checkEquals(NAME, this.createBiFunction().name());
    }

    @Test
    public void testMapSameFunction() {
        final ExpressionFunctionParametersMapper<Object, ExpressionEvaluationContext> function = this.createBiFunction();
        assertSame(function, function.mapParameters(MAPPER));
    }

    @Test
    public void testMapFunctionThenApply() {
        final ExpressionFunctionParametersMapper<Object, ExpressionEvaluationContext> function = this.createBiFunction();

        final BiFunction<List<Object>, ExpressionEvaluationContext, List<Object>> mapper = (p, c) -> p.stream()
                .map(pp -> pp.toString() + "-a")
                .collect(Collectors.toList());
        final ExpressionFunctionParametersMapper<Object, ExpressionEvaluationContext> function2 = Cast.to(function.mapParameters(mapper));
        assertNotSame(function, function2);

        this.applyAndCheck(function2,
                Lists.of("param-1", "param-2", 3),
                this.createContext(),
                "PARAM-1-A-namedFunction,PARAM-2-A-namedFunction,3-A-namedFunction");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), FUNCTION + "(" + MAPPER + ")");
    }

    // helpers..........................................................................................................

    @Override
    public ExpressionFunctionParametersMapper<Object, ExpressionEvaluationContext> createBiFunction() {
        return ExpressionFunctionParametersMapper.with(MAPPER, FUNCTION);
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    @Override
    public Class<ExpressionFunctionParametersMapper<Object, ExpressionEvaluationContext>> type() {
        return Cast.to(ExpressionFunctionParametersMapper.class);
    }
}
