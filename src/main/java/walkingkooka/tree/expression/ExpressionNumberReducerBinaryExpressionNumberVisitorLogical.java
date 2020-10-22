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

/**
 * A {@link BinaryExpressionNumberVisitor} that attempts to convert/widen values to either {@link Long} or {@link BigInteger},
 * leaving only two overloads to be implemented.
 */
abstract class ExpressionNumberReducerBinaryExpressionNumberVisitorLogical extends ExpressionNumberReducerBinaryExpressionNumberVisitor {

    ExpressionNumberReducerBinaryExpressionNumberVisitorLogical(final ExpressionNumberReducerContext context) {
        super(context);
    }

    @Override
    protected final void visit(final Number left, final Number right) {
        this.accept(this.toBigInteger(left), this.toBigInteger(right));
    }

    // BigDecimal.......................................................................................................

    protected final void visit(final BigDecimal left, final BigDecimal right) {
        this.accept(this.toBigInteger(left), this.toBigInteger(right));
    }

    @Override
    protected final void visit(final BigDecimal left, final BigInteger right) {
        this.accept(this.toBigInteger(left), this.toBigInteger(right));
    }

    @Override
    protected final void visit(final BigDecimal left, final Double right) {
        this.accept(this.toBigInteger(left), this.toBigInteger(right));
    }

    @Override
    protected final void visit(final BigDecimal left, final Long right) {
        this.accept(this.toBigInteger(left), this.toBigInteger(right));
    }

    @Override
    protected final void visit(final BigDecimal left, final Number right) {
        this.accept(this.toBigInteger(left), this.toBigInteger(right));
    }

    // BigInteger.......................................................................................................

    @Override
    protected final void visit(final BigInteger left, final BigDecimal right) {
        this.accept(left, this.toBigInteger(right));
    }

    @Override
    abstract protected void visit(final BigInteger left, final BigInteger right);

    @Override
    protected final void visit(final BigInteger left, final Double right) {
        this.accept(left, this.toBigInteger(right));
    }

    @Override
    protected final void visit(final BigInteger left, final Long right) {
        this.accept(left, this.toBigInteger(right));
    }

    @Override
    protected final void visit(final BigInteger left, final Number right) {
        this.accept(left, this.toBigInteger(right));
    }

    // Double...........................................................................................................

    @Override
    protected final void visit(final Double left, final BigDecimal right) {
        this.accept(this.toBigInteger(left), this.toBigInteger(right));
    }

    @Override
    protected final void visit(final Double left, final BigInteger right) {
        this.accept(this.toBigInteger(left), this.toBigInteger(right));
    }

    @Override
    protected final void visit(final Double left, final Double right) {
        this.accept(this.toBigInteger(left), this.toBigInteger(right));
    }

    @Override
    protected final void visit(final Double left, final Long right) {
        this.accept(this.toBigInteger(left), this.toBigInteger(right));
    }

    @Override
    protected final void visit(final Double left, final Number right) {
        this.accept(this.toBigInteger(left), this.toBigInteger(right));
    }

    // Long.............................................................................................................

    @Override
    protected final void visit(final Long left, final BigDecimal right) {
        this.accept(this.toBigInteger(left), this.toBigInteger(right));
    }

    @Override
    protected final void visit(final Long left, final BigInteger right) {
        this.accept(this.toBigInteger(left), right);
    }

    @Override
    protected final void visit(final Long left, final Double right) {
        this.accept(this.toBigInteger(left), this.toBigInteger(right));
    }

    @Override
    protected abstract void visit(final Long left, final Long right);

    @Override
    protected final void visit(final Long left, final Number right) {
        this.accept(left, this.toLong(right));
    }
}
