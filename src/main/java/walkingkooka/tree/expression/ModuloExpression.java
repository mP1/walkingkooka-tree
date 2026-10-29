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

import java.util.List;

/**
 * A modulo expression.
 */
public final class ModuloExpression extends ArithmeticExpression {

    public final static ExpressionFunctionName NAME = ExpressionFunctionName.fromClass(ModuloExpression.class);

    public final static String SYMBOL = "%";

    static ModuloExpression with(final Expression left,
                                 final Expression right) {
        return new ModuloExpression(
            NO_INDEX,
            ExpressionChildrenListBinary.with(
                left,
                right
            )
        );
    }

    private ModuloExpression(final int index,
                             final ExpressionChildrenListBinary children) {
        super(
            index,
            children
        );
    }

    @Override
    public ExpressionFunctionName name() {
        return NAME;
    }

    @Override
    public ModuloExpression removeParent() {
        return this.removeParent0()
            .cast();
    }

    @Override
    public ModuloExpression setChildren(final List<Expression> children) {
        return this.setChildren0(children)
            .cast();
    }

    @Override
    ModuloExpression replaceChildren(final ExpressionChildrenListBinary expressionChildrenList) {
        return new ModuloExpression(
            NO_INDEX,
            expressionChildrenList
        );
    }

    @Override
    ModuloExpression replace(final int index) {
        return new ModuloExpression(
            index,
            this.children
        );
    }

    // Visitor .........................................................................................................


    @Override
    public void accept(final ExpressionVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // Evaluation .......................................................................................................

    @Override
    String applyText(final String left,
                     final String right,
                     final ExpressionEvaluationContext context) {
        throw new UnsupportedOperationException(
            left + SYMBOL + right
        );
    }

    @Override
    ExpressionNumber applyExpressionNumber(final ExpressionNumber left,
                                           final ExpressionNumber right,
                                           final ExpressionEvaluationContext context) {
        return left.modulo(
            right,
            context
        );
    }

    // Object .........................................................................................................

    @Override
    void appendSymbol(final StringBuilder b) {
        b.append(SYMBOL);
    }
}
