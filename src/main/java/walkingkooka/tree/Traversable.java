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

import walkingkooka.Cast;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * A {@link Traversable} is part of a tree holding branches and leaves all of which are traversables.
 */
public interface Traversable<T extends Traversable<T>> {

    /**
     * Standard index response to indicate a unsuccessful scan of an item in a list.
     */
    int NO_INDEX = -1;

    /**
     * If not the root returns the index of this {@link Traversable}, or {@link #NO_INDEX} for root.
     */
    default int index() {
        int index = NO_INDEX;

        final Optional<T> parent = this.parent();
        if (parent.isPresent()) {
            index = parent.get().children().indexOf(this);
            if (NO_INDEX == index) {
                throw new NodeException("Child not present in children of parent=" + this);
            }
        }

        return index;
    }

    /**
     * Returns the parent {@link Traversable}.
     */
    Optional<T> parent();

    /**
     * Helper that gets and complain if a parent is absent.
     */
    default T parentOrFail() {
        return this.parent()
                .orElseThrow(() -> new NodeException("Parent missing from " + this));
    }

    /**
     * Returns true if this traversable is the root.
     */
    default boolean isRoot() {
        return !this.parent().isPresent();
    }

    /**
     * Returns the root {@link Traversable} which may or may not be this {@link Traversable}.
     * The default walks up the ancestor path until reaching the root.
     */
    default T root() {
        T root = Cast.to(this);

        for (; ; ) {
            final Optional<T> parent = root.parent();
            if (!parent.isPresent()) {
                break;
            }
            root = parent.get();
        }

        return root;
    }

    /**
     * Returns the children of this {@link Traversable}.
     */
    List<T> children();

    /**
     * Returns the previous sibling if one exists.
     */
    default Optional<T> previousSibling() {
        Optional<T> result = Optional.empty();

        final Optional<T> parent = this.parent();
        if (parent.isPresent()) {
            final List<T> children = parent.get().children();
            final int index = this.index();
            if (index > 0) {
                result = Optional.of(children.get(index - 1));
            }
        }

        return result;
    }

    /**
     * Returns the next sibling if one exists.
     */
    default Optional<T> nextSibling() {
        Optional<T> result = Optional.empty();

        final Optional<T> parent = this.parent();
        if (parent.isPresent()) {
            final List<T> children = parent.get().children();
            final int index = this.index();
            if (index + 1 < children.size()) {
                result = Optional.of(children.get(index + 1));
            }
        }

        return result;
    }

    /**
     * Returns the first child for the current {@link Traversable}.
     */
    default Optional<T> firstChild() {
        final List<T> children = this.children();
        return children.isEmpty() ?
                Optional.empty() :
                Optional.of(children.get(0));
    }

    /**
     * Returns the last child for the current {@link Traversable}.
     */
    default Optional<T> lastChild() {
        final List<T> children = this.children();
        return children.isEmpty() ?
                Optional.empty() :
                Optional.of(children.get(children.size() - 1));
    }


    /**
     * An {@link Iterator} that walks starting at this {@link Traversable} depth first.
     */
    default Iterator<T> traversableIterator() {
        return TraversableIterator.with(Cast.to(this));
    }
}
