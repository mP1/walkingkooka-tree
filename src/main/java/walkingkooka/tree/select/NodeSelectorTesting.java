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

import walkingkooka.collect.set.Sets;
import walkingkooka.naming.Name;
import walkingkooka.test.Testing;
import walkingkooka.tree.Node;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public interface NodeSelectorTesting<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> extends Testing {

    N createNode();

    @SuppressWarnings("unchecked")
    default void selectorApplyAndCheck(final N node,
                                       final NodeSelector<N, NAME, ANAME, AVALUE> selector,
                                       final N... expected) {
        final List<N> selected = this.selectorApplyAndCollect(node, selector);
        this.checkEquals(
                Sets.of(expected),
                new LinkedHashSet<>(selected),
                () -> "incorrect nodes selected, selector:\n" + selector
        );
    }

    default void selectorApplyAndCheckCount(final N node,
                                            final NodeSelector<N, NAME, ANAME, AVALUE> selector,
                                            final int count) {
        final List<N> selected = this.selectorApplyAndCollect(node, selector);
        this.checkEquals(
                count,
                selected.size(),
                () -> "incorrect number of matched node for selector\n" + selected
        );
    }

    default List<N> selectorApplyAndCollect(final N node,
                                            final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        final Set<N> selected = Sets.ordered();
        selector.apply(node,
                this.nodeSelectorContext(
                        (n) -> {
                        }, // ignore potentials
                        selected::add)); // capture selecteds

        return new ArrayList<>(selected);
    }

    default NodeSelectorContext<N, NAME, ANAME, AVALUE> nodeSelectorContext(final Consumer<N> potential,
                                                                            final Consumer<N> selected) {
        return new FakeNodeSelectorContext<>() {

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public void setNode(final N node) {
                // nop
            }

            @Override
            public boolean test(final N node) {
                potential.accept(node);
                return true;
            }

            @Override
            public N selected(final N node) {
                selected.accept(node);
                return node;
            }

            @Override
            public String toString() {
                return "selected: " + selected;
            }
        };
    }

    default void selectorApplyMapAndCheck(final N node,
                                          final NodeSelector<N, NAME, ANAME, AVALUE> selector,
                                          final Function<N, N> mapper,
                                          final N expected) {
        this.checkEquals(
                expected,
                selector.apply(node, new FakeNodeSelectorContext<>() {

                    @Override
                    public boolean isFinished() {
                        return false;
                    }

                    @Override
                    public boolean test(final N node) {
                        return true;
                    }

                    @Override
                    public N selected(final N node) {
                        return mapper.apply(node);
                    }

                    @Override
                    public String toString() {
                        return mapper.toString();
                    }
                }),
                () -> "selector " + selector + " map failed"
        );
    }
}
