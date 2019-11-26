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

import walkingkooka.visit.Visiting;
import walkingkooka.visit.Visitor;

import java.util.Objects;

/**
 * A {@link Visitor} for a graph of {@link Expression}.
 */
public abstract class ExpressionVisitor extends Visitor<Expression> {

    // Expression.......................................................................................................

    public final void accept(final Expression node) {
        Objects.requireNonNull(node, "node");

        if (Visiting.CONTINUE == this.startVisit(node)) {
            node.accept(this);
        }
        this.endVisit(node);
    }

    protected Visiting startVisit(final Expression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final Expression node) {
        // nop
    }

    protected void visit(final BigDecimalExpression node) {
        // nop
    }

    protected void visit(final BigIntegerExpression node) {
        // nop
    }

    protected void visit(final BooleanExpression node) {
        // nop
    }

    protected void visit(final DoubleExpression node) {
        // nop
    }

    protected void visit(final LocalDateExpression node) {
        // nop
    }

    protected void visit(final LocalDateTimeExpression node) {
        // nop
    }

    protected void visit(final LocalTimeExpression node) {
        // nop
    }

    protected void visit(final LongExpression node) {
        // nop
    }

    protected void visit(final ReferenceExpression node) {
        // nop
    }

    protected void visit(final StringExpression node) {
        // nop
    }

    protected Visiting startVisit(final AddExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final AddExpression node) {
        // nop
    }

    protected Visiting startVisit(final AndExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final AndExpression node) {
        // nop
    }

    protected Visiting startVisit(final DivideExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final DivideExpression node) {
        // nop
    }

    protected Visiting startVisit(final EqualsExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final EqualsExpression node) {
        // nop
    }

    protected Visiting startVisit(final FunctionExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final FunctionExpression node) {
        // nop
    }

    protected Visiting startVisit(final GreaterThanExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final GreaterThanExpression node) {
        // nop
    }

    protected Visiting startVisit(final GreaterThanEqualsExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final GreaterThanEqualsExpression node) {
        // nop
    }

    protected Visiting startVisit(final LessThanExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final LessThanExpression node) {
        // nop
    }

    protected Visiting startVisit(final LessThanEqualsExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final LessThanEqualsExpression node) {
        // nop
    }

    protected Visiting startVisit(final ModuloExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ModuloExpression node) {
        // nop
    }

    protected Visiting startVisit(final MultiplyExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final MultiplyExpression node) {
        // nop
    }

    protected Visiting startVisit(final NegativeExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NegativeExpression node) {
        // nop
    }

    protected Visiting startVisit(final NotEqualsExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NotEqualsExpression node) {
        // nop
    }

    protected Visiting startVisit(final NotExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NotExpression node) {
        // nop
    }

    protected Visiting startVisit(final OrExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final OrExpression node) {
        // nop
    }

    protected Visiting startVisit(final PowerExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final PowerExpression node) {
        // nop
    }

    protected Visiting startVisit(final SubtractExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SubtractExpression node) {
        // nop
    }

    protected Visiting startVisit(final XorExpression node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final XorExpression node) {
        // nop
    }
}
