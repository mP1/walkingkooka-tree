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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;
import walkingkooka.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

final public class SelfNodeSelectorTest extends
        AxisNodeSelectorTestCase<SelfNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testSelf() {
        final TestNode node = TestNode.with("self");
        this.applyAndCheck(node, node);
    }

    @Test
    public void testSelfAndDescendant() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(selfAndDescendant(),
                parent,
                child);
    }

    @Test
    public void testSelfAndDescendant2() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(selfAndDescendant(),
                parent.child(0));
    }

    private static NodeSelector<TestNode, StringName, StringName, Object> selfAndDescendant() {
        return TestNode.relativeNodeSelector()
                .self()
                .descendant();
    }

    @Test
    public void testSelfAndNamed() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(selfAndNamed(),
                parent);
    }

    @Test
    public void testSelfAndNamed2() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(selfAndNamed(),
                parent.child(0),
                child);
    }

    @Test
    public void testSelfFilter() {
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child1"),
                TestNode.with("child2"));

        this.applyFilterAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .parent(),
                parent,
                (n) -> false);
    }

    @Test
    public void testSelfMap() {
        final TestNode parent = TestNode.with("parent", TestNode.with("child"));

        TestNode.clear();

        this.acceptMapAndCheck(parent,
                TestNode.with("parent*0", TestNode.with("child")));
    }

    private static NodeSelector<TestNode, StringName, StringName, Object> selfAndNamed() {
        return TestNode.relativeNodeSelector()
                .self()
                .named(Names.string("child"));
    }

    // NodeSelectorVisitor............................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<NodeSelector<TestNode, StringName, StringName, Object>> visited = Lists.array();

        final SelfNodeSelector<TestNode, StringName, StringName, Object> selector = this.createSelector();
        final NodeSelector<TestNode, StringName, StringName, Object> next = selector.next;

        new FakeNodeSelectorVisitor<TestNode, StringName, StringName, Object>() {
            @Override
            protected Visiting startVisit(final NodeSelector<TestNode, StringName, StringName, Object> s) {
                b.append("1");
                visited.add(s);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodeSelector<TestNode, StringName, StringName, Object> s) {
                b.append("2");
                visited.add(s);
            }

            @Override
            protected Visiting startVisitSelf(final NodeSelector<TestNode, StringName, StringName, Object> s) {
                assertSame(selector, s, "selector");
                b.append("3");
                visited.add(s);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitSelf(final NodeSelector<TestNode, StringName, StringName, Object> s) {
                assertSame(selector, s, "selector");
                b.append("4");
                visited.add(s);
            }

            @Override
            protected void visitTerminal(final NodeSelector<TestNode, StringName, StringName, Object> s) {
                assertSame(next, s);
                b.append("5");
                visited.add(s);
            }
        }.accept(selector);

        this.checkEquals("1315242", b.toString());

        this.checkEquals(Lists.of(selector, selector,
                        next, next, next,
                        selector, selector),
                visited,
                "visited");
    }

    // Object....................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), ".");
    }

    @Override
    SelfNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return SelfNodeSelector.get();
    }

    @Override
    public Class<SelfNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(SelfNodeSelector.class);
    }
}
