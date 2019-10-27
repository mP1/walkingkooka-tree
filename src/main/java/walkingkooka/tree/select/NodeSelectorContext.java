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

package walkingkooka.tree.select;

import walkingkooka.Context;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.function.ExpressionFunctionContext;

/**
 * The {@link Context} that accompanies all match requests. Not it gathers the selected nodes and so cant be reused.
 */
public interface NodeSelectorContext<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> extends ExpressionFunctionContext {

    /**
     * One time flag that when true aborts future attempts to test and select additional {@link Node nodes}.
     */
    boolean isFinished();

    /**
     * Test method that filters {@link Node} prior to selection. If the test returns false the {@link Node} is not selected and is ignored.
     */
    boolean test(final N node);

    /**
     * Sets the current {@link Node}, in preparation for predicates or functions that read attributes or other values from
     * on the current {@link Node}.
     */
    void setNode(final N node);

    /**
     * Invoked with each and every selected {@link Node node}.
     */
    N selected(final N node);
}
