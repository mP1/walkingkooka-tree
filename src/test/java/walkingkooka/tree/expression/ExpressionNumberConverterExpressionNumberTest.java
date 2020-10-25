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
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.convert.Converters;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberConverterExpressionNumberTest implements ConverterTesting2<ExpressionNumberConverterExpressionNumber>,
        ToStringTesting<ExpressionNumberConverterExpressionNumber> {

    @Test
    public void testWithNullConverterFails() {
        assertThrows(NullPointerException.class, () -> ExpressionNumberConverterExpressionNumber.with(null));
    }

    @Test
    public void testBigDecimalFails() {
        this.convertFails(BigDecimal.ZERO, Boolean.class);
    }

    @Test
    public void testByteFails() {
        this.convertFails(Byte.MAX_VALUE, Boolean.class);
    }

    @Test
    public void testStringFails() {
        this.convertFails("fails!", Boolean.class);
    }

    @Test
    public void testExpressionNumberBigDecimalTruthy() {
        this.convertAndCheck(ExpressionNumber.with(BigDecimal.ONE), Boolean.class, true);
    }

    @Test
    public void testExpressionNumberBigDecimalFalsey() {
        this.convertAndCheck(ExpressionNumber.with(BigDecimal.ZERO), Boolean.class, false);
    }

    @Test
    public void testExpressionNumberDoubleTruthy() {
        this.convertAndCheck(ExpressionNumber.with(1), Boolean.class, true);
    }

    @Test
    public void testExpressionNumberDoubleFalsey() {
        this.convertAndCheck(ExpressionNumber.with(0), Boolean.class, false);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createConverter(), "ExpressionNumber->Truthy BigDecimal|BigInteger|Byte|Short|Integer|Long|Float|Double->Boolean");
    }

    @Override
    public ExpressionNumberConverterExpressionNumber createConverter() {
        return ExpressionNumberConverterExpressionNumber.with(Converters.truthyNumberBoolean());
    }

    @Override
    public ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    @Override
    public Class<ExpressionNumberConverterExpressionNumber> type() {
        return ExpressionNumberConverterExpressionNumber.class;
    }

    @Override
    public String typeNamePrefix() {
        return ExpressionNumberConverterExpressionNumber.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return "";
    }
}
