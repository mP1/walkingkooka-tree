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
import walkingkooka.NeverError;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

/**
 * A {@link NodeSelector} that selects and does nothing.
 */
final class TerminalNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        NonCustomToStringNodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link TerminalNodeSelector} getter
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> TerminalNodeSelector<N, NAME, ANAME, AVALUE> get() {
        return Cast.to(TerminalNodeSelector.INSTANCE);
    }

    @SuppressWarnings("rawtypes")
    private final static TerminalNodeSelector INSTANCE = new TerminalNodeSelector();

    /**
     * Private constructor use type safe getter
     */
    private TerminalNodeSelector() {
        super();
    }

    @Override
    NodeSelector<N, NAME, ANAME, AVALUE> append0(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return selector;
    }

    @Override
    NodeSelectorContext2<N, NAME, ANAME, AVALUE> beginPrepareContext(final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return context.all();
    }

    @Override
    NodeSelectorContext2<N, NAME, ANAME, AVALUE> finishPrepareContext(final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return context.all();
    }

    @Override final N apply1(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return !context.isFinished() && context.test(node) ?
                node.replace(context.selected(node)) :
                node;
    }

    @Override
    N select(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        throw new NeverError(this.getClass() + ".select(Node, NodeSelectorContext)");
    }

    // NodeSelectorVisitor..............................................................................................

    @Override
    void accept0(final NodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor) {
        visitor.visitTerminal(this);
    }

    // Object...........................................................................................................

    @Override
    void toString0(final NodeSelectorToStringBuilder b) {
        // nop
    }
}
