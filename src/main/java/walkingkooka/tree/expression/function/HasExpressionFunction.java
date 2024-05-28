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

package walkingkooka.tree.expression.function;

import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.Optional;

/**
 * Almost a provider that is able to supply {@link ExpressionFunction} given a {@link FunctionExpressionName}.
 * This interface is not intended to be implemented by user-code and only exists to provide {@link #expressionFunctionOrFail(FunctionExpressionName)}.
 * <br>
 * A full featured provider is available at
 * <br>
 * <a href="https://github.com/mP1/walkingkooka-tree-expression-function-provider/blob/master/src/main/java/walkingkooka/tree/expression/function/provider/ExpressionFunctionProvider.java">...</a>}
 */
public interface HasExpressionFunction {

    /**
     * Returns the {@link ExpressionFunction} with the given {@link FunctionExpressionName}.
     */
    Optional<ExpressionFunction<?, ExpressionEvaluationContext>> expressionFunction(final FunctionExpressionName name);

    /**
     * Helper that invokes {@link #expressionFunction(FunctionExpressionName)} and throws a {@link UnknownExpressionFunctionException}
     * if none was found.
     */
    default ExpressionFunction<?, ExpressionEvaluationContext> expressionFunctionOrFail(final FunctionExpressionName name) {
        return this.expressionFunction(name)
                .orElseThrow(() -> new UnknownExpressionFunctionException(name));
    }
}
