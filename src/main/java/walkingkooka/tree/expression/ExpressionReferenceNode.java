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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * A reference expression.
 */
public final class ExpressionReferenceNode extends ExpressionLeafNode<ExpressionReference> {

    public final static ExpressionNodeName NAME = ExpressionNodeName.fromClass(ExpressionReferenceNode.class);

    static ExpressionReferenceNode with(final ExpressionReference reference) {
        Objects.requireNonNull(reference, "reference");

        return new ExpressionReferenceNode(NO_INDEX, reference);
    }

    private ExpressionReferenceNode(final int index, final ExpressionReference reference) {
        super(index, reference);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    @Override
    public ExpressionReferenceNode removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    ExpressionReferenceNode replace1(final int index, final ExpressionReference value) {
        return new ExpressionReferenceNode(index, value);
    }

    // visitor..........................................................................................................

    @Override
    public void accept(final ExpressionNodeVisitor visitor) {
        visitor.visit(this);
    }

    // evaluation .....................................................................................................

    @Override
    public final BigDecimal toBigDecimal(final ExpressionEvaluationContext context) {
        return context.referenceOrFail(this.value).toBigDecimal(context);
    }

    @Override
    public final BigInteger toBigInteger(final ExpressionEvaluationContext context) {
        return context.referenceOrFail(this.value).toBigInteger(context);
    }

    @Override
    public final boolean toBoolean(final ExpressionEvaluationContext context) {
        return this.toExpressionNode(context).toBoolean(context);
    }

    @Override
    public final double toDouble(final ExpressionEvaluationContext context) {
        return this.toExpressionNode(context).toDouble(context);
    }

    @Override
    public final LocalDate toLocalDate(final ExpressionEvaluationContext context) {
        return this.toExpressionNode(context).toLocalDate(context);
    }

    @Override
    public final LocalDateTime toLocalDateTime(final ExpressionEvaluationContext context) {
        return this.toExpressionNode(context).toLocalDateTime(context);
    }

    @Override
    public final LocalTime toLocalTime(final ExpressionEvaluationContext context) {
        return this.toExpressionNode(context).toLocalTime(context);
    }

    @Override
    public final long toLong(final ExpressionEvaluationContext context) {
        return this.toExpressionNode(context).toLong(context);
    }

    @Override
    public final Number toNumber(final ExpressionEvaluationContext context) {
        return this.toExpressionNode(context).toNumber(context);
    }

    @Override
    public final String toText(final ExpressionEvaluationContext context) {
        return this.toExpressionNode(context).toText(context);
    }

    @Override
    public final Object toValue(final ExpressionEvaluationContext context) {
        return this.toExpressionNode(context).toValue(context);
    }

    private ExpressionNode toExpressionNode(final ExpressionEvaluationContext context) {
        return context.referenceOrFail(this.value);
    }

    // Object ....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionReferenceNode;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(this.value);
    }
}
