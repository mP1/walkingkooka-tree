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

import java.math.BigInteger;
import java.util.Objects;

/**
 * A {@link BigInteger} number value.
 */
public final class BigIntegerExpression extends ValueExpression<BigInteger> {

    public final static FunctionExpressionName NAME = FunctionExpressionName.fromClass(BigIntegerExpression.class);

    static BigIntegerExpression with(final BigInteger value) {
        Objects.requireNonNull(value, "value");
        return new BigIntegerExpression(NO_INDEX, value);
    }

    private BigIntegerExpression(final int index, final BigInteger value) {
        super(index, value);
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    @Override
    public BigIntegerExpression removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    BigIntegerExpression replace1(final int index, final BigInteger value) {
        return new BigIntegerExpression(index, value);
    }

    // visitor..........................................................................................................

    @Override
    public void accept(final ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    // Object ....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof BigIntegerExpression;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(this.value);
    }
}
