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
 * A {@link NodeSelector} that selects all the preceding nodes of a given {@link Node}. It does this by asking all ancestors to process their previous
 * siblings and their ancestors.
 */
final class PrecedingNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
    extends
    AxisNodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link PrecedingNodeSelector} getter
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> PrecedingNodeSelector<N, NAME, ANAME, AVALUE> get() {
        return Cast.to(PrecedingNodeSelector.INSTANCE);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private final static PrecedingNodeSelector INSTANCE = new PrecedingNodeSelector(NodeSelector.terminal());

    /**
     * Private constructor
     */
    private PrecedingNodeSelector(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
    }

    @Override
    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        // no point appending a preceeding to another...
        return selector instanceof PrecedingNodeSelector ?
            this :
            new PrecedingNodeSelector<>(selector);
    }

    @Override
    N apply1(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return this.selectPrecedingSiblings(node, context);
    }

    @Override
    N select(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        final N node2 = this.selectNext(node, context);
        return this.selectChildren(node2, context);
    }

    // NodeSelectorVisitor..............................................................................................

    @Override
    Visiting traverseStart(final NodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor) {
        return visitor.startVisitPreceding(this);
    }

    @Override
    void traverseEnd(final NodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor) {
        visitor.endVisitPreceding(this);
    }

    // Object...........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof PrecedingNodeSelector;
    }

    @Override
    void toString1(final NodeSelectorToStringBuilder b) {
        b.axisName("preceding");
    }
}
