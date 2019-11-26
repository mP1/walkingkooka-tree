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
 * Represents a function with zero or more parameters.
 */
public final class FunctionExpression extends VariableExpression {

    /**
     * Creates a new {@link FunctionExpression}
     */
    static FunctionExpression with(final FunctionExpressionName name, final List<Expression> expressions) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(expressions, "expressions");

        return new FunctionExpression(NO_INDEX, name, expressions);
    }

    /**
     * Private ctor
     */
    private FunctionExpression(final int index, FunctionExpressionName name, final List<Expression> expressions) {
        super(index, expressions);
        this.name = name;
    }

    @Override
    ParentExpression replace0(final int index, final List<Expression> children) {
        return new FunctionExpression(index, this.name, children);
    }

    @Override
    public FunctionExpressionName name() {
        return this.name;
    }

    private final FunctionExpressionName name;

    public FunctionExpression setName(final FunctionExpressionName name) {
        Objects.requireNonNull(name, "name");
        return this.name().equals(name) ?
                this :
                new FunctionExpression(this.index, name, this.value());
    }

    @Override
    public FunctionExpression removeParent() {
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
    public final BigDecimal toBigDecimal(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, BigDecimal.class);
    }

    @Override
    public final BigInteger toBigInteger(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, BigInteger.class);
    }

    @Override
    public final boolean toBoolean(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, Boolean.class);
    }

    @Override
    public final double toDouble(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, Double.class);
    }

    @Override
    public final LocalDate toLocalDate(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, LocalDate.class);
    }

    @Override
    public final LocalDateTime toLocalDateTime(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, LocalDateTime.class);
    }

    @Override
    public final LocalTime toLocalTime(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, LocalTime.class);
    }

    @Override
    public final long toLong(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, Long.class);
    }

    @Override
    public final Number toNumber(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, Number.class);
    }

    @Override
    public final String toString(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, String.class);
    }

    @Override
    public final Object toValue(final ExpressionEvaluationContext context) {
        return this.executeFunction(context);
    }

    private <T> T executeFunction(final ExpressionEvaluationContext context, final Class<T> target) {
        return context.convertOrFail(this.executeFunction(context), target);
    }

    private Object executeFunction(final ExpressionEvaluationContext context) {
        return context.function(this.name(),
                this.value()
                        .stream()
                        .map(v -> v.toValue(context))
                        .collect(Collectors.toList()));
    }

    // Object.........................................................................................................

    @Override final boolean equalsIgnoringParentAndChildren(final Expression other) {
        return this.name.equals(other.name());
    }

    @Override
    void toString0(StringBuilder b) {
        b.append(this.name());
        b.append('(');

        final List<Expression> expressions = this.value();
        int last = expressions.size() - 1;
        for (Expression parameter : expressions) {
            parameter.toString0(b);
            last--;
            if (last >= 0) {
                b.append(',');
            }
        }

        b.append(')');
    }
}
