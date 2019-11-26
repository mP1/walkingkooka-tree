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

import walkingkooka.NeverError;
import walkingkooka.visit.Visiting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

/**
 * A negative expression.
 */
public final class NegativeExpression extends UnaryExpression {

    public final static FunctionExpressionName NAME = FunctionExpressionName.fromClass(NegativeExpression.class);

    public final static String SYMBOL = "-";

    static NegativeExpression with(final Expression value) {
        Objects.requireNonNull(value, "value");
        return new NegativeExpression(NO_INDEX, value);
    }

    private NegativeExpression(final int index, final Expression value) {
        super(index, value);
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    @Override
    public NegativeExpression removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    public NegativeExpression setChildren(final List<Expression> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    NegativeExpression replace1(final int index, final Expression expression) {
        return new NegativeExpression(index, expression);
    }

    // visitor..........................................................................................................

    @Override
    public void accept(final ExpressionVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // evaluation .....................................................................................................

    @Override
    public BigDecimal toBigDecimal(final ExpressionEvaluationContext context) {
        return this.value().toBigDecimal(context).negate(context.mathContext());
    }

    @Override
    public BigInteger toBigInteger(final ExpressionEvaluationContext context) {
        return this.value().toBigInteger(context).negate();
    }

    @Override
    public boolean toBoolean(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.toNumber(context), Boolean.class);
    }

    @Override
    public double toDouble(final ExpressionEvaluationContext context) {
        return -this.value().toDouble(context);
    }

    @Override
    public long toLong(final ExpressionEvaluationContext context) {
        return -this.value().toLong(context);
    }

    @Override
    public Number toNumber(final ExpressionEvaluationContext context) {
        final Number number = this.value().toNumber(context);
        return number instanceof BigDecimal ?
                this.applyBigDecimal((BigDecimal) number, context) :
                number instanceof BigInteger ?
                        this.applyBigInteger((BigInteger) number) :
                        number instanceof Double ?
                                this.applyDouble((Double) number) :
                                number instanceof Long ?
                                        this.applyLong((Long) number) :
                                        failToNumber(number);
    }

    private BigDecimal applyBigDecimal(final BigDecimal bigDecimal, final ExpressionEvaluationContext context) {
        return bigDecimal.negate(context.mathContext());
    }

    private BigInteger applyBigInteger(final BigInteger bigInteger) {
        return bigInteger.negate();
    }

    private Double applyDouble(final Double doubleValue) {
        return -doubleValue;
    }

    private Long applyLong(final Long longValue) {
        return -longValue;
    }

    private Number failToNumber(final Number value) {
        return NeverError.unhandledCase(value);
    }

    @Override
    public String toString(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.toNumber(context), String.class);
    }

    // Object ....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NegativeExpression;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(SYMBOL);
        this.value().toString0(b);
    }
}
