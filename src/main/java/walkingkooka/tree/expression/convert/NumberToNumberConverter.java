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

import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.convert.Converter;
import walkingkooka.convert.Converters;
import walkingkooka.convert.ShortCircuitingConverter;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;

/**
 * A {@link Converter} that may be used to convert any {@link Number} including {@link ExpressionNumber} to another
 * {@link Number} including {@link ExpressionNumber}.
 */
final class NumberToNumberConverter<C extends ExpressionNumberConverterContext> implements ShortCircuitingConverter<C> {

    /**
     * Type safe singleton getter
     */
    static <C extends ExpressionNumberConverterContext> NumberToNumberConverter<C> instance() {
        return Cast.to(INSTANCE);
    }

    private final static NumberToNumberConverter<?> INSTANCE = new NumberToNumberConverter<>();

    private NumberToNumberConverter() {
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type,
                              final C context) {
        return (null == value || ExpressionNumber.is(value)) &&
            (
                ExpressionNumber.isClass(type) ||
                    Number.class == type
            );
    }

    @Override
    public <T> Either<T, String> doConvert(final Object value,
                                           final Class<T> type,
                                           final C context) {
        Either<T, String> result = null;

        if (this.canConvert(value, type, context)) {
            if (null == value || value.getClass() == type) {
                result = this.successfulConversion(
                    value,
                    type
                );
            } else {
                if (Number.class == type) {
                    result = this.successfulConversion(
                        value,
                        type
                    );
                } else {
                    Number number = (Number) value;

                    if (ExpressionNumber.isExpressionNumberAndNotNumber(type)) {
                        result = this.successfulConversion(
                            ExpressionNumberKind.BIG_DECIMAL.numberType() == type ?
                                ExpressionNumberKind.BIG_DECIMAL.create(number)
                                :
                                ExpressionNumberKind.DOUBLE.numberType() == type ?
                                    ExpressionNumberKind.DOUBLE.create(number)
                                    :
                                    context.expressionNumberKind()
                                        .create(number),
                            type
                        );
                    } else {
                        // target is Number, unwrap if necessary
                        if (value instanceof ExpressionNumber) {
                            number = ((ExpressionNumber) value).value();
                        }

                        result = this.numberToNumber.convert(
                            number,
                            type,
                            context
                        );
                    }
                }
            }
        }

        if (null == result) {
            result = this.failConversion(
                value,
                type
            );
        }

        return result;
    }

    private final Converter<C> numberToNumber = Converters.numberToNumber();

    @Override
    public String toString() {
        return "number to number";
    }
}
