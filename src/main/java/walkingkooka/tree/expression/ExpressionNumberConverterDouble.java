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

/**
 * A {@link walkingkooka.convert.Converter} that converts all {@link Number numbers} into a {@link }
 */
final class ExpressionNumberConverterDouble extends ExpressionNumberConverter {

    static ExpressionNumberConverterDouble with(final ExpressionNumberContext context) {
        checkContext(context);

        return new ExpressionNumberConverterDouble(context);
    }

    private ExpressionNumberConverterDouble(final ExpressionNumberContext context) {
        super(context);
    }

    @Override
    ExpressionNumber expressionNumber(final Object value) {
        return value instanceof ExpressionNumberDouble ?
                (ExpressionNumberDouble) value :
                this.expressionNumber0((Number) value);
    }

    private ExpressionNumber expressionNumber0(final Number value) {
        return ExpressionNumber.with(value.doubleValue());
    }

    String expressionNumberType() {
        return Double.class.getSimpleName();
    }
}
