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
public final class StringExpression extends ValueExpression<String> {

    public final static FunctionExpressionName NAME = FunctionExpressionName.fromClass(StringExpression.class);

    static StringExpression with(final String value) {
        Objects.requireNonNull(value, "value");
        return new StringExpression(NO_INDEX, value);
    }

    private StringExpression(final int index, final String text) {
        super(index, text);
    }

    @Override
    public FunctionExpressionName name() {
        return NAME;
    }

    @Override
    public StringExpression removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    StringExpression replace1(final int index, final String value) {
        return new StringExpression(index, value);
    }

    // visitor..........................................................................................................

    @Override
    public void accept(final ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    // Object ....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof StringExpression;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(CharSequences.quoteAndEscape(this.value));
    }
}
