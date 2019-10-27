/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.tree.expression.function;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class ExpressionAbsoluteFunctionTest extends ExpressionNumberFunction2TestCase<ExpressionAbsoluteFunction> {

    // BigDecimal........................................................................................................

    @Test
    public void testBigDecimalNegative() {
        this.applyAndCheck3(BigDecimal.valueOf(-1.5), BigDecimal.valueOf(1.5));
    }

    @Test
    public void testBigDecimalZero() {
        this.applyAndCheck3(BigDecimal.ZERO);
    }

    @Test
    public void testBigDecimalPositive() {
        this.applyAndCheck3(BigDecimal.valueOf(1.5));
    }

    // BigInteger........................................................................................................

    @Test
    public void testBigIntegerNegative() {
        this.applyAndCheck3(BigInteger.valueOf(-2), BigInteger.valueOf(2));
    }

    @Test
    public void testBigIntegerZero() {
        this.applyAndCheck3(BigInteger.ZERO);
    }

    @Test
    public void testBigIntegerPositive() {
        this.applyAndCheck3(BigInteger.valueOf(3));
    }

    // Double...........................................................................................................

    @Test
    public void testDoubleNegative() {
        this.applyAndCheck3(-2.0, 2.0);
    }

    @Test
    public void testDoubleZero() {
        this.applyAndCheck3(0.0);
    }

    @Test
    public void testDoublePositive() {
        this.applyAndCheck3(3.0);
    }

    // Long.............................................................................................................

    @Test
    public void testLongNegative() {
        this.applyAndCheck3(-2L, 2L);
    }

    @Test
    public void testLongZero() {
        this.applyAndCheck3(0L);
    }

    @Test
    public void testLongPositive() {
        this.applyAndCheck3(3L);
    }

    // Integer.............................................................................................................

    @Test
    public void testIntegerNegative() {
        this.applyAndCheck3(-34, BigDecimal.valueOf(34));
    }

    // helper...........................................................................................................

    @Override
    public ExpressionAbsoluteFunction createBiFunction() {
        return ExpressionAbsoluteFunction.INSTANCE;
    }

    @Override
    public Class<ExpressionAbsoluteFunction> type() {
        return ExpressionAbsoluteFunction.class;
    }

    @Override
    String functionToString() {
        return "abs";
    }
}
