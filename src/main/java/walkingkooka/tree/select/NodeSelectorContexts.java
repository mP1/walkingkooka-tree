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
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A collection of factory methods to create {@link NodeSelectorContext}.
 */
public final class NodeSelectorContexts implements PublicStaticHelper {

    /**
     * {@see BasicNodeSelectorContext}
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE,
        C extends ExpressionEvaluationContext> NodeSelectorContext<N, NAME, ANAME, AVALUE> basic(final BooleanSupplier finisher,
                                                                                                 final Predicate<N> filter,
                                                                                                 final Function<N, N> mapper,
                                                                                                 final Function<NodeSelectorContext<N, NAME, ANAME, AVALUE>, C> expressionEvaluationContext,
                                                                                                 final Class<N> nodeType) {
        return BasicNodeSelectorContext.with(finisher,
            filter,
            mapper,
            expressionEvaluationContext,
            nodeType);
    }

    /**
     * {@see BasicNodeSelectorContextFunction}
     */
    public static Function<ExpressionFunctionName, ExpressionFunction<?, ?>> basicFunctions() {
        return BasicNodeSelectorContextFunction.INSTANCE;
    }

    /**
     * {@see FakeNodeSelectorContext}
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> NodeSelectorContext<N, NAME, ANAME, AVALUE> fake() {
        return new FakeNodeSelectorContext<>();
    }

    /**
     * Stop creation.
     */
    private NodeSelectorContexts() {
        throw new UnsupportedOperationException();
    }
}
