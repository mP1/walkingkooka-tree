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
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;
import walkingkooka.tree.expression.FakeExpressionReference;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExpressionFunctionLambdaTest extends ExpressionFunctionTestCase<ExpressionFunctionLambda<String, FakeExpressionEvaluationContext>,
    FakeExpressionEvaluationContext,
    String> {

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        ExpressionFunctionParameterName.with("x")
            .required(Object.class),
        ExpressionFunctionParameterName.with("y")
            .required(Object.class)
    );
    private final Class<String> RETURN_TYPE = String.class;

    private static ExpressionReference var(final String name) {
        return new FakeExpressionReference() {

            @Override
            public boolean testParameterName(final ExpressionFunctionParameterName parameterName) {
                return name.equals(parameterName.value());
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    private static final Expression EXPRESSION = Expression.add(
        Expression.reference(var("x")),
        Expression.reference(var("y"))
    );

    // with............................................................................................................

    @Test
    public void testWithNullParametersFails() {
        this.withFails(
            null,
            RETURN_TYPE,
            EXPRESSION
        );
    }

    @Test
    public void testWithNullReturnTypeFails() {
        this.withFails(
            PARAMETERS,
            null,
            EXPRESSION
        );
    }

    @Test
    public void testWithNullExpressionFails() {
        this.withFails(
            PARAMETERS,
            RETURN_TYPE,
            null
        );
    }

    private void withFails(final List<ExpressionFunctionParameter<?>> parameters,
                           final Class<String> returnType,
                           final Expression expression) {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionFunctionLambda.with(
                parameters,
                returnType,
                expression
            )
        );
    }

    // apply............................................................................................................

    @Test
    public void testApplyWrongParameterCountFails() {
        assertThrows(
            IllegalArgumentException.class,
            this::apply2
        );
    }

    @Test
    public void testApply() {
        this.applyAndCheck(
            Lists.of(10, 20),
            "String30"
        );
    }

    @Test
    public void testApply2() {
        this.applyAndCheck(
            Lists.of(100, 200),
            "String300"
        );
    }

    // helpers..........................................................................................................

    @Override
    public ExpressionFunctionLambda<String, FakeExpressionEvaluationContext> createBiFunction() {
        return ExpressionFunctionLambda.with(
            PARAMETERS,
            RETURN_TYPE,
            EXPRESSION
        );
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }

    @Override
    public FakeExpressionEvaluationContext createContext() {
        return new FakeExpressionEvaluationContext() {

            @Override
            public FakeExpressionEvaluationContext enterScope(final Function<ExpressionReference, Optional<Optional<Object>>> scoped) {
                return new FakeExpressionEvaluationContext() {

                    @Override
                    public <T> Either<T, String> convert(final Object value,
                                                         final Class<T> target) {
                        checkEquals(ExpressionNumber.class, target, "target");

                        return this.successfulConversion(
                            ExpressionNumberKind.DOUBLE.create((Number) value),
                            target
                        );
                    }

                    @Override
                    public Object evaluateExpression(final Expression expression) {
                        return expression.toValue(this);
                    }

                    public boolean isText(final Object value) {
                        return value instanceof String;
                    }

                    @Override
                    public Optional<Optional<Object>> reference(final ExpressionReference reference) {
                        return scoped.apply(reference);
                    }
                };
            }

            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> target) {
                checkEquals(String.class, target, "target");

                return this.successfulConversion(
                    "String" + value,
                    target
                );
            }
        };
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "<anonymous>"
        );
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionFunctionLambda<String, FakeExpressionEvaluationContext>> type() {
        return Cast.to(ExpressionFunctionLambda.class);
    }
}
