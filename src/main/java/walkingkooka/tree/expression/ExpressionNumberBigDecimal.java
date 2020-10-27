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

    @Override
    public ExpressionNumberKind kind() {
        return ExpressionNumberKind.BIG_DECIMAL;
    }

    @Override
    ExpressionNumberBigDecimal setKindBigDecimal() {
        return this;
    }

    @Override
    ExpressionNumberDouble setKindDouble() {
        return ExpressionNumberDouble.with(this.doubleValue());
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
        return this.setValue(this.value.add(value.bigDecimal(), context.mathContext()));
    }

    // divide..............................................................................................................

    @Override
    ExpressionNumber divide0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return this.setValue(this.value.divide(value.bigDecimal(), context.mathContext()));
    }

    // min..............................................................................................................

    @Override
    ExpressionNumber min0(final ExpressionNumber value) {
        return this.setValue(this.value.min(value.bigDecimal()));
    }
    
    // modulo..............................................................................................................

    @Override
    ExpressionNumber modulo0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return this.setValue(this.value.remainder(value.bigDecimal(), context.mathContext()));
    }

    // multiply..............................................................................................................

    @Override
    ExpressionNumber multiply0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return this.setValue(this.value.multiply(value.bigDecimal(), context.mathContext()));
    }

    // power..............................................................................................................

    @Override
    ExpressionNumber power0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return this.setValue(BigDecimalMath.pow(this.value, value.bigDecimal(), context.mathContext()));
    }

    // subtract..............................................................................................................

    @Override
    ExpressionNumber subtract0(final ExpressionNumber value, final ExpressionNumberContext context) {
        return this.setValue(this.value.subtract(value.bigDecimal(), context.mathContext()));
    }

    // and..............................................................................................................

    @Override
    ExpressionNumber and0(final ExpressionNumber value) {
        return this.setValue(this.value.toBigIntegerExact().and(value.bigInteger()));
    }

    // or..............................................................................................................

    @Override
    ExpressionNumber or0(final ExpressionNumber value) {
        return this.setValue(this.value.toBigIntegerExact().or(value.bigInteger()));
    }

    // xor..............................................................................................................

    @Override
    ExpressionNumber xor0(final ExpressionNumber value) {
        return this.setValue(this.value.toBigIntegerExact().xor(value.bigInteger()));
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

    @Override
    public int compareTo(final ExpressionNumber other) {
        return this.value.compareTo(other.bigDecimal());
    }
}
