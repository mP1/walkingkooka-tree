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

import java.math.BigDecimal;
import java.math.BigInteger;

final class ExpressionNumberReducerExpressionNumberVisitorNot extends ExpressionNumberReducerExpressionNumberVisitorLogical {

    static Number compute(final Number value,
                          final ExpressionNumberReducerContext context) {
        final ExpressionNumberReducerExpressionNumberVisitorNot visitor = new ExpressionNumberReducerExpressionNumberVisitorNot(context);
        visitor.accept(value);
        return visitor.result;
    }

    ExpressionNumberReducerExpressionNumberVisitorNot(final ExpressionNumberReducerContext context) {
        super(context);
    }

    @Override
    protected void visit(final BigInteger number) {
        this.result = number.not();
    }

    @Override
    protected void visit(final Long number) {
        this.result = ~ number;
    }

    @Override
    public String toString() {
        return "not";
    }
}
