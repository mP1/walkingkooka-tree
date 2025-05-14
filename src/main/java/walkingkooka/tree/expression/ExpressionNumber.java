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

import walkingkooka.Value;
import walkingkooka.compare.CompareResult;
import walkingkooka.math.Maths;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Objects;

/**
 * Collection of utilities to assist with converting number values to one of the preferred expression number types of
 * {@link BigDecimal}, {@link BigInteger}, {@link Double} and {@link Long}.
 * Note that the result is always the same type, adding a double with a BigDecimal always returns the left(this) type and
 * does not widen or narrow the right parameter.
 */
public abstract class ExpressionNumber extends Number implements Comparable<ExpressionNumber>,
    Value<Number> {

    private static final long serialVersionUID = 0L;

    /**
     * Tests if the value is a number or {@link ExpressionNumber}
     */
    public static boolean is(final Object value) {
        return Maths.isNumber(value) || value instanceof ExpressionNumber;
    }

    /**
     * Tests if the value is a Number or {@link ExpressionNumber}
     */
    public static boolean isClass(final Class<?> value) {
        return (null != value && Maths.isNumberClass(value)) ||
            isExpressionNumberAndNotNumber(value);
    }

    /**
     * Tests if the value is a {@link ExpressionNumber} and not a {@link Number}
     */
    public static boolean isExpressionNumberAndNotNumber(final Class<?> value) {
        return value == ExpressionNumber.class ||
            value == ExpressionNumberBigDecimal.class ||
            value == ExpressionNumberDouble.class;
    }

    /**
     * Creates a {@link ExpressionNumber} wrapping the given {@link Double}.
     * Future math operations will operate on doubles.
     */
    static ExpressionNumberDouble with(final double value) {
        return ExpressionNumberDouble.withDouble(value);
    }

    /**
     * Creates a {@link ExpressionNumber} wrapping the given {@link BigDecimal}.
     * Future math operations will operate on {@link BigDecimal}.
     */
    static ExpressionNumberBigDecimal with(final BigDecimal value) {
        return ExpressionNumberBigDecimal.withBigDecimal(value);
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

    /**
     * Returns the matching {@link ExpressionNumberKind} for this kind of {@link ExpressionNumber}.
     */
    public abstract ExpressionNumberKind kind();

    /**
     * Would be setter that changes the kind of the {@link ExpressionNumber}.
     */
    public final ExpressionNumber setKind(final ExpressionNumberKind kind) {
        Objects.requireNonNull(kind, "kind");

        return ExpressionNumberKind.BIG_DECIMAL == kind ?
            this.setKindBigDecimal() :
            this.setKindDouble();
    }

    abstract ExpressionNumberBigDecimal setKindBigDecimal();

    abstract ExpressionNumberDouble setKindDouble();

    // value............................................................................................................

    @Override
    public abstract Number value();

    // sign.............................................................................................................

    public abstract ExpressionNumberSign sign();

    // map..............................................................................................................

    /**
     * Applies the given mapper returning the {@link ExpressionNumber} with the result.
     */
    abstract public ExpressionNumber map(final ExpressionNumberFunction function,
                                         final MathContext context);

    // abs..............................................................................................................

    public abstract ExpressionNumber abs(final ExpressionNumberContext context);

    // ceil..............................................................................................................

    public abstract ExpressionNumber ceil(final ExpressionNumberContext context);

    // exp..............................................................................................................

    public abstract ExpressionNumber exp(final ExpressionNumberContext context);

    // floor..............................................................................................................

    public abstract ExpressionNumber floor(final ExpressionNumberContext context);

    // ln..............................................................................................................

    public abstract ExpressionNumber ln(final ExpressionNumberContext context);

    // log10............................................................................................................

    public abstract ExpressionNumber log10(final ExpressionNumberContext context);

    // negate...........................................................................................................

    public abstract ExpressionNumber negate(final ExpressionNumberContext context);

    // not..............................................................................................................

    public abstract ExpressionNumber not();

    // round............................................................................................................

    public abstract ExpressionNumber round(final ExpressionNumberContext context);

    // binary operations.................................................................................................

    private static void check(final ExpressionNumber value) {
        Objects.requireNonNull(value, "value");
    }

    // add..............................................................................................................

    public final ExpressionNumber add(final ExpressionNumber value, final ExpressionNumberContext context) {
        check(value);

        return this.add0(value, context);
    }

    abstract ExpressionNumber add0(final ExpressionNumber value, final ExpressionNumberContext context);

    // divide..............................................................................................................

    public final ExpressionNumber divide(final ExpressionNumber value, final ExpressionNumberContext context) {
        check(value);

        try {
            return this.divide0(value, context);
        } catch (final ArithmeticException cause) {
            throw new ExpressionEvaluationException("Division by zero", cause);
        }
    }

    abstract ExpressionNumber divide0(final ExpressionNumber value, final ExpressionNumberContext context);

    // log.............................................................................................................

    public final ExpressionNumber log(final ExpressionNumber base,
                                      final ExpressionNumberContext context) {
        return this.ln(context)
            .divide(
                base.ln(context),
                context
            );
    }

    // max..............................................................................................................

    public final ExpressionNumber max(final ExpressionNumber value) {
        check(value);

        return this.max0(value);
    }

    abstract ExpressionNumber max0(final ExpressionNumber value);

    // min..............................................................................................................

    public final ExpressionNumber min(final ExpressionNumber value) {
        check(value);

        return this.min0(value);
    }

    abstract ExpressionNumber min0(final ExpressionNumber value);

    // modulo..............................................................................................................

    public final ExpressionNumber modulo(final ExpressionNumber value, final ExpressionNumberContext context) {
        check(value);

        return this.modulo0(value, context);
    }

    abstract ExpressionNumber modulo0(final ExpressionNumber value, final ExpressionNumberContext context);

    // multiply..............................................................................................................

    public final ExpressionNumber multiply(final ExpressionNumber value, final ExpressionNumberContext context) {
        check(value);

        return this.multiply0(value, context);
    }

    abstract ExpressionNumber multiply0(final ExpressionNumber value, final ExpressionNumberContext context);

    // power..............................................................................................................

    public final ExpressionNumber power(final ExpressionNumber value, final ExpressionNumberContext context) {
        check(value);

        return this.power0(value, context);
    }

    abstract ExpressionNumber power0(final ExpressionNumber value, final ExpressionNumberContext context);

    // sqrt..............................................................................................................

    public abstract ExpressionNumber sqrt(final ExpressionNumberContext context);

    // subtract..............................................................................................................

    public final ExpressionNumber subtract(final ExpressionNumber value, final ExpressionNumberContext context) {
        check(value);

        return this.subtract0(value, context);
    }

    abstract ExpressionNumber subtract0(final ExpressionNumber value, final ExpressionNumberContext context);

    // and..............................................................................................................

    public final ExpressionNumber and(final ExpressionNumber value) {
        check(value);

        return this.and0(value);
    }

    abstract ExpressionNumber and0(final ExpressionNumber value);

    // andNot...........................................................................................................

    public final ExpressionNumber andNot(final ExpressionNumber value) {
        check(value);

        return this.andNot0(value);
    }

    abstract ExpressionNumber andNot0(final ExpressionNumber value);

    // or..............................................................................................................

    public final ExpressionNumber or(final ExpressionNumber value) {
        check(value);

        return this.or0(value);
    }

    abstract ExpressionNumber or0(final ExpressionNumber value);

    // xor..............................................................................................................

    public final ExpressionNumber xor(final ExpressionNumber value) {
        check(value);

        return this.xor0(value);
    }

    abstract ExpressionNumber xor0(final ExpressionNumber value);

    // equals..............................................................................................................

    public final boolean equals(final ExpressionNumber value) {
        return CompareResult.EQ.test(this.compareTo(value));
    }

    // greaterThan..............................................................................................................

    public final boolean greaterThan(final ExpressionNumber value) {
        return CompareResult.GT.test(this.compareTo(value));
    }

    // greaterThanEquals..............................................................................................................

    public final boolean greaterThanEquals(final ExpressionNumber value) {
        return CompareResult.GTE.test(this.compareTo(value));
    }

    // lessThan..............................................................................................................

    public final boolean lessThan(final ExpressionNumber value) {
        return CompareResult.LT.test(this.compareTo(value));
    }

    // lessThanEquals..............................................................................................................

    public final boolean lessThanEquals(final ExpressionNumber value) {
        return CompareResult.LTE.test(this.compareTo(value));
    }

    // notEquals..............................................................................................................

    public final boolean notEquals(final ExpressionNumber value) {
        return CompareResult.NE.test(this.compareTo(value));
    }

    // toXXX............................................................................................................

    public abstract byte byteValueExact();

    public abstract short shortValueExact();

    public abstract int intValueExact();

    public abstract long longValueExact();

    public abstract BigInteger bigInteger();

    public abstract BigInteger bigIntegerExact();

    public abstract BigDecimal bigDecimal();

    // Object...........................................................................................................

    @Override
    public abstract int hashCode();

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
            this.canBeEqual(other) && CompareResult.EQ.test(this.compareTo((ExpressionNumber) other));
    }

    abstract boolean canBeEqual(final Object other);

    /**
     * If the toString has only decimal zeros remove them.
     */
    @Override
    public final String toString() {
        // remove trailing zeros...
        String toString = this.value().toString();
        final int decimal = toString.lastIndexOf('.');

        if (-1 != decimal) {
            int end = toString.length();
            int trailing = -1;
            int i = end;

            Stop:
            while (i >= decimal) {
                i--;
                switch (toString.charAt(i)) {
                    case '0':
                        end += trailing;
                        break;
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        trailing = 0;
                        break;
                    case '.':
                        toString = toString.substring(0, i + 1 == end ? i : end);
                        break Stop; // stop
                    default:
                        break Stop; // must have found exponent etc dont trim anything
                }
            }
        }
        return toString;
    }

    // toStringBase......................................................................................................

    /**
     * Adds support for formatting this number in the given base.
     */
    public final String toStringWithBase(final ExpressionNumber base) {
        Objects.requireNonNull(base, "base");

        return this.bigInteger().toString(base.intValueExact());
    }
}
