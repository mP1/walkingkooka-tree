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

import walkingkooka.Value;

import java.util.List;

/**
 * A expression which may have zero or more child expression.
 */
abstract class ExpressionVariableNode extends ExpressionParentNode implements Value<List<ExpressionNode>> {

    ExpressionVariableNode(final int index, final List<ExpressionNode> expressions) {
        super(index, expressions);
    }

    @Override
    final void replaceChildrenCheck(final List<ExpressionNode> children){
        // nothing to check
    }

    // Visitor........................................................................................................

    final void acceptValues(final ExpressionNodeVisitor visitor) {
        for (ExpressionNode node : this.children()) {
            visitor.accept(node);
        }
    }

    // Object........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionVariableNode;
    }
}
