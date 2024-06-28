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

import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.convert.Converter;

/**
 * A {@link Converter} that supports converting a {@link Number} or {@link ExpressionNumber} to a {@link Number}.
 */
final class ExpressionNumberConverterNumberOrExpressionNumberToNumber<C extends ExpressionNumberConverterContext> extends ExpressionNumberConverter<C> {

    static <C extends ExpressionNumberConverterContext> ExpressionNumberConverterNumberOrExpressionNumberToNumber<C> instance() {
        return Cast.to(INSTANCE);
    }

    private final static ExpressionNumberConverterNumberOrExpressionNumberToNumber<?> INSTANCE = new ExpressionNumberConverterNumberOrExpressionNumberToNumber();

    /**
     * Use factory
     */
    private ExpressionNumberConverterNumberOrExpressionNumberToNumber() {
        super();
    }

    // canConvert.......................................................................................................

    @Override
    boolean canConvertExpressionNumber(final ExpressionNumber value,
                                       final Class<?> type,
                                       final C context) {
        return Number.class == type;
    }

    @Override
    boolean canConvertNonExpressionNumber(final Object value,
                                          final Class<?> type,
                                          final C context) {
        return value instanceof Number && Number.class == type;
    }

    // convert..........................................................................................................

    @Override //
    <T> Either<T, String> convertExpressionNumber(final ExpressionNumber value,
                                                  final Class<T> type,
                                                  final C context) {
        return Number.class == type ?
                this.successfulConversion(
                        value.value(),
                        type
                ) :
                this.failConversion(
                        value,
                        type
                );
    }

    @Override //
    <T> Either<T, String> convertNonExpressionNumber(final Object value,
                                                     final Class<T> type,
                                                     final C context) {
        // short cut if value is Expression
        return Number.class == type ?
                this.successfulConversion(
                        value,
                        type
                ) :
                this.failConversion(
                        value,
                        type
                );
    }

    @Override
    public String toString() {
        return ExpressionNumber.class.getSimpleName() + " or " + Number.class.getSimpleName() + " to " + Number.class.getSimpleName();
    }
}
