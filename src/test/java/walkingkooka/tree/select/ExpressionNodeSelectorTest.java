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
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;


final public class ExpressionNodeSelectorTest extends
        NonTerminalNodeSelectorTestCase<ExpressionNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testWithNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> ExpressionNodeSelector.with(null));
    }

    @Test
    public void testFinishedTrueChildren() {
        this.applyFinisherAndCheck(this.createSelector().children(),
                TestNode.with("self"),
                () -> true);
    }

    @Test
    public void testAppendExpressionTrue() {
        final NodeSelector<TestNode, StringName, StringName, Object> selector = TestNode.relativeNodeSelector()
                .children()
                .expression(Expression.value(true));
        this.checkEqualsAndHashCode(TestNode.relativeNodeSelector()
                        .children()
                        .setToString("child::*[true()]"),
                selector);
    }

    @Test
    public void testExpressionSelfSelected() {
        final TestNode self = TestNode.with("self");
        this.applyAndCheck(self, self);
    }

    @Test
    public void testExpressionIgnoresNonSelfNodes() {
        final TestNode siblingBefore = TestNode.with("siblingBefore");
        final TestNode self = TestNode.with("self", TestNode.with("child"));
        final TestNode siblingAfter = TestNode.with("siblingAfter");
        final TestNode parent = TestNode.with("parent", siblingBefore, self, siblingAfter);

        this.applyAndCheck(parent.child(1), self);
    }

    @Test
    public void testExpressionFunctionBooleanChildrenSelectorAppended() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent = TestNode.with("self", child1, child2);

        this.applyAndCheck(ExpressionNodeSelector.<TestNode, StringName, StringName, Object>with(Expression.value(true)).children(),
                parent,
                child1, child2);
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Test
    public void testChildrenExpressionNumberPosition1() {
        this.childrenExpressionNumberPositionAndCheck(NodeSelector.INDEX_BIAS + 0, 0);
    }

    @Test
    public void testChildrenExpressionNumberPosition2() {
        this.childrenExpressionNumberPositionAndCheck(NodeSelector.INDEX_BIAS + 1, 1);
    }

    @Test
    public void testChildrenExpressionNumberPosition3() {
        this.childrenExpressionNumberPositionAndCheck(NodeSelector.INDEX_BIAS + 2, 2);
    }

    @Test
    public void testChildrenExpressionNumberPositionInvalid() {
        this.childrenExpressionNumberPositionAndCheck(0, -1);
    }

    @Test
    public void testChildrenExpressionNumberPositionOutOfBounds() {
        this.childrenExpressionNumberPositionAndCheck(99, -1);
    }

    private void childrenExpressionNumberPositionAndCheck(final long value,
                                                          final int childIndex) {
        this.childrenExpressionNumberPositionAndCheck(
                Expression.value(
                        ExpressionNumberKind.DEFAULT.create(value)
                ),
                childIndex
        );
    }

    private void childrenExpressionNumberPositionAndCheck(final Expression expression,
                                                          final int childIndex) {
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child1", TestNode.with("grandChild1")),
                TestNode.with("child2", TestNode.with("grandChild2")),
                TestNode.with("child3", TestNode.with("grandChild3")));

        this.applyAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .expression(expression),
                parent,
                childIndex < 0 ? new TestNode[0] : new TestNode[]{parent.child(childIndex)});
    }

    @Test
    public void testChildrenExpressionNumberPositionAttribute() {
        TestNode.disableUniqueNameChecks();

        final TestNode parent = TestNode.with("parent",
                nodeWithAttributes("child1", "A1", "V1"),
                nodeWithAttributes("child2", "B2", "V2"),
                nodeWithAttributes("child3", "B2", "V3"));

        this.applyAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .expression(expressionNumber(2))
                        .attributeValueStartsWith(Names.string("B2"), "V"),
                parent,
                parent.child(1));
    }

    @Test
    public void testChildrenExpressionFilter() {
        final TestNode parent = TestNode.with("parent",
                nodeWithAttributes("child1", "A1", "V1"),
                nodeWithAttributes("child2", "B2", "V2"),
                nodeWithAttributes("child3", "B2", "V3"));

        this.applyFilterAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .expression(expressionNumber(2))
                        .attributeValueStartsWith(Names.string("B2"), "V"),
                parent,
                Predicates.never());
    }

    @Test
    public void testChildrenExpressionFilter2() {
        final TestNode parent = TestNode.with("parent",
                nodeWithAttributes("child1", "A1", "V1"),
                nodeWithAttributes("child2", "B2", "V2"),
                nodeWithAttributes("child3", "B2", "V3"));

        this.applyFilterAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .expression(expressionNumber(2))
                        .attributeValueStartsWith(Names.string("B2"), "V"),
                parent,
                (n) -> !n.name().value().equals("child3"), // select parent, child1, child2, skip child3
                parent.child(1)); // $child2
    }

    @Test
    public void testExpressionFalseMap() {
        final TestNode node = TestNode.with("node");

        this.acceptMapAndCheck(ExpressionNodeSelector.with(Expression.value(false)),
                node);
    }

    @Test
    public void testExpressionTrueMap() {
        this.acceptMapAndCheck(ExpressionNodeSelector.with(Expression.value(true)),
                TestNode.with("node"),
                TestNode.with("node*0"));
    }

    @Test
    public void testExpressionTrue2Map() {
        final TestNode parent = TestNode.with("parent", TestNode.with("child"));

        TestNode.clear();

        this.acceptMapAndCheck(ExpressionNodeSelector.with(Expression.value(true)),
                parent.child(0),
                TestNode.with("parent", TestNode.with("child*0")).child(0));
    }

    @Test
    public void testExpressionTrueIgnoresChildrenMap() {
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child",
                        TestNode.with("grand-child1"), TestNode.with("grand-child2")));

        TestNode.clear();

        this.acceptMapAndCheck(ExpressionNodeSelector.with(Expression.value(true)),
                parent.child(0),
                TestNode.with("parent",
                                TestNode.with("child*0",
                                        TestNode.with("grand-child1"), TestNode.with("grand-child2")))
                        .child(0));
    }

    @Test
    public void testExpressionNumberNegativeMap() {
        this.acceptMapAndCheck(ExpressionNodeSelector.with(expressionNumber(-2)),
                TestNode.with("node"));
    }

    @Test
    public void testExpressionNumberOutOfRangeMap() {
        this.acceptMapAndCheck(ExpressionNodeSelector.with(expressionNumber(999)),
                TestNode.with("node"));
    }

    @Test
    public void testChildrenExpressionNumberInvalidMap() {
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child1"),
                TestNode.with("child2"),
                TestNode.with("child3"));

        TestNode.clear();

        this.acceptMapAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .expression(expressionNumber(99)),
                parent,
                parent);
    }

    @Test
    public void testChildrenExpressionNumberMap() {
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child1"),
                TestNode.with("child2"),
                TestNode.with("child3"));

        TestNode.clear();

        //noinspection PointlessArithmeticExpression
        this.acceptMapAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .expression(expressionNumber(NodeSelector.INDEX_BIAS + 0)),
                parent,
                TestNode.with("parent", TestNode.with("child1*0"), TestNode.with("child2"), TestNode.with("child3")));
    }

    @Test
    public void testChildrenExpressionNumberMap2() {
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child1"),
                TestNode.with("child2"),
                TestNode.with("child3"));

        TestNode.clear();

        this.acceptMapAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .expression(expressionNumber(NodeSelector.INDEX_BIAS + 1)),
                parent,
                TestNode.with("parent", TestNode.with("child1"), TestNode.with("child2*0"), TestNode.with("child3")));
    }

    @Test
    public void testChildrenExpressionNumberMap3() {
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child1"),
                TestNode.with("child2"),
                TestNode.with("child3"));

        TestNode.clear();

        this.acceptMapAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .expression(expressionNumber(NodeSelector.INDEX_BIAS + 2)),
                parent,
                TestNode.with("parent", TestNode.with("child1"), TestNode.with("child2"), TestNode.with("child3*0")));
    }

    @Test
    public void testChildrenExpressionTrueMap() {
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child1", TestNode.with("grandChildren1")),
                TestNode.with("child2", TestNode.with("grandChildren2")),
                TestNode.with("child3"));

        TestNode.clear();

        this.acceptMapAndCheck(TestNode.relativeNodeSelector().children().expression(Expression.value(true)),
                parent,
                TestNode.with("parent",
                        TestNode.with("child1*0", TestNode.with("grandChildren1")),
                        TestNode.with("child2*1", TestNode.with("grandChildren2")),
                        TestNode.with("child3*2")));
    }

    // /tr[1]/td[1]
    @Test
    public void testChildrenNamedExpressionNumberChildrenNamedExpressionNumberMap() {
        TestNode.disableUniqueNameChecks();

        final TestNode parent = TestNode.with("tbody",
                TestNode.with("skip"),
                TestNode.with("tr",
                        TestNode.with("td", TestNode.with("div1"))
                ),
                TestNode.with("tr",
                        TestNode.with("td", TestNode.with("div2"))
                ));

        this.acceptMapAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .named(Names.string("tr"))
                        .expression(expressionNumber(1))
                        .children()
                        .named(Names.string("td"))
                        .expression(expressionNumber(1)),
                parent,
                TestNode.with("tbody",
                        TestNode.with("skip"),
                        TestNode.with("tr",
                                TestNode.with("td*0", TestNode.with("div1"))
                        ),
                        TestNode.with("tr",
                                TestNode.with("td", TestNode.with("div2"))
                        )));
    }

    // /tr[1]/[2]
    @Test
    public void testChildrenExpressionNumberChildrenExpressionNumberMap() {
        TestNode.disableUniqueNameChecks();

        final TestNode parent = TestNode.with("tbody",
                TestNode.with("tr",
                        TestNode.with("td1"),
                        TestNode.with("td2")
                ),
                TestNode.with("tr",
                        TestNode.with("td3"),
                        TestNode.with("td4")
                ));

        this.acceptMapAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .named(Names.string("tr"))
                        .expression(expressionNumber(1))
                        .children()
                        .expression(expressionNumber(2)),
                parent,
                TestNode.with("tbody",
                        TestNode.with("tr",
                                TestNode.with("td1"),
                                TestNode.with("td2*0")
                        ),
                        TestNode.with("tr",
                                TestNode.with("td3"),
                                TestNode.with("td4")
                        )));
    }

    // /tr[1]/*[2]
    @Test
    public void testChildrenNamedExpressionNumberChildrenNamedExpressionNumberMap2() {
        TestNode.disableUniqueNameChecks();

        final TestNode parent = TestNode.with("tbody",
                TestNode.with("tr",
                        TestNode.with("td", TestNode.with("div1")),
                        TestNode.with("td", TestNode.with("div2"))
                ),
                TestNode.with("tr",
                        TestNode.with("td", TestNode.with("div3")),
                        TestNode.with("td", TestNode.with("div4"))
                ));

        this.acceptMapAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .named(Names.string("tr"))
                        .expression(expressionNumber(2))
                        .children()
                        .expression(expressionNumber(2)),
                parent,
                TestNode.with("tbody",
                        TestNode.with("tr",
                                TestNode.with("td", TestNode.with("div1")),
                                TestNode.with("td", TestNode.with("div2"))
                        ),
                        TestNode.with("tr",
                                TestNode.with("td", TestNode.with("div3")),
                                TestNode.with("td*0", TestNode.with("div4"))
                        )));
    }

    // NodeSelectorVisitor............................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<NodeSelector<TestNode, StringName, StringName, Object>> visited = Lists.array();

        final ExpressionNodeSelector<TestNode, StringName, StringName, Object> selector = this.createSelector();
        final Expression expression = selector.expression;

        final NodeSelector<TestNode, StringName, StringName, Object> next = selector.next;

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
            protected Visiting startVisitExpression(final NodeSelector<TestNode, StringName, StringName, Object> s,
                                                    final Expression e) {
                assertSame(selector, s, "selector");
                assertSame(expression, e, "predicate");
                b.append("3");
                visited.add(s);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitExpression(final NodeSelector<TestNode, StringName, StringName, Object> s,
                                              final Expression e) {
                assertSame(selector, s, "selector");
                assertSame(expression, e, "expression");
                b.append("4");
                visited.add(s);
            }

            @Override
            protected void visitTerminal(final NodeSelector<TestNode, StringName, StringName, Object> s) {
                assertSame(next, s);
                b.append("5");
                visited.add(s);
            }
        }.accept(selector);

        this.checkEquals("1315242", b.toString());

        this.checkEquals(Lists.of(selector, selector,
                        next, next, next,
                        selector, selector),
                visited,
                "visited");
    }

    // Object.......................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "*[" + expression() + "]");
    }

    @Test
    public void testToStringChildrenExpressionTrue() {
        this.toStringAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .expression(Expression.value(true)),
                "child::*[true()]");
    }

    @Test
    public void testToStringChildrenExpressionFalse() {
        this.toStringAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .expression(Expression.value(false)),
                "child::*[false()]");
    }

    @Test
    public void testToStringChildrenNamedExpression() {
        this.toStringAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .named(Names.string("ABC"))
                        .expression(Expression.value(true)),
                "child::ABC[true()]");
    }

    @Test
    public void testToStringExpressionChildren() {
        this.toStringAndCheck(TestNode.relativeNodeSelector()
                        .expression(expressionNumber(123))
                        .firstChild(),
                "*[123]/first-child::*");
    }

    private Expression expressionNumber(final double value) {
        return Expression.value(
                ExpressionNumberKind.DEFAULT.create(value)
        );
    }

    @Override
    ExpressionNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return ExpressionNodeSelector.with(expression());
    }

    private Expression expression() {
        return Expression.equalsExpression(
                Expression.function(FunctionExpressionName.with("name"), Lists.of(Expression.function(FunctionExpressionName.with("node"), Expression.NO_CHILDREN))),
                Expression.value("self")
        );
    }

    @Override
    public Class<ExpressionNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(ExpressionNodeSelector.class);
    }
}
