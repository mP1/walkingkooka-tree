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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

import java.math.MathContext;

public final class ExpressionFunctionEvalTest extends ExpressionFunctionTestCase<ExpressionFunctionEval<FakeExpressionEvaluationContext>,
    FakeExpressionEvaluationContext,
    Object> {

    private final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.BIG_DECIMAL;

    // 1 + 20
    private final static Expression EXPRESSION = Expression.add(
        Expression.value(1),
        Expression.value(20)
    );

    @Test
    public void testApply() {
        this.applyAndCheck(
            Lists.empty(), // no parameters
            EXPRESSION_NUMBER_KIND.create(1 + 20)
        );
    }

    @Override
    public FakeExpressionEvaluationContext createContext() {
        return new FakeExpressionEvaluationContext() {
            @Override
            public boolean isText(final Object value) {
                return value instanceof String;
            }

            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> target) {
                return this.successfulConversion(
                    EXPRESSION_NUMBER_KIND.create((Number) value),
                    target
                );
            }

            @Override
            public MathContext mathContext() {
                return MathContext.DECIMAL32;
            }
        };
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }

    @Override
    public ExpressionFunctionEval<FakeExpressionEvaluationContext> createBiFunction() {
        return ExpressionFunctionEval.with(EXPRESSION);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            ExpressionFunctionEval.with(EXPRESSION),
            "eval"
        );
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionFunctionEval<FakeExpressionEvaluationContext>> type() {
        return Cast.to(ExpressionFunctionEval.class);
    }
}
