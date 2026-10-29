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
import walkingkooka.text.printer.TreePrintable;

import java.util.List;

/**
 * A parent holding zero or more child expressions.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
abstract class ParentExpression<T extends ExpressionChildrenList<T>> extends Expression {

    ParentExpression(final int index,
                     final T children) {
        super(index);
        this.children = children.setParent(this);
    }

    @Override
    public final List<Expression> children() {
        return this.children;
    }

    public final T children;

    final ParentExpression<T> setChildren0(final List<Expression> children) {
        final T expressionChildrenList = this.expressionChildrenList(children);

        return expressionChildrenList.equals(this.children) ?
            this :
            this.replaceChildren(expressionChildrenList);
    }

    abstract T expressionChildrenList(final List<Expression> children);

    abstract ParentExpression<T> replaceChildren(final T expressionChildrenList);

    @Override //
    final Expression setChild(final Expression newChild) {
        return this.setChildren(
            this.children.replace(
                newChild.index(),
                newChild
            )
        );
    }

    /**
     * Invokes {@link #toValue(ExpressionEvaluationContext)}.
     */
    @Override
    public final Object toReferenceOrValue(final ExpressionEvaluationContext context) {
        return this.toValue(context);
    }


    @Override
    public final boolean toBoolean(final ExpressionEvaluationContext context) {
        return this.toValueAndConvert(
            context,
            Boolean.class
        );
    }

    @Override
    public final ExpressionNumber toExpressionNumber(final ExpressionEvaluationContext context) {
        return this.toValueAndConvert(
            context,
            ExpressionNumber.class
        );
    }

    @Override
    public final String toString(final ExpressionEvaluationContext context) {
        return this.toValueAndConvert(
            context,
            String.class
        );
    }

    final <T> T toValueAndConvert(final ExpressionEvaluationContext context,
                                  final Class<T> type) {
        return context.convertOrFail(
            this.toValue(context),
            type
        );
    }

    // ExpressionPurity..................................................................................................

    final boolean isPureChildren(final ExpressionPurityContext context) {
        boolean pure = true;

        for (final Expression expression : this.children()) {
            pure = pure & expression.isPure(context);
            if (false == pure) {
                break;
            }
        }

        return pure;
    }

    // TreePrintable....................................................................................................

    /**
     * Most subclasses of {@link ParentExpression} only have children and no other properties, making this a perfect
     * fit to printTree themselves.
     */
    final void printTreeTypeAndChildren(final IndentingPrinter printer) {
        this.printTreeType(printer);
        this.printTreeChildren(printer);
    }

    final void printTreeType(final IndentingPrinter printer) {
        printer.print(this.typeName());
        printer.println();
    }

    final void printTreeChildren(final IndentingPrinter printer) {
        printer.indent();

        for (final Object child : this.children()) {
            TreePrintable.printTreeOrToString(
                child,
                printer
            );
        }

        printer.outdent();
    }

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return this.children()
            .hashCode();
    }

    @Override //
    final boolean equalsChildren(final Expression other) {
        return this.children()
            .equals(
                other.children()
            );
    }
}
