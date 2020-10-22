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

public final class ExpressionNumberReducerBinaryExpressionNumberVisitorMultiplyTest extends ExpressionNumberReducerBinaryExpressionNumberVisitorTestCase2<ExpressionNumberReducerBinaryExpressionNumberVisitorMultiply> {

    @Override
    public ExpressionNumberReducerBinaryExpressionNumberVisitorMultiply createVisitor() {
        return new ExpressionNumberReducerBinaryExpressionNumberVisitorMultiply(CONTEXT);
    }

    @Override
    int left() {
        return 12;
    }

    @Override
    int right() {
        return 34;
    }

    @Override
    int expected() {
        return 12 * 34;
    }

    @Override
    Number function(final Number left, final Number right, final ExpressionNumberReducerContext context) {
        return ExpressionNumberReducerBinaryExpressionNumberVisitorMultiply.compute(left, right, context);
    }

    @Override
    String expectedToString() {
        return "multiply";
    }

    @Override
    public Class<ExpressionNumberReducerBinaryExpressionNumberVisitorMultiply> type() {
        return ExpressionNumberReducerBinaryExpressionNumberVisitorMultiply.class;
    }
}
