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

import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A parent holding zero or more child expressions.
 */
abstract class ExpressionParentNode extends ExpressionNode {

    ExpressionParentNode(final int index, final List<ExpressionNode> children) {
        super(index);

        final Optional<ExpressionNode> p = Optional.of(this);

        final List<ExpressionNode> copy = Lists.array();
        this.children = copy;

        int i = 0;
        for (ExpressionNode child : children) {
            copy.add(child.setParent(p, i));
            i++;
        }
    }

    @Override
    public final List<ExpressionNode> children() {
        return this.children;
    }

    private final List<ExpressionNode> children;

    final ExpressionNode setChildren0(final List<ExpressionNode> children) {
        Objects.requireNonNull(children, "children");

        final List<ExpressionNode> copy = Lists.immutable(children);
        return Lists.equals(this.children(), copy, (first, other) -> first.equalsIgnoringParentAndChildren(other) && first.equalsDescendants0(other)) ?
                this :
                this.replaceChildren(copy);
    }

    @Override
    final ExpressionNode setChild(final ExpressionNode newChild) {
        final int index = newChild.index();
        final ExpressionNode previous = this.children().get(index);
        return previous.equalsIgnoringParentAndChildren(newChild) && previous.equalsDescendants(newChild) ?
                this :
                this.replaceChild0(newChild, index);
    }

    private ExpressionNode replaceChild0(final ExpressionNode newChild, final int index) {
        final List<ExpressionNode> newChildren = Lists.array();
        newChildren.addAll(this.children());
        newChildren.set(index, newChild);

        return this.replaceChildren(newChildren);
    }

    private ExpressionParentNode replaceChildren(final List<ExpressionNode> children) {
        this.replaceChildrenCheck(children);
        return this.replace0(this.index, children)
                .replaceChild(this.parent())
                .cast();
    }

    abstract void replaceChildrenCheck(final List<ExpressionNode> children);

    @Override
    final ExpressionNode replace(final int index) {
        return this.replace0(index, this.children());
    }

    abstract ExpressionParentNode replace0(final int index, final List<ExpressionNode> children);

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return this.children().hashCode();
    }

    final boolean equalsDescendants0(final ExpressionNode other) {
        return this.equalsDescendants1(other.children());
    }

    /**
     * Only returns true if the descendants of this node and the given children are equal ignoring the parents.
     */
    private boolean equalsDescendants1(final List<ExpressionNode> otherChildren) {
        final List<ExpressionNode> children = this.children();
        final int count = children.size();
        boolean equals = count == otherChildren.size();

        if (equals) {
            for (int i = 0; equals && i < count; i++) {
                equals = children.get(i).equalsDescendants(otherChildren.get(i));
            }
        }

        return equals;
    }
}
