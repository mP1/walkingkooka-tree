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
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberTest implements ClassTesting<ExpressionNumber> {

    // with.............................................................................................................

    @Test
    public void testWithNullNumberFails() {
        assertThrows(NullPointerException.class, () -> ExpressionNumber.with((Number)null));
    }

    @Test
    public void testWithByte() {
        this.withNumberAndCheck((byte) 1);
    }

    @Test
    public void testWithShort() {
        this.withNumberAndCheck((short) 1);
    }

    @Test
    public void testWithInteger() {
        this.withNumberAndCheck(1);
    }

    @Test
    public void testWithLong() {
        this.withNumberAndCheck(1L);
    }

    @Test
    public void testWithFloat() {
        this.withNumberAndCheck(1.5f);
    }

    @Test
    public void testWithDouble() {
        this.withNumberAndCheck(1.5);
    }

    @Test
    public void testWithBigInteger() {
        this.withNumberAndCheck(BigInteger.TEN);
    }

    @Test
    public void testWithBigDecimal() {
        this.withNumberAndCheck(BigDecimal.valueOf(1.5));
    }

    private void withNumberAndCheck(final Number value) {
        if(value instanceof BigInteger || value instanceof BigDecimal) {
            final ExpressionNumber expressionNumber = ExpressionNumber.with(value);
            assertEquals(ExpressionNumberBigDecimal.class, expressionNumber.getClass(), "type");
            assertEquals(value instanceof BigDecimal ? value : new BigDecimal((BigInteger)value), expressionNumber.bigDecimal(), "bigDecimal");
        } else {
            final ExpressionNumber expressionNumber = ExpressionNumber.with(value);
            assertEquals(ExpressionNumberDouble.class, expressionNumber.getClass(), "type");
            assertEquals(value.doubleValue(), expressionNumber.doubleValue(), "doubleValue");
        }
    }

    @Test
    public void testWithExpressionNumberBigDecimal() {
        final ExpressionNumber number = ExpressionNumber.with(BigDecimal.ONE);
        assertSame(number, ExpressionNumber.with(number));
    }

    @Test
    public void testWithExpressionNumberBigInteger() {
        final ExpressionNumber number = ExpressionNumber.with(BigInteger.ONE);
        assertSame(number, ExpressionNumber.with(number));
    }

    @Test
    public void testWithUnknownNumberFails() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionNumber.with(new TestNumber()));
    }

    final static class TestNumber extends Number {
        private static final long serialVersionUID = 1;

        @Override
        public int intValue() {
            return 0;
        }

        @Override
        public long longValue() {
            return 0;
        }

        @Override
        public float floatValue() {
            return 0;
        }

        @Override
        public double doubleValue() {
            return 0;
        }
    }

    // helpers..........................................................................................................

    @Override
    public Class<ExpressionNumber> type() {
        return ExpressionNumber.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
