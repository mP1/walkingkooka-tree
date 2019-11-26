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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * A division expression.
 */
public final class DivisionExpression extends BinaryArithmeticExpression {

    public final static FunctionExpressionName NAME = FunctionExpressionName.fromClass(DivisionExpression.class);

    public final static String SYMBOL = "/";

    static DivisionExpression with(final Expression left, final Expression right) {
        check(left, right);
        return new DivisionExpression(NO_INDEX, left, right);
    }

    private DivisionExpression(final int index, final Expression left, final Expression right) {
        super(index, left, right);
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    @Override
    public DivisionExpression removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    public DivisionExpression setChildren(final List<Expression> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    DivisionExpression replace1(final int index, final Expression left, final Expression right) {
        return new DivisionExpression(index, left, right);
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
    String applyText0(final String left, final String right, final ExpressionEvaluationContext context) {
        throw new UnsupportedOperationException(left + SYMBOL + right);
    }

    @Override
    BigDecimal applyBigDecimal0(final BigDecimal left, final BigDecimal right, final ExpressionEvaluationContext context) {
        return left.divide(right, context.mathContext());
    }

    @Override
    BigInteger applyBigInteger0(final BigInteger left, final BigInteger right, final ExpressionEvaluationContext context) {
        return left.divide(right);
    }

    @Override
    double applyDouble0(final double left, final double right, final ExpressionEvaluationContext context) {
        return left / right;
    }

    @Override
    long applyLong0(final long left, final long right, final ExpressionEvaluationContext context) {
        return left / right;
    }

    // Object .........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof DivisionExpression;
    }

    @Override
    void appendSymbol(final StringBuilder b) {
        b.append(SYMBOL);
    }
}
