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

import walkingkooka.collect.set.Sets;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;
import java.util.Set;

/**
 * Customises the preparation of a {@link ExpressionFunctionParameter} value.
 */
public enum ExpressionFunctionParameterKind {

    /**
     * All parameters are converted to their declared type.
     */
    CONVERT,

    /**
     * Indicates that any parameters in {@link walkingkooka.tree.expression.Expression} form be evaluated into java
     * objects prior to invoking {@link ExpressionFunction#apply(Object, Object)}. For the vast majority of
     * cases all functions will return true, but for cases such as Excels isError it may be desired to catch any thrown
     * exceptions and substitute an error object of some kind.
     */
    EVALUATE,

    /**
     * Flatten expands any lists.
     */
    FLATTEN,

    /**
     * Indicates that parameters that implement {@link walkingkooka.tree.expression.ExpressionReference} are resolved to
     * their actual non {@link walkingkooka.tree.expression.Expression} value.
     * This is only honoured when {@link ExpressionFunctionContext#evaluate(FunctionExpressionName, List)} is used.
     */
    RESOLVE_REFERENCES;

    // See ExpressionFunctionParameter#toString
    final String parameterToString() {
        return "@" + this.name();
    }

    public final static Set<ExpressionFunctionParameterKind> CONVERT_EVALUATE = Sets.of(
            CONVERT,
            EVALUATE
    );

    public final static Set<ExpressionFunctionParameterKind> CONVERT_EVALUATE_RESOLVE_REFERENCES = Sets.of(
            CONVERT,
            EVALUATE,
            RESOLVE_REFERENCES
    );

    public final static Set<ExpressionFunctionParameterKind> CONVERT_EVALUATE_FLATTEN_RESOLVE_REFERENCES = Sets.of(
            CONVERT,
            EVALUATE,
            FLATTEN,
            RESOLVE_REFERENCES
    );

    public final static Set<ExpressionFunctionParameterKind> EVALUATE_RESOLVE_REFERENCES = Sets.of(
            EVALUATE,
            RESOLVE_REFERENCES
    );
}
