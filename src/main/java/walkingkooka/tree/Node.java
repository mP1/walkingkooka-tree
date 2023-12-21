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
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.HasName;
import walkingkooka.naming.Name;
import walkingkooka.tree.pointer.NodePointer;
import walkingkooka.visit.Visitable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A node is part of a tree holding branches and leaves all of which are nodes.
 */
public interface Node<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE>
        extends HasName<NAME>,
        Traversable<N>,
        Visitable {

    /**
     * Returns the name of this node.
     */
    NAME name();

    /**
     * While all {@link Node node's} have {@link Name names} they might not all be unique amongst siblings.
     */
    default boolean hasUniqueNameAmongstSiblings() {
        return false;
    }

    /**
     * If not the root returns the index of this traversable, or -1 for root.
     */
    @Override
    default int index() {
        try {
            return Traversable.super.index();
        } catch (final TraversableException cause) {
            throw new NodeException(cause.getMessage(), cause);
        }
    }

    /**
     * Helper that gets and complain if a parent is absent.
     */
    default N parentOrFail() {
        try {
            return Traversable.super.parentOrFail();
        } catch (final TraversableException cause) {
            throw new NodeException(cause.getMessage(), cause);
        }
    }

    /**
     * Removes the parent from this {@link Node}.
     */
    N removeParent();

    /**
     * If a child belonging to a parent, returns the parent without the child otherwise returns {@link Optional#empty()}
     */
    default Optional<N> parentWithout() {
        return this.parent()
                .map(p -> p.removeChild(this.index()));
    }

    /**
     * Replaces this {@link Node} returning the result.
     */
    default N replace(final N node) {
        Objects.requireNonNull(node, "node");

        // early abort if $node is the same as this, this prevents failures in readonly Node.setChildren that throw UOE.
        return this.equals(node) ?
                node :
                this.parent()
                        .map(p -> p.replaceChild(Cast.to(this), node).children().get(this.index()))
                        .orElse(node.parent().map(Node::removeParent).orElse(node));
    }

    /**
     * Sets new children.
     */
    N setChildren(final List<N> children);

    /**
     * Sets or replaces the child at the given index.<br>
     * Sub classes such as JsonArrayNode may wish to support auto expanding the children list with null slots.
     */
    default N setChild(final int index, final N child) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Invalid index " + index + "=" + this);
        }
        Objects.requireNonNull(child, "child");

        return this.replaceChild(this.children().get(index), child);
    }

    /**
     * Replaces if an existing child has the {@link Name} or appends a new child.
     */
    default N setChild(final NAME name, final N child) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(child, "child");

        return this.children()
                .stream()
                .filter(c -> c.name().equals(name))
                .findFirst()
                .map(c -> this.replaceChild(c, child))
                .orElse(this.appendChild(child));
    }

    /**
     * Conditionally replaces any nodes that a matched by the {@link Predicate} and then mapped by the {@link Function mapper}.
     */
    default N replaceIf(final Predicate<N> predicate,
                        final Function<N, N> mapper) {
        Objects.requireNonNull(predicate, "predicate");
        Objects.requireNonNull(mapper, "mapper");

        N result = (N) this;
        if (predicate.test(result)) {
            result = mapper.apply(result);
        } else {
            result = result.setChildren(
                    this.children()
                            .stream()
                            .map(n -> n.replaceIf(predicate, mapper))
                            .collect(Collectors.toList())
            );
        }

        return result;
    }

    /**
     * Appends a new child.
     */
    default N appendChild(final N child) {
        Objects.requireNonNull(child, "child");

        final List<N> newChildren = Lists.array();
        newChildren.addAll(this.children());
        newChildren.add(child);
        return this.setChildren(newChildren);
    }

    /**
     * Replaces an existing child with a new one.
     */
    default N replaceChild(final N oldChild, final N newChild) {
        Objects.requireNonNull(oldChild, "oldChild");
        Objects.requireNonNull(newChild, "newChild");

        final Optional<N> parent = oldChild.parent();
        if (!parent.isPresent()) {
            throw new IllegalArgumentException("Old child has no parent=" + oldChild);
        }
        if (parent.get() != this) {
            throw new IllegalArgumentException("Old child has different parent=" + oldChild);
        }

        // replace
        final List<N> newChildren = Lists.array();
        newChildren.addAll(this.children());
        newChildren.set(oldChild.index(), newChild);

        return this.setChildren(newChildren);
    }

    /**
     * Removes an existing child using its index.
     */
    default N removeChild(final int index) {
        N parentWithout;

        if (this instanceof HasChildrenValues) {
            final HasChildrenValues<Object, N> parentOfChildrenValues = Cast.to(this);

            final List<Object> without = Lists.array();
            without.addAll(parentOfChildrenValues.childrenValues());
            without.remove(index);
            parentWithout = parentOfChildrenValues.setChildrenValues(without);
        } else {
            final List<N> without = Lists.array();
            without.addAll(this.children());
            without.remove(index);
            parentWithout = this.setChildren(without);
        }

        return parentWithout;
    }

    /**
     * Returns the attributes of this node.
     */
    Map<ANAME, AVALUE> attributes();

    /**
     * Sets or replaces the attributes.
     */
    N setAttributes(final Map<ANAME, AVALUE> attributes);

    /**
     * Returns a {@link NodePointer} that uniquely identifies this {@link Node} starting at the root.
     */
    default NodePointer<N, NAME> pointer() {
        return Nodes.pointer(Cast.to(this));
    }
}
