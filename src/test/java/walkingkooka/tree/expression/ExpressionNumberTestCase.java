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
import walkingkooka.ToStringTesting;
import walkingkooka.compare.ComparableTesting2;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.IsMethodTesting;
import walkingkooka.reflect.JavaVisibility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ExpressionNumberTestCase<N extends ExpressionNumber> implements ClassTesting<N>,
        ComparableTesting2<ExpressionNumber>,
        IsMethodTesting<N>,
        ToStringTesting<N> {

    final static ExpressionNumberKind KIND = ExpressionNumberKind.DEFAULT;

    final static ExpressionNumberContext CONTEXT = new FakeExpressionNumberContext() {

        @Override
        public MathContext mathContext() {
            return MathContext.DECIMAL32;
        }
    };

    ExpressionNumberTestCase() {
        super();
    }

    @Test
    public final void testKind() {
        final N number = this.create();
        final ExpressionNumberKind kind = number.kind();
        assertSame(kind.numberType(), number.value().getClass());
    }

    @Test
    public final void setKindNullFails() {
        final N number = this.create();
        assertThrows(NullPointerException.class, () -> this.create().setKind(null));
    }

    @Test
    public final void setKindSame() {
        final N number = this.create();
        assertSame(number, number.setKind(number.kind()));
    }

    @Test
    public abstract void setKindDifferent();

    // sign............................................................................................................

    @Test
    public final void testSignZero() {
        this.signAndCheck(
                0,
                ExpressionNumberSign.ZERO
        );
    }

    @Test
    public final void testSignNegative() {
        this.signAndCheck(
                -1,
                ExpressionNumberSign.NEGATIVE
        );
    }

    @Test
    public final void testSignPositive() {
        this.signAndCheck(
                +1,
                ExpressionNumberSign.POSITIVE
        );
    }

    private void signAndCheck(final double value,
                              final ExpressionNumberSign sign) {
        assertSame(
                sign,
                this.create(value).sign(),
                () -> "sign of " + value
        );
    }

    // map..............................................................................................................

    @Test
    public abstract void testMap();

    @Test
    public abstract void testMapSame();

    // abs..............................................................................................................

    @Test
    public final void testAbsPositive() {
        final ExpressionNumber number = this.create(1);
        assertSame(number, number.abs(CONTEXT));
    }

    @Test
    public final void testAbsNegative() {
        final N number = this.create(-1);
        final ExpressionNumber different = number.abs(CONTEXT);
        assertNotSame(number, different);
        this.checkValue(1, different);
    }

    // ceil..............................................................................................................

    @Test
    public final void testCeilPositiveInteger() {
        final ExpressionNumber number = this.create(1);
        assertSame(number, number.ceil(CONTEXT));
    }

    @Test
    public final void testCeilNegativeInteger() {
        final ExpressionNumber number = this.create(-1);
        assertSame(number, number.ceil(CONTEXT));
    }

    @Test
    public final void testCeilRoundUp() {
        final N number = this.create(1.4);
        final ExpressionNumber different = number.ceil(CONTEXT);
        assertNotSame(number, different);
        this.checkValue(2, different);
    }

    // floor..............................................................................................................

    @Test
    public final void testFloorPositiveInteger() {
        final ExpressionNumber number = this.create(1);
        assertSame(number, number.floor(CONTEXT));
    }

    @Test
    public final void testFloorNegativeInteger() {
        final ExpressionNumber number = this.create(-1);
        assertSame(number, number.floor(CONTEXT));
    }

    @Test
    public final void testFloorRoundDown() {
        final N number = this.create(1.4);
        final ExpressionNumber different = number.floor(CONTEXT);
        assertNotSame(number, different);
        this.checkValue(1, different);
    }

    @Test
    public final void testFloorRoundDown2() {
        final N number = this.create(1.7);
        final ExpressionNumber different = number.floor(CONTEXT);
        assertNotSame(number, different);
        this.checkValue(1, different);
    }

    // ln...............................................................................................................

    @Test
    public final void testLnNegativeFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.create(-1).ln(CONTEXT)
        );
    }

    // log..............................................................................................................

    @Test
    public final void testLog16Base2() {
        this.checkEquals(
                this.create(16)
                        .log(
                                this.create(2),
                                CONTEXT
                        ),
                this.create(4)
        );
    }

    @Test
    public final void testLog100Base10() {
        this.checkEquals(
                this.create(100)
                        .log(
                                this.create(10),
                                CONTEXT
                        ),
                this.create(2)
        );
    }

    // log10............................................................................................................

    @Test
    public final void testLog10NegativeFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.create(-1).log10(CONTEXT)
        );
    }

    @Test
    public final void testLog100() {
        this.checkEquals(
                this.create(2),
                this.create(100).log10(CONTEXT)
        );
    }

    // negate...........................................................................................................

    @Test
    public final void testNegatePositive() {
        final N number = this.create(+1);
        final ExpressionNumber different = number.negate(CONTEXT);
        assertNotSame(number, different);
        this.checkValue(-1, different);
    }

    @Test
    public final void testNegateZero() {
        final ExpressionNumber number = this.create(0);
        assertSame(number, number.negate(CONTEXT));
    }

    @Test
    public final void testNegateNegative() {
        final N number = this.create(-1);
        final ExpressionNumber different = number.negate(CONTEXT);
        assertNotSame(number, different);
        this.checkValue(1, different);
    }

    // not..............................................................................................................

    @Test
    public final void testNotPositive() {
        final int value = 1;

        final N number = this.create(value);
        final ExpressionNumber different = number.not();
        assertNotSame(number, different);
        this.checkValue(~value, different);
    }

    @Test
    public final void testNotZero() {
        final int value = 0;

        final N number = this.create(value);
        final ExpressionNumber different = number.not();
        assertNotSame(number, different);
        this.checkValue(~value, different);
    }

    @Test
    public final void testNotNegative() {
        final int value = -1;

        final N number = this.create(value);
        final ExpressionNumber different = number.not();
        assertNotSame(number, different);
        this.checkValue(~value, different);
    }

    // round..............................................................................................................

    @Test
    public final void testRoundPositiveInteger() {
        final ExpressionNumber number = this.create(1);
        assertSame(number, number.round(CONTEXT));
    }

    @Test
    public final void testRoundNegativeInteger() {
        final ExpressionNumber number = this.create(-1);
        assertSame(number, number.round(CONTEXT));
    }

    @Test
    public final void testRoundRoundUp() {
        final N number = this.create(1.6);
        final ExpressionNumber different = number.round(CONTEXT);
        assertNotSame(number, different);
        this.checkValue(2, different);
    }

    @Test
    public final void testRoundHalf() {
        final N number = this.create(1.5);
        final ExpressionNumber different = number.round(CONTEXT);
        assertNotSame(number, different);
        this.checkValue(2, different);
    }

    @Test
    public final void testRoundRoundDown() {
        final N number = this.create(1.4);
        final ExpressionNumber different = number.round(CONTEXT);
        assertNotSame(number, different);
        this.checkValue(1, different);
    }

    // sqrt..............................................................................................................

    @Test
    public final void testSqrtNegativeFails() {
        assertThrows(
                ExpressionEvaluationException.class,
                () -> this.create(-1).sqrt(CONTEXT)
        );
    }

    @Test
    public final void testSqrtZero() {
        this.checkValue(
                0,
                this.create(0).sqrt(CONTEXT)
        );
    }

    @Test
    public final void testSqrtOne() {
        this.checkValue(
                1,
                this.create(1).sqrt(CONTEXT)
        );
    }

    @Test
    public final void testSqrtNine() {
        this.checkValue(
                3,
                this.create(9).sqrt(CONTEXT)
        );
    }

    // add..............................................................................................................

    @Test
    public final void testAddZero() {
        final int value = 1;

        final N number = this.create(value);
        assertSame(number, number.add(this.create(0), CONTEXT));
    }

    @Test
    public final void testAddBigDecimal() {
        final int value = 1;
        final int add = 2;

        final N number = this.create(value);
        final ExpressionNumber different = number.add(ExpressionNumber.with(BigDecimal.valueOf(add)), CONTEXT);
        assertNotSame(number, different);
        this.checkValue(value + add, different);
    }

    @Test
    public final void testAddDouble() {
        final int value = 1;
        final int add = 2;

        final N number = this.create(value);
        final ExpressionNumber different = number.add(ExpressionNumber.with(add), CONTEXT);
        assertNotSame(number, different);
        this.checkValue(value + add, different);
    }

    // divide..............................................................................................................

    @Test
    public final void testDivideZeroFails() {
        this.divideZeroAndFail(0);
    }

    @Test
    public final void testDivideZeroFails2() {
        this.divideZeroAndFail(1);
    }

    private void divideZeroAndFail(final int value) {
        final N number = this.create(value);
        final ExpressionEvaluationException thrown = assertThrows(
                ExpressionEvaluationException.class,
                () -> number.divide(this.create(0), CONTEXT)
        );
        this.checkEquals(
                "Division by zero",
                thrown.getMessage(),
                "message"
        );
        this.checkEquals(
                ArithmeticException.class,
                thrown.getCause().getClass(),
                () -> "cause class"
        );
    }

    @Test
    public final void testDivideOne() {
        final int value = 1;

        final N number = this.create(value);
        assertSame(number, number.divide(this.create(1), CONTEXT));
    }

    @Test
    public final void testDivideBigDecimal() {
        final int value = 10;
        final int divide = 2;

        final N number = this.create(value);
        final ExpressionNumber different = number.divide(ExpressionNumber.with(BigDecimal.valueOf(divide)), CONTEXT);
        assertNotSame(number, different);
        this.checkValue(value / divide, different);
    }

    @Test
    public final void testDivideDouble() {
        final int value = 10;
        final int divide = 2;

        final N number = this.create(value);
        final ExpressionNumber different = number.divide(ExpressionNumber.with(divide), CONTEXT);
        assertNotSame(number, different);
        this.checkValue(value / divide, different);
    }

    // max..............................................................................................................

    @Test
    public final void testMaxMoreBigDecimal() {
        final N number = this.create(2);
        final ExpressionNumber more = number.max(ExpressionNumber.with(BigDecimal.valueOf(1)));
        assertSame(number, more);
    }

    @Test
    public final void testMaxMoreDouble() {
        final N number = this.create(2);
        final ExpressionNumber more = number.max(ExpressionNumber.with(1.0));
        assertSame(number, more);
    }

    @Test
    public final void testMaxGreaterBigDecimal() {
        final int value = 20;

        final N number = this.create(10);
        final ExpressionNumber different = number.max(ExpressionNumber.with(BigDecimal.valueOf(value)));
        assertNotSame(number, different);
        this.checkValue(value, different);
    }

    @Test
    public final void testMaxGreaterDouble() {
        final int value = 20;

        final N number = this.create(10);
        final ExpressionNumber different = number.max(ExpressionNumber.with(value));
        assertNotSame(number, different);
        this.checkValue(value, different);
    }
    
    // min..............................................................................................................

    @Test
    public final void testMinLessBigDecimal() {
        final N number = this.create(1);
        final ExpressionNumber less = number.min(ExpressionNumber.with(BigDecimal.valueOf(2)));
        assertSame(number, less);
    }

    @Test
    public final void testMinLessDouble() {
        final N number = this.create(1);
        final ExpressionNumber less = number.min(ExpressionNumber.with(2.0));
        assertSame(number, less);
    }

    @Test
    public final void testMinGreaterBigDecimal() {
        final int value = 10;

        final N number = this.create(20);
        final ExpressionNumber different = number.min(ExpressionNumber.with(BigDecimal.valueOf(value)));
        assertNotSame(number, different);
        this.checkValue(value, different);
    }

    @Test
    public final void testMinGreaterDouble() {
        final int value = 10;

        final N number = this.create(20);
        final ExpressionNumber different = number.min(ExpressionNumber.with(value));
        assertNotSame(number, different);
        this.checkValue(value, different);
    }

    // modulo..............................................................................................................

    @Test
    public final void testModuloBigDecimal() {
        final int value = 7;
        final int modulo = 3;

        final N number = this.create(value);
        final ExpressionNumber different = number.modulo(ExpressionNumber.with(BigDecimal.valueOf(modulo)), CONTEXT);
        assertNotSame(number, different);
        this.checkValue((value % modulo), different);
    }

    @Test
    public final void testModuloDouble() {
        final int value = 7;
        final int modulo = 3;

        final N number = this.create(value);
        final ExpressionNumber different = number.modulo(ExpressionNumber.with(modulo), CONTEXT);
        assertNotSame(number, different);
        this.checkValue((value % modulo), different);
    }

    // multiply..............................................................................................................

    @Test
    public final void testMultiplyOneBigDecimal() {
        final int value = 1;

        final N number = this.create(value);
        assertSame(number, number.multiply(ExpressionNumber.with(BigDecimal.ONE), CONTEXT));
    }

    @Test
    public final void testMultiplyOneDouble() {
        final int value = 1;

        final N number = this.create(value);
        assertSame(number, number.multiply(ExpressionNumber.with(1), CONTEXT));
    }

    @Test
    public final void testMultiplyBigDecimal() {
        final int value = 7;
        final int multiply = 3;

        final N number = this.create(value);
        final ExpressionNumber different = number.multiply(ExpressionNumber.with(BigDecimal.valueOf(multiply)), CONTEXT);
        assertNotSame(number, different);
        this.checkValue(value * multiply, different);
    }

    @Test
    public final void testMultiplyDouble() {
        final int value = 7;
        final int multiply = 3;

        final N number = this.create(value);
        final ExpressionNumber different = number.multiply(ExpressionNumber.with(multiply), CONTEXT);
        assertNotSame(number, different);
        this.checkValue(value * multiply, different);
    }
    
    // power..............................................................................................................

    @Test
    public final void testPowerZero() {
        final int value = 1;

        final N number = this.create(value);
        assertSame(number, number.power(this.create(0), CONTEXT));
    }

    @Test
    public final void testPowerBigDecimal() {
        final int value = 2;
        final int power = 3;

        final N number = this.create(value);
        final ExpressionNumber different = number.power(ExpressionNumber.with(BigDecimal.valueOf(power)), CONTEXT);
        assertNotSame(number, different);
        this.checkValue((int) Math.pow(value, power), different);
    }

    @Test
    public final void testPowerDouble() {
        final int value = 2;
        final int power = 3;

        final N number = this.create(value);
        final ExpressionNumber different = number.power(ExpressionNumber.with(power), CONTEXT);
        assertNotSame(number, different);
        this.checkValue((int) Math.pow(value, power), different);
    }

    // subtract..............................................................................................................

    @Test
    public final void testSubtractZero() {
        final int value = 1;

        final N number = this.create(value);
        assertSame(number, number.subtract(this.create(0), CONTEXT));
    }

    @Test
    public final void testSubtractBigDecimal() {
        final int value = 5;
        final int subtract = 2;

        final N number = this.create(value);
        final ExpressionNumber different = number.subtract(ExpressionNumber.with(BigDecimal.valueOf(subtract)), CONTEXT);
        assertNotSame(number, different);
        this.checkValue(value - subtract, different);
    }

    @Test
    public final void testSubtractDouble() {
        final int value = 5;
        final int subtract = 2;

        final N number = this.create(value);
        final ExpressionNumber different = number.subtract(ExpressionNumber.with(subtract), CONTEXT);
        assertNotSame(number, different);
        this.checkValue(value - subtract, different);
    }

    // and..............................................................................................................

    @Test
    public final void testAndZero() {
        final int value = 1;

        final N number = this.create(value);
        assertSame(number, number.and(this.create(value)));
    }

    @Test
    public final void testAndBigDecimal() {
        final int value = 7;
        final int and = 5;

        final N number = this.create(value);
        final ExpressionNumber different = number.and(ExpressionNumber.with(BigDecimal.valueOf(and)));
        assertNotSame(number, different);
        this.checkValue(value & and, different);
    }

    @Test
    public final void testAndBigDecimalDecimals() {
        final int value = 7;
        final int and = 5;

        final N number = this.create(value + 0.1);
        final ExpressionNumber different = number.and(ExpressionNumber.with(BigDecimal.valueOf(and + 0.1)));
        assertNotSame(number, different);
        this.checkValue(value & and, different);
    }


    @Test
    public final void testAndDouble() {
        final int value = 7;
        final int and = 5;

        final N number = this.create(value);
        final ExpressionNumber different = number.and(ExpressionNumber.with(and));
        assertNotSame(number, different);
        this.checkValue(value & and, different);
    }

    @Test
    public final void testAndDoubleDecimals() {
        final int value = 7;
        final int and = 5;

        final N number = this.create(value + 0.1);
        final ExpressionNumber different = number.and(ExpressionNumber.with(and + 0.1));
        assertNotSame(number, different);
        this.checkValue(value & and, different);
    }

    // andNot...........................................................................................................

    @Test
    public final void testAndNotMinusOne() {
        final int value = 1;

        final N number = this.create(value);
        this.checkEquals(
                this.create(0),
                number.andNot(this.create(-1))
        );
    }

    @Test
    public final void testAndNotZero() {
        final int value = 1;

        final N number = this.create(value);
        this.checkEquals(
                number,
                number.andNot(this.create(0))
        );
    }

    @Test
    public final void testAndNotBigDecimal() {
        final int value = 7;
        final int andNot = 5;

        final N number = this.create(value);
        final ExpressionNumber different = number.andNot(ExpressionNumber.with(BigDecimal.valueOf(andNot)));
        assertNotSame(number, different);
        this.checkValue(value & ~andNot, different);
    }

    @Test
    public final void testAndNotBigDecimalDecimals() {
        final int value = 7;
        final int andNot = 5;

        final N number = this.create(value + 0.1);
        final ExpressionNumber different = number.andNot(ExpressionNumber.with(BigDecimal.valueOf(andNot + 0.1)));
        assertNotSame(number, different);
        this.checkValue(value & ~andNot, different);
    }


    @Test
    public final void testAndNotDouble() {
        final int value = 7;
        final int andNot = 5;

        final N number = this.create(value);
        final ExpressionNumber different = number.andNot(ExpressionNumber.with(andNot));
        assertNotSame(number, different);
        this.checkValue(value & ~andNot, different);
    }

    @Test
    public final void testAndNotDoubleDecimals() {
        final int value = 7;
        final int andNot = 5;

        final N number = this.create(value + 0.1);
        final ExpressionNumber different = number.andNot(ExpressionNumber.with(andNot + 0.1));
        assertNotSame(number, different);
        this.checkValue(value & ~andNot, different);
    }

    // or..............................................................................................................

    @Test
    public final void testOrSame() {
        final int value = 1;

        final N number = this.create(value);
        assertSame(number, number.or(this.create(value)));
    }

    @Test
    public final void testOrBigDecimal() {
        final int value = 9;
        final int or = 5;

        final N number = this.create(value);
        final ExpressionNumber different = number.or(ExpressionNumber.with(BigDecimal.valueOf(or)));
        assertNotSame(number, different);
        this.checkValue(value | or, different);
    }

    @Test
    public final void testOrDouble() {
        final int value = 9;
        final int or = 5;

        final N number = this.create(value);
        final ExpressionNumber different = number.or(ExpressionNumber.with(or));
        assertNotSame(number, different);
        this.checkValue(value | or, different);
    }

    // xor..............................................................................................................

    @Test
    public final void testXorSame() {
        final int value = 1;

        final N number = this.create(value);
        assertSame(number, number.xor(this.create(0)));
    }

    @Test
    public final void testXorBigDecimal() {
        final int value = 7;
        final int xor = 5;

        final N number = this.create(value);
        final ExpressionNumber different = number.xor(ExpressionNumber.with(BigDecimal.valueOf(xor)));
        assertNotSame(number, different);
        this.checkValue(value ^ xor, different);
    }

    @Test
    public final void testXorDouble() {
        final int value = 7;
        final int xor = 5;

        final N number = this.create(value);
        final ExpressionNumber different = number.xor(ExpressionNumber.with(xor));
        assertNotSame(number, different);
        this.checkValue(value ^ xor, different);
    }

    // equals..............................................................................................................

    @Test
    public final void testEqualsSame() {
        this.checkEquals(1 == 1, this.create(1).equals(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testEqualsSameBigDecimal() {
        this.checkEquals(1 == 1, this.create(1).equals(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testEqualsSameDouble() {
        this.checkEquals(1 == 1, this.create(1).equals(ExpressionNumber.with(1.0)));
    }

    @Test
    public final void testEqualsDifferent() {
        this.checkEquals(1 == 2, this.create(1).equals(ExpressionNumber.with(BigDecimal.valueOf(2))));
    }

    @Test
    public final void testEqualsDifferentBigDecimal() {
        this.checkEquals(1 == 2, this.create(1).equals(ExpressionNumber.with(BigDecimal.valueOf(2))));
    }

    @Test
    public final void testEqualsDifferentDouble() {
        this.checkEquals(1 == 2, this.create(1).equals(ExpressionNumber.with(2.0)));
    }

    // greaterThan..............................................................................................................

    @Test
    public final void testGreaterThanLess() {
        this.checkEquals(2 > 1, this.create(2).greaterThan(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testGreaterThanLessBigDecimal() {
        this.checkEquals(2 > 1, this.create(2).greaterThan(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testGreaterThanLessDouble() {
        this.checkEquals(2 > 1, this.create(2).greaterThan(ExpressionNumber.with(1.0)));
    }

    @Test
    public final void testGreaterThanSame() {
        this.checkEquals(1 > 1, this.create(1).greaterThan(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testGreaterThanSameBigDecimal() {
        this.checkEquals(1 > 1, this.create(1).greaterThan(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testGreaterThanSameDouble() {
        this.checkEquals(1 > 1, this.create(1).greaterThan(ExpressionNumber.with(1.0)));
    }

    @Test
    public final void testGreaterThanGreater() {
        this.checkEquals(2 > 1, this.create(2).greaterThan(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testGreaterThanGreaterBigDecimal() {
        this.checkEquals(2 > 1, this.create(2).greaterThan(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testGreaterThanGreaterDouble() {
        this.checkEquals(2 > 1, this.create(2).greaterThan(ExpressionNumber.with(1.0)));
    }

    // greaterThanEquals..............................................................................................................

    @Test
    public final void testGreaterThanEqualsLess() {
        this.checkEquals(2 >= 1, this.create(2).greaterThanEquals(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testGreaterThanEqualsLessBigDecimal() {
        this.checkEquals(2 >= 1, this.create(2).greaterThanEquals(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testGreaterThanEqualsLessDouble() {
        this.checkEquals(2 >= 1, this.create(2).greaterThanEquals(ExpressionNumber.with(1.0)));
    }

    @Test
    public final void testGreaterThanEqualsSame() {
        this.checkEquals(1 >= 1, this.create(1).greaterThanEquals(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testGreaterThanEqualsSameBigDecimal() {
        this.checkEquals(1 >= 1, this.create(1).greaterThanEquals(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testGreaterThanEqualsSameDouble() {
        this.checkEquals(1 >= 1, this.create(1).greaterThanEquals(ExpressionNumber.with(1.0)));
    }

    @Test
    public final void testGreaterThanEqualsGreater() {
        this.checkEquals(2 >= 1, this.create(2).greaterThanEquals(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testGreaterThanEqualsGreaterBigDecimal() {
        this.checkEquals(2 >= 1, this.create(2).greaterThanEquals(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testGreaterThanEqualsGreaterDouble() {
        this.checkEquals(2 >= 1, this.create(2).greaterThanEquals(ExpressionNumber.with(1.0)));
    }

    // lessThan..............................................................................................................

    @Test
    public final void testLessThanLess() {
        this.checkEquals(2 < 1, this.create(2).lessThan(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testLessThanLessBigDecimal() {
        this.checkEquals(2 < 1, this.create(2).lessThan(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testLessThanLessDouble() {
        this.checkEquals(2 < 1, this.create(2).lessThan(ExpressionNumber.with(1.0)));
    }

    @Test
    public final void testLessThanSame() {
        this.checkEquals(1 < 1, this.create(1).lessThan(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testLessThanSameBigDecimal() {
        this.checkEquals(1 < 1, this.create(1).lessThan(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testLessThanSameDouble() {
        this.checkEquals(1 < 1, this.create(1).lessThan(ExpressionNumber.with(1.0)));
    }

    @Test
    public final void testLessThanGreater() {
        this.checkEquals(2 < 1, this.create(2).lessThan(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testLessThanGreaterBigDecimal() {
        this.checkEquals(2 < 1, this.create(2).lessThan(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testLessThanGreaterDouble() {
        this.checkEquals(2 < 1, this.create(2).lessThan(ExpressionNumber.with(1.0)));
    }

    // lessThanEquals..............................................................................................................

    @Test
    public final void testLessThanEqualsLess() {
        this.checkEquals(2 <= 1, this.create(2).lessThanEquals(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testLessThanEqualsLessBigDecimal() {
        this.checkEquals(2 <= 1, this.create(2).lessThanEquals(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testLessThanEqualsLessDouble() {
        this.checkEquals(2 <= 1, this.create(2).lessThanEquals(ExpressionNumber.with(1.0)));
    }

    @Test
    public final void testLessThanEqualsSame() {
        this.checkEquals(1 <= 1, this.create(1).lessThanEquals(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testLessThanEqualsSameBigDecimal() {
        this.checkEquals(1 <= 1, this.create(1).lessThanEquals(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testLessThanEqualsSameDouble() {
        this.checkEquals(1 <= 1, this.create(1).lessThanEquals(ExpressionNumber.with(1.0)));
    }

    @Test
    public final void testLessThanEqualsGreater() {
        this.checkEquals(2 <= 1, this.create(2).lessThanEquals(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testLessThanEqualsGreaterBigDecimal() {
        this.checkEquals(2 <= 1, this.create(2).lessThanEquals(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testLessThanEqualsGreaterDouble() {
        this.checkEquals(2 <= 1, this.create(2).lessThanEquals(ExpressionNumber.with(1.0)));
    }
    // notEquals..............................................................................................................

    @Test
    public final void testNotEqualsSame() {
        this.checkEquals(1 != 1, this.create(1).notEquals(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testNotEqualsSameBigDecimal() {
        this.checkEquals(1 != 1, this.create(1).notEquals(ExpressionNumber.with(BigDecimal.ONE)));
    }

    @Test
    public final void testNotEqualsSameDouble() {
        this.checkEquals(1 != 1, this.create(1).notEquals(ExpressionNumber.with(1.0)));
    }

    @Test
    public final void testNotEqualsDifferent() {
        this.checkEquals(1 != 2, this.create(1).notEquals(ExpressionNumber.with(BigDecimal.valueOf(2))));
    }

    @Test
    public final void testNotEqualsDifferentBigDecimal() {
        this.checkEquals(1 != 2, this.create(1).notEquals(ExpressionNumber.with(BigDecimal.valueOf(2))));
    }

    @Test
    public final void testNotEqualsDifferentDouble() {
        this.checkEquals(1 != 2, this.create(1).notEquals(ExpressionNumber.with(2.0)));
    }

    // byteValue........................................................................................................

    @Test
    public final void testByteValue() {
        final byte value = 1;
        this.checkEquals(
                value,
                this.create(value).byteValue()
        );
    }

    @Test
    public final void testByteValueDecimal() {
        this.checkEquals(
                (byte) 1,
                this.create(1.5).byteValue()
        );
    }

    // byteValueExact..................................................................................................

    @Test
    public final void testByteValueExact() {
        final byte value = 1;
        this.checkEquals(
                value,
                this.create(value).byteValueExact()
        );
    }

    @Test
    public final void testByteValueExactFails() {
        assertThrows(
                ArithmeticException.class,
                () -> this.create(1.5).byteValueExact()
        );
    }

    @Test
    public final void testByteValueExactFails2() {
        assertThrows(
                ArithmeticException.class,
                () -> this.create(Short.MAX_VALUE).byteValueExact()
        );
    }

    // shortValue........................................................................................................

    @Test
    public final void testShortValue() {
        final short value = 1;
        this.checkEquals(
                value,
                this.create(value).shortValue()
        );
    }

    @Test
    public final void testShortValueDecimal() {
        this.checkEquals(
                (short) 1,
                this.create(1.5).shortValue()
        );
    }

    // shortValueExact..................................................................................................

    @Test
    public final void testShortValueExact() {
        final short value = 1;
        this.checkEquals(
                value,
                this.create(value).shortValueExact()
        );
    }

    @Test
    public final void testShortValueExactFails() {
        assertThrows(
                ArithmeticException.class,
                () -> this.create(1.5).shortValueExact()
        );
    }

    @Test
    public final void testShortValueExactFails2() {
        assertThrows(
                ArithmeticException.class,
                () -> this.create(Integer.MAX_VALUE).shortValueExact()
        );
    }

    // intValue........................................................................................................

    @Test
    public final void testIntValue() {
        final int value = 1;
        this.checkEquals(
                value,
                this.create(value).intValue()
        );
    }

    @Test
    public final void testIntValueDecimal() {
        this.checkEquals(
                (int) 1,
                this.create(1.5).intValue()
        );
    }

    // intValueExact....................................................................................................

    @Test
    public final void testIntValueExact() {
        final int value = 1;
        this.checkEquals(
                value,
                this.create(value).intValueExact()
        );
    }

    @Test
    public final void testIntValueExactFails() {
        assertThrows(
                ArithmeticException.class,
                () -> this.create(1.5).intValueExact()
        );
    }

    @Test
    public final void testIntValueExactFails2() {
        assertThrows(
                ArithmeticException.class,
                () -> this.create(Long.MAX_VALUE).intValueExact()
        );
    }

    // longValue........................................................................................................

    @Test
    public final void testLongValue() {
        final long value = 1;
        this.checkEquals(
                value,
                this.create(value).longValue()
        );
    }

    @Test
    public final void testLongValueDecimal() {
        this.checkEquals(
                (long) 1,
                this.create(1.5).longValue()
        );
    }

    // longValueExact...................................................................................................

    @Test
    public final void testLongValueExact() {
        final int value = 1;
        this.checkEquals(
                (long) value,
                this.create(value).longValueExact()
        );
    }

    @Test
    public final void testLongValueExactFails() {
        assertThrows(
                ArithmeticException.class,
                () -> this.create(1.5).longValueExact()
        );
    }

    @Test
    public final void testLongValueExactFails2() {
        assertThrows(
                ArithmeticException.class,
                () -> this.create(Double.MAX_VALUE).longValueExact()
        );
    }

    // floatValue.......................................................................................................

    @Test
    public final void testFloatValue() {
        final int value = 1;
        this.checkEquals(
                (float) value,
                this.create(value).floatValue()
        );
    }

    // doubleValue.......................................................................................................

    @Test
    public final void testDoubleValue() {
        final int value = 1;
        this.checkEquals(
                (double) value,
                this.create(value).doubleValue()
        );
    }

    // bigInteger.......................................................................................................

    @Test
    public final void testBigInteger() {
        final int value = 1;
        this.checkEquals(BigInteger.valueOf(value), this.create(value).bigInteger());
    }


    // bigIntegerExact.......................................................................................................

    @Test
    public final void testBigIntegerExact() {
        final int value = 1;
        this.checkEquals(
                BigInteger.valueOf(value),
                this.create(value).bigIntegerExact()
        );
    }

    @Test
    public final void testBigIntegerExactDecimalFails() {
        assertThrows(
                ArithmeticException.class,
                () -> this.create(1.5)
                        .bigIntegerExact()
        );
    }

    // bigDecimal.......................................................................................................

    @Test
    public final void testBigDecimal() {
        final int value = 1;
        this.checkEquals(
                BigDecimal.valueOf(value),
                this.create(value)
                        .bigDecimal()
                        .setScale(0, RoundingMode.HALF_UP)
        );
    }

    // equals...........................................................................................................

    @Test
    public final void testDifferent() {
        this.checkNotEquals(this.create(123));
    }

    @Test
    public final void testSameValueExpressionNumberBigDecimal() {
        final int value = 1;
        this.checkNotEquals(ExpressionNumberDouble.withDouble(value), ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.valueOf(value)));
    }

    // toString...........................................................................................................

    @Test
    public final void testToStringTrailingZero() {
        this.toStringAndCheck2(1, "1");
    }

    @Test
    public final void testToStringWithoutTrailingZero() {
        this.toStringAndCheck2(1.5, "1.5");
    }

    @Test
    public final void testToStringWithoutTrailingZero2() {
        this.toStringAndCheck2(1.125, "1.125");
    }

    @Test
    public final void testToStringScientific10() {
        this.toStringAndCheck2(1.1E10);
    }

    @Test
    public final void testToStringScientific10b() {
        this.toStringAndCheck2(1.0E10);
    }

    @Test
    public final void testToStringScientific20() {
        this.toStringAndCheck2(1.0E20);
    }

    @Test
    public final void testToStringScientific100() {
        this.toStringAndCheck2(1.0E100);
    }

    abstract void toStringAndCheck2(final double value);

    final void toStringAndCheck2(final double value, final String toString) {
        this.toStringAndCheck(this.create(value), toString);
    }

    // toString with base...............................................................................................

    @Test
    public final void testToStringWithBase2() {
        this.toStringWithBaseAndCheck(
                0xF5,
                2,
                "11110101"
        );
    }

    @Test
    public final void testToStringWithBase8() {
        this.toStringWithBaseAndCheck(
                0777,
                8,
                "777"
        );
    }

    @Test
    public final void testToStringWithBase16() {
        this.toStringWithBaseAndCheck(
                0x1234FEDC,
                16,
                "1234fedc"
        );
    }

    private void toStringWithBaseAndCheck(final double number,
                                          final double base,
                                          final String toString) {
        this.checkEquals(
                toString,
                this.create(number).toStringWithBase(this.create(base)),
                () -> "toStringWithBase " + number + " base=" + base
        );
    }

    // helper...........................................................................................................

    private N create() {
        return this.create(0);
    }

    abstract N create(final double value);

    final void checkValue(final double value, final ExpressionNumber number) {
        this.checkEquals(this instanceof ExpressionNumberBigDecimalTest, number.isBigDecimal(), "result is wrong kind");
        this.checkEquals(this instanceof ExpressionNumberDoubleTest, number.isDouble(), "result is wrong kind");

        try {
            this.checkValue0(value, (N) number);
        } catch (final ClassCastException again) {
            this.checkEquals(value, number.doubleValue());
        }
    }

    abstract void checkValue0(final double value, final N number);

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // HashEquals......................................................................................................

    @Override
    public ExpressionNumber createComparable() {
        return this.create();
    }

    // HashEquals......................................................................................................

    @Override
    public final N createObject() {
        return this.create();
    }

    // isXXX............................................................................................................

    @Override
    public final N createIsMethodObject() {
        return this.create();
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return ExpressionNumber.class.getSimpleName();
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return "";
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (s) -> false;
    }
}
