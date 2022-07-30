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
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.Objects;

/**
 * Represents a function with zero or more parameters.
 */
public final class FunctionExpression extends VariableExpression {

    /**
     * Creates a new {@link FunctionExpression}
     */
    static FunctionExpression with(final FunctionExpressionName name, final List<Expression> parameters) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(parameters, "parameters");

        return new FunctionExpression(NO_INDEX, name, parameters);
    }

    /**
     * Private ctor
     */
    private FunctionExpression(final int index, FunctionExpressionName name, final List<Expression> parameters) {
        super(index, parameters);
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

    // ExpressionPurity.................................................................................................

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return context.isPure(this.name()) &&
                this.isPureChildren(context);
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
    public boolean toBoolean(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, Boolean.class);
    }

    @Override
    public ExpressionNumber toExpressionNumber(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, ExpressionNumber.class);
    }

    @Override
    public String toString(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, String.class);
    }

    @Override
    public Object toValue(final ExpressionEvaluationContext context) {
        return this.executeFunction(context);
    }

    private <T> T executeFunction(final ExpressionEvaluationContext context,
                                  final Class<T> target) {
        return context.convertOrFail(
                this.executeFunction(context),
                target
        );
    }

    private Object executeFunction(final ExpressionEvaluationContext context) {
        return this.executeFunction0(
                this.value(),
                context
        );
    }

    /**
     * Delegates to the given context.
     */
    private Object executeFunction0(final List<Expression> parameters,
                                    final ExpressionEvaluationContext context) {
        return context.evaluate(
                this.name(),
                Cast.to(parameters)
        );
    }

    // Object.........................................................................................................

    @Override
    boolean equalsIgnoringParentAndChildren(final Expression other) {
        return this.name.equals(other.name());
    }

    @Override
    void toString0(StringBuilder b) {
        b.append(this.name());
        b.append('(');

        final List<Expression> parameters = this.value();
        int last = parameters.size() - 1;
        for (final Expression parameter : parameters) {
            parameter.toString0(b);
            last--;
            if (last >= 0) {
                b.append(',');
            }
        }

        b.append(')');
    }
}
