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
import walkingkooka.ToStringTesting;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicExpressionNumberConverterContextTest implements ExpressionNumberConverterContextTesting<BasicExpressionNumberConverterContext>,
    ToStringTesting<BasicExpressionNumberConverterContext> {

    private final static ExpressionNumberKind KIND = ExpressionNumberKind.DEFAULT;
    private final static Converter<ExpressionNumberConverterContext> CONVERTER = Converters.numberToNumber();

    @Test
    public void testWithNullConverterFails() {
        assertThrows(NullPointerException.class, () -> BasicExpressionNumberConverterContext.with(null, this.converterContext(), KIND));
    }

    @Test
    public void testWithNullConverterContextFails() {
        assertThrows(NullPointerException.class, () -> BasicExpressionNumberConverterContext.with(CONVERTER, null, KIND));
    }

    @Test
    public void testWithNullExpressionNumberKindFails() {
        assertThrows(NullPointerException.class, () -> BasicExpressionNumberConverterContext.with(CONVERTER, this.converterContext(), null));
    }

    @Test
    public void testConvert() {
        this.convertAndCheck(123, Float.class, 123f);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createContext(), this.converterContext() + " " + KIND);
    }

    @Override
    public BasicExpressionNumberConverterContext createContext() {
        return BasicExpressionNumberConverterContext.with(CONVERTER, this.converterContext(), KIND);
    }

    private ConverterContext converterContext() {
        return ConverterContexts.basic(
            Converters.JAVA_EPOCH_OFFSET, // dateOffset
            Converters.fake(),
            DateTimeContexts.locale(
                Locale.forLanguageTag("EN-AU"),
                1900,
                20,
                LocalDateTime::now
            ),
            this.decimalNumberContext()
        );
    }

    private DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.american(MathContext.DECIMAL32);
    }

    @Override
    public String currencySymbol() {
        return this.decimalNumberContext().currencySymbol();
    }

    @Override
    public char decimalSeparator() {
        return this.decimalNumberContext().decimalSeparator();
    }

    @Override
    public String exponentSymbol() {
        return this.decimalNumberContext().exponentSymbol();
    }

    @Override
    public char groupSeparator() {
        return this.decimalNumberContext().groupSeparator();
    }

    @Override
    public MathContext mathContext() {
        return this.decimalNumberContext().mathContext();
    }

    @Override
    public char negativeSign() {
        return this.decimalNumberContext().negativeSign();
    }

    @Override
    public char percentageSymbol() {
        return this.decimalNumberContext().percentageSymbol();
    }

    @Override
    public char positiveSign() {
        return this.decimalNumberContext().positiveSign();
    }

    @Override
    public Class<BasicExpressionNumberConverterContext> type() {
        return BasicExpressionNumberConverterContext.class;
    }
}
