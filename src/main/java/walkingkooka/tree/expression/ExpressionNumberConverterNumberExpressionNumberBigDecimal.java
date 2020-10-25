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

import walkingkooka.convert.Converter;

import java.math.BigDecimal;

/**
 * In addition to converting to a {@link Number} also supports creating a {@link ExpressionNumber#with(BigDecimal)}.
 */
final class ExpressionNumberConverterNumberExpressionNumberBigDecimal extends ExpressionNumberConverterNumberExpressionNumber<BigDecimal> {

    static ExpressionNumberConverterNumberExpressionNumberBigDecimal with(final Converter converter) {
        checkConverter(converter);
        return new ExpressionNumberConverterNumberExpressionNumberBigDecimal(converter);
    }

    private ExpressionNumberConverterNumberExpressionNumberBigDecimal(final Converter converter) {
        super(converter);
    }

    /**
     * Factory that creates a {@link ExpressionNumber} with the given {@link BigDecimal}.
     */
    @Override
    ExpressionNumber expressionNumber(final BigDecimal value) {
        return ExpressionNumber.with(value);
    }

    @Override
    Class<BigDecimal> expressionTypeValue() {
        return BigDecimal.class;
    }
}
