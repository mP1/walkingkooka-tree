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
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.Converters;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A {@link Converter} that should wrap another {@link Converter} that converts anything to a Number.
 * If the target is {@link ExpressionNumber} the wrapped will be used to convert the input value to a Number,
 * and then the ExpressionNumber created. To convert {@link Number} to {@link ExpressionNumber} the wrapped
 * converter should probably be {@link Converters#numberNumber()}.
 */
final class ExpressionNumberKindConverter implements Converter {

    /**
     * Factory that creates a new {@link ExpressionNumberKindConverter}. This should only be called by {@link ExpressionNumberKind#toConverter(Converter)}.
     */
    static ExpressionNumberKindConverter with(final ExpressionNumberKind kind,
                                              final Converter converter) {
        Objects.requireNonNull(converter, "converter");

        return new ExpressionNumberKindConverter(kind, converter);
    }

    /**
     * Private ctor use factory
     */
    private ExpressionNumberKindConverter(final ExpressionNumberKind kind,
                                          final Converter converter) {
        super();
        this.kind = kind;
        this.converter = converter;
    }

    // canConvert........................................................................................................

    @Override
    public final boolean canConvert(final Object value,
                                    final Class<?> type,
                                    final ConverterContext context) {
        return this.converterCanConvert(value, type, context) ||
                ExpressionNumber.class == type && this.converterCanConvert(value, this.expressionTypeValue(), context);
    }

    /**
     * Returns either {@link BigDecimal} | {@link Double}.
     */
    private Class<?> expressionTypeValue() {
        return this.kind.numberType();
    }

    private boolean converterCanConvert(final Object value,
                                        final Class<?> type,
                                        final ConverterContext context) {
        return this.converter.canConvert(value, type, context);
    }

    // convert..........................................................................................................

    @Override
    public final <T> Either<T, String> convert(final Object value,
                                               final Class<T> type,
                                               final ConverterContext context) {

        return this.converterCanConvert(value, type, context) ?
                this.converterConvert(value, type, context) :
                this.converterConvertAndCreateExpressionNumber(value, type, context);
    }

    private <T> Either<T, String> converterConvert(final Object value,
                                                   final Class<T> type,
                                                   final ConverterContext context) {
        return this.converter.convert(value, type, context);
    }

    private <T> Either<T, String> converterConvertAndCreateExpressionNumber(final Object value,
                                                                            final Class<T> type,
                                                                            final ConverterContext context) {
        final Either<T, String> result = Cast.to(this.converter.convert(value, this.expressionTypeValue(), context));
        return result.isRight() ?
                this.failConversion(value, type) :
                Cast.to(Either.left(this.kind.create((Number) result.leftValue())));
    }

    private final ExpressionNumberKind kind;

    /**
     * The {@link Converter}
     */
    private final Converter converter;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.converter.toString() + "|" + ExpressionNumber.class.getSimpleName() + "(" + this.expressionTypeValue().getSimpleName() + ")";
    }
}
