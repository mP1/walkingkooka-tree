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

import java.util.Collection;
import java.util.Objects;

/**
 * An {@link walkingkooka.collect.list.ImmutableList} that holds the only child of a {@link UnaryExpression}.
 */
final class ExpressionChildrenListUnary extends ExpressionChildrenList<ExpressionChildrenListUnary> {

    static ExpressionChildrenListUnary fromCollection(final Collection<Expression> children) {
        Objects.requireNonNull(children, "children");

        ExpressionChildrenListUnary expressionChildrenListUnary;

        if (children instanceof ExpressionChildrenListUnary) {
            expressionChildrenListUnary = (ExpressionChildrenListUnary) children;
        } else {
            final Object[] childrenArray = children.toArray();

            unaryChildCount(
                childrenArray.length
            );

            expressionChildrenListUnary = with(
                (Expression) childrenArray[0]
            );
        }
        return expressionChildrenListUnary;
    }
    
    static ExpressionChildrenListUnary with(final Expression child) {
        return new ExpressionChildrenListUnary(
            child
        );
    }

    private ExpressionChildrenListUnary(final Expression child) {
        super();

        this.child = Objects.requireNonNull(child, "child");
    }

    @Override
    public Expression get(final int index) {
        Expression expression;

        switch (index) {
            case 0:
                expression = this.child;
                break;
            default:
                throw new IndexOutOfBoundsException("Invalid index " + index + " < 0 or > 1");
        }

        return expression;
    }

    @Override
    public int size() {
        return 1; // always 1
    }

    @Override
    public ExpressionChildrenListUnary setElements(final Collection<Expression> collection) {
        Objects.requireNonNull(collection, "collection");

        ExpressionChildrenListUnary expressionChildrenListUnary;

        if (collection instanceof ExpressionChildrenListUnary) {
            expressionChildrenListUnary = (ExpressionChildrenListUnary) collection;
        } else {
            final Object[] children = collection.toArray();
            unaryChildCount(
                children.length
            );

            expressionChildrenListUnary = this.setChild(
                (Expression) children[0]
            );
        }
        return expressionChildrenListUnary;
    }

    ExpressionChildrenListUnary setChild(final Expression child) {
        return this.child.equals(child) ?
            this :
            new ExpressionChildrenListUnary(
                child
            );
    }

    @Override
    ExpressionChildrenListVariable toExpressionChildrenListVariable() {
        return new ExpressionChildrenListVariable(
            this.child
        );
    }

    final Expression child;
}
