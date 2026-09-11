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
import walkingkooka.logging.LoggingLevel;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

import java.util.List;

public final class TreeExpressionFunctionLogTest extends TreeExpressionFunctionTestCase<TreeExpressionFunctionLog<FakeExpressionEvaluationContext>, FakeExpressionEvaluationContext, Void> {

    @Test
    public void testApplyWithString() {
        final LoggingLevel loggingLevel = LoggingLevel.INFO;
        final String message = "Message 123";

        final List<Object> logged = Lists.array();

        this.applyAndCheck(
            TreeExpressionFunctionLog.instance(),
            Lists.of(
                loggingLevel,
                message
            ),
            new FakeExpressionEvaluationContext() {
                @Override
                public void log(final LoggingLevel loggingLevel,
                                final String message) {
                    logged.add(loggingLevel);
                    logged.add(message);
                }
            },
            null
        );

        this.checkEquals(
            Lists.of(
                loggingLevel,
                message
            ),
            logged
        );
    }

    @Override
    public TreeExpressionFunctionLog<FakeExpressionEvaluationContext> createBiFunction() {
        return TreeExpressionFunctionLog.instance();
    }

    @Override
    public FakeExpressionEvaluationContext createContext() {
        return new FakeExpressionEvaluationContext() {

        };
    }

    @Override
    public int minimumParameterCount() {
        return 2;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "log"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeExpressionFunctionLog<FakeExpressionEvaluationContext>> type() {
        return Cast.to(TreeExpressionFunctionLog.class);
    }
}
