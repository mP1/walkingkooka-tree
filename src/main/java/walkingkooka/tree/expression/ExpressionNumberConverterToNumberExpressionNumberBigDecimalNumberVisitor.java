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

import walkingkooka.math.NumberVisitor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Creates a {@link ExpressionNumberBigDecimal from the given {@link Number}}. This assumes only valid Number sub classes are passed.
 * Passing a {@link ExpressionNumber} will fail.
 */
final class ExpressionNumberConverterToNumberExpressionNumberBigDecimalNumberVisitor extends NumberVisitor {

    static ExpressionNumber wrap(final Number number) {
        final ExpressionNumberConverterToNumberExpressionNumberBigDecimalNumberVisitor visitor = new ExpressionNumberConverterToNumberExpressionNumberBigDecimalNumberVisitor();
        visitor.accept(number);
        return visitor.result;
    }

    // @VisibleForTesting
    ExpressionNumberConverterToNumberExpressionNumberBigDecimalNumberVisitor() {
        super();
    }

    @Override
    protected void visit(final BigDecimal number) {
        this.acceptBigDecimal(number);
    }

    @Override
    protected void visit(final BigInteger number) {
        this.acceptBigDecimal(new BigDecimal(number));
    }

    @Override
    protected void visit(final Byte number) {
        this.acceptBigDecimal(number);
    }

    @Override
    protected void visit(final Double number) {
        this.acceptBigDecimal(number);
    }

    @Override
    protected void visit(final Float number) {
        this.acceptBigDecimal(number);
    }

    @Override
    protected void visit(final Integer number) {
        this.acceptBigDecimal(number);
    }

    @Override
    protected void visit(final Long number) {
        this.acceptBigDecimal(number);
    }

    @Override
    protected void visit(final Short number) {
        this.acceptBigDecimal(number);
    }

    @Override
    protected void visitUnknown(final Number number) {
        throw new UnsupportedOperationException(number.toString());
    }

    private void acceptBigDecimal(final double number) {
        this.acceptBigDecimal(BigDecimal.valueOf(number));
    }

    private void acceptBigDecimal(final long number) {
        this.acceptBigDecimal(BigDecimal.valueOf(number));
    }

    private void acceptBigDecimal(final BigDecimal number) {
        this.result = ExpressionNumber.with(number);
    }

    ExpressionNumber result;

    @Override
    public String toString() {
        return String.valueOf(this.result);
    }
}
