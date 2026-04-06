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
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

import java.util.Locale;

public final class TreeExpressionFunctionLocaleTest extends TreeExpressionFunctionTestCase<TreeExpressionFunctionLocale<FakeExpressionEvaluationContext>, FakeExpressionEvaluationContext, Locale> {

    @Test
    public void testApplyWithString() {
        final Locale locale = Locale.forLanguageTag("en-AU");

        this.applyAndCheck(
            Lists.of(
                locale
            ),
            locale
        );
    }

    @Override
    public TreeExpressionFunctionLocale<FakeExpressionEvaluationContext> createBiFunction() {
        return TreeExpressionFunctionLocale.instance();
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
            "locale"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeExpressionFunctionLocale<FakeExpressionEvaluationContext>> type() {
        return Cast.to(TreeExpressionFunctionLocale.class);
    }
}
