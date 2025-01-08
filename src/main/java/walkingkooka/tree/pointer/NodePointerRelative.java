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

import java.util.Objects;
import java.util.Optional;

/**
 * Represents the opening relative pointer..
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
final class NodePointerRelative<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePointer<N, NAME> {

    /**
     * Creates a {@link NodePointerRelative}
     */
    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointerRelative<N, NAME> with(final int ancestorCount,
                                                                                                final boolean hash) {
        if (ancestorCount < 0) {
            throw new IllegalArgumentException("Invalid ancestorCount " + ancestorCount + " values should be greater or equal to 0");
        }

        return new NodePointerRelative<N, NAME>(ancestorCount, hash, absent());
    }

    /**
     * Private ctor.
     */
    private NodePointerRelative(final int ancestorCount, final boolean hash, final NodePointer<N, NAME> pointer) {
        super(pointer);
        this.ancestorCount = ancestorCount;
        this.hash = hash;
    }

    @Override
    NodePointer<N, NAME> appendToLast(final NodePointer<N, NAME> pointer) {
        return new NodePointerRelative<>(this.ancestorCount, this.hash, this.appendToLast0(pointer));
    }

    @Override
    N nextNodeOrNull(final N node) {
        N next = node;

        for (int i = 0; i < this.ancestorCount; i++) {
            final Optional<N> parent = next.parent();
            if (!parent.isPresent()) {
                next = null;
                break;
            }
            next = parent.get();
        }

        return next;
    }

    final int ancestorCount;
    private final boolean hash;

    @Override
    public boolean isRelative() {
        return true;
    }

    @Override
    N add0(final N node, final N value) {
        return this.next.add0(node, value);
    }

    @Override
    N remove0(final N node) {
        return this.traverseOrFail(node)
            .parentWithout()
            .orElseThrow(() -> new NodePointerException("Unable to remove " + this + " from " + node));
    }

    // NodePointerVisitor.............................................................................................

    @Override
    void accept(final NodePointerVisitor<N, NAME> visitor) {
        if (Visiting.CONTINUE == visitor.startVisitRelative(this)) {
            this.acceptNext(visitor);
        }
        visitor.endVisitRelative(this);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.next);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodePointerRelative;
    }

    @Override
    boolean equals1(final NodePointer<?, ?> other) {
        return this.equals2(Cast.to(other));
    }

    private boolean equals2(final NodePointerRelative<?, ?> other) {
        return this.ancestorCount == other.ancestorCount &&
            this.hash == other.hash;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(this.ancestorCount);
    }

    @Override
    void lastToString(final StringBuilder b) {
        if (this.hash) {
            b.append('#');
        }
    }
}
