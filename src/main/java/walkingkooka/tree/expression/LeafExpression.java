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

import java.util.List;
import java.util.Objects;

/**
 * A leaf node, where a leaf has no children.
 */
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

    final LeafExpression<V> setValue0(final V value) {
        return Objects.equals(this.value(), value) ?
                this :
                this.replaceValue(value);
    }

    final LeafExpression<V> replaceValue(final V value) {
        return this.replace1(this.index, value)
                .replaceChild(this.parent())
                .cast();
    }

    @Override
    final Expression replace(final int index) {
        return this.replace0(index);
    }

    final LeafExpression replace0(final int index) {
        return this.replace1(index, this.value);
    }

    abstract LeafExpression replace1(final int index, final V value);

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
        throw new UnsupportedOperationException();
    }

    @Override
    final Expression setChild(final Expression newChild) {
        return NeverError.unexpectedMethodCall(this, "setChild", newChild);
    }

    // Object ......................................................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    final boolean equalsDescendants0(final Expression other) {
        return true;
    }

    @Override
    final boolean equalsIgnoringParentAndChildren(final Expression other) {
        return other instanceof LeafExpression &&
                equalsIgnoringParentAndChildren0(Cast.to(other));

    }

    private boolean equalsIgnoringParentAndChildren0(final LeafExpression other) {
        return this.value.equals(other.value);
    }
}
