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

import walkingkooka.Either;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeContextDelegator;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.util.Locale;
import java.util.Objects;

final class BasicExpressionNumberConverterContext implements ExpressionNumberConverterContext,
    DateTimeContextDelegator,
    DecimalNumberContextDelegator {

    static BasicExpressionNumberConverterContext with(final Converter<ExpressionNumberConverterContext> converter,
                                                      final ConverterContext context,
                                                      final ExpressionNumberKind kind) {
        Objects.requireNonNull(converter, "converter");
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(kind, "kind");

        return new BasicExpressionNumberConverterContext(converter,
            context,
            kind);
    }

    private BasicExpressionNumberConverterContext(final Converter<ExpressionNumberConverterContext> converter,
                                                  final ConverterContext context,
                                                  final ExpressionNumberKind kind) {
        this.converter = converter;
        this.context = context;
        this.kind = kind;
    }

    @Override
    public boolean canNumbersHaveGroupSeparator() {
        return this.context.canNumbersHaveGroupSeparator();
    }

    @Override
    public long dateOffset() {
        return this.context.dateOffset();
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> target) {
        return this.converter.canConvert(value, target, this);
    }

    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> target) {
        return this.converter.convert(value, target, this);
    }

    private final Converter<ExpressionNumberConverterContext> converter;

    @Override
    public DateTimeContext dateTimeContext() {
        return this.context;
    }

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return this.context;
    }

    @Override
    public Locale locale() {
        return this.context.locale();
    }

    private final ConverterContext context;

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        return this.kind;
    }

    private final ExpressionNumberKind kind;

    @Override
    public String toString() {
        return this.context + " " + this.kind;
    }
}
