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
import walkingkooka.tree.expression.ExpressionNumber;

import java.util.Objects;

/**
 * A {@link Converter} that takes two {@link Converter converters}.
 * <br>
 * If the value is not a {@link ExpressionNumber} it will be given to the first converter which is expected to convert
 * the value to a {@link ExpressionNumber}.
 * <br>
 * If the value is a {@link ExpressionNumber} it will be passed to the 2nd converter.
 * The second converter will accept a {@link ExpressionNumber} and then attempt to convert that to the target type.
 * <br>
 * If the type is a {@link ExpressionNumber} the second #fromExpressionNumber will be skipped.
 * <br>
 * This converter is necessary to support a converter such as SpreadsheetNumberParsePattern#converter which
 * uses its own parser to convert a String -> ExpressionNumber and then a second Converter#numberNumber to convert
 * that intermediate ExpressionNumber to the target Number type.
 */
final class ExpressionNumberConverterToExpressionNumberThen<C extends ExpressionNumberConverterContext> extends ExpressionNumberConverter<C> {

    static <C extends ExpressionNumberConverterContext> ExpressionNumberConverterToExpressionNumberThen<C> with(final Converter<C> toExpressionNumber,
                                                                                                                final Converter<C> fromExpressionNumber) {
        Objects.requireNonNull(toExpressionNumber, "toExpressionNumber");
        Objects.requireNonNull(fromExpressionNumber, "fromExpressionNumber");

        return new ExpressionNumberConverterToExpressionNumberThen<>(
            toExpressionNumber,
            fromExpressionNumber
        );
    }

    private ExpressionNumberConverterToExpressionNumberThen(final Converter<C> toExpressionNumber,
                                                            final Converter<C> fromExpressionNumber) {
        this.toExpressionNumber = toExpressionNumber;
        this.fromExpressionNumber = fromExpressionNumber;
    }

    // canConvert.......................................................................................................

    @Override //
    boolean canConvertExpressionNumber(final ExpressionNumber value,
                                       final Class<?> type,
                                       final C context) {
        // first converter is unnecessary as input value is already an ExpressionNumber just need to verify second
        // converter can convert the ExpressionNumber to the target type.
        return this.fromExpressionNumber.canConvert(
            value,
            type,
            context
        );
    }

    @Override //
    boolean canConvertNonExpressionNumber(final Object value,
                                          final Class<?> type,
                                          final C context) {
        // need to convert value to ExpressionNumber.
        final Either<?, String> asExpressionNumber = this.toExpressionNumber.convert(
            value,
            ExpressionNumber.class,
            context
        );

        // if the first toExpressionNumber Converter was successful use pass the result value to the #fromExpressionNumber#canConvert.
        return asExpressionNumber.isLeft() &&
            (ExpressionNumber.isExpressionNumberAndNotNumber(type) ||
                this.canConvertExpressionNumber(
                    Cast.to(asExpressionNumber.leftValue()),
                    type,
                    context
                )
            );
    }

    // convert..........................................................................................................

    @Override //
    <T> Either<T, String> convertExpressionNumber(final ExpressionNumber value,
                                                  final Class<T> type,
                                                  final C context) {
        // skip the #toExpressionNumber converter and ask the #fromExpressionNumber
        // no need to correct or adjust the response fail messages as #fromExpressionNumber would have received the original inputs.
        return ExpressionNumber.isExpressionNumberAndNotNumber(type) ?
            context.successfulConversion(
                value,
                type
            ) :
            this.fromExpressionNumber.convert(
                value,
                type,
                context
            );
    }

    @Override //
    <T> Either<T, String> convertNonExpressionNumber(final Object value,
                                                     final Class<T> type,
                                                     final C context) {
        // need to convert value to ExpressionNumber.
        final Either<?, String> asExpressionNumber = this.toExpressionNumber.convert(
            value,
            ExpressionNumber.class,
            context
        );

        Either<T, String> result;

        // if #toExpressionNumber converter was successful pass the value (a ExpressionNumber to #fromExpressionNumber to complete the convert.
        if (asExpressionNumber.isLeft()) {
            if (ExpressionNumber.isExpressionNumberAndNotNumber(type)) {
                result = Cast.to(asExpressionNumber); // skip fromExpressionNumber already got ExpressionNumber
            } else {
                result = this.convertExpressionNumber(
                    Cast.to(asExpressionNumber.leftValue()),
                    type,
                    context
                );
                if (result.isRight()) {
                    // need to replace the fail message with one generated using the original value & type
                    result = this.failConversion(
                        value,
                        type
                    );
                }
            }
        } else {
            // need to replace the fail message with one generated using the original value & type
            result = this.failConversion(
                value,
                type
            );
        }

        return result;
    }

    /**
     * This {@link Converter} handles converting the value to an {@link ExpressionNumber}.
     */
    private final Converter<C> toExpressionNumber;

    /**
     * This {@link Converter} converts the {@link ExpressionNumber} to the final target type.
     */
    private final Converter<C> fromExpressionNumber;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.toExpressionNumber,
            this.fromExpressionNumber
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof ExpressionNumberConverterToExpressionNumberThen && this.equals0(Cast.to(other));
    }

    private boolean equals0(final ExpressionNumberConverterToExpressionNumberThen<?> other) {
        return this.toExpressionNumber.equals(other.toExpressionNumber) &&
            this.fromExpressionNumber.equals(other.fromExpressionNumber);
    }

    @Override
    public String toString() {
        return this.toExpressionNumber + " then " + this.fromExpressionNumber;
    }
}
