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
import walkingkooka.convert.ConverterContext;

import java.util.Objects;

/**
 * A {@link Converter} that unwraps the {@link double} or {@link java.math.BigDecimal }from a {@link ExpressionNumber}.
 * This value is then passed to the wrapped {@link Converter} which is assumed to accept these types.
 */
final class ExpressionNumberConverterExpressionNumber implements Converter {

    /**
     * Wraps another {@link Converter}, which will receive the actual value of the {@link ExpressionNumber}.
     */
    static final ExpressionNumberConverterExpressionNumber with(final Converter converter) {
        Objects.requireNonNull(converter, "converter");

        return new ExpressionNumberConverterExpressionNumber(converter);
    }

    /**
     * Use Singleton
     */
    private ExpressionNumberConverterExpressionNumber(final Converter converter) {
        super();
        this.converter = converter;
    }

    /**
     * Only {@link ExpressionNumber} values and target type = JDK {@link Number} is supported.
     */
    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type,
                              final ConverterContext context) {
        return value instanceof ExpressionNumber &&
                this.converter.canConvert(this.value((ExpressionNumber) value), type, context);
    }

    private Object value(final ExpressionNumber expressionNumber) {
        return expressionNumber.value();
    }

    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> type,
                                         final ConverterContext context) {
        return this.canConvert(value, type, context) ?
                this.converter.convert(this.value((ExpressionNumber) value), type, context) :
                this.failConversion(value, type);
    }

    /**
     * The assumed {@link Converter} which takes a {@link Number} and converts to some other target.
     */
    private final Converter converter;

    @Override
    public String toString() {
        return ExpressionNumber.class.getSimpleName() + "->" + this.converter.toString();
    }
}
