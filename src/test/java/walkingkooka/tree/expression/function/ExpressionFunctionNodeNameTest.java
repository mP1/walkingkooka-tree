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
import walkingkooka.Either;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.tree.FakeNode;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

public final class ExpressionFunctionNodeNameTest extends ExpressionFunctionTestCase<ExpressionFunctionNodeName<ExpressionEvaluationContext>,
        ExpressionEvaluationContext,
        String> {

    private final static String NAME = "Abc123";

    @Test
    public void testExecuteFunction() {
        this.applyAndCheck2(this.createBiFunction(),
                parameters(new TestFakeNode()),
                this.createContext(),
                NAME);
    }

    final static class TestFakeNode extends FakeNode<TestFakeNode, StringName, StringName, Object> {

        @Override
        public StringName name() {
            return Names.string(NAME);
        }
    }

    @Override
    public ExpressionFunctionNodeName<ExpressionEvaluationContext> createBiFunction() {
        return ExpressionFunctionNodeName.instance();
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }

    @Override
    public ExpressionEvaluationContext createContext() {
        return new FakeExpressionEvaluationContext() {
            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> target) {
                checkEquals(true, value instanceof TestFakeNode, "value " + value);
                checkEquals(Node.class, target, "target");

                return Either.left(target.cast(value));
            }
        };
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createBiFunction(),
                "name"
        );
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionFunctionNodeName<ExpressionEvaluationContext>> type() {
        return Cast.to(ExpressionFunctionNodeName.class);
    }
}
