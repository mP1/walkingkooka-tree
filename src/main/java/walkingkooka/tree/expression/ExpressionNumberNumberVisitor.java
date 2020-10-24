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
 * A {@link Number} which accepts a {@link Number} and attempts to wrap the value in a {@link ExpressionNumber}.
 */
final class ExpressionNumberNumberVisitor extends NumberVisitor {

    static ExpressionNumber toExpressionNumber(final Number number) {
        final ExpressionNumberNumberVisitor visitor = new ExpressionNumberNumberVisitor();
        visitor.accept(number);
        return visitor.number;
    }

    ExpressionNumberNumberVisitor() {
        super();
    }

    @Override
    protected void visit(final BigDecimal number) {
        this.save(number);
    }

    @Override
    protected void visit(final BigInteger number) {
        this.save(new BigDecimal(number));
    }

    @Override
    protected void visit(final Byte number) {
        this.save(number);
    }

    @Override
    protected void visit(final Double number) {
        this.save(number);
    }

    @Override
    protected void visit(final Float number) {
        this.save(number);
    }

    @Override
    protected void visit(final Integer number) {
        this.save(number);
    }

    @Override
    protected void visit(final Long number) {
        this.save(number);
    }

    @Override
    protected void visit(final Short number) {
        this.save(number);
    }

    @Override
    protected void visitUnknown(final Number number) {
        if (false == number instanceof ExpressionNumber) {
            Expression.valueOrFailFail(number);
        }
        this.number = (ExpressionNumber) number;
    }

    private void save(final BigDecimal value) {
        this.number = ExpressionNumber.with(value);
    }

    private void save(final double value) {
        this.number = ExpressionNumber.with(value);
    }

    private ExpressionNumber number;

    @Override
    public String toString() {
        return String.valueOf(this.number);
    }
}
