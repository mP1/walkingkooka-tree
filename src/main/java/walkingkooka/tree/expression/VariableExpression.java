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

import walkingkooka.HasValue;

import java.util.List;

/**
 * A expression which may have zero or more child expression.
 */
abstract class VariableExpression extends ParentExpression<ExpressionChildrenListVariable> implements HasValue<List<Expression>> {

    VariableExpression(final int index,
                       final ExpressionChildrenListVariable children) {
        super(
            index,
            children
        );
    }

    @Override //
    final ExpressionChildrenListVariable expressionChildrenList(final List<Expression> children) {
        return ExpressionChildrenListVariable.EMPTY.setElements(children);
    }

    // Visitor..........................................................................................................

    final void acceptValues(final ExpressionVisitor visitor) {
        for (Expression node : this.children()) {
            visitor.accept(node);
        }
    }
}
