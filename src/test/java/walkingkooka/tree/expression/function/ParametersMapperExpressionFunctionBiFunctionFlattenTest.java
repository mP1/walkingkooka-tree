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

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ParametersMapperExpressionFunctionBiFunctionFlattenTest extends ParametersMapperExpressionFunctionBiFunctionTestCase2<ParametersMapperExpressionFunctionBiFunctionFlatten<FakeExpressionFunctionContext>> {

    @Test
    public void testEmptyFlattenUnnecessary() {
        this.applyAndCheck2(Lists.empty());
    }

    @Test
    public void testWithoutList() {
        this.applyAndCheck2(Lists.of(1, 2, "3c"));
    }

    @Test
    public void testIncludesList() {
        this.applyAndCheck2(Lists.of(List.of(1), 2, "3c"), Lists.of(1, 2, "3c"));
    }

    @Test
    public void testIncludesNestedList() {
        this.applyAndCheck2(Lists.of(List.of(Lists.of(1), 2), 3, "4d"), Lists.of(1, 2, 3, "4d"));
    }

    @Test
    public void testFunctionFlatten() {
        assertEquals("1/2/3/4/5",
                new FakeExpressionFunction<String, FakeExpressionFunctionContext>() {

                    @Override
                    public String apply(final List<Object> parameters,
                                        final FakeExpressionFunctionContext context) {
                        return parameters.stream()
                                .peek(p -> {
                                    if (p instanceof List) {
                                        throw new UnsupportedOperationException("parameters include List");
                                    }
                                })
                                .map(Object::toString)
                                .collect(Collectors.joining("/"));
                    }
                }.flatten()
                        .apply(Lists.of(1, Lists.of(2, 3, Lists.of(4)), 5), new FakeExpressionFunctionContext()));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "flatten");
    }

    @Override
    public ParametersMapperExpressionFunctionBiFunctionFlatten<FakeExpressionFunctionContext> createBiFunction() {
        return ParametersMapperExpressionFunctionBiFunctionFlatten.instance();
    }

    @Override
    public Class<ParametersMapperExpressionFunctionBiFunctionFlatten<FakeExpressionFunctionContext>> type() {
        return Cast.to(ParametersMapperExpressionFunctionBiFunctionFlatten.class);
    }
}
