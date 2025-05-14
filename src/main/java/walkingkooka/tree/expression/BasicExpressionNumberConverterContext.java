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
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

final class BasicExpressionNumberConverterContext implements ExpressionNumberConverterContext,
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
    public List<String> ampms() {
        return this.context.ampms();
    }

    @Override
    public String ampm(int hourOfDay) {
        return this.context.ampm(hourOfDay);
    }

    @Override
    public int defaultYear() {
        return this.context.defaultYear();
    }

    @Override
    public List<String> monthNames() {
        return this.context.monthNames();
    }

    @Override
    public String monthName(int month) {
        return this.context.monthName(month);
    }

    @Override
    public List<String> monthNameAbbreviations() {
        return this.context.monthNameAbbreviations();
    }

    @Override
    public String monthNameAbbreviation(int month) {
        return this.context.monthNameAbbreviation(month);
    }

    @Override
    public LocalDateTime now() {
        return this.context.now();
    }

    @Override public int twoDigitYear() {
        return this.context.twoDigitYear();
    }

    @Override
    public List<String> weekDayNames() {
        return this.context.weekDayNames();
    }

    @Override
    public String weekDayName(int day) {
        return this.context.weekDayName(day);
    }

    @Override
    public List<String> weekDayNameAbbreviations() {
        return this.context.weekDayNameAbbreviations();
    }

    @Override
    public String weekDayNameAbbreviation(int day) {
        return this.context.weekDayNameAbbreviation(day);
    }

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return this.context;
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
