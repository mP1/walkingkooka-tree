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

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionReference;

import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link Function} or lambda with a nice {@link #toString()}.
 */
final class BasicNodeSelectorExpressionEvaluationContextReferenceFunction<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> implements Function<ExpressionReference, Optional<Optional<Object>>> {

    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    BasicNodeSelectorExpressionEvaluationContextReferenceFunction<N, NAME, ANAME, AVALUE> with(final BasicNodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> context) {
        return new BasicNodeSelectorExpressionEvaluationContextReferenceFunction<>(context);
    }

    private BasicNodeSelectorExpressionEvaluationContextReferenceFunction(final BasicNodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> context) {
        super();
        this.context = context;
    }

    @Override
    public Optional<Optional<Object>> apply(final ExpressionReference reference) {
        return context.reference(reference);
    }

    private final BasicNodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> context;

    @Override
    public String toString() {
        return "";
    }
}
