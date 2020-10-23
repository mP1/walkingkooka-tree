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

public final class ExpressionNumberReducerExpressionNumberVisitorNegateTest extends ExpressionNumberReducerExpressionNumberVisitorArithmeticTestCase<ExpressionNumberReducerExpressionNumberVisitorNegate> {

    @Override
    public ExpressionNumberReducerExpressionNumberVisitorNegate createVisitor() {
        return new ExpressionNumberReducerExpressionNumberVisitorNegate(CONTEXT);
    }

    @Override
    int value() {
        return -123;
    }

    @Override
    int expected() {
        return 123;
    }

    @Override
    Number function(final Number value, final ExpressionNumberReducerContext context) {
        return ExpressionNumberReducerExpressionNumberVisitorNegate.compute(value, context);
    }

    @Override
    String expectedToString() {
        return "negate";
    }

    @Override
    public Class<ExpressionNumberReducerExpressionNumberVisitorNegate> type() {
        return ExpressionNumberReducerExpressionNumberVisitorNegate.class;
    }
}
