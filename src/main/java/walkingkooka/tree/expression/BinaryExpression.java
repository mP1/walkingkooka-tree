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

import java.util.List;
import java.util.Optional;

/**
 * Base class for any expression that accepts two parameters.
 */
abstract public class BinaryExpression extends ParentFixedExpression<ExpressionChildrenListBinary> {

    BinaryExpression(final int index,
                     final ExpressionChildrenListBinary children) {
        super(
            index,
            children
        );
    }

    public final Expression left() {
        return this.children.left;
    }

    public final Expression right() {
        return this.children.right;
    }

    @Override //
    final ExpressionChildrenListBinary expressionChildrenList(final List<Expression> children) {
        return ExpressionChildrenListBinary.fromCollection(children);
    }

    // Node........................................................................................................

    @Override
    public final Optional<Expression> firstChild() {
        return Optional.of(
            this.left()
        );
    }

    @Override
    public final Optional<Expression> lastChild() {
        return Optional.of(
            this.right()
        );
    }

    // Visitor........................................................................................................

    final void acceptValues(final ExpressionVisitor visitor) {
        visitor.accept(this.left());
        visitor.accept(this.right());
    }

    // Evaluation ......................................................................................................

    @Override
    ExpressionFunction<?, ExpressionEvaluationContext> function(final ExpressionEvaluationContext context) {
        return this.toValueFunction();
    }

    /**
     * Executes the binary expression after converting both the left and right to values. This is necessary
     * because {@link LogicalExpression} needs to perform a post operation converting the value to {@link Boolean}.
     */
    final Object apply(final ExpressionEvaluationContext context) {
        return this.apply(
            this.left()
                .toValue(context),
            this.right()
                .toValue(context),
            context
        );
    }

    /**
     * Sub-classes must take the left and right values and create a result.
     */
    abstract public Object apply(final Object left,
                                 final Object right,
                                 final ExpressionEvaluationContext context);

    // printTree.......................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        this.printTreeTypeAndChildren(printer);
    }

    // Object........................................................................................................

    @Override //
    final boolean equalsIgnoringChildren(final Expression other) {
        return true; // no other properties name was already tested.
    }

    @Override
    final void toString0(final StringBuilder b) {
        this.left().toString0(b);
        this.appendSymbol(b);
        this.right().toString0(b);
    }

    abstract void appendSymbol(final StringBuilder b);
}
