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

/**
 * Base {@link Converter} that includes logic to always convert a {@link ExpressionNumber} to a {@link ExpressionNumber},
 * and dispatches each method depending if the value is a {@link ExpressionNumber}.
 */
abstract class ExpressionNumberConverter<C extends ExpressionNumberConverterContext> implements Converter<C> {

    /**
     * Package private to limit sub-classing.
     */
    ExpressionNumberConverter() {
        super();
    }

    // canConvert.......................................................................................................

    @Override
    public final boolean canConvert(final Object value,
                                    final Class<?> type,
                                    final C context) {
        return value instanceof ExpressionNumber ?
            ExpressionNumber.isClass(type) || // ExpressionNumber to ExpressionNumber -> true
                this.canConvertExpressionNumber(
                    (ExpressionNumber) value,
                    type,
                    context
                ) :
            this.canConvertNonExpressionNumber(
                value,
                type,
                context
            );
    }

    abstract boolean canConvertExpressionNumber(final ExpressionNumber value,
                                                final Class<?> type,
                                                final C context);

    abstract boolean canConvertNonExpressionNumber(final Object value,
                                                   final Class<?> type,
                                                   final C context);

    // convert..........................................................................................................

    @Override
    public final <T> Either<T, String> convert(final Object value,
                                               final Class<T> type,
                                               final C context) {
        return value instanceof ExpressionNumber ?
            this.convertExpressionNumber(
                (ExpressionNumber) value,
                type,
                context
            ) :
            this.convertNonExpressionNumber(
                value,
                type,
                context
            );
    }

    /**
     * Handles converting a {@link ExpressionNumber} to the target type.
     */
    abstract <T> Either<T, String> convertExpressionNumber(final ExpressionNumber value,
                                                           final Class<T> type,
                                                           final C context);

    /**
     * Handles converting non {@link ExpressionNumber} values.
     */
    abstract <T> Either<T, String> convertNonExpressionNumber(final Object value,
                                                              final Class<T> type,
                                                              final C context);

    /**
     * Force sub-classes to implement.
     */
    abstract public String toString();
}
