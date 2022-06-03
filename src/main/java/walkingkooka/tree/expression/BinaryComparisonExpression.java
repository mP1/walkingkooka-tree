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

import walkingkooka.Cast;
import walkingkooka.compare.ComparisonRelation;

/**
 * Base class for all comparison {@link BinaryExpression} nodes such as LT, GTE etc.
 */
abstract class BinaryComparisonExpression extends BinaryExpression {

    BinaryComparisonExpression(final int index, final Expression left, final Expression right) {
        super(index, left, right);
    }

    // evaluation .....................................................................................................

    @Override
    public final Boolean toValue(final ExpressionEvaluationContext context) {
        return this.toBoolean(context);
    }

    @Override final Expression apply(final Object left,
                                     final Object right,
                                     final ExpressionEvaluationContext context) {
        final ComparisonRelation compare = this.comparisonRelation();

        final Object result;
        if (left instanceof Character || left instanceof String) {
            result = this.applyText(
                    compare,
                    context.convertOrFail(left, String.class),
                    context.convertOrFail(right, String.class),
                    context
            );
        } else {
            if (!(left instanceof Comparable)) {
                throw new IllegalArgumentException(left + " is not comparable");
            }

            final Class<Comparable<?>> leftClass = Cast.to(left.getClass());
            result = this.applyNonText(
                    compare,
                    Cast.to(left),
                    Cast.to(context.convertOrFail(right, leftClass))
            );
        }

        return Expression.value(result);
    }

    /**
     * The {@link String#compareTo(String)} honours {@link ExpressionEvaluationContext#caseSensitivity()}
     */
    final boolean applyText(final ComparisonRelation compare,
                            final String left,
                            final String right,
                            final ExpressionEvaluationContext context) {
        return compare.test(
                context.caseSensitivity()
                        .comparator()
                        .compare(left, right)
        );
    }

    /**
     * Handles all other non {@link String#compareTo(String)}. This assumes the two values are compare compatible.
     */
    private <CC extends Comparable<CC>> boolean applyNonText(final ComparisonRelation compare,
                                                             final CC left,
                                                             final CC right) {
        return compare.test(
                left.compareTo(right)
        );
    }

    /**
     * Converts the ternary result of a comparison into a boolean for this comparison reflect.
     */
    abstract ComparisonRelation comparisonRelation();
}
