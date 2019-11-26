/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
import java.math.BigInteger;

/**
 * Base class for any expression that accepts two parameters and accepts all types of numbers including {@link Double} and {@link BigDecimal}.
 */
abstract class BinaryExpression2 extends BinaryExpression {

    BinaryExpression2(final int index, final Expression left, final Expression right) {
        super(index, left, right);
    }

    @Override final Expression apply(final Expression left,
                                     final Expression right,
                                     final ExpressionEvaluationContext context) {
        Expression result;

        try {
            for (; ; ) {
                final Object leftValue = left.toValue(context);
                final Object rightValue = right.toValue(context);

                if (leftValue instanceof String) {

                    result = this.applyText(
                            context.convertOrFail(leftValue, String.class),
                            context.convertOrFail(rightValue, String.class),
                            context);
                    break;
                }

                // both Long
                final boolean leftByteShortIntegerLong = isByteShortIntegerLong(leftValue);
                final boolean rightByteShortIntegerLong = isByteShortIntegerLong(rightValue);
                if (leftByteShortIntegerLong && rightByteShortIntegerLong) {
                    result = this.applyLong(
                            context.convertOrFail(leftValue, Long.class),
                            context.convertOrFail(rightValue, Long.class),
                            context);
                    break;
                }
                // BigInteger and Long or both BigInteger
                final boolean leftBigInteger = leftValue instanceof BigInteger;
                final boolean rightBigInteger = rightValue instanceof BigInteger;
                if (leftBigInteger && rightBigInteger ||
                        leftBigInteger && rightByteShortIntegerLong ||
                        leftByteShortIntegerLong && rightBigInteger) {
                    result = this.applyBigInteger(
                            context.convertOrFail(leftValue, BigInteger.class),
                            context.convertOrFail(rightValue, BigInteger.class),
                            context);
                    break;
                }
                // both must be double,
                if (isFloatDouble(leftValue) && isFloatDouble(rightValue)) {
                    result = this.applyDouble(
                            context.convertOrFail(leftValue, Double.class),
                            context.convertOrFail(rightValue, Double.class),
                            context);
                    break;
                }
                // default is to promote both to BigDecimal.
                result = this.applyBigDecimal(
                        context.convertOrFail(leftValue, BigDecimal.class),
                        context.convertOrFail(rightValue, BigDecimal.class),
                        context);
                break;
            }
        } catch (final ArithmeticException cause) {
            throw new ExpressionEvaluationException(cause.getMessage() + "\n" + this.toString(), cause);
        }

        return result;
    }

    abstract Expression applyText(final String left, final String right, final ExpressionEvaluationContext context);

    abstract Expression applyBigDecimal(final BigDecimal left, final BigDecimal right, final ExpressionEvaluationContext context);

    abstract Expression applyDouble(final double left, final double right, final ExpressionEvaluationContext context);
}
