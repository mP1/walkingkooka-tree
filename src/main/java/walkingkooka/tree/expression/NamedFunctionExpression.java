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

import java.util.List;
import java.util.Objects;

/**
 * A handle to a function identified by the given {@link FunctionExpressionName}.
 * <br>
 * Identifying the {@link walkingkooka.tree.expression.function.ExpressionFunction} is performed by the {@link ExpressionEvaluationContext}
 * during the evaluate phase.
 */
final public class NamedFunctionExpression extends LeafExpression<FunctionExpressionName> {

    public final static FunctionExpressionName NAME = FunctionExpressionName.fromClass(NamedFunctionExpression.class);

    static <V> NamedFunctionExpression with(final FunctionExpressionName name) {
        return new NamedFunctionExpression(NO_INDEX, name);
    }

    /**
     * Private ctor
     */
    private NamedFunctionExpression(final int index,
                                    final FunctionExpressionName name) {
        super(index, name);
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    @Override
    public NamedFunctionExpression removeParent() {
        return this.removeParent0().cast();
    }

    public NamedFunctionExpression setValue(final FunctionExpressionName value) {
        return Objects.equals(this.value(), value) ?
                this :
                this.replaceValue(value);
    }

    private NamedFunctionExpression replaceValue(final FunctionExpressionName value) {
        return this.replace1(this.index, value)
                .replaceChild(this.parent())
                .cast();
    }

    @Override
    public NamedFunctionExpression replace(final int index) {
        return this.replace1(
                index,
                this.value()
        );
    }

    private NamedFunctionExpression replace1(final int index,
                                             final FunctionExpressionName value) {
        return new NamedFunctionExpression(index, value);
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return context.isPure(this.value());
    }

    // visitor..........................................................................................................

    @Override
    public void accept(final ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    // toXXX............................................................................................................

    @Override
    Object call(final List<Expression> parameters,
                final ExpressionEvaluationContext context) {
        return context.evaluateFunction(
                context.function(this.value()),
                Cast.to(parameters)
        );
    }

    @Override
    public boolean toBoolean(final ExpressionEvaluationContext context) {
        return context.convertOrFail(
                this.value(),
                Boolean.class
        );
    }

    @Override
    public ExpressionNumber toExpressionNumber(final ExpressionEvaluationContext context) {
        return context.convertOrFail(
                this.value(),
                ExpressionNumber.class
        );
    }

    @Override
    public Object toReferenceOrValue(final ExpressionEvaluationContext context) {
        return this.toValue(context);
    }

    // ExpressionEvaluationContext......................................................................................

    @Override
    public FunctionExpressionName toValue(final ExpressionEvaluationContext context) {
        return this.value();
    }

    @Override
    public String toString(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.value(), String.class);
    }

    // Object ..........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NamedFunctionExpression;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(this.value);
    }
}
