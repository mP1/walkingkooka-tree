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

import walkingkooka.visit.Visiting;

import java.math.BigDecimal;
import java.math.BigInteger;

abstract class ExpressionNumberReducerExpressionNumberVisitorLogical extends ExpressionNumberReducerExpressionNumberVisitor {

    ExpressionNumberReducerExpressionNumberVisitorLogical(final ExpressionNumberReducerContext context) {
        super(context);
    }

    @Override
    protected final void visit(final BigDecimal number) {
        this.visit(this.context.convertOrFail(number, BigInteger.class));
    }

    @Override
    protected abstract void visit(final BigInteger number);

    @Override
    protected final void visit(final Double number) {
        this.visit(this.context.convertOrFail(number, BigInteger.class));
    }

    @Override
    protected abstract void visit(final Long number);

    @Override
    final Class<? extends Number> acceptFloatDoubleType() {
        return BigInteger.class;
    }

    @Override
    final Class<? extends Number> acceptNumberType() {
        return BigInteger.class;
    }
}
