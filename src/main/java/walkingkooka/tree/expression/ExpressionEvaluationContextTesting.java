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
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.function.ExpressionFunctionContextTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixing testing interface for {@link ExpressionEvaluationContext}
 */
public interface ExpressionEvaluationContextTesting<C extends ExpressionEvaluationContext> extends ExpressionFunctionContextTesting<C> {

    @Test
    default void testEvaluateNullExpressionFails() {
        assertThrows(NullPointerException.class, () -> this.createContext().evaluate(null));
    }

    @Test
    default void testEvaluateExpressionUnknownFunctionNameFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createContext().evaluate(Expression.function(FunctionExpressionName.with("unknown-function-123"), Expression.NO_CHILDREN)));
    }

    default void evaluateAndCheck(final Expression expression,
                                  final Object value) {
        this.evaluateAndCheck(this.createContext(), expression, value);
    }

    default void evaluateAndCheck(final C context,
                                  final Expression expression,
                                  final Object value) {
        assertEquals(value,
                context.evaluate(expression),
                () -> "evaluate " + expression + " " + context);
    }

    @Test
    default void testEvaluateNullFunctionNameFails() {
        assertThrows(NullPointerException.class, () -> this.createContext().evaluate(null, ExpressionEvaluationContext.NO_PARAMETERS));
    }

    @Test
    default void testEvaluateUnknownFunctionNameFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createContext().evaluate(this.unknownFunctionName(), Lists.empty()));
    }

    default FunctionExpressionName unknownFunctionName() {
        return FunctionExpressionName.with("unknown-function-123");
    }

    @Test
    default void testEvaluateFunctionNullParametersFails() {
        assertThrows(NullPointerException.class, () -> this.createContext().evaluate(FunctionExpressionName.with("sum"), null));
    }

    @Test
    default void testReferenceNullReferenceFails() {
        assertThrows(NullPointerException.class, () -> this.createContext().reference(null));
    }

    default void toValueAndCheck(final Expression node, final ExpressionEvaluationContext context, final Object value) {
        assertEquals(value,
                node.toValue(context),
                () -> "Expression.toValue failed, node=" + node + " context=" + context);
    }

    @Override
    default String typeNameSuffix() {
        return ExpressionEvaluationContext.class.getSimpleName();
    }
}
