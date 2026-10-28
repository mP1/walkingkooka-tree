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

import walkingkooka.collect.list.ImmutableListDefaults;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Objects;

/**
 * An {@link walkingkooka.collect.list.ImmutableList} that holds the only child of a {@link UnaryExpression}.
 */
final class UnaryExpressionChildrenList extends AbstractList<Expression>
    implements ImmutableListDefaults<UnaryExpressionChildrenList, Expression> {

    static UnaryExpressionChildrenList with(final Expression child) {
        return new UnaryExpressionChildrenList(
            child
        );
    }

    private UnaryExpressionChildrenList(final Expression child) {
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
    public UnaryExpressionChildrenList setElements(final Collection<Expression> collection) {
        Objects.requireNonNull(collection, "collection");

        UnaryExpressionChildrenList binaryExpressionChildrenList;

        if (collection instanceof UnaryExpressionChildrenList) {
            binaryExpressionChildrenList = (UnaryExpressionChildrenList) collection;
        } else {
            final Object[] children = collection.toArray();
            final int count = children.length;

            if (1 != count) {
                throw new IllegalArgumentException("Expected one child got " + count + " children");
            }

            binaryExpressionChildrenList = this.setChild(
                (Expression) children[0]
            );
        }
        return binaryExpressionChildrenList;
    }

    UnaryExpressionChildrenList setChild(final Expression child) {
        return this.child.equals(child) ?
            this :
            new UnaryExpressionChildrenList(
                child
            );
    }

    @Override
    public void elementCheck(final Expression expression) {
        Objects.requireNonNull(expression, "expression");
    }

    final Expression child;
}
