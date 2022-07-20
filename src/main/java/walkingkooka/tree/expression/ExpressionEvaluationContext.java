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

package walkingkooka.tree.expression;

import walkingkooka.Context;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;

import java.util.List;
import java.util.Optional;

/**
 * A {@link Context} that travels during any expression evaluation.
 */
public interface ExpressionEvaluationContext extends Context,
        ExpressionNumberConverterContext,
        ExpressionNumberContext,
        ExpressionPurityContext {

    /**
     * If the value is a reference or expression resolve or evaluate.
     */
    default Object evaluateIfNecessary(final Object value) {
        Object result = value;

        do {
            if (result instanceof ExpressionReference) {
                result = this.referenceOrFail((ExpressionReference) result);
            }
            if (result instanceof Expression) {
                result = this.evaluate((Expression) result);
            }
        } while (result instanceof ExpressionReference || result instanceof Expression);

        return result;
    }

    /**
     * Evaluate the given {@link Expression} returning the result/value.
     */
    Object evaluate(final Expression expression);

    /**
     * Returns the {@link ExpressionFunction} with the given {@link FunctionExpressionName}.
     */
    ExpressionFunction<?, ExpressionEvaluationContext> function(final FunctionExpressionName name);

    /**
     * Wraps the {@link List} of parameters values and performs several actions lazily for each parameter.
     * <ul>
     * <li>Resolve {@link Expression} if {@link ExpressionFunctionParameterKind#EVALUATE}</li>
     * <li>Resolve {@link ReferenceExpression} if {@link ExpressionFunctionParameterKind#RESOLVE_REFERENCES}</li>
     * <li>Convert values to the {@link ExpressionFunctionParameter#type()}</li>
     * </ul>
     * The above list is only performed once for each parameter and cached for future fetches.
     */
    default List<Object> prepareParameters(final ExpressionFunction<?, ExpressionEvaluationContext> function,
                                           final List<Object> parameters) {
        return ExpressionEvaluationContextPrepareParametersList.with(
                function.parameters(parameters.size()),
                parameters,
                this
        );
    }

    /**
     * This method is called with each parameter and value pair, prior to invoking the function.
     * <br>
     * This provides an opportunity to convert the value to the required parameter type if the language requires such
     * semantics.
     */
    <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                           final Object value);

    /**
     * Constant for functions without any parameters.
     */
    List<Object> NO_PARAMETERS = Lists.empty();

    /**
     * Locates a function with the given name and then executes it with the provided parameter values.
     */
    Object evaluate(final FunctionExpressionName name, final List<Object> parameters);

    /**
     * Receives all {@link RuntimeException} thrown by a {@link ExpressionFunction} or {@link Expression}.
     * <br>
     * This method exists a spreadsheet can handle expressions like 1/0 which throw an {@link ArithmeticException}
     * which needs to be converted into an error object rather than propagating up the call chain until caught.
     * <br
     * Most implementations will simply rethrow.
     * <br>
     * This should be called whenever an {@link RuntimeException} is thrown by
     * <ul>
     *     <li>{@link #evaluate(FunctionExpressionName, List)} throws</li>
     *     <li>{@link #prepareParameter(ExpressionFunctionParameter, Object)} throws</li>
     * </ul>
     */
    Object handleException(final RuntimeException exception);

    /**
     * Locates the value or a {@link Expression} for the given {@link ExpressionReference}
     */
    Optional<Object> reference(final ExpressionReference reference);

    /**
     * Locates the value for the given {@link ExpressionReference} or throws a
     * {@link ExpressionEvaluationReferenceException}.
     */
    default Object referenceOrFail(final ExpressionReference reference) {
        Object result;
        try {
            result = this.reference(reference)
                    .orElseThrow(() -> this.referenceNotFound(reference));
        } catch (final RuntimeException exception) {
            result = this.handleException(exception);
        }

        return result;
    }

    /**
     * Returns a {@link ExpressionEvaluationException} that captures the given {@link ExpressionReference} was not found.
     */
    default ExpressionEvaluationException referenceNotFound(final ExpressionReference reference) {
        return ExpressionEvaluationContexts.referenceNotFound().apply(reference);
    }

    /**
     * Tests if the given value should be considered text. This might be useful to prepare a value for {@link String
     * comparison.
     */
    boolean isText(final Object value);

    /**
     * Controls whether equals or not equals tests are case sensitive for {@link String strings}
     */
    CaseSensitivity caseSensitivity();
}
