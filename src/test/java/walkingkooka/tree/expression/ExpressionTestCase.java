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
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverter;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.Name;
import walkingkooka.predicate.Predicates;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.IsMethodTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticFactoryTesting;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.tree.NodeTesting;

import java.math.MathContext;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ExpressionTestCase<N extends Expression> implements ClassTesting2<Expression>,
        IsMethodTesting<N>,
        NodeTesting<Expression, FunctionExpressionName, Name, Object> {

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

    final BooleanExpression booleanValue(final boolean value) {
        return Expression.booleanExpression(value);
    }

    final ExpressionNumberExpression expressionNumber(final double value) {
        return Expression.expressionNumber(expressionNumberValue(value));
    }

    final ExpressionNumber expressionNumberValue(final double value) {
        return EXPRESSION_NUMBER_KIND.create(value);
    }

    final LocalDate localDateValue(final long value) {
        return Converters.numberLocalDate(Converters.JAVA_EPOCH_OFFSET)
                .convertOrFail(value, LocalDate.class, this.converterContext());
    }

    final LocalDateExpression localDate(final long value) {
        return Expression.localDate(localDateValue(value));
    }

    final LocalDateTime localDateTimeValue(final double value) {
        return Converters.numberLocalDateTime(Converters.JAVA_EPOCH_OFFSET)
                .convertOrFail(value, LocalDateTime.class, this.converterContext());
    }

    final LocalDateTimeExpression localDateTime(final double value) {
        return Expression.localDateTime(localDateTimeValue(value));
    }

    final LocalTime localTimeValue(final long value) {
        return Converters.numberLocalTime()
                .convertOrFail(value, LocalTime.class, this.converterContext());
    }

    final LocalTimeExpression localTime(final long value) {
        return Expression.localTime(localTimeValue(value));
    }

    final StringExpression text(final String value) {
        return Expression.string(value);
    }

    final StringExpression text(final Object value) {
        return text(context().convertOrFail(value, String.class));
    }

    final String textText(final ValueExpression value) {
        return value.toString(context());
    }

    final void evaluateAndCheckBoolean(final Expression node, final boolean expected) {
        this.evaluateAndCheckBoolean(node, context(), expected);
    }

    final void evaluateAndCheckBoolean(final Expression node, final ExpressionEvaluationContext context, final boolean expected) {
        this.checkEquals("toBoolean of " + node + " failed", expected, node.toBoolean(context));
    }

    final void evaluateAndCheckExpressionNumber(final Expression node,
                                                final double expected) {
        this.evaluateAndCheckExpressionNumber(node, expressionNumberValue(expected));
    }

    final void evaluateAndCheckExpressionNumber(final Expression node,
                                                final ExpressionNumber expected) {
        this.evaluateAndCheckExpressionNumber(node, this.context(), expected);
    }

    final void evaluateAndCheckExpressionNumber(final Expression node,
                                                final ExpressionEvaluationContext context,
                                                final ExpressionNumber expected) {
        this.checkEquals("toExpressionNumber of " + node + " failed", expected, node.toExpressionNumber(context));
    }

    final void evaluateAndCheckLocalDate(final Expression node, final long expected) {
        this.evaluateAndCheckLocalDate(node, this.localDateValue(expected));
    }

    final void evaluateAndCheckLocalDate(final Expression node, final LocalDate expected) {
        this.evaluateAndCheckLocalDate(node, context(), expected);
    }

    final void evaluateAndCheckLocalDate(final Expression node, final ExpressionEvaluationContext context, final LocalDate expected) {
        this.checkEquals("toLocalDate of " + node + " failed", expected, node.toLocalDate(context));
    }

    final void evaluateAndCheckLocalDateTime(final Expression node, final double expected) {
        this.evaluateAndCheckLocalDateTime(node, localDateTimeValue(expected));
    }

    final void evaluateAndCheckLocalDateTime(final Expression node, final LocalDateTime expected) {
        this.evaluateAndCheckLocalDateTime(node, context(), expected);
    }

    final void evaluateAndCheckLocalDateTime(final Expression node, final ExpressionEvaluationContext context, final LocalDateTime expected) {
        this.checkEquals("toLocalDateTime of " + node + " failed", expected, node.toLocalDateTime(context));
    }

    final void evaluateAndCheckLocalTime(final Expression node, final long expected) {
        this.evaluateAndCheckLocalTime(node, this.localTimeValue(expected));
    }

    final void evaluateAndCheckLocalTime(final Expression node, final LocalTime expected) {
        this.evaluateAndCheckLocalTime(node, context(), expected);
    }

    final void evaluateAndCheckLocalTime(final Expression node, final ExpressionEvaluationContext context, final LocalTime expected) {
        this.checkEquals("toLocalTime of " + node + " failed", expected, node.toLocalTime(context));
    }

    final void evaluateAndCheckText(final Expression node, final String expected) {
        this.evaluateAndCheckText(node, context(), expected);
    }

    final void evaluateAndCheckText(final Expression node, final ExpressionEvaluationContext context, final String expected) {
        this.checkEquals("toText of " + node + " failed", expected, node.toString(context));
    }

    final void evaluateAndCheckValue(final Expression node, final Object expected) {
        this.evaluateAndCheckValue(node, context(), expected);
    }

    final void evaluateAndCheckValue(final Expression node, final ExpressionEvaluationContext context, final Object expected) {
        final Object value = node.toValue(context);
        if (expected instanceof Comparable && value instanceof Comparable) {
            this.checkEquals("toValue of " + node + " failed", Cast.to(expected), Cast.to(value));
        } else {
            assertEquals(expected, value, () -> "toValue of " + node + " failed");
        }
    }

    private <T extends Comparable<T>> void checkEquals(final String message, final T expected, final T actual) {
        // necessary because BigDecimals of different precisions (extra zeros) will not be equal.
        if (expected.getClass() != actual.getClass() || 0 != expected.compareTo(actual)) {
            assertEquals(expected, actual, message);
        }
    }

    private ExpressionNumberConverterContext converterContext() {
        return ExpressionNumberConverterContexts.basic(Converters.simple(),
                ConverterContexts.basic(Converters.fake(),
                        DateTimeContexts.locale(Locale.ENGLISH, 20),
                        DecimalNumberContexts.american(MathContext.DECIMAL32)),
                EXPRESSION_NUMBER_KIND);
    }

    ExpressionEvaluationContext context() {
        final Function<ConverterContext, ParserContext> parserContext = (c) -> ParserContexts.basic(c, c);

        final Converter<ExpressionNumberConverterContext> stringNumber = Converters.parser(Number.class,
                Parsers.bigDecimal(),
                parserContext);
        final Converter<ExpressionNumberConverterContext> stringDouble = Converters.parser(Double.class,
                Parsers.doubleParser(),
                parserContext);
        final Converter<ExpressionNumberConverterContext> stringLocalDate = Converters.parser(LocalDate.class,
                Parsers.localDate((c) -> DateTimeFormatter.ISO_LOCAL_DATE),
                parserContext);
        final Converter<ExpressionNumberConverterContext> stringLocalDateTime = Converters.parser(LocalDateTime.class,
                Parsers.localDateTime((c) -> DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                parserContext);
        final Converter<ExpressionNumberConverterContext> stringLocalTime = Converters.parser(LocalTime.class,
                Parsers.localTime((c) -> DateTimeFormatter.ISO_LOCAL_TIME),
                parserContext);

        final Converter<ExpressionNumberConverterContext> converters = Converters.collection(Lists.of(
                Converters.simple(),
                new FakeConverter<ExpressionNumberConverterContext>() {
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
                                Cast.to(Either.left(this.toExpressionNumber((ExpressionNumber)value))) :
                                this.failConversion(value, type);
                    }

                    private ExpressionNumber toExpressionNumber(final ExpressionNumber value) {
                        return value.setKind(EXPRESSION_NUMBER_KIND);
                    }
                },
                // localDate ->
                toBoolean(LocalDate.class, LocalDate.ofEpochDay(0)),
                Converters.localDateLocalDateTime(),
                ExpressionNumber.toConverter(Converters.localDateNumber(Converters.JAVA_EPOCH_OFFSET)),
                Converters.localDateString((c) -> DateTimeFormatter.ISO_LOCAL_DATE),
                // localDateTime ->
                toBoolean(LocalDateTime.class, LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC)),
                Converters.localDateTimeLocalDate(),
                Converters.localDateTimeLocalTime(),
                ExpressionNumber.toConverter(Converters.localDateTimeNumber(Converters.JAVA_EPOCH_OFFSET)),
                Converters.localDateTimeString((c) -> DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                // localTime
                toBoolean(LocalTime.class, LocalTime.ofNanoOfDay(0)),
                Converters.localTimeLocalDateTime(),
                ExpressionNumber.toConverter(Converters.localTimeNumber()),
                Converters.localTimeString((c) -> DateTimeFormatter.ISO_LOCAL_TIME),
                // ExpressionNumber ->),
                ExpressionNumber.fromConverter(Converters.numberNumber()),
                ExpressionNumber.fromConverter(Converters.truthyNumberBoolean()),
                ExpressionNumber.fromConverter(Converters.numberLocalDate(Converters.JAVA_EPOCH_OFFSET)),
                ExpressionNumber.fromConverter(Converters.numberLocalDateTime(Converters.JAVA_EPOCH_OFFSET)),
                ExpressionNumber.fromConverter(Converters.numberLocalTime()),
                ExpressionNumber.fromConverter(Converters.numberString((c) -> new DecimalFormat("#.###"))),
                // Number ->),
                ExpressionNumber.fromConverter(Converters.numberNumber()),
                ExpressionNumber.fromConverter(Converters.truthyNumberBoolean()),
                ExpressionNumber.fromConverter(Converters.numberLocalDate(Converters.JAVA_EPOCH_OFFSET)),
                ExpressionNumber.fromConverter(Converters.numberLocalDateTime(Converters.JAVA_EPOCH_OFFSET)),
                ExpressionNumber.fromConverter(Converters.numberLocalTime()),
                ExpressionNumber.fromConverter(Converters.numberString((c) -> new DecimalFormat("#.###"))),
                // string ->
                Converters.<String, Boolean>function(v -> v instanceof String, Predicate.isEqual(Boolean.class), Boolean::valueOf),
                stringLocalDate,
                stringLocalDateTime,
                stringLocalTime,
                ExpressionNumber.toConverter(stringDouble),
                Converters.objectString(),
                // boolean ->
                listToBoolean(),
                fromBoolean(LocalDate.class, Converters.numberLocalDate(Converters.JAVA_EPOCH_OFFSET)),
                fromBoolean(LocalDateTime.class, Converters.numberLocalDateTime(Converters.JAVA_EPOCH_OFFSET)),
                fromBoolean(LocalTime.class, Converters.numberLocalTime()),
                fromBoolean(ExpressionNumber.class, ExpressionNumber.toConverter(Converters.numberNumber()))));

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
        };
    }

    /**
     * Converts an empty list to false, and non empty list to true.
     */
    private static Converter<ExpressionNumberConverterContext> listToBoolean() {
        return Converters.booleanTrueFalse((v) -> v instanceof List,
                emptyList(),
                Predicates.is(Boolean.class),
                Boolean.TRUE,
                Boolean.FALSE);
    }

    private static Predicate<Object> emptyList() {
        return (v) -> {
            final List<?> list = Cast.to(v);
            return list.isEmpty();
        };
    }

    private <T> Converter<ExpressionNumberConverterContext> fromBoolean(final Class<T> targetType,
                                                                        final Converter<ExpressionNumberConverterContext> trueOrFalse) {
        final ExpressionNumberConverterContext context = this.converterContext();
        return Converters.booleanTrueFalse((t)-> t instanceof Boolean,
                Predicate.isEqual(Boolean.FALSE),
                (t) -> t == targetType,
                trueOrFalse.convertOrFail(1L, targetType, context),
                trueOrFalse.convertOrFail(0L, targetType, context));
    }

    private static <S> Converter<ExpressionNumberConverterContext> toBoolean(final Class<S> sourceType, final S falseValue) {
        return Converters.booleanTrueFalse((t) -> t.getClass() == sourceType,
                Predicate.isEqual(falseValue),
                (t) -> t == Boolean.class,
                Boolean.TRUE,
                Boolean.FALSE);
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
    public final String isMethodTypeNamePrefix() {
        return "";
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return Expression.class.getSimpleName();
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> m.equals("isRoot") || m.equals("parentOrFail");
    }

    // ClassTestCase.........................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
