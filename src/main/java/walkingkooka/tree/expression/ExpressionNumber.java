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

import walkingkooka.compare.ComparisonRelation;
import walkingkooka.convert.Converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Collection of utilities to assist with converting number values to one of the preferred expression number types of
 * {@link BigDecimal}, {@link BigInteger}, {@link Double} and {@link Long}.
 * The following Number methods will fail if precision is lost.
 * - byteValue
 * - shortValue
 * - intValue
 * - longValue
 */
public abstract class ExpressionNumber extends Number implements Comparable<ExpressionNumber> {

    private static final long serialVersionUID = 0L;

    public static boolean isByteShortIntegerLong(final Object value) {
        return value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long;
    }

    public static boolean isFloatDouble(final Object value) {
        return value instanceof Float || value instanceof Double;
    }

    /**
     * Returns true if the value type is one of the supported or upcastable types.
     */
    public static boolean is(final Object value) {
        return isByteShortIntegerLong(value) ||
                isFloatDouble(value) ||
                value instanceof BigDecimal ||
                value instanceof BigInteger ||
                value instanceof ExpressionNumber;
    }

    public static Class<? extends Number> wider(final Object value) {
        return isByteShortIntegerLong(value) ?
                Long.class :
                isFloatDouble(value) ?
                        Double.class :
                        value instanceof BigInteger ?
                                BigInteger.class :
                                value instanceof BigDecimal ?
                                        BigDecimal.class :
                                        failUp(value);
    }

    private static Class<? extends Number> failUp(final Object value) {
        throw new IllegalArgumentException("value is not a ExpressionNumber");
    }

    /**
     * Accepts any JRE number and wraps that value in a {@link ExpressionNumber}
     */
    public static ExpressionNumber with(final Number value) {
        return ExpressionNumberNumberVisitor.toExpressionNumber(value);
    }

    /**
     * Creates a {@link ExpressionNumber} wrapping the given {@link Double}.
     * Future math operations will operate on doubles.
     */
    public static ExpressionNumber with(final double value) {
        return ExpressionNumberDouble.withDouble(value);
    }

    /**
     * Creates a {@link ExpressionNumber} wrapping the given {@link BigDecimal}.
     * Future math operations will operate on {@link BigDecimal}.
     */
    public static ExpressionNumber with(final BigDecimal value) {
        return ExpressionNumberBigDecimal.withBigDecimal(value);
    }

    /**
     * {@see ExpressionNumberConverterToNumberExpressionNumberBigDecimal.expressionNumberBigDecimal}
     */
    public static Converter toExpressionNumberBigDecimalConverter(final Converter converter) {
        return ExpressionNumberConverterToNumberExpressionNumber.expressionNumberBigDecimal(converter);
    }

    /**
     * {@see ExpressionNumberConverterToNumberExpressionNumber#expressionNumberDouble}
     */
    public static Converter toExpressionNumberDouble(final Converter converter) {
        return ExpressionNumberConverterToNumberExpressionNumber.expressionNumberDouble(converter);
    }

    /**
     * {@see ExpressionNumberConverterExpressionNumber}
     */
    public static Converter expressionNumberNumberConverter(final Converter converter) {
        return ExpressionNumberConverterExpressionNumber.with(converter);
    }

    /**
     * Stop creation
     */
    ExpressionNumber() {
        super();
    }

    public final boolean isBigDecimal() {
        return this instanceof ExpressionNumberBigDecimal;
    }

    public final boolean isDouble() {
        return this instanceof ExpressionNumberDouble;
    }

    abstract Object value();

    // abs..............................................................................................................

    public abstract ExpressionNumber abs(final ExpressionNumberContext context);

    // negate...........................................................................................................

    public abstract ExpressionNumber negate(final ExpressionNumberContext context);

    // not..............................................................................................................

    public abstract ExpressionNumber not();

    // binary operations.................................................................................................

    private static void check(final ExpressionNumber value) {
        Objects.requireNonNull(value, "value");
    }

    // add..............................................................................................................

    public final ExpressionNumber add(final ExpressionNumber value, final ExpressionNumberContext context) {
        check(value);

        return value.add0(this, context);
    }

    abstract ExpressionNumber add0(final ExpressionNumber value, final ExpressionNumberContext context);

    abstract ExpressionNumber add1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context);

    abstract ExpressionNumber add1(final ExpressionNumberDouble value, final ExpressionNumberContext context);

    // divide..............................................................................................................

    public final ExpressionNumber divide(final ExpressionNumber value, final ExpressionNumberContext context) {
        check(value);

        return value.divide0(this, context);
    }

    abstract ExpressionNumber divide0(final ExpressionNumber value, final ExpressionNumberContext context);

    abstract ExpressionNumber divide1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context);

    abstract ExpressionNumber divide1(final ExpressionNumberDouble value, final ExpressionNumberContext context);

