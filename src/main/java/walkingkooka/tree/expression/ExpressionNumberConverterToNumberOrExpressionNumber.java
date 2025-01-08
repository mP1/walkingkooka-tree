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
final class ExpressionNumberConverterToNumberOrExpressionNumber<C extends ExpressionNumberConverterContext> extends ExpressionNumberConverter<C> {

    /**
     * Factory that creates a new {@link ExpressionNumberConverterToNumberOrExpressionNumber}.
     * This should only be called by {@link ExpressionNumberConverters#toNumberOrExpressionNumber(Converter)}.
     */
    static <C extends ExpressionNumberConverterContext> ExpressionNumberConverterToNumberOrExpressionNumber<C> with(final Converter<C> converter) {
        Objects.requireNonNull(converter, "converter");

        return converter instanceof ExpressionNumberConverterToNumberOrExpressionNumber ?
            Cast.to(converter) :
            new ExpressionNumberConverterToNumberOrExpressionNumber<>(converter);
    }

    /**
     * Private ctor use factory
     */
    private ExpressionNumberConverterToNumberOrExpressionNumber(final Converter<C> converter) {
        super();
        this.converter = converter;
    }

    // canConvert.......................................................................................................

    @Override
    boolean canConvertExpressionNumber(final ExpressionNumber value,
                                       final Class<?> type,
                                       final C context) {
        return this.canConvertNonExpressionNumber(
            value,
            context.expressionNumberKind()
                .numberType(),
            context
        );
    }

    @Override
    boolean canConvertNonExpressionNumber(final Object value,
                                          final Class<?> type,
                                          final C context) {
        final boolean typeIsExpressionNumber = ExpressionNumber.isClass(type);

        return ExpressionNumber.is(value) && typeIsExpressionNumber ||
            this.converter.canConvert(
                value,
                type,
                context
            ) ||
            typeIsExpressionNumber && this.converter.canConvert(
                value,
                context.expressionNumberKind().numberType(),
                context
            );
    }

    // convert..........................................................................................................

    @Override //
    <T> Either<T, String> convertExpressionNumber(final ExpressionNumber value,
                                                  final Class<T> type,
                                                  final C context) {
        return ExpressionNumber.isExpressionNumberAndNotNumber(type) ?
            context.successfulConversion(
                value,
                type
            ) :
            this.convertNonExpressionNumber(
                value.value(),
                type,
                context
            );
    }

    @Override//
    <T> Either<T, String> convertNonExpressionNumber(final Object value,
                                                     final Class<T> type,
                                                     final C context) {
        return ExpressionNumber.isExpressionNumberAndNotNumber(type) ?
            this.convertNonExpressionNumberToExpressionNumber(
                value,
                type,
                context
            ) :
            this.convertNonExpressionNumberToNonExpressionNumber(
                value,
                type,
                context
            );
    }

    private <T> Either<T, String> convertNonExpressionNumberToExpressionNumber(final Object value,
                                                                               final Class<T> type,
                                                                               final C context) {
        final ExpressionNumberKind kind = context.expressionNumberKind();

        final Either<?, String> numberResult = this.converter.convert(
            value,
            kind.numberType(),
            context
        );

        final Either<T, String> expressionNumberResult;

        // wrap a successful number in a ExpressionNumber
        if (numberResult.isRight()) {
            expressionNumberResult = this.failConversion(value, type);
        } else {
            final Object numberResultValue = numberResult.leftValue();
            if (null == numberResultValue) {
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

    private <T> Either<T, String> convertNonExpressionNumberToNonExpressionNumber(final Object value,
                                                                                  final Class<T> type,
                                                                                  final C context) {
        final Either<T, String> numberResult = this.converter.convert(
            value,
            type,
            context
        );

        final Either<T, String> expressionNumberResult;
        if (numberResult.isRight()) {
            expressionNumberResult = this.failConversion(value, type);
        } else {
            expressionNumberResult = numberResult;
        }

        return expressionNumberResult;
    }

    /**
     * The {@link Converter}
     */
    private final Converter<C> converter;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.converter.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof ExpressionNumberConverterToNumberOrExpressionNumber && this.equals0(Cast.to(other));
    }

    private boolean equals0(final ExpressionNumberConverterToNumberOrExpressionNumber<?> other) {
        return this.converter.equals(other.converter);
    }

    @Override
    public String toString() {
        return this.converter.toString() + " | " + ExpressionNumber.class.getSimpleName();
    }
}
