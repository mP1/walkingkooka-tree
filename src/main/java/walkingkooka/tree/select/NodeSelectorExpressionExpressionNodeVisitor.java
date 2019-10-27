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
import walkingkooka.tree.expression.ExpressionAdditionNode;
import walkingkooka.tree.expression.ExpressionAndNode;
import walkingkooka.tree.expression.ExpressionBooleanNode;
import walkingkooka.tree.expression.ExpressionDivisionNode;
import walkingkooka.tree.expression.ExpressionEqualsNode;
import walkingkooka.tree.expression.ExpressionFunctionNode;
import walkingkooka.tree.expression.ExpressionGreaterThanEqualsNode;
import walkingkooka.tree.expression.ExpressionGreaterThanNode;
import walkingkooka.tree.expression.ExpressionLessThanEqualsNode;
import walkingkooka.tree.expression.ExpressionLessThanNode;
import walkingkooka.tree.expression.ExpressionModuloNode;
import walkingkooka.tree.expression.ExpressionMultiplicationNode;
import walkingkooka.tree.expression.ExpressionNegativeNode;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.expression.ExpressionNodeVisitor;
import walkingkooka.tree.expression.ExpressionNotEqualsNode;
import walkingkooka.tree.expression.ExpressionNotNode;
import walkingkooka.tree.expression.ExpressionOrNode;
import walkingkooka.tree.expression.ExpressionPowerNode;
import walkingkooka.tree.expression.ExpressionSubtractionNode;
import walkingkooka.tree.expression.ExpressionXorNode;
import walkingkooka.visit.Visiting;

import java.util.function.Predicate;

/**
 * A {@link ExpressionNodeVisitor} that analyzes accepted {@link ExpressionNode} and possibly adds a {@link Predicate}
 * in place of the actual {@link ExpressionNode}.
 */
final class NodeSelectorExpressionExpressionNodeVisitor<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> extends ExpressionNodeVisitor {

    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> acceptExpression(final ExpressionNode expression,
                                                                          final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        final NodeSelectorExpressionExpressionNodeVisitor<N, NAME, ANAME, AVALUE> visitor = new NodeSelectorExpressionExpressionNodeVisitor<>(selector);
        visitor.accept(expression);
        return visitor.addExpression ?
                selector.append(ExpressionNodeSelector.with(expression)) :
                visitor.selector;
    }

    NodeSelectorExpressionExpressionNodeVisitor(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super();
        this.selector = selector;
    }

    @Override
    protected Visiting startVisit(final ExpressionAdditionNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionAndNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionDivisionNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionEqualsNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionFunctionNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionGreaterThanNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionGreaterThanEqualsNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionLessThanNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionLessThanEqualsNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionModuloNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionMultiplicationNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionNegativeNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionNotEqualsNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionNotNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionOrNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionPowerNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionSubtractionNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ExpressionXorNode node) {
        return Visiting.SKIP;
    }

    @Override
    protected void visit(final ExpressionBooleanNode node) {
        if (node.value()) {
            this.selector = this.selector.setToString(this.selector.toString() + "[true()]");
            this.addExpression = false;
        }
    }

    private NodeSelector<N, NAME, ANAME, AVALUE> selector;

    private boolean addExpression = true;

    @Override
    public String toString() {
        return this.selector.toString();
    }
}
