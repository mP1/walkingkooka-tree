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

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * An {@link walkingkooka.collect.list.ImmutableList} that holds the left and right children of a {@link BinaryExpression}.
 */
final class ExpressionChildrenListVariable extends ExpressionChildrenList<ExpressionChildrenListVariable> {

    final static ExpressionChildrenListVariable EMPTY = new ExpressionChildrenListVariable(
        new Expression[0]
    );

    // @VisibleForTesting
    ExpressionChildrenListVariable(final Expression... children) {
        super();

        this.children = Objects.requireNonNull(children, "children");
    }

    @Override
    public Expression get(final int index) {
        return this.children[index];
    }

    @Override
    public int size() {
        return this.children.length;
    }

    @Override
    public ExpressionChildrenListVariable setElements(final Collection<Expression> collection) {
        Objects.requireNonNull(collection, "collection");

        ExpressionChildrenListVariable expressionChildrenListVariable;

        if (collection instanceof ExpressionChildrenListVariable) {
            expressionChildrenListVariable = (ExpressionChildrenListVariable) collection;
        } else {
            final Object[] from = collection.toArray();
            final Expression[] copy = new Expression[from.length];

            int i = 0;
            for (final Object child : from) {
                Objects.requireNonNull(child, "child");
                copy[i++] = (Expression) child;
            }

            expressionChildrenListVariable =
                Arrays.equals(
                    copy,
                    this.children
                ) ?
                    this :
                    new ExpressionChildrenListVariable(copy);
        }
        return expressionChildrenListVariable;
    }

    @Override
    ExpressionChildrenListVariable toExpressionChildrenListVariable() {
        return this;
    }

    private final Expression[] children;
}
