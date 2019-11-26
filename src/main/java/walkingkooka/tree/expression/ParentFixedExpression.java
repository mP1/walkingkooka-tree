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

import java.util.List;

/**
 * Base class for either the unary or binary nodes.
 */
abstract class ParentFixedExpression extends ParentExpression {

    ParentFixedExpression(final int index, final List<Expression> children) {
        super(index, children);
    }

    @Override
    final void replaceChildrenCheck(final List<Expression> children) {
        final int expected = this.expectedChildCount();
        final int count = children.size();
        if (count != expected) {
            throw new IllegalArgumentException("Expected " + expected + " children but got " + count + "=" + children);
        }
    }

    abstract int expectedChildCount();

    // Node........................................................................................................

    @Override
    public Expression appendChild(final Expression child) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression removeChild(int index) {
        throw new UnsupportedOperationException();
    }

    @Override final boolean equalsIgnoringParentAndChildren(final Expression other) {
        return true; // no other properties name already tested.
    }
}
