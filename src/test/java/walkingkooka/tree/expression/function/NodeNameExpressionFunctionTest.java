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
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.FakeNode;

public final class NodeNameExpressionFunctionTest implements ClassTesting2<NodeNameExpressionFunction>,
        ExpressionFunctionTesting<NodeNameExpressionFunction, String> {

    private final static String NAME = "Abc123";

    @Test
    public void testExecuteFunction() {
        this.applyAndCheck2(this.createBiFunction(),
                parameters(new TestFakeNode()),
                NAME);
    }

    final static class TestFakeNode extends FakeNode<TestFakeNode, StringName, StringName, Object> {

        @Override
        public StringName name() {
            return Names.string(NAME);
        }
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "name");
    }

    @Override
    public NodeNameExpressionFunction createBiFunction() {
        return NodeNameExpressionFunction.INSTANCE;
    }

    @Override
    public Class<NodeNameExpressionFunction> type() {
        return NodeNameExpressionFunction.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
