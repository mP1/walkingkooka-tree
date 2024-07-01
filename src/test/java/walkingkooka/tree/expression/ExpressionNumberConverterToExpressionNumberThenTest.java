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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverter;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberConverterToExpressionNumberThenTest implements ConverterTesting2<ExpressionNumberConverterToExpressionNumberThen<ExpressionNumberConverterContext>, ExpressionNumberConverterContext>,
        HashCodeEqualsDefinedTesting2<ExpressionNumberConverterToExpressionNumberThen<ExpressionNumberConverterContext>>,
        ToStringTesting<ExpressionNumberConverterToExpressionNumberThen<ExpressionNumberConverterContext>> {

    private final static String STRING_TO_EXPRESSION_NUMBER = "String to ExpressionNumber";

    private final static Converter<ExpressionNumberConverterContext> EXPRESSION_NUMBER_TO_NUMBER_CONVERTER = ExpressionNumberConverters.numberOrExpressionNumberToNumber()
            .to(
                    Number.class,
                    Converters.numberToNumber()
            );

    // with..........................................................................................................

    @Test
    public void testWithNullToExpressionNumberConverterFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionNumberConverterToExpressionNumberThen.with(
                        null,
                        Converters.fake()
                )
        );
    }

    @Test
    public void testWithNullFromExpressionNumberConverterFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionNumberConverterToExpressionNumberThen.with(
                        Converters.fake(),
                        null
                )
        );
    }

    // input ExpressionNumber -> skips toExpressionNumber...............................................................

    @Test
    public void testConvertExpressionNumberBigDecimalToNonExpressionNumberAndToExpressionNumberConverterAndContextExpressionNumberKindBigDecimalFails() {
        this.convertExpressionNumberBigDecimalToNonExpressionNumberAndToExpressionNumberConverterFails(ExpressionNumberKind.BIG_DECIMAL);
    }

    @Test
    public void testConvertExpressionNumberBigDecimalToNonExpressionNumberAndToExpressionNumberConverterAndContextExpressionNumberKindDoubleFails() {
        this.convertExpressionNumberBigDecimalToNonExpressionNumberAndToExpressionNumberConverterFails(ExpressionNumberKind.DOUBLE);
    }

    private void convertExpressionNumberBigDecimalToNonExpressionNumberAndToExpressionNumberConverterFails(final ExpressionNumberKind kind) {
        final ExpressionNumber expressionNumber = kind.create(123);
        final Class<String> targetType = String.class;
        final String failMessage = "Fail message 123";

        this.convertFails(
                ExpressionNumberConverterToExpressionNumberThen.with(
                        Converters.fake(), // should not be called as input is already ExpressionNumber
                        new Converter<>() {
                            @Override
                            public boolean canConvert(final Object value,
                                                      final Class<?> type,
                                                      final ExpressionNumberConverterContext context) {
                                return true;
                            }

                            @Override
                            public <T> Either<T, String> convert(final Object value,
                                                                 final Class<T> type,
                                                                 final ExpressionNumberConverterContext context) {
                                checkEquals(expressionNumber, value);
                                checkEquals(targetType, type);
                                return Either.right(failMessage);
                            }
                        }
                ),
                expressionNumber,
                targetType,
                this.createContext(kind),
                failMessage
        );
    }

    @Test
    public void testConvertExpressionNumberToNonExpressionNumberAndContextExpressionNumberKindBigDecimal() {
        this.convertExpressionNumberToNonExpressionNumberAndCheck(ExpressionNumberKind.BIG_DECIMAL);
    }

    @Test
    public void testConvertExpressionNumberToNonExpressionNumberAndContextExpressionNumberKindDouble() {
        this.convertExpressionNumberToNonExpressionNumberAndCheck(ExpressionNumberKind.DOUBLE);
    }

    private void convertExpressionNumberToNonExpressionNumberAndCheck(final ExpressionNumberKind kind) {
        final ExpressionNumber expressionNumber = kind.create(123);
        final Class<Character> targetType = Character.class;
        final Character expected = '+';

        this.convertAndCheck(
                ExpressionNumberConverterToExpressionNumberThen.with(
                        Converters.fake(), // should not be called as input is already ExpressionNumber
                        new Converter<>() {
                            @Override
                            public boolean canConvert(final Object value,
                                                      final Class<?> type,
                                                      final ExpressionNumberConverterContext context) {
                                checkEquals(expressionNumber, value);
                                checkEquals(targetType, type);
                                return true;
                            }

                            @Override
                            public <T> Either<T, String> convert(final Object value,
                                                                 final Class<T> type,
                                                                 final ExpressionNumberConverterContext context) {
                                checkEquals(expressionNumber, value);
                                checkEquals(targetType, type);
                                return this.successfulConversion(
                                        expected,
                                        type
                                );
                            }
                        }
                ),
                expressionNumber,
                targetType,
                this.createContext(kind),
                expected
        );
    }

    // input NonExpressionNumber -> toExpressionNumber, fromExpressionNum ..............................................
    @Test
    public void testConvertNonExpressionNumberToNonExpressionNumberAndToExpressionNumberConverterAndContextExpressionNumberKindBigDecimalFails() {
        this.convertNonExpressionNumberToNonExpressionNumberAndExpressionNumberConverterFails(ExpressionNumberKind.BIG_DECIMAL);
    }

    @Test
    public void testConvertNonExpressionNumberToNonExpressionNumberAndToExpressionNumberConverterAndContextExpressionNumberKindDoubleFails() {
        this.convertNonExpressionNumberToNonExpressionNumberAndExpressionNumberConverterFails(ExpressionNumberKind.DOUBLE);
    }

    private void convertNonExpressionNumberToNonExpressionNumberAndExpressionNumberConverterFails(final ExpressionNumberKind kind) {
        final Integer input = 123;
        final Class<String> targetType = String.class;

        this.convertFails(
                ExpressionNumberConverterToExpressionNumberThen.with(
                        new Converter<>() {
                            @Override
                            public boolean canConvert(final Object value,
                                                      final Class<?> type,
                                                      final ExpressionNumberConverterContext context) {
                                checkEquals(input, value);
                                checkEquals(ExpressionNumber.class, type);
                                return true;
                            }

                            @Override
                            public <T> Either<T, String> convert(final Object value,
                                                                 final Class<T> type,
                                                                 final ExpressionNumberConverterContext context) {
                                checkEquals(input, value);
                                checkEquals(ExpressionNumber.class, type);
                                return Either.right("This message should not be returned to the caller");
                            }
                        },
                        Converters.fake()
                ),
                input,
                targetType,
                this.createContext(kind),
                "Failed to convert 123 (java.lang.Integer) to java.lang.String"
        );
    }

    @Test
    public void testConvertNonExpressionNumberFromExpressionNumberConverterBigDecimalFails() {
        this.convertNonExpressionNumberAndFromExpressionNumberConverterFails(ExpressionNumberKind.BIG_DECIMAL);
    }

    @Test
    public void testConvertNonExpressionNumberFromExpressionNumberConverterDoubleFails() {
        this.convertNonExpressionNumberAndFromExpressionNumberConverterFails(ExpressionNumberKind.DOUBLE);
    }

    private void convertNonExpressionNumberAndFromExpressionNumberConverterFails(final ExpressionNumberKind kind) {
        final Integer input = 123;
        final Class<String> targetType = String.class;
        final ExpressionNumber expressionNumber = kind.create(input);

        this.convertFails(
                ExpressionNumberConverterToExpressionNumberThen.with(
                        new Converter<>() {
                            @Override
                            public boolean canConvert(final Object value,
                                                      final Class<?> type,
                                                      final ExpressionNumberConverterContext context) {
                                checkEquals(input, value);
                                checkEquals(ExpressionNumber.class, type);
                                return true;
                            }

                            @Override
                            public <T> Either<T, String> convert(final Object value,
                                                                 final Class<T> type,
                                                                 final ExpressionNumberConverterContext context) {
                                checkEquals(input, value);
                                checkEquals(ExpressionNumber.class, type);
                                return this.successfulConversion(
                                        expressionNumber,
                                        type
                                );
                            }
                        },
                        new Converter<>() {
                            @Override
                            public boolean canConvert(final Object value,
                                                      final Class<?> type,
                                                      final ExpressionNumberConverterContext context) {
                                checkEquals(expressionNumber, value);
                                checkEquals(targetType, type);
                                return true;
                            }

                            @Override
                            public <T> Either<T, String> convert(final Object value,
                                                                 final Class<T> type,
                                                                 final ExpressionNumberConverterContext context) {
                                checkEquals(expressionNumber, value);
                                checkEquals(targetType, type);
                                return Either.right("This message should not be returned to the caller");
                            }
                        }
                ),
                input,
                targetType,
                this.createContext(kind),
                "Failed to convert 123 (java.lang.Integer) to java.lang.String"
        );
    }

    @Test
    public void testConvertNonExpressionNumberToNonExpressionNumberAndContextExpressionNumberKindBigDecimal() {
        this.convertNonExpressionNumberToNonExpressionNumberAndCheck(ExpressionNumberKind.BIG_DECIMAL);
    }

    @Test
    public void testConvertNonExpressionNumberToNonExpressionNumberAndContextExpressionNumberKindDouble() {
        this.convertNonExpressionNumberToNonExpressionNumberAndCheck(ExpressionNumberKind.DOUBLE);
    }

    private void convertNonExpressionNumberToNonExpressionNumberAndCheck(final ExpressionNumberKind kind) {
        final Integer input = 123;
        final Class<String> targetType = String.class;
        final ExpressionNumber expressionNumber = kind.create(input);
        final String expected = "*EXPECTED 123*";

        this.convertAndCheck(
                ExpressionNumberConverterToExpressionNumberThen.with(
                        new Converter<>() {
                            @Override
                            public boolean canConvert(final Object value,
                                                      final Class<?> type,
                                                      final ExpressionNumberConverterContext context) {
                                checkEquals(input, value);
                                checkEquals(ExpressionNumber.class, type);
                                return true;
                            }

                            @Override
                            public <T> Either<T, String> convert(final Object value,
                                                                 final Class<T> type,
                                                                 final ExpressionNumberConverterContext context) {
                                checkEquals(input, value);
                                checkEquals(ExpressionNumber.class, type);
                                return this.successfulConversion(
                                        expressionNumber,
                                        type
                                );
                            }
                        },
                        new Converter<>() {
                            @Override
                            public boolean canConvert(final Object value,
                                                      final Class<?> type,
                                                      final ExpressionNumberConverterContext context) {
                                checkEquals(expressionNumber, value);
                                checkEquals(targetType, type);
                                return true;
                            }

                            @Override
                            public <T> Either<T, String> convert(final Object value,
                                                                 final Class<T> type,
                                                                 final ExpressionNumberConverterContext context) {
                                checkEquals(expressionNumber, value);
                                checkEquals(targetType, type);
                                return this.successfulConversion(
                                        expected,
                                        type
                                );
                            }
                        }
                ),
                input,
                targetType,
                this.createContext(kind),
                expected
        );
    }


    @Test
    public void testConvertNonExpressionNumberToExpressionNumberAndContextExpressionNumberKindBigDecimal() {
        this.convertNonExpressionNumberToExpressionNumberAndCheck(ExpressionNumberKind.BIG_DECIMAL);
    }

    @Test
    public void testConvertNonExpressionNumberToExpressionNumberAndContextExpressionNumberKindDouble() {
        this.convertNonExpressionNumberToExpressionNumberAndCheck(ExpressionNumberKind.DOUBLE);
    }

    private void convertNonExpressionNumberToExpressionNumberAndCheck(final ExpressionNumberKind kind) {
        final Integer input = 123;
        final Class<ExpressionNumber> targetType = ExpressionNumber.class;
        final ExpressionNumber expressionNumber = kind.create(input);

        this.convertAndCheck(
                ExpressionNumberConverterToExpressionNumberThen.with(
                        new Converter<>() {
                            @Override
                            public boolean canConvert(final Object value,
                                                      final Class<?> type,
                                                      final ExpressionNumberConverterContext context) {
                                checkEquals(input, value);
                                checkEquals(ExpressionNumber.class, type);
                                return true;
                            }

                            @Override
                            public <T> Either<T, String> convert(final Object value,
                                                                 final Class<T> type,
                                                                 final ExpressionNumberConverterContext context) {
                                checkEquals(input, value);
                                checkEquals(ExpressionNumber.class, type);
                                return this.successfulConversion(
                                        expressionNumber,
                                        type
                                );
                            }
                        },
                        Converters.fake() // should be skipped because toExpressionConverter(1st) already returns ExpressionNumber
                ),
                input,
                targetType,
                this.createContext(kind),
                expressionNumber
        );
    }

    // convertExpressionNumberToExpressionNumber........................................................................

    @Test
    public void testConvertExpressionNumberToExpressionNumberBigDecimal() {
        this.convertAndCheck(
                ExpressionNumberKind.BIG_DECIMAL.create(123.5)
        );
    }

    @Test
    public void testConvertExpressionNumberToExpressionNumberDouble() {
        this.convertAndCheck(
                ExpressionNumberKind.DOUBLE.create(123.5)
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createConverter(),
                STRING_TO_EXPRESSION_NUMBER + " then " + EXPRESSION_NUMBER_TO_NUMBER_CONVERTER
        );
    }

    // helpers..........................................................................................................

    @Override
    public ExpressionNumberConverterToExpressionNumberThen<ExpressionNumberConverterContext> createConverter() {
        return ExpressionNumberConverterToExpressionNumberThen.with(
                new FakeConverter<>() {
                    @Override
                    public boolean canConvert(final Object value,
                                              final Class<?> type,
                                              final ExpressionNumberConverterContext context) {
                        return value instanceof String && ExpressionNumber.class == type ||
                                ExpressionNumber.class.isInstance(value);
                    }

                    @Override
                    public <T> Either<T, String> convert(final Object value,
                                                         final Class<T> type,
                                                         final ExpressionNumberConverterContext context) {
                        return this.canConvert(
                                value,
                                type,
                                context
                        ) ?
                                type.isInstance(value) ?
                                        context.successfulConversion(
                                                type.cast(value),
                                                type
                                        ) :
                                        parseString(
                                                (String) value,
                                                type,
                                                context
                                        ) :
                                context.failConversion(
                                        value,
                                        type
                                );
                    }

                    public String toString() {
                        return STRING_TO_EXPRESSION_NUMBER;
                    }
                },
                EXPRESSION_NUMBER_TO_NUMBER_CONVERTER
        );
    }

    private <T> Either<T, String> parseString(final String value,
                                              final Class<T> type,
                                              final ExpressionNumberConverterContext context) {
        try {
            return context.successfulConversion(
                    context.expressionNumberKind().parse(value),
                    type
            );
        } catch (final Exception cause) {
            return context.failConversion(
                    value,
                    type
            );
        }
    }

    @Override
    public ExpressionNumberConverterContext createContext() {
        return this.createContext(ExpressionNumberKind.DEFAULT);
    }

    private ExpressionNumberConverterContext createContext(final ExpressionNumberKind kind) {
        return new FakeExpressionNumberConverterContext() {
            @Override
            public ExpressionNumberKind expressionNumberKind() {
                return kind;
            }
        };
    }

    // hashCode/equals..................................................................................................

    private final static Converter<ExpressionNumberConverterContext> FIRST = Converters.fake();

    private final static Converter<ExpressionNumberConverterContext> SECOND = Converters.fake();

    @Test
    public void testEqualsDifferentToExpressionNumberConverter() {
        this.checkNotEquals(
                ExpressionNumberConverterToExpressionNumberThen.with(
                        Converters.fake(),
                        SECOND
                )
        );
    }

    @Test
    public void testEqualsDifferentFromExpressionNumberConverter() {
        this.checkNotEquals(
                ExpressionNumberConverterToExpressionNumberThen.with(
                        FIRST,
                        Converters.fake()
                )
        );
    }

    @Override
    public ExpressionNumberConverterToExpressionNumberThen<ExpressionNumberConverterContext> createObject() {
        return ExpressionNumberConverterToExpressionNumberThen.with(
                FIRST,
                SECOND
        );
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionNumberConverterToExpressionNumberThen<ExpressionNumberConverterContext>> type() {
        return Cast.to(ExpressionNumberConverterToExpressionNumberThen.class);
    }

    // type naming......................................................................................................

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
