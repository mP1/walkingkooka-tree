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
import java.util.Objects;

abstract class ExpressionChildrenList<T extends ExpressionChildrenList<T>> extends AbstractList<Expression>
    implements ImmutableListDefaults<T, Expression> {

    ExpressionChildrenList() {
        super();
    }

    @Override
    public final void elementCheck(final Expression expression) {
        Objects.requireNonNull(expression, "expression");
    }

    abstract T setParent(final ParentExpression<?> parent);

    static void binaryChildCount(final int count) {
        if (2 != count) {
            throw new IllegalArgumentException("Expected left and right got " + count + " children");
        }
    }

    static void unaryChildCount(final int count) {
        if (1 != count) {
            throw new IllegalArgumentException("Expected one child got " + count + " children");
        }
    }

    abstract ExpressionChildrenListUnary toExpressionChildrenListUnary();

    abstract ExpressionChildrenListBinary toExpressionChildrenListBinary();

    abstract ExpressionChildrenListVariable toExpressionChildrenListVariable();

    @Override
    public final T subList(final int from,
                           final int to) {
        if (from < 0) {
            throw new IndexOutOfBoundsException("Invalid from index " + from + " < 0");
        }

        if (to < from) {
            throw new IndexOutOfBoundsException("Invalid to index " + to + " < " + from);
        }

        final int size = this.size();
        if (to > size) {
            throw new IndexOutOfBoundsException("Invalid to index " + to + " > " + size);
        }

        if (0 == from && this.size() == to) {
            return (T) this;
        }
        throw new UnsupportedOperationException();
    }
}
