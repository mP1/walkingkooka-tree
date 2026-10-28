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
import walkingkooka.tree.expression.AddExpression;
import walkingkooka.tree.expression.AndExpression;
import walkingkooka.tree.expression.CallExpression;
import walkingkooka.tree.expression.DivideExpression;
import walkingkooka.tree.expression.EqualsExpression;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionVisitor;
import walkingkooka.tree.expression.GreaterThanEqualsExpression;
import walkingkooka.tree.expression.GreaterThanExpression;
import walkingkooka.tree.expression.LessThanEqualsExpression;
import walkingkooka.tree.expression.LessThanExpression;
import walkingkooka.tree.expression.ListExpression;
import walkingkooka.tree.expression.ModuloExpression;
import walkingkooka.tree.expression.MultiplyExpression;
import walkingkooka.tree.expression.NegativeExpression;
import walkingkooka.tree.expression.NotEqualsExpression;
import walkingkooka.tree.expression.NotExpression;
import walkingkooka.tree.expression.OrExpression;
import walkingkooka.tree.expression.PowerExpression;
import walkingkooka.tree.expression.SubtractExpression;
import walkingkooka.tree.expression.ValueExpression;
import walkingkooka.tree.expression.XorExpression;
import walkingkooka.visit.Visiting;

import java.util.function.Predicate;

/**
 * A {@link ExpressionVisitor} that analyzes accepted {@link Expression} and possibly adds a {@link Predicate}
 * in place of the actual {@link Expression}.
 */
final class NodeSelectorExpressionExpressionVisitor<N extends Node<N, NAME, ANAME, AVALUE>,
    NAME extends Name,
    ANAME extends Name,
    AVALUE> extends ExpressionVisitor {

    static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> acceptExpression(final Expression expression,
                                                                      final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        final NodeSelectorExpressionExpressionVisitor<N, NAME, ANAME, AVALUE> visitor = new NodeSelectorExpressionExpressionVisitor<>(selector);
        visitor.accept(expression);
        return visitor.addExpression ?
            selector.append(ExpressionNodeSelector.with(expression)) :
            visitor.selector;
    }

    NodeSelectorExpressionExpressionVisitor(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super();
        this.selector = selector;
    }

    @Override
    protected Visiting startVisit(final AddExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final AndExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final CallExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final DivideExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final EqualsExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final GreaterThanExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final GreaterThanEqualsExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final LessThanExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final LessThanEqualsExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ListExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final ModuloExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final MultiplyExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final NegativeExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final NotEqualsExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final NotExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final OrExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final PowerExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final SubtractExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final XorExpression expression) {
        return Visiting.SKIP;
    }

    @Override
    protected void visit(final ValueExpression<?> expression) {
        final Object value = expression.value();
        if (Boolean.TRUE.equals(expression.value())) {
            this.selector = this.selector.setToString(this.selector + "[true()]");
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
