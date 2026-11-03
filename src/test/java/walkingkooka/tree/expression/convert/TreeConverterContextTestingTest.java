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

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.PublicClassTesting;
import walkingkooka.tree.expression.HasExpressionNumberKindTesting;

import java.math.MathContext;

public final class TreeConverterContextTestingTest implements TreeConverterContextTesting,
    PublicClassTesting<TreeConverterContextTesting>,
    HasExpressionNumberKindTesting {

    // multiplyToExpressionNumber.......................................................................................

    @Test
    public void testMultiplyToExpressionNumberAndCheck() {
        this.multiplyToExpressionNumberAndCheck(
            new FakeTreeConverterContext() {
                @Override
                public <N extends Number> N multiply(final Number left,
                                                     final Number right,
                                                     final Class<N> type) {
                    return type.cast(
                        EXPRESSION_NUMBER_KIND.create(left)
                            .multiply(
                                EXPRESSION_NUMBER_KIND.create(right),
                                this
                            )
                    );
                }

                @Override
                public MathContext mathContext() {
                    return MATH_CONTEXT;
                }
            },
            12,
            34,
            EXPRESSION_NUMBER_KIND.create(12 * 34)
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeConverterContextTesting> type() {
        return TreeConverterContextTesting.class;
    }
}
