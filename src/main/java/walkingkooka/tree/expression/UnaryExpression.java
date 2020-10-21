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

import walkingkooka.Value;
import walkingkooka.collect.list.Lists;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Base class for any expression with a single parameter.
 */
abstract class UnaryExpression extends ParentFixedExpression implements Value<Expression> {

    UnaryExpression(final int index, final Expression expression) {
        super(index, Lists.of(expression));
    }

    @Override
    public final Expression value() {
        return this.children().get(0);
    }

    @Override
    final int expectedChildCount() {
        return 1;
    }

    @Override final UnaryExpression replace0(final int index, final List<Expression> children) {
        return replace1(index, children.get(0));
    }

    abstract UnaryExpression replace1(final int index, final Expression expression);

    // Node.............................................................................................................

    @Override
    public final Optional<Expression> firstChild() {
        return Optional.of(this.value());
    }

    @Override
    public final Optional<Expression> lastChild() {
        return this.firstChild();
    }

    // Visitor........................................................................................................

    final void acceptValues(final ExpressionVisitor visitor) {
        visitor.accept(this.value());
    }

    // Evaluation...................................................................................................

    @Override
    public final LocalDate toLocalDate(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.toNumber(context), LocalDate.class);
    }

    @Override
    public final LocalDateTime toLocalDateTime(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.toNumber(context), LocalDateTime.class);
    }

    @Override
    public final LocalTime toLocalTime(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.toNumber(context), LocalTime.class);
    }

    @Override
    public final Number toValue(final ExpressionEvaluationContext context) {
        return this.toNumber(context);
    }
}
