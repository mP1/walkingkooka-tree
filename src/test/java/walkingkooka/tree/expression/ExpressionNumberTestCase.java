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
import walkingkooka.HashCodeEqualsDefinedTesting;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.IsMethodTesting;
import walkingkooka.reflect.JavaVisibility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ExpressionNumberTestCase<N extends ExpressionNumber> implements ClassTesting<N>,
        HashCodeEqualsDefinedTesting2<N>,
        IsMethodTesting<N>,
        ToStringTesting<N> {

    final static ExpressionNumberContext CONTEXT = new ExpressionNumberContext() {
        @Override
        public MathContext mathContext() {
            return MathContext.DECIMAL32;
        }
    };

    ExpressionNumberTestCase() {
        super();
    }

    // abs..............................................................................................................

    @Test
    public final void testAbsPositive() {
        final ExpressionNumber number = this.create(1);
        assertSame(number, number.abs());
    }

    @Test
    public final void testAbsNegative() {
        final N number = this.create(-1);
        final ExpressionNumber different = number.abs();
        assertNotSame(number, different);
        this.checkValue(1, different);
    }

    // negate...........................................................................................................

    @Test
    public final void testNegatePositive() {
        final N number = this.create(+1);
        final ExpressionNumber different = number.negate();
        assertNotSame(number, different);
        this.checkValue(-1, different);
    }

    @Test
    public final void testNegateZero() {
        final ExpressionNumber number = this.create(0);
        assertSame(number, number.negate());
    }

    @Test
    public final void testNegateNegative() {
        final N number = this.create(-1);
        final ExpressionNumber different = number.negate();
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
        this.checkValue(~ value, different);
    }

    @Test
    public final void testNotZero() {
        final int value = 0;

        final N number = this.create(value);
        final ExpressionNumber different = number.not();
        assertNotSame(number, different);
        this.checkValue(~ value, different);
    }

    @Test
    public final void testNotNegative() {
        final int value = -1;

        final N number = this.create(value);
        final ExpressionNumber different = number.not();
        assertNotSame(number, different);
        this.checkValue(~ value, different);
    }

    // add..............................................................................................................

    @Test
    public final void testAddZero() {
        final int value = 1;

        final N number = this.create(value);
        assertSame(number, number.add(this.create(0)));
    }

    @Test
    public final void testAddBigDecimal() {
        final int value = 1;
        final int add = 2;

        final N number = this.create(value);
        final ExpressionNumber different = number.add(ExpressionNumber.with(BigDecimal.valueOf(add), CONTEXT));
        assertNotSame(number, different);
        this.checkValue(value + add, different);
    }

    @Test
    public final void testAddDouble() {
        final int value = 1;
        final int add = 2;

        final N number = this.create(value);
        final ExpressionNumber different = number.add(ExpressionNumber.with((double)add, CONTEXT));
        assertNotSame(number, different);
        this.checkValue(value + add, different);
    }

    // divide..............................................................................................................

    @Test
    public final void testDivideZeroFails() {
        final int value = 1;

        final N number = this.create(value);
        assertThrows(ArithmeticException.class, () -> number.divide(this.create(0)));
    }

    @Test
    public final void testDivideOne() {
        final int value = 1;

        final N number = this.create(value);
        assertSame(number, number.divide(this.create(1)));
    }

    @Test
    public final void testDivideBigDecimal() {
        final int value = 10;
        final int divide = 2;

        final N number = this.create(value);
        final ExpressionNumber different = number.divide(ExpressionNumber.with(BigDecimal.valueOf(divide), CONTEXT));
        assertNotSame(number, different);
        this.checkValue(value / divide, different);
    }

    @Test
    public final void testDivideDouble() {
        final int value = 10;
        final int divide = 2;

        final N number = this.create(value);
        final ExpressionNumber different = number.divide(ExpressionNumber.with((double)divide, CONTEXT));
        assertNotSame(number, different);
        this.checkValue(value / divide, different);
    }

    // power..............................................................................................................

    @Test
    public final void testPowerZero() {
        final int value = 1;

        final N number = this.create(value);
        assertSame(number, number.power(this.create(0)));
    }

    @Test
    public final void testPowerBigDecimal() {
        final int value = 2;
        final int power = 3;

        final N number = this.create(value);
        final ExpressionNumber different = number.power(ExpressionNumber.with(BigDecimal.valueOf(power), CONTEXT));
        assertNotSame(number, different);
        this.checkValue((int)Math.pow(value, power), different);
    }

    @Test
    public final void testPowerDouble() {
        final int value = 2;
        final int power = 3;

        final N number = this.create(value);
        final ExpressionNumber different = number.power(ExpressionNumber.with((double)power, CONTEXT));
        assertNotSame(number, different);
        this.checkValue((int)Math.pow(value, power), different);
    }

    // subtract..............................................................................................................

    @Test
    public final void testSubtractZero() {
        final int value = 1;

        final N number = this.create(value);
        assertSame(number, number.subtract(this.create(0)));
    }

    @Test
    public final void testSubtractBigDecimal() {
        final int value = 5;
        final int subtract = 2;

        final N number = this.create(value);
        final ExpressionNumber different = number.subtract(ExpressionNumber.with(BigDecimal.valueOf(subtract), CONTEXT));
        assertNotSame(number, different);
        this.checkValue(value - subtract, different);
    }

    @Test
    public final void testSubtractDouble() {
        final int value = 5;
        final int subtract = 2;

        final N number = this.create(value);
        final ExpressionNumber different = number.subtract(ExpressionNumber.with((double)subtract, CONTEXT));
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
        final ExpressionNumber different = number.and(ExpressionNumber.with(BigDecimal.valueOf(and), CONTEXT));
        assertNotSame(number, different);
        this.checkValue(value & and, different);
    }

    @Test
    public final void testAndDouble() {
        final int value = 7;
        final int and = 5;

        final N number = this.create(value);
        final ExpressionNumber different = number.and(ExpressionNumber.with((double)and, CONTEXT));
        assertNotSame(number, different);
        this.checkValue(value & and, different);
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
        final ExpressionNumber different = number.or(ExpressionNumber.with(BigDecimal.valueOf(or), CONTEXT));
        assertNotSame(number, different);
        this.checkValue(value | or, different);
    }

    @Test
    public final void testOrDouble() {
        final int value = 9;
        final int or = 5;

        final N number = this.create(value);
        final ExpressionNumber different = number.or(ExpressionNumber.with((double)or, CONTEXT));
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
        final ExpressionNumber different = number.xor(ExpressionNumber.with(BigDecimal.valueOf(xor), CONTEXT));
        assertNotSame(number, different);
        this.checkValue(value ^ xor, different);
    }

    @Test
    public final void testXorDouble() {
        final int value = 7;
        final int xor = 5;

        final N number = this.create(value);
        final ExpressionNumber different = number.xor(ExpressionNumber.with((double)xor, CONTEXT));
        assertNotSame(number, different);
        this.checkValue(value ^ xor, different);
    }

    // equals..............................................................................................................

    @Test
    public final void testEqualsSame() {
        assertEquals(1 == 1, this.create(1).equals(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testEqualsSameBigDecimal() {
        assertEquals(1 == 1, this.create(1).equals(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testEqualsSameDouble() {
        assertEquals(1 == 1, this.create(1).equals(ExpressionNumber.with(Double.valueOf(1), CONTEXT)));
    }

    @Test
    public final void testEqualsDifferent() {
        assertEquals(1 == 2, this.create(1).equals(ExpressionNumber.with(BigDecimal.valueOf(2), CONTEXT)));
    }

    @Test
    public final void testEqualsDifferentBigDecimal() {
        assertEquals(1 == 2, this.create(1).equals(ExpressionNumber.with(BigDecimal.valueOf(2), CONTEXT)));
    }

    @Test
    public final void testEqualsDifferentDouble() {
        assertEquals(1 == 2, this.create(1).equals(ExpressionNumber.with(Double.valueOf(2), CONTEXT)));
    }

    // greaterThan..............................................................................................................

    @Test
    public final void testGreaterThanLess() {
        assertEquals(2 > 1, this.create(2).greaterThan(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testGreaterThanLessBigDecimal() {
        assertEquals(2 > 1, this.create(2).greaterThan(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testGreaterThanLessDouble() {
        assertEquals(2 > 1, this.create(2).greaterThan(ExpressionNumber.with(Double.valueOf(1), CONTEXT)));
    }

    @Test
    public final void testGreaterThanSame() {
        assertEquals(1 > 1, this.create(1).greaterThan(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testGreaterThanSameBigDecimal() {
        assertEquals(1 > 1, this.create(1).greaterThan(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testGreaterThanSameDouble() {
        assertEquals(1 > 1, this.create(1).greaterThan(ExpressionNumber.with(Double.valueOf(1), CONTEXT)));
    }

    @Test
    public final void testGreaterThanGreater() {
        assertEquals(2 > 1, this.create(2).greaterThan(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testGreaterThanGreaterBigDecimal() {
        assertEquals(2 > 1, this.create(2).greaterThan(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testGreaterThanGreaterDouble() {
        assertEquals(2 > 1, this.create(2).greaterThan(ExpressionNumber.with(Double.valueOf(1), CONTEXT)));
    }

    // greaterThanEquals..............................................................................................................

    @Test
    public final void testGreaterThanEqualsLess() {
        assertEquals(2 >= 1, this.create(2).greaterThanEquals(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testGreaterThanEqualsLessBigDecimal() {
        assertEquals(2 >= 1, this.create(2).greaterThanEquals(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testGreaterThanEqualsLessDouble() {
        assertEquals(2 >= 1, this.create(2).greaterThanEquals(ExpressionNumber.with(Double.valueOf(1), CONTEXT)));
    }

    @Test
    public final void testGreaterThanEqualsSame() {
        assertEquals(1 >= 1, this.create(1).greaterThanEquals(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testGreaterThanEqualsSameBigDecimal() {
        assertEquals(1 >= 1, this.create(1).greaterThanEquals(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testGreaterThanEqualsSameDouble() {
        assertEquals(1 >= 1, this.create(1).greaterThanEquals(ExpressionNumber.with(Double.valueOf(1), CONTEXT)));
    }

    @Test
    public final void testGreaterThanEqualsGreater() {
        assertEquals(2 >= 1, this.create(2).greaterThanEquals(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testGreaterThanEqualsGreaterBigDecimal() {
        assertEquals(2 >= 1, this.create(2).greaterThanEquals(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testGreaterThanEqualsGreaterDouble() {
        assertEquals(2 >= 1, this.create(2).greaterThanEquals(ExpressionNumber.with(Double.valueOf(1), CONTEXT)));
    }

    // lessThan..............................................................................................................

    @Test
    public final void testLessThanLess() {
        assertEquals(2 < 1, this.create(2).lessThan(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testLessThanLessBigDecimal() {
        assertEquals(2 < 1, this.create(2).lessThan(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testLessThanLessDouble() {
        assertEquals(2 < 1, this.create(2).lessThan(ExpressionNumber.with(Double.valueOf(1), CONTEXT)));
    }

    @Test
    public final void testLessThanSame() {
        assertEquals(1 < 1, this.create(1).lessThan(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testLessThanSameBigDecimal() {
        assertEquals(1 < 1, this.create(1).lessThan(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testLessThanSameDouble() {
        assertEquals(1 < 1, this.create(1).lessThan(ExpressionNumber.with(Double.valueOf(1), CONTEXT)));
    }

    @Test
    public final void testLessThanGreater() {
        assertEquals(2 < 1, this.create(2).lessThan(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testLessThanGreaterBigDecimal() {
        assertEquals(2 < 1, this.create(2).lessThan(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testLessThanGreaterDouble() {
        assertEquals(2 < 1, this.create(2).lessThan(ExpressionNumber.with(Double.valueOf(1), CONTEXT)));
    }

    // lessThanEquals..............................................................................................................

    @Test
    public final void testLessThanEqualsLess() {
        assertEquals(2 <= 1, this.create(2).lessThanEquals(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testLessThanEqualsLessBigDecimal() {
        assertEquals(2 <= 1, this.create(2).lessThanEquals(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testLessThanEqualsLessDouble() {
        assertEquals(2 <= 1, this.create(2).lessThanEquals(ExpressionNumber.with(Double.valueOf(1), CONTEXT)));
    }

    @Test
    public final void testLessThanEqualsSame() {
        assertEquals(1 <= 1, this.create(1).lessThanEquals(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testLessThanEqualsSameBigDecimal() {
        assertEquals(1 <= 1, this.create(1).lessThanEquals(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testLessThanEqualsSameDouble() {
        assertEquals(1 <= 1, this.create(1).lessThanEquals(ExpressionNumber.with(Double.valueOf(1), CONTEXT)));
    }

    @Test
    public final void testLessThanEqualsGreater() {
        assertEquals(2 <= 1, this.create(2).lessThanEquals(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testLessThanEqualsGreaterBigDecimal() {
        assertEquals(2 <= 1, this.create(2).lessThanEquals(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testLessThanEqualsGreaterDouble() {
        assertEquals(2 <= 1, this.create(2).lessThanEquals(ExpressionNumber.with(Double.valueOf(1), CONTEXT)));
    }
    // notEquals..............................................................................................................

    @Test
    public final void testNotEqualsSame() {
        assertEquals(1 != 1, this.create(1).notEquals(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testNotEqualsSameBigDecimal() {
        assertEquals(1 != 1, this.create(1).notEquals(ExpressionNumber.with(BigDecimal.ONE, CONTEXT)));
    }

    @Test
    public final void testNotEqualsSameDouble() {
        assertEquals(1 != 1, this.create(1).notEquals(ExpressionNumber.with(Double.valueOf(1), CONTEXT)));
    }

    @Test
    public final void testNotEqualsDifferent() {
        assertEquals(1 != 2, this.create(1).notEquals(ExpressionNumber.with(BigDecimal.valueOf(2), CONTEXT)));
    }

    @Test
    public final void testNotEqualsDifferentBigDecimal() {
        assertEquals(1 != 2, this.create(1).notEquals(ExpressionNumber.with(BigDecimal.valueOf(2), CONTEXT)));
    }

    @Test
    public final void testNotEqualsDifferentDouble() {
        assertEquals(1 != 2, this.create(1).notEquals(ExpressionNumber.with(Double.valueOf(2), CONTEXT)));
    }
    
    // byteValue........................................................................................................

    @Test
    public final void testByteValue() {
        final byte value = 1;
        assertEquals(value, this.create(value).byteValue());
    }

    @Test
    public final void testByteValueFails() {
        assertThrows(ArithmeticException.class, () -> this.create(1.5).byteValue());
    }

    @Test
    public final void testByteValueFails2() {
        assertThrows(ArithmeticException.class, () -> this.create(Short.MAX_VALUE).byteValue());
    }

    // shortValue.......................................................................................................

    @Test
    public final void testShortValue() {
        final short value = 1;
        assertEquals(value, this.create(value).shortValue());
    }

    @Test
    public final void testShortValueFails() {
        assertThrows(ArithmeticException.class, () -> this.create(1.5).shortValue());
    }

    @Test
    public final void testShortValueFails2() {
        assertThrows(ArithmeticException.class, () -> this.create(Integer.MAX_VALUE).shortValue());
    }

    // intValue.......................................................................................................

    @Test
    public final void testIntValue() {
        final int value = 1;
        assertEquals(value, this.create(value).intValue());
    }

    @Test
    public final void testIntValueFails() {
        assertThrows(ArithmeticException.class, () -> this.create(1.5).intValue());
    }

    @Test
    public final void testIntValueFails2() {
        assertThrows(ArithmeticException.class, () -> this.create(Long.MAX_VALUE).intValue());
    }

    // longValue.......................................................................................................

    @Test
    public final void testLongValue() {
        final int value = 1;
        assertEquals(value, this.create(value).longValue());
    }

    @Test
    public final void testLongValueFails() {
        assertThrows(ArithmeticException.class, () -> this.create(1.5).longValue());
    }

    @Test
    public final void testLongValueFails2() {
        assertThrows(ArithmeticException.class, () -> this.create(Double.MAX_VALUE).longValue());
    }

    // floatValue.......................................................................................................

    @Test
    public final void testFloatValue() {
        final int value = 1;
        assertEquals(value, this.create(value).floatValue());
    }

    // doubleValue.......................................................................................................

    @Test
    public final void testDoubleValue() {
        final int value = 1;
        assertEquals(value, this.create(value).doubleValue());
    }

    // bigInteger.......................................................................................................

    @Test
    public final void testBigInteger() {
        final int value = 1;
        assertEquals(BigInteger.valueOf(value), this.create(value).bigInteger());
    }

    // bigDecimal.......................................................................................................

    @Test
    public final void testBigDecimal() {
        final int value = 1;
        assertEquals(BigDecimal.valueOf(value), this.create(value).bigDecimal().setScale(0));
    }
    
    // context..........................................................................................................

    @Test
    public final void testContext() {
        assertSame(CONTEXT, this.create().context());
    }

    @Test
    public final void testSetContextNullFails() {
        assertThrows(NullPointerException.class, () -> this.create().setContext(null));
    }

    @Test
    public final void testSetContextSame() {
        final N number = this.create();
        assertSame(number, number.setContext(CONTEXT));
    }

    @Test
    public final void testSetContextDifferent() {
        final N number = this.create();
        final ExpressionNumberContext differentContext = new FakeExpressionNumberContext();
        final ExpressionNumber different = number.setContext(differentContext);
        assertSame(differentContext, different.context());
    }

    // equals...........................................................................................................

    @Test
    public final void testDifferent() {
        this.checkNotEquals(this.create(123));
    }

    @Test
    public final void testSameValueExpressionNumberBigDecimal() {
        final int value = 1;
        this.checkNotEquals(ExpressionNumberDouble.withDouble(value, CONTEXT), ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.valueOf(value), CONTEXT));
    }

    // helper...........................................................................................................

    private N create() {
        return this.create(0);
    }
    
    abstract N create(final double value);

    final void checkValue(final double value, final ExpressionNumber number) {
        try {
            this.checkValue0(value, (N) number);
        } catch (final ClassCastException again) {
            assertEquals(value, number.doubleValue());
        }
    }
    abstract void checkValue0(final double value, final N number);

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
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
