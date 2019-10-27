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


import java.time.LocalTime;
import java.util.Objects;

/**
 * A {@link LocalTime} number value.
 */
public final class ExpressionLocalTimeNode extends ExpressionValueNode<LocalTime> {

    public final static ExpressionNodeName NAME = ExpressionNodeName.fromClass(ExpressionLocalTimeNode.class);

    static ExpressionLocalTimeNode with(final LocalTime value) {
        Objects.requireNonNull(value, "value");
        return new ExpressionLocalTimeNode(NO_INDEX, value);
    }

    private ExpressionLocalTimeNode(final int index, final LocalTime value) {
        super(index, value);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    @Override
    public ExpressionLocalTimeNode removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    ExpressionLocalTimeNode replace1(final int index, final LocalTime value) {
        return new ExpressionLocalTimeNode(index, value);
    }

    // visitor..........................................................................................................

    @Override
    public void accept(final ExpressionNodeVisitor visitor) {
        visitor.visit(this);
    }

    // Object ....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionLocalTimeNode;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(this.value);
    }
}
