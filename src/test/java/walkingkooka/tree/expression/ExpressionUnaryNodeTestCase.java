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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ExpressionUnaryNodeTestCase<N extends ExpressionUnaryNode> extends ExpressionParentFixedNodeTestCase<N> {

    final static String CHILD = "child123";
    final static CharSequence CHILD_TO_STRING = CharSequences.quoteAndEscape(CHILD);

    @Test
    public final void testWithNullChildFails() {
        assertThrows(NullPointerException.class, () -> this.createExpressionNode(null));
    }

    @Test
    public final void testSetChildrenZeroFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createExpressionNode().setChildren(ExpressionNode.NO_CHILDREN));
    }

    @Test
    public final void testSetChildrenTwoFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createExpressionNode().setChildren(Lists.of(child(), differentChild())));
    }

    @Test
    public void testSetDifferentChildren() {
        final N expression = this.createExpressionNode();
        final List<ExpressionNode> children = expression.children();

        final List<ExpressionNode> differentChildren = Lists.of(differentChild());
        final N different = expression.setChildren(differentChildren).cast();
        assertNotSame(expression, different);

        this.checkChildren(different, differentChildren);
        this.checkChildren(expression, children);

        assertSame(children, expression.children(), "original children");
        assertNotSame(differentChildren, different.children(), "updated children");
    }

    @Test
    public void testReplaceChild() {
        final N expression = this.createExpressionNode();
        final ExpressionNode replacement = this.differentChild();
        final N replaced = expression.replaceChild(expression.value(), replacement).cast();

        this.checkChildren(replaced, Lists.of(replacement));
    }

    @Test
    public void testSetDifferentChildrenListCopied() {
        final N expression = this.createExpressionNode();

        final List<ExpressionNode> differentChildren = Lists.array();
        differentChildren.add(differentChild());

        final N different = expression.setChildren(differentChildren).cast();
        assertNotSame(expression, different);

        this.checkChildren(different, differentChildren);
        checkChildren(expression, this.children());
    }

    @Test
    public final void testEqualsDifferentChildren() {
        assertNotEquals(this.createExpressionNode(), this.createExpressionNode(differentChild()));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createExpressionNode(), this.expectedToString());
    }

    abstract String expectedToString();

    @Override
    N createExpressionNode() {
        return this.createExpressionNode(this.child());
    }

    abstract N createExpressionNode(final ExpressionNode child);

    final ExpressionNode child() {
        return text(CHILD);
    }

    @Override
    List<ExpressionNode> children() {
        return Lists.of(this.child());
    }

    final ExpressionNode differentChild() {
        return text("different-child123");
    }
}
