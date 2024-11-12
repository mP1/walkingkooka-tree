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
 * Base class for all arithmetic {@link BinaryExpression} nodes such as addition, power etc.
 */
abstract public class ArithmeticExpression extends BinaryExpression {

    ArithmeticExpression(final int index,
                         final Expression left,
                         final Expression right) {
        super(index, left, right);
    }

    // evaluation .....................................................................................................

    @Override
    public final Number toValue(final ExpressionEvaluationContext context) {
        return this.toExpressionNumber(context);
    }

    /**
     * Includes dispatch logic with a special case if the left parameter is text, otherwise both values
     * are converted to {@link ExpressionNumber} and given to {@link #applyExpressionNumber(ExpressionNumber, ExpressionNumber, ExpressionEvaluationContext)}.
     */
    @Override //
    final Expression apply(final Object left,
                           final Object right,
                           final ExpressionEvaluationContext context) {
        final Object result;

        if (context.isText(left)) {
            result = this.applyText(
                    context.convertOrFail(left, String.class),
                    context.convertOrFail(right, String.class),
                    context
            );
        } else {
            result = this.applyExpressionNumber(
                    context.convertOrFail(left, ExpressionNumber.class),
                    context.convertOrFail(right, ExpressionNumber.class),
                    context
            );
        }

        return Expression.value(result);
    }

    /**
     * Currently only addition supports two text parameters, which it concats both, all other operands throw
     * {@link UnsupportedOperationException}
     */
    abstract String applyText(final String left,
                              final String right,
                              final ExpressionEvaluationContext context);

    /**
     * Performs the actual binary operation using the two {@link ExpressionNumber} values.
     */
    abstract ExpressionNumber applyExpressionNumber(final ExpressionNumber left,
                                                    final ExpressionNumber right,
                                                    final ExpressionEvaluationContext context);
}
