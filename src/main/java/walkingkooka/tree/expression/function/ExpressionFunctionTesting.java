/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.util.BiFunctionTesting;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Mixing interface that provides methods to test a {@link ExpressionFunction}
 */
public interface ExpressionFunctionTesting<F extends ExpressionFunction<V>, V>
        extends BiFunctionTesting<F, List<Object>, ExpressionFunctionContext, V>,
        TypeNameTesting<F> {

    default void apply2(final Object... parameters) {
        this.createBiFunction().apply(parameters(parameters), this.createContext());
    }

    default void applyAndCheck2(final List<Object> parameters,
                                final V result) {
        this.applyAndCheck2(this.createBiFunction(), parameters, result);
    }

    default <TT, RR> void applyAndCheck2(final ExpressionFunction<RR> function,
                                         final List<Object> parameters,
                                         final RR result) {
        this.applyAndCheck2(function, parameters, this.createContext(), result);
    }

    default <TT, RR> void applyAndCheck2(final ExpressionFunction<RR> function,
                                         final List<Object> parameters,
                                         final ExpressionFunctionContext context,
                                         final RR result) {
        assertEquals(result,
                function.apply(parameters, context),
                () -> "Wrong result for " + function + " for params: " + CharSequences.quoteIfChars(parameters));
    }

    default ExpressionFunctionContext createContext() {
        return new FakeExpressionFunctionContext() {
            @Override
            public <T> Either<T, String> convert(final Object value, final Class<T> target) {
                return ExpressionFunctionTesting.this.convert(value, target);
            }
        };
    }

    default <T> Either<T, String> convert(final Object value, final Class<T> target) {
        if (target.isInstance(value)) {
            return Either.left(target.cast(value));
        }
        if (target == String.class) {
            return Either.left(Cast.to(value.toString()));
        }
        if (value instanceof String && (target == Integer.class || target == Number.class)) {
            return Either.left(Cast.to(Integer.parseInt(value.toString())));
        }
        if (target == Boolean.class) {
            return Either.left(Cast.to(Boolean.parseBoolean(value.toString())));
        }

        return Converters.numberNumber().convert(value, target, ConverterContexts.fake());
    }

    default List<Object> parameters(final Object... values) {
        return Lists.of(values);
    }

    // TypeNameTesting...........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return ExpressionFunction.class.getSimpleName();
    }
}
