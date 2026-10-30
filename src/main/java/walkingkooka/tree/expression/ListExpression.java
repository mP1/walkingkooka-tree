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

import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.Objects;

/**
 * A {@link ListExpression} holds an ordered sequence of values, what may be termed an array or list in other programming languages.
 */
public final class ListExpression extends VariableExpression {

    public final static ExpressionFunctionName NAME = ExpressionFunctionName.fromClass(ListExpression.class);

    /**
     * Creates a new {@link ListExpression}
     */
    static ListExpression with(final List<Expression> parameters) {
        Objects.requireNonNull(parameters, "parameters");

        return new ListExpression(
            NO_INDEX,
            ExpressionChildrenListVariable.EMPTY.setElements(parameters)
        );
    }

    /**
     * Private ctor
     */
    private ListExpression(final int index,
                           final ExpressionChildrenListVariable parameters) {
        super(
            index,
            parameters
        );
    }

    @Override
    public ExpressionFunctionName name() {
        return NAME;
    }

    @Override
    public ListExpression removeParent() {
        return this.removeParent0()
            .cast();
    }

    @Override
    public ListExpression setChildren(final List<Expression> children) {
        return this.setChildren0(children)
            .cast();
    }

    @Override
    ListExpression replaceChildren(final ExpressionChildrenListVariable expressionChildrenList) {
        return new ListExpression(
            NO_INDEX,
            expressionChildrenList
        );
    }

    @Override
    ListExpression replaceIndex(final int index) {
        return new ListExpression(
            this.index,
            this.children
        );
    }

    // Value.........................................................................................................

    @Override
    public List<Expression> value() {
        return this.children();
    }

    // ExpressionPurity.................................................................................................

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return this.isPureChildren(context);
    }

    // Visitor.........................................................................................................

    @Override
    public void accept(final ExpressionVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // evaluation .....................................................................................................

    @Override
    ExpressionFunction<?, ExpressionEvaluationContext> function(final ExpressionEvaluationContext context) {
        return this.toValueFunction();
    }

    @Override
    public Object apply(final List<Object> value,
                        final ExpressionEvaluationContext context) {
        return value;
    }

    // printTree.......................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.printTreeTypeAndChildren(printer);
    }

    // Object.........................................................................................................

    @Override
    boolean equalsIgnoringChildren(final Expression other) {
        return true; // no new properties to equality check
    }

    /**
     * Returns a csv {@link String} of the values.
     */
    @Override
    void toString0(final StringBuilder b) {
        b.append('[');

        final List<Expression> expressions = this.value();
        int last = expressions.size() - 1;

        for (final Expression value : expressions) {
            value.toString0(b);
            last--;
            if (last >= 0) {
                b.append(',');
            }
        }

        b.append(']');
    }
}
