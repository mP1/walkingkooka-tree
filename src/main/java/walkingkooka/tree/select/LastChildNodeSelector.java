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

/**
 * A {@link NodeSelector} that selects the last child of any given {@link Node}.
 */
final class LastChildNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        AxisNodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link LastChildNodeSelector} getter
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> LastChildNodeSelector<N, NAME, ANAME, AVALUE> get() {
        return Cast.to(LastChildNodeSelector.INSTANCE);
    }

    @SuppressWarnings("unchecked")
    private final static LastChildNodeSelector INSTANCE = new LastChildNodeSelector(NodeSelector.terminal());

    /**
     * Private constructor
     */
    private LastChildNodeSelector(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
    }

    @Override
    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return new LastChildNodeSelector<>(selector);
    }

    @Override
    N apply1(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return this.selectChild(node.lastChild(), node, context);
    }

    @Override final N select(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return this.selectNext(node, context);
    }

    // NodeSelectorVisitor..............................................................................................

    @Override
    Visiting traverseStart(final NodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor) {
        return visitor.startVisitLastChild(this);
    }

    @Override
    void traverseEnd(final NodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor) {
        visitor.endVisitLastChild(this);
    }

    // Object...........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LastChildNodeSelector;
    }

    @Override
    void toString1(final NodeSelectorToStringBuilder b) {
        b.axisName("last-child");
    }
}
