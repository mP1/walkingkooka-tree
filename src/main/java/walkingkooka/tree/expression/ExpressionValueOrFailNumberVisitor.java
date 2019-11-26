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

import walkingkooka.math.NumberVisitor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A {@link Number} which accepts a {@link Number} which is then traversed and made into a {@link Expression}
 */
final class ExpressionValueOrFailNumberVisitor extends NumberVisitor {

    static Expression expression(final Number number) {
        final ExpressionValueOrFailNumberVisitor visitor = new ExpressionValueOrFailNumberVisitor();
        visitor.accept(number);
        return visitor.node;
    }

    ExpressionValueOrFailNumberVisitor() {
        super();
    }

    @Override
    protected void visit(final BigDecimal number) {
        this.node = Expression.bigDecimal(number);
    }

    @Override
    protected void visit(final BigInteger number) {
        this.node = Expression.bigInteger(number);
    }

    @Override
    protected void visit(final Byte number) {
        this.node = Expression.longExpression(number);
    }

    @Override
    protected void visit(final Double number) {
        this.node = Expression.doubleExpression(number);
    }

    @Override
    protected void visit(final Float number) {
        this.node = Expression.doubleExpression(number);
    }

    @Override
    protected void visit(final Integer number) {
        this.node = Expression.longExpression(number);
    }

    @Override
    protected void visit(final Long number) {
        this.node = Expression.longExpression(number);
    }

    @Override
    protected void visit(final Short number) {
        this.node = Expression.longExpression(number);
    }

    @Override
    protected void visitUnknown(final Number number) {
        Expression.valueOrFailFail(number);
    }

    Expression node;

    @Override
    public String toString() {
        return String.valueOf(this.node);
    }
}
