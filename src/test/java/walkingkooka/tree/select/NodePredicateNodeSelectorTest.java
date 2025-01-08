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
import walkingkooka.predicate.Predicates;
import walkingkooka.tree.TestNode;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;


final public class NodePredicateNodeSelectorTest extends
    NamedOrNodePredicateNodeSelectorTestCase<NodePredicateNodeSelector<TestNode, StringName, StringName, Object>> {

    // constants

    private final static String MAGIC_VALUE = "self";
    private final static Predicate<TestNode> PREDICATE = (n) -> n.name().value().equals(MAGIC_VALUE);

    @Test
    public void testWithNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> NodePredicateNodeSelector.with(null));
    }

    @Test
    public void testPredicate() {
        final TestNode self = TestNode.with("self");
        this.applyAndCheck(this.createSelector2(),
            self,
            self);
    }

    @Test
    public void testPredicateIgnoresNonSelfNodes() {
        final TestNode siblingBefore = TestNode.with("siblingBefore");
        final TestNode self = TestNode.with(MAGIC_VALUE, TestNode.with("child"));
        final TestNode siblingAfter = TestNode.with("siblingAfter");
        final TestNode parent = TestNode.with("parent", siblingBefore, self, siblingAfter);

        this.applyAndCheck(this.createSelector2(),
            parent.child(1),
            self);
    }

    @Test
    public void testDescendantOrSelfPredicate() {
        final TestNode grand1 = nodeWithAttributes("grand1", "a1", "v1");
        final TestNode grand2 = TestNode.with("grand2");
        final TestNode child1 = TestNode.with("child1", grand1, grand2).setAttributes(this.attributes("a1", "v1"));

        final TestNode grand3 = nodeWithAttributes("grand3", "different", "v1");
        final TestNode grand4 = nodeWithAttributes("grand4", "a1", "v1");
        final TestNode grand5 = TestNode.with("grand5");
        final TestNode child2 = TestNode.with("child2", grand3, grand4, grand5);

        this.applyAndCheck(TestNode.absoluteNodeSelector().descendantOrSelf().attributeValueEquals(Names.string("a1"), "v1"),
            TestNode.with("parent", child1, child2),
            child1, grand1, grand4);
    }

    @Test
    public void testPredicateChildren() {
        final TestNode grand1 = nodeWithAttributes("grand1", "a1", "v1");
        final TestNode grand2 = TestNode.with("grand2");
        final TestNode child1 = TestNode.with("child1", grand1, grand2).setAttributes(this.attributes("a1", "v1"));

        this.applyAndCheck(TestNode.relativeNodeSelector().attributeValueEquals(Names.string("a1"), "v1").children(),
            child1,
            grand1, grand2);
    }

    @Test
    public void testDescendantOrSelfPredicateChildren() {
        final TestNode grand1 = nodeWithAttributes("grand1", "a1", "v1");
        final TestNode grand2 = TestNode.with("grand2");
        final TestNode child1 = TestNode.with("child1", grand1, grand2).setAttributes(this.attributes("a1", "v1"));

        final TestNode grand3 = nodeWithAttributes("grand3", "different", "v1");
        final TestNode grand4 = nodeWithAttributes("grand4", "a1", "v1");
        final TestNode grand5 = TestNode.with("grand5");
        final TestNode child2 = TestNode.with("child2", grand3, grand4, grand5);

        this.applyAndCheck(TestNode.relativeNodeSelector().descendantOrSelf().attributeValueEquals(Names.string("a1"), "v1").children(),
            TestNode.with("parent", child1, child2),
            grand1, grand2);
    }

    @Test
    public void testChildrenPredicateFilter() {
        final TestNode child1 = nodeWithAttributes("child1", "a1", "v1");
        final TestNode child2 = nodeWithAttributes("child2", "a1", "v1");

        this.applyFilterAndCheck(TestNode.relativeNodeSelector()
                .children()
                .attributeValueEquals(Names.string("a1"), "v1"),
            TestNode.with("parent", child1, child2),
            (n) -> !n.name().value().equals("child2"), // ignores child2
            child1);
    }

    @Test
    public void testPredicateMap() {
        final TestNode parent = TestNode.with("parent", TestNode.with(MAGIC_VALUE), TestNode.with("child"));

        TestNode.clear();

        this.acceptMapAndCheck(this.createSelector2(),
            parent.child(0),
            TestNode.with("parent", TestNode.with(MAGIC_VALUE + "*0"), TestNode.with("child"))
                .child(0));
    }

    private NodePredicateNodeSelector<TestNode, StringName, StringName, Object> createSelector2() {
        return NodePredicateNodeSelector.with(PREDICATE);
    }

    // NodeSelectorVisitor............................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<NodeSelector<TestNode, StringName, StringName, Object>> visited = Lists.array();

        final Predicate<TestNode> predicate = this.predicate();
        final NodePredicateNodeSelector<TestNode, StringName, StringName, Object> selector = NodePredicateNodeSelector.with(predicate);
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
            protected Visiting startVisitPredicate(final NodeSelector<TestNode, StringName, StringName, Object> s,
                                                   final Predicate<TestNode> p) {
                assertSame(selector, s, "selector");
                assertSame(predicate, p, "predicate");
                b.append("3");
                visited.add(s);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitPredicate(final NodeSelector<TestNode, StringName, StringName, Object> s,
                                             final Predicate<TestNode> p) {
                assertSame(selector, s, "selector");
                assertSame(predicate, p, "predicate");
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
    public void testEqualsDifferentPredicate() {
        this.checkNotEquals(this.createSelector(Predicates.fake(), this.wrapped()));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "*[" + this.predicate() + "]");
    }

    @Override
    NodePredicateNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return NodePredicateNodeSelector.with(this.predicate());
    }

    private Predicate<TestNode> predicate() {
        return NodeSelectorNodeAttributeValuePredicate.startsWith(this.attributeName(), this.attributeValue());
    }

    private StringName attributeName() {
        return Names.string("attribute123");
    }

    private String attributeValue() {
        return "value456";
    }

    @Override
    public Class<NodePredicateNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(NodePredicateNodeSelector.class);
    }

    private NodePredicateNodeSelector<TestNode, StringName, StringName, Object> createSelector(final Predicate<TestNode> node,
                                                                                               final NodeSelector<TestNode, StringName, StringName, Object> selector) {
        return Cast.to(NodePredicateNodeSelector.with(node).append(selector));
    }
}
