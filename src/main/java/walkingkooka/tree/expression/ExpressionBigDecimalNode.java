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
import java.util.Objects;

/**
 * A {@link BigDecimal} number value.
 */
public final class ExpressionBigDecimalNode extends ExpressionValueNode<BigDecimal> {

    public final static ExpressionNodeName NAME = ExpressionNodeName.fromClass(ExpressionBigDecimalNode.class);

    static ExpressionBigDecimalNode with(final BigDecimal value) {
        Objects.requireNonNull(value, "value");
        return new ExpressionBigDecimalNode(NO_INDEX, value);
    }

    private ExpressionBigDecimalNode(final int index, final BigDecimal value) {
        super(index, value);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    @Override
    public ExpressionBigDecimalNode removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    ExpressionBigDecimalNode replace1(final int index, final BigDecimal value) {
        return new ExpressionBigDecimalNode(index, value);
    }

    // visitor..........................................................................................................

    @Override
    public void accept(final ExpressionNodeVisitor visitor) {
        visitor.visit(this);
    }

    // Object ....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionBigDecimalNode;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(this.value);
    }
}
