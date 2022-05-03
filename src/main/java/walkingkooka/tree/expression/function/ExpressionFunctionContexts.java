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

import walkingkooka.convert.ConverterContext;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionEvaluationException;
import walkingkooka.tree.expression.ExpressionEvaluationReferenceException;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.Optional;
import java.util.function.Function;

public final class ExpressionFunctionContexts implements PublicStaticHelper {

    /**
     * {@see BasicExpressionFunctionContext}
     */
    public static ExpressionFunctionContext basic(final ExpressionNumberKind expressionNumberKind,
                                                  final Function<FunctionExpressionName, ExpressionFunction<?, ExpressionFunctionContext>> functions,
                                                  final Function<RuntimeException, Object> exceptionHandler,
                                                  final Function<ExpressionReference, Optional<Object>> references,
                                                  final Function<ExpressionReference, ExpressionEvaluationException> referenceNotFound,
                                                  final CaseSensitivity caseSensitivity,
                                                  final ConverterContext converterContext) {
        return BasicExpressionFunctionContext.with(
                expressionNumberKind,
                functions,
                exceptionHandler,
                references,
                referenceNotFound,
                caseSensitivity,
                converterContext
        );
    }

    /**
     * {@see FakeExpressionFunctionContext}
     */
    public static ExpressionFunctionContext fake() {
        return new FakeExpressionFunctionContext();
    }

    /**
     * A function that creates a {@link ExpressionEvaluationReferenceException}.
     */
    public static Function<ExpressionReference, ExpressionEvaluationException> referenceNotFound() {
        return (r) -> new ExpressionEvaluationReferenceException(
                "Reference not found: " + r,
                r
        );
    }

    /**
     * Stop creation
     */
    private ExpressionFunctionContexts() {
        throw new UnsupportedOperationException();
    }
}
