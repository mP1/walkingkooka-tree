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
import walkingkooka.convert.BinaryNumberConverterFunctions;
import walkingkooka.tree.expression.ExpressionNumber;

import java.util.Objects;

/**
 * A {@link BinaryNumberConverterFunction} that first converts each of the two number parameters to the requested {@link Number},
 * including {@link ExpressionNumber} and then multiplies both numbers.
 */
final class BasicMultiplyBinaryNumberConverterFunction<C extends ExpressionNumberConverterContext> implements BinaryNumberConverterFunction<C> {

    /**
     * Type safe getter
     */
    static <C extends ExpressionNumberConverterContext> BasicMultiplyBinaryNumberConverterFunction<C> instance() {
        return INSTANCE;
    }

    /**
     * Singleton
     */
    private final static BasicMultiplyBinaryNumberConverterFunction INSTANCE = new BasicMultiplyBinaryNumberConverterFunction<>();

    private BasicMultiplyBinaryNumberConverterFunction() {
        super();
    }

    @Override
    public <N extends Number> N apply(final Number left,
                                      final Number right,
                                      final Class<N> targetType,
                                      final C context) {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
        Objects.requireNonNull(targetType, "targetType");
        Objects.requireNonNull(context, "context");

        final N leftNumber = context.convertOrFail(
            left,
            targetType
        );

        final Number number;

        if (leftNumber instanceof ExpressionNumber) {
            number = ((ExpressionNumber) leftNumber).multiply(
                (ExpressionNumber)
                    context.convertOrFail(
                        right,
                        targetType
                    ),
                context
            );
        } else {
            number = BinaryNumberConverterFunctions.multiply()
                .apply(
                    leftNumber,
                    right,
                    targetType,
                    context
                );
        }

        return (N) number;
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.getClass().getName();
    }
}
