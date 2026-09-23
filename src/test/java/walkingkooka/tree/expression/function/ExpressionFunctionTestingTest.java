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
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.PublicClassTesting;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;

import java.util.List;

public final class ExpressionFunctionTestingTest implements ExpressionFunctionTesting,
    PublicClassTesting<ExpressionFunctionTesting> {

    // applyAndCheck ...................................................................................................

    @Test
    public void testApplyAndCheck() {
        final List<Object> parameters = Lists.of(true, 1, "***parameters3***");
        final String expected = "**123**";

        this.applyAndCheck(
            new FakeExpressionFunction<>() {

                @Override
                public Class<String> returnType() {
                    return String.class;
                }

                @Override
                public String apply(final List<Object> parameters,
                                    final ExpressionEvaluationContext context) {
                    return expected;
                }
            },
            parameters,
            ExpressionEvaluationContexts.fake(),
            expected
        );
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionFunctionTesting> type() {
        return ExpressionFunctionTesting.class;
    }
}
