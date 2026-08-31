/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
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
 * Base class for several {@link NodeSelectorContext} wrappers. Static factory methods are also available for all subclasses.
 */
abstract class NodeSelectorNodeSelectorContext<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> implements NodeSelectorContext<N, NAME, ANAME, AVALUE> {

    /**
     * {@see AllNodeSelectorContext2}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> NodeSelectorNodeSelectorContext<N, NAME, ANAME, AVALUE> all(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        return NodeSelectorNodeSelectorContextAll.with(context);
    }

    /**
     * {@see NodeSelectorContext2ExpressionNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> NodeSelectorNodeSelectorContextExpressionNodeSelector<N, NAME, ANAME, AVALUE> expression(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        return NodeSelectorNodeSelectorContextExpressionNodeSelector.with(context);
    }

    /**
     * Package private to limit sub classing.
     */
    NodeSelectorNodeSelectorContext(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
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
    public final Object evaluateExpression(final Expression expression) {
        return this.context.evaluateExpression(expression);
    }

    /**
     * Unconditionally returns a {@link NodeSelectorNodeSelectorContextAll}.
     */
    abstract NodeSelectorNodeSelectorContext<N, NAME, ANAME, AVALUE> all();

    /**
     * The context should create a {@link NodeSelectorNodeSelectorContextExpressionNodeSelector} if it is not already one.
     */
    abstract NodeSelectorNodeSelectorContext<N, NAME, ANAME, AVALUE> expressionCreateIfNecessary();

    /**
     * Unconditionally create a {@link NodeSelectorNodeSelectorContextExpressionNodeSelector}.
     */
    abstract NodeSelectorNodeSelectorContext<N, NAME, ANAME, AVALUE> expression();

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
