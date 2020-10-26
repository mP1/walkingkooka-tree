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

import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.reflect.PublicStaticHelper;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class ExpressionEvaluationContexts implements PublicStaticHelper {

    /**
     * {@see BasicExpressionEvaluationContext}
     */
    public static ExpressionEvaluationContext basic(final ExpressionNumberKind expressionNumberKind,
                                                    final BiFunction<FunctionExpressionName, List<Object>, Object> functions,
                                                    final Function<ExpressionReference, Optional<Expression>> references,
                                                    final Converter converter,
                                                    final ConverterContext context) {
        return BasicExpressionEvaluationContext.with(expressionNumberKind,
                functions,
                references,
                converter,
                context);
    }

    /**
     * {@see CycleDetectingExpressionEvaluationContext}
     */
    public static ExpressionEvaluationContext cycleDetecting(final ExpressionEvaluationContext context) {
        return CycleDetectingExpressionEvaluationContext.with(context);
    }

    /**
     * {@see FakeExpressionEvaluationContext}
     */
    public static ExpressionEvaluationContext fake() {
        return new FakeExpressionEvaluationContext();
    }

    /**
     * Stop creation
     */
    private ExpressionEvaluationContexts() {
        throw new UnsupportedOperationException();
    }
}
