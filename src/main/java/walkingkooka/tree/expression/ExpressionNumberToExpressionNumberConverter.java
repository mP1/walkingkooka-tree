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
import walkingkooka.convert.Converters;

import java.util.Objects;

/**
 * A {@link Converter} that should wrap another {@link Converter} that converts anything to a Number.
 * If the target is {@link ExpressionNumber} the wrapped will be used to convert the input value to a Number,
 * and then the ExpressionNumber created. To convert {@link Number} to {@link ExpressionNumber} the wrapped
 * converter should probably be {@link Converters#numberNumber()}.
 */
final class ExpressionNumberToExpressionNumberConverter<C extends ExpressionNumberConverterContext> implements Converter<C> {

    /**
     * Factory that creates a new {@link ExpressionNumberToExpressionNumberConverter}. This should only be called by {@link ExpressionNumberKind#toConverter(Converter)}.
     */
    static <C extends ExpressionNumberConverterContext> ExpressionNumberToExpressionNumberConverter<C> with(final Converter<C> converter) {
        Objects.requireNonNull(converter, "converter");

        return new ExpressionNumberToExpressionNumberConverter<>(converter);
    }

    /**
     * Private ctor use factory
     */
    private ExpressionNumberToExpressionNumberConverter(final Converter<C> converter) {
        super();
        this.converter = converter;
    }

    // canConvert........................................................................................................

    @Override
    public final boolean canConvert(final Object value,
                                    final Class<?> type,
                                    final C context) {
        return (value instanceof ExpressionNumber && ExpressionNumber.class == type) ||
                this.converterCanConvert(value, type, context) ||
                (ExpressionNumber.isClass(type) && this.converterCanConvert(value, context.expressionNumberKind().numberType(), context));
    }

    private boolean converterCanConvert(final Object value,
                                        final Class<?> type,
                                        final C context) {
        return this.converter.canConvert(value, type, context);
    }

    // convert..........................................................................................................

    @Override
    public final <T> Either<T, String> convert(final Object value,
                                               final Class<T> type,
                                               final C context) {

        return (value instanceof ExpressionNumber && ExpressionNumber.class == type) ?
                Cast.to(Either.left(toExpressionNumber((ExpressionNumber) value, context))) :
                this.converterCanConvert(value, type, context) ?
                        this.converterConvert(value, type, context) :
                        this.converterConvertAndCreateExpressionNumber(value, type, context);
    }

    /**
     * Performs a cast and updates the {@link ExpressionNumberKind} to match the context.
     */
    private static ExpressionNumber toExpressionNumber(final ExpressionNumber number,
                                                       final ExpressionNumberConverterContext context) {
        return number.setKind(context.expressionNumberKind());
    }

    private <T> Either<T, String> converterConvert(final Object value,
                                                   final Class<T> type,
                                                   final C context) {
        return this.converter.convert(value, type, context);
    }

    private <T> Either<T, String> converterConvertAndCreateExpressionNumber(final Object value,
                                                                            final Class<T> type,
                                                                            final C context) {
        final ExpressionNumberKind kind = context.expressionNumberKind();
        final Either<T, String> result = Cast.to(this.converter.convert(value, kind.numberType(), context));
        return result.isRight() ?
                this.failConversion(value, type) :
                Cast.to(Either.left(kind.create((Number) result.leftValue())));
    }

    /**
     * The {@link Converter}
     */
    private final Converter<C> converter;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.converter.toString() + "|" + ExpressionNumber.class.getSimpleName();
    }
}
