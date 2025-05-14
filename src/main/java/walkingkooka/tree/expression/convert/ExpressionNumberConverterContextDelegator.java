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

import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContextDelegator;
import walkingkooka.tree.expression.ExpressionNumberKind;

public interface ExpressionNumberConverterContextDelegator extends ExpressionNumberConverterContext,
    ConverterContextDelegator {

    @Override
    default ConverterContext converterContext() {
        return this.expressionNumberConverterContext();
    }

    @Override
    default ExpressionNumberKind expressionNumberKind() {
        return this.expressionNumberConverterContext()
            .expressionNumberKind();
    }

    ExpressionNumberConverterContext expressionNumberConverterContext();
}
