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

public final class ExpressionNumberConverterDoubleTest extends ExpressionNumberConverterTestCase<ExpressionNumberConverterDouble> {

    @Override
    ExpressionNumber expressionNumberOne() {
        return ExpressionNumber.with(1.0);
    }

    @Override
    ExpressionNumber expressionNumberHalf() {
        return ExpressionNumber.with(0.5);
    }

    @Override
    public ExpressionNumberConverterDouble createConverter() {
        return ExpressionNumberConverterDouble.instance();
    }

    @Override
    public Class<ExpressionNumberConverterDouble> type() {
        return ExpressionNumberConverterDouble.class;
    }
}
