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

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.Objects;

/**
 * Represents a reference to an invalid array element.
 * <a href="https://tools.ietf.org/html/rfc6901#page-5"></a>
 * <pre>
 * Note that the use of the "-" character to index an array will always
 * result in such an error condition because by definition it refers to
 * a nonexistent array element.  Thus, applications of JSON Pointer need
 * to specify how that character is to be handled, if it is to be
 * useful.
 * ...
 * </pre>
 */
final class NodePointerAppend<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePointer<N, NAME> {

    /**
     * Creates a {@link NodePointerAppend}
     */
    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointerAppend<N, NAME> create() {
        return new NodePointerAppend<N, NAME>(absent());
    }

    /**
     * Private ctor.
     */
    private NodePointerAppend(final NodePointer<N, NAME> next) {
        super(next);
    }

    @Override
    NodePointer<N, NAME> appendToLast(final NodePointer<N, NAME> pointer) {
        throw new UnsupportedOperationException();
    }

    @Override
    N nextNodeOrNull(final N node) {
        return null;
    }

    @Override
    public boolean isRelative() {
        return false;
    }

    @Override
    N add0(final N node, final N value) {
        return node.appendChild(value);
    }

    @Override
    N remove0(final N node) {
        throw new UnsupportedOperationException("Remove not supported for " + this);
    }

    // NodePointerVisitor.............................................................................................

    @Override
    void accept(final NodePointerVisitor<N, NAME> visitor) {
        visitor.visitAppend(this);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.next);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodePointerAppend;
    }

    @Override
    boolean equals1(final NodePointer<?, ?> other) {
        return true;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(SEPARATOR.character());
        b.append(APPEND);
    }

    @Override
    void lastToString(final StringBuilder b) {
    }
}
