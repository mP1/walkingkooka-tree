/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverter;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.Name;
import walkingkooka.predicate.Predicates;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.IsMethodTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticFactoryTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.HasTextTesting;
import walkingkooka.text.cursor.parser.DoubleParserToken;
import walkingkooka.text.cursor.parser.InvalidCharacterExceptionFactory;
import walkingkooka.text.cursor.parser.LocalDateParserToken;
import walkingkooka.text.cursor.parser.LocalDateTimeParserToken;
import walkingkooka.text.cursor.parser.LocalTimeParserToken;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.NodeTesting;
import walkingkooka.tree.expression.convert.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.convert.ExpressionNumberConverterContexts;
import walkingkooka.tree.expression.convert.ExpressionNumberConverters;

import java.math.MathContext;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class ExpressionTestCase<N extends Expression> implements TreePrintableTesting,
    ClassTesting2<Expression>,
    HasTextTesting,
    ExpressionPurityTesting,
    IsMethodTesting<N>,
    NodeTesting<Expression, ExpressionFunctionName, Name, Object> {

    final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.DEFAULT;

    ExpressionTestCase() {
        super();
    }

    @Test
    public final void testPublicStaticFactoryMethod() {
        PublicStaticFactoryTesting.checkFactoryMethods(Expression.class,
            "",
            "Expression",
            this.type());
    }

    @Override
    public final void testSetSameAttributes() {
        // Ignored
    }

    // HasText.........................................................................................................

    @Test
    public final void testText() {
        final Expression expression = this.createExpression();

        this.textAndCheck(
            expression,
            expression.toString()
        );
    }

    // helper..........................................................................................................

    @Override
    public Expression createNode() {
        return this.createExpression();
    }

    abstract N createExpression();

    @Override
    public final Class<Expression> type() {
        return Cast.to(this.expressionType());
    }

    abstract Class<N> expressionType();

    // evaluation........................................................................................................

    final ValueExpression<Boolean> booleanValue(final boolean value) {
        return Expression.value(value);
    }

    final ValueExpression<ExpressionNumber> expressionNumber(final double value) {
        return Expression.value(
            expressionNumberValue(value)
        );
    }

    final ExpressionNumber expressionNumberValue(final double value) {
        return EXPRESSION_NUMBER_KIND.create(value);
    }

    final LocalDate localDateValue(final long value) {
        return Converters.numberToLocalDate()
            .convertOrFail(value, LocalDate.class, this.converterContext());
    }

    final ValueExpression<LocalDate> localDate(final long value) {
        return Expression.value(localDateValue(value));
    }

    final LocalDateTime localDateTimeValue(final double value) {
        return Converters.numberToLocalDateTime()
            .convertOrFail(value, LocalDateTime.class, this.converterContext());
    }

    final ValueExpression<LocalDateTime> localDateTime(final double value) {
        return Expression.value(
            localDateTimeValue(value)
        );
    }

    final LocalTime localTimeValue(final long value) {
        return Converters.numberToLocalTime()
            .convertOrFail(value, LocalTime.class, this.converterContext());
    }

    final ValueExpression<LocalTime> localTime(final long value) {
        return Expression.value(
            localTimeValue(value)
        );
    }

    final ValueExpression<String> text(final String value) {
        return Expression.value(value);
    }

    final ValueExpression<String> text(final Object value) {
        return text(context().convertOrFail(value, String.class));
    }

    final String textText(final ValueExpression<?> value) {
        return value.toString(context());
    }

    final void toBooleanAndCheck(final Expression expression,
                                 final boolean expected) {
        this.toBooleanAndCheck(
            expression,
            context(),
            expected
        );
    }

    final void toBooleanAndCheck(final Expression expression,
                                 final ExpressionEvaluationContext context,
                                 final boolean expected) {
        this.checkEquals(
            expected,
            expression.toBoolean(context),
            () -> "toBoolean of " + expression + " failed"
        );
    }

    final void toExpressionNumberAndCheck(final Expression expression,
                                          final double expected) {
        this.toExpressionNumberAndCheck(
            expression,
            expressionNumberValue(expected)
        );
    }

    final void toExpressionNumberAndCheck(final Expression expression,
                                          final ExpressionNumber expected) {
        this.toExpressionNumberAndCheck(
            expression,
            this.context(),
            expected
        );
    }

    final void toExpressionNumberAndCheck(final Expression expression,
                                          final ExpressionEvaluationContext context,
                                          final ExpressionNumber expected) {
        this.checkEquals(
            expected,
            expression.toExpressionNumber(context),
            () -> "toExpressionNumber of " + expression + " failed"
        );
    }

    final void toExpressionNumberLocalDateCheck(final Expression expression,
                                                final long expected) {
        this.toExpressionNumberLocalDateCheck(
            expression,
            this.localDateValue(expected)
        );
    }

    final void toExpressionNumberLocalDateCheck(final Expression expression,
                                                final LocalDate expected) {
        this.toExpressionNumberLocalDateAndCheck(
            expression,
            context(),
            expected
        );
    }

    final void toExpressionNumberLocalDateAndCheck(final Expression expression,
                                                   final ExpressionEvaluationContext context,
                                                   final LocalDate expected) {
        this.checkEquals(
            expected,
            context.convertOrFail(
                expression.toExpressionNumber(context),
                LocalDate.class
            ),
            () -> "toExpressionNumber of " + expression + " failed"
        );
    }

    final void toExpressionNumberLocalDateTimeCheck(final Expression expression,
                                                    final double expected) {
        this.toExpressionNumberLocalDateTimeCheck(
            expression,
            localDateTimeValue(expected)
        );
    }

    final void toExpressionNumberLocalDateTimeCheck(final Expression expression,
                                                    final LocalDateTime expected) {
        this.toExpressionNumberLocalDateTimeCheck(
            expression,
            this.context(),
            expected
        );
    }

    final void toExpressionNumberLocalDateTimeCheck(final Expression expression,
                                                    final ExpressionEvaluationContext context,
                                                    final LocalDateTime expected) {
        this.checkEquals(
            expected,
            context.convertOrFail(
                expression.toExpressionNumber(context),
                LocalDateTime.class
            ),
            () -> "toExpressionNumber of " + expression + " failed"
        );
    }

    final void toExpressionNumberLocalTimeCheck(final Expression expression,
                                                final long expected) {
        this.toExpressionNumberLocalTimeCheck(
            expression,
            this.localTimeValue(expected)
        );
    }

    final void toExpressionNumberLocalTimeCheck(final Expression expression,
                                                final LocalTime expected) {
        this.toExpressionNumberLocalTimeCheck(
            expression,
            context(),
            expected
        );
    }

    final void toExpressionNumberLocalTimeCheck(final Expression expression,
                                                final ExpressionEvaluationContext context,
                                                final LocalTime expected) {
        this.checkEquals(
            expected,
            context.convertOrFail(
                expression.toExpressionNumber(context),
                LocalTime.class
            ),
            () -> "toExpressionNumber of " + expression + " failed"
        );
    }

    final void toTextAndCheck(final Expression expression,
                              final String expected) {
        this.toTextAndCheck(
            expression,
            context(),
            expected
        );
    }

    final void toTextAndCheck(final Expression expression,
                              final ExpressionEvaluationContext context,
                              final String expected) {
        this.checkEquals(
            expected,
            expression.toString(context),
            () -> "toString of " + expression + " failed"
        );
    }

    final void toValueAndCheck(final Expression expression,
                               final Object expected) {
        this.toValueAndCheck(
            expression,
            context(),
            expected
        );
    }

    final void toValueAndCheck(final Expression expression,
                               final ExpressionEvaluationContext context,
                               final Object expected) {
        final Object value = expression.toExpressionNumber(context);
        this.checkEquals(
            expected,
            value,
            () -> "toExpressionNumber of " + expression + " failed"
        );

        if (false == expression.isReference()) {
            final Object referenceOrValue = expression.toReferenceOrValue(context);
            if (expected instanceof Comparable && referenceOrValue instanceof Comparable) {
                this.checkEquals(
                    expected,
                    referenceOrValue,
                    () -> "toReferenceOrValue of " + expression + " failed"
                );
            } else {
                this.checkEquals(
                    expected,
                    value,
                    () -> "toReferenceOrValue of " + expression + " failed"
                );
            }
        }
    }

    private ExpressionNumberConverterContext converterContext() {
        final Locale locale = Locale.ENGLISH;

        return ExpressionNumberConverterContexts.basic(
            Converters.collection(
                Lists.of(
                    Converters.simple(),
                    Converters.characterOrCharSequenceOrHasTextOrStringToCharacterOrCharSequenceOrString()
                )
            ),
            ConverterContexts.basic(
                Converters.JAVA_EPOCH_OFFSET,
                Converters.simple(),
                DateTimeContexts.basic(
                    DateTimeSymbols.fromDateFormatSymbols(
                        new DateFormatSymbols(locale)
                    ),
                    locale,
                    1900,
                    20,
                    LocalDateTime::now
                ),
                DecimalNumberContexts.american(MathContext.DECIMAL32)
            ),
            EXPRESSION_NUMBER_KIND
        );
    }

    ExpressionEvaluationContext context() {
        final Function<ExpressionNumberConverterContext, ParserContext> parserContext = (c) -> ParserContexts.basic(
            InvalidCharacterExceptionFactory.POSITION,
            c,
            c
        );

        final Converter<ExpressionNumberConverterContext> stringDouble = Converters.parser(
            Double.class,
            Parsers.doubleParser(),
            parserContext,
            (v, c) -> v.cast(DoubleParserToken.class).value()
        );
        final Converter<ExpressionNumberConverterContext> stringLocalDate = Converters.parser(
            LocalDate.class,
            Parsers.localDate((c) -> DateTimeFormatter.ISO_LOCAL_DATE),
            parserContext,
            (v, c) -> v.cast(LocalDateParserToken.class).value()
        );
        final Converter<ExpressionNumberConverterContext> stringLocalDateTime = Converters.parser(
            LocalDateTime.class,
            Parsers.localDateTime((c) -> DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            parserContext,
            (v, c) -> v.cast(LocalDateTimeParserToken.class).value()
        );
        final Converter<ExpressionNumberConverterContext> stringLocalTime = Converters.parser(
            LocalTime.class,
            Parsers.localTime((c) -> DateTimeFormatter.ISO_LOCAL_TIME),
            parserContext,
            (v, c) -> v.cast(LocalTimeParserToken.class).value()
        );

        final Converter<ExpressionNumberConverterContext> converters = Converters.collection(
            Lists.of(
                Converters.simple(),
                Converters.characterOrCharSequenceOrHasTextOrStringToCharacterOrCharSequenceOrString(),
                new FakeConverter<>() {
                    @Override
                    public boolean canConvert(final Object value,
                                              final Class<?> type,
                                              final ExpressionNumberConverterContext context) {
                        return value instanceof ExpressionNumber && ExpressionNumber.class == type;
                    }

                    @Override
                    public <T> Either<T, String> convert(final Object value,
                                                         final Class<T> type,
                                                         final ExpressionNumberConverterContext context) {
                        return this.canConvert(value, type, context) ?
                            Cast.to(Either.left(this.toExpressionNumber((ExpressionNumber) value))) :
                            this.failConversion(value, type);
                    }

                    private ExpressionNumber toExpressionNumber(final ExpressionNumber value) {
                        return value.setKind(EXPRESSION_NUMBER_KIND);
                    }
                },
                // localDate ->
                toBoolean(LocalDate.class, LocalDate.ofEpochDay(0)),
                Converters.localDateToLocalDateTime(),
                ExpressionNumberConverters.toNumberOrExpressionNumber(Converters.localDateToNumber()),
                Converters.localDateToString((c) -> DateTimeFormatter.ISO_LOCAL_DATE),
                // localDateTime ->
                toBoolean(LocalDateTime.class, LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC)),
                Converters.localDateTimeToLocalDate(),
                Converters.localDateTimeToLocalTime(),
                ExpressionNumberConverters.toNumberOrExpressionNumber(Converters.localDateTimeToNumber()),
                Converters.localDateTimeToString((c) -> DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                // localTime
                toBoolean(LocalTime.class, LocalTime.ofNanoOfDay(0)),
                Converters.localTimeToLocalDateTime(),
                ExpressionNumberConverters.toNumberOrExpressionNumber(Converters.localTimeToNumber()),
                Converters.localTimeToString((c) -> DateTimeFormatter.ISO_LOCAL_TIME),
                // ExpressionNumber ->,
                ExpressionNumberConverters.numberOrExpressionNumberToNumber().to(Number.class, Converters.numberToNumber()),
                ExpressionNumberConverters.numberOrExpressionNumberToNumber().to(Number.class, Converters.numberToBoolean()),
                ExpressionNumberConverters.numberOrExpressionNumberToNumber().to(Number.class, Converters.numberToLocalDate()),
                ExpressionNumberConverters.numberOrExpressionNumberToNumber().to(Number.class, Converters.numberToLocalDateTime()),
                ExpressionNumberConverters.numberOrExpressionNumberToNumber().to(Number.class, Converters.numberToLocalTime()),
                ExpressionNumberConverters.numberOrExpressionNumberToNumber().to(Number.class, Converters.numberToString((c) -> new DecimalFormat("#.###"))),
                // Number ->,
                ExpressionNumberConverters.numberOrExpressionNumberToNumber().to(Number.class, Converters.numberToNumber()),
                ExpressionNumberConverters.numberOrExpressionNumberToNumber().to(Number.class, Converters.numberToBoolean()),
                ExpressionNumberConverters.numberOrExpressionNumberToNumber().to(Number.class, Converters.numberToLocalDate()),
                ExpressionNumberConverters.numberOrExpressionNumberToNumber().to(Number.class, Converters.numberToLocalDateTime()),
                ExpressionNumberConverters.numberOrExpressionNumberToNumber().to(Number.class, Converters.numberToLocalTime()),
                ExpressionNumberConverters.numberOrExpressionNumberToNumber().to(Number.class, Converters.numberToString((c) -> new DecimalFormat("#.###"))),
                // string ->
                Converters.<String, Boolean, ExpressionNumberConverterContext>mapper(v -> v instanceof String, Predicate.isEqual(Boolean.class), Boolean::valueOf),
                Converters.<ExpressionFunctionName, Boolean, ExpressionNumberConverterContext>mapper(v -> v instanceof ExpressionFunctionName, Predicate.isEqual(Boolean.class), (i) -> true),
                stringLocalDate,
                stringLocalDateTime,
                stringLocalTime,
                ExpressionNumberConverters.toNumberOrExpressionNumber(stringDouble),
                Converters.objectToString(),
                // boolean ->
                listToBoolean(),
                fromBoolean(LocalDate.class, Converters.numberToLocalDate()),
                fromBoolean(LocalDateTime.class, Converters.numberToLocalDateTime()),
                fromBoolean(LocalTime.class, Converters.numberToLocalTime()),
                fromBoolean(ExpressionNumber.class, ExpressionNumberConverters.toNumberOrExpressionNumber(Converters.numberToNumber()))
            )
        );

        return new FakeExpressionEvaluationContext() {

            @Override
            public MathContext mathContext() {
                return MathContext.DECIMAL64;
            }

            @Override
            public <T> Either<T, String> convert(final Object value, final Class<T> target) {
                return converters.convert(value, target, ExpressionTestCase.this.converterContext());
            }

            @Override
            public ExpressionNumberKind expressionNumberKind() {
                return EXPRESSION_NUMBER_KIND;
            }

            @Override
            public CaseSensitivity stringEqualsCaseSensitivity() {
                return CaseSensitivity.SENSITIVE;
            }

            @Override
            public boolean isText(final Object value) {
                return value instanceof String;
            }
        };
    }

    /**
     * Converts an empty list to false, and non empty list to true.
     */
    private static Converter<ExpressionNumberConverterContext> listToBoolean() {
        return Converters.toBoolean(
            (v) -> v instanceof List,
            Predicates.is(Boolean.class),
            (v) -> v.equals(Lists.empty()),
            Boolean.FALSE,
            Boolean.TRUE
        );
    }

    private <T> Converter<ExpressionNumberConverterContext> fromBoolean(final Class<T> targetType,
                                                                        final Converter<ExpressionNumberConverterContext> trueOrFalse) {
        final ExpressionNumberConverterContext context = this.converterContext();
        return Converters.toBoolean(
            (t) -> t instanceof Boolean,
            (t) -> t == targetType,
            Predicate.isEqual(Boolean.TRUE),
            trueOrFalse.convertOrFail(1L, targetType, context),
            trueOrFalse.convertOrFail(0L, targetType, context)
        );
    }

    private static <S> Converter<ExpressionNumberConverterContext> toBoolean(final Class<S> sourceType,
                                                                             final S falseValue) {
        return Converters.toBoolean(
            (t) -> t.getClass() == sourceType,
            (t) -> t == Boolean.class,
            falseValue::equals,
            Boolean.FALSE,
            Boolean.TRUE
        );
    }

    @Override
    public final String typeNamePrefix() {
        return "";
    }

    @Override
    public final String typeNameSuffix() {
        return Expression.class.getSimpleName();
    }

    // IsMethodTesting.................................................................................................

    @Override
    public final N createIsMethodObject() {
        return Cast.to(this.createNode());
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> m.equals("isRoot") ||
            m.equals("parentOrFail") ||
            m.equals("isPure") ||
            m.equals("isEmpty") ||
            m.equals("isNotEmpty") ||
            m.equals("isArithmetic") ||
            m.equals("isCompare") ||
            m.equals("isLogical");
    }

    @Override
    public final String toIsMethodName(final String typeName) {
        return this.toIsMethodNameWithPrefixSuffix(
            typeName,
            "", // prefix
            Expression.class.getSimpleName() // suffix
        );
    }

    // ClassTestCase.........................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
