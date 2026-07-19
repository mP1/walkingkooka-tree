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

import walkingkooka.ToStringTesting;
import walkingkooka.convert.BinaryNumberConverterFunctions;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.convert.Converters;
import walkingkooka.currency.CurrencyLocaleContexts;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContextTesting;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.TextPrinting;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.nio.charset.StandardCharsets;

public abstract class ExpressionNumberConverterTestCase<C extends ExpressionNumberConverter<ExpressionNumberConverterContext>>
    implements ConverterTesting2<C, ExpressionNumberConverterContext>,
    DecimalNumberContextTesting,
    ToStringTesting<C> {

    private final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.BIG_DECIMAL;

    ExpressionNumberConverterTestCase() {
        super();
    }

    @Override
    public final ExpressionNumberConverterContext createContext() {
        return this.createContext(EXPRESSION_NUMBER_KIND);
    }

    final ExpressionNumberConverterContext createContext(final ExpressionNumberKind kind) {
        return ExpressionNumberConverterContexts.basic(
            Converters.fake(),
            BinaryNumberConverterFunctions.fake(), // multiplier
            ConverterContexts.basic(
                false, // canNumbersHaveGroupSeparator
                Converters.JAVA_EPOCH_OFFSET, // dateOffset
                ',', // valueSeparator
                Converters.fake(),
                BinaryNumberConverterFunctions.fake(), // multiplier
                TextPrinting.with(
                    Indentation.SPACES2,
                    LineEnding.NL
                ).setCharset(StandardCharsets.UTF_8),
                CurrencyLocaleContexts.fake(),
                DateTimeContexts.fake(),
                DECIMAL_NUMBER_CONTEXT
            ),
            kind
        );
    }

    // class............................................................................................................

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
