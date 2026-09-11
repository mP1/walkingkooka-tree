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

import java.util.List;

public final class TreeExpressionFunctionLoggingDebugTest extends TreeExpressionFunctionLoggingTestCase<TreeExpressionFunctionLoggingDebug<FakeExpressionEvaluationContext>> {

    @Test
    public void testApplyWithString() {
        final String message = "Message 123";

        final List<String> logged = Lists.array();

        final FakeExpressionEvaluationContext context = new FakeExpressionEvaluationContext() {

            @Override
            public void debug(final String message) {
                logged.add(message);
            }
        };

        this.applyAndCheck(
            TreeExpressionFunctionLoggingDebug.instance(),
            Lists.of(
                message
            ),
            context,
            null
        );

        this.checkEquals(
            Lists.of(message),
            logged,
            "logged"
        );
    }

    @Override
    public TreeExpressionFunctionLoggingDebug<FakeExpressionEvaluationContext> createBiFunction() {
        return TreeExpressionFunctionLoggingDebug.instance();
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "debug"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeExpressionFunctionLoggingDebug<FakeExpressionEvaluationContext>> type() {
        return Cast.to(TreeExpressionFunctionLoggingDebug.class);
    }
}
