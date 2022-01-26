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
import java.math.MathContext;
import java.util.Objects;

/**
 * {@link ExpressionNumber} come in two kinds, one that uses {@link BigDecimal} and another that uses {@link Double}.
 * The former uses more memory, is more accurate, but slower, while the later is less accurate limited to 64 bits but is faster.
 */
public enum ExpressionNumberKind {
    BIG_DECIMAL {
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
        ExpressionNumber randomBetween0(final ExpressionNumber lower,
                                        final ExpressionNumber upper,
                                        final ExpressionNumberContext context) {
            final BigDecimal lowerDouble = lower.bigDecimal();
            final BigDecimal upperDouble = upper.bigDecimal();
            if (lowerDouble.compareTo(upperDouble) > 0) {
                throw new IllegalArgumentException("Lower " + upper + " must be less than upper " + upper);
            }

            final MathContext mathContext = context.mathContext();
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
                            )
                    )
            );
        }

        @Override
        public ExpressionNumber setSign(final ExpressionNumberSign sign) {
            return sign.expressionNumberBigDecimal();
        }
    },

    DOUBLE {
        @Override
        public ExpressionNumber create(final Number number) {
            return number instanceof ExpressionNumberDouble ?
                    (ExpressionNumberDouble) number :
                    fromDouble(number.doubleValue());
        }

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

    /**
     * Factory that creates the appropriate {@link ExpressionNumber} instance
     */
    public abstract ExpressionNumber create(final Number number);

    /**
     * Factory that parses the {@link String text} into the appropriate {@link ExpressionNumber} instance
     */
    public abstract ExpressionNumber parse(final String text);

    /**
     * Returns either {@link BigDecimal} or {@link Double}.
     */
    public abstract Class<?> numberType();

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
     * This constant will disappear when {@link ExpressionNumberKind} is configurable.
     */
    public final static ExpressionNumberKind DEFAULT = DOUBLE;
}
