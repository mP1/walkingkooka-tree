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

import java.util.Objects;
import java.util.function.Predicate;

/**
 * A {@link NodeSelector} that passes the current {@link Node} to the {@link Predicate}.
 */
final class NodePredicateNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
    extends
    NamedOrNodePredicateNodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link NodePredicateNodeSelector} getter
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE>
    NodePredicateNodeSelector<N, NAME, ANAME, AVALUE> with(final Predicate<N> predicate) {
        Objects.requireNonNull(predicate, "predicate");

        return new NodePredicateNodeSelector<>(predicate, NodeSelector.terminal());
    }

    /**
     * Private constructor
     */
    private NodePredicateNodeSelector(final Predicate<N> predicate, final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
        this.predicate = predicate;
    }

    @Override
    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return new NodePredicateNodeSelector<>(this.predicate, selector);
    }

    @Override
    N apply1(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        context.setNode(node);

        return this.predicate.test(node) ?
            this.select(node, context) :
            node;
    }

    private final Predicate<N> predicate;

    // NodeSelectorVisitor..............................................................................................

    @Override
    Visiting traverseStart(final NodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor) {
        return visitor.startVisitPredicate(this, this.predicate);
    }

    @Override
    void traverseEnd(final NodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor) {
        visitor.endVisitPredicate(this, this.predicate);
    }

    // Object...........................................................................................................

    @Override
    int hashCode0(final NodeSelector<N, NAME, ANAME, AVALUE> next) {
        return Objects.hash(next, this.predicate);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodePredicateNodeSelector;
    }

    @Override
    boolean equals1(final NonTerminalNodeSelector<?, ?, ?, ?> other) {
        return this.equals2(Cast.to(other));
    }

    private boolean equals2(final NodePredicateNodeSelector<?, ?, ?, ?> other) {
        return this.predicate.equals(other.predicate);
    }

    @Override
    void toString1(final NodeSelectorToStringBuilder b) {
        b.predicate(this.predicate);
    }
}
