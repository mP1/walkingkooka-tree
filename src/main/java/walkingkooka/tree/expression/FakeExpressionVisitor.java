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

package walkingkooka.tree.expression;

import walkingkooka.test.Fake;
import walkingkooka.visit.Visiting;

public class FakeExpressionVisitor extends ExpressionVisitor implements Fake {
    public FakeExpressionVisitor() {
    }

    @Override
    protected Visiting startVisit(final Expression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final Expression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final NamedFunctionExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final ReferenceExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final ValueExpression<?> expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final AddExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final AddExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final AndExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final AndExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final CallExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final CallExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final DivideExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final DivideExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final EqualsExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final EqualsExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final GreaterThanExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final GreaterThanExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final GreaterThanEqualsExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final GreaterThanEqualsExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final LambdaFunctionExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final LambdaFunctionExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final LessThanExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final LessThanExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final LessThanEqualsExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final LessThanEqualsExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ListExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ListExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ModuloExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ModuloExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final MultiplyExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final MultiplyExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final NegativeExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final NegativeExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final NotExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final NotExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final NotEqualsExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final NotEqualsExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final OrExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final OrExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final PowerExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final PowerExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final SubtractExpression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final SubtractExpression expression) {
        throw new UnsupportedOperationException();
    }
}
