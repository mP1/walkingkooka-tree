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
import java.util.Objects;

/**
 * A {@link ExpressionNumber} that holds and performs all operations on {@link BigDecimal values}.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
final class ExpressionNumberBigDecimal extends ExpressionNumber {

    private static final long serialVersionUID = 0L;

    static ExpressionNumberBigDecimal withBigDecimal(final BigDecimal value,
                                                     final ExpressionNumberContext context) {
        Objects.requireNonNull(value, "value");
        checkContext(context);
        return new ExpressionNumberBigDecimal(value, context);
    }

    private ExpressionNumberBigDecimal(final BigDecimal value, final ExpressionNumberContext context) {
        super(context);
        this.value = value;
    }

    @Override
    ExpressionNumber replaceContext(final ExpressionNumberContext context) {
        return new ExpressionNumberBigDecimal(this.value, context);
    }

    // abs..............................................................................................................

    @Override
    public ExpressionNumber abs() {
        return this.setValue(this.value.abs(this.mathContext()));
    }

    // neg..............................................................................................................

    @Override
    public ExpressionNumber negate() {
        return this.setValue(this.value.negate(this.mathContext()));
    }

    // not..............................................................................................................

    @Override
    public ExpressionNumber not() {
        return this.setValue(this.bigInteger().not());
    }

    // add..............................................................................................................

    @Override
    ExpressionNumber add0(final ExpressionNumber value) {
        return value.add1(this);
    }

    @Override
    ExpressionNumber add1(final ExpressionNumberBigDecimal value) {
        return this.setValue(this.value.add(value.value, value.mathContext()));
    }

    @Override
    ExpressionNumber add1(final ExpressionNumberDouble value) {
        return this.setValue(value.bigDecimal().add(this.value, value.mathContext()));
    }

    // divide..............................................................................................................

    @Override
    ExpressionNumber divide0(final ExpressionNumber value) {
        return value.divide1(this);
    }

    @Override
    ExpressionNumber divide1(final ExpressionNumberBigDecimal value) {
        return this.setValue(this.value.divide(value.value, value.mathContext()));
    }

    @Override
    ExpressionNumber divide1(final ExpressionNumberDouble value) {
        return this.setValue(this.value.divide(value.bigDecimal(), this.mathContext()));
    }

// modulo..............................................................................................................

    @Override
    ExpressionNumber modulo0(final ExpressionNumber value) {
        return value.modulo1(this);
    }

    @Override
    ExpressionNumber modulo1(final ExpressionNumberBigDecimal value) {
        return this.setValue(this.value.remainder(value.value, this.mathContext()));
    }

    @Override
    ExpressionNumber modulo1(final ExpressionNumberDouble value) {
        return this.setValue(this.value.remainder(this.bigDecimal(), this.mathContext()));
    }

    // multiply..............................................................................................................

    @Override
    ExpressionNumber multiply0(final ExpressionNumber value) {
        return value.multiply1(this);
    }

    @Override
    ExpressionNumber multiply1(final ExpressionNumberBigDecimal value) {
        return this.setValue(this.value.multiply(value.value, this.mathContext()));
    }

    @Override
    ExpressionNumber multiply1(final ExpressionNumberDouble value) {
        return this.setValue(this.value.multiply(value.bigDecimal(), this.mathContext()));
    }

    // power..............................................................................................................

    @Override
    ExpressionNumber power0(final ExpressionNumber value) {
        return value.power1(this);
    }

    @Override
    ExpressionNumber power1(final ExpressionNumberBigDecimal value) {
        return this.setValue(BigDecimalMath.pow(this.value, value.value, this.mathContext()));
    }

    @Override
    ExpressionNumber power1(final ExpressionNumberDouble value) {
        return this.setValue(BigDecimalMath.pow(this.value, value.bigDecimal(), this.mathContext()));
    }

    // subtract..............................................................................................................

    @Override
    ExpressionNumber subtract0(final ExpressionNumber value) {
        return value.subtract1(this);
    }

    @Override
    ExpressionNumber subtract1(final ExpressionNumberBigDecimal value) {
        return this.setValue(this.value.subtract(value.value, this.mathContext()));
    }

    @Override
    ExpressionNumber subtract1(final ExpressionNumberDouble value) {
        return this.setValue(this.value.subtract(value.bigDecimal(), this.mathContext()));
    }

    @Override
    int compare0(final ExpressionNumber value) {
        return value.compare1(this);
    }

    @Override
    int compare1(final ExpressionNumberBigDecimal value) {
        return this.value.compareTo(value.value);
    }

    @Override
    int compare1(final ExpressionNumberDouble value) {
        return this.value.compareTo(value.bigDecimal());
    }

    // and..............................................................................................................

    @Override
    ExpressionNumber and0(final ExpressionNumber value) {
        return value.and1(this);
    }

    @Override
    ExpressionNumber and1(final ExpressionNumberBigDecimal value) {
        return this.setValue(this.bigInteger().and(value.bigInteger()));
    }

    @Override
    ExpressionNumber and1(final ExpressionNumberDouble value) {
        return this.setValue(value.bigInteger().and(this.value.toBigInteger()));
    }

    // or..............................................................................................................

    @Override
    ExpressionNumber or0(final ExpressionNumber value) {
        return value.or1(this);
    }

    @Override
    ExpressionNumber or1(final ExpressionNumberBigDecimal value) {
        return this.setValue(this.bigInteger().or(value.bigInteger()));
    }

    @Override
    ExpressionNumber or1(final ExpressionNumberDouble value) {
        return this.setValue(value.bigInteger().or(this.value.toBigInteger()));
    }

    // xor..............................................................................................................

    @Override
    ExpressionNumber xor0(final ExpressionNumber value) {
        return value.xor1(this);
    }

    @Override
    ExpressionNumber xor1(final ExpressionNumberBigDecimal value) {
        return this.setValue(this.bigInteger().xor(value.bigInteger()));
    }

    @Override
    ExpressionNumber xor1(final ExpressionNumberDouble value) {
        return this.setValue(value.bigInteger().xor(this.value.toBigInteger()));
    }

    // toXXX............................................................................................................

    @Override
    public byte byteValue() {
        return this.value.byteValueExact();
    }

    @Override
    public short shortValue() {
        return this.value.shortValueExact();
    }

    @Override
    public int intValue() {
        return this.value.intValueExact();
    }

    @Override
    public long longValue() {
        return this.value.longValueExact();
    }

    @Override
    public float floatValue() {
        return this.value.floatValue();
    }

    @Override
    public double doubleValue() {
        return this.value.doubleValue();
    }

    @Override
    public BigInteger bigInteger() {
        return this.value.toBigIntegerExact();
    }

    @Override
    public BigDecimal bigDecimal() {
        return this.value;
    }

    // value............................................................................................................

    /**
     * Would be setter returns a new {@link ExpressionNumberBigDecimal} if the value is different.
     */
    private ExpressionNumber setValue(final BigInteger value) {
        return this.setValue(new BigDecimal(value));
    }

    /**
     * Would be setter returns a new {@link ExpressionNumberBigDecimal} if the value is different.
     */
    ExpressionNumber setValue(final BigDecimal value) {
        return this.equalsValue(value) ?
                this :
                new ExpressionNumberBigDecimal(value, this.context);
    }

    final BigDecimal value;

    // Object............................................................................................................

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionNumberBigDecimal;
    }

    @Override
    boolean equals0(final ExpressionNumber other) {
        return this.equals1((ExpressionNumberBigDecimal) other);
    }

    private boolean equals1(final ExpressionNumberBigDecimal other) {
        return this.equalsValue(other.value);
    }

    private boolean equalsValue(final BigDecimal value) {
        return ComparisonRelation.EQ.test(this.value.compareTo(value));
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    // Comparable.......................................................................................................

    int compareTo0(final ExpressionNumber other) {
        return other.compareTo1(this);
    }

    @Override
    int compareTo1(final ExpressionNumberBigDecimal other) {
        return this.value.compareTo(other.value);
    }

    @Override
    int compareTo1(final ExpressionNumberDouble other) {
        return this.value.compareTo(other.bigDecimal());
    }
}
