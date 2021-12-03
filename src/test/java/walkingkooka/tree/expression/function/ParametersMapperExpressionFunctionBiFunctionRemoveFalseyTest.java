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
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public final class ParametersMapperExpressionFunctionBiFunctionRemoveFalseyTest extends ParametersMapperExpressionFunctionBiFunctionTestCase2<ParametersMapperExpressionFunctionBiFunctionRemoveFalsey<FakeExpressionFunctionContext>> {

    @Test
    public void testRemoveNone() {
        this.applyAndCheck2(Lists.of(Byte.MAX_VALUE, 2, Long.MAX_VALUE, Float.MAX_VALUE));
    }

    @Test
    public void testRemoveZeros() {
        this.applyAndCheck2(Lists.of(1, 2, 0, 4, BigDecimal.ZERO, BigInteger.valueOf(5)), Lists.of(1, 2, 4, BigInteger.valueOf(5)));
    }

    @Test
    public void testFunctionRemoveFalsey() {
        this.checkEquals("1/2/3/4/5",
                new FakeExpressionFunction<String, FakeExpressionFunctionContext>() {

                    @Override
                    public String apply(final List<Object> parameters,
                                        final FakeExpressionFunctionContext context) {
                        return parameters.stream()
                                .map(Object::toString)
                                .collect(Collectors.joining("/"));
                    }
                }.removeFalsey()
                        .apply(Lists.of((byte) 1, 2, 0, 0, (short) 3, 4, 5),
                                this.createContext()));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "remove falsey");
    }

    @Override
    public ParametersMapperExpressionFunctionBiFunctionRemoveFalsey<FakeExpressionFunctionContext> createBiFunction() {
        return ParametersMapperExpressionFunctionBiFunctionRemoveFalsey.instance();
    }

    @Override
    public Class<ParametersMapperExpressionFunctionBiFunctionRemoveFalsey<FakeExpressionFunctionContext>> type() {
        return Cast.to(ParametersMapperExpressionFunctionBiFunctionRemoveFalsey.class);
    }

    @Override
    FakeExpressionFunctionContext createContext() {
        return new FakeExpressionFunctionContext() {
            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> target) {
                checkEquals(Boolean.class, target, "target");

                return Converters.<FakeExpressionFunctionContext>collection(Lists.of(
                        Converters.truthyNumberBoolean(),
                        Converters.booleanTrueFalse((v) -> v instanceof String,
                                (v) -> "".equals(v),
                                (v) -> Boolean.class == v,
                                true,
                                false)
                )).convert(value, target, this);
            }
        };
    }
}
