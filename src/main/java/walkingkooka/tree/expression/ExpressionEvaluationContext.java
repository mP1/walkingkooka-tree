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

import walkingkooka.Cast;
import walkingkooka.Context;
import walkingkooka.collect.list.Lists;
import walkingkooka.locale.LocaleContext;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.convert.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctions;
import walkingkooka.tree.expression.function.HasExpressionFunction;
import walkingkooka.tree.expression.function.InvalidExpressionFunctionParameterCountException;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A {@link Context} that travels during any expression evaluation.
 */
public interface ExpressionEvaluationContext extends ExpressionNumberConverterContext,
    ExpressionNumberContext,
    ExpressionPurityContext,
    HasExpressionFunction,
    LocaleContext {

    /**
     * Factory that returns a {@link ExpressionEvaluationContext} of the same type with the given scoped variables.
     */
    ExpressionEvaluationContext enterScope(final Function<ExpressionReference, Optional<Optional<Object>>> scoped);

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
                result = this.evaluateExpression((Expression) result);
            }
        } while (result instanceof ExpressionReference || result instanceof Expression);

        return result;
    }

    /**
     * Evaluate the given {@link Expression} returning the result/value.
     */
    default Object evaluateExpression(final Expression expression) {
        Objects.requireNonNull(expression, "expression");

        Object result;

        try {
            result = expression.toValue(this);
        } catch (final UnsupportedOperationException rethrow) {
            throw rethrow;
        } catch (final RuntimeException exception) {
            result = this.handleException(exception);
        }

        return result;
    }

    /**
     * Creates a lambda {@link ExpressionFunction}, the given parameters become scoped variables when the
     * {@link Expression} is executed.
     */
    default <TT, CC extends ExpressionEvaluationContext> ExpressionFunction<TT, CC> lambdaFunction(final List<ExpressionFunctionParameter<?>> parameters,
                                                                                                   final Class<TT> returnType,
                                                                                                   final Expression expression) {
        return ExpressionFunctions.lambda(
            parameters,
            returnType,
            expression
        );
    }

    /**
     * Wraps the {@link List} of parameters values and performs several actions lazily for each parameter.
     * <ul>
     * <li>Resolve {@link Expression} if {@link ExpressionFunctionParameterKind#EVALUATE}</li>
     * <li>Resolve {@link ReferenceExpression} if {@link ExpressionFunctionParameterKind#RESOLVE_REFERENCES}</li>
     * <li>Convert values to the {@link ExpressionFunctionParameter#type()}</li>
     * </ul>
     * The above list is only performed once for each parameter and cached for future fetches.
     */
    default List<Object> prepareParameters(final ExpressionFunction<?, ? extends ExpressionEvaluationContext> function,
                                           final List<Object> parameters) {
        return ExpressionEvaluationContextPrepareParametersList.with(
            function.parameters(parameters.size()),
            parameters,
            function.name(),
            this
        );
    }

    /**
     * This method is called with each parameter and value pair, prior to invoking the namedFunction.
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
     * Executes the given {@link ExpressionFunction} with the parameters, and also handles any exception.
     */
    default Object evaluateFunction(final ExpressionFunction<?, ? extends ExpressionEvaluationContext> function,
                                    final List<Object> parameters) {
        Objects.requireNonNull(function, "function");
        Objects.requireNonNull(parameters, "parameters");

        Object result;

        try {
            result = function.apply(
                this.prepareParameters(function, parameters),
                Cast.to(this)
            );
        } catch (final InvalidExpressionFunctionParameterCountException cause) {
            // Function may be wrapped - want to use the outer ExpressionFunction.name
            result = this.handleException(
                cause.setFunctionName(
                    function.name()
                )
            );
        } catch (final UnsupportedOperationException rethrow) {
            throw rethrow;
        } catch (final RuntimeException exception) {
            result = this.handleException(exception);
        }

        return result;
    }

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
     *     <li>{@link #evaluateFunction(ExpressionFunction, List)} throws</li>
     *     <li>{@link #prepareParameter(ExpressionFunctionParameter, Object)} throws</li>
     * </ul>
     */
    Object handleException(final RuntimeException exception);

    /**
     * A constant holding the value for a reference that was not found.
     */
    Optional<Optional<Object>> REFERENCE_NOT_FOUND_VALUE = Optional.empty();

    /**
     * A constant holding the value for a reference that has a null value.
     */
    Optional<Optional<Object>> REFERENCE_NULL_VALUE = Optional.of(
        Optional.empty()
    );

    /**
     * Locates the value or a {@link Expression} for the given {@link ExpressionReference}.
     * <pre>
     *  var missingReference = Optional.empty();
     *
     *  var referencePresentButNullValue = Optional.of(Optional.empty());
     *
     *  var referenceWithValue = Optional.of(Optional.of(123));
     * </pre>
     * The double {@link Optional} is required so it is possible to determine whether a reference is missing or has a null value.
     */
    Optional<Optional<Object>> reference(final ExpressionReference reference);

    /**
     * Locates the value for the given {@link ExpressionReference} or throws a
     * {@link ExpressionEvaluationReferenceException}.
     */
    default Object referenceOrFail(final ExpressionReference reference) {
        Object result;

        try {
            final Supplier<ExpressionEvaluationException> thrower = () -> this.referenceNotFound(reference);
            result = this.reference(reference)
                .orElseThrow(thrower)
                .orElseThrow(thrower);
        } catch (final UnsupportedOperationException rethrow) {
            throw rethrow;
        } catch (final RuntimeException exception) {
            result = this.handleException(exception);
        }

        return result;
    }

    /**
     * Returns a {@link ExpressionEvaluationException} that captures the given {@link ExpressionReference} was not found.
     */
    default ExpressionEvaluationException referenceNotFound(final ExpressionReference reference) {
        return ExpressionEvaluationContexts.referenceNotFound()
            .apply(reference);
    }

    /**
     * Tests if the given value should be considered text. This might be useful to prepare a value for {@link String
     * comparison.
     */
    boolean isText(final Object value);

    /**
     * Controls whether equals or not equals tests are case-sensitive for {@link String strings}
     */
    CaseSensitivity stringEqualsCaseSensitivity();

    // LocaleContext....................................................................................................

    @Override
    ExpressionEvaluationContext setLocale(final Locale locale);
}
