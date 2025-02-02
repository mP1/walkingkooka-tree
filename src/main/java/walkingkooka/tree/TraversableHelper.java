/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
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

import walkingkooka.reflect.PublicStaticHelper;

import java.util.List;
import java.util.Optional;

/**
 * This helper exists to avoid GWTC problems including failure because of private methods in interfaces
 */
final class TraversableHelper implements PublicStaticHelper {

    /**
     * Helper used by the ###Sibling methods, to verify this {@link Traversable} has a parent and then contains this
     * child and retrieves the previous/next sibling.
     */
    static <T extends Traversable<T>> Optional<T> sibling(final Traversable<T> that,
                                                          final int delta) {
        T nextSibling = null;

        final Optional<T> maybeParent = that.parent();
        if (maybeParent.isPresent()) {
            final T parent = maybeParent.get();
            nextSibling = child(
                parent,
                that.index() + delta
            );
        }

        return Optional.ofNullable(
            nextSibling
        );
    }

    /**
     * Helper used by various sibling and child methods. If the index is out of range a null is returned.
     */
    static <T extends Traversable<T>> T child(final Traversable<T> that,
                                              final int index) {
        final List<T> children = that.children();
        return index >= 0 && index < children.size() ?
            children.get(index) :
            null;
    }

    /**
     * Stop creation
     */
    private TraversableHelper() {
        throw new UnsupportedOperationException();
    }
}
