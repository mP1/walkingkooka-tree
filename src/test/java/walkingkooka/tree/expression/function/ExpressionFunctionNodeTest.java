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
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.select.FakeNodeSelectorExpressionEvaluationContext;
import walkingkooka.tree.select.NodeSelectorExpressionEvaluationContext;

public final class ExpressionFunctionNodeTest extends ExpressionFunctionTestCase<ExpressionFunctionNode<TestNode, StringName, StringName, Object, NodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object>>,
        NodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object>,
        TestNode> {

    private final static TestNode NODE = TestNode.with("test");

    @Test
    public void testExecuteFunction() {
        this.applyAndCheck2(
                this.createBiFunction(),
                parameters(),
                this.createContext(),
                NODE
        );
    }

    @Override
    public ExpressionFunctionNode<TestNode, StringName, StringName, Object, NodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object>> createBiFunction() {
        return ExpressionFunctionNode.instance();
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }

    @Override
    public NodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object> createContext() {
        return new FakeNodeSelectorExpressionEvaluationContext<>() {

            @Override
            public TestNode node() {
                return NODE;
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionFunctionNode<TestNode, StringName, StringName, Object, NodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object>>> type() {
        return Cast.to(ExpressionFunctionNode.class);
    }
}
