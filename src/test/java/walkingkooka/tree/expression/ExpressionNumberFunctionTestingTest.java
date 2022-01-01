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

import org.junit.jupiter.api.Test;
import walkingkooka.test.Testing;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class ExpressionNumberFunctionTestingTest implements Testing {

    @Test
    public void testMapBigDecimalAndCheck() {
        for (final RoundingMode roundingMode : RoundingMode.values()) {
            new ExpressionNumberFunctionTesting<>() {
                @Override
                public FakeExpressionNumberFunction createExpressionNumberFunction() {
                    throw new UnsupportedOperationException();
                }
            }.mapBigDecimalAndCheck(
                    new FakeExpressionNumberFunction() {
                        @Override
                        public BigDecimal mapBigDecimal(final BigDecimal value,
                                                        final RoundingMode r) {
                            assertSame(roundingMode, r);
                            return value.multiply(BigDecimal.TEN);
                        }
                    },
                    BigDecimal.TEN,
                    roundingMode,
                    BigDecimal.valueOf(100)
            );
        }
    }

    @Test
    public void testMapBigDecimalAndCheckFails() {
        boolean failed = false;

        try {
            new ExpressionNumberFunctionTesting<>() {
                @Override
                public FakeExpressionNumberFunction createExpressionNumberFunction() {
                    throw new UnsupportedOperationException();
                }
            }.mapBigDecimalAndCheck(
                    new FakeExpressionNumberFunction() {
                        @Override
                        public BigDecimal mapBigDecimal(final BigDecimal value,
                                                        final RoundingMode r) {
                            return value.multiply(BigDecimal.TEN);
                        }
                    },
                    BigDecimal.TEN,
                    RoundingMode.HALF_UP,
                    BigDecimal.valueOf(-1)
            );
            failed = false;
        } catch (final Throwable expected) {
            failed = true;
        }
        this.checkEquals(true, failed);
    }

    @Test
    public void testMapDoubleAndCheck() {
        new ExpressionNumberFunctionTesting<>() {
            @Override
            public FakeExpressionNumberFunction createExpressionNumberFunction() {
                throw new UnsupportedOperationException();
            }
        }.mapDoubleAndCheck(
                new FakeExpressionNumberFunction() {
                    @Override
                    public double mapDouble(final double value) {
                        return value * 2;
                    }
                },
                10.0,
                20.0
        );
    }

    @Test
    public void testMapDoubleAndCheckFails() {
        boolean failed = false;

        try {
            new ExpressionNumberFunctionTesting<>() {
                @Override
                public FakeExpressionNumberFunction createExpressionNumberFunction() {
                    throw new UnsupportedOperationException();
                }
            }.mapDoubleAndCheck(
                    new FakeExpressionNumberFunction() {
                        @Override
                        public double mapDouble(final double value) {
                            return value * 10;
                        }
                    },
                    10.0,
                    -1.0
            );
            failed = false;
        } catch (final Throwable expected) {
            failed = true;
        }
        this.checkEquals(true, failed);
    }
}
