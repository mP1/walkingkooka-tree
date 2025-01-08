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
 * A base {@link NodeSelector} for all sub classes that are not {@link CustomToStringNodeSelector}.
 */
abstract class NonCustomToStringNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
    extends
    NodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Package private constructor to limit sub classing.
     */
    NonCustomToStringNodeSelector() {
        super();
    }

    @Override final NodeSelector<N, NAME, ANAME, AVALUE> unwrapIfCustomToStringNodeSelector() {
        return this;
    }
}
