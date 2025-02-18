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

package walkingkooka.tree;

import org.junit.jupiter.api.Test;
import walkingkooka.naming.Name;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A mixin interface that contains tests and helpers to assist testing of {@link Node} with children implementations..
 */
public interface ParentNodeTesting<N extends Node<N, NAME, ANAME, AVALUE>,
    NAME extends Name,
    ANAME extends Name,
    AVALUE>
    extends
    NodeTesting<N, NAME, ANAME, AVALUE> {

    @Test
    default void testParentWithoutRoot() {
        this.parentMissingCheck(this.createNode().root());
    }

    @Test
    default void testChildrenCached() {
        final N node = this.createNode();
        this.checkCached(node, "children", node.children(), node.children());
    }

    @Test
    default void testAppendChild() {
        final N parent = this.createNode();
        final int parentChildCount = parent.children().size();

        final N child1 = this.createNode();
        final N parent1 = this.appendChildAndCheck(parent, child1);

        this.childCountCheck(parent1, parentChildCount + 1);
    }

    @Test
    default void testAppendChild2() {
        final N parent = this.createNode();
        final int parentChildCount = parent.children().size();

        final N child1 = this.createNode();
        final N parent1 = this.appendChildAndCheck(parent, child1);

        final N child2 = this.createNode();
        final N parent2 = this.appendChildAndCheck(parent1, child2);

        this.childCountCheck(parent2, parentChildCount + 2);
    }

    @Test
    default void testRemoveChildFirst() {
        this.appendTwoChildrenAndRemove(0);
    }

    @Test
    default void testRemoveChildLast() {
        this.appendTwoChildrenAndRemove(1);
    }

    default void appendTwoChildrenAndRemove(final int remove) {
        final N parent = this.createNode();
        final int parentCount = parent.children().size();

        final N child1 = this.createNode();
        final N parent1 = this.appendChildAndCheck(parent, child1);

        final N child2 = this.createNode();
        final N parent2 = this.appendChildAndCheck(parent1, child2);

        final N removed = parent2.children().get(remove);
        final N parent3 = this.removeChildAndCheck(parent2, removed);

        this.childCountCheck(parent3, parentCount + 1 + 1 - 1);
    }

    @Test
    default void testReplaceChildWithoutParent() {
        assertThrows(IllegalArgumentException.class, () -> {
            final N parent = this.createNode();
            final N child = this.createNode();

            parent.replaceChild(child, this.createNode());
        });
    }

    @Test
    default void testReplaceChildDifferentParent() {
        assertThrows(IllegalArgumentException.class, () -> {
            final N parent1 = this.createNode();

            final N parent2 = this.createNode();
            final N childOf2 = parent2.children().get(0);

            parent1.replaceChild(childOf2, this.createNode());
        });
    }

    @Test
    default void testReplaceChild() {
        final N parent = this.createNode();

        final N child1 = this.createNode();
        final N child2 = this.createNode();

        @SuppressWarnings("unchecked") final N set = this.setChildrenAndCheck(parent, child1, child2);

        //noinspection unchecked
        this.childCountCheck(set, child1, child2);
    }

    @Test
    default void setChildrenWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            final N parent = this.createNode();
            parent.setChildren(null);
        });
    }

    @Test
    default void testSetChildrenSame() {
        final N parentBefore = this.appendChildAndCheck(this.createNode(), this.createNode());

        final N parentAfter = parentBefore.setChildren(parentBefore.children());
        assertSame(parentAfter, parentBefore);
    }

    @Test
    default void testSetDifferentChildren() {
        final N parent = this.createNode();

        final N child1 = this.createNode();
        final N child2 = this.createNode();

        @SuppressWarnings("unchecked") final N parent1 = this.setChildrenAndCheck(parent, child1, child2);

        final N child3 = this.createNode();
        @SuppressWarnings("unchecked") final N parent2 = this.setChildrenAndCheck(parent1, child3);

        //noinspection unchecked
        this.childCountCheck(parent2, child3);
    }

    @Test
    default void testAttributesCached() {
        final N node = this.createNode();
        this.checkCached(node, "attributes", node.attributes(), node.attributes());
    }
}
