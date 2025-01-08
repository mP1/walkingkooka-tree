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

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.Expression;

/**
 * Base class for several {@link NodeSelectorContext} wrappers. Static factory methods are also available for all sub classes.
 */
abstract class NodeSelectorContext2<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> implements NodeSelectorContext<N, NAME, ANAME, AVALUE> {

    /**
     * {@see AllNodeSelectorContext2}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> NodeSelectorContext2<N, NAME, ANAME, AVALUE> all(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        return NodeSelectorContext2All.with(context);
    }

    /**
     * {@see NodeSelectorContext2ExpressionNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> NodeSelectorContext2ExpressionNodeSelector<N, NAME, ANAME, AVALUE> expression(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        return NodeSelectorContext2ExpressionNodeSelector.with(context);
    }

    /**
     * Package private to limit sub classing.
     */
    NodeSelectorContext2(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        super();
        this.context = context;
    }

    // delegate NodeSelectorContext methods to this.context.

    @Override
    public final boolean isFinished() {
        return this.context.isFinished();
    }

    @Override
    public final N node() {
        return this.context.node();
    }

    @Override
    public final void setNode(final N node) {
        this.context.setNode(node);
    }

    @Override
    public final boolean test(final N node) {
        return this.context.test(node);
    }

    @Override
    public final N selected(final N node) {
        return this.context.selected(node);
    }

    @Override
    public final Object evaluate(final Expression expression) {
        return this.context.evaluate(expression);
    }

    /**
     * Unconditionally returns a {@link NodeSelectorContext2All}.
     */
    abstract NodeSelectorContext2<N, NAME, ANAME, AVALUE> all();

    /**
     * The context should create a {@link NodeSelectorContext2ExpressionNodeSelector} if it is not already one.
     */
    abstract NodeSelectorContext2<N, NAME, ANAME, AVALUE> expressionCreateIfNecessary();

    /**
     * Unconditionally create a {@link NodeSelectorContext2ExpressionNodeSelector}.
     */
    abstract NodeSelectorContext2<N, NAME, ANAME, AVALUE> expression();

    /**
     * Invoked during a {@link ExpressionNodeSelector} to test a value against the position of the current {@link Node}.
     */
    abstract boolean isNodeSelected(final Expression expression);

    /**
     * Returns the current node's position.
     */
    abstract int nodePosition();

    /**
     * The active {@link NodeSelectorContext}.
     */
    final NodeSelectorContext<N, NAME, ANAME, AVALUE> context;
}
