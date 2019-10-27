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

public class FakeExpressionNodeVisitor extends ExpressionNodeVisitor implements Fake {
    public FakeExpressionNodeVisitor() {
    }

    @Override
    protected Visiting startVisit(final ExpressionNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final ExpressionBigDecimalNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final ExpressionBooleanNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final ExpressionDoubleNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final ExpressionLocalDateNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final ExpressionLocalDateTimeNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final ExpressionLocalTimeNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final ExpressionLongNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final ExpressionBigIntegerNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final ExpressionReferenceNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final ExpressionTextNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ExpressionAdditionNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionAdditionNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ExpressionAndNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionAndNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ExpressionDivisionNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionDivisionNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ExpressionEqualsNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionEqualsNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ExpressionGreaterThanNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionGreaterThanNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ExpressionGreaterThanEqualsNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionGreaterThanEqualsNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ExpressionLessThanNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionLessThanNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ExpressionLessThanEqualsNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionLessThanEqualsNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ExpressionModuloNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionModuloNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ExpressionMultiplicationNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionMultiplicationNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ExpressionNegativeNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionNegativeNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ExpressionNotNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionNotNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ExpressionNotEqualsNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionNotEqualsNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ExpressionOrNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionOrNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ExpressionPowerNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionPowerNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ExpressionSubtractionNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ExpressionSubtractionNode node) {
        throw new UnsupportedOperationException();
    }
}
