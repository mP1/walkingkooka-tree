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

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ExpressionNumberReducerBinaryExpressionNumberVisitorComparisonTestCase<V extends ExpressionNumberReducerBinaryExpressionNumberVisitorComparison>
        extends ExpressionNumberReducerBinaryExpressionNumberVisitorTestCase<V> {

    ExpressionNumberReducerBinaryExpressionNumberVisitorComparisonTestCase() {
        super();
    }

    // BigDecimal.......................................................................................................

    @Test
    public final void testBigDecimalBigDecimal() {
        this.bigDecimalLeftFunctionAndCheck(new BigDecimal(this.right()), true);
    }

    @Test
    public final void testBigDecimalBigInteger() {
        this.bigDecimalLeftFunctionAndCheck(BigInteger.valueOf(this.right()), true);
    }

    @Test
    public final void testBigDecimalDouble() {
        this.bigDecimalLeftFunctionAndCheck(Double.valueOf(this.right()), true);
    }

    @Test
    public final void testBigDecimalLong() {
        this.bigDecimalLeftFunctionAndCheck(Long.valueOf(this.right()), true);
    }

    @Test
    public final void testBigDecimalNumber() {
        this.bigDecimalLeftFunctionAndCheck(this.right(), true);
    }

    @Test
    public final void testBigDecimalBigDecimal2() {
        this.bigDecimalLeftFunctionAndCheck(new BigDecimal(this.right2()), false);
    }

    @Test
    public final void testBigDecimalBigInteger2() {
        this.bigDecimalLeftFunctionAndCheck(BigInteger.valueOf(this.right2()), false);
    }

    @Test
    public final void testBigDecimalDouble2() {
        this.bigDecimalLeftFunctionAndCheck(Double.valueOf(this.right2()), false);
    }

    @Test
    public final void testBigDecimalLong2() {
        this.bigDecimalLeftFunctionAndCheck(Long.valueOf(this.right2()), false);
    }

    @Test
    public final void testBigDecimalNumber2() {
        this.bigDecimalLeftFunctionAndCheck(this.right2(), false);
    }

    private void bigDecimalLeftFunctionAndCheck(final Number right, final boolean expected) {
        this.functionAndCheck(BigDecimal.valueOf(this.left()), right, expected);
    }

    // BigInteger........................................................................................................

    @Test
    public final void testBigIntegerBigDecimal() {
        this.bigIntegerLeftFunctionAndCheck(new BigDecimal(this.right()), true);
    }

    @Test
    public final void testBigIntegerBigInteger() {
        this.bigIntegerLeftFunctionAndCheck(BigInteger.valueOf(this.right()), true);
    }

    @Test
    public final void testBigIntegerDouble() {
        this.bigIntegerLeftFunctionAndCheck(Double.valueOf(this.right()), true);
    }

    @Test
    public final void testBigIntegerLong() {
        this.bigIntegerLeftFunctionAndCheck(Long.valueOf(this.right()), true);
    }

    @Test
    public final void testBigIntegerNumber() {
        this.bigIntegerLeftFunctionAndCheck(this.right(), true);
    }

    @Test
    public final void testBigIntegerBigDecimal2() {
        this.bigIntegerLeftFunctionAndCheck(new BigDecimal(this.right2()), false);
    }

    @Test
    public final void testBigIntegerBigInteger2() {
        this.bigIntegerLeftFunctionAndCheck(BigInteger.valueOf(this.right2()), false);
    }

    @Test
    public final void testBigIntegerDouble2() {
        this.bigIntegerLeftFunctionAndCheck(Double.valueOf(this.right2()), false);
    }

    @Test
    public final void testBigIntegerLong2() {
        this.bigIntegerLeftFunctionAndCheck(Long.valueOf(this.right2()), false);
    }

    @Test
    public final void testBigIntegerNumber2() {
        this.bigIntegerLeftFunctionAndCheck(this.right2(), false);
    }
    
    private void bigIntegerLeftFunctionAndCheck(final Number right, final boolean expected) {
        this.functionAndCheck(BigInteger.valueOf(this.left()), right, expected);
    }

    // Double...........................................................................................................

    @Test
    public final void testDoubleBigDecimal() {
        this.doubleLeftFunctionAndCheck(new BigDecimal(this.right()), true);
    }

    @Test
    public final void testDoubleBigInteger() {
        this.doubleLeftFunctionAndCheck(BigInteger.valueOf(this.right()), true);
    }

    @Test
    public final void testDoubleDouble() {
        this.doubleLeftFunctionAndCheck(Double.valueOf(this.right()), true);
    }

    @Test
    public final void testDoubleLong() {
        this.doubleLeftFunctionAndCheck(Long.valueOf(this.right()), true);
    }

    @Test
    public final void testDoubleNumber() {
        this.doubleLeftFunctionAndCheck(this.right(), true);
    }

    @Test
    public final void testDoubleBigDecimal2() {
        this.doubleLeftFunctionAndCheck(new BigDecimal(this.right2()), false);
    }

    @Test
    public final void testDoubleBigInteger2() {
        this.doubleLeftFunctionAndCheck(BigInteger.valueOf(this.right2()), false);
    }

    @Test
    public final void testDoubleDouble2() {
        this.doubleLeftFunctionAndCheck(Double.valueOf(this.right2()), false);
    }

    @Test
    public final void testDoubleLong2() {
        this.doubleLeftFunctionAndCheck(Long.valueOf(this.right2()), false);
    }

    @Test
    public final void testDoubleNumber2() {
        this.doubleLeftFunctionAndCheck(this.right2(), false);
    }


    private void doubleLeftFunctionAndCheck(final Number right, final boolean expected) {
        this.functionAndCheck(Double.valueOf(this.left()), right, expected);
    }

    // Long..............................................................................................................

    @Test
    public final void testLongBigDecimal() {
        this.longLeftFunctionAndCheck(new BigDecimal(this.right()), true);
    }

    @Test
    public final void testLongBigInteger() {
        this.longLeftFunctionAndCheck(BigInteger.valueOf(this.right()), true);
    }

    @Test
    public final void testLongDouble() {
        this.longLeftFunctionAndCheck(Double.valueOf(this.right()), true);
    }

    @Test
    public final void testLongLong() {
        this.longLeftFunctionAndCheck(Long.valueOf(this.right()), true);
    }

    @Test
    public final void testLongNumber() {
        this.longLeftFunctionAndCheck(this.right(), true);
    }

    @Test
    public final void testLongBigDecimal2() {
        this.longLeftFunctionAndCheck(new BigDecimal(this.right2()), false);
    }

    @Test
    public final void testLongBigInteger2() {
        this.longLeftFunctionAndCheck(BigInteger.valueOf(this.right2()), false);
    }

    @Test
    public final void testLongDouble2() {
        this.longLeftFunctionAndCheck(Double.valueOf(this.right2()), false);
    }

    @Test
    public final void testLongLong2() {
        this.longLeftFunctionAndCheck(Long.valueOf(this.right2()), false);
    }

    @Test
    public final void testLongNumber2() {
        this.longLeftFunctionAndCheck(this.right2(), false);
    }

    private void longLeftFunctionAndCheck(final Number right, final boolean expected) {
        this.functionAndCheck(Double.valueOf(this.left()), right, expected);
    }

    // helpers..........................................................................................................

    final void functionAndCheck(final Number left, final Number right, final boolean expected) {
        assertEquals(expected, this.function(left, right, CONTEXT));
    }

    abstract boolean function(final Number left, final Number right, final ExpressionNumberReducerContext context);

    /**
     * This value must fail the comparison.
     */
    abstract int right2();
}
