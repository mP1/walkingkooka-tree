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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * A mixin interface that contains tests and helpers to assist testing of {@link Traversable} implementations..
 */
public interface TraversableTesting {

    default void parentMissingCheck(final Traversable<?> traversable) {
        assertEquals(Optional.empty(), traversable.parent(), "parent");
        assertEquals(true, traversable.isRoot(), "root");
    }

    default void parentPresentCheck(final Traversable<?> traversable) {
        assertNotEquals(Optional.empty(), traversable.parent(), "parent");
        assertEquals(false, traversable.isRoot(), "root");
    }

    default void childrenCheck(final Traversable<?> parent) {
        final Optional<Traversable<?>> nodeAsParent = Optional.of(parent);

        int i = 0;
        for (Traversable<?> child : parent.children()) {
            assertEquals(i, child.index(), () -> "Incorrect index of " + child);
            final int j = i;
            assertEquals(nodeAsParent, child.parent(), () -> "Incorrect parent of child " + j + "=" + child);

            this.childrenCheck(child);
            i++;
        }
    }

    default void childrenParentCheck(final Traversable<?> parent) {
        int i = 0;
        for (Traversable<?> child : parent.children()) {
            assertSame(parent, child.parentOrFail(), () -> "parent of child[" + i + "]=" + child);
        }
    }

    @SuppressWarnings("unchecked")
    default <TT extends Traversable<TT>> void childCountCheck(final TT parent,
                                                              final TT... children) {
        this.childCountCheck(parent, children.length);
    }

    default void childCountCheck(final Traversable<?> parent, final int count) {
        assertEquals(parent.children().size(), count, "children of parent");
        this.childrenParentCheck(parent);
    }
}
