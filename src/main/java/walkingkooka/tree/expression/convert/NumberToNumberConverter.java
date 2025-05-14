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
import walkingkooka.math.Maths;
import walkingkooka.tree.expression.ExpressionNumber;

/**
 * A {@link Converter} that may be used to convert any {@link Number} including {@link ExpressionNumber} to another
 * {@link Number} including {@link ExpressionNumber}.
 */
final class NumberToNumberConverter<C extends ExpressionNumberConverterContext> implements Converter<C> {

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
        return ExpressionNumber.is(value) &&
            (
                ExpressionNumber.isClass(type) ||
                    Number.class == type
            );
    }

    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> type,
                                         final C context) {
        Either<T, String> result;

        if (value.getClass() == type || Number.class == type) {
            result = this.successfulConversion(
                value,
                type
            );
        } else {
            if (ExpressionNumber.is(value) && ExpressionNumber.isClass(type)) {
                if (Maths.isNumber(value)) {
                    // Number -> Number
                    if (Maths.isNumberClass(type)) {
                        result = this.numberToNumber.convert(
                            value,
                            type,
                            context
                        );
                    } else {
                        // Number -> ExpressionNumber
                        result = this.successfulConversion(
                            context.expressionNumberKind()
                                .create(
                                    (Number) value
                                ),
                            type
                        );
                    }

                } else {
                    // ExpressionNumber -> Number
                    if (Maths.isNumberClass(type)) {
                        final ExpressionNumber expressionNumber = (ExpressionNumber) value;

                        result = this.numberToNumber.convert(
                            expressionNumber.value(),
                            type,
                            context
                        );
                    } else {
                        // ExpressionNumber -> ExpressionNumber
                        result = this.successfulConversion(
                            context.expressionNumberKind()
                                .create(
                                    (Number) value
                                ),
                            type
                        );
                    }
                }
            } else {
                result = this.failConversion(
                    value,
                    type
                );
            }
        }

        return result;
    }

    private final Converter<C> numberToNumber = Converters.numberToNumber();

    @Override
    public String toString() {
        return "number to number";
    }
}
