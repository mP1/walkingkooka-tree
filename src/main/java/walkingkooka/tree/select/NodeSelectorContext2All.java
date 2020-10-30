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

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

/**
 * The default {@link NodeSelectorContext2}.
 */
final class NodeSelectorContext2All<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends NodeSelectorContext2<N, NAME, ANAME, AVALUE> {

    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NodeSelectorContext2All<N, NAME, ANAME, AVALUE> with(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        return new NodeSelectorContext2All<>(context);
    }

    private NodeSelectorContext2All(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        super(context);
    }

    @Override
    NodeSelectorContext2<N, NAME, ANAME, AVALUE> all() {
        return this;
    }

    @Override
    NodeSelectorContext2<N, NAME, ANAME, AVALUE> expressionCreateIfNecessary() {
        return this.expression();
    }

    @Override
    NodeSelectorContext2<N, NAME, ANAME, AVALUE> expression() {
        return NodeSelectorContext2.expression(this.context);
    }

    @Override
    boolean nodePositionTest(final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    int nodePosition() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return this.context.toString();
    }
}
