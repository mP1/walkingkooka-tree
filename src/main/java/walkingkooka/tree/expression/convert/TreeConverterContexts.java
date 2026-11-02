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

import walkingkooka.convert.BinaryNumberConverterFunction;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.tree.expression.ExpressionNumberKind;

public final class TreeConverterContexts implements PublicStaticHelper {

    /**
     * {@see TreeConverterContextBasic}
     */
    public static TreeConverterContext basic(final Converter<TreeConverterContext> converter,
                                             final ExpressionNumberKind expressionNumberKind,
                                             final BinaryNumberConverterFunction<TreeConverterContext> multiplier,
                                             final ConverterContext context) {
        return TreeConverterContextBasic.with(
            converter,
            expressionNumberKind,
            multiplier,
            context
        );
    }

    /**
     * {@see FakeExpressionNumberContext}
     */
    public static TreeConverterContext fake() {
        return new FakeTreeConverterContext();
    }

    private TreeConverterContexts() {
        throw new UnsupportedOperationException();
    }
}
