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

/**
 * In addition to converting to a {@link Number} also supports creating a {@link ExpressionNumber#with(double)}.
 */
final class ExpressionNumberConverterToNumberExpressionNumberDouble extends ExpressionNumberConverterToNumberExpressionNumber<Double> {

    static ExpressionNumberConverterToNumberExpressionNumberDouble with(final Converter converter) {
        checkConverter(converter);
        return new ExpressionNumberConverterToNumberExpressionNumberDouble(converter);
    }

    private ExpressionNumberConverterToNumberExpressionNumberDouble(final Converter converter) {
        super(converter);
    }

    /**
     * Factory that creates a {@link ExpressionNumber} with the given {@link Double}.
     */
    @Override
    ExpressionNumber expressionNumber(final Double value) {
        return ExpressionNumber.with(value);
    }

    @Override
    Class<Double> expressionTypeValue() {
        return Double.class;
    }
}
