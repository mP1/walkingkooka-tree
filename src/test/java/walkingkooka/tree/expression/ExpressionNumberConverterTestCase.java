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
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContexts;

import java.math.MathContext;

public abstract class ExpressionNumberConverterTestCase<C extends ExpressionNumberConverter<ExpressionNumberConverterContext>>
        implements ConverterTesting2<C, ExpressionNumberConverterContext>,
        ToStringTesting<C> {

    ExpressionNumberConverterTestCase() {
        super();
    }

    @Test
    public final void testConvertValueToSameValueTypeNotNumber() {
        this.convertFails(
                "Hello",
                String.class
        );
    }

    @Test
    public final void testConvertExpressionNumberToExpressionNumberBigDecimal() {
        this.convertAndCheck(
                ExpressionNumberKind.BIG_DECIMAL.create(123.5)
        );
    }

    @Test
    public final void testConvertExpressionNumberToExpressionNumberDouble() {
        this.convertAndCheck(
                ExpressionNumberKind.DOUBLE.create(123.5)
        );
    }

    @Override
    public final ExpressionNumberConverterContext createContext() {
        return this.createContext(ExpressionNumberKind.DEFAULT);
    }

    final ExpressionNumberConverterContext createContext(final ExpressionNumberKind kind) {
        return ExpressionNumberConverterContexts.basic(
                Converters.fake(),
                ConverterContexts.basic(
                        Converters.fake(),
                        DateTimeContexts.fake(),
                        DecimalNumberContexts.american(MathContext.DECIMAL32)
                ),
                kind
        );
    }

    // typeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return ExpressionNumberConverter.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
