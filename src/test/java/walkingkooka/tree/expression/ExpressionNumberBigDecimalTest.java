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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberBigDecimalTest extends ExpressionNumberTestCase<ExpressionNumberBigDecimal> {

    @Test
    public void testWithNullBigDecimalFails() {
        assertThrows(NullPointerException.class, () -> ExpressionNumberBigDecimal.withBigDecimal(null, CONTEXT));
    }

    @Test
    public void testWithBigDecimalNullContextFails() {
        assertThrows(NullPointerException.class, () -> ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.ONE, null));
    }

    // comparable......................................................................................................

    @Test
    public void testCompareBigDecimalEquals() {
        this.compareToAndCheckEquals(ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.ONE, CONTEXT),
                ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.ONE, CONTEXT));
    }

    @Test
    public void testCompareDoubleEquals() {
        this.compareToAndCheckEquals(ExpressionNumberDouble.withDouble(1, CONTEXT),
                ExpressionNumberDouble.withDouble(1, CONTEXT));
    }

    @Test
    public void testCompareBigDecimalLess() {
        this.compareToAndCheckLess(ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.ONE, CONTEXT),
                ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.valueOf(2), CONTEXT));
    }

    @Test
    public void testCompareDoubleLess() {
        this.compareToAndCheckLess(ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.ONE, CONTEXT),
                ExpressionNumberDouble.withDouble(2, CONTEXT));
    }

    // bigDecimal.......................................................................................................

    @Test
    public final void testBigDecimal2() {
        final BigDecimal value = BigDecimal.valueOf(12.5);
        assertEquals(value, ExpressionNumberBigDecimal.withBigDecimal(value, CONTEXT).bigDecimal());
    }

    @Override
    ExpressionNumberBigDecimal create(final double value) {
        return ExpressionNumberBigDecimal.withBigDecimal(BigDecimal.valueOf(value), CONTEXT);
    }

    @Override final void checkValue0(final double value, final ExpressionNumberBigDecimal number) {
        final BigDecimal actual = number.bigDecimal();
        assertEquals(BigDecimal.valueOf(value).setScale(actual.scale()), actual);
    }

    @Override
    public Class<ExpressionNumberBigDecimal> type() {
        return ExpressionNumberBigDecimal.class;
    }
}
