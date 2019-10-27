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
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.Name;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.IsMethodTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticFactoryTesting;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.tree.Node;
import walkingkooka.tree.NodeTesting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ExpressionNodeTestCase<N extends ExpressionNode> implements ClassTesting2<ExpressionNode>,
        IsMethodTesting<N>,
        NodeTesting<ExpressionNode, ExpressionNodeName, Name, Object> {

    ExpressionNodeTestCase() {
        super();
    }

    @Test
    public final void testPublicStaticFactoryMethod() {
        PublicStaticFactoryTesting.check(ExpressionNode.class,
                "Expression",
                Node.class,
                this.type());
    }

    @Override
    public final void testSetSameAttributes() {
        // Ignored
    }

    @Override
    public ExpressionNode createNode() {
        return this.createExpressionNode();
    }

    abstract N createExpressionNode();

    @Override
    public final Class<ExpressionNode> type() {
        return Cast.to(this.expressionNodeType());
    }

    abstract Class<N> expressionNodeType();

    // evaluation........................................................................................................

    final ExpressionBooleanNode booleanValue(final boolean value) {
        return ExpressionNode.booleanNode(value);
    }

    final ExpressionBigDecimalNode bigDecimal(final double value) {
        return ExpressionNode.bigDecimal(BigDecimal.valueOf(value));
    }

    final ExpressionBigIntegerNode bigInteger(final long value) {
        return ExpressionNode.bigInteger(BigInteger.valueOf(value));
    }

    final ExpressionDoubleNode doubleValue(final double value) {
        return ExpressionNode.doubleNode(value);
    }

    final LocalDate localDateValue(final long value) {
        return Converters.numberLocalDate(Converters.JAVA_EPOCH_OFFSET)
                .convertOrFail(value, LocalDate.class, this.converterContext());
    }

    final ExpressionLocalDateNode localDate(final long value) {
        return ExpressionNode.localDate(localDateValue(value));
    }

    final LocalDateTime localDateTimeValue(final double value) {
        return Converters.numberLocalDateTime(Converters.JAVA_EPOCH_OFFSET)
                .convertOrFail(value, LocalDateTime.class, this.converterContext());
    }

    final ExpressionLocalDateTimeNode localDateTime(final double value) {
        return ExpressionNode.localDateTime(localDateTimeValue(value));
    }

    final LocalTime localTimeValue(final long value) {
        return Converters.numberLocalTime()
                .convertOrFail(value, LocalTime.class, this.converterContext());
    }

    final ExpressionLocalTimeNode localTime(final long value) {
        return ExpressionNode.localTime(localTimeValue(value));
    }

    final ExpressionLongNode longValue(final long value) {
        return ExpressionNode.longNode(value);
    }

    final ExpressionTextNode text(final String value) {
        return ExpressionNode.text(value);
    }

    final ExpressionTextNode text(final Object value) {
        return text(context().convertOrFail(value, String.class));
    }

    final String textText(final ExpressionValueNode value) {
        return value.toText(context());
    }

    final void evaluateAndCheckBoolean(final ExpressionNode node, final boolean expected) {
        this.evaluateAndCheckBoolean(node, context(), expected);
    }

    final void evaluateAndCheckBoolean(final ExpressionNode node, final ExpressionEvaluationContext context, final boolean expected) {
        this.checkEquals("toBoolean of " + node + " failed", expected, node.toBoolean(context));
    }

    final void evaluateAndCheckBigDecimal(final ExpressionNode node, final double expected) {
        this.evaluateAndCheckBigDecimal(node, BigDecimal.valueOf(expected));
    }

    final void evaluateAndCheckBigDecimal(final ExpressionNode node, final BigDecimal expected) {
        this.evaluateAndCheckBigDecimal(node, context(), expected);
    }

    final void evaluateAndCheckBigDecimal(final ExpressionNode node, final ExpressionEvaluationContext context, final double expected) {
        this.evaluateAndCheckBigDecimal(node, context, BigDecimal.valueOf(expected));
    }

    final void evaluateAndCheckBigDecimal(final ExpressionNode node, final ExpressionEvaluationContext context, final BigDecimal expected) {
        this.checkEquals("toBigDecimal of " + node + " failed", expected, node.toBigDecimal(context));
    }

    final void evaluateAndCheckBigInteger(final ExpressionNode node, final long expected) {
        this.evaluateAndCheckBigInteger(node, BigInteger.valueOf(expected));
    }

    final void evaluateAndCheckBigInteger(final ExpressionNode node, final BigInteger expected) {
        this.evaluateAndCheckBigInteger(node, context(), expected);
    }

    final void evaluateAndCheckBigInteger(final ExpressionNode node, final ExpressionEvaluationContext context, final long expected) {
        this.evaluateAndCheckBigInteger(node, context, BigInteger.valueOf(expected));
    }

    final void evaluateAndCheckBigInteger(final ExpressionNode node, final ExpressionEvaluationContext context, final BigInteger expected) {
        this.checkEquals("toBigInteger of " + node + " failed", expected, node.toBigInteger(context));
    }

    final void evaluateAndCheckDouble(final ExpressionNode node, final double expected) {
        this.evaluateAndCheckDouble(node, context(), expected);
    }

    final void evaluateAndCheckDouble(final ExpressionNode node, final ExpressionEvaluationContext context, final double expected) {
        this.checkEquals("toDouble of " + node + " failed", expected, node.toDouble(context));
    }

    final void evaluateAndCheckLocalDate(final ExpressionNode node, final long expected) {
        this.evaluateAndCheckLocalDate(node, this.localDateValue(expected));
    }

    final void evaluateAndCheckLocalDate(final ExpressionNode node, final LocalDate expected) {
        this.evaluateAndCheckLocalDate(node, context(), expected);
    }

    final void evaluateAndCheckLocalDate(final ExpressionNode node, final ExpressionEvaluationContext context, final long expected) {
        this.evaluateAndCheckLocalDate(node, context, this.localDateValue(expected));
    }

    final void evaluateAndCheckLocalDate(final ExpressionNode node, final ExpressionEvaluationContext context, final LocalDate expected) {
        this.checkEquals("toLocalDate of " + node + " failed", expected, node.toLocalDate(context));
    }

    final void evaluateAndCheckLocalDateTime(final ExpressionNode node, final double expected) {
        this.evaluateAndCheckLocalDateTime(node, localDateTimeValue(expected));
    }

    final void evaluateAndCheckLocalDateTime(final ExpressionNode node, final LocalDateTime expected) {
        this.evaluateAndCheckLocalDateTime(node, context(), expected);
    }

    final void evaluateAndCheckLocalDateTime(final ExpressionNode node, final ExpressionEvaluationContext context, final double expected) {
        this.evaluateAndCheckLocalDateTime(node, context, localDateTimeValue(expected));
    }

    final void evaluateAndCheckLocalDateTime(final ExpressionNode node, final ExpressionEvaluationContext context, final LocalDateTime expected) {
        this.checkEquals("toLocalDateTime of " + node + " failed", expected, node.toLocalDateTime(context));
    }

    final void evaluateAndCheckLocalTime(final ExpressionNode node, final long expected) {
        this.evaluateAndCheckLocalTime(node, this.localTimeValue(expected));
    }

    final void evaluateAndCheckLocalTime(final ExpressionNode node, final LocalTime expected) {
        this.evaluateAndCheckLocalTime(node, context(), expected);
    }

    final void evaluateAndCheckLocalTime(final ExpressionNode node, final ExpressionEvaluationContext context, final long expected) {
        this.evaluateAndCheckLocalTime(node, context, this.localTimeValue(expected));
    }

    final void evaluateAndCheckLocalTime(final ExpressionNode node, final ExpressionEvaluationContext context, final LocalTime expected) {
        this.checkEquals("toLocalTime of " + node + " failed", expected, node.toLocalTime(context));
    }

    final void evaluateAndCheckLong(final ExpressionNode node, final long expected) {
        this.evaluateAndCheckLong(node, context(), expected);
    }

    final void evaluateAndCheckLong(final ExpressionNode node, final ExpressionEvaluationContext context, final long expected) {
        this.checkEquals("toLong of " + node + " failed", expected, node.toLong(context));
    }

    final void evaluateAndCheckNumberBigDecimal(final ExpressionNode node, final ExpressionEvaluationContext context, final double expected) {
        this.evaluateAndCheckNumber(node, context, BigDecimal.valueOf(expected));
    }

    final void evaluateAndCheckNumberBigDecimal(final ExpressionNode node, final double expected) {
        this.evaluateAndCheckNumberBigDecimal(node, BigDecimal.valueOf(expected));
    }

    final void evaluateAndCheckNumberBigDecimal(final ExpressionNode node, final BigDecimal expected) {
        this.evaluateAndCheckNumber(node, expected);
    }

    final void evaluateAndCheckNumberBigInteger(final ExpressionNode node, final long expected) {
        this.evaluateAndCheckNumberBigInteger(node, BigInteger.valueOf(expected));
    }

    final void evaluateAndCheckNumberBigInteger(final ExpressionNode node, final BigInteger expected) {
        this.evaluateAndCheckNumber(node, expected);
    }

    final void evaluateAndCheckNumberDouble(final ExpressionNode node, final double expected) {
        this.evaluateAndCheckNumber(node, expected);
    }

    final void evaluateAndCheckNumberLong(final ExpressionNode node, final long expected) {
        this.evaluateAndCheckNumber(node, expected);
    }

    final void evaluateAndCheckNumber(final ExpressionNode node, final Number expected) {
        this.evaluateAndCheckNumber(node, context(), expected);
    }

    final void evaluateAndCheckNumber(final ExpressionNode node, final ExpressionEvaluationContext context, final Number expected) {
        this.checkEquals("toNumber of " + node + " failed", Cast.to(expected), Cast.to(node.toNumber(context)));
    }

    final void evaluateAndCheckText(final ExpressionNode node, final String expected) {
        this.evaluateAndCheckText(node, context(), expected);
    }

    final void evaluateAndCheckText(final ExpressionNode node, final ExpressionEvaluationContext context, final String expected) {
        this.checkEquals("toText of " + node + " failed", expected, node.toText(context));
    }

    final void evaluateAndCheckValue(final ExpressionNode node, final Object expected) {
        this.evaluateAndCheckValue(node, context(), expected);
    }

    final void evaluateAndCheckValue(final ExpressionNode node, final ExpressionEvaluationContext context, final Object expected) {
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

    private ConverterContext converterContext() {
        return ConverterContexts.basic(DateTimeContexts.locale(Locale.ENGLISH, 20), DecimalNumberContexts.american(MathContext.DECIMAL32));
    }

    ExpressionEvaluationContext context() {
        final Function<ConverterContext, ParserContext> parserContext = (c) -> ParserContexts.basic(c, c);

        final Converter stringBigDecimal = Converters.parser(BigDecimal.class,
                Parsers.bigDecimal(),
                parserContext);
        final Converter stringNumber = Converters.parser(Number.class,
                Parsers.bigDecimal(),
                parserContext);
        final Converter stringBigInteger = Converters.parser(BigInteger.class,
                Parsers.bigInteger(10),
                parserContext);
        final Converter stringDouble = Converters.parser(Double.class,
                Parsers.doubleParser(),
                parserContext);
        final Converter stringLocalDate = Converters.parser(LocalDate.class,
                Parsers.localDate((c) -> DateTimeFormatter.ISO_LOCAL_DATE),
                parserContext);
        final Converter stringLocalDateTime = Converters.parser(LocalDateTime.class,
                Parsers.localDateTime((c) -> DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                parserContext);
        final Converter stringLocalTime = Converters.parser(LocalTime.class,
                Parsers.localTime((c) -> DateTimeFormatter.ISO_LOCAL_TIME),
                parserContext);
        final Converter stringLong = Converters.parser(Long.class,
                Parsers.longParser(10),
                parserContext);

        final Converter converters = Converters.collection(Lists.of(
                Converters.simple(),
                // localDate ->
                toBoolean(LocalDate.class, LocalDate.ofEpochDay(0)),
                Converters.localDateLocalDateTime(),
                Converters.localDateNumber(Converters.JAVA_EPOCH_OFFSET),
                Converters.localDateString((c) -> DateTimeFormatter.ISO_LOCAL_DATE),
                // localDateTime ->
                toBoolean(LocalDateTime.class, LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC)),
                Converters.localDateTimeLocalDate(),
                Converters.localDateTimeLocalTime(),
                Converters.localDateTimeNumber(Converters.JAVA_EPOCH_OFFSET),
                Converters.localDateTimeString((c) -> DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                // localTime
                toBoolean(LocalTime.class, LocalTime.ofNanoOfDay(0)),
                Converters.localTimeLocalDateTime(),
                Converters.localTimeNumber(),
                Converters.localTimeString((c) -> DateTimeFormatter.ISO_LOCAL_TIME),
                // number ->
                Converters.numberNumber(),
                Converters.truthyNumberBoolean(),
                Converters.numberLocalDate(Converters.JAVA_EPOCH_OFFSET),
                Converters.numberLocalDateTime(Converters.JAVA_EPOCH_OFFSET),
                Converters.numberLocalTime(),
                Converters.numberString((c) -> new DecimalFormat("#.###")),
                // string ->
                stringBigDecimal,
                stringBigInteger,
                Converters.function(String.class, Boolean.class, Boolean::valueOf),
                stringDouble,
                stringLocalDate,
                stringLocalDateTime,
                stringLocalTime,
                stringLong,
                stringNumber,
                Converters.objectString(),
                // boolean ->
                fromBoolean(BigDecimal.class, Converters.numberNumber()),
                fromBoolean(BigInteger.class, Converters.numberNumber()),
                fromBoolean(Double.class, Converters.numberNumber()),
                fromBoolean(LocalDate.class, Converters.numberLocalDate(Converters.JAVA_EPOCH_OFFSET)),
                fromBoolean(LocalDateTime.class, Converters.numberLocalDateTime(Converters.JAVA_EPOCH_OFFSET)),
                fromBoolean(LocalTime.class, Converters.numberLocalTime()),
                fromBoolean(Long.class, Converters.numberNumber())));

        return new FakeExpressionEvaluationContext() {

            @Override
            public String currencySymbol() {
                return "$";
            }

            @Override
            public char decimalSeparator() {
                return '.';
            }

            @Override
            public char exponentSymbol() {
                return 'E';
            }

            @Override
            public char groupingSeparator() {
                return ',';
            }

            @Override
            public char negativeSign() {
                return '-';
            }

            @Override
            public char percentageSymbol() {
                return '%';
            }

            @Override
            public char positiveSign() {
                return '+';
            }

            @Override
            public MathContext mathContext() {
                return this.matchContext;
            }

            private final MathContext matchContext = MathContext.DECIMAL64;

            @Override
            public <T> Either<T, String> convert(final Object value, final Class<T> target) {
//                if (!(value instanceof Boolean ||
//                        value instanceof LocalDate ||
//                        value instanceof LocalDateTime ||
//                        value instanceof LocalTime ||
//                        value instanceof Number ||
//                        value instanceof String)) {
//                    fail("Cannot convert expects only Boolean | LocalDate | LocalDateTime, LocalTime | Number | String " + value.getClass().getName() + "=" + value);
//                }
                return converters.convert(value, target, ExpressionNodeTestCase.this.converterContext());
            }
        };
    }

    private static <T> Converter fromBoolean(final Class<T> targetType, final Converter trueOrFalse) {
        final ConverterContext context = ConverterContexts.fake();
        return Converters.booleanTrueFalse(Boolean.class,
                Boolean.FALSE,
                targetType,
                trueOrFalse.convertOrFail(1L, targetType, context),
                trueOrFalse.convertOrFail(0L, targetType, context));
    }

    private static <S> Converter toBoolean(final Class<S> sourceType, final S falseValue) {
        return Converters.booleanTrueFalse(sourceType,
                falseValue,
                Boolean.class,
                Boolean.TRUE,
                Boolean.FALSE);
    }

    @Override
    public final String typeNamePrefix() {
        return "Expression";
    }

    // IsMethodTesting.................................................................................................

    @Override
    public final N createIsMethodObject() {
        return Cast.to(this.createNode());
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return "Expression";
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return Node.class.getSimpleName();
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
