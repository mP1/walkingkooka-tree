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

import ch.obermuhlner.math.big.BigDecimalMath;
import walkingkooka.visit.Visiting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * A power expression.
 */
public final class PowerExpression extends BinaryArithmeticExpression {

    public final static FunctionExpressionName NAME = FunctionExpressionName.fromClass(PowerExpression.class);

    public final static String SYMBOL = "^^";

    static PowerExpression with(final Expression left, final Expression right) {
        check(left, right);
        return new PowerExpression(NO_INDEX, left, right);
    }

    private PowerExpression(final int index, final Expression left, final Expression right) {
        super(index, left, right);
    }

    @Override
    public FunctionExpressionName name() {
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
    String applyText0(final String left, final String right, final ExpressionEvaluationContext context) {
        throw new UnsupportedOperationException(left + SYMBOL + right);
    }

    @Override
    BigDecimal applyBigDecimal0(final BigDecimal left, final BigDecimal right, final ExpressionEvaluationContext context) {
        return BigDecimalMath.pow(left, right, context.mathContext());
    }

    @Override
    BigInteger applyBigInteger0(final BigInteger left, final BigInteger right, final ExpressionEvaluationContext context) {
        return this.applyBigDecimal0(new BigDecimal(left), new BigDecimal(right), context).toBigIntegerExact();
    }

    @Override
    double applyDouble0(final double left, final double right, final ExpressionEvaluationContext context) {
        return Math.pow(left, right);
    }

    @Override
    long applyLong0(final long left, final long right, final ExpressionEvaluationContext context) {
        final double doubleValue = this.applyDouble0(left, right, context);
        final long longValue = (long) doubleValue;
        if (doubleValue != longValue) {
            throw new ExpressionEvaluationException("Precision loss " + left + "^^" + right);
        }

        return longValue;
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
