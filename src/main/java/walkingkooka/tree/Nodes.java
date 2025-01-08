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
import walkingkooka.naming.Name;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.tree.pointer.NodePointer;

import java.util.Optional;

final public class Nodes implements PublicStaticHelper {

    public static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name, AVALUE>
    Node<N, NAME, ANAME, AVALUE> fake() {
        return new FakeNode<>();
    }

    /**
     * Only called by default method {@link Node#pointer()}. Walks the parent axis, until the axis and then begins
     * building a {@link NodePointer}.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> NodePointer<N, NAME> pointer(final N node) {

        return pointer0(node);
    }

    private static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> NodePointer<N, NAME> pointer0(final N node) {

        final Optional<N> parent = node.parent();
        return parent.isPresent() ?
            pointer1(parent.get(), node) :
            walkingkooka.tree.pointer.NodePointer.any(Cast.to(node.getClass()));
    }

    private static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> NodePointer<N, NAME> pointer1(final N parent,
                                              final N node) {

        final NodePointer<N, NAME> back = pointer0(parent);
        return node.hasUniqueNameAmongstSiblings() ?
            back.named(node.name()) :
            back.indexed(node.index());
    }

    /**
     * Stop creation
     */
    private Nodes() {
        throw new UnsupportedOperationException();
    }
}
