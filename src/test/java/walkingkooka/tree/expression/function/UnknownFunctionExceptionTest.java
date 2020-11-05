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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.ThrowableTesting2;
import walkingkooka.tree.expression.FunctionExpressionName;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class UnknownFunctionExceptionTest implements ThrowableTesting2<UnknownFunctionException> {

    @Test
    public void testCreate() {
        final FunctionExpressionName name = FunctionExpressionName.with("custom-function");
        final UnknownFunctionException exception = new UnknownFunctionException(name);
        this.checkMessage(exception, "Unknown function \"custom-function\"");
        assertEquals(name, exception.name());
    }

    @Override
    public Class<UnknownFunctionException> type() {
        return UnknownFunctionException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
