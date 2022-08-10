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

import walkingkooka.collect.list.Lists;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Base class for any expression that accepts two parameters.
 */
abstract class BinaryExpression extends ParentFixedExpression {

    static void check(final Expression left, final Expression right) {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
    }

    BinaryExpression(final int index, final Expression left, final Expression right) {
        super(index, Lists.of(left, right));
    }

    public final Expression left() {
        return this.children().get(0);
    }

    public final Expression right() {
        return this.children().get(1);
    }

    @Override
    int expectedChildCount() {
        return 2;
    }

    @Override
    final BinaryExpression replace0(final int index, final List<Expression> children) {
        return replace1(index, children.get(0), children.get(1));
    }

    abstract BinaryExpression replace1(final int index, final Expression left, final Expression right);

    // Node........................................................................................................

    @Override
    public final Optional<Expression> firstChild() {
        return Optional.of(this.left());
    }

    @Override
    public final Optional<Expression> lastChild() {
        return Optional.of(this.right());
    }

    // Visitor........................................................................................................

    final void acceptValues(final ExpressionVisitor visitor) {
        visitor.accept(this.left());
        visitor.accept(this.right());
    }

    // Evaluation ...................................................................................................

    @Override
    ExpressionFunction<?, ExpressionEvaluationContext> function(final ExpressionEvaluationContext context) {
        return this.toValueFunction();
    }

    @Override
    public final boolean toBoolean(final ExpressionEvaluationContext context) {
        return this.apply(context)
                .toBoolean(context);
    }

    @Override
    public final ExpressionNumber toExpressionNumber(final ExpressionEvaluationContext context) {
        return this.apply(context)
                .toExpressionNumber(context);
    }

    @Override
    public final String toString(final ExpressionEvaluationContext context) {
        return this.apply(context)
                .toString(context);
    }

    final Expression apply(final ExpressionEvaluationContext context) {
        return this.apply(
                this.left().toValue(context),
                this.right().toValue(context),
                context
        );
    }

    /**
     * Sub classes must take the left and right values and create a result.
     */
    abstract Expression apply(final Object left,
                              final Object right,
                              final ExpressionEvaluationContext context);

    // printTree.......................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        this.printTreeTypeAndChildren(printer);
    }

    // Object........................................................................................................

    @Override final boolean equalsIgnoringParentAndChildren(final Expression other) {
        return true; // no other properties name already tested.
    }

    final void toString0(final StringBuilder b) {
        this.left().toString0(b);
        this.appendSymbol(b);
        this.right().toString0(b);
    }

    abstract void appendSymbol(final StringBuilder b);
}
