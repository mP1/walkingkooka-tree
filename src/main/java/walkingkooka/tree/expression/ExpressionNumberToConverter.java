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
 * A {@link Converter} that supports converting values to a {@link ExpressionNumber} using an intermediary {@link Converter}.
 * It is assumed the intermediary handles converting the value to a {@link Number} and if that was successful that number
 * will be used to create a {@link ExpressionNumber}.
 * <br>
 * If the wrapped {@link Converter} cannot convert or convert fails then that result is returned and no {@link ExpressionNumber}
 * is ever created.
 * <br>
 * If the input is an {@link ExpressionNumber} and the target a {@link ExpressionNumber} no convert happens, the wrapped
 * {@link Converter} is never invoked.
 */
final class ExpressionNumberToConverter<C extends ExpressionNumberConverterContext> implements Converter<C> {

    /**
     * Factory that creates a new {@link ExpressionNumberToConverter}.
     * This should only be called by {@link ExpressionNumber#toConverter(Converter)}.
     */
    static <C extends ExpressionNumberConverterContext> ExpressionNumberToConverter<C> with(final Converter<C> converter) {
        Objects.requireNonNull(converter, "converter");

        return converter instanceof ExpressionNumberToConverter ?
                Cast.to(converter) :
                new ExpressionNumberToConverter<>(converter);
    }

    /**
     * Private ctor use factory
     */
    private ExpressionNumberToConverter(final Converter<C> converter) {
        super();
        this.converter = converter;
    }

    // canConvert........................................................................................................

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type,
                              final C context) {
        return (ExpressionNumber.is(value) && ExpressionNumber.class == type) ||
                this.converterCanConvert(value, type, context) ||
                (ExpressionNumber.isClass(type) &&
                        this.converterCanConvert(
                                value,
                                context.expressionNumberKind().numberType(),
                                context
                        )
                );
    }

    /**
     * Queries if the wrapped {@link Converter} can convert the value to the target type which will be
     * either {@link double} or {@link java.math.BigDecimal}.
     */
    private boolean converterCanConvert(final Object value,
                                        final Class<?> type,
                                        final C context) {
        return this.converter.canConvert(value, type, context);
    }

    // convert..........................................................................................................

    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> type,
                                         final C context) {

        return (ExpressionNumber.is(value) && ExpressionNumber.class == type) ?
                this.successfulConversion(
                        context.expressionNumberKind().create((Number) value),
                        type
                ) :
                (value instanceof ExpressionNumber && ExpressionNumber.class == type) ?
                        this.successfulConversion(
                                toExpressionNumber((ExpressionNumber) value, context),
                                type
                        ) :
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

        final Either<?, String> numberResult = this.converter.convert(
                value,
                kind.numberType(),
                context
        );

        final Either<T, String> expressionNumberResult;
        if (numberResult.isRight()) {
            expressionNumberResult = this.failConversion(value, type);
        } else {
            final Object numberResultValue = numberResult.leftValue();
            if (null == numberResultValue || numberResultValue instanceof ExpressionNumber) {
                expressionNumberResult = Cast.to(numberResult);
            } else {
                expressionNumberResult = this.successfulConversion(
                        kind.create(
                                (Number) numberResultValue
                        ),
                        type
                );
            }
        }

        return expressionNumberResult;
    }

    /**
     * The {@link Converter}
     */
    private final Converter<C> converter;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.converter.toString() + "? " + ExpressionNumber.class.getSimpleName();
    }
}
