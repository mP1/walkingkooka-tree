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
import walkingkooka.ContextTesting;
import walkingkooka.convert.CanConvertTesting;
import walkingkooka.tree.expression.FunctionExpressionName;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixing testing interface for {@link ExpressionFunctionContext}
 */
public interface ExpressionFunctionContextTesting<C extends ExpressionFunctionContext> extends CanConvertTesting<C>,
        ContextTesting<C> {

    @Test
    default void testFunctionNullNameFails() {
        assertThrows(NullPointerException.class, () -> this.createContext().function(null, ExpressionFunctionContext.NO_PARAMETERS));
    }

    @Test
    default void testFunctionNullParametersFails() {
        assertThrows(NullPointerException.class, () -> this.createContext().function(FunctionExpressionName.with("sum"), null));
    }

    @Override
    default String typeNameSuffix() {
        return ExpressionFunctionContext.class.getSimpleName();
    }

    default C createCanConvert() {
        return this.createContext();
    }
}
