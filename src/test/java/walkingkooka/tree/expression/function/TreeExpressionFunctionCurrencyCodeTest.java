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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.currency.CurrencyCode;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

public final class TreeExpressionFunctionCurrencyCodeTest extends TreeExpressionFunctionTestCase<TreeExpressionFunctionCurrencyCode<FakeExpressionEvaluationContext>, FakeExpressionEvaluationContext, CurrencyCode> {

    @Test
    public void testApplyWithString() {
        final CurrencyCode currencyCode = CurrencyCode.parse("AUD");

        this.applyAndCheck(
            Lists.of(
                currencyCode
            ),
            currencyCode
        );
    }

    @Override
    public TreeExpressionFunctionCurrencyCode<FakeExpressionEvaluationContext> createBiFunction() {
        return TreeExpressionFunctionCurrencyCode.instance();
    }

    @Override
    public FakeExpressionEvaluationContext createContext() {
        return new FakeExpressionEvaluationContext() {

        };
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "currencyCode"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeExpressionFunctionCurrencyCode<FakeExpressionEvaluationContext>> type() {
        return Cast.to(TreeExpressionFunctionCurrencyCode.class);
    }
}
