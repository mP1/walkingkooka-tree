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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import walkingkooka.Either;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.tree.Node;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.ExpressionNumberConverterContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.math.MathContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract public class NodeSelectorTestCase4<S extends NodeSelector<TestNode, StringName, StringName, Object>> extends NodeSelectorTestCase3<S>
        implements HashCodeEqualsDefinedTesting2<S>,
        ToStringTesting<S> {

    final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.DEFAULT;

    NodeSelectorTestCase4() {
        super();
    }

    @BeforeEach
    public void beforeEachTest() {
        TestNode.clear();
    }

    @Test
    public void testAppendNullFails() {
        assertThrows(NullPointerException.class, () -> this.createSelector().append(null));
    }

    @Test
    public final void testSetToStringNullFails() {
        assertThrows(NullPointerException.class, () -> this.createSelector().setToString(null));
    }

    @Test
    public final void testSetToString() {
        final S selector = this.createSelector();

        final String toString = "custom " + selector;
        final NodeSelector<TestNode, StringName, StringName, Object> custom = selector.setToString(toString);
        assertEquals(toString, custom.toString(), "toString");
    }

    abstract S createSelector();

    final NodeSelector<TestNode, StringName, StringName, Object> wrapped() {
        return NodeSelector.terminal();
    }

    final void applyAndCheck(final TestNode start) {
        this.applyAndCheck0(this.createSelector(), start);
    }

    final void applyAndCheck(final TestNode start, final String... nodes) {
        this.applyAndCheck0(this.createSelector(), start, nodes);
    }

    final void applyAndCheck(final TestNode start, final TestNode... nodes) {
        this.applyAndCheck(this.createSelector(), start, nodes);
    }

    @SafeVarargs final void applyAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                          final TestNode start,
                                          final TestNode... nodes) {
        this.applyAndCheck0(selector,
                start,
                nodeNames(nodes).toArray(new String[0]));
    }

    abstract void applyAndCheck0(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                 final TestNode start,
                                 final String... nodes);

    final void applyAndCheckUsingContext(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                         final TestNode start,
                                         final String... nodes) {
        final Set<TestNode> potential = Sets.ordered();
        final Set<TestNode> selected = Sets.ordered();
        assertSame(start,
                selector.apply(start,
                        this.context(
                                (n) -> {
                                    potential.add(n);
                                    return true;
                                },
                                selected::add)));
        assertEquals(Lists.of(nodes), nodeNames(selected), () -> "Selector.apply\n" + start);
        assertNotEquals(Sets.empty(), potential, "potentials must not be empty");
        assertTrue(potential.contains(start), () -> "potentials must include initial node=" + potential);
    }

    // applyFinisherAndCheck............................................................................................

    final void applyFinisherAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                     final TestNode start,
                                     final int selectCount) {
        this.applyFinisherAndCheck(selector,
                start,
                selectCount,
                new String[0]);
    }

    final void applyFinisherAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                     final TestNode start,
                                     final int selectCount,
                                     final TestNode... nodes) {
        final Set<TestNode> selected = Sets.ordered();
        assertSame(start,
                selector.apply(start,
                        this.context(this.finisher(selectCount, selected),
                                Predicates.always(),
                                selected::add)),
                () -> "incorrect start node returned, selector: " + selector);
        assertEquals(nodeNames(nodes),
                nodeNames(selected),
                () -> "Selector.apply\n" + start);
    }

    final void applyFinisherAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                     final TestNode start,
                                     final int selectCount,
                                     final String... nodes) {
        final Set<TestNode> selected = Sets.ordered();
        assertSame(start,
                selector.apply(start,
                        this.context(this.finisher(selectCount, selected),
                                Predicates.always(),
                                selected::add)),
                () -> "incorrect start node returned, selector: " + selector);
        assertEquals(Lists.of(nodes),
                nodeNames(selected),
                () -> "Selector.apply\n" + start);
    }

    private BooleanSupplier finisher(final int selectCount, final Set<TestNode> selected) {
        return new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                return selected.size() >= selectCount;
            }

            @Override
            public String toString() {
                return selectCount + " >= " + selected.size() + " " + selected + (this.getAsBoolean() ? " FINISHED" : "");
            }
        };
    }

    final void applyFinisherAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                     final TestNode start,
                                     final BooleanSupplier finisher) {
        this.applyFinisherAndCheck(selector,
                start,
                finisher,
                new String[0]);
    }

    final void applyFinisherAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                     final TestNode start,
                                     final BooleanSupplier finisher,
                                     final TestNode... nodes) {
        final Set<TestNode> selected = Sets.ordered();
        assertSame(start,
                selector.apply(start,
                        this.context(finisher,
                                Predicates.always(),
                                selected::add)),
                () -> "incorrect start node returned, selector: " + selector);
        assertEquals(nodeNames(nodes),
                nodeNames(selected),
                () -> "Selector.apply\n" + start);
    }

    final void applyFinisherAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                     final TestNode start,
                                     final BooleanSupplier finisher,
                                     final String... nodes) {
        final Set<TestNode> selected = Sets.ordered();
        assertSame(start,
                selector.apply(start,
                        this.context(finisher,
                                Predicates.always(),
                                selected::add)),
                () -> "incorrect start node returned, selector: " + selector);
        assertEquals(Lists.of(nodes),
                nodeNames(selected),
                () -> "Selector.apply\n" + start);
    }

    // applyFilterAndCheck............................................................................................

    final void applyFilterAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                   final TestNode start,
                                   final Predicate<TestNode> filter) {
        this.applyFilterAndCheck(selector,
                start,
                filter,
                new String[0]);
    }

    final void applyFilterAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                   final TestNode start,
                                   final Predicate<TestNode> filter,
                                   final TestNode... nodes) {
        final Set<TestNode> selected = Sets.ordered();
        assertSame(start,
                selector.apply(start,
                        this.context(filter,
                                selected::add)));
        assertEquals(nodeNames(nodes),
                nodeNames(selected),
                () -> "Selector.apply\n" + start);
    }

    final void applyFilterAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                   final TestNode start,
                                   final Predicate<TestNode> filter,
                                   final String... nodes) {
        final Set<TestNode> selected = Sets.ordered();
        assertSame(start,
                selector.apply(start,
                        this.context(filter,
                                selected::add)));
        assertEquals(Lists.of(nodes),
                nodeNames(selected),
                () -> "Selector.apply\n" + start);
    }

    private List<String> nodeNames(final TestNode... nodes) {
        return nodeNames(Lists.of(nodes));
    }

    private List<String> nodeNames(final Collection<TestNode> nodes) {
        return nodes.stream()
                .map(n -> n.name().value())
                .collect(Collectors.toList());
    }

    final void acceptMapAndCheck(final TestNode start) {
        this.acceptMapAndCheck(start, start);
    }

    final void acceptMapAndCheck(final TestNode start,
                                 final TestNode result) {
        this.acceptMapAndCheck(this.createSelector(), start, result);
    }

    final void acceptMapAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                 final TestNode start) {
        this.acceptMapAndCheck(selector, start, start);
    }

    final void acceptMapAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                 final TestNode start,
                                 final TestNode result) {
        TestNode.clear();

        final String startToString = start.toString();

        assertEquals(result,
                selector.apply(start,
                        this.context0(() -> false,
                                Predicates.always(),
                                new Function<>() {
                                    @Override
                                    public TestNode apply(final TestNode testNode) {
                                        return TestNode.with(testNode.name().value() + "*" + this.i++)
                                                .setChildren(testNode.children());
                                    }

                                    private int i;

                                    @Override
                                    public String toString() {
                                        return "mapper: next: " + i;
                                    }
                                })));

        assertEquals(startToString,
                start.toString(),
                "toString has changed for starting node, indicating it probably was mutated");
    }

    final NodeSelectorContext<TestNode, StringName, StringName, Object> context(final Predicate<TestNode> filter,
                                                                                final Consumer<TestNode> selected) {
        return this.context(() -> false,
                filter,
                selected);
    }

    final NodeSelectorContext<TestNode, StringName, StringName, Object> context(final BooleanSupplier finisher,
                                                                                final Predicate<TestNode> filter,
                                                                                final Consumer<TestNode> selected) {
        return this.context0(finisher,
                filter,
                (n) -> {
                    this.checkSelectCaller();
                    selected.accept(n);
                    return n;
                });
    }

    /**
     * Scan the call stack, expecting the callingNodeSelector ignoring contexts to be {@link TerminalNodeSelector}.
     */
    private void checkSelectCaller() {
        final Class<?> caller = TerminalNodeSelector.class;

        assertEquals(Optional.of(caller.getSimpleName()),
                Arrays.stream(Thread.currentThread().getStackTrace())
                        .map(this::simpleClassName)
                        .filter(c -> c.endsWith(NodeSelector.class.getSimpleName()))
                        .findFirst(),
                () -> "Expected callingNodeSelector to be " + caller.getName());
    }

    private String simpleClassName(final StackTraceElement element) {
        final String className = element.getClassName();
        final int dot = className.lastIndexOf('.');
        return -1 == dot ?
                className :
                className.substring(dot + 1);
    }

    final NodeSelectorContext<TestNode, StringName, StringName, Object> context0(final BooleanSupplier finisher,
                                                                                 final Predicate<TestNode> filter,
                                                                                 final Function<TestNode, TestNode> mapper) {
        final NodeSelectorContext<TestNode, StringName, StringName, Object> context = NodeSelectorContexts.basic(finisher,
                filter,
                mapper,
                //EXPRESSION_NUMBER_KIND,
                this.functions(),
                this.converter(),
                ExpressionNumberConverterContexts.fake(),
                TestNode.class);

        return new NodeSelectorContext<>() {
            @Override
            public boolean isFinished() {
                return context.isFinished();
            }

            private void finisherGuardCheck() {
                assertEquals(false, this.isFinished(), () -> "finisher should be false: " + this);
            }

            @Override
            public boolean test(final TestNode node) {
                this.finisherGuardCheck();
                return context.test(node);
            }

            @Override
            public void setNode(final TestNode node) {
                this.finisherGuardCheck();
                context.setNode(node);
            }

            @Override
            public TestNode selected(final TestNode node) {
                this.finisherGuardCheck();
                return context.selected(node);
            }

            @Override
            public ExpressionNumberKind expressionNumberKind() {
                return context.expressionNumberKind();
            }

            @Override
            public Object function(final FunctionExpressionName name,
                                   final List<Object> parameters) {
                this.finisherGuardCheck();
                return context.function(name, parameters);
            }

            @Override
            public <T> Either<T, String> convert(final Object value, final Class<T> target) {
                this.finisherGuardCheck();
                return context.convert(value, target);
            }

            @Override
            public Object evaluate(final Expression expression) {
                this.finisherGuardCheck();
                return context.evaluate(expression);
            }

            //            @Override
//            public Locale locale() {
//                return context.locale();
//            }
//
//            @Override
//            public MathContext mathContext() {
//                this.finisherGuardCheck();
//                return context.mathContext();
//            }

            @Override
            public String toString() {
                return context.toString();
            }
        };
    }

    final TestNode nodeWithAttributes(final String name, final String attribute, final String value) {
        return TestNode.with(name).setAttributes(this.attributes(attribute, value));
    }

    final Map<StringName, Object> attributes(final String name, final Object value) {
        return Maps.of(Names.string(name), value);
    }

    final Function<FunctionExpressionName, Optional<ExpressionFunction<?>>> functions() {
        return NodeSelectorContexts.basicFunctions();
    }

    final Converter<ExpressionNumberConverterContext> converter() {
        return Converters.collection(Lists.of(
                ExpressionNumber.fromExpressionNumberConverter(Converters.numberNumber()),
                Converters.<String, Integer>function((t) -> t instanceof String, Predicates.is(Integer.class), Integer::parseInt),
                Converters.function(t -> t instanceof Node, Predicates.is(Node.class), Function.identity()),
                Converters.simple()
        ));
    }

    // HashCodeEqualsDefinedTesting.....................................................................................

    @Override
    public final S createObject() {
        return this.createSelector();
    }
}
