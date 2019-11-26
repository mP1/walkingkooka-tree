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
 * Base class for node predicates classes, aka {@link NamedNodeSelector} and {@link NodePredicateNodeSelector}
 */
abstract class NamedOrNodePredicateNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends NonTerminalNodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Package private constructor
     */
    NamedOrNodePredicateNodeSelector(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
    }

    /**
     * Ask the next {@link NodeSelector} to finish.
     */
    @Override
    final NodeSelectorContext2<N, NAME, ANAME, AVALUE> beginPrepareContext(final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return this.next.beginPrepareContext(context);
    }

    @Override
    final NodeSelectorContext2<N, NAME, ANAME, AVALUE> finishPrepareContext(final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return this.next.finishPrepareContext(context);
    }

    @Override final N select(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return this.selectNext(node, context);
    }
}
