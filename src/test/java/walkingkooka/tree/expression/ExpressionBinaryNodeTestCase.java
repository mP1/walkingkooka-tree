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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ExpressionBinaryNodeTestCase<N extends ExpressionBinaryNode> extends ExpressionParentFixedNodeTestCase<N> {

    final static String LEFT = "left-123";
    final static String RIGHT = "right-456";

    final static CharSequence LEFT_TO_STRING = CharSequences.quoteAndEscape(LEFT);
    final static CharSequence RIGHT_TO_STRING = CharSequences.quoteAndEscape(RIGHT);

    @Test
    public final void testWithNullLeftFails() {
        assertThrows(NullPointerException.class, () -> this.createExpressionNode(null, this.right()));
    }

    @Test
    public final void testWithNullRightFails() {
        assertThrows(NullPointerException.class, () -> this.createExpressionNode(this.left(), null));
    }

    @Test
    public final void testRemoveParentWithParent() {
        final N node = this.createExpressionNode();

        final ExpressionNode parent = ExpressionNode.not(node);
        assertEquals(node,
                parent.children().get(0).removeParent());
    }

    @Test
    public final void testSetChildrenZeroFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createExpressionNode().setChildren(ExpressionNode.NO_CHILDREN));
    }

    @Test
    public final void testSetChildrenOneFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createExpressionNode().setChildren(Lists.of(left())));
    }

    @Test
    public final void testSetChildrenThreeFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createExpressionNode().setChildren(Lists.of(left(), right(), text("third"))));
    }

    @Test
    public void testSetDifferentChildren() {
        final N expression = this.createExpressionNode();
        final List<ExpressionNode> children = expression.children();

        final List<ExpressionNode> differentChildren = Lists.of(differentLeft(), differentRight());
        final N different = expression.setChildren(differentChildren).cast();
        assertNotSame(expression, different);

        this.checkChildren(different, differentChildren);
        this.checkChildren(expression, children);

        assertSame(children, expression.children(), "original children");
        assertNotSame(differentChildren, different.children(), "updated children");
    }

    @Test
    public void testSetDifferentChildrenListCopied() {
        final N expression = this.createExpressionNode();

        final List<ExpressionNode> differentChildren = Lists.array();
        differentChildren.add(differentLeft());
        differentChildren.add(differentRight());

        final N different = expression.setChildren(differentChildren).cast();
        assertNotSame(expression, different);

        this.checkChildren(different, differentChildren);
        checkChildren(expression, this.children());
    }

    @Test
    public void testToString() {
        assertEquals(this.expectedToString(), this.createExpressionNode().toString());
    }

    abstract String expectedToString();

    @Test
    public final void testEqualsDifferentChildren() {
        assertNotEquals(this.createExpressionNode(), this.createExpressionNode(differentLeft(), differentRight()));
    }

    @Override
    N createExpressionNode() {
        return this.createExpressionNode(this.left(), this.right());
    }

    abstract N createExpressionNode(final ExpressionNode left, final ExpressionNode right);

    @Override
    List<ExpressionNode> children() {
        return Lists.of(this.left(), this.right());
    }

    final ExpressionNode left() {
        return text(LEFT);
    }

    final ExpressionNode right() {
        return text(RIGHT);
    }

    final ExpressionNode differentLeft() {
        return text("different-left123");
    }

    final ExpressionNode differentRight() {
        return text("different-right456");
    }
}
