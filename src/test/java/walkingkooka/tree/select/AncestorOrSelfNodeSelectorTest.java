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
import walkingkooka.predicate.Predicates;
import walkingkooka.tree.TestNode;
import walkingkooka.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

final public class AncestorOrSelfNodeSelectorTest extends
        AxisNodeSelectorTestCase<AncestorOrSelfNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testAncestorOrSelfRoot() {
        final TestNode root = TestNode.with("root");
        this.applyAndCheck(root, root);
    }

    @Test
    public void testAncestorOrSelfFromParent() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(parent, parent);
    }

    @Test
    public void testAncestorOrSelfFromGrandChild() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child", grandChild);
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(parent.child(0).child(0), grandChild, child, parent);
    }

    @Test
    public void testAncestorOrSelfIgnoresDescendants() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(parent, parent);
    }

    @Test
    public void testAncestorOrSelfIgnoresSiblings() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent = TestNode.with("parent", child1, child2);

        this.applyAndCheck(parent.child(0), child1, parent);
    }

    @Test
    public void testAncestorOrSelfFilters() {
        final TestNode grandParent = TestNode.with("grandParent",
                TestNode.with("parent",
                        TestNode.with("child")));

        this.applyFilterAndCheck(TestNode.relativeNodeSelector()
                        .ancestorOrSelf(),
                grandParent.child(0).child(0), // child
                Predicates.never());
    }

    @Test
    public void testAncestorOrSelfFilters2() {
        final TestNode grandParent = TestNode.with("grandParent",
                TestNode.with("parent",
                        TestNode.with("child")));

        this.applyFilterAndCheck(TestNode.relativeNodeSelector()
                        .ancestorOrSelf(),
                grandParent.child(0).child(0), // child
                (n) -> !n.name().value().startsWith("grandParent"),
                "child", "parent");
    }

    @Test
    public final void testAncestorOrSelfFinishedTrue() {
        this.applyFinisherAndCheck(this.createSelector(),
                TestNode.with("parent", TestNode.with("child")).child(0),
                () -> true);
    }

    @Test
    public final void testAncestorOrSelfFinishedCountdown() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);
        final TestNode grandParent = TestNode.with("grandParent", parent);

        this.applyFinisherAndCheck(this.createSelector(),
                grandParent.child(0).child(0),
                1,
                child);
    }

    @Test
    public final void testAncestorOrSelfFinishedCountdown2() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);
        final TestNode grandParent = TestNode.with("grandParent", parent);

        this.applyFinisherAndCheck(this.createSelector(),
                grandParent.child(0).child(0),
                2,
                child, parent);
    }

    @Test
    public void testAncestorOrSelfMap() {
        final TestNode grandParent = TestNode.with("grand",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                TestNode.with("parent2", TestNode.with("child3")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent.child(0),
                TestNode.with("grand*1",
                        TestNode.with("parent1*0",
                                TestNode.with("child1"), TestNode.with("child2")),
                        TestNode.with("parent2", TestNode.with("child3")))
                        .child(0));
    }

    // NodeSelectorVisitor............................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<NodeSelector> visited = Lists.array();

        final AncestorOrSelfNodeSelector<TestNode, StringName, StringName, Object> selector = this.createSelector();
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
            protected Visiting startVisitAncestorOrSelf(final NodeSelector<TestNode, StringName, StringName, Object> s) {
                assertSame(selector, s, "selector");
                b.append("3");
                visited.add(s);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitAncestorOrSelf(final NodeSelector<TestNode, StringName, StringName, Object> s) {
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

        assertEquals("1315242", b.toString());

        assertEquals(Lists.of(selector, selector,
                next, next, next,
                selector, selector),
                visited,
                "visited");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "ancestor-or-self::*");
    }

    @Override
    AncestorOrSelfNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return AncestorOrSelfNodeSelector.get();
    }

    @Override
    public Class<AncestorOrSelfNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(AncestorOrSelfNodeSelector.class);
    }
}
