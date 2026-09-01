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

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.convert.BinaryNumberConverterFunction;
import walkingkooka.convert.BinaryNumberConverterFunctions;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.currency.CurrencyLocaleContextTesting;
import walkingkooka.datetime.DateTimeContextTesting;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.HasExpressionNumberKindTesting;

import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberConverterContextBasicTest implements ExpressionNumberConverterContextTesting2<ExpressionNumberConverterContextBasic>,
    ToStringTesting<ExpressionNumberConverterContextBasic>,
    CurrencyLocaleContextTesting,
    DateTimeContextTesting,
    DecimalNumberContextDelegator,
    HasExpressionNumberKindTesting {

    private final static Converter<ExpressionNumberConverterContext> CONVERTER = ExpressionNumberConverters.numberToNumber();

    private final static BinaryNumberConverterFunction<ExpressionNumberConverterContext> MULTIPLIER = ExpressionNumberBinaryNumberConverterFunctions.multiply();

    private final static ConverterContext CONVERTER_CONTEXT = ConverterContexts.basic(
        false, // canNumbersHaveGroupSeparator
        Converters.JAVA_EPOCH_OFFSET, // dateOffset
        ',', // valueSeparator
        Converters.fake(),
        BinaryNumberConverterFunctions.fake(), // multiplier
        BINARY_TEXT_CONTEXT,
        CURRENCY_LOCALE_CONTEXT,
        DATE_TIME_CONTEXT,
        DECIMAL_NUMBER_CONTEXT
    );

    @Test
    public void testWithNullConverterFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionNumberConverterContextBasic.with(
                null,
                MULTIPLIER,
                CONVERTER_CONTEXT,
                EXPRESSION_NUMBER_KIND
            )
        );
    }

    @Test
    public void testWithNullMultiplierFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionNumberConverterContextBasic.with(
                CONVERTER,
                null,
                CONVERTER_CONTEXT,
                EXPRESSION_NUMBER_KIND
            )
        );
    }

    @Test
    public void testWithNullConverterContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionNumberConverterContextBasic.with(
                CONVERTER,
                MULTIPLIER,
                null,
                EXPRESSION_NUMBER_KIND
            )
        );
    }

    @Test
    public void testWithNullExpressionNumberKindFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionNumberConverterContextBasic.with(
                CONVERTER,
                MULTIPLIER,
                CONVERTER_CONTEXT,
                null
            )
        );
    }

    @Test
    public void testConvert() {
        this.convertAndCheck(123, Float.class, 123f);
    }

    @Test
    public void testMultiplyNumberNumberInteger() {
        this.multiplyAndCheck(
            this.createContext(),
            2,
            3,
            Integer.class,
            6
        );
    }

    @Test
    public void testMultiplyNumberNumberNumber() {
        this.multiplyAndCheck(
            this.createContext(),
            2,
            3,
            Number.class,
            6
        );
    }

    @Test
    public void testMultiplyNumberNumberExpressionNumber() {
        this.multiplyAndCheck(
            this.createContext(),
            2,
            3,
            ExpressionNumber.class,
            EXPRESSION_NUMBER_KIND.create(6)
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createContext(),
            CONVERTER_CONTEXT + " " + EXPRESSION_NUMBER_KIND
        );
    }

    @Override
    public ExpressionNumberConverterContextBasic createContext() {
        return ExpressionNumberConverterContextBasic.with(
            CONVERTER,
            MULTIPLIER,
            CONVERTER_CONTEXT,
            EXPRESSION_NUMBER_KIND
        );
    }

    // DecimalNumberContext.............................................................................................

    @Override
    public int decimalNumberDigitCount() {
        return DECIMAL_NUMBER_CONTEXT.decimalNumberDigitCount();
    }

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return DECIMAL_NUMBER_CONTEXT;
    }

    @Override
    public MathContext mathContext() {
        return DECIMAL_NUMBER_CONTEXT.mathContext();
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionNumberConverterContextBasic> type() {
        return ExpressionNumberConverterContextBasic.class;
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
