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
import walkingkooka.InvalidCharacterException;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CharSequences;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberKindTest implements ClassTesting<ExpressionNumberKind> {

    // create............................................................................................................

    @Test
    public void testCreateBigDecimalInteger() {
        this.checkEquals(
                ExpressionNumberKind.BIG_DECIMAL.create(Integer.MAX_VALUE),
                ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.valueOf(Integer.MAX_VALUE))
        );
    }

    @Test
    public void testCreateBigDecimalLong() {
        this.checkEquals(
                ExpressionNumberKind.BIG_DECIMAL.create(Long.MAX_VALUE),
                ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.valueOf(Long.MAX_VALUE))
        );
    }

    @Test
    public void testCreateBigDecimalBigInteger() {
        final BigInteger value = new BigInteger("123456789012345678901234567890");

        this.checkEquals(
                ExpressionNumberKind.BIG_DECIMAL.create(value),
                ExpressionNumberBigDecimal.withBigDecimal(new BigDecimal(value))
        );
    }

    @Test
    public void testCreateBigDecimalBigDecimal() {
        final BigDecimal value = new BigDecimal("123456789012345678901234567890");

        this.checkEquals(
                ExpressionNumberKind.BIG_DECIMAL.create(value),
                ExpressionNumberBigDecimal.withBigDecimal(value)
        );
    }

    @Test
    public void testCreateDoubleFloat() {
        this.checkEquals(
                ExpressionNumberKind.DOUBLE.create(123.5f),
                ExpressionNumberDouble.withDouble(123.5f)
        );
    }

    @Test
    public void testCreateDoubleDouble() {
        this.checkEquals(
                ExpressionNumberKind.DOUBLE.create(123.5),
                ExpressionNumberDouble.withDouble(123.5)
        );
    }

    // e................................................................................................................

    @Test
    public void testEBigDecimal() {
        final ExpressionNumber number = ExpressionNumberKind.BIG_DECIMAL.e(
                new FakeExpressionNumberContext() {
                    @Override
                    public MathContext mathContext() {
                        return MathContext.DECIMAL32;
                    }
                }
        );
        this.checkEquals(
                new BigDecimal("2.718282"),
                number.bigDecimal()
        );
    }

    @Test
    public void testEDouble() {
        final ExpressionNumber number = ExpressionNumberKind.DOUBLE.e(
                ExpressionNumberContexts.fake()
        );
        this.checkEquals(
                Math.E,
                number.doubleValue()
        );
    }

    // one............................................................................................................

    @Test
    public void testOneBigDecimal() {
        this.checkEquals(
                ExpressionNumberKind.BIG_DECIMAL.create(1),
                ExpressionNumberKind.BIG_DECIMAL.one()
        );
    }

    @Test
    public void testOneDouble() {
        this.checkEquals(
                ExpressionNumberKind.DOUBLE.create(1),
                ExpressionNumberKind.DOUBLE.one()
        );
    }

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

    // parseWithBase........................................................................................................

    @Test
    public void testParseWithBaseBigDecimalNullTextFails() {
        this.parseWithBaseFails(
                ExpressionNumberKind.BIG_DECIMAL,
                null,
                2,
                NullPointerException.class,
                "text"
        );
    }

    @Test
    public void testParseWithBaseBigDecimalEmptyTextFails() {
        this.parseWithBaseFails(
                ExpressionNumberKind.BIG_DECIMAL,
                "",
                2,
                IllegalArgumentException.class,
                "text is empty"
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBaseMinusOneFails() {
        this.parseWithBaseInvalidBaseFails(
                ExpressionNumberKind.BIG_DECIMAL,
                -1
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase0Fails() {
        this.parseWithBaseInvalidBaseFails(
                ExpressionNumberKind.BIG_DECIMAL,
                0
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase1Fails() {
        this.parseWithBaseInvalidBaseFails(
                ExpressionNumberKind.BIG_DECIMAL,
                1
        );
    }

    @Test
    public void testParseWithBaseDouble1Fails() {
        this.parseWithBaseInvalidBaseFails(
                ExpressionNumberKind.DOUBLE,
                1
        );
    }

    @Test
    public void testParseWithBaseDouble37Fails() {
        this.parseWithBaseInvalidBaseFails(
                ExpressionNumberKind.DOUBLE,
                37
        );
    }

    private void parseWithBaseInvalidBaseFails(final ExpressionNumberKind kind,
                                               final int base) {
        this.parseWithBaseFails(
                kind,
                "0",
                base,
                IllegalArgumentException.class,
                "Invalid base " + base + " expected between 2 and 36"
        );
    }

    @Test
    public void testParseWithBaseBigDecimalInvalidCharacterFails() {
        this.parseWithBaseFails(
                ExpressionNumberKind.BIG_DECIMAL,
                "12",
                2,
                InvalidCharacterException.class,
                "Invalid character '2' at 1 in \"12\""
        );
    }

    @Test
    public void testParseWithBaseBigDecimalInvalidCharacterFails2() {
        this.parseWithBaseFails(
                ExpressionNumberKind.BIG_DECIMAL,
                "11-",
                2,
                InvalidCharacterException.class,
                "Invalid character '-' at 2 in \"11-\""
        );
    }

    private <T extends RuntimeException> void parseWithBaseFails(
            final ExpressionNumberKind kind,
            final String text,
            final int base,
            final Class<T> thrown,
            final String expected) {
        final T actual =
                assertThrows(
                        thrown,
                        () -> kind.parseWithBase(
                                text,
                                base,
                                this.createContext(kind)
                        )
                );
        this.checkEquals(
                expected,
                actual.getMessage(),
                () -> kind + ".parseWithBase " + CharSequences.quoteAndEscape(text) + ", base=" + base
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase2_0() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "0",
                2,
                0
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase2_101() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "101",
                2,
                5
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase2_01010101() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "01010101",
                2,
                0x55
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase2_Minus01010101() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "-01010101",
                2,
                -0x55
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase2_BigNumber() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "11110000101010100101010100000001",
                2,
                0xf0AA5501L
        );
    }

    @Test
    public void testParseWithBase3() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "12",
                3,
                5
        );
    }

    @Test
    public void testParseWithBase4() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "12",
                4,
                6
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase8_0() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "0",
                8,
                0
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase8_127() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "127",
                8,
                0127
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase8_0776() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "0776",
                8,
                0776
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase8_Minus776() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "-776",
                8,
                -0776
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase10_101() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "101",
                10,
                101
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase10_987() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "987",
                10,
                987
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase16_Minus876() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "-876",
                10,
                -876
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase16_101() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "101",
                16,
                0x101
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase16_0FF() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "0FF",
                16,
                0xff
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase16_Minus12EF() {
        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                "-12EF",
                16,
                -0x12EF
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase10_BigNumber() {
        final String text = "-12345678901234567890123456789012345678901234567890";

        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                text,
                10,
                new BigDecimal(text)
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase10_DifferentPlusSign() {
        final String text = "*123";

        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                text,
                10,
                this.createContext(
                        ExpressionNumberKind.BIG_DECIMAL,
                        '*',
                        '!',
                        '@'
                ),
                ExpressionNumberKind.BIG_DECIMAL.create(123)
        );
    }

    @Test
    public void testParseWithBaseBigDecimalBase10_DifferentMinusSign() {
        final String text = "*123";

        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                text,
                10,
                this.createContext(
                        ExpressionNumberKind.BIG_DECIMAL,
                        '!',
                        '*',
                        '@'
                ),
                ExpressionNumberKind.BIG_DECIMAL.create(-123)
        );
    }

    @Test
    public void testParseWithBaseDecimal() {
        final String text = "123@";

        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                text,
                10,
                this.createContext(
                        ExpressionNumberKind.BIG_DECIMAL,
                        '!',
                        '*',
                        '@'
                ),
                ExpressionNumberKind.BIG_DECIMAL.create(123)
        );
    }

    @Test
    public void testParseWithBaseDecimalsIgnored() {
        final String text = "123@678";

        this.parseWithBaseAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                text,
                10,
                this.createContext(
                        ExpressionNumberKind.BIG_DECIMAL,
                        '!',
                        '*',
                        '@'
                ),
                ExpressionNumberKind.BIG_DECIMAL.create(123)
        );
    }

    private void parseWithBaseAndCheck(final ExpressionNumberKind kind,
                                       final String text,
                                       final int base,
                                       final Number expected) {
        this.parseWithBaseAndCheck(
                kind,
                text,
                base,
                kind.create(expected)
        );
    }

    private void parseWithBaseAndCheck(final ExpressionNumberKind kind,
                                       final String text,
                                       final int base,
                                       final ExpressionNumber expected) {
        this.parseWithBaseAndCheck(
                kind,
                text,
                base,
                this.createContext(kind),
                expected
        );
    }

    private void parseWithBaseAndCheck(final ExpressionNumberKind kind,
                                       final String text,
                                       final int base,
                                       final ExpressionNumberContext context,
                                       final ExpressionNumber expected) {
        this.checkEquals(
                expected,
                kind.parseWithBase(
                        text,
                        base,
                        context
                )
        );
    }

    // pi..........................................................................................................

    @Test
    public void testPiBigDecimal() {
        final ExpressionNumber number = ExpressionNumberKind.BIG_DECIMAL.pi(
                new FakeExpressionNumberContext() {
                    @Override
                    public MathContext mathContext() {
                        return MathContext.DECIMAL32;
                    }
                }
        );
        this.checkEquals(
                new BigDecimal("3.141593"),
                number.bigDecimal()
        );
    }

    @Test
    public void testPiDouble() {
        final ExpressionNumber number = ExpressionNumberKind.DOUBLE.pi(
                ExpressionNumberContexts.fake()
        );
        this.checkEquals(
                Math.PI,
                number.doubleValue()
        );
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
                () -> kind.randomBetween(
                        kind.create(5),
                        kind.create(4),
                        ExpressionNumberContexts.fake()
                )
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
        final ExpressionNumberContext context = this.createContext(kind);

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

    private ExpressionNumberContext createContext(final ExpressionNumberKind kind) {
        return this.createContext(
                kind,
                '+',
                '-',
                '.'
        );
    }

    private ExpressionNumberContext createContext(final ExpressionNumberKind kind,
                                                  final char plus,
                                                  final char minus,
                                                  final char decimalSeparator) {
        return new FakeExpressionNumberContext() {

            @Override
            public char decimalSeparator() {
                return decimalSeparator;
            }

            @Override
            public ExpressionNumberKind expressionNumberKind() {
                return kind;
            }

            @Override
            public MathContext mathContext() {
                return MathContext.UNLIMITED;
            }

            @Override
            public char negativeSign() {
                return minus;
            }

            @Override
            public char positiveSign() {
                return plus;
            }

            @Override
            public String toString() {
                return this.expressionNumberKind() + " " + this.mathContext();
            }
        };
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
