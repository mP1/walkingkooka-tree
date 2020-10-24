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
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.ConverterTesting2;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class ExpressionNumberConverterTestCase<C extends ExpressionNumberConverter> implements ConverterTesting2<C> {

    static final ExpressionNumberContext CONTEXT = ExpressionNumberContexts.fake();

    ExpressionNumberConverterTestCase() {
        super();
    }

    @Test
    public final void testByteOne() {
        this.convertOneAndCheck((byte) 1);
    }

    @Test
    public final void testShortOne() {
        this.convertOneAndCheck((short) 1);
    }

    @Test
    public final void testIntegerOne() {
        this.convertOneAndCheck(1);
    }

    @Test
    public final void testLongOne() {
        this.convertOneAndCheck(1L);
    }

    @Test
    public final void testFloatOne() {
        this.convertOneAndCheck(1.0f);
    }

    @Test
    public final void testDoubleOne() {
        this.convertOneAndCheck(1.0);
    }

    @Test
    public final void testBigIntegerOne() {
        this.convertOneAndCheck(BigInteger.ONE);
    }

    @Test
    public final void testBigDecimalOne() {
        this.convertOneAndCheck(BigDecimal.ONE);
    }

    private void convertOneAndCheck(final Object value) {
        this.convertAndCheck(value, ExpressionNumber.class, this.expressionNumberOne());
    }

    abstract ExpressionNumber expressionNumberOne();

    @Test
    public final void testFloatHalf() {
        this.convertHalfAndCheck(0.5f);
    }

    @Test
    public final void testDoubleHalf() {
        this.convertHalfAndCheck(0.5);
    }

    @Test
    public final void testBigDecimalHalf() {
        this.convertHalfAndCheck(BigDecimal.valueOf(0.5));
    }

    private void convertHalfAndCheck(final Object value) {
        this.convertAndCheck(value, ExpressionNumber.class, this.expressionNumberHalf());
    }

    abstract ExpressionNumber expressionNumberHalf();

    @Override
    public final ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    @Override
    public final String typeNamePrefix() {
        return ExpressionNumberConverter.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
