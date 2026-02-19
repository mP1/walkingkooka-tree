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

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.text.CharSequences;
import walkingkooka.text.CharacterConstant;
import walkingkooka.tree.Node;
import walkingkooka.visit.Visitable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A node pointer may be thought of as a JsonPointer that operates on any type of {@link Node}.
 * THe only difference is an element index will always match any first child, for something like a json object this will be the first key/value.
 * Because the return value is a {@link Node} any relative pointer terminating with a trailing will ignore the hash itself and return the result as if it was absent.
 * <br>
 * <a href="http://json-schema.org/latest/relative-json-pointer.html#RFC4627">spec</a>
 */
public abstract class NodePointer<N extends Node<N, NAME, ?, ?>, NAME extends Name> implements Visitable {

    final static String APPEND = "-";

    /**
     * Accepts and parses a {@link String} holding a pointer.
     */
    public static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointer<N, NAME> parse(final String pointer,
                                                                                                final Function<String, NAME> nameFactory,
                                                                                                final Class<N> nodeType) {
        Objects.requireNonNull(pointer, "pointer");
        Objects.requireNonNull(nameFactory, "name factory");
        Objects.requireNonNull(nodeType, "nodeType");

        final String[] components = pointer.split(SEPARATOR.string());
        NodePointer<N, NAME> result = any(nodeType);
        boolean relative = true;
        boolean hash = false;

        for (String component : components) {
            if (relative) {
                if (component.isEmpty()) {
                    relative = false; // found a slash...
                    continue;
                }
                if (component.endsWith("#")) {
                    component = component.substring(0, component.length() - 1);
                    hash = true;
                }
            }
            if (component.isEmpty()) {
                throw new IllegalArgumentException("Empty component found within pointer=" + CharSequences.quote(pointer));
            }

            try {
                final int number = Integer.parseInt(component);
                result = result.appendToLast(relative ?
                    NodePointerRelative.with(number, hash) :
                    indexed(number, nodeType));
            } catch (final NumberFormatException mustBeName) {
                if (relative) {
                    throw new IllegalArgumentException("Relative pointer expected number but got=" + CharSequences.quote(pointer));
                }
                if (APPEND.equals(component)) {
                    result = result.append();
                } else {
                    final NAME name = nameFactory.apply(component.replace("~1", "/")
                        .replace("~0", "~"));
                    result = result.named(name);
                }
            }
            relative = false;
        }

        return result;
    }

    /**
     * Type safe null for use to mark no next. Intended for use only within this package.
     */
    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointer<N, NAME> absent() {
        return null;
    }

    /**
     * Creates a match all.
     */
    public static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointer<N, NAME> any(final Class<N> nodeType) {
        Objects.requireNonNull(nodeType, "nodeType");

        return NodePointerAny.get();
    }

    /**
     * Creates an array element pointer.
     */
    public static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointer<N, NAME> indexed(final int index,
                                                                                                  final Class<N> nodeType) {
        Objects.requireNonNull(nodeType, "nodeType");

        return NodePointerIndexedChild.with(index);
    }

    /**
     * Creates an object property pointer
     */
    public static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointer<N, NAME> named(final NAME name,
                                                                                                final Class<N> nodeType) {
        Objects.requireNonNull(nodeType, "nodeType");

        return NodePointerNamedChild.with(name);
    }

    /**
     * Creates an relative.
     */
    public static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointer<N, NAME> relative(final int ancestor,
                                                                                                   final Class<N> nodeType) {
        Objects.requireNonNull(nodeType, "nodeType");

        return NodePointerRelative.with(ancestor, false);
    }

    /**
     * Creates an relative.
     */
    public static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointer<N, NAME> relativeHash(final int ancestor,
                                                                                                       final Class<N> nodeType) {
        Objects.requireNonNull(nodeType, "nodeType");

        return NodePointerRelative.with(ancestor, true);
    }

    /**
     * Package private ctor to limit sub classing.
     */
    NodePointer(final NodePointer<N, NAME> next) {
        this.next = next;
    }

    /**
     * Appends a none pointer, equivalent to the "-" token within a string pointer.
     */
    public final NodePointer<N, NAME> append() {
        return this.appendToLast(NodePointerAppend.create());
    }

    /**
     * Appends a child name to this pointer.
     */
    public final NodePointer<N, NAME> named(final NAME name) {
        return this.appendToLast(NodePointerNamedChild.with(name));
    }

    /**
     * Appends an index to this pointer.
     */
    public final NodePointer<N, NAME> indexed(final int index) {
        return this.appendToLast(NodePointerIndexedChild.with(index));
    }

    /**
     * Appends one pointer to end.
     */
    abstract NodePointer<N, NAME> appendToLast(final NodePointer<N, NAME> pointer);

    final NodePointer<N, NAME> appendToLast0(final NodePointer<N, NAME> pointer) {
        return null == this.next ?
            pointer :
            this.next.appendToLast(pointer);
    }

