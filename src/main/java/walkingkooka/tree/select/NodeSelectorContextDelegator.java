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

package walkingkooka.tree.select;

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

public interface NodeSelectorContextDelegator<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> extends NodeSelectorContext<N, NAME, ANAME, AVALUE> {

    @Override
    default boolean isFinished() {
        return this.nodeSelectorContext()
            .isFinished();
    }

    @Override
    default boolean test(final N node) {
        return this.nodeSelectorContext()
            .test(node);
    }

    @Override
    default N node() {
        return this.nodeSelectorContext()
            .node();
    }

    @Override
    default void setNode(final N node) {
        this.nodeSelectorContext()
            .setNode(node);
    }

    @Override
    default N selected(final N node) {
        return this.nodeSelectorContext()
            .selected(node);
    }

    @Override
    default NodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> expressionEvaluationContext(final N node) {
        return this.nodeSelectorContext()
            .expressionEvaluationContext(node);
    }

    NodeSelectorContext<N, NAME, ANAME, AVALUE> nodeSelectorContext();
}
