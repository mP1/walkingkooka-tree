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
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LambdaExpressionFunctionTest implements ExpressionFunctionTesting<LambdaExpressionFunction<String, FakeExpressionEvaluationContext>, String, FakeExpressionEvaluationContext>,
        ToStringTesting<LambdaExpressionFunction<String, FakeExpressionEvaluationContext>> {

    private final static boolean PURE = true;

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
            ExpressionFunctionParameterName.with("x")
                    .required(String.class),
            ExpressionFunctionParameterName.with("y")
                    .required(String.class)
    );
    private final Class<String> RETURN_TYPE = String.class;

    private final static BiPredicate<ExpressionFunctionParameterName, ExpressionReference> PARAMETERS_MATCHER = (p, r) -> p.value().equals(r.toString());

    private static ExpressionReference var(final String toString) {
        return new ExpressionReference() {
            @Override
            public String toString() {
                return toString;
            }
        };
    }

    private final static Function<FakeExpressionEvaluationContext, String> FUNCTION = (c) -> "x=" + c.referenceOrFail(
            var("x")
    ) +
            ",y=" +
            c.referenceOrFail(var("y"));

    @Override
    public int minimumParameterCount() {
        return 0;
    }

    // with............................................................................................................

    @Test
    public void testWithNullParametersFails() {
        this.withFails(
                PURE,
                null,
                RETURN_TYPE,
                FUNCTION,
                PARAMETERS_MATCHER
        );
    }

    @Test
    public void testWithNullReturnTypeFails() {
        this.withFails(
                PURE,
                PARAMETERS,
                null,
                FUNCTION,
                PARAMETERS_MATCHER
        );
    }

    @Test
    public void testWithNullFunctionFails() {
        this.withFails(
                PURE,
                PARAMETERS,
                RETURN_TYPE,
                null,
                PARAMETERS_MATCHER
        );
    }

    @Test
    public void testWithNullParameterMatcherFails() {
        this.withFails(
                PURE,
                PARAMETERS,
                RETURN_TYPE,
                FUNCTION,
                null
        );
    }

    private void withFails(final boolean pure,
                           final List<ExpressionFunctionParameter<?>> parameters,
                           final Class<String> returnType,
                           final Function<FakeExpressionEvaluationContext, String> function,
                           final BiPredicate<ExpressionFunctionParameterName, ExpressionReference> parameterMatcher) {
        assertThrows(
                NullPointerException.class,
                () -> LambdaExpressionFunction.with(
                        pure,
                        parameters,
                        returnType,
                        function,
                        parameterMatcher
                )
        );
    }

    // apply............................................................................................................

    @Test
    public void testApplyWrongParameterCountFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.apply2()
        );
    }

    @Test
    public void testApply() {
        this.applyAndCheck(
                Lists.of(10, 20),
                "x=10,y=20"
        );
    }

    @Test
    public void testApply2() {
        this.applyAndCheck(
                Lists.of(100, 200),
                "x=100,y=200"
        );
    }

    // toString..........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createBiFunction(),
                ExpressionFunction.ANONYMOUS
        );
    }

    // helpers..........................................................................................................

    @Override
    public LambdaExpressionFunction<String, FakeExpressionEvaluationContext> createBiFunction() {
        return LambdaExpressionFunction.with(
                PURE,
                PARAMETERS,
                RETURN_TYPE,
                FUNCTION,
                PARAMETERS_MATCHER
        );
    }

    @Override
    public FakeExpressionEvaluationContext createContext() {
        return new FakeExpressionEvaluationContext() {
            @Override
            public FakeExpressionEvaluationContext context(final Function<ExpressionReference, Optional<Object>> scoped) {
                return new FakeExpressionEvaluationContext() {
                    @Override
                    public Optional<Object> reference(final ExpressionReference reference) {
                        return scoped.apply(reference);
                    }
                };
            }
        };
    }

    @Override
    public Class type() {
        return Cast.to(LambdaExpressionFunction.class);
    }
}
