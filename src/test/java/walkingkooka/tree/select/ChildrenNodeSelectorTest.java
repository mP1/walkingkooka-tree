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
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;
import walkingkooka.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

final public class ChildrenNodeSelectorTest
        extends AxisNodeSelectorTestCase<ChildrenNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testChildrenChildless() {
        this.applyAndCheck(TestNode.with("childless"));
    }

    @Test
    public void testChildrenChildWithParent() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent").setChildren(Lists.of(child));

        this.applyAndCheck(parent.child(0));
    }

    @Test
    public void testChildrenParentWithChild() {
        final TestNode child = TestNode.with("child");

        this.applyAndCheck(TestNode.with("parent of one", child), child);
    }

    @Test
    public void testChildrenManyChildren() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");

        this.applyAndCheck(TestNode.with("parent of many children", child1, child2), child1, child2);
    }

    @Test
    public void testChildrenIgnoresGrandChild() {
        final TestNode grandChild = TestNode.with("grandchild");
        final TestNode child = TestNode.with("child", grandChild);
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(parent, child);
    }

    @Test
    public void testChildrenIgnoresSiblings() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(parent, child);
    }

    @Test
    public void testChildrenFilter() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode child3 = TestNode.with("child3");
        final TestNode parent = TestNode.with("parent", child1, child2, child3);

        this.applyFilterAndCheck(TestNode.relativeNodeSelector().children(),
                parent,
                (n) -> !n.name().equals(child2.name()),
                child1, child3);
    }

    @Test
    public void testDescendantOrSelfChildren() {
        final TestNode grand1 = TestNode.with("grand1");
        final TestNode grand2 = TestNode.with("grand2");
        final TestNode child1 = TestNode.with("child1", grand1, grand2);

        final TestNode grand3 = TestNode.with("grand3");
        final TestNode grand4 = TestNode.with("grand4");
        final TestNode grand5 = TestNode.with("grand5");
        final TestNode child2 = TestNode.with("child2", grand3, grand4, grand5);

        this.applyAndCheck(TestNode.absoluteNodeSelector().descendantOrSelf().children(),
                TestNode.with("parent", child1, child2),
                child1, child2, grand1, grand2, grand3, grand4, grand5);
    }

    @Test
    public void testDescendantOrSelfNamedChildren() {
        TestNode.disableUniqueNameChecks();

        final TestNode grand1 = TestNode.with("grand1");
        final TestNode grand2 = TestNode.with("grand2");
        final TestNode child1 = TestNode.with("child", grand1, grand2);

        final TestNode grand3 = TestNode.with("grand3");
        final TestNode grand4 = TestNode.with("grand4");
        final TestNode grand5 = TestNode.with("grand5");
        final TestNode child3 = TestNode.with("child", grand3, grand4, grand5);

        this.applyAndCheck(TestNode.absoluteNodeSelector().descendantOrSelf().named(child1.name()).children(),
                TestNode.with("parent", child1, TestNode.with("skip", TestNode.with("skip2")), child3),
                grand1, grand2, grand3, grand4, grand5);
    }

    @Test
    public void testChildrenMapWithoutChildren() {
        this.acceptMapAndCheck(TestNode.with("without-children"));
    }

    @Test
    public void testChildrenFinishedTrue2() {
        this.applyFinisherAndCheck(this.createSelector(),
                TestNode.with("parent", TestNode.with("child")),
                () -> true);
    }

    @Test
    public void testChildrenFinishedCountdown() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent = TestNode.with("parent", child1, child2);

        this.applyFinisherAndCheck(this.createSelector(),
                parent,
                1,
                child1);
    }

    @Test
    public void testChildrenFinishedCountdown2() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode child3 = TestNode.with("child3");
        final TestNode parent = TestNode.with("parent", child1, child2, child3);

        this.applyFinisherAndCheck(this.createSelector(),
                parent,
                2,
                child1, child2);
    }

    @Test
    public void testChildrenMap() {
        final TestNode grandParent = TestNode.with("grand",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                TestNode.with("parent2", TestNode.with("child3")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent.child(0),
                TestNode.with("grand",
                        TestNode.with("parent1",
                                TestNode.with("child1*0"), TestNode.with("child2*1")),
                        TestNode.with("parent2", TestNode.with("child3")))
                        .child(0));
    }

    @Test
    public void testChildrenMap2() {
        final TestNode grandParent = TestNode.with("grand",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                TestNode.with("parent2", TestNode.with("child3")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent,
                TestNode.with("grand",
                        TestNode.with("parent1*0",
                                TestNode.with("child1"), TestNode.with("child2")),
                        TestNode.with("parent2*1", TestNode.with("child3"))));
    }

    // NodeSelectorVisitor............................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<NodeSelector> visited = Lists.array();

        final ChildrenNodeSelector<TestNode, StringName, StringName, Object> selector = this.createSelector();
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
            protected Visiting startVisitChildren(final NodeSelector<TestNode, StringName, StringName, Object> s) {
                assertSame(selector, s, "selector");
                b.append("3");
                visited.add(s);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitChildren(final NodeSelector<TestNode, StringName, StringName, Object> s) {
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

    // toString....................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "child::*");
    }

    @Override
    ChildrenNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return ChildrenNodeSelector.get();
    }

    @Override
    public Class<ChildrenNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(ChildrenNodeSelector.class);
    }
}
