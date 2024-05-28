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

package walkingkooka.tree.expression;

import org.junit.jupiter.api.Test;
import walkingkooka.convert.CanConvertTesting;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctions;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixing testing interface for {@link ExpressionEvaluationContext}
 */
public interface ExpressionEvaluationContextTesting<C extends ExpressionEvaluationContext> extends
        CanConvertTesting<C>,
        ExpressionNumberContextTesting<C>,
        ExpressionPurityContextTesting<C>,
        TreePrintableTesting {

    @Test
    default void testEvaluateNullExpressionFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createContext().evaluate(null)
        );
    }

    @Test
    default void testEvaluateExpressionUnknownFunctionNameFails() {
        assertThrows(
                UnknownExpressionFunctionException.class,
                () -> this.createContext()
                        .evaluate(
                                Expression.call(
                                        Expression.namedFunction(
                                                FunctionExpressionName.with("unknown-namedFunction-123")
                                        ),
                                        Expression.NO_CHILDREN
                                )
                        )
        );
    }

    default void evaluateAndCheck(final Expression expression,
                                  final Object value) {
        this.evaluateAndCheck(this.createContext(), expression, value);
    }

    default void evaluateAndCheck(final C context,
                                  final Expression expression,
                                  final Object value) {
        this.checkEquals(value,
                context.evaluate(expression),
                () -> "evaluate " + expression + " " + context);
    }

    default void toValueAndCheck(final Expression node, final ExpressionEvaluationContext context, final Object value) {
        this.checkEquals(value,
                node.toValue(context),
                () -> "Expression.toValue failed, node=" + node + " context=" + context);
    }

    @Test
    default void testExpressionFunctionNullFunctionNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createContext()
                        .expressionFunctionOrFail(null)
        );
    }

    @Test
    default void testEvaluateFunctionNullFunctionNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createContext()
                        .evaluateFunction(
                                null,
                                ExpressionEvaluationContext.NO_PARAMETERS
                        )
        );
    }

    @Test
    default void testEvaluateFunctionNullParametersFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createContext()
                        .evaluateFunction(
                                ExpressionFunctions.fake(),
                                null
                        )
        );
    }

    @Test
    default void testReferenceNullReferenceFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createContext()
                        .reference(null)
        );
    }

    default <T> void evaluateFunctionAndCheck(final ExpressionFunction<T, C> function,
                                              final List<Object> parameters,
                                              final T expected) {

        this.evaluateFunctionAndCheck(
                this.createContext(),
                function,
                parameters,
                expected
        );
    }

    default <T> void evaluateFunctionAndCheck(final C context,
                                              final ExpressionFunction<T, C> function,
                                              final List<Object> parameters,
                                              final T expected) {
        this.checkEquals(
                expected,
                context.evaluateFunction(function, parameters),
                () -> "evaluate " + function + " " + parameters
        );
    }

    default C createCanConvert() {
        return this.createContext();
    }

    @Override
    default String typeNameSuffix() {
        return ExpressionEvaluationContext.class.getSimpleName();
    }
}
