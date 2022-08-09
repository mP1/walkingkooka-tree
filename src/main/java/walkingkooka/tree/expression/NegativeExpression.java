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

import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.Objects;

/**
 * A negative expression.
 */
public final class NegativeExpression extends UnaryExpression {

    public final static FunctionExpressionName NAME = FunctionExpressionName.fromClass(NegativeExpression.class);

    public final static String SYMBOL = "-";

    static NegativeExpression with(final Expression value) {
        Objects.requireNonNull(value, "value");
        return new NegativeExpression(NO_INDEX, value);
    }

    private NegativeExpression(final int index, final Expression value) {
        super(index, value);
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    @Override
    public NegativeExpression removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    public NegativeExpression setChildren(final List<Expression> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    NegativeExpression replace1(final int index, final Expression expression) {
        return new NegativeExpression(index, expression);
    }

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
        return this.callRequiringNoParameters(
                parameters,
                context
        );
    }

    @Override
    public boolean toBoolean(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.toExpressionNumber(context), Boolean.class);
    }

    @Override
    public ExpressionNumber toExpressionNumber(final ExpressionEvaluationContext context) {
        return this.value().toExpressionNumber(context).negate(context);
    }

    @Override
    public String toString(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.toExpressionNumber(context), String.class);
    }

    @Override
    public Number toValue(final ExpressionEvaluationContext context) {
        return this.toExpressionNumber(context);
    }

    // printTree.......................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.printTreeTypeAndChildren(printer);
    }

    // Object ....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NegativeExpression;
    }

    @Override
    boolean equalsIgnoringParentAndChildren(final Expression other) {
        return true; // no other properties name already tested.
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(SYMBOL);
        this.value().toString0(b);
    }
}
