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
import walkingkooka.convert.ConversionException;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.visit.Visiting;

import java.util.Objects;

/**
 * A {@link NodeSelector} that selects {@link Node nodes} depending on the result of executing the {@link ExpressionNode}.
 */
final class ExpressionNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        NonTerminalNodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link ExpressionNodeSelector} factory
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    ExpressionNodeSelector<N, NAME, ANAME, AVALUE> with(final ExpressionNode expressionNode) {
        Objects.requireNonNull(expressionNode, "expressionNode");

        return new ExpressionNodeSelector<N, NAME, ANAME, AVALUE>(expressionNode, NodeSelector.terminal());
    }

    /**
     * Private constructor
     */
    private ExpressionNodeSelector(final ExpressionNode expressionNode,
                                   final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
        this.expressionNode = expressionNode;
    }

    @Override
    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return new ExpressionNodeSelector<>(this.expressionNode, selector);
    }

    @Override
    NodeSelectorContext2<N, NAME, ANAME, AVALUE> beginPrepareContext(final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return context.expressionCreateIfNecessary();
    }

    @Override
    NodeSelectorContext2<N, NAME, ANAME, AVALUE> finishPrepareContext(final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        //return context.expressionCreateIfNecessary();
        return context.expression();
    }

    @Override
    N apply1(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        N result = node;

        try {
            context.setNode(node);
            if (context.nodePositionTest(this.expressionNode.toValue(ExpressionNodeSelectorExpressionEvaluationContext.with(node, context)))) {
                result = this.select(node, context);
            }
        } catch (final ConversionException cause) {
            throw new NodeSelectorException("Failed to execute expression: " + this, cause);
        }

        return result;
    }

    /**
     * The expression holds the xpath predicate. If the value is boolean true or a number equal to the current node position
     * the {@link Node} is selected.
     */
    // VisibleForTesting
    final ExpressionNode expressionNode;

    @Override
    final N select(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return this.selectNext(node, context);
    }

    // NodeSelectorVisitor..............................................................................................

    @Override
    Visiting traverseStart(final NodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor) {
        return visitor.startVisitExpression(this, this.expressionNode);
    }

    @Override
    void traverseEnd(final NodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor) {
        visitor.endVisitExpression(this, this.expressionNode);
    }

    // Object...........................................................................................................

    @Override
    int hashCode0(final NodeSelector<N, NAME, ANAME, AVALUE> next) {
        return Objects.hash(next, this.expressionNode);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionNodeSelector;
    }

    @Override
    boolean equals1(final NonTerminalNodeSelector<?, ?, ?, ?> other) {
        return this.equals2(Cast.to(other));
    }

    private boolean equals2(final ExpressionNodeSelector<?, ?, ?, ?> other) {
        return this.expressionNode.equals(other.expressionNode);
    }

    @Override
    void toString1(final NodeSelectorToStringBuilder b) {
        b.expression(this.expressionNode);
    }
}
