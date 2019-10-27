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

package walkingkooka.tree.pointer;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;
import walkingkooka.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class NodePointerIndexedChildTest extends NodePointerTestCase2<NodePointerIndexedChild<TestNode, StringName>> {

    @Test
    public void testWith() {
        final NodePointerIndexedChild<TestNode, StringName> pointer = this.createNodePointer();
        assertEquals(1, pointer.index, "index");
    }

    // add..............................................................................................................

    @Test
    public void testAddUnknownPathFails() {
        this.addAndFail(NodePointer.indexed(1, TestNode.class).appendToLast(NodePointerIndexedChild.with(99)),
                TestNode.with("name1"),
                TestNode.with("value2"));
    }

    @Test
    public void testAddUnknownPathFails2() {
        this.addAndFail(NodePointer.indexed(1, TestNode.class).appendToLast(NodePointerIndexedChild.with(99)),
                TestNode.with("name1"),
                TestNode.with("value2"));
    }

    @Test
    public void testAddReplaceFirstChild() {
        this.addAndCheck2(0);
    }

    @Test
    public void testAddReplaceChild() {
        this.addAndCheck2(1);
    }

    @Test
    public void testAddReplaceLastChild() {
        this.addAndCheck2(2);
    }

    private void addAndCheck2(final int index) {
        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("value-0a"))
                .appendChild(TestNode.with("value-1b"))
                .appendChild(TestNode.with("value-2c"));

        final TestNode add = TestNode.with("add");

        this.addAndCheck(NodePointerIndexedChild.with(index),
                root,
                add,
                root.setChild(index, add));
    }

    // remove...........................................................................................................

    @Test
    public void testRemoveUnknownPathFails() {
        this.removeAndFail(NodePointerIndexedChild.with(0),
                TestNode.with("remove"));
    }

    @Test
    public void testRemoveUnknownPathFails2() {
        this.removeAndFail(NodePointerIndexedChild.with(1),
                TestNode.with("root").appendChild(TestNode.with("a")));
    }

    @Test
    public void testRemoveChildIndex0() {
        this.removeAndCheck2(TestNode.with("root")
                        .appendChild(TestNode.with("a1"))
                        .appendChild(TestNode.with("b2")),
                0);

    }

    @Test
    public void testRemoveChildIndex1() {
        this.removeAndCheck2(TestNode.with("root")
                        .appendChild(TestNode.with("a1"))
                        .appendChild(TestNode.with("b2")),
                1);

    }

    @Test
    public void testRemoveChildIndex2() {
        this.removeAndCheck2(TestNode.with("root")
                        .appendChild(TestNode.with("a1"))
                        .appendChild(TestNode.with("b2"))
                        .appendChild(TestNode.with("c3")),
                2);

    }

    private void removeAndCheck2(final TestNode node,
                                 final int index) {
        this.removeAndCheck(NodePointerIndexedChild.with(index),
                node,
                node.removeChild(index));
    }

    @Test
    public final void testEqualsDifferentIndex() {
        this.checkNotEquals(NodePointerIndexedChild.with(99));
    }

    // visitor..........................................................................................................

    @Test
    public void testVisitor() {
        final StringBuilder b = new StringBuilder();

        new FakeNodePointerVisitor<TestNode, StringName>() {
            @Override
            protected Visiting startVisit(final NodePointer<TestNode, StringName> node) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodePointer<TestNode, StringName> node) {
                b.append("2");
            }

            @Override
            protected Visiting startVisitIndexedChild(final NodePointer<TestNode, StringName> node,
                                                      final int index) {
                b.append("3");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitIndexedChild(final NodePointer<TestNode, StringName> node,
                                                final int index) {
                b.append("4");
            }

        }.accept(this.createNodePointer());

        assertEquals("1342", b.toString());
    }

    @Test
    public void testVisitorWithNext() {
        final StringBuilder b = new StringBuilder();

        new FakeNodePointerVisitor<TestNode, StringName>() {
            @Override
            protected Visiting startVisit(final NodePointer<TestNode, StringName> node) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodePointer<TestNode, StringName> node) {
                b.append("2");
            }

            @Override
            protected void visitAppend(final NodePointer<TestNode, StringName> node) {
                b.append("3");
            }

            @Override
            protected Visiting startVisitIndexedChild(final NodePointer<TestNode, StringName> node,
                                                      final int index) {
                b.append("4");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitIndexedChild(final NodePointer<TestNode, StringName> node,
                                                final int index) {
                b.append("5");
            }

        }.accept(this.createNodePointer().append());

        assertEquals("1413252", b.toString());
    }

    @Test
    public void testVisitorWithNextSkipped() {
        final StringBuilder b = new StringBuilder();

        new FakeNodePointerVisitor<TestNode, StringName>() {
            @Override
            protected Visiting startVisit(final NodePointer<TestNode, StringName> node) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodePointer<TestNode, StringName> node) {
                b.append("2");
            }

            @Override
            protected Visiting startVisitIndexedChild(final NodePointer<TestNode, StringName> node,
                                                      final int index) {
                b.append("4");
                return Visiting.SKIP;
            }

            @Override
            protected void endVisitIndexedChild(final NodePointer<TestNode, StringName> node,
                                                final int index) {
                b.append("5");
            }

        }.accept(this.createNodePointer().append());

        assertEquals("1452", b.toString());
    }

    @Override
    NodePointerIndexedChild<TestNode, StringName> createNodePointer() {
        return NodePointerIndexedChild.with(1);
    }

    @Override
    public Class<NodePointerIndexedChild<TestNode, StringName>> type() {
        return Cast.to(NodePointerIndexedChild.class);
    }
}
