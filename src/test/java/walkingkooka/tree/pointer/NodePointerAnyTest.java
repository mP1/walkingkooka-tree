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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NodePointerAnyTest extends NodePointerTestCase2<NodePointerAny<TestNode, StringName>> {

    @Test
    @Override
    public void testNextAppend() {
        this.nextAndCheck(this.createNodePointer().append(), null);
    }

    @Test
    public void testAddFails() {
        assertThrows(UnsupportedOperationException.class, () -> this.createNodePointer().add(TestNode.with("name1"), TestNode.with("value2")));
    }

    @Test
    public void testRemoveFails() {
        assertThrows(UnsupportedOperationException.class, () -> this.createNodePointer().remove(TestNode.with("remove fails")));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(NodePointerAny.get(), "");
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
            protected void visitAny(final NodePointer<TestNode, StringName> node) {
                b.append("3");
            }
        }.accept(this.createNodePointer());

        this.checkEquals("132", b.toString());
    }

    @Override
    NodePointerAny<TestNode, StringName> createNodePointer() {
        return NodePointerAny.get();
    }

    @Override
    public Class<NodePointerAny<TestNode, StringName>> type() {
        return Cast.to(NodePointerAny.class);
    }
}
