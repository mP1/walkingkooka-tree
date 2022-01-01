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

import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Name;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberFunction;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.select.NodeSelectorExpressionFunctionContext;

import java.util.function.Consumer;

/**
 * Collection of static factory methods for numerous {@link ExpressionFunction}.
 */
public final class ExpressionFunctions implements PublicStaticHelper {

    /**
     * Visit all {@link ExpressionFunction functions}. Note this does not include the {@link #fake() fake function}
     */
    public static void visit(final Consumer<ExpressionFunction<?, ?>> consumer) {
        Lists.of(
                node(),
                nodeName(),
                typeName()
        ).forEach(consumer);
    }

    /**
     * {@see ExpressionNumberFunctionExpressionFunction}
     */
    public static <C extends ExpressionFunctionContext> ExpressionFunction<ExpressionNumber, C> expressionNumberFunction(final FunctionExpressionName name,
                                                                                                                         final ExpressionNumberFunction function) {
        return ExpressionNumberFunctionExpressionFunction.with(name, function);
    }

    /**
     * {@see FakeExpressionFunction}
     */
    public static <T, C extends ExpressionFunctionContext> ExpressionFunction<T, C> fake() {
        return new FakeExpressionFunction<>();
    }

    /**
     * {@see NodeExpressionFunction}
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE,
            C extends NodeSelectorExpressionFunctionContext<N, NAME, ANAME, AVALUE>> ExpressionFunction<N, C> node() {
        return NodeExpressionFunction.instance();
    }

    /**
     * {@see NodeNameExpressionFunction}
     */
    public static <C extends ExpressionFunctionContext> ExpressionFunction<String, C> nodeName() {
        return NodeNameExpressionFunction.instance();
    }

    /**
     * {@see TypeNameExpressionFunction}
     */
    public static <C extends ExpressionFunctionContext> ExpressionFunction<String, C> typeName() {
        return TypeNameExpressionFunction.instance();
    }

    /**
     * Stops creation
     */
    private ExpressionFunctions() {
        throw new UnsupportedOperationException();
    }
}
