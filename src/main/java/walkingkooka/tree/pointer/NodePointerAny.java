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

import java.util.Objects;

/**
 * Matches all the nodes, or the start node.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
final class NodePointerAny<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePointer<N, NAME> {

    /**
     * Creates a {@link NodePointerAny}
     */
    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointerAny<N, NAME> get() {
        return Cast.to(INSTANCE);
    }

    private final static NodePointer<?, ?> INSTANCE = new NodePointerAny<>();

    /**
     * Private ctor.
     */
    private NodePointerAny() {
        super(null);
    }

    @Override
    NodePointer<N, NAME> appendToLast(final NodePointer<N, NAME> pointer) {
        return pointer;
    }

    @Override
    N nextNodeOrNull(final N node) {
        return node;
    }

    @Override
    public boolean isRelative() {
        return false;
    }

    @Override
    N add0(final N node, final N value) {
        throw new UnsupportedOperationException("Add not supported for " + this);
    }

    @Override
    N remove0(final N node) {
        throw new UnsupportedOperationException("Remove not supported for " + this);
    }

    // NodePointerVisitor................................................................................

    void accept(final NodePointerVisitor<N, NAME> visitor) {
        visitor.visitAny(this);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.next);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodePointerAny;
    }

    @Override
    boolean equals1(final NodePointer<?, ?> other) {
        return true;
    }

    @Override
    void toString0(final StringBuilder b) {
        // nop
    }

    @Override
    void lastToString(final StringBuilder b) {
    }
}