// modulo..............................................................................................................

    public final ExpressionNumber modulo(final ExpressionNumber value, final ExpressionNumberContext context) {
        check(value);

        return value.modulo0(this, context);
    }

    abstract ExpressionNumber modulo0(final ExpressionNumber value, final ExpressionNumberContext context);

    abstract ExpressionNumber modulo1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context);

    abstract ExpressionNumber modulo1(final ExpressionNumberDouble value, final ExpressionNumberContext context);

    // multiply..............................................................................................................

    public final ExpressionNumber multiply(final ExpressionNumber value, final ExpressionNumberContext context) {
        check(value);

        return value.multiply0(this, context);
    }

    abstract ExpressionNumber multiply0(final ExpressionNumber value, final ExpressionNumberContext context);

    abstract ExpressionNumber multiply1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context);

    abstract ExpressionNumber multiply1(final ExpressionNumberDouble value, final ExpressionNumberContext context);

    // power..............................................................................................................

    public final ExpressionNumber power(final ExpressionNumber value, final ExpressionNumberContext context) {
        check(value);

        return value.power0(this, context);
    }

    abstract ExpressionNumber power0(final ExpressionNumber value, final ExpressionNumberContext context);

    abstract ExpressionNumber power1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context);

    abstract ExpressionNumber power1(final ExpressionNumberDouble value, final ExpressionNumberContext context);

    // subtract..............................................................................................................

    public final ExpressionNumber subtract(final ExpressionNumber value, final ExpressionNumberContext context) {
        check(value);

        return value.subtract0(this, context);
    }

    abstract ExpressionNumber subtract0(final ExpressionNumber value, final ExpressionNumberContext context);

    abstract ExpressionNumber subtract1(final ExpressionNumberBigDecimal value, final ExpressionNumberContext context);

    abstract ExpressionNumber subtract1(final ExpressionNumberDouble value, final ExpressionNumberContext context);

    // and..............................................................................................................

    public final ExpressionNumber and(final ExpressionNumber value) {
        check(value);

        return value.and0(this);
    }

    abstract ExpressionNumber and0(final ExpressionNumber value);

    abstract ExpressionNumber and1(final ExpressionNumberBigDecimal value);

    abstract ExpressionNumber and1(final ExpressionNumberDouble value);

    // or..............................................................................................................

    public final ExpressionNumber or(final ExpressionNumber value) {
        check(value);

        return value.or0(this);
    }

    abstract ExpressionNumber or0(final ExpressionNumber value);

    abstract ExpressionNumber or1(final ExpressionNumberBigDecimal value);

    abstract ExpressionNumber or1(final ExpressionNumberDouble value);

    // xor..............................................................................................................

    public final ExpressionNumber xor(final ExpressionNumber value) {
        check(value);

        return value.xor0(this);
    }

    abstract ExpressionNumber xor0(final ExpressionNumber value);

    abstract ExpressionNumber xor1(final ExpressionNumberBigDecimal value);

    abstract ExpressionNumber xor1(final ExpressionNumberDouble value);

    // equals..............................................................................................................

    public final boolean equals(final ExpressionNumber value) {
        check(value);

        return ComparisonRelation.EQ.test(value.compare0(this));
    }

    // greaterThan..............................................................................................................

    public final boolean greaterThan(final ExpressionNumber value) {
        check(value);

        return ComparisonRelation.GT.test(value.compare0(this));
    }

    // greaterThanEquals..............................................................................................................

    public final boolean greaterThanEquals(final ExpressionNumber value) {
        check(value);

        return ComparisonRelation.GTE.test(value.compare0(this));
    }

    // lessThan..............................................................................................................

    public final boolean lessThan(final ExpressionNumber value) {
        check(value);

        return ComparisonRelation.LT.test(value.compare0(this));
    }

    // lessThanEquals..............................................................................................................

    public final boolean lessThanEquals(final ExpressionNumber value) {
        check(value);

        return ComparisonRelation.LTE.test(value.compare0(this));
    }

    // notEquals..............................................................................................................

    public final boolean notEquals(final ExpressionNumber value) {
        check(value);

        return ComparisonRelation.NE.test(value.compare0(this));
    }

    abstract int compare0(final ExpressionNumber value);

    abstract int compare1(final ExpressionNumberBigDecimal value);

    abstract int compare1(final ExpressionNumberDouble value);

    // toXXX............................................................................................................

    @Override
    public abstract byte byteValue();

    @Override
    public abstract short shortValue();

    @Override
    public abstract int intValue();

    @Override
    public abstract long longValue();

    @Override
    public abstract float floatValue();

    @Override
    public abstract double doubleValue();

    public abstract BigInteger bigInteger();

    public abstract BigDecimal bigDecimal();

    // Object...........................................................................................................

    @Override
    public abstract int hashCode();

    @Override
    public final boolean equals(final Object other) {
        return this == other || this.canBeEqual(other) && this.equals0((ExpressionNumber) other);
    }

    abstract boolean canBeEqual(final Object other);

    abstract boolean equals0(final ExpressionNumber other);

    @Override
    public abstract String toString();

    // Comparable.......................................................................................................

    @Override
    public final int compareTo(final ExpressionNumber other) {
        return other.compareTo0(this);
    }

    abstract int compareTo0(final ExpressionNumber other);

    abstract int compareTo1(final ExpressionNumberBigDecimal other);

    abstract int compareTo1(final ExpressionNumberDouble other);
}
