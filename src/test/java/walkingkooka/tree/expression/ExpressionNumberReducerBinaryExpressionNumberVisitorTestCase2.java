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
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContexts;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ExpressionNumberReducerBinaryExpressionNumberVisitorTestCase2<V extends ExpressionNumberReducerBinaryExpressionNumberVisitor>
        extends ExpressionNumberReducerBinaryExpressionNumberVisitorTestCase<V>
        implements BinaryExpressionNumberVisitorTesting<V>, ToStringTesting<V> {

    final static ExpressionNumberReducerContext CONTEXT = new ExpressionNumberReducerContext() {

        private final ConverterContext converterContext = ConverterContexts.basic(DateTimeContexts.fake(), DecimalNumberContexts.american(this.mathContext()));

        @Override
        public <T> T convertOrFail(final Object value,
                                   final Class<T> target) {
            return Converters.numberNumber()
                    .convertOrFail(value, target, this.converterContext);
        }

        @Override
        public MathContext mathContext() {
            return MathContext.DECIMAL32;
        }
    };

    ExpressionNumberReducerBinaryExpressionNumberVisitorTestCase2() {
        super();
    }

    // BigDecimal.......................................................................................................

    @Test
    public final void testBigDecimalBigDecimal() {
        this.bigDecimalLeftFunctionAndCheck(new BigDecimal(this.right()), new BigDecimal(this.expected()));
    }

    @Test
    public final void testBigDecimalBigInteger() {
        this.bigDecimalLeftFunctionAndCheck(BigInteger.valueOf(this.right()), new BigDecimal(this.expected()));
    }

    @Test
    public final void testBigDecimalDouble() {
        this.bigDecimalLeftFunctionAndCheck(Double.valueOf(this.right()), new BigDecimal(this.expected()));
    }

    @Test
    public final void testBigDecimalLong() {
        this.bigDecimalLeftFunctionAndCheck(Long.valueOf(this.right()), new BigDecimal(this.expected()));
    }

    @Test
    public final void testBigDecimalNumber() {
        this.bigDecimalLeftFunctionAndCheck(this.right(), new BigDecimal(this.expected()));
    }

    private void bigDecimalLeftFunctionAndCheck(final Number right, final Number expected) {
        this.functionAndCheck(BigDecimal.valueOf(this.left()), right, expected);
    }

    // BigInteger........................................................................................................

    @Test
    public final void testBigIntegerBigDecimal() {
        this.bigIntegerLeftFunctionAndCheck(new BigDecimal(this.right()), new BigDecimal(this.expected()));
    }

    @Test
    public final void testBigIntegerBigInteger() {
        this.bigIntegerLeftFunctionAndCheck(BigInteger.valueOf(this.right()), BigInteger.valueOf(this.expected()));
    }

    @Test
    public final void testBigIntegerDouble() {
        this.bigIntegerLeftFunctionAndCheck(Double.valueOf(this.right()), new BigDecimal(this.expected()));
    }

    @Test
    public final void testBigIntegerLong() {
        this.bigIntegerLeftFunctionAndCheck(Long.valueOf(this.right()), BigInteger.valueOf(this.expected()));
    }

    @Test
    public final void testBigIntegerNumber() {
        this.bigIntegerLeftFunctionAndCheck(this.right(), new BigDecimal(this.expected()));
    }

    private void bigIntegerLeftFunctionAndCheck(final Number right, final Number expected) {
        this.functionAndCheck(BigInteger.valueOf(this.left()), right, expected);
    }

    // Double...........................................................................................................

    final void doubleLeftFunctionAndCheck(final Number right, final Number expected) {
        this.functionAndCheck(Double.valueOf(this.left()), right, expected);
    }

    // Long..............................................................................................................

    final void longLeftFunctionAndCheck(final Number right, final Number expected) {
        this.functionAndCheck(Long.valueOf(this.left()), right, expected);
    }

    // Number...........................................................................................................

    @Test
    public final void testNumberNumber() {
        this.functionAndCheck(this.left(), this.right(), new BigDecimal(this.expected()));
    }

    // toString...........................................................................................................

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.createVisitor().toString(), this.expectedToString());
    }

    abstract String expectedToString();

    // helpers..........................................................................................................

    final void functionAndCheck(final Number left, final Number right, final Number expected) {
        assertEquals(this.visitExpected(bigDecimalFix(expected)), bigDecimalFix(this.function(left, right, CONTEXT)));
    }

    /**
     * THe logical operations never return BigDecimal or Double this allows those expected value types to be fixed.
     */
    abstract Number visitExpected(final Number number);

    abstract int left();

    abstract int right();

    abstract int expected();

    abstract Number longLongExpected();

    abstract Number function(final Number left, final Number right, final ExpressionNumberReducerContext context);

    private static Number bigDecimalFix(final Number number) {
        return number instanceof BigDecimal ? bigDecimalFix0((BigDecimal) number) : number;
    }

    private static Number bigDecimalFix0(final BigDecimal number) {
        return number.setScale(0);
    }

    @Override
    public final String typeNamePrefix() {
        return ExpressionNumberReducerBinaryExpressionNumberVisitor.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
