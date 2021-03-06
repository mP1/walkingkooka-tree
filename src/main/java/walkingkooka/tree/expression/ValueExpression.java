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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Base class for all leafs except {@link ReferenceExpression}.
 * All toXXX methods take their value and convert it to the actual target value.
 */
abstract class ValueExpression<V> extends LeafExpression<V> {

    ValueExpression(final int index, final V value) {
        super(index, value);
    }

    @Override
    public final boolean isPure(final ExpressionPurityContext context) {
        return true;
    }

    @Override
    public final boolean toBoolean(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.value(), Boolean.class);
    }

    @Override
    public final ExpressionNumber toExpressionNumber(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.value(), ExpressionNumber.class);
    }

    @Override
    public final LocalDate toLocalDate(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.value(), LocalDate.class);
    }

    @Override
    public final LocalDateTime toLocalDateTime(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.value(), LocalDateTime.class);
    }

    @Override
    public final LocalTime toLocalTime(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.value(), LocalTime.class);
    }

    @Override
    public final Object toReferenceOrValue(final ExpressionEvaluationContext context) {
        return this.toValue(context);
    }

    @Override
    public final String toString(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.value(), String.class);
    }

    @Override
    public final V toValue(final ExpressionEvaluationContext context) {
        return this.value();
    }
}
