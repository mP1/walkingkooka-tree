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
        return this.apply(context)
                .toValue(context);
    }

    @Override
    final Expression apply(final ExpressionEvaluationContext context) {
        final Expression result;

        try {
            for (; ; ) {
                final Object left = this.left().toValue(context);
                final Object right = this.right().toValue(context);

                if (left instanceof Boolean) {
                    result = this.applyBoolean(
                            context.convertOrFail(left, Boolean.class),
                            context.convertOrFail(right, Boolean.class),
                            context);
                    break;
                }

                result = this.applyExpressionNumber(
                        context.convertOrFail(left, ExpressionNumber.class),
                        context.convertOrFail(right, ExpressionNumber.class));
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

    private Expression applyExpressionNumber(final ExpressionNumber left,
                                             final ExpressionNumber right) {
        return Expression.expressionNumber(this.applyExpressionNumber0(left, right));
    }

    abstract ExpressionNumber applyExpressionNumber0(final ExpressionNumber left,
                                                     final ExpressionNumber right);
}
