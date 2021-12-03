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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.tree.TestNode;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertSame;

public abstract class NonTerminalNodeSelectorTestCase<S extends NodeSelector<TestNode, StringName, StringName, Object>>
        extends NodeSelectorTestCase4<S> {

    NonTerminalNodeSelectorTestCase() {
        super();
    }

    @Test
    public final void testFinishedTrue() {
        this.applyFinisherAndCheck(this.createSelector(),
                TestNode.with("self"),
                () -> true);
    }

    @Test
    public final void testEqualsDifferentSelector() {
        this.checkNotEquals(this.createSelector().parent());
    }

    final void applyAndCheck0(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                              final TestNode start,
                              final String... nodes) {
        this.applyAndCheckRequiringOrder(selector, start, nodes);
        this.applyAndCheckUsingContext(selector, start, nodes);
    }

    final void applyAndCheckRequiringOrder(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                           final TestNode start,
                                           final String[] nodes) {
        final Set<TestNode> selected = Sets.ordered();
        assertSame(start,
                selector.apply(start,
                        context(Predicates.always(),
                                selected::add)));
        final List<String> selectedNames = selected
                .stream()
                .map(n -> n.name().value())
                .collect(Collectors.toList());
        this.checkEquals(Lists.of(nodes), selectedNames, "names of selected nodes");
    }

    final S createSelector(final NodeSelector<TestNode, StringName, StringName, Object> selector) {
        return Cast.to(this.createSelector().append(selector));
    }
}
