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
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;
import walkingkooka.visit.Visiting;

public final class NodePointerNamedChildTest extends NodePointerTestCase2<NodePointerNamedChild<TestNode, StringName>> {

    private final static StringName A1_NAME = Names.string("A1");
    private final static StringName B2_NAME = Names.string("B2");

    private final static TestNode A1_NODE = TestNode.with(A1_NAME.value());
    private final static TestNode B2_NODE = TestNode.with(B2_NAME.value());

    @Test
    public void testWith() {
        final NodePointerNamedChild<TestNode, StringName> pointer = this.createNodePointer();
        this.checkEquals(this.name(), pointer.name, "name");
    }

    // add..............................................................................................................

    @Test
    public void testAddUnknownPathFails() {
        this.addAndFail(NodePointer.named(A1_NAME, TestNode.class).appendToLast(NodePointerNamedChild.with(B2_NAME)),
            TestNode.with("child"),
            A1_NODE);
    }

    @Test
    public void testAddUnknownPathFails2() {
        this.addAndFail(NodePointer.named(A1_NAME, TestNode.class).appendToLast(NodePointerIndexedChild.with(99)),
            TestNode.with("child"),
            A1_NODE);
    }

    @Test
    public void testAddSameProperty() {
        final TestNode root = TestNode.with("root")
            .appendChild(A1_NODE);

        this.addAndCheck(NodePointerNamedChild.with(A1_NAME),
            root,
            A1_NODE,
            root);
    }

    @Test
    public void testAddNewProperty() {
        final TestNode root = TestNode.with("root")
            .appendChild(A1_NODE);

        this.addAndCheck(NodePointerNamedChild.with(B2_NAME),
            root,
            B2_NODE,
            root.appendChild(B2_NODE));
    }

    @Test
    public void testAddReplaces() {
        final TestNode oldB2 = TestNode.with("b2-old-value");

        final TestNode root = TestNode.with("root")
            .appendChild(A1_NODE)
            .appendChild(oldB2);

        final TestNode replacedB2 = TestNode.with("b2-replaced-value");

        this.addAndCheck(NodePointerNamedChild.with(B2_NAME),
            root,
            replacedB2,
            root.appendChild(replacedB2));
    }

    // remove...........................................................................................................

    @Test
    public void testRemoveUnknownPathFails() {
        this.removeAndFail(NodePointerNamedChild.with(A1_NAME),
            TestNode.with("remove"));
    }

    @Test
    public void testRemoveUnknownPathFails2() {
        this.removeAndFail(NodePointerNamedChild.with(A1_NAME),
            TestNode.with("root")
                .appendChild((B2_NODE)));
    }

    @Test
    public void testRemoveChild() {
        this.removeAndCheck2(TestNode.with("root")
                .appendChild(A1_NODE),
            A1_NAME);
    }

    @Test
    public void testRemoveChild2() {
        this.removeAndCheck2(TestNode.with("root")
                .appendChild(A1_NODE)
                .appendChild(B2_NODE),
            B2_NAME);
    }

    private void removeAndCheck2(final TestNode node,
                                 final StringName name) {
        this.removeAndCheck(NodePointerNamedChild.with(name),
            node,
            node.removeChild(name));
    }

    @Test
    public void testEqualsDifferentName() {
        this.checkNotEquals(NodePointerNamedChild.with(Names.string("different")));
    }

    // toString.........................................................................................................

    @Test
    public void testToStringWithSlash() {
        this.toStringAndCheck(
            NodePointerNamedChild.with(Names.string("slash")),
            "/slash"
        );
    }

    @Test
    public void testToStringWithTilde() {
        this.toStringAndCheck(
            NodePointerNamedChild.with(Names.string("tilde~")),
            "/tilde~0"
        );
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
            protected Visiting startVisitNamedChild(final NodePointer<TestNode, StringName> node,
                                                    final StringName name) {
                b.append("3");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitNamedChild(final NodePointer<TestNode, StringName> node,
                                              final StringName name) {
                b.append("4");
            }

        }.accept(this.createNodePointer());

        this.checkEquals("1342", b.toString());
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
            protected Visiting startVisitNamedChild(final NodePointer<TestNode, StringName> node,
                                                    final StringName name) {
                b.append("5");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitNamedChild(final NodePointer<TestNode, StringName> node,
                                              final StringName name) {
                b.append("6");
            }

        }.accept(this.createNodePointer().append());

        this.checkEquals("1513262", b.toString());
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
            protected Visiting startVisitNamedChild(final NodePointer<TestNode, StringName> node,
                                                    final StringName name) {
                b.append("4");
                return Visiting.SKIP;
            }

            @Override
            protected void endVisitNamedChild(final NodePointer<TestNode, StringName> node,
                                              final StringName name) {
                b.append("5");
            }

        }.accept(this.createNodePointer().append());

        this.checkEquals("1452", b.toString());
    }

    @Override
    NodePointerNamedChild<TestNode, StringName> createNodePointer() {
        return NodePointerNamedChild.with(this.name());
    }

    private StringName name() {
        return Names.string("someProperty");
    }

    @Override
    public Class<NodePointerNamedChild<TestNode, StringName>> type() {
        return Cast.to(NodePointerNamedChild.class);
    }
}
