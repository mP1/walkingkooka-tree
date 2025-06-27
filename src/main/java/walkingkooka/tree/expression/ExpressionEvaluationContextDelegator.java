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
import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContextDelegator;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.Locale;
import java.util.function.Function;

/**
 * Delegates all {@link ExpressionEvaluationContext} except for {@link #reference(ExpressionReference)} and
 * {@link #enterScope(Function)}.
 */
public interface ExpressionEvaluationContextDelegator extends ExpressionEvaluationContext,
    ConverterContextDelegator,
    LocaleContextDelegator {

    @Override
    default ConverterContext converterContext() {
        return this.expressionEvaluationContext();
    }

    // LocaleContext....................................................................................................

    @Override
    default LocaleContext localeContext() {
        return this.expressionEvaluationContext();
    }

    @Override
    default Locale locale() {
        return this.localeContext()
            .locale();
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
    default <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                   final Object value) {
        return this.expressionEvaluationContext()
            .prepareParameter(
                parameter,
                value
            );
    }

    @Override
    default Object handleException(final RuntimeException exception) {
        return this.expressionEvaluationContext()
            .handleException(exception);
    }

    @Override
    default boolean isText(final Object value) {
        return this.expressionEvaluationContext()
            .isText(value);
    }

    @Override
    default CaseSensitivity stringEqualsCaseSensitivity() {
        return this.expressionEvaluationContext()
            .stringEqualsCaseSensitivity();
    }

    ExpressionEvaluationContext expressionEvaluationContext();
}
