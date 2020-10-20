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

import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionException;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.math.MathContext;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
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
            AVALUE> BasicNodeSelectorContext<N, NAME, ANAME, AVALUE> with(final BooleanSupplier finisher,
                                                                          final Predicate<N> filter,
                                                                          final Function<N, N> mapper,
                                                                          final Function<FunctionExpressionName, Optional<ExpressionFunction<?>>> functions,
                                                                          final Converter converter,
                                                                          final ConverterContext converterContext,
                                                                          final Class<N> nodeType) {
        Objects.requireNonNull(finisher, "finisher");
        Objects.requireNonNull(filter, "filter");
        Objects.requireNonNull(mapper, "mapper");
        Objects.requireNonNull(functions, "functions");
        Objects.requireNonNull(converter, "converter");
        Objects.requireNonNull(converterContext, "converterContext");
        Objects.requireNonNull(nodeType, "nodeType");

        return new BasicNodeSelectorContext<>(finisher,
                filter,
                mapper,
                functions,
                converter,
                converterContext);
    }

    private BasicNodeSelectorContext(final BooleanSupplier finisher,
                                     final Predicate<N> filter,
                                     final Function<N, N> mapper,
                                     final Function<FunctionExpressionName, Optional<ExpressionFunction<?>>> functions,
                                     final Converter converter,
                                     final ConverterContext converterContext) {
        this.finisher = finisher;
        this.filter = filter;
        this.mapper = mapper;
        this.functions = functions;
        this.converter = converter;
        this.converterContext = converterContext;
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
    public <T> Either<T, String> convert(final Object value, final Class<T> target) {
        return this.converter.convert(value, target, this.converterContext);
    }

    private final Converter converter;

    private final ConverterContext converterContext;

    @Override
    public Object function(final FunctionExpressionName name, final List<Object> parameters) {
        return NODE.equals(name) ?
                this.node() :
                this.function0(name, parameters);
    }

    private final static FunctionExpressionName NODE = FunctionExpressionName.with("node");

    public Locale locale() {
        return this.converterContext.locale();
    }

    private N node() {
        final N current = this.current;
        if (null == current) {
            throw new NodeSelectorException("Current node not set");
        }
        return current;
    }

    /**
     * Handles dispatching all functions other than <code>node()</code> which is special cased
     * because it needs to read {@link #current}.
     */
    private Object function0(final FunctionExpressionName name, final List<Object> parameters) {
        final Optional<ExpressionFunction<?>> function = this.functions.apply(name);
        if (!function.isPresent()) {
            throw new ExpressionException("Unknown function " + name);
        }

        return function.get().apply(Lists.readOnly(parameters), this);
    }

    private final Function<FunctionExpressionName, Optional<ExpressionFunction<?>>> functions;

    @Override
    public MathContext mathContext() {
        return this.converterContext.mathContext();
    }

    /**
     * The current {@link Node} which is also becomes the first argument for all function invocations.
     */
    private N current;

    @Override
    public String toString() {
        return this.finisher + " " +
                this.filter + " " +
                this.mapper + " " +
                this.functions + " " +
                this.converter + " " +
                this.converterContext;
    }
}
