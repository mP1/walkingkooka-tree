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
 * Base class for all comparison {@link BinaryExpression} nodes such as LT, GTE etc.
 */
abstract class BinaryComparisonExpression extends BinaryExpression2 {

    BinaryComparisonExpression(final int index, final Expression left, final Expression right) {
        super(index, left, right);
    }

    // evaluation .....................................................................................................

    @Override
    public final Boolean toValue(final ExpressionEvaluationContext context) {
        return this.toBoolean(context);
    }

    @Override
    final Expression applyText(final String left, final String right, final ExpressionEvaluationContext context) {
        return Expression.booleanExpression(this.isComparisonTrue(left.compareTo(right)));
    }

    @Override
    final Expression applyBigDecimal(final BigDecimal left, final BigDecimal right, final ExpressionEvaluationContext context) {
        return Expression.booleanExpression(this.isComparisonTrue(left.compareTo(right)));
    }

    @Override
    final Expression applyBigInteger(final BigInteger left, final BigInteger right, final ExpressionEvaluationContext context) {
        return Expression.booleanExpression(this.isComparisonTrue(left.compareTo(right)));
    }

    @Override
    final Expression applyDouble(final double left, final double right, final ExpressionEvaluationContext context) {
        return Expression.booleanExpression(Double.isFinite(left) & Double.isFinite(right) &
                (this.isComparisonTrue(Double.compare(left, right))));
    }

    @Override final Expression applyLong(final long left, final long right, final ExpressionEvaluationContext context) {
        return Expression.booleanExpression(this.isComparisonTrue(Long.compare(left, right)));
    }

    /**
     * Converts the ternary result of a comparison into a boolean for this comparison reflect.
     */
    abstract boolean isComparisonTrue(final int comparisonResult);
}
