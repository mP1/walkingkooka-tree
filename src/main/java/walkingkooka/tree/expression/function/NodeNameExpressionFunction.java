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
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * A non standard function that returns {@link Node#name()}. It assumes the {@link Node} is the first parameter.
 */
final class NodeNameExpressionFunction<C extends ExpressionFunctionContext> implements ExpressionFunction<String, C> {

    /**
     * Instance getter.
     */
    static <C extends ExpressionFunctionContext> NodeNameExpressionFunction<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private static final NodeNameExpressionFunction INSTANCE = new NodeNameExpressionFunction();

    /**
     * Private ctor
     */
    private NodeNameExpressionFunction() {
        super();
    }

    @Override
    public String apply(final List<Object> parameters,
                        final C context) {
        this.checkParameterCount(parameters);

        return NODE.getOrFail(parameters, 0)
                .name()
                .value();
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    private final static FunctionExpressionName NAME = FunctionExpressionName.with("name");

    @Override
    public List<ExpressionFunctionParameter<?>> parameters() {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<Node> NODE = ExpressionFunctionParameterName.with("node")
            .required(Node.class);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(NODE);

    @Override
    public Class<String> returnType() {
        return String.class;
    }

    @Override
    public Set<ExpressionFunctionKind> kinds() {
        return KINDS;
    }

    private final Set<ExpressionFunctionKind> KINDS = EnumSet.of(
            ExpressionFunctionKind.REQUIRES_EVALUATED_PARAMETERS,
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
