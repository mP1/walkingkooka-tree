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

final class ExpressionNumberReducerBinaryExpressionNumberVisitorGreaterThan extends ExpressionNumberReducerBinaryExpressionNumberVisitorComparison {

    static boolean compare(final Number left,
                           final Number right,
                           final ExpressionNumberReducerContext context) {
        final ExpressionNumberReducerBinaryExpressionNumberVisitorGreaterThan visitor = new ExpressionNumberReducerBinaryExpressionNumberVisitorGreaterThan(context);
        visitor.accept(left, right);
        return visitor.comparisonResult;
    }

    ExpressionNumberReducerBinaryExpressionNumberVisitorGreaterThan(final ExpressionNumberReducerContext context) {
        super(context);
    }

    @Override
    boolean isComparisonTrue(final int compareResult) {
        return compareResult > 0;
    }

    @Override
    public String toString() {
        return "greaterThan";
    }
}
