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
     * This constant will disappear when {@link ExpressionNumberKind} is configurable.
     */
    public final static ExpressionNumberKind DEFAULT = DOUBLE;
}
