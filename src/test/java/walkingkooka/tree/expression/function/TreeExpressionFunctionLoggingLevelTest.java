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
import walkingkooka.logging.HasLoggingLevelTesting;
import walkingkooka.logging.LoggingLevel;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

public final class TreeExpressionFunctionLoggingLevelTest extends TreeExpressionFunctionTestCase<TreeExpressionFunctionLoggingLevel<FakeExpressionEvaluationContext>, FakeExpressionEvaluationContext, LoggingLevel>
    implements HasLoggingLevelTesting {

    @Test
    public void testApplyWithNoParametersAndContextDebug() {
        this.applyAndCheck2(
            LoggingLevel.DEBUG
        );
    }

    @Test
    public void testApplyWithNoParametersAndContextInfo() {
        this.applyAndCheck2(
            LoggingLevel.INFO
        );
    }

    private void applyAndCheck2(final LoggingLevel loggingLevel) {
        this.applyAndCheck(
            Lists.empty(),
            this.createContext(loggingLevel),
            loggingLevel
        );
    }

    @Test
    public void testApplyWithLoggingLevelDebugParameter() {
        this.applyAndCheck3(LoggingLevel.DEBUG);
    }

    @Test
    public void testApplyWithLoggingLevelInfoParameter() {
        this.applyAndCheck3(LoggingLevel.INFO);
    }

    private void applyAndCheck3(final LoggingLevel loggingLevel) {
        this.applyAndCheck(
            Lists.of(loggingLevel),
            new FakeExpressionEvaluationContext(),
            loggingLevel
        );
    }

    @Override
    public TreeExpressionFunctionLoggingLevel<FakeExpressionEvaluationContext> createBiFunction() {
        return TreeExpressionFunctionLoggingLevel.instance();
    }

    @Override
    public FakeExpressionEvaluationContext createContext() {
        return this.createContext(LOGGING_LEVEL);
    }

    private FakeExpressionEvaluationContext createContext(final LoggingLevel loggingLevel) {
        return new FakeExpressionEvaluationContext() {

            @Override
            public LoggingLevel loggingLevel() {
                return loggingLevel;
            }
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
            "loggingLevel"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeExpressionFunctionLoggingLevel<FakeExpressionEvaluationContext>> type() {
        return Cast.to(TreeExpressionFunctionLoggingLevel.class);
    }
}
