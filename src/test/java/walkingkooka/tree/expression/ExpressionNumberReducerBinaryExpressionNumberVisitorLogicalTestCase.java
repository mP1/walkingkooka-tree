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

public abstract class ExpressionNumberReducerBinaryExpressionNumberVisitorLogicalTestCase<V extends ExpressionNumberReducerBinaryExpressionNumberVisitorLogical>
        extends ExpressionNumberReducerBinaryExpressionNumberVisitorTestCase<V> {

    ExpressionNumberReducerBinaryExpressionNumberVisitorLogicalTestCase() {
        super();
    }

    // Double...........................................................................................................

    @Test
    public final void testDoubleBigDecimal() {
        this.doubleLeftFunctionAndCheck(new BigDecimal(this.right()), BigInteger.valueOf(this.expected()));
    }

    @Test
    public final void testDoubleBigInteger() {
        this.doubleLeftFunctionAndCheck(BigInteger.valueOf(this.right()), BigInteger.valueOf(this.expected()));
    }

    @Test
    public final void testDoubleDouble() {
        this.doubleLeftFunctionAndCheck(Double.valueOf(this.right()), BigInteger.valueOf(this.expected()));
    }

    @Test
    public final void testDoubleLong() {
        this.doubleLeftFunctionAndCheck(Long.valueOf(this.right()), BigInteger.valueOf(this.expected()));
    }

    @Test
    public final void testDoubleNumber() {
        this.doubleLeftFunctionAndCheck(this.right(), BigInteger.valueOf(this.expected()));
    }

    // Long..............................................................................................................

    @Test
    public final void testLongBigDecimal() {
        this.longLeftFunctionAndCheck(new BigDecimal(this.right()), BigInteger.valueOf(this.expected()));
    }

    @Test
    public final void testLongBigInteger() {
        this.longLeftFunctionAndCheck(BigInteger.valueOf(this.right()), BigInteger.valueOf(this.expected()));
    }

    @Test
    public final void testLongDouble() {
        this.longLeftFunctionAndCheck(Double.valueOf(this.right()), BigInteger.valueOf(this.expected()));
    }

    @Test
    public final void testLongNumber() {
        this.longLeftFunctionAndCheck(this.right(), Long.valueOf(this.expected()));
    }

    @Override
    Number visitExpected(final Number number) {
        return number instanceof BigDecimal ?
                toBigInteger((BigDecimal) number) :
                number instanceof Double ?
                        toLong((Double) number) :
                        number;
    }

    private static BigInteger toBigInteger(final BigDecimal value) {
        return value.toBigInteger();
    }

    private static Long toLong(final Double value) {
        return value.longValue();
    }
}
