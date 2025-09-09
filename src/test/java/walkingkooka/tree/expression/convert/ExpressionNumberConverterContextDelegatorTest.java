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

import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.convert.ExpressionNumberConverterContextDelegatorTest.TestExpressionNumberConverterContextDelegator;

import java.math.MathContext;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.util.Locale;

public final class ExpressionNumberConverterContextDelegatorTest implements ExpressionNumberConverterContextTesting<TestExpressionNumberConverterContextDelegator>,
    DecimalNumberContextDelegator {

    @Override
    public TestExpressionNumberConverterContextDelegator createContext() {
        return new TestExpressionNumberConverterContextDelegator();
    }

    // DecimalNumberContextDelegator....................................................................................

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return DECIMAL_CONTEXT;
    }

    @Override
    public MathContext mathContext() {
        return MathContext.DECIMAL32;
    }

    private final static DecimalNumberContext DECIMAL_CONTEXT = DecimalNumberContexts.american(MathContext.DECIMAL32);

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
                    false, // canNumbersHaveGroupSeparator
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
                    DECIMAL_CONTEXT
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
