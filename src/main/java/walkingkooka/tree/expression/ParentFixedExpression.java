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

/**
 * Base class for either the unary or binary nodes.
 */
abstract class ParentFixedExpression<T extends ExpressionChildrenList<T>> extends ParentExpression<T> {

    ParentFixedExpression(final int index,
                          final T children) {
        super(
            index,
            children
        );
    }

    // Node........................................................................................................

    @Override
    public final Expression appendChild(final Expression child) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Expression removeChild(int index) {
        throw new UnsupportedOperationException();
    }

    // ExpressionPurity.................................................................................................

    @Override
    public final boolean isPure(final ExpressionPurityContext context) {
        return this.isPureChildren(context);
    }
}