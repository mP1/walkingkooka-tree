/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunctionContext;

import java.math.MathContext;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Wraps and delegates all methods to a {@link ExpressionFunctionContext} along with a {@link Node} getter.
 */
final class BasicNodeSelectorExpressionFunctionContext<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> implements NodeSelectorExpressionFunctionContext<N, NAME, ANAME, AVALUE> {

    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    BasicNodeSelectorExpressionFunctionContext<N, NAME, ANAME, AVALUE> with(final N node,
                                                                            final ExpressionFunctionContext context) {
        Objects.requireNonNull(node, "node");
        Objects.requireNonNull(context, "context");

        return new BasicNodeSelectorExpressionFunctionContext<>(node, context);
    }

    private BasicNodeSelectorExpressionFunctionContext(final N node,
                                                       final ExpressionFunctionContext context) {
        super();
        this.node = node;
        this.context = context;
    }

    @Override
    public N node() {
        return this.node;
    }

    private final N node;

    @Override
    public Object evaluate(final FunctionExpressionName name,
                           final List<Object> parameters) {
        return this.context.evaluate(name, parameters);
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> target) {
        return this.context.canConvert(value, target);
    }

    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> target) {
        return this.context.convert(value, target);
    }

    @Override
    public Locale locale() {
        return this.context.locale();
    }

    @Override
    public MathContext mathContext() {
        return this.context.mathContext();
    }

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        return this.context.expressionNumberKind();
    }

    private final ExpressionFunctionContext context;

    @Override
    public String toString() {
        return this.node + " " + this.context;
    }
}
