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

public final class ExpressionNumberTest implements ClassTesting<ExpressionNumber> {

    @Test
    public void testIsNull() {
        this.isAndCheck(null, false);
    }

    @Test
    public void testIsByte() {
        this.isAndCheck(Byte.MAX_VALUE);
    }

    @Test
    public void testIsShort() {
        this.isAndCheck(Short.MAX_VALUE);
    }

    @Test
    public void testIsInteger() {
        this.isAndCheck(Integer.MAX_VALUE);
    }

    @Test
    public void testIsLong() {
        this.isAndCheck(Long.MAX_VALUE);
    }

    @Test
    public void testIsFloat() {
        this.isAndCheck(Float.MAX_VALUE);
    }

    @Test
    public void testIsDouble() {
        this.isAndCheck(Double.MAX_VALUE);
    }

    @Test
    public void testIsBigInteger() {
        this.isAndCheck(BigInteger.ZERO);
    }

    @Test
    public void testIsBigDecimal() {
        this.isAndCheck(BigDecimal.ONE);
    }

    @Test
    public void testIsExpressionNumberExpressionNumber() {
        this.checkEquals(true, ExpressionNumber.isClass(ExpressionNumber.class));
    }

    @Test
    public void testIsExpressionNumberBigDecimal() {
        this.isAndCheck(ExpressionNumber.with(BigDecimal.ONE));
    }

    @Test
    public void testIsExpressionNumberDouble() {
        this.isAndCheck(ExpressionNumber.with(1.0));
    }

    @Test
    public void testIsNonJdkNumberType() {
        this.isAndCheck(new Number() {
            private static final long serialVersionUID = 0;

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
        }, false);
    }

    @Test
    public void testIs() {
        this.isAndCheck(this, false);
    }

    private void isAndCheck(final Number value) {
        this.isAndCheck(value, true);
    }

    private void isAndCheck(final Object value, final boolean expected) {
        this.checkEquals(expected, ExpressionNumber.is(value));

        final Class<?> type = null != value ? value.getClass() : null;
        this.checkEquals(expected, ExpressionNumber.isClass(type));
    }

    // with.............................................................................................................

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
        if (value instanceof BigInteger || value instanceof BigDecimal) {
            final ExpressionNumber expressionNumber = ExpressionNumber.with(value instanceof BigDecimal ?
                (BigDecimal) value :
                new BigDecimal((BigInteger) value));
            this.checkEquals(ExpressionNumberBigDecimal.class, expressionNumber.getClass(), "type");
            this.checkEquals(value instanceof BigDecimal ? value : new BigDecimal((BigInteger) value), expressionNumber.bigDecimal(), "bigDecimal");
        } else {
            final ExpressionNumber expressionNumber = ExpressionNumber.with(value.doubleValue());
            this.checkEquals(ExpressionNumberDouble.class, expressionNumber.getClass(), "type");
            this.checkEquals(value.doubleValue(), expressionNumber.doubleValue(), "doubleValue");
        }
    }

    // isExpressionNumberAndNotNumber...................................................................................

    @Test
    public void testIsExpressionNumberAndNotNumberWithByte() {
        this.isExpressionNumberAndNotNumber(
            Byte.class,
            false
        );
    }

    @Test
    public void testIsExpressionNumberAndNotNumberWithShort() {
        this.isExpressionNumberAndNotNumber(
            Short.class,
            false
        );
    }

    @Test
    public void testIsExpressionNumberAndNotNumberWithInteger() {
        this.isExpressionNumberAndNotNumber(
            Integer.class,
            false
        );
    }

    @Test
    public void testIsExpressionNumberAndNotNumberWithLong() {
        this.isExpressionNumberAndNotNumber(
            Long.class,
            false
        );
    }

    @Test
    public void testIsExpressionNumberAndNotNumberWithFloat() {
        this.isExpressionNumberAndNotNumber(
            Float.class,
            false
        );
    }

    @Test
    public void testIsExpressionNumberAndNotNumberWithDouble() {
        this.isExpressionNumberAndNotNumber(
            Double.class,
            false
        );
    }

    @Test
    public void testIsExpressionNumberAndNotNumberWithBigInteger() {
        this.isExpressionNumberAndNotNumber(
            BigInteger.class,
            false
        );
    }

    @Test
    public void testIsExpressionNumberAndNotNumberWithBigDecimal() {
        this.isExpressionNumberAndNotNumber(
            BigDecimal.class,
            false
        );
    }

    @Test
    public void testIsExpressionNumberAndNotNumberWithNumber() {
        this.isExpressionNumberAndNotNumber(
            Number.class,
            false
        );
    }

    @Test
    public void testIsExpressionNumberAndNotNumberWithExpressionNumber() {
        this.isExpressionNumberAndNotNumber(
            ExpressionNumber.class,
            true
        );
    }

    @Test
    public void testIsExpressionNumberAndNotNumberWithExpressionNumberBigDecimal() {
        this.isExpressionNumberAndNotNumber(
            ExpressionNumberBigDecimal.class,
            true
        );
    }

    @Test
    public void testIsExpressionNumberAndNotNumberWithExpressionNumberDouble() {
        this.isExpressionNumberAndNotNumber(
            ExpressionNumber.class,
            true
        );
    }

    private void isExpressionNumberAndNotNumber(final Class<?> type,
                                                final boolean expected) {
        this.checkEquals(
            expected,
            ExpressionNumber.isExpressionNumberAndNotNumber(type)
        );
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
