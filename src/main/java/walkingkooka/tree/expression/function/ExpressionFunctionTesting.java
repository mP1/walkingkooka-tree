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
import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionPurityTesting;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.util.BiFunctionTesting;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Mixing interface that provides methods to test a {@link ExpressionFunction}
 */
public interface ExpressionFunctionTesting<F extends ExpressionFunction<V, C>, V, C extends ExpressionFunctionContext>
        extends BiFunctionTesting<F, List<Object>, C, V>,
        ExpressionPurityTesting,
        TypeNameTesting<F> {

    @Test
    default void testName() {
        checkNotEquals(
                null,
                this.createBiFunction().name()
        );
    }

    @Test
    default void testSetNameNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createBiFunction()
                        .setName(null)
        );
    }

    @Test
    default void testSetNameSame() {
        final F function = this.createBiFunction();
        assertSame(
                function,
                function.setName(function.name())
        );
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
    default void testParametersOptionalNotBeforeRequired() {
        final List<ExpressionFunctionParameter<?>> parameters = this.createBiFunction().parameters();

        ExpressionFunctionParameter previous = null;

        for (int i = 0; i < parameters.size(); i++) {
            final ExpressionFunctionParameter<?> parameter = parameters.get(i);
            final ExpressionFunctionParameterCardinality current = parameter.cardinality();
            switch (current) {
                case OPTIONAL:
                case VARIABLE:
                    previous = parameter;
                    break;
                case REQUIRED:
                    if (null != previous) {
                        fail("Required parameter " + parameter + " after " + parameter);
                    }
                    break;
                default:
                    NeverError.unhandledCase(current, ExpressionFunctionParameterCardinality.values());
            }
        }
    }

    @Test
    default void testParametersOnlyLastMayBeVariable() {
        final List<ExpressionFunctionParameter<?>> parameters = this.createBiFunction()
                .parameters();

        final int secondLast = Math.max(0, parameters.size() - 1);

        for (int i = 0; i < secondLast; i++) {
            final ExpressionFunctionParameter<?> parameter = parameters.get(i);

            this.checkNotEquals(
                    ExpressionFunctionParameterCardinality.VARIABLE,
                    parameter.cardinality(),
                    () -> "non last parameter is variable=" + parameter
            );
        }
    }

    @Test
    default void testMapParametersWithNullFunction() {
        assertThrows(
                NullPointerException.class,
                () -> this.createBiFunction()
                        .mapParameters(null)
        );
    }

    // apply...........................................................................................................

    default V apply2(final Object... parameters) {
        return this.createBiFunction()
                .apply(
                        parameters(parameters),
                        this.createContext()
                );
    }

    default void applyAndCheck2(final List<Object> parameters,
                                final V result) {
        this.applyAndCheck2(
                this.createBiFunction(),
                parameters,
                result
        );
    }

    default void applyAndCheck2(final F function,
                                final List<Object> parameters,
                                final V result) {
        this.applyAndCheck2(function, parameters, this.createContext(), result);
    }

    default <RR, CC extends ExpressionFunctionContext> void applyAndCheck2(final ExpressionFunction<RR, CC> function,
                                                                           final List<Object> parameters,
                                                                           final CC context,
                                                                           final RR result) {
        for (final ExpressionFunctionKind kind : function.kinds()) {
            switch (kind) {
                case EVALUATE_PARAMETERS:
                    this.checkEquals(
                            Lists.empty(),
                            parameters.stream()
                                    .filter(Expression.class::isInstance)
                                    .collect(Collectors.toList()
                                    ),
                            () -> "Should not include parameter(s) of type " + Expression.class.getName()
                    );
                    break;
                case FLATTEN:
                    this.checkEquals(
                            Lists.empty(),
                            parameters.stream()
                                    .filter(List.class::isInstance)
                                    .collect(Collectors.toList()
                                    ),
                            () -> "Should not include parameter(s) of type " + List.class.getName()
                    );
                    break;
                case RESOLVE_REFERENCES:
                    this.checkEquals(
                            Lists.empty(),
                            parameters.stream()
                                    .filter(ExpressionReference.class::isInstance)
                                    .collect(Collectors.toList()
                                    ),
                            () -> "Should not include parameter(s) of type " + ExpressionReference.class.getName()
                    );
                    break;
                default:
                    break;
            }
        }

        this.checkEquals(
                result,
                function.apply(parameters, context),
                () -> "Wrong result for " +
                        function +
                        " for params: " +
                        parameters.stream()
                                .map(CharSequences::quoteIfChars)
                                .collect(Collectors.joining(", "))
        );
    }

    C createContext();

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
