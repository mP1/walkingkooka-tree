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
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExpressionFunctionNullValueTest extends ExpressionFunctionTestCase<ExpressionFunctionNullValue<FakeExpressionEvaluationContext>,
        FakeExpressionEvaluationContext,
        Object> {

    // apply............................................................................................................

    @Test
    public void testApplyWrongParameterCountFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.apply2("A")
        );
    }

    @Test
    public void testApply() {
        this.applyAndCheck(
                Lists.empty(),
                null
        );
    }

    // helpers..........................................................................................................

    @Override
    public ExpressionFunctionNullValue<FakeExpressionEvaluationContext> createBiFunction() {
        return ExpressionFunctionNullValue.instance();
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }

    @Override
    public FakeExpressionEvaluationContext createContext() {
        return new FakeExpressionEvaluationContext();
    }

    // toString..........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createBiFunction(),
                "nullValue"
        );
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionFunctionNullValue<FakeExpressionEvaluationContext>> type() {
        return Cast.to(ExpressionFunctionNullValue.class);
    }
}
