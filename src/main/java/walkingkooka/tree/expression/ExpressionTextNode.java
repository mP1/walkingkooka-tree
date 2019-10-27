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

import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * A text value.
 */
public final class ExpressionTextNode extends ExpressionValueNode<String> {

    public final static ExpressionNodeName NAME = ExpressionNodeName.fromClass(ExpressionTextNode.class);

    static ExpressionTextNode with(final String value) {
        Objects.requireNonNull(value, "value");
        return new ExpressionTextNode(NO_INDEX, value);
    }

    private ExpressionTextNode(final int index, final String text) {
        super(index, text);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    @Override
    public ExpressionTextNode removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    ExpressionTextNode replace1(final int index, final String value) {
        return new ExpressionTextNode(index, value);
    }

    // visitor..........................................................................................................

    @Override
    public void accept(final ExpressionNodeVisitor visitor) {
        visitor.visit(this);
    }

    // Object ....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionTextNode;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(CharSequences.quoteAndEscape(this.value));
    }
}
