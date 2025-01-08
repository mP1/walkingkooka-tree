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
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;

public abstract class NodePointerTestCase2<N extends NodePointer<TestNode, StringName>> extends NodePointerTestCase<N> {

    NodePointerTestCase2() {
        super();
    }

    @Test
    public void testNextAppend() {
        this.nextAndCheck(this.createNodePointer().append(),
            NodePointerAppend.create());
    }

    // equals ..........................................................................................................

    @Test
    public final void testEqualsDifferentNext() {
        this.checkNotEquals(this.createObject().appendToLast(NodePointer.indexed(0, TestNode.class)),
            this.createObject().appendToLast(NodePointer.indexed(99, TestNode.class)));
    }

    @Test
    public final void testEqualsDifferentNext2() {
        this.checkNotEquals(this.createObject(),
            this.createObject().appendToLast(NodePointer.indexed(99, TestNode.class)));
    }

    @Test
    public final void testEqualsNext2() {
        final NodePointer<TestNode, StringName> next = NodePointer.indexed(99, TestNode.class);

        this.checkEqualsAndHashCode(this.createObject().appendToLast(next),
            this.createObject().appendToLast(next));
    }
}
