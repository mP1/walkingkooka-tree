/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
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

import walkingkooka.compare.CompareResult;
import walkingkooka.visit.Visiting;

import java.util.List;

public final class LessThanExpression extends CompareExpression {

    public final static ExpressionFunctionName NAME = ExpressionFunctionName.fromClass(LessThanExpression.class);

    public final static String SYMBOL = "<";

    static LessThanExpression with(final Expression left,
                                   final Expression right) {
        return new LessThanExpression(
            NO_INDEX,
            ExpressionChildrenListBinary.with(
                left,
                right
            )
        );
    }

    private LessThanExpression(final int index,
                               final ExpressionChildrenListBinary children) {
        super(
            index,
            children
        );
    }

    @Override
    public ExpressionFunctionName name() {
        return NAME;
    }

    @Override
    public LessThanExpression removeParent() {
        return this.removeParent0()
            .cast();
    }

    @Override
    public LessThanExpression setChildren(final List<Expression> children) {
        return this.setChildren0(children)
            .cast();
    }

    @Override
    LessThanExpression replaceChildren(final ExpressionChildrenListBinary expressionChildrenList) {
        return new LessThanExpression(
            NO_INDEX,
            expressionChildrenList
        );
    }

    @Override
    LessThanExpression replace(final int index) {
        return new LessThanExpression(
            index,
            this.children
        );
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

    @Override //
    CompareResult compareResult() {
        return CompareResult.LT;
    }

    // Object .........................................................................................................

    @Override
    void appendSymbol(final StringBuilder b) {
        b.append(SYMBOL);
    }
}
