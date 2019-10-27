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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.BeanPropertiesTesting;
import walkingkooka.reflect.TypeNameTesting;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * A mixin interface that contains tests and helpers to assist testing of {@link Traversable} implementations..
 */
public interface TraversableTesting2<T extends Traversable<T> >
        extends BeanPropertiesTesting,
        HashCodeEqualsDefinedTesting2<T>,
        ToStringTesting<T>,
        TraversableTesting,
        TypeNameTesting<T> {

    @Test
    default void testParentCached() {
        final T node = this.createTraversable();
        this.checkCached(node, "parent", node.parent(), node.parent());
    }

    @Test
    default void testParentWithoutChild() {
        final T parent = this.createTraversable();
        final List<T> children = parent.children();
        assertNotEquals(Lists.empty(), children, "expected at least 1 child");

        this.parentMissingCheck(this.createTraversable());
    }

    @Test
    default void testRootWithoutParent() {
        final T node = this.createTraversable();
        assertEquals(Optional.empty(), node.parent(), "node must have no parent");
        assertSame(node, node.root());
    }

    @Test
    default void testChildrenIndices() {
        this.childrenCheck(this.createTraversable());
    }

    @Test
    default void testFirstChild() {
        final T node = this.createTraversable();
        final List<T> children = node.children();
        final Optional<T> first = node.firstChild();
        if (children.isEmpty()) {
            assertEquals(Optional.empty(), first, "childless node must not have a first child.");
        } else {
            assertEquals(Optional.of(children.get(0)), first, "node with children must have a first child.");
        }
    }

    @Test
    default void testLastChild() {
        final T node = this.createTraversable();
        final List<T> children = node.children();
        final Optional<T> last = node.lastChild();
        if (children.isEmpty()) {
            assertEquals(Optional.empty(), last, "childless node must not have a last child.");
        } else {
            assertEquals(Optional.of(children.get(children.size() - 1)), last, "node with children must have a last child.");
        }
    }

    @Test
    default void testPropertiesNeverReturnNull() throws Exception {
        this.allPropertiesNeverReturnNullCheck(this.createTraversable(),
                (m) -> m.getName().equals("parentOrFail"));
    }

    T createTraversable();

    default <TT> void checkCached(final T traversable,
                                  final String property,
                                  final TT value,
                                  final TT value2) {
        assertSame(value, value2, () -> traversable + " did not cache " + property);
    }

    // HashcodeAndEqualityTesting.......................................................................................

    @Override
    default T createObject() {
        return this.createTraversable();
    }
}
