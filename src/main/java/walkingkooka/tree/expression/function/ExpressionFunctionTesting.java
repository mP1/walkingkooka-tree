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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.ExpressionNumberConverterContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionPurityTesting;
import walkingkooka.util.BiFunctionTesting;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixing interface that provides methods to test a {@link ExpressionFunction}
 */
public interface ExpressionFunctionTesting<F extends ExpressionFunction<V, C>, V, C extends ExpressionFunctionContext>
        extends BiFunctionTesting<F, List<Object>, C, V>,
        ExpressionPurityTesting,
        TypeNameTesting<F> {

    @Test
    default void testName() {
        assertNotNull(this.createBiFunction().name());
    }

    @Test
    default void testSetNameNullFails() {
        assertThrows(NullPointerException.class, () -> this.createBiFunction().setName(null));
    }

    @Test
    default void testSetNameSame() {
        final F function = this.createBiFunction();
        assertSame(function, function.setName(function.name()));
    }

    @Test
    default void testParameterNamesUnique() {
        final F function = this.createBiFunction();
        final List<ExpressionFunctionParameter<?>> parameters = function.parameters();
        this.checkEquals(
                parameters.stream()
                        .map(ExpressionFunctionParameter::name)
                        .distinct()
                        .collect(Collectors.toList()),
                parameters.stream()
                        .map(ExpressionFunctionParameter::name)
                        .collect(Collectors.toList()),
                "parameters includes duplicate parameter names"
        );
    }

    @Test
    default void testMapParametersWithNullFunction() {
        assertThrows(NullPointerException.class, () -> this.createBiFunction().mapParameters(null));
    }

    // apply...........................................................................................................

    default V apply2(final Object... parameters) {
        return this.createBiFunction()
                .apply(parameters(parameters), this.createContext());
    }

    default void applyAndCheck2(final List<Object> parameters,
                                final V result) {
        this.applyAndCheck2(this.createBiFunction(), parameters, result);
    }

    default void applyAndCheck2(final F function,
                                final List<Object> parameters,
                                final V result) {
        this.applyAndCheck2(function, parameters, this.createContext(), result);
    }

    default <TT, RR, CC extends ExpressionFunctionContext> void applyAndCheck2(final ExpressionFunction<RR, CC> function,
                                                                               final List<Object> parameters,
                                                                               final CC context,
                                                                               final RR result) {
        this.checkEquals(result,
                function.apply(parameters, context),
                () -> "Wrong result for " + function + " for params: " + CharSequences.quoteIfChars(parameters));
    }

    C createContext();

    // requiresEvaluatedParameters......................................................................................

    default void requiresEvaluatedParametersAndCheck(final boolean requiresEvaluatedParameters) {
        this.requiresEvaluatedParametersAndCheck(this.createBiFunction(), requiresEvaluatedParameters);
    }

    default void requiresEvaluatedParametersAndCheck(final ExpressionFunction<?, ?> function,
                                                     final boolean requiresEvaluatedParameters) {
        this.checkEquals(requiresEvaluatedParameters,
                function.requiresEvaluatedParameters(),
                () -> function.name() + " requiresEvaluatedParameters: " + function);
    }

    // resolveReferences................................................................................................

    default void resolveReferencesAndCheck(final boolean resolveReference) {
        this.resolveReferencesAndCheck(this.createBiFunction(), resolveReference);
    }

    default void resolveReferencesAndCheck(final ExpressionFunction<?, ?> function,
                                           final boolean resolveReference) {
        this.checkEquals(resolveReference,
                function.resolveReferences(),
                () -> function.name() + " resolveReferences: " + function);
    }

    // convert..........................................................................................................

    default <T> Either<T, String> convert(final Object value, final Class<T> target) {
        if (target.isInstance(value)) {
            return Either.left(target.cast(value));
        }
        if (target == String.class) {
            return Either.left(Cast.to(value.toString()));
        }

        final ExpressionNumberConverterContext context = ExpressionNumberConverterContexts.basic(Converters.fake(),
                ConverterContexts.fake(),
                this.expressionNumberKind());

        if (value instanceof String && Number.class.isAssignableFrom(target)) {
            final Double doubleValue = Double.parseDouble((String) value);
            return ExpressionNumber.toConverter(ExpressionNumber.fromConverter(Converters.numberNumber()))
                    .convert(doubleValue, target, context);
        }
        if (target == Boolean.class) {
            return Either.left(Cast.to(Boolean.parseBoolean(value.toString())));
        }
        return ExpressionNumber.toConverter(ExpressionNumber.fromConverter(Converters.numberNumber()))
                .convert(value, target, context);
    }

    default List<Object> parameters(final Object... values) {
        return Lists.of(values);
    }

    default ExpressionNumberKind expressionNumberKind() {
        return ExpressionNumberKind.DEFAULT;
    }

    // TypeNameTesting..................................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return ExpressionFunction.class.getSimpleName();
    }
}
