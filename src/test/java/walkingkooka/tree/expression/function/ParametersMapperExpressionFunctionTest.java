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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ParametersMapperExpressionFunctionTest implements ExpressionFunctionTesting<ParametersMapperExpressionFunction<Object, FakeExpressionFunctionContext>,
        Object,
        FakeExpressionFunctionContext>,
        ToStringTesting<ParametersMapperExpressionFunction<Object, FakeExpressionFunctionContext>> {

    private final static BiFunction<List<Object>, FakeExpressionFunctionContext, List<Object>> MAPPER = (p, c) -> p.stream()
            .map(pp -> pp.toString().toUpperCase())
            .collect(Collectors.toList());

    private final static ExpressionFunction<Object, FakeExpressionFunctionContext> FUNCTION = new ExpressionFunction<Object, FakeExpressionFunctionContext>() {

        @Override
        public Object apply(final List<Object> parameters,
                            final FakeExpressionFunctionContext context) {
            return parameters.stream()
                    .map(p -> p.toString() + "-function")
                    .collect(Collectors.joining(","));
        }

        @Override
        public boolean isPure() {
            return true;
        }

        @Override
        public FunctionExpressionName name() {
            return NAME;
        }

        @Override
        public boolean resolveReferences() {
            return false;
        }
    };

    private final static FunctionExpressionName NAME = FunctionExpressionName.with("custom-function");

    @Test
    public void testWithNullMapperFails() {
        assertThrows(NullPointerException.class, () -> ParametersMapperExpressionFunction.with(null, FUNCTION));
    }

    @Test
    public void testWithNullFunctionFails() {
        assertThrows(NullPointerException.class, () -> ParametersMapperExpressionFunction.with(MAPPER, null));
    }

    @Test
    public void testApply() {
        this.applyAndCheck(Lists.of("param-1", "param-2", 3),
                this.createContext(),
                "PARAM-1-function,PARAM-2-function,3-function");
    }

    @Test
    public void testName() {
        assertEquals(NAME, this.createBiFunction().name());
    }

    @Test
    public void testResolveReferencesTrue() {
        this.resolveReferencesAndCheck(true);
    }

    @Test
    public void testResolveReferencesFalse() {
        this.resolveReferencesAndCheck(false);
    }

    private void resolveReferencesAndCheck(final boolean resolveReferences) {
        assertEquals(resolveReferences, ParametersMapperExpressionFunction.with(MAPPER, new FakeExpressionFunction<>() {
            @Override
            public boolean resolveReferences() {
                return resolveReferences;
            }
        }).resolveReferences());
    }

    @Test
    public void testMapSameFunction() {
        final ParametersMapperExpressionFunction<Object, FakeExpressionFunctionContext> function = this.createBiFunction();
        assertSame(function, function.parameters(MAPPER));
    }

    @Test
    public void testMapFunctionThenApply() {
        final ParametersMapperExpressionFunction<Object, FakeExpressionFunctionContext> function = this.createBiFunction();

        final BiFunction<List<Object>, FakeExpressionFunctionContext, List<Object>> mapper = (p, c) -> p.stream()
                .map(pp -> pp.toString() + "-a")
                .collect(Collectors.toList());
        final ParametersMapperExpressionFunction<Object, FakeExpressionFunctionContext> function2 = Cast.to(function.parameters(mapper));
        assertNotSame(function, function2);

        this.applyAndCheck(function2,
                Lists.of("param-1", "param-2", 3),
                this.createContext(),
                "PARAM-1-A-function,PARAM-2-A-function,3-A-function");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), FUNCTION + "(" + MAPPER + ")");
    }

    // helpers..........................................................................................................

    @Override
    public ParametersMapperExpressionFunction<Object, FakeExpressionFunctionContext> createBiFunction() {
        return ParametersMapperExpressionFunction.with(MAPPER, FUNCTION);
    }

    @Override
    public FakeExpressionFunctionContext createContext() {
        return new FakeExpressionFunctionContext();
    }

    @Override
    public Class<ParametersMapperExpressionFunction<Object, FakeExpressionFunctionContext>> type() {
        return Cast.to(ParametersMapperExpressionFunction.class);
    }
}
