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


/**
 * A {@link Double} number value.
 */
public final class ExpressionDoubleNode extends ExpressionValueNode<Double> {

    public final static ExpressionNodeName NAME = ExpressionNodeName.fromClass(ExpressionDoubleNode.class);

    static ExpressionDoubleNode with(final double value) {
        return new ExpressionDoubleNode(NO_INDEX, value);
    }

    private ExpressionDoubleNode(final int index, final Double value) {
        super(index, value);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    @Override
    public ExpressionDoubleNode removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    ExpressionDoubleNode replace1(final int index, final Double value) {
        return new ExpressionDoubleNode(index, value);
    }

    // visitor..........................................................................................................

    @Override
    public void accept(final ExpressionNodeVisitor visitor) {
        visitor.visit(this);
    }

    // Object ....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionDoubleNode;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(this.value);
    }
}
