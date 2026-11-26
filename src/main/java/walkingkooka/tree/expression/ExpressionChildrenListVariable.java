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

import walkingkooka.collect.list.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An {@link walkingkooka.collect.list.ImmutableList} that holds the left and right children of a {@link BinaryExpression}.
 */
final class ExpressionChildrenListVariable extends ExpressionChildrenList<ExpressionChildrenListVariable> {

    final static ExpressionChildrenListVariable EMPTY = new ExpressionChildrenListVariable(
        Lists.empty()
    );

    // @VisibleForTesting
    ExpressionChildrenListVariable(final List<Expression> children) {
        super();

        this.children = Objects.requireNonNull(children, "children");
    }

    @Override
    public Expression get(final int index) {
        return this.children.get(index);
    }

    @Override
    public int size() {
        return this.children.size();
    }

    @Override
    public ExpressionChildrenListVariable setElements(final Collection<Expression> collection) {
        Objects.requireNonNull(collection, "collection");

        final ExpressionChildrenListVariable expressionChildrenListVariable;

        if (collection instanceof ExpressionChildrenListVariable) {
            expressionChildrenListVariable = (ExpressionChildrenListVariable) collection;
        } else {
            final List<Expression> copy = Lists.array();

            for (final Expression child : collection) {
                Objects.requireNonNull(child, "child");
                copy.add(child);
            }

            expressionChildrenListVariable = this.children.equals(copy)
                ?
                this :
                new ExpressionChildrenListVariable(copy);
        }
        return expressionChildrenListVariable;
    }

    @Override
    ExpressionChildrenListVariable setParent(final ParentExpression parent) {
        final Optional<Expression> optionalParent = Optional.of(parent);

        final List<Expression> from = this.children;
        final int count = from.size();
        final List<Expression> after = Lists.array();

        for (int i = 0; i < count; i++) {
            after.add(
                from.get(i)
                    .setParent(
                        optionalParent,
                        i
                    )
            );
        }

        return new ExpressionChildrenListVariable(after);
    }

    @Override
    ExpressionChildrenListUnary toExpressionChildrenListUnary() {
        unaryChildCount(
            this.size()
        );
        return ExpressionChildrenListUnary.with(
            this.get(0)
        );
    }

    @Override
    ExpressionChildrenListBinary toExpressionChildrenListBinary() {
        return ExpressionChildrenListBinary.fromCollection(this);
    }

    @Override
    ExpressionChildrenListVariable toExpressionChildrenListVariable() {
        return this;
    }

    private final List<Expression> children;
}
