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
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * A {@link Converter} that may be used to wrap {@link Number} within a {@link ExpressionNumber}.
 */
abstract class ExpressionNumberConverter implements Converter {

    static Converter expressionNumberBigDecimal(final ExpressionNumberContext context) {
        return ExpressionNumberConverterBigDecimal.with(context);
    }

    static Converter expressionNumberDouble(final ExpressionNumberContext context) {
        return ExpressionNumberConverterDouble.with(context);
    }

    static void checkContext(final ExpressionNumberContext context) {
        Objects.requireNonNull(context, "context");
    }

    ExpressionNumberConverter(final ExpressionNumberContext context) {
        super();
        this.context = context;
    }

    @Override
    public final boolean canConvert(final Object value,
                                    final Class<?> type,
                                    final ConverterContext context) {
        return ExpressionNumber.is(value) &&
                ExpressionNumber.class == type;
    }

    @Override
    public final <T> Either<T, String> convert(final Object value,
                                               final Class<T> type,
                                               final ConverterContext context) {
        return this.canConvert(value, type, context) ?
                Cast.to(Either.left(expressionNumber(value))) :
                Either.right("Unable to convert " + CharSequences.quoteIfChars(value) + " to " + type);
    }

    /**
     * Factory that calls either of the two {@link ExpressionNumber} methods to wrap a double or {@link java.math.BigDecimal}
     */
    abstract ExpressionNumber expressionNumber(final Object value);

    final ExpressionNumberContext context;

    @Override
    public final String toString() {
        return Number.class.getSimpleName() + ">" + ExpressionNumber.class.getSimpleName() + "(" + this.expressionNumberType() + ")";
    }

    abstract String expressionNumberType();
}