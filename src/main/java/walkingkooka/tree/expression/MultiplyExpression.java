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

import java.util.List;

/**
 * A multiply expression.
 */
public final class MultiplyExpression extends BinaryArithmeticExpression {

    public final static FunctionExpressionName NAME = FunctionExpressionName.fromClass(MultiplyExpression.class);

    public final static String SYMBOL = "*";

    static MultiplyExpression with(final Expression left, final Expression right) {
        check(left, right);
        return new MultiplyExpression(NO_INDEX, left, right);
    }

    private MultiplyExpression(final int index, final Expression left, final Expression right) {
        super(index, left, right);
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    @Override
    public MultiplyExpression removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    public MultiplyExpression setChildren(final List<Expression> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    MultiplyExpression replace1(final int index, final Expression left, final Expression right) {
        return new MultiplyExpression(index, left, right);
    }

    // Visitor .........................................................................................................

    @Override
    public void accept(final ExpressionVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // Evaluation .......................................................................................................

    @Override
    String applyText0(final String left, final String right, final ExpressionEvaluationContext context) {
        throw new UnsupportedOperationException(left + SYMBOL + right); // MAYBE try and convert right to int and times the string.
    }

    @Override
    ExpressionNumber applyExpressionNumber0(final ExpressionNumber left,
                                            final ExpressionNumber right,
                                            final ExpressionEvaluationContext context) {
        return left.multiply(right, context);
    }

    // Object .........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof MultiplyExpression;
    }

    @Override
    void appendSymbol(final StringBuilder b) {
        b.append(SYMBOL);
    }
}
