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
 * A {@link NodeSelectorContext2} that tracks the position of selected {@link Node}. This allows {@link ExpressionNodeSelector} to
 * test numeric values against the current position of the current {@link Node}.
 */
final class NodeSelectorContext2ExpressionNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
    extends NodeSelectorContext2<N, NAME, ANAME, AVALUE> {

    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NodeSelectorContext2ExpressionNodeSelector<N, NAME, ANAME, AVALUE> with(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        return new NodeSelectorContext2ExpressionNodeSelector<>(context);
    }

    private NodeSelectorContext2ExpressionNodeSelector(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        super(context);
    }

    @Override
    NodeSelectorContext2<N, NAME, ANAME, AVALUE> all() {
        return NodeSelectorContext2.all(this.context);
    }

    @Override
    NodeSelectorContext2<N, NAME, ANAME, AVALUE> expressionCreateIfNecessary() {
        return this;
    }

    @Override
    NodeSelectorContext2<N, NAME, ANAME, AVALUE> expression() {
        return NodeSelectorContext2.expression(this.context);
    }

    /**
     * If the expression is a boolean return that or if a number compare that against the {@link #nodePosition).
     */
    @Override
    boolean isNodeSelected(final Expression expression) {
        final Object value = this.evaluate(expression);
        boolean selected = Boolean.TRUE.equals(value);
        if (false == selected && value instanceof Number) {
            final Number number = (Number) value;
            selected = number.intValue() == this.nodePosition();
        }
        this.position++;
        return selected;
    }

    @Override
    int nodePosition() {
        return this.position;
    }

    // VisibleForTesting
    int position = NodeSelector.INDEX_BIAS;

    @Override
    public String toString() {
        return "position: " + this.position + " " + this.context.toString();
    }
}
