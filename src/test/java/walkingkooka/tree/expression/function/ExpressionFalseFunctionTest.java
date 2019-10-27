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

public final class ExpressionFalseFunctionTest extends ExpressionFunctionTestCase<ExpressionFalseFunction, Boolean> {

    @Test
    public void testExecuteFunction() {
        this.applyAndCheck2(parameters(), Boolean.FALSE);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "false");
    }

    @Override
    public ExpressionFalseFunction createBiFunction() {
        return ExpressionFalseFunction.INSTANCE;
    }

    @Override
    public Class<ExpressionFalseFunction> type() {
        return ExpressionFalseFunction.class;
    }
}
