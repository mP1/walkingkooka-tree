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
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.stream.StreamTesting;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberConverterContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public final class NodeSelectorTest implements ClassTesting2<NodeSelector<TestNode, StringName, StringName, Object>>,
        StreamTesting<Stream<TestNode>, TestNode> {

    @BeforeEach
    public void beforeEachTest() {
        TestNode.clear();
    }

    // Stream..........................................................................................................

    @Test
    public void testDescendantOrSelfStream() {
        TestNode.disableUniqueNameChecks();

        final TestNode root = TestNode.with("root",
                TestNode.with("branch1", TestNode.with("leaf1")),
                TestNode.with("branch2",
                        TestNode.with("leaf2"),
                        TestNode.with("leaf3")));

        this.collectAndCheck(() -> this.stream(TestNode.absoluteNodeSelector()
                        .descendantOrSelf(),
                root),
                root,
                root.child(0),
                root.child(0).child(0),
                root.child(1),
                root.child(1).child(0), // leaf2
                root.child(1).child(1)); // leaf3
    }

    @Test
    public void testDescendantOrSelfSkip1Limit2Stream() {
        TestNode.disableUniqueNameChecks();

        final TestNode root = TestNode.with("root",
                TestNode.with("branch1", TestNode.with("leaf1")),
                TestNode.with("branch2",
                        TestNode.with("leaf2"),
                        TestNode.with("leaf3")));

        this.collectAndCheck(() -> this.stream(TestNode.absoluteNodeSelector()
                        .descendantOrSelf(),
                root)
                        .skip(1)
                        .limit(2),
                //root, skip 1
                root.child(0), // branch1
                root.child(0).child(0) // leaf1
                //root.child(1),
                //root.child(1).child(0), // leaf2
                //root.child(1).child(1)
        ); // leaf3
    }

    @Test
    public void testDescendantOrSelfNamedStream() {
        TestNode.disableUniqueNameChecks();

        final TestNode root = TestNode.with("root",
                TestNode.with("branch1", TestNode.with("leaf")),
                TestNode.with("branch2",
                        TestNode.with("skip"),
                        TestNode.with("leaf")));

        this.collectAndCheck(() -> this.stream(TestNode.absoluteNodeSelector()
                        .descendantOrSelf()
                        .named(Names.string("leaf")),
                root),
                root.child(0).child(0),
                root.child(1).child(1));
    }

    // StreamTesting.....................................................................................................

    @Override
    public Stream<TestNode> createStream() {
        return this.stream(TestNode.absoluteNodeSelector()
                        .descendantOrSelf(),
                ROOT);
    }

    private Stream<TestNode> stream(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                    final TestNode node) {
        return selector.stream(node,
            this.expressionEvaluationContext(),
                TestNode.class);
    }

    private Function<NodeSelectorContext<TestNode, StringName, StringName, Object>, ExpressionEvaluationContext> expressionEvaluationContext() {
        return (c) -> ExpressionEvaluationContexts.fake();
    }

    @Override
    public List<TestNode> values() {
        final List<TestNode> nodes = Lists.array();

        nodes.add(ROOT);
        nodes.add(ROOT.child(0));
        nodes.add(ROOT.child(1));
        nodes.add(ROOT.child(1).child(0));
        nodes.add(ROOT.child(1).child(1));

        return nodes;
    }

    private final static TestNode ROOT = TestNode.with("root",
            TestNode.with("branch1"),
            TestNode.with("branch2",
                    TestNode.with("leaf1"),
                    TestNode.with("leaf2")));

    // ClassTesting.....................................................................................................

    @Override
    public Class<NodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(NodeSelector.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
