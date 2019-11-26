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

import walkingkooka.tree.expression.ExpressionNumberVisitor;

import java.math.BigDecimal;
import java.math.BigInteger;

final class ExpressionNumberFunctionExpressionNumberVisitor extends ExpressionNumberVisitor {

    static Number apply(final Number number,
                        final NumberExpressionFunction2 function,
                        final ExpressionFunctionContext context) {
        final ExpressionNumberFunctionExpressionNumberVisitor visitor = new ExpressionNumberFunctionExpressionNumberVisitor(function, context);
        visitor.accept(number);
        return visitor.result;
    }

    ExpressionNumberFunctionExpressionNumberVisitor(final NumberExpressionFunction2 function,
                                                    final ExpressionFunctionContext context) {
        super();
        this.function = function;
        this.context = context;
    }

    @Override
    protected void visit(final BigDecimal number) {
        this.result = this.function.applyBigDecimal(number);
    }

    @Override
    protected void visit(final BigInteger number) {
        this.result = this.function.applyBigInteger(number);
    }

    @Override
    protected void visit(final Double number) {
        this.result = this.function.applyDouble(number);
    }

    @Override
    protected void visit(final Long number) {
        this.result = this.function.applyLong(number);
    }

    private Number result;

    @Override
    protected Number visit(final Number number) {
        return this.context.convertOrFail(number, BigDecimal.class);
    }

    private final NumberExpressionFunction2 function;
    private final ExpressionFunctionContext context;

    @Override
    public String toString() {
        return this.function.toString();
    }
}
