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
import walkingkooka.tree.Node;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.Objects;

/**
 * Represents a component that matches a node by its index relative to its parent.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
final class NodePointerIndexedChild<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePointer<N, NAME> {

    /**
     * Creates a {@link NodePointerIndexedChild}
     */
    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointerIndexedChild<N, NAME> with(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Invalid index " + index + " values should be greater or equal to 0");
        }

        return new NodePointerIndexedChild<N, NAME>(index, absent());
    }

    /**
     * Private ctor.
     */
    private NodePointerIndexedChild(final int index, final NodePointer<N, NAME> pointer) {
        super(pointer);
        this.index = index;
    }

    final int index;

    @Override
    NodePointer<N, NAME> appendToLast(final NodePointer<N, NAME> pointer) {
        return new NodePointerIndexedChild<>(this.index, this.appendToLast0(pointer));
    }

    @Override
    N nextNodeOrNull(final N node) {
        final List<N> children = node.children();
        return this.index < children.size() ?
            children.get(this.index) :
            null;
    }

    @Override
    public boolean isRelative() {
        return false;
    }

    @Override
    N add0(final N node, final N value) {
        return node.setChild(this.index, value);
    }

    @Override
    N remove0(final N node) {
        return this.removeOrFail(node);
    }

    // NodePointerVisitor.............................................................................................

    @Override
    void accept(final NodePointerVisitor<N, NAME> visitor) {
        final int index = this.index;
        if (Visiting.CONTINUE == visitor.startVisitIndexedChild(this, index)) {
            this.acceptNext(visitor);
        }
        visitor.endVisitIndexedChild(this, index);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.index, this.next);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodePointerIndexedChild;
    }

    @Override
    boolean equals1(final NodePointer<?, ?> other) {
        return this.equals2(Cast.to(other));
    }

    private boolean equals2(final NodePointerIndexedChild<?, ?> other) {
        return this.index == other.index;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(SEPARATOR.character());
        b.append(this.index);
    }

    @Override
    void lastToString(final StringBuilder b) {
    }
}
