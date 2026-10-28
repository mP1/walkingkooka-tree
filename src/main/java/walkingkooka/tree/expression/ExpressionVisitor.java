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

import walkingkooka.visit.Visiting;
import walkingkooka.visit.Visitor;

import java.util.Objects;

/**
 * A {@link Visitor} for a graph of {@link Expression}.
 */
public abstract class ExpressionVisitor extends Visitor<Expression> {

    // Expression.......................................................................................................

    @Override
    public final void accept(final Expression expression) {
        Objects.requireNonNull(expression, "expression");

        if (Visiting.CONTINUE == this.startVisit(expression)) {
            expression.accept(this);
        }
        this.endVisit(expression);
    }

    protected Visiting startVisit(final Expression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final Expression expression) {
        // nop
    }

    protected void visit(final ReferenceExpression expression) {
        // nop
    }

    protected void visit(final ValueExpression<?> expression) {
        // nop
    }

    protected Visiting startVisit(final AddExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final AddExpression expression) {
        // nop
    }

    protected Visiting startVisit(final AndExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final AndExpression expression) {
        // nop
    }

    protected Visiting startVisit(final CallExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final CallExpression expression) {
        // nop
    }

    protected Visiting startVisit(final DivideExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final DivideExpression expression) {
        // nop
    }

    protected Visiting startVisit(final EqualsExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final EqualsExpression expression) {
        // nop
    }

    protected Visiting startVisit(final GreaterThanExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final GreaterThanExpression expression) {
        // nop
    }

    protected Visiting startVisit(final GreaterThanEqualsExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final GreaterThanEqualsExpression expression) {
        // nop
    }

    protected Visiting startVisit(final LambdaFunctionExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final LambdaFunctionExpression expression) {
        // nop
    }

    protected Visiting startVisit(final LessThanExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final LessThanExpression expression) {
        // nop
    }

    protected Visiting startVisit(final LessThanEqualsExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final LessThanEqualsExpression expression) {
        // nop
    }

    protected Visiting startVisit(final ListExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ListExpression expression) {
        // nop
    }

    protected Visiting startVisit(final ModuloExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ModuloExpression expression) {
        // nop
    }

    protected Visiting startVisit(final MultiplyExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final MultiplyExpression expression) {
        // nop
    }

    protected void visit(final NamedFunctionExpression expression) {
    }

    protected Visiting startVisit(final NegativeExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NegativeExpression expression) {
        // nop
    }

    protected Visiting startVisit(final NotEqualsExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NotEqualsExpression expression) {
        // nop
    }

    protected Visiting startVisit(final NotExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NotExpression expression) {
        // nop
    }

    protected Visiting startVisit(final OrExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final OrExpression expression) {
        // nop
    }

    protected Visiting startVisit(final PowerExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final PowerExpression expression) {
        // nop
    }

    protected Visiting startVisit(final SubtractExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SubtractExpression expression) {
        // nop
    }

    protected Visiting startVisit(final XorExpression expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final XorExpression expression) {
        // nop
    }
}
