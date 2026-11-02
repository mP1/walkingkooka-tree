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

import walkingkooka.convert.BinaryNumberConverterFunctions;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.currency.CurrencyLocaleContextTesting;
import walkingkooka.datetime.DateTimeContextTesting;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.math.DecimalNumberContextTesting;
import walkingkooka.tree.expression.HasExpressionNumberKindTesting;
import walkingkooka.tree.expression.convert.TreeConverterContextDelegatorTest.TestTreeConverterContextDelegator;

import java.math.MathContext;

public final class TreeConverterContextDelegatorTest implements TreeConverterContextTesting2<TestTreeConverterContextDelegator>,
    CurrencyLocaleContextTesting,
    DateTimeContextTesting,
    DecimalNumberContextDelegator,
    DecimalNumberContextTesting,
    HasExpressionNumberKindTesting {

    @Override
    public TestTreeConverterContextDelegator createContext() {
        return new TestTreeConverterContextDelegator();
    }

    // DecimalNumberContextDelegator....................................................................................

    @Override
    public int decimalNumberDigitCount() {
        return DEFAULT_NUMBER_DIGIT_COUNT;
    }

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return DECIMAL_NUMBER_CONTEXT;
    }

    @Override
    public MathContext mathContext() {
        return MATH_CONTEXT;
    }

    // class............................................................................................................

    @Override
    public Class<TestTreeConverterContextDelegator> type() {
        return TestTreeConverterContextDelegator.class;
    }

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    final static class TestTreeConverterContextDelegator implements TreeConverterContextDelegator {

        @Override
        public TreeConverterContext treeConverterContext() {
            return TreeConverterContexts.basic(
                Converters.numberToBoolean(),
                EXPRESSION_NUMBER_KIND,
                BinaryNumberConverterFunctions.multiply(), // multiplier
                ConverterContexts.basic(
                    false, // canNumbersHaveGroupSeparator
                    0,
                    ',', // valueSeparator
                    Converters.fake(),
                    BinaryNumberConverterFunctions.multiply(), // multiplier
                    BINARY_TEXT_CONTEXT,
                    CURRENCY_LOCALE_CONTEXT,
                    DATE_TIME_CONTEXT,
                    DECIMAL_NUMBER_CONTEXT
                )
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
