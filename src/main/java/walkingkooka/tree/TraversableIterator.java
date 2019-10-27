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

import walkingkooka.collect.stack.Stack;
import walkingkooka.collect.stack.Stacks;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An {@link Iterator} returned by {@link Traversable#traversableIterator()} that walks all the descendants starting a the beginning {@link Traversable}.
 */
final class TraversableIterator<T extends Traversable<T>> implements Iterator<T> {

    /**
     * Package private factory only called by default method {@link Traversable#traversableIterator()}
     */
    static <T extends Traversable<T>> TraversableIterator<T> with(final T traversable) {
        return new TraversableIterator<>(traversable);
    }

    private TraversableIterator(final T traversable) {
        final Stack<T> unprocessed = Stacks.arrayList();
        this.unprocessed = unprocessed;

        unprocessed.push(traversable);
    }

    @Override
    public boolean hasNext() {
        boolean has = null != this.next;
        if (!has) {
            this.next = this.nextOrNull();
            has = null != this.next;
        }

        return has;
    }

    @Override
    public T next() {
        T give = this.next;
        this.next = null;
        if (null == give) {
            this.next = this.nextOrNull();
            if (null == this.next) {
                throw new NoSuchElementException();
            }
            give = this.next;
            this.next = null;
        }
        return give;
    }

    /**
     * Cache of the next traversable or null.
     */
    private T next;

    private T nextOrNull() {
        T traversable = null;

        final Stack<T> stack = this.unprocessed;
        if (false == stack.isEmpty()) {
            traversable = stack.peek();
            stack.pop();

            this.pushNextSibling(traversable);
            this.pushFirstChild(traversable);
        }

        return traversable;
    }

    /**
     * A {@link Stack} of {@link Traversable} that remain unprocessed and are immediate candidates for match testing.
     */
    private final Stack<T> unprocessed;

    /**
     * Starting at the given {@link Traversable} pushes the next sibling.
     */
    private void pushNextSibling(final T traversable) {
        if (this.skipNextSibling) {
            this.skipNextSibling = false;
        } else {
            traversable.nextSibling()
                    .ifPresent(this.unprocessed::push);
        }
    }

    private boolean skipNextSibling = true;

    /**
     * Pushes the first child of the {@link Traversable} if it is a parent.
     */
    private void pushFirstChild(final T traversable) {
        traversable.firstChild().ifPresent(this.unprocessed::push);
    }

    @Override
    public String toString() {
        return null != this.next ?
                this.next.toString() :
                this.unprocessed.isEmpty() ?
                        "???" :
                        this.unprocessed.peek().toString();
    }
}
