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

package walkingkooka.tree.expression;

import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContextDelegator;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

public interface ExpressionEvaluationContextDelegator extends ExpressionEvaluationContext,
        ConverterContextDelegator {

    @Override
    default ConverterContext converterContext() {
        return this.expressionEvaluationContext();
    }

    @Override
    default Locale locale() {
        return this.expressionEvaluationContext().locale();
    }

    // ExpressionEvaluationContext......................................................................................

    @Override
    default boolean isPure(final ExpressionFunctionName name) {
        return this.expressionEvaluationContext().isPure(name);
    }

    @Override
    default ExpressionNumberKind expressionNumberKind() {
        return this.expressionEvaluationContext()
                .expressionNumberKind();
    }

    @Override
    default ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name) {
        return this.expressionEvaluationContext()
                .expressionFunction(name);
    }

    @Override
    default ExpressionEvaluationContext context(final Function<ExpressionReference, Optional<Optional<Object>>> scoped) {
        return this.expressionEvaluationContext()
                .context(scoped);
    }

    @Override
    default Object evaluate(final Expression expression) {
        return this.expressionEvaluationContext()
                .evaluate(expression);
    }

    @Override
    default <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                   final Object value) {
        return this.expressionEvaluationContext()
                .prepareParameter(
                        parameter,
                        value
                );
    }

    @Override
    default Object evaluateFunction(final ExpressionFunction<?, ? extends ExpressionEvaluationContext> function,
                                    final List<Object> parameters) {
        return this.expressionEvaluationContext()
                .evaluateFunction(
                        function,
                        parameters
                );
    }

    @Override
    default Object handleException(final RuntimeException exception) {
        return this.expressionEvaluationContext()
                .handleException(exception);
    }

    @Override
    default Optional<Optional<Object>> reference(final ExpressionReference reference) {
        return this.expressionEvaluationContext()
                .reference(reference);
    }

    @Override
    default boolean isText(final Object value) {
        return this.expressionEvaluationContext()
                .isText(value);
    }

    @Override
    default CaseSensitivity caseSensitivity() {
        return this.expressionEvaluationContext()
                .caseSensitivity();
    }

    ExpressionEvaluationContext expressionEvaluationContext();
}
