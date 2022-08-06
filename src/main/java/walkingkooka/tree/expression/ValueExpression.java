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

import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;

/**
 * Holds a value which may or may not be null.
 */
final public class ValueExpression<V> extends LeafExpression<V> {

    public final static FunctionExpressionName NAME = FunctionExpressionName.fromClass(ValueExpression.class);

    static <V> ValueExpression<V> with(final V value) {
        return new ValueExpression<>(NO_INDEX, value);
    }

    private ValueExpression(final int index, final V value) {
        super(index, value);
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    @Override
    public ValueExpression<V> removeParent() {
        return this.removeParent0().cast();
    }

    public <VV> ValueExpression<VV> setValue(final VV value) {
        return Objects.equals(this.value(), value) ?
                (ValueExpression<VV>) this :
                this.replaceValue(value);
    }

    private <VV> ValueExpression<VV> replaceValue(final VV value) {
        return this.replace1(this.index, value)
                .replaceChild(this.parent())
                .cast();
    }

    @Override
    public ValueExpression<V> replace(final int index) {
        return this.replace1(index, this.value);
    }

    private <VV> ValueExpression<VV> replace1(final int index, final VV value) {
        return new ValueExpression<>(index, value);
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return true;
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
        return this.callRequiringNoParameters(
                parameters,
                context
        );
    }

    @Override
    public boolean toBoolean(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.value(), Boolean.class);
    }

    @Override
    public ExpressionNumber toExpressionNumber(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.value(), ExpressionNumber.class);
    }

    @Override
    public Object toReferenceOrValue(final ExpressionEvaluationContext context) {
        return this.toValue(context);
    }

    // ExpressionEvaluationContext......................................................................................

    @Override
    public V toValue(final ExpressionEvaluationContext context) {
        return this.value();
    }

    @Override
    public String toString(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.value(), String.class);
    }

    // Object ..........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ValueExpression;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(CharSequences.quoteIfChars(this.value));
    }
}
