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

import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.tree.expression.ExpressionNumberConverterContextDelegatorTest.TestExpressionNumberConverterContextDelegator;

import java.math.MathContext;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.util.Locale;

public final class ExpressionNumberConverterContextDelegatorTest implements ExpressionNumberConverterContextTesting<TestExpressionNumberConverterContextDelegator> {

    @Override
    public TestExpressionNumberConverterContextDelegator createContext() {
        return new TestExpressionNumberConverterContextDelegator();
    }

    @Override
    public String currencySymbol() {
        return new TestExpressionNumberConverterContextDelegator()
            .currencySymbol();
    }

    @Override
    public char decimalSeparator() {
        return new TestExpressionNumberConverterContextDelegator()
            .decimalSeparator();
    }

    @Override
    public String exponentSymbol() {
        return new TestExpressionNumberConverterContextDelegator()
            .exponentSymbol();
    }

    @Override
    public char groupSeparator() {
        return new TestExpressionNumberConverterContextDelegator()
            .groupSeparator();
    }

    @Override
    public MathContext mathContext() {
        return new TestExpressionNumberConverterContextDelegator()
            .mathContext();
    }

    @Override
    public char negativeSign() {
        return new TestExpressionNumberConverterContextDelegator()
            .negativeSign();
    }

    @Override
    public char percentSymbol() {
        return new TestExpressionNumberConverterContextDelegator()
            .percentSymbol();
    }

    @Override
    public char positiveSign() {
        return new TestExpressionNumberConverterContextDelegator()
            .positiveSign();
    }

    // class............................................................................................................

    @Override
    public Class<TestExpressionNumberConverterContextDelegator> type() {
        return TestExpressionNumberConverterContextDelegator.class;
    }

    final static class TestExpressionNumberConverterContextDelegator implements ExpressionNumberConverterContextDelegator {

        @Override
        public ExpressionNumberConverterContext expressionNumberConverterContext() {
            final Locale locale = Locale.ENGLISH;

            return ExpressionNumberConverterContexts.basic(
                Converters.numberToBoolean(),
                ConverterContexts.basic(
                    0,
                    Converters.fake(),
                    DateTimeContexts.basic(
                        DateTimeSymbols.fromDateFormatSymbols(
                            new DateFormatSymbols(locale)
                        ),
                        locale,
                        1900,
                        50,
                        LocalDateTime::now
                    ),
                    DecimalNumberContexts.american(MathContext.DECIMAL32)
                ),
                ExpressionNumberKind.BIG_DECIMAL
            );
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
