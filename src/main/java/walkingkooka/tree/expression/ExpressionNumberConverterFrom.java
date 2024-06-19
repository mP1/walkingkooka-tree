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

import java.util.Objects;

/**
 * A {@link Converter} that adds support for converting a {@link ExpressionNumber} to a target type by passing
 * the value from the {@link ExpressionNumber} to an intermediate {@link Converter}. For all other input value types
 * the wrapped converter will be given the original input parameters including the value.
 */
final class ExpressionNumberConverterFrom<C extends ExpressionNumberConverterContext> extends ExpressionNumberConverter<C> {

    /**
     * Wraps another {@link Converter}, which will receive the actual value of the {@link ExpressionNumber}.
     * Note if the converter is already a {@link ExpressionNumberConverterFrom} it will not be double wrapped.
     */
    static <C extends ExpressionNumberConverterContext> ExpressionNumberConverterFrom<C> with(final Converter<C> converter) {
        Objects.requireNonNull(converter, "converter");

        return converter instanceof ExpressionNumberConverterFrom ?
                Cast.to(converter) :
                new ExpressionNumberConverterFrom<>(converter);
    }

    /**
     * Use factory
     */
    private ExpressionNumberConverterFrom(final Converter<C> converter) {
        super();
        this.converter = converter;
    }

    // canConvert.......................................................................................................

    @Override
    boolean canConvertExpressionNumber(final ExpressionNumber value,
                                       final Class<?> type,
                                       final C context) {
        return this.canConvertNonExpressionNumber(
                value.value(),
                type,
                context
        );
    }

    @Override
    boolean canConvertNonExpressionNumber(final Object value,
                                          final Class<?> type,
                                          final C context) {
        return this.converter.canConvert(
                value,
                type,
                context
        );
    }

    // convert..........................................................................................................

    @Override //
    <T> Either<T, String> convertExpressionNumber(final ExpressionNumber value,
                                                  final Class<T> type,
                                                  final C context) {
        return this.convertNonExpressionNumber(
                value.value(),
                type,
                context
        );
    }

    /**
     * Handles converting non {@link ExpressionNumber} values.
     */
    @Override //
    <T> Either<T, String> convertNonExpressionNumber(final Object value,
                                                     final Class<T> type,
                                                     final C context) {
        return (ExpressionNumber.is(value) && ExpressionNumber.class == type) ?
                this.successfulConversion(
                        context.expressionNumberKind().create((Number) value),
                        type
                ) :
                this.converter.convert(
                        value,
                        type,
                        context
                );
    }

    /**
     * The assumed {@link Converter} which takes a {@link Number} from a {@link ExpressionNumber} and converts to the requested
     * target type.
     */
    private final Converter<C> converter;

    @Override
    public String toString() {
        return ExpressionNumber.class.getSimpleName() + "? " + this.converter;
    }
}
