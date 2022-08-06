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
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.Objects;

/**
 * A lambda function where the value is the body.
 */
public final class LambdaFunctionExpression extends UnaryExpression {

    public final static FunctionExpressionName NAME = FunctionExpressionName.fromClass(LambdaFunctionExpression.class);

    static LambdaFunctionExpression with(final List<ExpressionFunctionParameter<?>> parameters,
                                         final Expression value) {
        Objects.requireNonNull(parameters, "parameters");
        Objects.requireNonNull(value, "value");

        return new LambdaFunctionExpression(
                NO_INDEX,
                Lists.immutable(parameters),
                value
        );
    }

    private LambdaFunctionExpression(final int index,
                                     final List<ExpressionFunctionParameter<?>> parameters,
                                     final Expression value) {
        super(index, value);
        this.parameters = parameters;
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    @Override
    public LambdaFunctionExpression removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    public LambdaFunctionExpression setChildren(final List<Expression> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    LambdaFunctionExpression replace1(final int index,
                                      final Expression expression) {
        return new LambdaFunctionExpression(
                index,
                this.parameters,
                expression
        );
    }

    // parameter ..................................................................................................

    public List<ExpressionFunctionParameter<?>> parameters() {
        return this.parameters;
    }

    public LambdaFunctionExpression setParameters(final List<ExpressionFunctionParameter<?>> parameters) {
        Objects.requireNonNull(parameters, "parameters");

        final List<ExpressionFunctionParameter<?>> copy = Lists.immutable(parameters);
        return this.parameters.equals(copy) ?
                this :
                new LambdaFunctionExpression(
                        index,
                        parameters,
                        this.value()
                );
    }

    private final List<ExpressionFunctionParameter<?>> parameters;

    // visitor..........................................................................................................

    @Override
    public void accept(final ExpressionVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // evaluation .....................................................................................................

    @Override
    Object call(final List<Expression> parameters,
                final ExpressionEvaluationContext context) {

        final ExpressionFunction<?, ExpressionEvaluationContext> lambdaFunction = context.lambdaFunction(
                this.parameters,
                Object.class,
                this.value()
        );

        return context.evaluateFunction(
                lambdaFunction,
                Cast.to(parameters)
        );
    }

    @Override
    public boolean toBoolean(final ExpressionEvaluationContext context) {
        return this.value()
                .toBoolean(context);
    }

    @Override
    public ExpressionNumber toExpressionNumber(final ExpressionEvaluationContext context) {
        return this.value()
                .toExpressionNumber(context);
    }

    @Override
    public String toString(final ExpressionEvaluationContext context) {
        return this.value()
                .toString(context);
    }

    @Override
    public Object toValue(final ExpressionEvaluationContext context) {
        return context.evaluate(
                this.value()
        );
    }

    // Object ..........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LambdaFunctionExpression;
    }

    @Override
    boolean equalsIgnoringParentAndChildren(final Expression other) {
        return this.equalsIgnoringParentAndChildren0((LambdaFunctionExpression) other);
    }

    private boolean equalsIgnoringParentAndChildren0(final LambdaFunctionExpression other) {
        return this.parameters.equals(other.parameters);
    }

    // (x, y)->{}
    @Override
    void toString0(final StringBuilder b) {
        b.append('(');

        String separator = "";

        for (final ExpressionFunctionParameter<?> parameter : this.parameters) {
            b.append(separator);
            b.append(parameter.name().value());

            separator = ", ";
        }

        b.append(")->{");

        this.value()
                .toString0(b);
        b.append('}');
    }
}
