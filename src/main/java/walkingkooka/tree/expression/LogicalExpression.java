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
 * Base class for a logical {@link Expression}, basically the AND, OR, XOR operators.
 * <br>
 * When executed if the left parameter is boolean it will attempt to convert the right to a boolean and call
 * {@link #applyBoolean(boolean, boolean)}.
 * <br>
 * When the left is not boolean it will attempt to convert both the left and right to {@link ExpressionNumber} and invoke
 * {@link #applyExpressionNumber(ExpressionNumber, ExpressionNumber)}.
 */
abstract class LogicalExpression extends BinaryExpression {

    LogicalExpression(final int index, final Expression left, final Expression right) {
        super(index, left, right);
    }

    // evaluation .....................................................................................................

    @Override
    public final Object toValue(final ExpressionEvaluationContext context) {
        return this.apply(context)
                .toValue(context);
    }

    @Override final Expression apply(final Object left,
                                     final Object right,
                                     final ExpressionEvaluationContext context) {
        final Object result;
        if (left instanceof Boolean) {
            result = this.applyBoolean(
                    context.convertOrFail(left, Boolean.class),
                    context.convertOrFail(right, Boolean.class)
            );
        } else {
            result = this.applyExpressionNumber(
                    context.convertOrFail(left, ExpressionNumber.class),
                    context.convertOrFail(right, ExpressionNumber.class)
            );
        }

        return Expression.value(result);
    }

    /**
     * Sub classes will perform the logical operation.
     */
    abstract boolean applyBoolean(final boolean left,
                                  final boolean right);

    /**
     * Sub classes will perform the bitwise operation.
     */
    abstract ExpressionNumber applyExpressionNumber(final ExpressionNumber left,
                                                    final ExpressionNumber right);
}
