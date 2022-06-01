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
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.tree.TestNode;
import walkingkooka.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

final public class TerminalNodeSelectorTest
        extends NodeSelectorTestCase4<TerminalNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testFinishedTrue() {
        this.applyFinisherAndCheck(this.createSelector(),
                TestNode.with("self"),
                () -> true);
    }

    @Test
    public void testFilterFalse() {
        this.applyFilterAndCheck(this.createSelector(),
                TestNode.with("self"),
                Predicates.never());
    }

    @Test
    public void testFilterTrue() {
        final TestNode self = TestNode.with("self");
        this.applyFilterAndCheck(this.createSelector(),
                self,
                Predicates.always(),
                self);
    }

    @Test
    public void testMap() {
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child1"), TestNode.with("child2"), TestNode.with("child3"));

        TestNode.clear();

        this.acceptMapAndCheck(parent,
                TestNode.with("parent*0",
                        TestNode.with("child1"), TestNode.with("child2"), TestNode.with("child3")));
    }

    // NodeSelectorVisitor............................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<NodeSelector<TestNode, StringName, StringName, Object>> visited = Lists.array();

        final TerminalNodeSelector<TestNode, StringName, StringName, Object> selector = this.createSelector();

        new FakeNodeSelectorVisitor<TestNode, StringName, StringName, Object>() {
            @Override
            protected Visiting startVisit(final NodeSelector<TestNode, StringName, StringName, Object> s) {
                b.append("1");
                visited.add(s);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodeSelector<TestNode, StringName, StringName, Object> s) {
                b.append("2");
                visited.add(s);
            }

            @Override
            protected void visitTerminal(final NodeSelector<TestNode, StringName, StringName, Object> s) {
                assertSame(s, s);
                b.append("3");
                visited.add(s);
            }
        }.accept(selector);

        this.checkEquals("132", b.toString());

        this.checkEquals(Lists.of(selector, selector, selector),
                visited,
                "visited");
    }

    // Object....................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "");
    }

    @Override
    TerminalNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return TerminalNodeSelector.get();
    }

    @Override
    void applyAndCheck0(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                        final TestNode start,
                        final String... nodes) {
        this.applyAndCheckUsingContext(selector, start, nodes);
    }

    @Override
    public Class<TerminalNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(TerminalNodeSelector.class);
    }
}
