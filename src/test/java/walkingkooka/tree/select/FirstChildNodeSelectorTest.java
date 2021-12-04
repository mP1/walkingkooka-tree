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


final public class FirstChildNodeSelectorTest extends
        AxisNodeSelectorTestCase<FirstChildNodeSelector<TestNode, StringName, StringName, Object>> {

    // constants

    @Test
    public void testFirstChildChildless() {
        this.applyAndCheck(TestNode.with("childless"));
    }

    @Test
    public void testFirstChildOnlyChild() {
        final TestNode child = TestNode.with("child");
        this.applyAndCheck(TestNode.with("parent", child), child);
    }

    @Test
    public void testFirstChildTwoChildren() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        this.applyAndCheck(TestNode.with("parent", child1, child2), child1);
    }

    @Test
    public void testFirstChildManyChildren() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode child3 = TestNode.with("child3");

        this.applyAndCheck(TestNode.with("parent", child1, child2, child3), child1);
    }

    @Test
    public void testFirstChildIgnoresDescendants() {
        final TestNode child1 = TestNode.with("child1", TestNode.with("descendant1"));
        final TestNode child2 = TestNode.with("child2", TestNode.with("descendant2"));

        this.applyAndCheck(TestNode.with("parent", child1, child2), child1);
    }

    @Test
    public void testDescendantOrSelfFirstChild() {
        final TestNode grand1 = TestNode.with("grand1");
        final TestNode grand2 = TestNode.with("grand2");
        final TestNode child1 = TestNode.with("child1", grand1, grand2);

        final TestNode grand3 = TestNode.with("grand3");
        final TestNode grand4 = TestNode.with("grand4");
        final TestNode grand5 = TestNode.with("grand5");
        final TestNode child2 = TestNode.with("child2", grand3, grand4, grand5);

        this.applyAndCheck(TestNode.absoluteNodeSelector().descendantOrSelf().firstChild(),
                TestNode.with("parent", child1, child2),
                child1, grand1, grand3);
    }

    @Test
    public void testChildrenFirstChildFilter() {
        final TestNode grand1 = TestNode.with("grand1");
        final TestNode grand2 = TestNode.with("grand2");
        final TestNode child1 = TestNode.with("child1", grand1, grand2);

        final TestNode grand3 = TestNode.with("grand3");
        final TestNode grand4 = TestNode.with("grand4");
        final TestNode child2 = TestNode.with("child2", grand3, grand4);

        this.applyFilterAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .firstChild(),
                TestNode.with("parent", child1, child2),
                (n) -> !n.name().value().equals("grand3"),
                grand1);
    }

    @Test
    public void testFirstChildFinishedTrue() {
        this.applyFinisherAndCheck(this.createSelector(),
                TestNode.with("parent", TestNode.with("child")),
                () -> true);
    }

    @Test
    public void testFirstChildFinishedCountdown() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent = TestNode.with("parent", child1, child2);

        this.applyFinisherAndCheck(this.createSelector(),
                parent,
                1,
                child1);
    }

    @Test
    public void testFirstChildMap() {
        this.acceptMapAndCheck(TestNode.with("parent"));
    }

    @Test
    public void testFirstChildMap2() {
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child1"), TestNode.with("child2"), TestNode.with("child3"));

        this.acceptMapAndCheck(parent,
                parent.setChild(0, TestNode.with("child1*0")));
    }

    @Test
    public void testFirstChildMap3() {
        final TestNode grand = TestNode.with("grand-parent",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2"), TestNode.with("child3")),
                TestNode.with("parent2", TestNode.with("child4")));

        TestNode.clear();

        this.acceptMapAndCheck(grand.child(0),
                TestNode.with("grand-parent",
                        TestNode.with("parent1",
                                TestNode.with("child1*0"), TestNode.with("child2"), TestNode.with("child3")),
                        TestNode.with("parent2", TestNode.with("child4")))
                        .child(0));
    }

    // NodeSelectorVisitor............................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<NodeSelector> visited = Lists.array();

        final FirstChildNodeSelector<TestNode, StringName, StringName, Object> selector = this.createSelector();
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
            protected Visiting startVisitFirstChild(final NodeSelector<TestNode, StringName, StringName, Object> s) {
                assertSame(selector, s, "selector");
                b.append("3");
                visited.add(s);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitFirstChild(final NodeSelector<TestNode, StringName, StringName, Object> s) {
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
        this.toStringAndCheck(this.createSelector(), "first-child::*");
    }

    @Override
    FirstChildNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return FirstChildNodeSelector.get();
    }

    @Override
    public Class<FirstChildNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(FirstChildNodeSelector.class);
    }
}
