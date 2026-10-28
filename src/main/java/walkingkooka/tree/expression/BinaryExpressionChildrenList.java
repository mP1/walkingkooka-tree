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
 * An {@link walkingkooka.collect.list.ImmutableList} that holds the left and right children of a {@link BinaryExpression}.
 */
final class BinaryExpressionChildrenList extends AbstractList<Expression>
    implements ImmutableListDefaults<BinaryExpressionChildrenList, Expression> {

    static BinaryExpressionChildrenList with(final Expression left,
                                             final Expression right) {
        return new BinaryExpressionChildrenList(
            left,
            right
        );
    }

    private BinaryExpressionChildrenList(final Expression left,
                                         final Expression right) {
        super();

        this.left = Objects.requireNonNull(left, "left");
        this.right = Objects.requireNonNull(right, "right");
    }

    @Override
    public Expression get(final int index) {
        Expression expression;

        switch (index) {
            case 0:
                expression = left;
                break;
            case 1:
                expression = right;
                break;
            default:
                throw new IndexOutOfBoundsException("Invalid index " + index + " < 0 or > 2");
        }

        return expression;
    }

    @Override
    public int size() {
        return 2; // always 2
    }

    @Override
    public BinaryExpressionChildrenList setElements(final Collection<Expression> collection) {
        Objects.requireNonNull(collection, "collection");

        BinaryExpressionChildrenList binaryExpressionChildrenList;

        if (collection instanceof BinaryExpressionChildrenList) {
            binaryExpressionChildrenList = (BinaryExpressionChildrenList) collection;
        } else {
            final Object[] children = collection.toArray();
            final int count = children.length;

            if (2 != count) {
                throw new IllegalArgumentException("Expected left and right got " + count + " children");
            }

            binaryExpressionChildrenList = this.setLeftAndRight(
                (Expression) children[0],
                (Expression) children[1]
            );
        }
        return binaryExpressionChildrenList;
    }

    BinaryExpressionChildrenList setLeftAndRight(final Expression left,
                                                 final Expression right) {
        return this.left.equals(left) &&
            this.right.equals(right) ?
            this :
            new BinaryExpressionChildrenList(
                left,
                right
            );
    }

    @Override
    public void elementCheck(final Expression expression) {
        Objects.requireNonNull(expression, "expression");
    }

    final Expression left;

    final Expression right;
}
