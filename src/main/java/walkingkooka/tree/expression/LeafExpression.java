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

import walkingkooka.Cast;
import walkingkooka.NeverError;
import walkingkooka.Value;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;

/**
 * A leaf node, where a leaf has no children.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
abstract class LeafExpression<V> extends Expression implements Value<V> {

    LeafExpression(final int index, final V value) {
        super(index);
        this.value = value;
    }

    @Override
    public final V value() {
        return this.value;
    }

    final V value;

    @Override
    public final List<Expression> children() {
        return Lists.empty();
    }

    /**
     * By definition a leaf node has no children.
     */
    @Override
    public final Expression setChildren(final List<Expression> children) {
        Objects.requireNonNull(children, "children");

        if (false == children.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return this;
    }

    @Override final Expression setChild(final Expression newChild) {
        return NeverError.unexpectedMethodCall(this, "setChild", newChild);
    }

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        final Object value = this.value();

        printer.print(this.typeName());
        printer.print(" ");
        printer.print(CharSequences.quoteIfChars(value));

        if (!this.isNamedFunction() && null != value) {
            printer.print(" (");
            printer.print(value.getClass().getName());
            printer.print(")");
        }

        printer.println();
    }

    // Object ......................................................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(this.value);
    }

    @Override //
    final boolean equalsChildren(final Expression other) {
        return true;
    }

    @Override //
    final boolean equalsIgnoringChildren(final Expression other) {
        return other instanceof LeafExpression &&
            equalsChildren0(Cast.to(other));
    }

    private boolean equalsChildren0(final LeafExpression<?> other) {
        return this.value.equals(other.value);
    }
}
