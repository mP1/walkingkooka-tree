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

package walkingkooka.tree.select;

import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.select.NodeSelectorContextDelegatorTest.TestNodeSelectorContextDelegator;

import java.util.function.Function;

public final class NodeSelectorContextDelegatorTest implements NodeSelectorContextTesting2<TestNodeSelectorContextDelegator, TestNode, StringName, StringName, Object> {
    @Override
    public TestNodeSelectorContextDelegator createContext() {
        return new TestNodeSelectorContextDelegator();
    }

    @Override
    public Class<TestNodeSelectorContextDelegator> type() {
        return TestNodeSelectorContextDelegator.class;
    }

    final static class TestNodeSelectorContextDelegator implements NodeSelectorContextDelegator<TestNode, StringName, StringName, Object> {
        @Override
        public NodeSelectorContext<TestNode, StringName, StringName, Object> nodeSelectorContext() {
            return NodeSelectorContexts.basic(
                () -> false, // finisher
                Predicates.always(), // filter
                Function.identity(), // mapper
                (final TestNode testNode) -> NodeSelectorExpressionEvaluationContexts.fake(),
                TestNode.class
            );
        }

        @Override
        public String toString() {
            return this.getClass()
                .getSimpleName();
        }
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
