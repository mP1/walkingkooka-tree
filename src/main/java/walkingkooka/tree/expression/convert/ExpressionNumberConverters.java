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

package walkingkooka.tree.expression.convert;

import walkingkooka.convert.Converter;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.tree.expression.ExpressionNumber;

/**
 * A collection of useful {@link Converter} involving {@link ExpressionNumber}.
 */
public final class ExpressionNumberConverters implements PublicStaticHelper {

    /**
     * {@see ExpressionNumberConverterNumberOrExpressionNumberToNumber}
     */
    public static <C extends ExpressionNumberConverterContext> Converter<C> numberOrExpressionNumberToNumber() {
        return ExpressionNumberConverterNumberOrExpressionNumberToNumber.instance();
    }

    /**
     * {@see NumberToNumberConverter}
     */
    public static <C extends ExpressionNumberConverterContext> Converter<C> numberToNumber() {
        return NumberToNumberConverter.instance();
    }

    /**
     * {@see ExpressionNumberConverterToExpressionNumberThen}
     */
    public static <C extends ExpressionNumberConverterContext> Converter<C> toExpressionNumberThen(final Converter<C> toExpressionNumber,
                                                                                                   final Converter<C> toTarget) {
        return ExpressionNumberConverterToExpressionNumberThen.with(
            toExpressionNumber,
            toTarget
        );
    }

    /**
     * Adds support for converting numbers to another numbers or {@link ExpressionNumber}.
     */
    public static <C extends ExpressionNumberConverterContext> Converter<C> toNumberOrExpressionNumber(final Converter<C> converter) {
        return ExpressionNumberConverterToNumberOrExpressionNumber.with(converter);
    }

    /**
     * Stop creation
     */
    private ExpressionNumberConverters() {
        throw new UnsupportedOperationException();
    }
}