    /**
     * Walks this pointer and returns the matching {@link Node}
     */
    public final Optional<N> traverse(final N node) {
        Objects.requireNonNull((Node<?, ?, ?, ?>) node, "node");

        return Optional.ofNullable(
            this.traverseOrNull(node)
        );
    }

    /**
     * Executes this pointer, throwing a {@link NodePointerException} if the traverse is unsuccessful.
     */
    final N traverseOrFail(final N node) {
        final N found = this.traverseOrNull(node);
        if (null == found) {
            throw new NodePointerException("Unable to find " + this + " starting at " + node);
        }
        return found;
    }

    /**
     * Executes this pointer, returning the node found or null if the traverse is unsuccessful.
     */
    private N traverseOrNull(final N node) {
        N result;

        N current = node;
        NodePointer<N, NAME> pointer = this;

        for (; ; ) {
            final N next = pointer.nextNodeOrNull(current);
            if (null == next) {
                result = null;
                break;
            }
            pointer = pointer.next;
            if (null == pointer) {
                result = next;
                break;
            }
            current = next;
        }

        return result;
    }

    /**
     * Very similar to {@link #traverse(Node)} but returns the second last node or fails.
     */
    final N traverseAndAddOrFail(final N node, final N value) {
        N result;

        N current = node;
        NodePointer<N, NAME> pointer = this;

        for (; ; ) {
            final NodePointer<N, NAME> nextPointer = pointer.next;
            final N next = pointer.nextNodeOrNull(current);
            if (null == next) {
                result = null == nextPointer ?
                    pointer.add0(current, value)
                    : null;
                break;
            }

            if (null == nextPointer) {
                result = pointer.add0(current, value);
                break;
            }
            pointer = nextPointer;
            current = next;
        }

        if (null == result) {
            throw new NodePointerException("Unable to find " + pointer + " starting at " + node);
        }
        return result;
    }

    /**
     * Returns the next {@link NodePointer} in the chain.
     */
    public Optional<NodePointer<N, NAME>> next() {
        return Optional.ofNullable(this.next);
    }

    abstract N nextNodeOrNull(final N node);

    /**
     * The next part in the pointer or null.
     */
    final NodePointer<N, NAME> next;

    /**
     * Tests if this is a absolute pointer, basically the opposite of {@link #isRelative()}
     */
    public final boolean isAbsolute() {
        return !this.isRelative();
    }

    /**
     * Tests if this is a relative pointer.
     */
    abstract public boolean isRelative();

    // NodePatch helpers.................................................................................................

    /**
     * Uses this {@link NodePointer} to find the node that path and add the given node, returning the result.
     */
    final public N add(final N node, final N value) {
        Objects.requireNonNull((Node<?, ?, ?, ?>) node, "node");
        Objects.requireNonNull(value, "value");

        return this.traverseAndAddOrFail(node, value);
    }

    /**
     * Sub classes must add the value to the given {@link Node}.
     */
    abstract N add0(final N node, final N value);

    /**
     * Uses this {@link NodePointer} to find the node that path and remove the given node, returning the result.
     */
    final public N remove(final N node) {
        Objects.requireNonNull((Node<?, ?, ?, ?>) node, "node");

        return this.remove0(node);
    }

    abstract N remove0(final N node);

    /**
     * General purpose report that a remove fail.
     */
    final N removeOrFail(final N node) {
        return this.traverseOrFail(node)
            .parentWithout()
            .orElseThrow(() -> new NodePointerException("Unable to remove " + this + " from " + node));
    }

    // NodePointerVisitor................................................................................

    abstract void accept(final NodePointerVisitor<N, NAME> visitor);

    /**
     * Visits the next component if one exists otherwise returns immediately.
     */
    final void acceptNext(final NodePointerVisitor<N, NAME> visitor) {
        final NodePointer<N, NAME> next = this.next;
        if (null != next) {
            visitor.accept0(next);
        }
    }

    // Object...........................................................................................................

    @Override
    public abstract int hashCode();

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public final boolean equals(final Object other) {
        return this == other || this.canBeEqual(other) && this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final NodePointer<?, ?> other) {
        return this.equals1(other) &&
            Objects.equals(this.next, other.next);
    }

    abstract boolean equals1(final NodePointer<?, ?> other);

    // Object ..............................................................................................

    /**
     * Return the full node pointer string.
     */
    @Override
    public final String toString() {
        final StringBuilder b = new StringBuilder();

        NodePointer<N, NAME> pointer = this;
        for (; ; ) {
            pointer.toString0(b);

            if (null == pointer.next) {
                pointer.lastToString(b);
                break;
            }
            pointer = pointer.next;
        }

        return b.toString();
    }

    final static CharacterConstant SEPARATOR = CharacterConstant.with('/');

    abstract void toString0(final StringBuilder b);

    abstract void lastToString(final StringBuilder b);
}
