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

package walkingkooka.tree.select.parser;

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.select.NodeSelectorVisitor;
import walkingkooka.visit.VisitorTesting;

public interface NodeSelectorVisitorTesting<V extends NodeSelectorVisitor<N, NAME, ANAME, AVALUE>,
    N extends Node<N, NAME, ANAME, AVALUE>,
    NAME extends Name,
    ANAME extends Name,
    AVALUE>
    extends VisitorTesting<V, NodeSelector<N, NAME, ANAME, AVALUE>> {

    @Override
    default String typeNameSuffix() {
        return NodeSelectorVisitor.class.getSimpleName();
    }
}
