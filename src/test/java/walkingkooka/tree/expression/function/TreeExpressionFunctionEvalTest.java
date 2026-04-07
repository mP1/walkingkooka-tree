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
import walkingkooka.convert.Converters;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

import java.math.MathContext;

public final class TreeExpressionFunctionEvalTest extends TreeExpressionFunctionTestCase<TreeExpressionFunctionEval<FakeExpressionEvaluationContext>,
    FakeExpressionEvaluationContext,
    Object> {

    private final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.BIG_DECIMAL;

    @Test
    public void testApply() {
        this.applyAndCheck2(
            Lists.of(
                Expression.add(
                    Expression.value(
                        EXPRESSION_NUMBER_KIND.create(11)
                    ),
                    Expression.value(
                        EXPRESSION_NUMBER_KIND.create(22)
                    )
                )
            ),
            EXPRESSION_NUMBER_KIND.create(
                11 + 22
            )
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
                return Converters.simple()
                    .convert(
                        value,
                        target,
                        this
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
    public TreeExpressionFunctionEval<FakeExpressionEvaluationContext> createBiFunction() {
        return TreeExpressionFunctionEval.instance();
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "eval"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeExpressionFunctionEval<FakeExpressionEvaluationContext>> type() {
        return Cast.to(TreeExpressionFunctionEval.class);
    }
}
