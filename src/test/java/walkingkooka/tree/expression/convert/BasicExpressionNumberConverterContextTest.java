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
import walkingkooka.currency.CurrencyCode;
import walkingkooka.currency.FakeCurrencyContext;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.math.MathContext;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicExpressionNumberConverterContextTest implements ExpressionNumberConverterContextTesting<BasicExpressionNumberConverterContext>,
    ToStringTesting<BasicExpressionNumberConverterContext>,
    DecimalNumberContextDelegator {

    private final static ExpressionNumberKind KIND = ExpressionNumberKind.DEFAULT;
    private final static Converter<ExpressionNumberConverterContext> CONVERTER = ExpressionNumberConverters.numberToNumber();

    private final static BinaryNumberConverterFunction<ExpressionNumberConverterContext> MULTIPLER = ExpressionNumberBinaryNumberConverterFunctions.multiply();

    @Test
    public void testWithNullConverterFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionNumberConverterContext.with(
                null,
                MULTIPLER,
                this.converterContext(),
                KIND
            )
        );
    }

    @Test
    public void testWithNullMultiplierFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionNumberConverterContext.with(
                CONVERTER,
                null,
                this.converterContext(),
                KIND
            )
        );
    }

    @Test
    public void testWithNullConverterContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionNumberConverterContext.with(
                CONVERTER,
                MULTIPLER,
                null,
                KIND
            )
        );
    }

    @Test
    public void testWithNullExpressionNumberKindFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionNumberConverterContext.with(
                CONVERTER,
                MULTIPLER,
                this.converterContext(),
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
            KIND.create(6)
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createContext(), this.converterContext() + " " + KIND);
    }

    @Override
    public BasicExpressionNumberConverterContext createContext() {
        return BasicExpressionNumberConverterContext.with(
            CONVERTER,
            MULTIPLER,
            this.converterContext(),
            KIND
        );
    }

    private ConverterContext converterContext() {
        final Locale locale = Locale.forLanguageTag("EN-AU");

        return ConverterContexts.basic(
            false, // canNumbersHaveGroupSeparator
            Converters.JAVA_EPOCH_OFFSET, // dateOffset
            Indentation.SPACES2,
            LineEnding.NL,
            ',', // valueSeparator
            Converters.fake(),
            BinaryNumberConverterFunctions.fake(), // multiplier
            new FakeCurrencyContext() {

                @Override
                public Optional<Currency> currencyForCurrencyCode(final CurrencyCode currencyCode) {
                    Objects.requireNonNull(currencyCode, "currencyCode");

                    throw new UnsupportedOperationException();
                }

                @Override
                public Optional<Currency> currencyForLocale(final Locale locale) {
                    return Optional.of(
                        Currency.getInstance(locale)
                    );
                }
            }.setLocaleContext(
                LocaleContexts.jre(locale)
            ),
            DateTimeContexts.basic(
                DateTimeSymbols.fromDateFormatSymbols(
                    new DateFormatSymbols(locale)
                ),
                locale,
                1900,
                20,
                LocalDateTime::now
            ),
            this.decimalNumberContext()
        );
    }

    // DecimalNumberContext.............................................................................................

    @Override
    public int decimalNumberDigitCount() {
        return this.decimalNumberContext()
            .decimalNumberDigitCount();
    }

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.american(this.mathContext());
    }

    @Override
    public MathContext mathContext() {
        return MathContext.DECIMAL32;
    }

    // class............................................................................................................

    @Override
    public Class<BasicExpressionNumberConverterContext> type() {
        return BasicExpressionNumberConverterContext.class;
    }
}
