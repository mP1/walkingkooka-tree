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
import walkingkooka.ToStringTesting;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverter;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberConverterIntermediateTest implements ConverterTesting2<ExpressionNumberConverterIntermediate<ExpressionNumberConverterContext>, ExpressionNumberConverterContext>,
        ToStringTesting<ExpressionNumberConverterIntermediate<ExpressionNumberConverterContext>> {

    private final static String STRING_TO_EXPRESSION_NUMBER = "String->ExpressionNumber";

    private final static Converter<ExpressionNumberConverterContext> EXPRESSION_NUMBER_TO_NUMBER_CONVERTER = ExpressionNumber.fromConverter(
            Converters.numberNumber()
    );

    // with..........................................................................................................

    @Test
    public void testWithNullToExpressionNumberConverterFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionNumberConverterIntermediate.with(
                        null,
                        Converters.fake()
                )
        );
    }

    @Test
    public void testWithNullFromExpressionNumberConverterFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionNumberConverterIntermediate.with(
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
                ExpressionNumberConverterIntermediate.with(
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
                ExpressionNumberConverterIntermediate.with(
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
                ExpressionNumberConverterIntermediate.with(
                        new Converter<>() {
                            @Override
                            public boolean canConvert(final Object value,
                                                      final Class<?> type,
                                                      final ExpressionNumberConverterContext context) {
                                checkEquals(input, value);
                                checkEquals(kind.numberType(), type);
                                return true;
                            }

                            @Override
                            public <T> Either<T, String> convert(final Object value,
                                                                 final Class<T> type,
                                                                 final ExpressionNumberConverterContext context) {
                                checkEquals(input, value);
                                checkEquals(kind.numberType(), type);
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
                ExpressionNumberConverterIntermediate.with(
                        new Converter<>() {
                            @Override
                            public boolean canConvert(final Object value,
                                                      final Class<?> type,
                                                      final ExpressionNumberConverterContext context) {
                                checkEquals(input, value);
                                checkEquals(kind.numberType(), type);
                                return true;
                            }

                            @Override
                            public <T> Either<T, String> convert(final Object value,
                                                                 final Class<T> type,
                                                                 final ExpressionNumberConverterContext context) {
                                checkEquals(input, value);
                                checkEquals(kind.numberType(), type);
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
                ExpressionNumberConverterIntermediate.with(
                        new Converter<>() {
                            @Override
                            public boolean canConvert(final Object value,
                                                      final Class<?> type,
                                                      final ExpressionNumberConverterContext context) {
                                checkEquals(input, value);
                                checkEquals(kind.numberType(), type);
                                return true;
                            }

                            @Override
                            public <T> Either<T, String> convert(final Object value,
                                                                 final Class<T> type,
                                                                 final ExpressionNumberConverterContext context) {
                                checkEquals(input, value);
                                checkEquals(kind.numberType(), type);
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
                ExpressionNumberConverterIntermediate.with(
                        new Converter<>() {
                            @Override
                            public boolean canConvert(final Object value,
                                                      final Class<?> type,
                                                      final ExpressionNumberConverterContext context) {
                                checkEquals(input, value);
                                checkEquals(kind.numberType(), type);
                                return true;
                            }

                            @Override
                            public <T> Either<T, String> convert(final Object value,
                                                                 final Class<T> type,
                                                                 final ExpressionNumberConverterContext context) {
                                checkEquals(input, value);
                                checkEquals(kind.numberType(), type);
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

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createConverter(),
                STRING_TO_EXPRESSION_NUMBER + "->" + EXPRESSION_NUMBER_TO_NUMBER_CONVERTER
        );
    }

    // helpers..........................................................................................................

    @Override
    public ExpressionNumberConverterIntermediate<ExpressionNumberConverterContext> createConverter() {
        return ExpressionNumberConverterIntermediate.with(
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

    // class............................................................................................................

    @Override
    public Class<ExpressionNumberConverterIntermediate<ExpressionNumberConverterContext>> type() {
        return Cast.to(ExpressionNumberConverterIntermediate.class);
    }

    // type naming......................................................................................................

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
