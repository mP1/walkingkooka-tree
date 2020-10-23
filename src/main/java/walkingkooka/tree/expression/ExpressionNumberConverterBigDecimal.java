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

/**
 * A {@link walkingkooka.convert.Converter} that converts all {@link Number numbers} into a {@link }
 */
final class ExpressionNumberConverterBigDecimal extends ExpressionNumberConverter {

    static ExpressionNumberConverterBigDecimal with(final ExpressionNumberContext context) {
        checkContext(context);

        return new ExpressionNumberConverterBigDecimal(context);
    }

    private ExpressionNumberConverterBigDecimal(final ExpressionNumberContext context) {
        super(context);
    }

    @Override
    ExpressionNumber expressionNumber(final Object value) {
        return value instanceof ExpressionNumberDouble ?
                (ExpressionNumberDouble) value :
                this.expressionNumber0((Number) value);
    }

    private ExpressionNumber expressionNumber0(final Number value) {
        return ExpressionNumberConverterBigDecimalNumberVisitor.wrap(value, this.context);
    }

    @Override
    String expressionNumberType() {
        return BigDecimal.class.getSimpleName();
    }
}
