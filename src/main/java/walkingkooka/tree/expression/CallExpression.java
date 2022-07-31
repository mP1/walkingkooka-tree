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
 * Represents an attempt to call a function.
 */
public final class CallExpression extends VariableExpression {

    public final static FunctionExpressionName NAME = FunctionExpressionName.fromClass(CallExpression.class);

    /**
     * Creates a new {@link CallExpression}
     */
    static CallExpression with(final Expression callable,
                               final List<Expression> parameters) {
        Objects.requireNonNull(callable, "callable");
        Objects.requireNonNull(parameters, "parameters");

        return new CallExpression(NO_INDEX, callable, parameters);
    }

    /**
     * Private ctor
     */
    private CallExpression(final int index,
                           final Expression callable,
                           final List<Expression> parameters) {
        super(index, parameters);
        this.callable = callable;
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    @Override
    ParentExpression replace0(final int index,
                              final List<Expression> children) {
        return new CallExpression(
                index,
                this.callable,
                children
        );
    }

    /**
     * The callable which is probably a {@link NamedFunctionExpression}.
     */
    public Expression callable() {
        return this.callable;
    }

    private final Expression callable;

    public CallExpression setCallable(final Expression callable) {
        Objects.requireNonNull(callable, "callable");
        return this.callable().equals(callable) ?
                this :
                new CallExpression(this.index, callable, this.value());
    }

    @Override
    public CallExpression removeParent() {
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
        return this.callable().isPure(context) &&
                this.isPureChildren(context);
    }

    // Visitor.........................................................................................................

    @Override
    public void accept(final ExpressionVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.callable.accept(visitor);
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // evaluation .....................................................................................................

    @Override
    public boolean toBoolean(final ExpressionEvaluationContext context) {
        return this.executeFunction(
                context,
                Boolean.class
        );
    }

    @Override
    public ExpressionNumber toExpressionNumber(final ExpressionEvaluationContext context) {
        return this.executeFunction(
                context,
                ExpressionNumber.class
        );
    }

    @Override
    public String toString(final ExpressionEvaluationContext context) {
        return this.executeFunction(
                context,
                String.class
        );
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
     * Currently only supports a {@link NamedFunctionExpression} as the callable.
     */
    private Object executeFunction0(final List<Expression> parameters,
                                    final ExpressionEvaluationContext context) {
        final NamedFunctionExpression callable = (NamedFunctionExpression) this.callable();

        return context.evaluate(
                callable.value(),
                Cast.to(parameters)
        );
    }

    // Object.........................................................................................................

    @Override
    boolean equalsIgnoringParentAndChildren(final Expression other) {
        return this.equalsIgnoringParentAndChildren0(Cast.to(other));
    }

    private boolean equalsIgnoringParentAndChildren0(final CallExpression other) {
        return this.callable.equals(other.callable());
    }

    @Override
    void toString0(final StringBuilder b) {
        this.callable().toString0(b);

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
