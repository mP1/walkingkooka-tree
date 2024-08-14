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
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;

import java.lang.reflect.Method;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionsTest implements PublicStaticHelperTesting<ExpressionFunctions> {

    @Test
    public void testLookupNullFunctionsFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctions.lookup(null, CaseSensitivity.SENSITIVE)
        );
    }

    @Test
    public void testLookupNullCaseSensitiveFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctions.lookup(
                        Sets.empty(),
                        null
                )
        );
    }

    @Test
    public void testLookupIncludesAnonymousFunctionFails() {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () ->
                        ExpressionFunctions.lookup(
                                Sets.of(
                                        functionWithName(null)
                                ),
                                CaseSensitivity.SENSITIVE
                        )
        );
        this.checkEquals(
                "Anonymous functions are not supported",
                thrown.getMessage(),
                "message"
        );
    }

    @Test
    public void testLookupNullDuplicateFunctionNameFails() {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> ExpressionFunctions.lookup(
                        Sets.of(
                                functionWithName("duplicate123"),
                                functionWithName("duplicate123")
                        ),
                        CaseSensitivity.SENSITIVE
                )
        );
        this.checkEquals(
                "Duplicate f" +
                        "unction \"duplicate123\"",
                thrown.getMessage(),
                "message"
        );
    }

    @Test
    public void testLookupNullDuplicateFunctionNameCaseInsensitiveFails() {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> ExpressionFunctions.lookup(
                        Sets.of(
                                functionWithName("duplicate123"),
                                functionWithName("DUPLICATE123")
                        ),
                        CaseSensitivity.INSENSITIVE
                )
        );
        this.checkEquals(
                "Duplicate function \"DUPLICATE123\"",
                thrown.getMessage(),
                "message"
        );
    }

    @Test
    public void testLookupCaseSensitive() {
        final ExpressionFunction<Void, ExpressionEvaluationContext> function = functionWithName("test-1");

        final Function<ExpressionFunctionName, Optional<ExpressionFunction<?, ExpressionEvaluationContext>>> lookup =
                ExpressionFunctions.lookup(
                        Sets.of(
                                function,
                                functionWithName("test-functionWithName-2")
                        ),
                        CaseSensitivity.SENSITIVE
                );
        this.checkEquals(
                Optional.of(
                        function
                ),
                lookup.apply(
                        ExpressionFunctionName.with("test-1")
                )
        );
    }

    @Test
    public void testLookupCaseSensitiveWrongCaseNotFound() {
        final ExpressionFunction<Void, ExpressionEvaluationContext> function = functionWithName("test-1");

        final Function<ExpressionFunctionName, Optional<ExpressionFunction<?, ExpressionEvaluationContext>>> lookup =
                ExpressionFunctions.lookup(
                        Sets.of(
                                function,
                                functionWithName("test-functionWithName-2")
                        ),
                        CaseSensitivity.SENSITIVE
                );
        this.checkEquals(
                Optional.empty(),
                lookup.apply(
                        ExpressionFunctionName.with("TEST-1")
                )
        );
    }

    @Test
    public void testLookupCaseInsensitiveWrongCase() {
        final ExpressionFunction<Void, ExpressionEvaluationContext> function = functionWithName("test-functionWithName-1");

        final Function<ExpressionFunctionName, Optional<ExpressionFunction<?, ExpressionEvaluationContext>>> lookup =
                ExpressionFunctions.lookup(
                        Sets.of(
                                function,
                                functionWithName("test-functionWithName-2")
                        ),
                        CaseSensitivity.INSENSITIVE
                );
        this.checkEquals(
                Optional.of(
                        function
                ),
                lookup.apply(
                        ExpressionFunctionName.with("TEST-functionWithName-1")
                )
        );
    }

    @Test
    public void testLookupCaseInsensitiveNotFound() {
        final ExpressionFunction<Void, ExpressionEvaluationContext> function = functionWithName("test-functionWithName-1");

        final Function<ExpressionFunctionName, Optional<ExpressionFunction<?, ExpressionEvaluationContext>>> lookup =
                ExpressionFunctions.lookup(
                        Sets.of(
                                function,
                                functionWithName("test-functionWithName-2")
                        ),
                        CaseSensitivity.INSENSITIVE
                );
        this.checkEquals(
                Optional.empty(),
                lookup.apply(
                        ExpressionFunctionName.with("TEST-functionWithName-unknown")
                )
        );
    }

    private static ExpressionFunction<Void, ExpressionEvaluationContext> functionWithName(final String name) {
        return new FakeExpressionFunction<>() {
            @Override
            public Optional<ExpressionFunctionName> name() {
                return Optional.ofNullable(name)
                        .map(ExpressionFunctionName::with);
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    @Test
    public void testVisit() {
        final Set<ExpressionFunctionName> names = Sets.sorted();

        ExpressionFunctions.visit(
                (e) -> names.add(
                        e.name()
                                .orElseThrow(() -> new IllegalStateException("namedFunction must have a name"))
                )
        );

        final Set<String> methods = Arrays.stream(ExpressionFunctions.class.getDeclaredMethods())
                .filter(m -> m.getReturnType() == ExpressionFunction.class)
                .filter(m -> m.getParameterCount() == 0)
                .filter(m -> !(m.getName().equals("fake")))
                .map(Method::getName)
                .collect(Collectors.toCollection(Sets::sorted));

        this.checkEquals(
                methods.size(),
                names.size(),
                methods::toString
        );
        this.checkEquals(
                true,
                names.contains(
                        ExpressionFunctions.typeName()
                                .name()
                                .get()
                )
        );
    }

    @Test
    public void testPublicStaticMethodsWithoutMathContextParameter() {
        this.publicStaticMethodParametersTypeCheck(MathContext.class);
    }

    @Override
    public Class<ExpressionFunctions> type() {
        return ExpressionFunctions.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
