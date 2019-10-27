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

import walkingkooka.visit.Visiting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

/**
 * A not expression.
 */
public final class ExpressionNotNode extends ExpressionUnaryNode {

    public final static ExpressionNodeName NAME = ExpressionNodeName.fromClass(ExpressionNotNode.class);

    public final static String SYMBOL = "!";

    static ExpressionNotNode with(final ExpressionNode value) {
        Objects.requireNonNull(value, "value");
        return new ExpressionNotNode(NO_INDEX, value);
    }

    private ExpressionNotNode(final int index, final ExpressionNode value) {
        super(index, value);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    @Override
    public ExpressionNotNode removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    public ExpressionNotNode setChildren(final List<ExpressionNode> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    ExpressionNotNode replace1(final int index, final ExpressionNode expression) {
        return new ExpressionNotNode(index, expression);
    }

    // visitor..........................................................................................................

    @Override
    public void accept(final ExpressionNodeVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // evaluation .....................................................................................................

    @Override
    public BigDecimal toBigDecimal(final ExpressionEvaluationContext context) {
        return new BigDecimal(this.toBigInteger(context));
    }

    @Override
    public BigInteger toBigInteger(final ExpressionEvaluationContext context) {
        return this.value().toBigInteger(context).not();
    }

    @Override
    public boolean toBoolean(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.toNumber(context), Boolean.class);
    }

    @Override
    public double toDouble(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.value().toBigInteger(context).not(), Double.class);
    }

    @Override
    public long toLong(final ExpressionEvaluationContext context) {
        return ~this.value().toLong(context);
    }

    @Override
    public Number toNumber(final ExpressionEvaluationContext context) {
        final Number number = this.value().toNumber(context);
        return number instanceof Long ?
                this.applyLong((Long) number) :
                this.applyBigInteger(context.convertOrFail(number, BigInteger.class));
    }

    private BigInteger applyBigInteger(final BigInteger bigInteger) {
        return bigInteger.not();
    }

    private Long applyLong(final Long longValue) {
        return ~longValue;
    }

    @Override
    public String toText(final ExpressionEvaluationContext context) {
        return context.convertOrFail(this.toNumber(context), String.class);
    }

    // Object ....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionNotNode;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(SYMBOL);
        this.value().toString0(b);
    }
}
