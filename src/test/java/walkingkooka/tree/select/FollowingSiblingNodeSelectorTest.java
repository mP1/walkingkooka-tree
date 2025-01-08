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

final public class FollowingSiblingNodeSelectorTest extends
    AxisNodeSelectorTestCase<FollowingSiblingNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testFollowingSiblingRoot() {
        this.applyAndCheck(TestNode.with("root"));
    }

    @Test
    public void testFollowingSiblingChildIgnoresParent() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(parent.child(0));
    }

    @Test
    public void testFollowingSiblingFirstChildWithFollowingSibling() {
        final TestNode self = TestNode.with("self");
        final TestNode following = TestNode.with("following");
        final TestNode parent = TestNode.with("parent", self, following);

        this.applyAndCheck(parent.child(0), following);
    }

    @Test
    public void testFollowingSiblingFirstChildWithFollowingSiblings2() {
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following2");
        final TestNode parent = TestNode.with("parent", self, following1, following2);

        this.applyAndCheck(parent.child(0), following1, following2);
    }

    @Test
    public void testFollowingSiblingLastChild() {
        final TestNode self = TestNode.with("self");
        final TestNode parent = TestNode.with("parent", TestNode.with("before"), self);

        this.applyAndCheck(parent.child(1));
    }

    @Test
    public void testFollowingSiblingIgnoresPreceeding() {
        final TestNode preceeding = TestNode.with("preceeding");
        final TestNode self = TestNode.with("self");
        final TestNode following = TestNode.with("following");

        final TestNode parent = TestNode.with("parent", preceeding, self, following);

        this.applyAndCheck(parent.child(1), following);
    }

    @Test
    public void testFollowingSiblingIgnoresPreceeding2() {
        final TestNode preceeding1 = TestNode.with("preceeding1");
        final TestNode preceeding2 = TestNode.with("preceeding2");
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following2");

        final TestNode parent = TestNode.with("parent", preceeding1, preceeding2, self, following1, following2);

        this.applyAndCheck(parent.child(2), following1, following2);
    }

    @Test
    public void testFollowingSiblingIgnoresChildren() {
        this.applyAndCheck(TestNode.with("parent", TestNode.with("child")));
    }

    @Test
    public void testFollowingSiblingGrandChildrenIgnored() {
        final TestNode preceeding = TestNode.with("preceeding");
        final TestNode self = TestNode.with("self");

        final TestNode followingGrandChild = TestNode.with("followingGrandChild");
        final TestNode following = TestNode.with("following", followingGrandChild);

        final TestNode parent = TestNode.with("parent", preceeding, self, following);

        this.applyAndCheck(parent.child(1), following);
    }

    @Test
    public void testFollowingSiblingNamed() {
        TestNode.disableUniqueNameChecks();

        final TestNode preceding1 = TestNode.with("preceding1");
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following", TestNode.with("ignored"));
        final TestNode following2 = TestNode.with("skip");
        final TestNode following3 = TestNode.with("following");

        final TestNode parent = TestNode.with("parent", preceding1, preceding2, self, following1, following2, following3);

        this.applyAndCheck(TestNode.relativeNodeSelector().followingSibling().named(following1.name()),
            parent.child(2),
            following1, following3);
    }

    @Test
    public void testFollowingSiblingWithAttributeEqualsValue() {
        final TestNode preceding1 = TestNode.with("preceding1");
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1", TestNode.with("ignored")).setAttributes(this.attributes("a1", "v1"));
        final TestNode following2 = nodeWithAttributes("following2", "a1", "v1");
        final TestNode following3 = nodeWithAttributes("following3", "a1", "different");
        final TestNode following4 = nodeWithAttributes("following4", "a1", "v1");

        final TestNode parent = TestNode.with("parent", preceding1, preceding2, self, following1, following2, following3, following4);

        this.applyAndCheck(TestNode.relativeNodeSelector().followingSibling().attributeValueEquals(Names.string("a1"), "v1"),
            parent.child(2),
            following1, following2, following4);
    }

    @Test
    public void testFollowingSiblingFilter() {
        final TestNode preceding1 = TestNode.with("preceding1");
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode self = TestNode.with("self", TestNode.with("ignored1"));
        final TestNode following1 = TestNode.with("following1", TestNode.with("ignored2"));
        final TestNode following2 = TestNode.with("following2");

        final TestNode parent = TestNode.with("parent", preceding1, preceding2, self, following1, following2);

        this.applyFilterAndCheck(TestNode.relativeNodeSelector()
                .followingSibling(),
            parent.child(2), // self!
            (n) -> n.children().size() > 0,
            following1);
    }

    @Test
    public void testFollowingSiblingFinished() {
        final TestNode preceding1 = TestNode.with("preceding1");
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode preceding3 = TestNode.with("preceding3");
        final TestNode self = TestNode.with("self");
        final TestNode following2 = TestNode.with("following2");
        final TestNode following1 = TestNode.with("following1", following2);
        final TestNode following3 = TestNode.with("following3");

        final TestNode parent = TestNode.with("parent", preceding3, preceding2, preceding1, self, following1, following3);

        this.applyFinisherAndCheck(this.createSelector(),
            parent.child(3), // self
            0);
    }

    @Test
    public void testFollowingSiblingFinishedCountdown() {
        final TestNode preceding1 = TestNode.with("preceding1");
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode preceding3 = TestNode.with("preceding3");
        final TestNode self = TestNode.with("self");
        final TestNode following2 = TestNode.with("following2");
        final TestNode following1 = TestNode.with("following1", following2);
        final TestNode following3 = TestNode.with("following3");

        final TestNode parent = TestNode.with("parent", preceding3, preceding2, preceding1, self, following1, following3);

        this.applyFinisherAndCheck(this.createSelector(),
            parent.child(3), // self
            1,
            following1);
    }

    @Test
    public void testFollowingSiblingFinishedCountdown2() {
        final TestNode preceding1 = TestNode.with("preceding1");
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode preceding3 = TestNode.with("preceding3");
        final TestNode self = TestNode.with("self");
        final TestNode following2 = TestNode.with("following2");
        final TestNode following1 = TestNode.with("following1", following2);
        final TestNode following3 = TestNode.with("following3");

        final TestNode parent = TestNode.with("parent", preceding3, preceding2, preceding1, self, following1, following3);

        this.applyFinisherAndCheck(this.createSelector(),
            parent.child(3), // self
            2,
            following1, following3);
    }

    @Test
    public void testFollowingSiblingMap() {
        final TestNode grandParent = TestNode.with("grand",
            TestNode.with("parent1",
                TestNode.with("child1"), TestNode.with("child2")),
            TestNode.with("parent2", TestNode.with("child3")),
            TestNode.with("parent3", TestNode.with("child4")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent.child(1),
            TestNode.with("grand",
                    TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                    TestNode.with("parent2", TestNode.with("child3")),
                    TestNode.with("parent3*0", TestNode.with("child4")))
                .child(1));
    }

    @Test
    public void testFollowingSiblingMapSeveralFollowingSiblings() {
        final TestNode grandParent = TestNode.with("grand",
            TestNode.with("parent1",
                TestNode.with("child1"), TestNode.with("child2")),
            TestNode.with("parent2", TestNode.with("child3")),
            TestNode.with("parent3", TestNode.with("child4")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent.child(0),
            TestNode.with("grand",
                    TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                    TestNode.with("parent2*0", TestNode.with("child3")),
                    TestNode.with("parent3*1", TestNode.with("child4")))
                .child(0));
    }

    @Test
    public void testFollowingSiblingMapWithoutFollowingSiblings() {
        this.acceptMapAndCheck(TestNode.with("node123"));
    }

    // NodeSelectorVisitor............................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<NodeSelector<TestNode, StringName, StringName, Object>> visited = Lists.array();

        final FollowingSiblingNodeSelector<TestNode, StringName, StringName, Object> selector = this.createSelector();
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
            protected Visiting startVisitFollowingSibling(final NodeSelector<TestNode, StringName, StringName, Object> s) {
                assertSame(selector, s, "selector");
                b.append("3");
                visited.add(s);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitFollowingSibling(final NodeSelector<TestNode, StringName, StringName, Object> s) {
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
        this.toStringAndCheck(this.createSelector(), "following-sibling::*");
    }

    @Override
    FollowingSiblingNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return FollowingSiblingNodeSelector.get();
    }

    @Override
    public Class<FollowingSiblingNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(FollowingSiblingNodeSelector.class);
    }
}
