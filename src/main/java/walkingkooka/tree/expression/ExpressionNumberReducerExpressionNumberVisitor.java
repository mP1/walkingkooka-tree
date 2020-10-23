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

abstract class ExpressionNumberReducerExpressionNumberVisitor extends ExpressionNumberVisitor {

    static Number abs(final Number value,
                      final ExpressionNumberReducerContext context) {
        return ExpressionNumberReducerExpressionNumberVisitorAbs.compute(value, context);
    }

    ExpressionNumberReducerExpressionNumberVisitor(final ExpressionNumberReducerContext context) {
        super();
        this.context = context;
    }

    @Override
    protected abstract void visit(final BigDecimal number);

    @Override
    protected abstract void visit(final BigInteger number);

    @Override
    protected abstract void visit(final Double number);

    @Override
    protected abstract void visit(final Long number);

    @Override
    protected final void visit(final Number number) {
        final ExpressionNumberReducerContext context = this.context;

        if(ExpressionNumber.isByteShortIntegerLong(number)) {
            this.accept(context.convertOrFail(number, Long.class));
        } else {
            if(ExpressionNumber.isFloatDouble(number)) {
                this.accept(context.convertOrFail(number, Double.class));
            } else {
                this.accept(context.convertOrFail(number, BigDecimal.class));
            }
        }
    }

    final ExpressionNumberReducerContext context;

    Number result;

    public abstract String toString();
}
