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

    @Override
    public ExpressionNumberKind kind() {
        return ExpressionNumberKind.DOUBLE;
    }

    @Override
    ExpressionNumberBigDecimal setKindBigDecimal() {
        return ExpressionNumberBigDecimal.with(this.bigDecimal());
    }

    @Override
    ExpressionNumberDouble setKindDouble() {
        return this;
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

    // round...........................................................................................................

    @Override
    public ExpressionNumber round(final ExpressionNumberContext context) {
        return this.setValue(Math.round(this.value));
    }

    // add..............................................................................................................

    @Override
    ExpressionNumber add0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return this.setValue(this.value + value.doubleValue());
    }

    // divide..............................................................................................................

    @Override
    ExpressionNumber divide0(final ExpressionNumber value, final ExpressionNumberContext context) {
        final double doubleValue = value.doubleValue();
        if (doubleValue == 0) {
            throw new ArithmeticException();
        }
        return this.setValue(this.value / doubleValue);
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

    // Comparable.......................................................................................................

    @Override
    public int compareTo(final ExpressionNumber other) {
        return Double.compare(this.value, other.doubleValue());
    }
}
