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

import walkingkooka.compare.ComparisonRelation;

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
        return Expression.booleanExpression(this.comparisonRelation().test(left.compareTo(right)));
    }

    @Override //
    final Expression applyExpressionNumber(final ExpressionNumber left,
                                           final ExpressionNumber right,
                                           final ExpressionEvaluationContext context) {
        return Expression.booleanExpression(this.comparisonRelation().test(left.compareTo(right)));
    }

    /**
     * Converts the ternary result of a comparison into a boolean for this comparison reflect.
     */
    abstract ComparisonRelation comparisonRelation();
}
