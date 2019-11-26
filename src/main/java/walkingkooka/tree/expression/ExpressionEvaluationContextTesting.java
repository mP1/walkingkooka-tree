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
import walkingkooka.ContextTesting;
import walkingkooka.math.DecimalNumberContextTesting2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixing testing interface for {@link ExpressionEvaluationContext}
 */
public interface ExpressionEvaluationContextTesting<C extends ExpressionEvaluationContext> extends DecimalNumberContextTesting2<C>,
        ContextTesting<C> {

    @Test
    default void testFunctionNullNameFails() {
        assertThrows(NullPointerException.class, () -> this.createContext().function(null, ExpressionEvaluationContext.NO_PARAMETERS));
    }

    @Test
    default void testFunctionNullParametersFails() {
        assertThrows(NullPointerException.class, () -> this.createContext().function(FunctionExpressionName.with("sum"), null));
    }

    @Test
    default void testReferenceNullReferenceFails() {
        assertThrows(NullPointerException.class, () -> this.createContext().reference(null));
    }

    @Test
    default void testConvertNullValueFails() {
        assertThrows(NullPointerException.class, () -> this.createContext().convert(null, Object.class));
    }

    @Test
    default void testConvertNullTargetTypeFails() {
        assertThrows(NullPointerException.class, () -> this.createContext().convert("value", null));
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
