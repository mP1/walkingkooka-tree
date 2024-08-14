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

import java.util.List;

/**
 * A power expression.
 */
public final class PowerExpression extends BinaryArithmeticExpression {

    public final static ExpressionFunctionName NAME = ExpressionFunctionName.fromClass(PowerExpression.class);

    public final static String SYMBOL = "^^";

    static PowerExpression with(final Expression left, final Expression right) {
        check(left, right);
        return new PowerExpression(NO_INDEX, left, right);
    }

    private PowerExpression(final int index, final Expression left, final Expression right) {
        super(index, left, right);
    }

    @Override
    public ExpressionFunctionName name() {
        return NAME;
    }

    @Override
    public PowerExpression removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    public PowerExpression setChildren(final List<Expression> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    PowerExpression replace1(final int index, final Expression left, final Expression right) {
        return new PowerExpression(index, left, right);
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
    String applyText(final String left, final String right, final ExpressionEvaluationContext context) {
        throw new UnsupportedOperationException(left + SYMBOL + right);
    }

    @Override
    ExpressionNumber applyExpressionNumber(final ExpressionNumber left,
                                           final ExpressionNumber right,
                                           final ExpressionEvaluationContext context) {
        return left.power(right, context);
    }

    // Object .........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof PowerExpression;
    }

    @Override
    void appendSymbol(final StringBuilder b) {
        b.append(SYMBOL);
    }
}
