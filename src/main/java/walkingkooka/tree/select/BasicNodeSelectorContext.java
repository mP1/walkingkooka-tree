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
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A {@link NodeSelectorContext} that routes test and selected {@link Node} to a individual {@link Consumer}
 */
final class BasicNodeSelectorContext<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        implements NodeSelectorContext<N, NAME, ANAME, AVALUE> {

    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE,
            C extends ExpressionNumberConverterContext> BasicNodeSelectorContext<N, NAME, ANAME, AVALUE> with(final BooleanSupplier finisher,
                                                                                                              final Predicate<N> filter,
                                                                                                              final Function<N, N> mapper,
                                                                                                              final Function<NodeSelectorContext<N, NAME, ANAME, AVALUE>, ExpressionEvaluationContext> expressionEvaluationContext,
                                                                                                              final Class<N> nodeType) {
        Objects.requireNonNull(finisher, "finisher");
        Objects.requireNonNull(filter, "filter");
        Objects.requireNonNull(mapper, "mapper");
        Objects.requireNonNull(expressionEvaluationContext, "expressionEvaluationContext");
        Objects.requireNonNull(nodeType, "nodeType");

        return new BasicNodeSelectorContext<N, NAME, ANAME, AVALUE>(finisher,
                filter,
                mapper,
                expressionEvaluationContext);
    }

    private BasicNodeSelectorContext(final BooleanSupplier finisher,
                                     final Predicate<N> filter,
                                     final Function<N, N> mapper,
                                     final Function<NodeSelectorContext<N, NAME, ANAME, AVALUE>, ExpressionEvaluationContext> expressionEvaluationContext) {
        this.finisher = finisher;
        this.filter = filter;
        this.mapper = mapper;

        this.expressionEvaluationContext = expressionEvaluationContext;
    }

    @Override
    public boolean isFinished() {
        return this.finisher.getAsBoolean();
    }

    /**
     * Allows outsider termination of node selection.
     */
    private final BooleanSupplier finisher;

    @Override
    public boolean test(final N node) {
        return this.filter.test(node);
    }

    /**
     * The {@link Consumer} receives all {@link Node} that are visited, this provides an opportunity to throw
     * an {@link RuntimeException} to abort a long running select.
     */
    private final Predicate<N> filter;

    /**
     * Returns the current {@link Node}
     */
    @Override
    public N node() {
        final N current = this.current;
        if (null == current) {
            throw new NodeSelectorException("Current node not set"); // runtime error
        }
        return current;
    }

    /**
     * Saves the current node for that predicates and expressions that may need it.
     */
    @Override
    public void setNode(final N node) {
        this.current = node;
    }

    @Override
    public N selected(final N node) {
        return this.mapper.apply(node);
    }

    private final Function<N, N> mapper;

    @Override
    public Object evaluate(final Expression expression) {
        // create a new context and then evaluate the expression.
        return expression.toValue(this.expressionEvaluationContext.apply(this));
    }

    /**
     * A factory that returns a {@link ExpressionEvaluationContext} after being given a {@link NodeSelectorContext}.
     */
    private final Function<NodeSelectorContext<N, NAME, ANAME, AVALUE>, ExpressionEvaluationContext> expressionEvaluationContext;

    /**
     * The current {@link Node} which is also becomes the first argument for all function invocations.
     */
    private N current;

    @Override
    public String toString() {
        return this.finisher + " " +
                this.filter + " " +
                this.mapper + " " +
                this.expressionEvaluationContext;
    }
}
