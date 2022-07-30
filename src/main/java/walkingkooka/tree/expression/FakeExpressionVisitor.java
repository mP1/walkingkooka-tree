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

package walkingkooka.tree.expression;

import walkingkooka.test.Fake;
import walkingkooka.visit.Visiting;

public class FakeExpressionVisitor extends ExpressionVisitor implements Fake {
    public FakeExpressionVisitor() {
    }

    @Override
    protected Visiting startVisit(final Expression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final Expression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final ReferenceExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final ValueExpression<?> node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final AddExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final AddExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final AndExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final AndExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final DivideExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final DivideExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final EqualsExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final EqualsExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final GreaterThanExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final GreaterThanExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final GreaterThanEqualsExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final GreaterThanEqualsExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final LessThanExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final LessThanExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final LessThanEqualsExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final LessThanEqualsExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ListExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ListExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ModuloExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ModuloExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final MultiplyExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final MultiplyExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final NamedFunctionExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final NamedFunctionExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final NegativeExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final NegativeExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final NotExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final NotExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final NotEqualsExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final NotEqualsExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final OrExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final OrExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final PowerExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final PowerExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final SubtractExpression node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final SubtractExpression node) {
        throw new UnsupportedOperationException();
    }
}
