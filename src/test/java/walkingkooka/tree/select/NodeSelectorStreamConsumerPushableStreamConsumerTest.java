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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NodeSelectorStreamConsumerPushableStreamConsumerTest implements ClassTesting2<NodeSelectorStreamConsumerPushableStreamConsumer<TestNode, StringName, StringName, Object>>,
    ToStringTesting<NodeSelectorStreamConsumerPushableStreamConsumer<TestNode, StringName, StringName, Object>>,
    TypeNameTesting<NodeSelectorStreamConsumerPushableStreamConsumer<TestNode, StringName, StringName, Object>> {

    @BeforeEach
    public void beforeEachTest() {
        TestNode.clear();
        this.node = TestNode.with("node"); // On github-actions CI, duplicate node names were being reported.
    }

    private final static NodeSelector<TestNode, StringName, StringName, Object> SELECTOR = TestNode.relativeNodeSelector()
        .named(
            Names.string("abc123")
        );

    private final static Function<NodeSelectorContext<TestNode, StringName, StringName, Object>, ExpressionEvaluationContext> EXPRESSION_EVALUATION_CONTEXT = (c) -> ExpressionEvaluationContexts.fake();

    private final static Class<TestNode> NODE_TYPE = TestNode.class;

    @Test
    public void testWithNullNodeFails() {
        assertThrows(
            NullPointerException.class,
            () -> NodeSelectorStreamConsumerPushableStreamConsumer.with(
                null,
                SELECTOR,
                EXPRESSION_EVALUATION_CONTEXT,
                NODE_TYPE
            )
        );
    }

    @Test
    public void testWithNullExpressionEvaluationContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> NodeSelectorStreamConsumerPushableStreamConsumer.with(
                this.node,
                SELECTOR,
                null,
                NODE_TYPE
            )
        );
    }

    @Test
    public void testWithNullNodeTypeFails() {
        assertThrows(
            NullPointerException.class,
            () -> NodeSelectorStreamConsumerPushableStreamConsumer.with(
                this.node,
                SELECTOR,
                EXPRESSION_EVALUATION_CONTEXT,
                null
            )
        );
    }

    @Test
    public void testToString() {
        final NodeSelector<TestNode, StringName, StringName, Object> selector = SELECTOR;

        this.toStringAndCheck(
            NodeSelectorStreamConsumerPushableStreamConsumer.with(
                this.node,
                selector,
                EXPRESSION_EVALUATION_CONTEXT,
                NODE_TYPE
            ),
            selector.toString()
        );
    }

    private TestNode node;

    // class............................................................................................................

    @Override
    public Class<NodeSelectorStreamConsumerPushableStreamConsumer<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(NodeSelectorStreamConsumerPushableStreamConsumer.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return NodeSelector.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return Consumer.class.getSimpleName();
    }
}
