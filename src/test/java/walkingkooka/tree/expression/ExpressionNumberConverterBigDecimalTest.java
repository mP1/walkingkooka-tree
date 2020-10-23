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

import java.math.BigDecimal;

public final class ExpressionNumberConverterBigDecimalTest extends ExpressionNumberConverterTestCase<ExpressionNumberConverterBigDecimal> {
    @Override
    ExpressionNumber expressionNumberOne() {
        return ExpressionNumber.with(BigDecimal.ONE, CONTEXT);
    }

    @Override
    ExpressionNumber expressionNumberHalf() {
        return ExpressionNumber.with(BigDecimal.valueOf(0.5), CONTEXT);
    }

    @Override
    ExpressionNumberConverterBigDecimal createConverter(final ExpressionNumberContext context) {
        return ExpressionNumberConverterBigDecimal.with(context);
    }


    @Override
    public Class<ExpressionNumberConverterBigDecimal> type() {
        return ExpressionNumberConverterBigDecimal.class;
    }
}
