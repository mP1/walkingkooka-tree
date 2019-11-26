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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NotExpressionFunctionTest extends ExpressionFunctionTestCase<NotExpressionFunction, Boolean> {

    @Test
    public void testWithNullFunctionFails() {
        assertThrows(NullPointerException.class, () -> NotExpressionFunction.with(null));
    }

    @Test
    public void testInverts() {
        this.applyAndCheck2(parameters("a1", "a"), false);
    }

    @Test
    public void testInverts2() {
        this.applyAndCheck2(parameters("a1", "z"), true);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "not(" + ExpressionFunctions.contains() + ")");
    }

    @Override
    public NotExpressionFunction createBiFunction() {
        return NotExpressionFunction.with(ExpressionFunctions.contains());
    }

    @Override
    public Class<NotExpressionFunction> type() {
        return NotExpressionFunction.class;
    }
}
