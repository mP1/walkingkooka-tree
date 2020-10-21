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

public class FakeBinaryExpressionNumberVisitor extends BinaryExpressionNumberVisitor {

    protected FakeBinaryExpressionNumberVisitor() {
        super();
    }

    @Override
    protected Visiting startVisit(final Number left, final Number right) {
        return super.startVisit(left, right);
    }

    @Override
    protected void endVisit(final Number left, final Number right) {
        super.endVisit(left, right);
    }

    @Override
    protected void visit(final Number left, final Number right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final BigDecimal left, final BigDecimal right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final BigDecimal left, final BigInteger right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final BigDecimal left, final Double right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final BigDecimal left, final Long right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final BigDecimal left, final Number right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final BigInteger left, final BigDecimal right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final BigInteger left, final BigInteger right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final BigInteger left, final Double right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final BigInteger left, final Long right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final BigInteger left, final Number right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final Double left, final BigDecimal right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final Double left, final BigInteger right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final Double left, final Double right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final Double left, final Long right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final Double left, final Number right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final Long left, final BigDecimal right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final Long left, final BigInteger right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final Long left, final Double right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final Long left, final Long right) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final Long left, final Number right) {
        throw new UnsupportedOperationException();
    }
}
