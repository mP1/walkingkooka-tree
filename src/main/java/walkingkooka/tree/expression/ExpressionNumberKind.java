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
import walkingkooka.InvalidCharacterException;
import walkingkooka.text.CharSequences;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * {@link ExpressionNumber} come in two kinds, one that uses {@link BigDecimal} and another that uses {@link Double}.
 * The former uses more memory, is more accurate, but slower, while the later is less accurate limited to 64 bits but is faster.
 */
public enum ExpressionNumberKind {
    BIG_DECIMAL(
            ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.ZERO),
            ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.ONE)
    ) {
        @Override
        public ExpressionNumber create(final Number number) {
            return number instanceof ExpressionNumberBigDecimal ?
                    (ExpressionNumberBigDecimal) number :
                    number instanceof BigDecimal ? fromBigDecimal((BigDecimal) number) :
                            number instanceof BigInteger ? fromBigDecimal(new BigDecimal((BigInteger) number)) :
                                    number instanceof Long ? fromBigDecimal(new BigDecimal((Long) number)) :
                                            fromBigDecimal(BigDecimal.valueOf(number.doubleValue()));
        }

        @Override
        public ExpressionNumber e(final ExpressionNumberContext context) {
            return this.create(
                    BigDecimalMath.e(context.mathContext())
            );
        }

        @Override
        public ExpressionNumber parse(final String text) {
            return fromBigDecimal(new BigDecimal(text));
        }

        private ExpressionNumber fromBigDecimal(final BigDecimal value) {
            return ExpressionNumberBigDecimal.withBigDecimal(value);
        }

        @Override
        public Class<?> numberType() {
            return BigDecimal.class;
        }

        @Override
        public ExpressionNumber pi(final ExpressionNumberContext context) {
            return this.create(
                    BigDecimalMath.pi(context.mathContext())
            );
        }

        @Override
        ExpressionNumber randomBetween0(final ExpressionNumber lower,
                                        final ExpressionNumber upper,
                                        final ExpressionNumberContext context) {
            final BigDecimal lowerDouble = lower.bigDecimal();
            final BigDecimal upperDouble = upper.bigDecimal();
            if (lowerDouble.compareTo(upperDouble) > 0) {
                throw new IllegalArgumentException("Lower " + upper + " must be less than upper " + upper);
            }

            BigDecimal bottom = ExpressionNumberBigDecimal.ceilBigDecimal(lowerDouble);
            BigDecimal top = ExpressionNumberBigDecimal.floorBigDecimal(lowerDouble);

            if (bottom.compareTo(top) > 0) {
                top = bottom;
            }

            return this.create(
                    ExpressionNumberBigDecimal.roundBigDecimal(
                            bottom.add(
                                    new BigDecimal(Math.random())
                                            .multiply(top.subtract(bottom))
                            ),
                            RoundingMode.HALF_UP
                    )
            );
        }

        @Override
        public ExpressionNumber setSign(final ExpressionNumberSign sign) {
            return sign.expressionNumberBigDecimal();
        }
    },

    DOUBLE(
            ExpressionNumberDouble.withDouble(0),
            ExpressionNumberDouble.withDouble(1)
    ) {
        @Override
        public ExpressionNumber create(final Number number) {
            return number instanceof ExpressionNumberDouble ?
                    (ExpressionNumberDouble) number :
                    fromDouble(number.doubleValue());
        }

        @Override
        public ExpressionNumber e(final ExpressionNumberContext context) {
            return E;
        }

        private final ExpressionNumber E = ExpressionNumber.with(Math.E);

        @Override
        public ExpressionNumber parse(final String text) {
            return fromDouble(Double.parseDouble(text));
        }

        private ExpressionNumber fromDouble(final double value) {
            return ExpressionNumberDouble.withDouble(value);
        }

        @Override
        public Class<?> numberType() {
            return Double.class;
        }

        @Override
        public ExpressionNumber pi(final ExpressionNumberContext context) {
            return PI;
        }

        private final ExpressionNumber PI = ExpressionNumber.with(Math.PI);

        @Override
        ExpressionNumber randomBetween0(final ExpressionNumber lower,
                                        final ExpressionNumber upper,
                                        final ExpressionNumberContext context) {
            final double lowerDouble = lower.doubleValue();
            final double upperDouble = upper.doubleValue();
            if (lowerDouble > upperDouble) {
                throw new IllegalArgumentException("Lower " + upper + " must be less than upper " + upper);
            }

            double bottom = Math.ceil(lowerDouble);
            double top = Math.floor(upperDouble);
            if (bottom > top) {
                top = bottom;
            }

            return this.create(
                    (bottom + (long) (Math.random() * ((top - bottom))))
            );
        }

        @Override
        public ExpressionNumber setSign(final ExpressionNumberSign sign) {
            return sign.expressionNumberDouble();
        }
    };

    ExpressionNumberKind(final ExpressionNumber zero,
                         final ExpressionNumber one) {
        this.zero = zero;
        this.one = one;
    }

    /**
     * Factory that creates the appropriate {@link ExpressionNumber} instance
     */
    public abstract ExpressionNumber create(final Number number);

    public abstract ExpressionNumber e(final ExpressionNumberContext context);

    /**
     * Returns an {@link ExpressionNumber} with a value of +1.
     */
    public final ExpressionNumber one() {
        return this.one;
    }

    private final ExpressionNumber one;

    /**
     * Factory that parses the {@link String text} into the appropriate {@link ExpressionNumber} instance
     */
    public abstract ExpressionNumber parse(final String text);

    /**
     * Returns either {@link BigDecimal} or {@link Double}.
     */
    public abstract Class<?> numberType();

    /**
     * Parses the given text into a integer value. A leading sign will make any number negative, while any decimals will
     * cause an {@link IllegalArgumentException} to be thrown.
     */
    public final ExpressionNumber parseWithBase(final String text,
                                                final int base,
                                                final ExpressionNumberContext context) {
        CharSequences.failIfNullOrEmpty(text, "text");

        switch (base) {
            case 2:
            case 8:
            case 10:
            case 16:
                break;
            default:
                throw new IllegalArgumentException("Invalid base " + base + " expected 2,8,10 or 16");
        }
        Objects.requireNonNull(context, "context");

        final ExpressionNumberKind kind = context.expressionNumberKind();
        final ExpressionNumber multiplier = this.create(base);
        final int length = text.length();

        int start = 0;
        boolean negative = false;

        final char maybeSign = text.charAt(0);

        if (context.positiveSign() == maybeSign) {
            start++;
        } else {
            if (context.negativeSign() == maybeSign) {
                negative = true;
                start++;
            }
        }

        ExpressionNumber value = this.zero();
        for (int i = start; i < length; i++) {
            final char c = text.charAt(i);
            final int charNumericValue = Character.digit(c, base);
            if (-1 == charNumericValue) {
                throw new InvalidCharacterException(text, i);
            }

            value = value.multiply(multiplier, context)
                    .add(
                            kind.create(charNumericValue),
                            context
                    );
        }

        return negative ?
                value.negate(context) :
                value;
    }

    public abstract ExpressionNumber pi(final ExpressionNumberContext context);

    /**
     * Generates a random {@link ExpressionNumber} between 0 and 1
     */
    public final ExpressionNumber random(final ExpressionNumberContext context) {
        return this.create(Math.random());
    }

    /**
     * Generates a random {@link ExpressionNumber} integer value between the lower (inclusive) and upper bounds (exclusive)
     */
    public final ExpressionNumber randomBetween(final ExpressionNumber lower,
                                                final ExpressionNumber upper,
                                                final ExpressionNumberContext context) {
        Objects.requireNonNull(lower, "lower");
        Objects.requireNonNull(upper, "upper");

        return this.randomBetween0(lower, upper, context);
    }

    abstract ExpressionNumber randomBetween0(final ExpressionNumber lower,
                                             final ExpressionNumber upper,
                                             final ExpressionNumberContext context);

    /**
     * Factory that returns a {@link ExpressionNumber} with this sign and the kind.
     */
    public abstract ExpressionNumber setSign(final ExpressionNumberSign sign);

    /**
     * Returns an {@link ExpressionNumber} with a value of 0.
     */
    public final ExpressionNumber zero() {
        return this.zero;
    }

    private final ExpressionNumber zero;

    /**
     * This constant will disappear when {@link ExpressionNumberKind} is configurable.
     */
    public final static ExpressionNumberKind DEFAULT = DOUBLE;
}
