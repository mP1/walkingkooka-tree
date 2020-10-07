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

import walkingkooka.visit.Visiting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A {@link ListExpression} holds an ordered sequence of values, what may be termed an array or list in other programming languages.
 */
public class ListExpression extends VariableExpression {

    public final static FunctionExpressionName NAME = FunctionExpressionName.fromClass(ListExpression.class);

    /**
     * Creates a new {@link ListExpression}
     */
    static ListExpression with(final List<Expression> expressions) {
        Objects.requireNonNull(expressions, "expressions");

        return new ListExpression(NO_INDEX, expressions);
    }

    /**
     * Private ctor
     */
    private ListExpression(final int index,
                           final List<Expression> expressions) {
        super(index, expressions);
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    @Override
    ParentExpression replace0(final int index, final List<Expression> children) {
        return new ListExpression(index, children);
    }

    @Override
    public ListExpression removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    public Expression setChildren(final List<Expression> children) {
        return this.setChildren0(children).cast();
    }

    // Value.........................................................................................................

    @Override
    public List<Expression> value() {
        return this.children();
    }

    // Visitor.........................................................................................................

    @Override
    public void accept(final ExpressionVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // evaluation .....................................................................................................

    @Override
    public BigDecimal toBigDecimal(final ExpressionEvaluationContext context) {
        return this.convertValues(context, BigDecimal.class);
    }

    @Override
    public BigInteger toBigInteger(final ExpressionEvaluationContext context) {
        return this.convertValues(context, BigInteger.class);
    }

    @Override
    public boolean toBoolean(final ExpressionEvaluationContext context) {
        return this.convertValues(context, Boolean.class);
    }

    @Override
    public double toDouble(final ExpressionEvaluationContext context) {
        return this.convertValues(context, Double.class);
    }

    @Override
    public LocalDate toLocalDate(final ExpressionEvaluationContext context) {
        return this.convertValues(context, LocalDate.class);
    }

    @Override
    public LocalDateTime toLocalDateTime(final ExpressionEvaluationContext context) {
        return this.convertValues(context, LocalDateTime.class);
    }

    @Override
    public LocalTime toLocalTime(final ExpressionEvaluationContext context) {
        return this.convertValues(context, LocalTime.class);
    }

    @Override
    public long toLong(final ExpressionEvaluationContext context) {
        return this.convertValues(context, Long.class);
    }

    @Override
    public Number toNumber(final ExpressionEvaluationContext context) {
        return this.convertValues(context, Number.class);
    }

    @Override
    public String toString(final ExpressionEvaluationContext context) {
        return this.convertValues(context, String.class);
    }

    @Override
    public Object toValue(final ExpressionEvaluationContext context) {
        return this.convertValues(context);
    }

    private <T> T convertValues(final ExpressionEvaluationContext context,
                                  final Class<T> target) {
        return context.convertOrFail(this.convertValues(context), target);
    }

    private List<?> convertValues(final ExpressionEvaluationContext context) {
        return this.value()
                        .stream()
                        .map(v -> v.toValue(context))
                        .collect(Collectors.toList());
    }

    // Object.........................................................................................................

    @Override
    final boolean equalsIgnoringParentAndChildren(final Expression other) {
        return true; // no new properties to equality check
    }

    /**
     * Returns a csv {@link String} of the values.
     */
    @Override
    void toString0(final StringBuilder b) {
        b.append('[');

        final List<Expression> expressions = this.value();
        int last = expressions.size() - 1;

        for (final Expression value : expressions) {
            value.toString0(b);
            last--;
            if (last >= 0) {
                b.append(',');
            }
        }

        b.append(']');
    }
}
