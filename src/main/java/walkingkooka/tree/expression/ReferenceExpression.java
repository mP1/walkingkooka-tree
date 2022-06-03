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

import java.util.Objects;

/**
 * A reference expression.
 */
public final class ReferenceExpression extends LeafExpression<ExpressionReference> {

    public final static FunctionExpressionName NAME = FunctionExpressionName.fromClass(ReferenceExpression.class);

    static ReferenceExpression with(final ExpressionReference reference) {
        Objects.requireNonNull(reference, "reference");

        return new ReferenceExpression(NO_INDEX, reference);
    }

    private ReferenceExpression(final int index, final ExpressionReference reference) {
        super(index, reference);
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    @Override
    public ReferenceExpression removeParent() {
        return this.removeParent0().cast();
    }

    public ReferenceExpression setValue(final ExpressionReference value) {
        return Objects.equals(this.value(), value) ?
                this :
                this.replaceValue(value);
    }

    private ReferenceExpression replaceValue(final ExpressionReference value) {
        return this.replace1(this.index, value)
                .replaceChild(this.parent())
                .cast();
    }

    @Override
    public ReferenceExpression replace(final int index) {
        return this.replace1(index, this.value);
    }

    private ReferenceExpression replace1(final int index,
                                         final ExpressionReference value) {
        return new ReferenceExpression(index, value);
    }

    // ExpressionPurity..................................................................................................

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return false;
    }

    // visitor..........................................................................................................

    @Override
    public void accept(final ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    // evaluation .....................................................................................................

    @Override
    public boolean toBoolean(final ExpressionEvaluationContext context) {
        return this.toValueAndConvert(context, Boolean.class);
    }

    @Override
    public ExpressionNumber toExpressionNumber(final ExpressionEvaluationContext context) {
        return this.toValueAndConvert(context, ExpressionNumber.class);
    }

    @Override
    public String toString(final ExpressionEvaluationContext context) {
        return this.toValueAndConvert(context, String.class);
    }

    @Override
    public Object toReferenceOrValue(final ExpressionEvaluationContext context) {
        Objects.requireNonNull(context, "context");
        return this.value;
    }

    @Override
    public Object toValue(final ExpressionEvaluationContext context) {
        return context.referenceOrFail(this.value);
    }

    private <T> T toValueAndConvert(final ExpressionEvaluationContext context,
                                    final Class<T> type) {
        return context.convertOrFail(
                context.referenceOrFail(this.value),
                type
        );
    }

    // Object ....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ReferenceExpression;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(this.value);
    }
}
