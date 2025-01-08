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

package walkingkooka.tree.select;

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.visit.Visiting;

import java.util.Optional;

/**
 * Base class for all non logical selectors.
 */
abstract class NonTerminalNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
    extends NonCustomToStringNodeSelector<N, NAME, ANAME, AVALUE> {

    NonTerminalNodeSelector(final NodeSelector<N, NAME, ANAME, AVALUE> next) {
        super();
        this.next = next;
    }

    @Override final NodeSelector<N, NAME, ANAME, AVALUE> append0(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.append1(this.next.append0(selector));
    }

    abstract NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector);

    /**
     * Used to select a node wrapped in an optional. Empty optionals have no effect.
     */
    final N selectChild(final Optional<N> node,
                        final N parent,
                        final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return node.map(child -> this.select(child, context).parentOrFail())
            .orElse(parent);
    }

    /**
     * The default simply records the {@link Node} to the {@link NodeSelectorContext}.
     */
    final N selectNext(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return this.next.apply0(node, context);
    }

    // Testing...
    final NodeSelector<N, NAME, ANAME, AVALUE> next;

    // NodeSelectorVisitor..............................................................................................

    @Override final void accept0(final NodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor) {
        if (Visiting.CONTINUE == this.traverseStart(visitor)) {
            this.next.traverse(visitor);
        }
        this.traverseEnd(visitor);
    }

    abstract Visiting traverseStart(final NodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor);

    abstract void traverseEnd(final NodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor);

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return this.hashCode0(this.next);
    }

    abstract int hashCode0(final NodeSelector<N, NAME, ANAME, AVALUE> next);

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public final boolean equals(final Object other) {
        return this == other ||
            this.canBeEqual(other) && this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final NonTerminalNodeSelector<?, ?, ?, ?> other) {
        return this.equals1(other) && this.next.equals(other.next);
    }

    abstract boolean equals1(final NonTerminalNodeSelector<?, ?, ?, ?> other);

    @Override final void toString0(final NodeSelectorToStringBuilder b) {
        this.toString1(b);
        this.toStringNext(b);
    }

    abstract void toString1(final NodeSelectorToStringBuilder b);

    final void toStringNext(final NodeSelectorToStringBuilder b) {
        if (null != this.next) {
            this.next.toString0(b);
        }
    }
}
