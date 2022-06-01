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

final public class DescendantNodeSelectorTest extends
        AxisNodeSelectorTestCase<DescendantNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testDescendantChildless() {
        this.applyAndCheck(TestNode.with("only"));
    }

    @Test
    public void testDescendantIgnoresParent() {
        final TestNode child = TestNode.with("child");
        TestNode.with("parent", child);

        this.applyAndCheck(child);
    }

    @Test
    public void testDescendantParentWithChildren() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");

        this.applyAndCheck(TestNode.with("parent", child1, child2), child1, child2);
    }

    @Test
    public void testDescendantParentWithGrandChildren() {
        final TestNode grandChild1 = TestNode.with("grandChild1");
        final TestNode grandChild2 = TestNode.with("grandChild2");

        final TestNode child1 = TestNode.with("child1", grandChild1);
        final TestNode child2 = TestNode.with("child2", grandChild2);

        this.applyAndCheck(TestNode.with("parent", child1, child2), child1, grandChild1, child2, grandChild2);
    }

    @Test
    public void testDescendantChildrenWithChildren2() {
        final TestNode grandChild1 = TestNode.with("grandChild1");
        final TestNode grandChild2 = TestNode.with("grandChild2");

        final TestNode child1 = TestNode.with("child1", grandChild1, grandChild2);
        final TestNode child2 = TestNode.with("child2");

        this.applyAndCheck(TestNode.with("parent", child1, child2), child1, grandChild1, grandChild2, child2);
    }

    @Test
    public void testDescendantIgnoresSiblings() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);
        final TestNode siblingOfParent = TestNode.with("siblingOfParent", TestNode.with("siblingOfParent-child"));
        TestNode.with("grandParent", parent, siblingOfParent);

        this.applyAndCheck(parent, child);
    }

    @Test
    public void testDescendantChildrenNamed() {
        TestNode.disableUniqueNameChecks();

        final TestNode grand1 = TestNode.with("grand1");
        final TestNode grand2 = TestNode.with("grand2");
        final TestNode child1 = TestNode.with("match", grand1, grand2);

        final TestNode grand3 = TestNode.with("grand3");
        final TestNode grand4 = TestNode.with("grand4");
        final TestNode grand5 = TestNode.with("match");
        final TestNode child2 = TestNode.with("match", grand3, grand4, grand5);

        this.applyAndCheck(TestNode.absoluteNodeSelector().descendant().named(child1.name()),
                TestNode.with("parent", child1, child2),
                child1, child2, grand5);
    }

    @Test
    public void testDescendantMapWithoutDescendants() {
        this.acceptMapAndCheck(TestNode.with("without"));
    }

    @Test
    public void testDescendantFilters() {
        TestNode.disableUniqueNameChecks();

        final TestNode grand1 = TestNode.with("grand1");
        final TestNode grand3 = TestNode.with("grand3");
        final TestNode child1 = TestNode.with("child1", grand1, TestNode.with("skip"), grand3);

        final TestNode grand4 = TestNode.with("grand4");
        final TestNode child2 = TestNode.with("child2", TestNode.with("skip"), grand4);

        this.applyFilterAndCheck(TestNode.relativeNodeSelector()
                        .descendant(),
                TestNode.with("parent", child1, child2),
                (n) -> !n.name().value().equals("skip"),
                child1, grand1, grand3, child2, grand4);
    }

    @Test
    public void testDescendantFilters2() {
        final TestNode child = TestNode.with("skip", TestNode.with("grand"));

        this.applyFilterAndCheck(TestNode.relativeNodeSelector()
                        .descendant(),
                TestNode.with("parent", child),
                (n) -> !n.name().value().equals("skip"));
    }

    @Test
    public void testDescendantFilters3() {
        final TestNode grand1 = TestNode.with("grand1");
        final TestNode child1 = TestNode.with("child1", grand1);
        final TestNode child2 = TestNode.with("skip", TestNode.with("grand2"));

        this.applyFilterAndCheck(TestNode.relativeNodeSelector()
                        .descendant(),
                TestNode.with("parent", child1, child2),
                (n) -> !n.name().value().equals("skip"),
                child1, grand1);
    }

    @Test
    public void testDescendantFinishedTrue() {
        this.applyFinisherAndCheck(this.createSelector(),
                TestNode.with("parent", TestNode.with("child")),
                () -> true);
    }

    @Test
    public void testDescendantFinishedCountdown() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child1 = TestNode.with("child1", grandChild);
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent = TestNode.with("parent", child1, child2);

        this.applyFinisherAndCheck(this.createSelector(),
                parent,
                1,
                child1);
    }

    @Test
    public void testDescendantFinishedCountdown2() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child1 = TestNode.with("child1", grandChild);
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent = TestNode.with("parent", child1, child2);

        this.applyFinisherAndCheck(this.createSelector(),
                parent,
                2,
                child1, grandChild);
    }

    @Test
    public void testDescendantMap() {
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
    public void testDescendantMap2() {
        final TestNode grandParent = TestNode.with("grand",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                TestNode.with("parent2", TestNode.with("child3")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent,
                TestNode.with("grand",
                        TestNode.with("parent1*0",
                                TestNode.with("child1*1"), TestNode.with("child2*2")),
                        TestNode.with("parent2*3", TestNode.with("child3*4"))));
    }

    // NodeSelectorVisitor............................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<NodeSelector<TestNode, StringName, StringName, Object>> visited = Lists.array();

        final DescendantNodeSelector<TestNode, StringName, StringName, Object> selector = this.createSelector();
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
            protected Visiting startVisitDescendant(final NodeSelector<TestNode, StringName, StringName, Object> s) {
                assertSame(selector, s, "selector");
                b.append("3");
                visited.add(s);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitDescendant(final NodeSelector<TestNode, StringName, StringName, Object> s) {
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
        this.toStringAndCheck(this.createSelector(), "descendant::*");
    }

    @Override
    DescendantNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return DescendantNodeSelector.get();
    }

    @Override
    public Class<DescendantNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(DescendantNodeSelector.class);
    }
}
