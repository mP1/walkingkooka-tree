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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NodePointerAppendTest extends NodePointerTestCase<NodePointerAppend<TestNode, StringName>> {

    @Test
    public void testAppendIndexedFails() {
        assertThrows(UnsupportedOperationException.class, () -> this.createNodePointer().indexed(1));
    }

    @Test
    public void testAppendNamedFails() {
        assertThrows(UnsupportedOperationException.class, () -> this.createNodePointer().named(Names.string("fail!")));
    }

    @Test
    public void testAppendAppendFails() {
        assertThrows(UnsupportedOperationException.class, () -> this.createNodePointer().append());
    }

    @Test
    public void testAdd() {
        final TestNode array = TestNode.with("empty");
        final TestNode add = TestNode.with("add");

        this.addAndCheck(this.createNodePointer(),
            array,
            add,
            array.appendChild(add));
    }

    @Test
    public void testAdd2() {
        final TestNode array = TestNode.with("root")
            .appendChild(TestNode.with("a1"));
        final TestNode add = TestNode.with("add");

        this.addAndCheck(this.createNodePointer(),
            array,
            add,
            array.appendChild(add));
    }

    @Test
    public void testRemoveFails() {
        assertThrows(UnsupportedOperationException.class, () -> this.createNodePointer().remove(TestNode.with("remove")));
    }

    @Test
    public void testToStringElementAppend() {
        final NodePointer<TestNode, StringName> element = NodePointer.named(Names.string("abc"), TestNode.class);
        this.toStringAndCheck(element.append(), "/abc/-");
    }

    @Test
    public void testToStringArrayAppend() {
        final NodePointer<TestNode, StringName> array = NodePointer.indexed(123, TestNode.class);
        this.toStringAndCheck(array.append(), "/123/-");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(NodePointerAppend.create(), "/-");
    }

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
            protected void visitAppend(final NodePointer<TestNode, StringName> node) {
                b.append("3");
            }

        }.accept(this.createNodePointer());

        this.checkEquals("132", b.toString());
    }

    @Override
    NodePointerAppend<TestNode, StringName> createNodePointer() {
        return NodePointerAppend.create();
    }

    @Override
    public Class<NodePointerAppend<TestNode, StringName>> type() {
        return Cast.to(NodePointerAppend.class);
    }
}
