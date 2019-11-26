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

package walkingkooka.tree.select;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NodeSelectorStreamConsumerPushableStreamConsumerTest extends NodeSelectorTestCase2<NodeSelectorStreamConsumerPushableStreamConsumer<TestNode, StringName, StringName, Object>>
        implements ToStringTesting {

    @BeforeEach
    public void beforeEachTest() {
        TestNode.clear();
    }

    @Test
    public void testWithNullNodeFails() {
        assertThrows(NullPointerException.class, () -> NodeSelectorStreamConsumerPushableStreamConsumer.with(null,
                this.selector(),
                this.functions(),
                this.converter(),
                this.converterContext(),
                this.nodeType()));
    }

    // no TestWithNullSelector because NodeSelector.stream

    @Test
    public void testWithNullFunctionsFails() {
        assertThrows(NullPointerException.class, () -> NodeSelectorStreamConsumerPushableStreamConsumer.with(this.node,
                this.selector(),
                null,
                this.converter(),
                this.converterContext(),
                this.nodeType()));
    }

    @Test
    public void testWithNullConverterFails() {
        assertThrows(NullPointerException.class, () -> NodeSelectorStreamConsumerPushableStreamConsumer.with(this.node,
                this.selector(),
                this.functions(),
                null,
                this.converterContext(),
                this.nodeType()));
    }

    @Test
    public void testWithNullConverterContextFails() {
        assertThrows(NullPointerException.class, () -> NodeSelectorStreamConsumerPushableStreamConsumer.with(this.node,
                this.selector(),
                this.functions(),
                this.converter(),
                null,
                this.nodeType()));
    }

    @Test
    public void testWithNullNodeTypeFails() {
        assertThrows(NullPointerException.class, () -> NodeSelectorStreamConsumerPushableStreamConsumer.with(this.node,
                this.selector(),
                this.functions(),
                this.converter(),
                this.converterContext(),
                null));
    }

    @Test
    public void testToString() {
        final NodeSelector<TestNode, StringName, StringName, Object> selector = this.selector();

        this.toStringAndCheck(NodeSelectorStreamConsumerPushableStreamConsumer.with(this.node,
                selector,
                this.functions(),
                this.converter(),
                this.converterContext(),
                this.nodeType()),
                selector.toString());
    }

    private final TestNode node = TestNode.with("node");

    private NodeSelector<TestNode, StringName, StringName, Object> selector() {
        return TestNode.relativeNodeSelector().named(Names.string("abc123"));
    }

    private Function<FunctionExpressionName, Optional<ExpressionFunction<?>>> functions() {
        return NodeSelectorContexts.basicFunctions();
    }

    private Converter converter() {
        return Converters.fake();
    }

    private ConverterContext converterContext() {
        return ConverterContexts.fake();
    }

    private Class<TestNode> nodeType() {
        return TestNode.class;
    }

    @Override
    public Class<NodeSelectorStreamConsumerPushableStreamConsumer<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(NodeSelectorStreamConsumerPushableStreamConsumer.class);
    }

    @Override
    public String typeNameSuffix() {
        return Consumer.class.getSimpleName();
    }
}
