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

import walkingkooka.Either;
import walkingkooka.convert.Converter;

import java.util.Objects;

/**
 * A {@link Converter} that adds support for converting {@link ExpressionNumber} to a target type by passing
 * the value from the {@link ExpressionNumber} to an intermediate {@link Converter}.
 */
final class ExpressionNumberFromConverter<C extends ExpressionNumberConverterContext> implements Converter<C> {

    /**
     * Wraps another {@link Converter}, which will receive the actual value of the {@link ExpressionNumber}.
     */
    static <C extends ExpressionNumberConverterContext> ExpressionNumberFromConverter<C> with(final Converter<C> converter) {
        Objects.requireNonNull(converter, "converter");

        return new ExpressionNumberFromConverter<>(converter);
    }

    /**
     * Use factory
     */
    private ExpressionNumberFromConverter(final Converter<C> converter) {
        super();
        this.converter = converter;
    }

    /**
     * Only {@link ExpressionNumber} values and target type = JDK {@link Number} is supported.
     */
    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type,
                              final C context) {
        return value instanceof ExpressionNumber ?
                this.canConvertExpressionNumber((ExpressionNumber) value, type, context) :
                this.converter.canConvert(value, type, context);
    }

    /**
     * Asks the wrapped {@link Converter} if it can convert the {@link double} or {@link java.math.BigDecimal} value held
     * by the {@link ExpressionNumber}.
     */
    private boolean canConvertExpressionNumber(final ExpressionNumber value,
                                               final Class<?> type,
                                               final C context) {
        return this.converter.canConvert(value.value(),
                type,
                context);
    }


    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> type,
                                         final C context) {
        return value instanceof ExpressionNumber ?
                this.convertExpressionNumber((ExpressionNumber) value, type, context) :
                this.converter.convert(value, type, context);
    }

    private <T> Either<T, String> convertExpressionNumber(final ExpressionNumber number,
                                                          final Class<T> type,
                                                          final C context) {
        final Either<T, String> result = this.converter.convert(number.value(), type, context);
        return result.isRight() ?
                this.failConversion(number, type) :
                result;
    }

    /**
     * The assumed {@link Converter} which takes a {@link Number} from a {@link ExpressionNumber} and converts to requested
     * target target.
     */
    private final Converter<C> converter;

    @Override
    public String toString() {
        return ExpressionNumber.class.getSimpleName() + "|" + this.converter.toString();
    }
}
