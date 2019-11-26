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

public final class NumberExpressionFunction2FloorTest extends NumberExpressionFunction2TestCase<NumberExpressionFunction2Floor> {

    // BigDecimal........................................................................................................

    @Test
    public void testBigDecimal() {
        this.applyAndCheck3(BigDecimal.valueOf(1.25), BigDecimal.valueOf(1));
    }

    @Test
    public void testBigDecimal2() {
        this.applyAndCheck3(BigDecimal.valueOf(1.5), BigDecimal.valueOf(1));
    }

    @Test
    public void testBigDecimal3() {
        this.applyAndCheck3(BigDecimal.valueOf(1));
    }

    // BigInteger........................................................................................................

    @Test
    public void testBigInteger() {
        this.applyAndCheck3(BigInteger.valueOf(1));
    }

    // Double...........................................................................................................

    @Test
    public void testDouble() {
        this.applyAndCheck3(1.25, 1.0);
    }

    @Test
    public void testDouble2() {
        this.applyAndCheck3(1.5, 1.0);
    }

    @Test
    public void testDouble3() {
        this.applyAndCheck3(1.0);
    }

    // Long.............................................................................................................

    @Test
    public void testLong() {
        this.applyAndCheck3(-2L);
    }

    // Integer.............................................................................................................

    @Test
    public void testIntegerNegative() {
        this.applyAndCheck3(-34, BigDecimal.valueOf(-34));
    }

    // helper...........................................................................................................

    @Override
    public NumberExpressionFunction2Floor createBiFunction() {
        return NumberExpressionFunction2Floor.INSTANCE;
    }

    @Override
    public Class<NumberExpressionFunction2Floor> type() {
        return NumberExpressionFunction2Floor.class;
    }

    @Override
    String functionToString() {
        return "floor";
    }
}
