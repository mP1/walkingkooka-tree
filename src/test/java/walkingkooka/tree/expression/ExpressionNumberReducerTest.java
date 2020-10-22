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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberReducerTest implements ClassTesting<ExpressionNumberReducer>, ToStringTesting<ExpressionNumberReducer> {

    private final static ExpressionNumberReducerContext CONTEXT = new ExpressionNumberReducerContext() {

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

    @Test
    public void testWithNullNumberFails() {
        assertThrows(NullPointerException.class, () -> ExpressionNumberReducer.with(null, CONTEXT));
    }

    @Test
    public void testWithNullContextFails() {
        assertThrows(NullPointerException.class, () -> ExpressionNumberReducer.with(1, null));
    }

    @Test
    public void testEmpty() {
        assertEquals(1L, this.reducer().value());
    }

    @Test
    public void testAdd() {
        assertEquals(3L, this.reducer().add(2L).value());
    }

    @Test
    public void testAddSeveral() {
        assertEquals(6L, this.reducer().add(2L).add(3L).value());
    }

    @Test
    public void testDivide() {
        assertEquals(60L / 12L, this.reducer(60L).divide(12L).value());
    }

    @Test
    public void testModulo() {
        assertEquals(12L % 5L, this.reducer(12L).modulo(5L).value());
    }

    @Test
    public void testMultiply() {
        assertEquals(3L * 4L, this.reducer(3L).multiply(4L).value());
    }

    @Test
    public void testPower() {
        assertEquals(8.0, this.reducer(2L).power(3L).value());
    }

    @Test
    public void testSubtract() {
        assertEquals(1L - -5L, this.reducer().subtract(-5L).value());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.reducer(), "1");
    }

    private ExpressionNumberReducer reducer() {
        return this.reducer(1L);
    }

    private ExpressionNumberReducer reducer(final Number number) {
        return ExpressionNumberReducer.with(number, CONTEXT);
    }

    @Override
    public Class<ExpressionNumberReducer> type() {
        return ExpressionNumberReducer.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
