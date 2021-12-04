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

final public class ParentNodeSelectorTest extends
        AxisNodeSelectorTestCase<ParentNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testParentRoot() {
        this.applyAndCheck(TestNode.with("root"));
    }

    @Test
    public void testParentChildOfRoot() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(parent.child(0), parent);
    }

    @Test
    public void testParentIgnoresDescendants() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child", grandChild); //
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(parent.child(0), parent);
    }

    @Test
    public void testParentIgnoresSiblings() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child1 = TestNode.with("child1", grandChild); //
        final TestNode child2 = TestNode.with("child2"); //
        final TestNode parent = TestNode.with("parent", child1, child2);

        this.applyAndCheck(parent.child(1), parent);
    }

    @Test
    public void testParentIgnoresGrandParent() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child", grandChild); //
        TestNode.with("parent", child);

        this.applyAndCheck(child.child(0), child);
    }

    @Test
    public void testParentFilter() {
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child1"),
                TestNode.with("child2"));

        this.applyFilterAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .parent(),
                parent.child(0),
                (n) -> !n.name().value().equals("parent"));
    }

    @Test
    public void testParentFinishedTrue() {
        this.applyFinisherAndCheck(this.createSelector(),
                TestNode.with("parent", TestNode.with("child")).child(0),
                () -> true);
    }

    @Test
    public void testParentFinishedCountdown() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);
        final TestNode grandParent = TestNode.with("grandParent", parent);

        this.applyFinisherAndCheck(this.createSelector(),
                grandParent.child(0).child(0), // child
                1,
                parent);
    }

    @Test
    public void testParentFinishedCountdown2() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);
        final TestNode grandParent = TestNode.with("grandParent", parent);

        this.applyFinisherAndCheck(this.createSelector(),
                grandParent.child(0).child(0), // child
                2,
                parent);
    }

    @Test
    public void testParentMap() {
        final TestNode parent = TestNode.with("parent", TestNode.with("child"));

        TestNode.clear();

        this.acceptMapAndCheck(parent.child(0),
                TestNode.with("parent*0", TestNode.with("child"))
                        .child(0));
    }

    @Test
    public void testParentMapWithoutParent() {
        final TestNode parent = TestNode.with("parent", TestNode.with("child"));

        TestNode.clear();

        this.acceptMapAndCheck(parent);
    }

    // NodeSelectorVisitor............................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<NodeSelector> visited = Lists.array();

        final ParentNodeSelector<TestNode, StringName, StringName, Object> selector = this.createSelector();
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
            protected Visiting startVisitParent(final NodeSelector<TestNode, StringName, StringName, Object> s) {
                assertSame(selector, s, "selector");
                b.append("3");
                visited.add(s);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitParent(final NodeSelector<TestNode, StringName, StringName, Object> s) {
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

    // Object.......................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "..");
    }

    @Override
    ParentNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return ParentNodeSelector.get();
    }

    @Override
    public Class<ParentNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(ParentNodeSelector.class);
    }
}
