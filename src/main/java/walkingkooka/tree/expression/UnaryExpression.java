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
import java.util.Optional;

/**
 * Base class for any expression with a single parameter.
 */
abstract class UnaryExpression extends ParentFixedExpression<ExpressionChildrenListUnary>
    implements HasValue<Expression> {

    UnaryExpression(final int index,
                    final ExpressionChildrenListUnary children) {
        super(
            index,
            children
        );
    }

    @Override //
    final ExpressionChildrenListUnary expressionChildrenList(final List<Expression> children) {
        return ExpressionChildrenListUnary.fromCollection(children);
    }

    @Override
    public final Expression value() {
        return this.children.child;
    }

    // Node.............................................................................................................

    @Override
    public final Optional<Expression> firstChild() {
        return Optional.of(
            this.children.child
        );
    }

    @Override
    public final Optional<Expression> lastChild() {
        return this.firstChild();
    }

    // Visitor........................................................................................................

    final void acceptValues(final ExpressionVisitor visitor) {
        visitor.accept(this.value());
    }
}
