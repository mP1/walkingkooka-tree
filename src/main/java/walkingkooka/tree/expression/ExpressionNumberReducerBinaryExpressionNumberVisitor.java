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
 * A {@link BinaryExpressionNumberVisitor} that is the base class for several visitors.
 */
abstract class ExpressionNumberReducerBinaryExpressionNumberVisitor extends BinaryExpressionNumberVisitor {

    static Number add(final Number left,
                      final Number right,
                      final ExpressionNumberReducerContext context) {
        return ExpressionNumberReducerBinaryExpressionNumberVisitorAdd.compute(left, right, context);
    }

    static Number and(final Number left,
                      final Number right,
                      final ExpressionNumberReducerContext context) {
        return ExpressionNumberReducerBinaryExpressionNumberVisitorAnd.compute(left, right, context);
    }

    static Number divide(final Number left,
                         final Number right,
                         final ExpressionNumberReducerContext context) {
        return ExpressionNumberReducerBinaryExpressionNumberVisitorDivide.compute(left, right, context);
    }

    static Number modulo(final Number left,
                         final Number right,
                         final ExpressionNumberReducerContext context) {
        return ExpressionNumberReducerBinaryExpressionNumberVisitorModulo.compute(left, right, context);
    }

    static Number multiply(final Number left,
                           final Number right,
                           final ExpressionNumberReducerContext context) {
        return ExpressionNumberReducerBinaryExpressionNumberVisitorMultiply.compute(left, right, context);
    }

    static Number or(final Number left,
                     final Number right,
                     final ExpressionNumberReducerContext context) {
        return ExpressionNumberReducerBinaryExpressionNumberVisitorOr.compute(left, right, context);
    }

    static Number power(final Number left,
                        final Number right,
                        final ExpressionNumberReducerContext context) {
        return ExpressionNumberReducerBinaryExpressionNumberVisitorPower.compute(left, right, context);
    }

    static Number subtract(final Number left,
                           final Number right,
                           final ExpressionNumberReducerContext context) {
        return ExpressionNumberReducerBinaryExpressionNumberVisitorSubtract.compute(left, right, context);
    }

    static Number xor(final Number left,
                      final Number right,
                      final ExpressionNumberReducerContext context) {
        return ExpressionNumberReducerBinaryExpressionNumberVisitorXor.compute(left, right, context);
    }

    ExpressionNumberReducerBinaryExpressionNumberVisitor(final ExpressionNumberReducerContext context) {
        super();
        this.context = context;
    }

    @Override
    protected abstract void visit(final Number left, final Number right);

    // BigDecimal.......................................................................................................

    protected abstract void visit(final BigDecimal left, final BigDecimal right);

    @Override
    protected abstract void visit(final BigDecimal left, final BigInteger right);

    @Override
    protected abstract void visit(final BigDecimal left, final Double right);

    @Override
    protected abstract void visit(final BigDecimal left, final Long right);

    @Override
    protected abstract void visit(final BigDecimal left, final Number right);

    // BigInteger.......................................................................................................

    protected abstract void visit(final BigInteger left, final BigDecimal right);

    @Override
    protected abstract void visit(final BigInteger left, final BigInteger right);

    @Override
    protected abstract void visit(final BigInteger left, final Double right);

    @Override
    protected abstract void visit(final BigInteger left, final Long right);

    @Override
    protected abstract void visit(final BigInteger left, final Number right);

    // Double...........................................................................................................

    protected abstract void visit(final Double left, final BigDecimal right);

    @Override
    protected abstract void visit(final Double left, final BigInteger right);

    @Override
    protected abstract void visit(final Double left, final Double right);

    @Override
    protected abstract void visit(final Double left, final Long right);

    @Override
    protected abstract void visit(final Double left, final Number right);

    // Long.............................................................................................................

    protected abstract void visit(final Long left, final BigDecimal right);

    @Override
    protected abstract void visit(final Long left, final BigInteger right);

    @Override
    protected abstract void visit(final Long left, final Double right);

    @Override
    protected abstract void visit(final Long left, final Long right);

    @Override
    protected abstract void visit(final Long left, final Number right);

    // helpers..........................................................................................................

    final BigDecimal toBigDecimal(final Object value) {
        return this.convertOrFail(value, BigDecimal.class);
    }

    final BigInteger toBigInteger(final Object value) {
        return this.convertOrFail(value, BigInteger.class);
    }

    final Double toDouble(final Object value) {
        return this.convertOrFail(value, Double.class);
    }

    final Long toLong(final Object value) {
        return this.convertOrFail(value, Long.class);
    }

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
