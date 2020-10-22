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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * A {@link BinaryExpressionNumberVisitor} that converts both numbers to the same type and leaving four overloads
 * to do actual work.
 */
abstract class ExpressionNumberReducerBinaryExpressionNumberVisitor extends BinaryExpressionNumberVisitor {

    static Number add(final Number left,
                      final Number right,
                      final ExpressionNumberReducerContext context) {
        return ExpressionNumberReducerBinaryExpressionNumberVisitorAdd.compute(left, right, context);
    }

    static Number divide(final Number left,
                         final Number right,
                         final ExpressionNumberReducerContext context) {
        return ExpressionNumberReducerBinaryExpressionNumberVisitorDivide.compute(left, right, context);
    }

    static Number multiply(final Number left,
                           final Number right,
                           final ExpressionNumberReducerContext context) {
        return ExpressionNumberReducerBinaryExpressionNumberVisitorMultiply.compute(left, right, context);
    }

    static Number subtract(final Number left,
                           final Number right,
                           final ExpressionNumberReducerContext context) {
        return ExpressionNumberReducerBinaryExpressionNumberVisitorSubtract.compute(left, right, context);
    }

    ExpressionNumberReducerBinaryExpressionNumberVisitor(final ExpressionNumberReducerContext context) {
        super();
        this.context = context;
    }

    @Override
    protected final void visit(final Number left, final Number right) {
        this.accept(this.toBigDecimal(left), this.toBigDecimal(right));
    }

    // BigDecimal.......................................................................................................

    protected abstract void visit(final BigDecimal left, final BigDecimal right);

    @Override
    protected final void visit(final BigDecimal left, final BigInteger right) {
        this.accept(left, this.toBigDecimal(right));
    }

    @Override
    protected final void visit(final BigDecimal left, final Double right) {
        this.accept(left, this.toBigDecimal(right));
    }

    @Override
    protected final void visit(final BigDecimal left, final Long right) {
        this.accept(left, this.toBigDecimal(right));
    }

    @Override
    protected final void visit(final BigDecimal left, final Number right) {
        this.accept(left, this.toBigDecimal(right));
    }

    // BigInteger.......................................................................................................

    @Override
    protected final void visit(final BigInteger left, final BigDecimal right) {
        this.accept(this.toBigDecimal(left), right);
    }

    @Override
    abstract protected void visit(final BigInteger left, final BigInteger right);

    @Override
    protected final void visit(final BigInteger left, final Double right) {
        this.accept(this.toBigDecimal(left), this.toBigDecimal(right));
    }

    @Override
    protected final void visit(final BigInteger left, final Long right) {
        this.accept(left, this.toBigInteger(right));
    }

    @Override
    protected final void visit(final BigInteger left, final Number right) {
        this.accept(this.toBigDecimal(left), this.toBigDecimal(right));
    }

    // Double...........................................................................................................

    @Override
    protected final void visit(final Double left, final BigDecimal right) {
        this.accept(this.toBigDecimal(left), right);
    }

    @Override
    protected final void visit(final Double left, final BigInteger right) {
        this.accept(this.toBigDecimal(left), this.toBigDecimal(right));
    }

    @Override
    protected abstract void visit(final Double left, final Double right);

    @Override
    protected final void visit(final Double left, final Long right) {
        this.accept(left, this.toDouble(right));
    }

    @Override
    protected final void visit(final Double left, final Number right) {
        this.accept(this.toBigDecimal(left), this.toBigDecimal(right));
    }

    // Long.............................................................................................................

    @Override
    protected final void visit(final Long left, final BigDecimal right) {
        this.accept(this.toBigDecimal(left), right);
    }

    @Override
    protected final void visit(final Long left, final BigInteger right) {
        this.accept(this.toBigInteger(left), right);
    }

    @Override
    protected final void visit(final Long left, final Double right) {
        this.accept(this.toDouble(left), right);
    }

    @Override
    protected abstract void visit(final Long left, final Long right);

    @Override
    protected final void visit(final Long left, final Number right) {
        this.accept(this.toBigDecimal(left), this.toBigDecimal(right));
    }

    // helpers..........................................................................................................

    private BigDecimal toBigDecimal(final Object value) {
        return this.convertOrFail(value, BigDecimal.class);
    }

    private BigInteger toBigInteger(final Object value) {
        return this.convertOrFail(value, BigInteger.class);
    }

    private Double toDouble(final Object value) {
        return this.convertOrFail(value, Double.class);
    }

//    private Long toLong(final Long value) {
//        return this.convertOrFail(value, Long.class);
//    }

    private <T> T convertOrFail(final Object value,
                                final Class<T> target) {
        return this.context.convertOrFail(value, target);
    }

    /**
     * Used to convert and supply the {@link MathContext}
     */
    final ExpressionNumberReducerContext context;

    /**
     * The result will be stored here.
     */
    Number result;

    public abstract String toString();
}
