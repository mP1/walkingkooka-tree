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
 * A {@link ValueExpression} that contains an {@link ExpressionNumber}.
 */
public final class ExpressionNumberExpression extends ValueExpression<ExpressionNumber> {

    public final static FunctionExpressionName NAME = FunctionExpressionName.fromClass(ExpressionNumberExpression.class);

    static ExpressionNumberExpression with(final ExpressionNumber value) {
        Objects.requireNonNull(value, "value");
        return new ExpressionNumberExpression(NO_INDEX, value);
    }

    private ExpressionNumberExpression(final int index, final ExpressionNumber value) {
        super(index, value);
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    @Override
    public ExpressionNumberExpression removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    ExpressionNumberExpression replace1(final int index, final ExpressionNumber value) {
        return new ExpressionNumberExpression(index, value);
    }

    // visitor..........................................................................................................

    @Override
    public void accept(final ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    // Object ....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionNumberExpression;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(this.value);
    }
}
