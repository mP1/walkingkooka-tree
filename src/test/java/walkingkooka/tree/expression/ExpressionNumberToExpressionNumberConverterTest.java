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
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.convert.Converters;

public final class ExpressionNumberToExpressionNumberConverterTest implements ConverterTesting2<ExpressionNumberToExpressionNumberConverter<ConverterContext>, ConverterContext>,
        ToStringTesting<ExpressionNumberToExpressionNumberConverter<ConverterContext>> {

    @Test
    public void testConvertersSimpleFails() {
        this.convertFails(Converters.simple(), ExpressionNumberKind.DEFAULT.create(1), ExpressionNumber.class);
    }

    @Test
    public void testExpressionNumberBigDecimal() {
        this.convertAndCheck(ExpressionNumberKind.BIG_DECIMAL.create(1.0), ExpressionNumber.class);
    }

    @Test
    public void testExpressionNumberDouble() {
        this.convertAndCheck(ExpressionNumberKind.DOUBLE.create(1.0), ExpressionNumber.class);
    }

    @Test
    public void testExpressionNumberValueDifferentType() {
        this.convertFails(ExpressionNumberKind.DEFAULT.create(1), this.getClass());
    }

    @Test
    public void testNotExpressionNumber() {
        this.convertFails(this, ExpressionNumber.class);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createConverter(), ExpressionNumber.class.getSimpleName());
    }

    @Override
    public ExpressionNumberToExpressionNumberConverter<ConverterContext> createConverter() {
        return ExpressionNumberToExpressionNumberConverter.instance();
    }

    @Override
    public ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    @Override
    public Class<ExpressionNumberToExpressionNumberConverter<ConverterContext>> type() {
        return Cast.to(ExpressionNumberToExpressionNumberConverter.class);
    }

    @Override
    public String typeNamePrefix() {
        return ExpressionNumber.class.getSimpleName();
    }
}
