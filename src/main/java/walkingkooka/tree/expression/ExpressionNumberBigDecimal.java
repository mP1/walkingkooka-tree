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
import java.math.RoundingMode;
import java.util.Objects;

/**
 * A {@link ExpressionNumber} that holds and performs all operations on {@link BigDecimal values}.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
final class ExpressionNumberBigDecimal extends ExpressionNumber {

    private static final long serialVersionUID = 0L;

    static ExpressionNumberBigDecimal withBigDecimal(final BigDecimal value) {
        Objects.requireNonNull(value, "value");
        return new ExpressionNumberBigDecimal(value);
    }

    private ExpressionNumberBigDecimal(final BigDecimal value) {
        super();
        this.value = value;
    }

    Object value() {
        return this.value;
    }

    // abs..............................................................................................................

    @Override
    public ExpressionNumber abs(final ExpressionNumberContext context) {
        return this.setValue(this.value.abs(context.mathContext()));
    }

    // ceil..............................................................................................................

    @Override
    public ExpressionNumber ceil(final ExpressionNumberContext context) {
        return this.setValue(this.value.setScale(0, RoundingMode.CEILING));
    }

    // floor..............................................................................................................

    @Override
    public ExpressionNumber floor(final ExpressionNumberContext context) {
        return this.setValue(this.value.setScale(0, RoundingMode.FLOOR));
    }

    // neg..............................................................................................................

    @Override
    public ExpressionNumber negate(final ExpressionNumberContext context) {
        return this.setValue(this.value.negate(context.mathContext()));
    }

    // not..............................................................................................................

    @Override
    public ExpressionNumber not() {
        return this.setValue(this.bigInteger().not());
    }

    // round............................................................................................................

    @Override
    public ExpressionNumber round(final ExpressionNumberContext context) {
        return this.setValue(this.value.setScale(0, RoundingMode.HALF_UP));
    }

    // add..............................................................................................................

    @Override
    ExpressionNumber add0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return value.add1(this, context);
    }

    @Override
    ExpressionNumber add1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context) {
        return this.setValue(this.value.add(value.value, context.mathContext()));
    }

    @Override
    ExpressionNumber add1(final ExpressionNumberDouble value, final ExpressionNumberContext context) {
        return this.setValue(value.bigDecimal().add(this.value, context.mathContext()));
    }

    // divide..............................................................................................................

    @Override
    ExpressionNumber divide0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return value.divide1(this, context);
    }

    @Override
    ExpressionNumber divide1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context) {
        return this.setValue(this.value.divide(value.value, context.mathContext()));
    }

    @Override
    ExpressionNumber divide1(final ExpressionNumberDouble value, final ExpressionNumberContext context) {
        return this.setValue(this.value.divide(value.bigDecimal(), context.mathContext()));
    }

// modulo..............................................................................................................

    @Override
    ExpressionNumber modulo0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return value.modulo1(this, context);
    }

    @Override
    ExpressionNumber modulo1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context) {
        return this.setValue(this.value.remainder(value.value, context.mathContext()));
    }

    @Override
    ExpressionNumber modulo1(final ExpressionNumberDouble value, final ExpressionNumberContext context) {
        return this.setValue(this.value.remainder(this.bigDecimal(), context.mathContext()));
    }

    // multiply..............................................................................................................

    @Override
    ExpressionNumber multiply0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return value.multiply1(this, context);
    }

    @Override
    ExpressionNumber multiply1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context) {
        return this.setValue(this.value.multiply(value.value, context.mathContext()));
    }

    @Override
    ExpressionNumber multiply1(final ExpressionNumberDouble value, final ExpressionNumberContext context) {
        return this.setValue(this.value.multiply(value.bigDecimal(), context.mathContext()));
    }

    // power..............................................................................................................

    @Override
    ExpressionNumber power0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return value.power1(this, context);
    }

    @Override
    ExpressionNumber power1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context) {
        return this.setValue(BigDecimalMath.pow(this.value, value.value, context.mathContext()));
    }

    @Override
    ExpressionNumber power1(final ExpressionNumberDouble value, final ExpressionNumberContext context) {
        return this.setValue(BigDecimalMath.pow(this.value, value.bigDecimal(), context.mathContext()));
    }

    // subtract..............................................................................................................

    @Override
    ExpressionNumber subtract0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return value.subtract1(this, context);
    }

    @Override
    ExpressionNumber subtract1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context) {
        return this.setValue(this.value.subtract(value.value, context.mathContext()));
    }

    @Override
    ExpressionNumber subtract1(final ExpressionNumberDouble value, final ExpressionNumberContext context) {
        return this.setValue(this.value.subtract(value.bigDecimal(), context.mathContext()));
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
                new ExpressionNumberBigDecimal(value);
    }

    final BigDecimal value;

    // Object............................................................................................................

    @Override
    public int hashCode() {
        return value.stripTrailingZeros().hashCode();
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionNumberBigDecimal;
    }

    private boolean equalsValue(final BigDecimal value) {
        return ComparisonRelation.EQ.test(this.value.compareTo(value));
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
