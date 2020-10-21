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

import java.math.BigInteger;

/**
 * Base class for the AND, OR, XOR {@link BinaryExpression}.
 */
abstract class BinaryLogicalExpression extends BinaryExpression {

    BinaryLogicalExpression(final int index, final Expression left, final Expression right) {
        super(index, left, right);
    }

    // evaluation .....................................................................................................

    @Override
    public final Object toValue(final ExpressionEvaluationContext context) {
        return this.apply(this.left(), this.right(), context)
                .toValue(context);
    }

    @Override final Expression apply(final Expression left,
                                     final Expression right,
                                     final ExpressionEvaluationContext context) {
        Expression result;

        try {
            for (; ; ) {
                final Object leftValue = left.toValue(context);
                final Object rightValue = right.toValue(context);

                if (leftValue instanceof Boolean) {
                    result = this.applyBoolean(
                            context.convertOrFail(leftValue, Boolean.class),
                            context.convertOrFail(rightValue, Boolean.class),
                            context);
                    break;
                }

                // both Long
                final boolean leftLong = ExpressionNumber.isByteShortIntegerLong(leftValue);
                final boolean rightLong = ExpressionNumber.isByteShortIntegerLong(rightValue);
                if (leftLong && rightLong) {
                    result = this.applyLong(
                            context.convertOrFail(leftValue, Long.class),
                            context.convertOrFail(rightValue, Long.class),
                            context);
                    break;
                }
                // default is to promote both to BigInteger, doubles and BigDecimal may fail if they have decimals.
                result = this.applyBigInteger(
                        context.convertOrFail(leftValue, BigInteger.class),
                        context.convertOrFail(rightValue, BigInteger.class),
                        context);
                break;
            }
        } catch (final ArithmeticException cause) {
            throw new ExpressionEvaluationException(cause.getMessage() + "\n" + this.toString(), cause);
        }

        return result;
    }

    final Expression applyBoolean(final boolean left, final boolean right, final ExpressionEvaluationContext context) {
        return Expression.booleanExpression(this.applyBoolean0(left, right));
    }

    abstract boolean applyBoolean0(final boolean left, final boolean right);
}
