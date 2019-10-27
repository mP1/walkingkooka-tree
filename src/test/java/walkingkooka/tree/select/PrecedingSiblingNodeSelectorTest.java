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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

final public class PrecedingSiblingNodeSelectorTest extends
        AxisNodeSelectorTestCase<PrecedingSiblingNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testPrecedingSiblingRoot() {
        this.applyAndCheck(TestNode.with("root"));
    }

    @Test
    public void testPrecedingSiblingOnlyChild() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(parent.child(0));
    }

    @Test
    public void testPrecedingSiblingFirstChild() {
        final TestNode self = TestNode.with("self");
        final TestNode following = TestNode.with("following");
        final TestNode parent = TestNode.with("parent", self, following);

        this.applyAndCheck(parent.child(0));
    }

    @Test
    public void testPrecedingSiblingLastChild() {
        final TestNode preceding = TestNode.with("preceding");
        final TestNode self = TestNode.with("self");
        final TestNode parent = TestNode.with("parent", preceding, self);

        this.applyAndCheck(parent.child(1), preceding);
    }

    @Test
    public void testPrecedingSiblingIgnoresGrandChildren() {
        final TestNode preceding = TestNode.with("preceding", TestNode.with("grandchild-of-preceding"));
        final TestNode self = TestNode.with("self");
        final TestNode parent = TestNode.with("parent", preceding, self);

        this.applyAndCheck(parent.child(1), preceding);
    }

    @Test
    public void testPrecedingSiblingMiddleChildIgnoresFollowing() {
        final TestNode preceding = TestNode.with("preceding");
        final TestNode self = TestNode.with("self");
        final TestNode following = TestNode.with("following");

        final TestNode parent = TestNode.with("parent", preceding, self, following);

        this.applyAndCheck(parent.child(1), preceding);
    }

    @Test
    public void testPrecedingSiblingMiddleChild2() {
        final TestNode preceding1 = TestNode.with("preceding1");
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following2");

        final TestNode parent = TestNode.with("parent", preceding1, preceding2, self, following1, following2);

        this.applyAndCheck(parent.child(2), preceding2, preceding1);
    }

    @Test
    public void testPrecedingSiblingIgnoresChildren() {
        this.applyAndCheck(TestNode.with("parent", TestNode.with("child")));
    }

    @Test
    public void testPrecedingSiblingNamed() {
        TestNode.disableUniqueNameChecks();

        final TestNode preceding3 = TestNode.with("preceding", TestNode.with("ignored"));
        final TestNode preceding2 = TestNode.with("skip");
        final TestNode preceding1 = TestNode.with("preceding");
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following1");

        final TestNode parent = TestNode.with("parent", preceding3, preceding2, preceding1, self, following1, following2);

        this.applyAndCheck(TestNode.relativeNodeSelector().precedingSibling().named(preceding1.name()),
                parent.child(3),
                preceding1, preceding3);
    }

    @Test
    public void testPrecedingSiblingWithAttributeEqualsValue() {
        final TestNode preceding4 = TestNode.with("preceding4", TestNode.with("ignored")).setAttributes(this.attributes("a1", "v1"));
        final TestNode preceding3 = nodeWithAttributes("preceding3", "a1", "v1");
        final TestNode preceding2 = nodeWithAttributes("preceding2", "a1", "different");
        final TestNode preceding1 = nodeWithAttributes("preceding1", "a1", "v1");
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following2");

        final TestNode parent = TestNode.with("parent", preceding4, preceding3, preceding2, preceding1, self, following1, following2);

        this.applyAndCheck(TestNode.relativeNodeSelector().precedingSibling().attributeValueEquals(Names.string("a1"), "v1"),
                parent.child(4),
                preceding1, preceding3, preceding4);
    }

    @Test
    public void testPrecedingSiblingFilter() {
        final TestNode preceding1 = TestNode.with("preceding1", TestNode.with("ignored2"));
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode self = TestNode.with("self", TestNode.with("ignored1"));
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following2");

        final TestNode parent = TestNode.with("parent", preceding2, preceding1, self, following1, following2);

        this.applyFilterAndCheck(TestNode.relativeNodeSelector()
                        .preceding(),
                parent.child(2), // self!
                (n) -> n.children().size() > 0,
                preceding1);
    }


    @Test
    public void testPrecedingSiblingFinished() {
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode preceding1 = TestNode.with("preceding1", preceding2);
        final TestNode preceding3 = TestNode.with("preceding3");
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following2");
        final TestNode following3 = TestNode.with("following3");

        final TestNode parent = TestNode.with("parent", preceding3, preceding1, self, following1, following2, following3);

        this.applyFinisherAndCheck(this.createSelector(),
                parent.child(2), // self
                0);
    }

    @Test
    public void testPrecedingSiblingFinishedCountdown() {
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode preceding1 = TestNode.with("preceding1", preceding2);
        final TestNode preceding3 = TestNode.with("preceding3");
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following2");
        final TestNode following3 = TestNode.with("following3");

        final TestNode parent = TestNode.with("parent", preceding3, preceding1, self, following1, following2, following3);

        this.applyFinisherAndCheck(this.createSelector(),
                parent.child(2), // self
                1,
                preceding1);
    }

    @Test
    public void testPrecedingSiblingFinishedCountdown2() {
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode preceding1 = TestNode.with("preceding1", preceding2);
        final TestNode preceding3 = TestNode.with("preceding3");
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following2");
        final TestNode following3 = TestNode.with("following3");

        final TestNode parent = TestNode.with("parent", preceding3, preceding1, self, following1, following2, following3);

        this.applyFinisherAndCheck(this.createSelector(),
                parent.child(2), // self
                2,
                preceding1, preceding3);
    }

    @Test
    public void testPrecedingSiblingMap() {
        final TestNode grandParent = TestNode.with("grand",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                TestNode.with("parent2", TestNode.with("child3")),
                TestNode.with("parent3", TestNode.with("child4")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent.child(1),
                TestNode.with("grand",
                        TestNode.with("parent1*0",
                                TestNode.with("child1"), TestNode.with("child2")),
                        TestNode.with("parent2", TestNode.with("child3")),
                        TestNode.with("parent3", TestNode.with("child4")))
                        .child(1));
    }

    @Test
    public void testPrecedingSiblingMapSeveralPrecedingSiblings() {
        final TestNode grandParent = TestNode.with("grand",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                TestNode.with("parent2", TestNode.with("child3")),
                TestNode.with("parent3", TestNode.with("child4")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent.child(2),
                TestNode.with("grand",
                        TestNode.with("parent1*1",
                                TestNode.with("child1"), TestNode.with("child2")),
                        TestNode.with("parent2*0", TestNode.with("child3")),
                        TestNode.with("parent3", TestNode.with("child4")))
                        .child(2));
    }

    @Test
    public void testPrecedingSiblingMapWithoutPrecedingSiblings() {
        this.acceptMapAndCheck(TestNode.with("node123"));
    }

    // NodeSelectorVisitor............................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<NodeSelector> visited = Lists.array();

        final PrecedingSiblingNodeSelector<TestNode, StringName, StringName, Object> selector = this.createSelector();
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
            protected Visiting startVisitPrecedingSibling(final NodeSelector<TestNode, StringName, StringName, Object> s) {
                assertSame(selector, s, "selector");
                b.append("3");
                visited.add(s);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitPrecedingSibling(final NodeSelector<TestNode, StringName, StringName, Object> s) {
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

    // Object.......................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "preceding-sibling::*");
    }

    @Override
    PrecedingSiblingNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return PrecedingSiblingNodeSelector.get();
    }

    @Override
    public Class<PrecedingSiblingNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(PrecedingSiblingNodeSelector.class);
    }
}
