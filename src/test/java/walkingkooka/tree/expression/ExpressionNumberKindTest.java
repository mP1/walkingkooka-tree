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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.math.BigDecimal;
import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberKindTest implements ClassTesting<ExpressionNumberKind> {

    // parse............................................................................................................

    @Test
    public void testParseBigDecimal() {
        final String text = "12.3";
        this.checkEquals(ExpressionNumberKind.BIG_DECIMAL.create(new BigDecimal(text)), ExpressionNumberKind.BIG_DECIMAL.parse(text));
    }

    @Test
    public void testParseDouble() {
        final String text = "12.3";
        this.checkEquals(ExpressionNumberKind.DOUBLE.create(Double.parseDouble(text)), ExpressionNumberKind.DOUBLE.parse(text));
    }

    // random..........................................................................................................

    @Test
    public void testRandom() {
        final ExpressionNumber number = ExpressionNumberKind.DEFAULT.random(ExpressionNumberContexts.fake());
        this.checkNotEquals(
                null,
                number
        );
    }

    // randomBetween...................................................................................................

    private final int RANDOM_COUNT = 100;

    @Test
    public void testRandomBetweenLowerGreaterThanUpperBigDecimalFails() {
        this.randomBetweenLowerGreaterThanUpperFails(ExpressionNumberKind.BIG_DECIMAL);
    }

    @Test
    public void testRandomBetweenLowerGreaterThanUpperDoubleFails() {
        this.randomBetweenLowerGreaterThanUpperFails(ExpressionNumberKind.DOUBLE);
    }

    private void randomBetweenLowerGreaterThanUpperFails(final ExpressionNumberKind kind) {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    kind.randomBetween(
                            kind.create(5),
                            kind.create(4),
                            ExpressionNumberContexts.fake()
                    );
                }
        );
    }

    @Test
    public void testRandomBetweenBigDecimal() {
        for (int i = 0; i < RANDOM_COUNT; i++) {
            this.randomBetween(
                    1000 + i,
                    1000 + RANDOM_COUNT + i,
                    ExpressionNumberKind.BIG_DECIMAL
            );
        }
    }

    @Test
    public void testRandomBetweenBigDecimal2() {
        for (int i = 0; i < RANDOM_COUNT; i++) {
            this.randomBetween(
                    -1000 + i,
                    -1000 + RANDOM_COUNT + i,
                    ExpressionNumberKind.BIG_DECIMAL
            );
        }
    }

    @Test
    public void testRandomBetweenBigDecimal3() {
        for (int i = 0; i < RANDOM_COUNT; i++) {
            this.randomBetween(
                    1,
                    2,
                    ExpressionNumberKind.BIG_DECIMAL
            );
        }
    }

    @Test
    public void testRandomBetweenDouble() {
        for (int i = 0; i < RANDOM_COUNT; i++) {
            this.randomBetween(
                    1000 + i,
                    1010 + RANDOM_COUNT + i,
                    ExpressionNumberKind.DOUBLE
            );
        }
    }

    @Test
    public void testRandomBetweenDouble2() {
        for (int i = 0; i < RANDOM_COUNT; i++) {
            this.randomBetween(
                    -1000 + i,
                    -1000 + RANDOM_COUNT + i,
                    ExpressionNumberKind.DOUBLE
            );
        }
    }

    @Test
    public void testRandomBetweenDouble3() {
        for (int i = 0; i < RANDOM_COUNT; i++) {
            this.randomBetween(
                    1,
                    2,
                    ExpressionNumberKind.DOUBLE
            );
        }
    }

    private void randomBetween(final double lower,
                               final double upper,
                               final ExpressionNumberKind kind) {
        this.randomBetween(
                kind.create(lower),
                kind.create(upper),
                kind
        );
    }

    private void randomBetween(final ExpressionNumber lower,
                               final ExpressionNumber upper,
                               final ExpressionNumberKind kind) {
        final ExpressionNumberContext context = new FakeExpressionNumberContext() {
            @Override
            public ExpressionNumberKind expressionNumberKind() {
                return kind;
            }

            @Override
            public MathContext mathContext() {
                return MathContext.DECIMAL32;
            }

            @Override
            public String toString() {
                return this.expressionNumberKind() + " " + this.mathContext();
            }
        };

        final ExpressionNumber random = kind.randomBetween(
                lower,
                upper,
                context
        );

        this.checkEquals(
                random.round(context),
                random,
                "random is not an integer value"
        );

        this.checkEquals(
                true,
                random.compareTo(lower) >= 0,
                "random(" + random + ") must be >= lower(" + lower + ")"
        );

        this.checkEquals(
                true,
                random.compareTo(upper) < 0,
                "random(" + random + ") must be < upper(" + upper + ")"
        );
    }

    // setSign..........................................................................................................

    @Test
    public void testSetSignBigDecimalNegative() {
        this.setSignAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                ExpressionNumberSign.NEGATIVE,
                ExpressionNumberKind.BIG_DECIMAL.create(-1)
        );
    }

    @Test
    public void testSetSignBigDecimalZero() {
        this.setSignAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                ExpressionNumberSign.ZERO,
                ExpressionNumberKind.BIG_DECIMAL.create(0)
        );
    }

    @Test
    public void testSetSignBigDecimalPositive() {
        this.setSignAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                ExpressionNumberSign.POSITIVE,
                ExpressionNumberKind.BIG_DECIMAL.create(1)
        );
    }

    @Test
    public void testSetSignDoubleNegative() {
        this.setSignAndCheck(
                ExpressionNumberKind.DOUBLE,
                ExpressionNumberSign.NEGATIVE,
                ExpressionNumberKind.DOUBLE.create(-1.0)
        );
    }

    @Test
    public void testSetSignDoubleZero() {
        this.setSignAndCheck(
                ExpressionNumberKind.DOUBLE,
                ExpressionNumberSign.ZERO,
                ExpressionNumberKind.DOUBLE.create(0.0)
        );
    }

    @Test
    public void testSetSignDoublePositive() {
        this.setSignAndCheck(
                ExpressionNumberKind.DOUBLE,
                ExpressionNumberSign.POSITIVE,
                ExpressionNumberKind.DOUBLE.create(1.0)
        );
    }

    private void setSignAndCheck(final ExpressionNumberKind kind,
                                 final ExpressionNumberSign sign,
                                 final ExpressionNumber expected) {
        final ExpressionNumber number = kind.setSign(sign);
        this.checkEquals(
                expected,
                number,
                kind + " setSign " + sign
        );

        assertSame(
                kind,
                number.kind(),
                () -> "kind " + number
        );
    }

    // zero............................................................................................................

    @Test
    public void testZeroBigDecimal() {
        this.checkEquals(
                ExpressionNumberKind.BIG_DECIMAL.create(0),
                ExpressionNumberKind.BIG_DECIMAL.zero()
        );
    }

    @Test
    public void testZeroOne() {
        this.checkEquals(
                ExpressionNumberKind.DOUBLE.create(0),
                ExpressionNumberKind.DOUBLE.zero()
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ExpressionNumberKind> type() {
        return ExpressionNumberKind.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
