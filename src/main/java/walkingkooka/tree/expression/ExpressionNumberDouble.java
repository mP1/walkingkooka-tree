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

import ch.obermuhlner.math.big.BigDecimalMath;
import walkingkooka.compare.ComparisonRelation;

import java.math.BigDecimal;
import java.math.BigInteger;

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

    final Object value() {
        return this.value;
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

    // floor..............................................................................................................

    @Override
    public ExpressionNumber floor(final ExpressionNumberContext context) {
        return this.setValue(Math.floor(this.value));
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

    // add..............................................................................................................

    @Override
    ExpressionNumber add0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return value.add1(this, context);
    }

    @Override
    ExpressionNumber add1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context) {
        return value.add1(this, context);
    }

    @Override
    ExpressionNumber add1(final ExpressionNumberDouble value, final ExpressionNumberContext context) {
        return this.setValue(this.value + value.value);
    }

    // divide..............................................................................................................

    @Override
    ExpressionNumber divide0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return value.divide1(this, context);
    }

    @Override
    ExpressionNumber divide1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context) {
        return value.setValue(this.bigDecimal().divide(value.value, context.mathContext()));
    }

    @Override
    ExpressionNumber divide1(final ExpressionNumberDouble value, final ExpressionNumberContext context) {
        final double doubleValue = value.value;
        if (doubleValue == 0) {
            throw new ArithmeticException();
        }
        return this.setValue(this.value / doubleValue);
    }

    // modulo..............................................................................................................

    @Override
    ExpressionNumber modulo0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return value.modulo1(this, context);
    }

    @Override
    ExpressionNumber modulo1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context) {
        return value.modulo1(this, context);
    }

    @Override
    ExpressionNumber modulo1(final ExpressionNumberDouble value, final ExpressionNumberContext context) {
        return this.setValue(this.value % value.value);
    }

    // multiply..............................................................................................................

    @Override
    ExpressionNumber multiply0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return value.multiply1(this, context);
    }

    @Override
    ExpressionNumber multiply1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context) {
        return value.multiply1(this, context);
    }

    @Override
    ExpressionNumber multiply1(final ExpressionNumberDouble value, final ExpressionNumberContext context) {
        return this.setValue(this.value * value.value);
    }

    // power..............................................................................................................

    @Override
    ExpressionNumber power0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return value.power1(this, context);
    }

    @Override
    ExpressionNumber power1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context) {
        return value.setValue(BigDecimalMath.pow(this.bigDecimal(), value.value, context.mathContext()));
    }

    @Override
    ExpressionNumber power1(final ExpressionNumberDouble value, final ExpressionNumberContext context) {
        return this.setValue(Math.pow(this.value, value.value));
    }

    // subtract..............................................................................................................

    @Override
    ExpressionNumber subtract0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return value.subtract1(this, context);
    }

    @Override
    ExpressionNumber subtract1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context) {
        return value.setValue(this.bigDecimal().subtract(value.value));
    }

    @Override
    ExpressionNumber subtract1(final ExpressionNumberDouble value, final ExpressionNumberContext context) {
        return this.setValue(this.value - value.value);
    }

    @Override
    int compare0(final ExpressionNumber value) {
        return value.compare1(this);
    }

    @Override
    int compare1(final ExpressionNumberBigDecimal value) {
        return this.bigDecimal().compareTo(value.value);
    }

    @Override
    int compare1(final ExpressionNumberDouble value) {
        return Double.compare(this.value, value.value);
    }

    // and..............................................................................................................

    @Override
    ExpressionNumber and0(final ExpressionNumber value) {
        return value.and1(this);
    }

    @Override
    ExpressionNumber and1(final ExpressionNumberBigDecimal value) {
        return value.and1(this);
    }

    @Override
    ExpressionNumber and1(final ExpressionNumberDouble value) {
        return this.setValue(value.longValue() & (long) this.value);
    }

    // or..............................................................................................................

    @Override
    ExpressionNumber or0(final ExpressionNumber value) {
        return value.or1(this);
    }

    @Override
    ExpressionNumber or1(final ExpressionNumberBigDecimal value) {
        return value.or1(this);
    }

    @Override
    ExpressionNumber or1(final ExpressionNumberDouble value) {
        return this.setValue(value.longValue() | (long) this.value);
    }

    // xor..............................................................................................................

    @Override
    ExpressionNumber xor0(final ExpressionNumber value) {
        return value.xor1(this);
    }

    @Override
    ExpressionNumber xor1(final ExpressionNumberBigDecimal value) {
        return value.xor1(this);
    }

    @Override
    ExpressionNumber xor1(final ExpressionNumberDouble value) {
        return this.setValue(value.longValue() ^ (long) this.value);
    }

    // toXXX............................................................................................................

    @Override
    public byte byteValue() {
        final byte value = (byte) this.value;
        this.failIfDifferent(value, "byteValue");
        return value;
    }

    @Override
    public short shortValue() {
        final short value = (short) this.value;
        this.failIfDifferent(value, "shortValue");
        return value;
    }

    @Override
    public int intValue() {
        final int value = (int) this.value;
        this.failIfDifferent(value, "intValue");
        return value;
    }

    @Override
    public long longValue() {
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
        return this.bigDecimal().toBigIntegerExact();
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

    @Override
    boolean equals0(final ExpressionNumber other) {
        return this.equals1((ExpressionNumberDouble) other);
    }

    private boolean equals1(final ExpressionNumberDouble other) {
        return ComparisonRelation.EQ.test(Double.compare(this.value, other.value));
    }

    // Comparable.......................................................................................................

    int compareTo0(final ExpressionNumber other) {
        return other.compareTo1(this);
    }

    @Override
    int compareTo1(final ExpressionNumberBigDecimal other) {
        return this.bigDecimal().compareTo(other.value);
    }

    @Override
    int compareTo1(final ExpressionNumberDouble other) {
        return Double.compare(this.value, other.value);
    }
}
