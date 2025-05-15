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

import walkingkooka.math.Maths;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * A {@link ExpressionNumber} that holds and performs all operations on {@link double values}.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
final class ExpressionNumberDouble extends ExpressionNumber {

    private static final long serialVersionUID = 0L;

    static ExpressionNumberDouble withDouble(final double value) {
        return new ExpressionNumberDouble(value);
    }

    private ExpressionNumberDouble(final double value) {
        super();
        this.value = value;
    }

    @Override
    public Double value() {
        return this.value;
    }

    @Override
    public ExpressionNumberKind kind() {
        return ExpressionNumberKind.DOUBLE;
    }

    @Override
    ExpressionNumberBigDecimal setKindBigDecimal() {
        return ExpressionNumberBigDecimal.withBigDecimal(this.bigDecimal());
    }

    @Override
    ExpressionNumberDouble setKindDouble() {
        return this;
    }

    @Override
    public ExpressionNumberSign sign() {
        return ExpressionNumberSign.pick(this.value);
    }

    // map..............................................................................................................

    @Override
    public ExpressionNumber map(final ExpressionNumberFunction function,
                                final MathContext context) {
        return this.setValue(
            function.mapDouble(this.doubleValue())
        );
    }

    // abs..............................................................................................................

    @Override
    public ExpressionNumber abs(final ExpressionNumberContext context) {
        return this.setValue(Math.abs(this.value));
    }

    // ceil..............................................................................................................

    @Override
    public ExpressionNumber ceil(final ExpressionNumberContext context) {
        return this.setValue(Math.ceil(this.value));
    }

    // exp..............................................................................................................

    @Override
    public ExpressionNumber exp(final ExpressionNumberContext context) {
        return this.setValue(
            Math.exp(this.value)
        );
    }

    // floor..............................................................................................................

    @Override
    public ExpressionNumber floor(final ExpressionNumberContext context) {
        return this.setValue(Math.floor(this.value));
    }

    // ln...............................................................................................................

    @Override
    public ExpressionNumber ln(final ExpressionNumberContext context) {
        return this.setValueAndCheckPositive(
            Math.log(this.value)
        );
    }

    // log10............................................................................................................

    @Override
    public ExpressionNumber log10(final ExpressionNumberContext context) {
        return this.setValueAndCheckPositive(
            Math.log10(this.value)
        );
    }

    private ExpressionNumber setValueAndCheckPositive(final double value) {

        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Number " + this.value + " < 0");
        }
        return this.setValue(value);
    }

    // neg..............................................................................................................

    @Override
    public ExpressionNumber negate(final ExpressionNumberContext context) {
        return this.setValue(-this.value);
    }

    // not..............................................................................................................

    @Override
    public ExpressionNumber not() {
        return this.setValue(~this.longValue());
    }

    // round...........................................................................................................

    @Override
    public ExpressionNumber round(final ExpressionNumberContext context) {
        return this.setValue(
            Maths.round(
                this.value,
                context.mathContext().getRoundingMode()
            )
        );
    }

    // add..............................................................................................................

    @Override
    ExpressionNumber add0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return this.setValue(this.value + value.doubleValue());
    }

    // divide..............................................................................................................

    @Override
    ExpressionNumber divide0(final ExpressionNumber value,
                             final ExpressionNumberContext context) {
        final double result = this.value / value.doubleValue();

        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new ArithmeticException("Division by zero");
        }
        return this.setValue(result);
    }

    // max..............................................................................................................

    @Override
    ExpressionNumber max0(final ExpressionNumber value) {
        return this.setValue(Math.max(this.value, value.doubleValue()));
    }

    // min..............................................................................................................

    @Override
    ExpressionNumber min0(final ExpressionNumber value) {
        return this.setValue(Math.min(this.value, value.doubleValue()));
    }

    // modulo..............................................................................................................

    @Override
    ExpressionNumber modulo0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return this.setValue(this.value % value.doubleValue());
    }

    // multiply..............................................................................................................

    @Override
    ExpressionNumber multiply0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return this.setValue(this.value * value.doubleValue());
    }

    // power..............................................................................................................

    @Override
    ExpressionNumber power0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return this.setValue(Math.pow(this.value, value.doubleValue()));
    }

    // sqrt..............................................................................................................

    @Override
    public ExpressionNumber sqrt(final ExpressionNumberContext context) {
        final double sqrt = Math.sqrt(this.value);
        if (Double.isNaN(sqrt) || Double.isInfinite(sqrt)) {
            throw new ExpressionEvaluationException("Invalid value");
        }

        return this.setValue(
            sqrt
        );
    }

    // subtract..............................................................................................................

    @Override
    ExpressionNumber subtract0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return this.setValue(this.value - value.doubleValue());
    }

    // and..............................................................................................................

    @Override
    ExpressionNumber and0(final ExpressionNumber value) {
        return this.setValue(this.longValue() & value.longValue());
    }

    // andNot...........................................................................................................

    @Override
    ExpressionNumber andNot0(final ExpressionNumber value) {
        return this.setValue(
            this.longValue() & ~value.longValue()
        );
    }

    // or..............................................................................................................

    @Override
    ExpressionNumber or0(final ExpressionNumber value) {
        return this.setValue(this.longValue() | value.longValue());
    }

    // xor..............................................................................................................

    @Override
    ExpressionNumber xor0(final ExpressionNumber value) {
        return this.setValue(this.longValue() ^ value.longValue());
    }

    // toXXX............................................................................................................

    @Override
    public byte byteValue() {
        return (byte) this.value;
    }

    @Override
    public byte byteValueExact() {
        final byte value = (byte) this.value;
        this.failIfDifferent(value, "byteValue");
        return value;
    }

    @Override
    public short shortValue() {
        return (short) this.value;
    }

    @Override
    public short shortValueExact() {
        final short value = (short) this.value;
        this.failIfDifferent(value, "shortValue");
        return value;
    }

    @Override
    public int intValue() {
        return (int) this.value;
    }

    @Override
    public int intValueExact() {
        final int value = (int) this.value;
        this.failIfDifferent(value, "intValue");
        return value;
    }

    @Override
    public long longValue() {
        return (long) this.value;
    }

    @Override
    public long longValueExact() {
        final long value = (long) this.value;
        this.failIfDifferent(value, "longValue");
        return value;
    }

    private void failIfDifferent(final double value,
                                 final String operation) {
        if (this.value != value) {
            throw new ArithmeticException(operation + " precision loss " + this.value);
        }
    }

    @Override
    public float floatValue() {
        return (float) this.value;
    }

    @Override
    public double doubleValue() {
        return this.value;
    }

    @Override
    public BigInteger bigInteger() {
        return this.bigDecimal()
            .toBigInteger();
    }

    @Override
    public BigInteger bigIntegerExact() {
        return this.bigDecimal()
            .toBigIntegerExact();
    }

    @Override
    public BigDecimal bigDecimal() {
        return BigDecimal.valueOf(this.value);
    }

    // value............................................................................................................

    /**
     * Would be setter returns a new {@link ExpressionNumberDouble} if the value is different.
     */
    private ExpressionNumber setValue(final double value) {
        return this.value == value ?
            this :
            new ExpressionNumberDouble(value);
    }

    private final double value;

    // Object............................................................................................................

    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionNumberDouble;
    }

    // Comparable.......................................................................................................

    @Override
    public int compareTo(final ExpressionNumber other) {
        return Double.compare(this.value, other.doubleValue());
    }
}
