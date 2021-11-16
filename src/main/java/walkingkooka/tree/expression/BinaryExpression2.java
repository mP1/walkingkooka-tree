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

/**
 * Base class for any expression that accepts two parameters and accepts all types of numbers including {@link Double} and {@link BigDecimal}.
 */
abstract class BinaryExpression2 extends BinaryExpression {

    BinaryExpression2(final int index, final Expression left, final Expression right) {
        super(index, left, right);
    }

    @Override
    final Expression apply(final ExpressionEvaluationContext context) {
        final Expression result;

        do {
            final Object left = this.left().toValue(context);
            final Object right = this.right().toValue(context);

            if (left instanceof String) {
                result = this.applyText(
                        context.convertOrFail(left, String.class),
                        context.convertOrFail(right, String.class),
                        context);
                break;
            }

            // default is to promote both to ExpressionNumber.
            result = this.applyExpressionNumber(
                    context.convertOrFail(left, ExpressionNumber.class),
                    context.convertOrFail(right, ExpressionNumber.class),
                    context
            );
        } while (false);

        return result;
    }

    abstract Expression applyText(final String left, final String right, final ExpressionEvaluationContext context);

    abstract Expression applyExpressionNumber(final ExpressionNumber left,
                                              final ExpressionNumber right,
                                              final ExpressionEvaluationContext context);
}
