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

    @Override
    ReferenceExpression replace1(final int index, final ExpressionReference value) {
        return new ReferenceExpression(index, value);
    }

    // visitor..........................................................................................................

    @Override
    public void accept(final ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    // evaluation .....................................................................................................

    @Override
    public final boolean toBoolean(final ExpressionEvaluationContext context) {
        return this.toExpression(context).toBoolean(context);
    }

    @Override
    public final ExpressionNumber toExpressionNumber(final ExpressionEvaluationContext context) {
        return this.toExpression(context).toExpressionNumber(context);
    }

    @Override
    public final LocalDate toLocalDate(final ExpressionEvaluationContext context) {
        return this.toExpression(context).toLocalDate(context);
    }

    @Override
    public final LocalDateTime toLocalDateTime(final ExpressionEvaluationContext context) {
        return this.toExpression(context).toLocalDateTime(context);
    }

    @Override
    public final LocalTime toLocalTime(final ExpressionEvaluationContext context) {
        return this.toExpression(context).toLocalTime(context);
    }

    @Override
    public final String toString(final ExpressionEvaluationContext context) {
        return this.toExpression(context).toString(context);
    }

    @Override
    public final Object toValue(final ExpressionEvaluationContext context) {
        return this.toExpression(context).toValue(context);
    }

    private Expression toExpression(final ExpressionEvaluationContext context) {
        return context.referenceOrFail(this.value);
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
