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

/**
 * Handles the simple case of casting a value to {@link ExpressionNumber}. This is necessary because {@link ExpressionNumber}
 * actually has two package private sub classes and because {@link Converters#simple} will fail.
 */
final class ExpressionNumberConverterToExpressionNumber implements Converter {

    /**
     * Singleton
     */
    final static ExpressionNumberConverterToExpressionNumber INSTANCE = new ExpressionNumberConverterToExpressionNumber();

    /**
     * Private to limit subclassing.
     */
    private ExpressionNumberConverterToExpressionNumber() {
        super();
    }

    // canConvert........................................................................................................

    @Override
    public final boolean canConvert(final Object value,
                                    final Class<?> type,
                                    final ConverterContext context) {
        return value instanceof ExpressionNumber && ExpressionNumber.class == type;
    }

    // convert..........................................................................................................

    @Override
    public final <T> Either<T, String> convert(final Object value,
                                               final Class<T> type,
                                               final ConverterContext context) {

        return this.canConvert(value, type, context) ?
                Cast.to(Either.left((ExpressionNumber) value)) :
                this.failConversion(value, type);
    }

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return "ExpressionNumber";
    }
}
