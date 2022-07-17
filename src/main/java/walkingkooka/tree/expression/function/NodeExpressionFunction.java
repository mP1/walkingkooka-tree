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

package walkingkooka.tree.expression.function;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.select.NodeSelectorExpressionEvaluationContext;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Returns the current {@link Node} which is fetched from {@link NodeSelectorExpressionEvaluationContext#node()}
 */
final class NodeExpressionFunction<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE,
        C extends NodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE>> implements ExpressionFunction<N, C> {

    /**
     * Instance getter.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE,
            C extends NodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE>> NodeExpressionFunction<N, NAME, ANAME, AVALUE, C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private static final NodeExpressionFunction INSTANCE = new NodeExpressionFunction<>();

    /**
     * Private ctor
     */
    private NodeExpressionFunction() {
        super();
    }

    @Override
    public N apply(final List<Object> parameters,
                   final C context) {
        return context.node();
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    private final static FunctionExpressionName NAME = FunctionExpressionName.with("node");

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.empty();

    @Override
    public Class<N> returnType() {
        return Cast.to(Node.class);
    }

    @Override
    public Set<ExpressionFunctionKind> kinds() {
        return KINDS;
    }

    private final Set<ExpressionFunctionKind> KINDS = EnumSet.of(
            ExpressionFunctionKind.EVALUATE_PARAMETERS,
            ExpressionFunctionKind.RESOLVE_REFERENCES
    );

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return true;
    }

    @Override
    public String toString() {
        return this.name().toString();
    }
}
