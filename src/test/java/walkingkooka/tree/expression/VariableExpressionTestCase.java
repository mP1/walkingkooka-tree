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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class VariableExpressionTestCase<N extends VariableExpression> extends ParentExpressionTestCase<N> {

    @Test
    public final void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> this.createExpression((List<Expression>) null));
    }

    @Test
    public final void testSetChildrenZero() {
        this.createAndSetChildren();
    }

    @Test
    public final void testSetChildrenOne() {
        this.createAndSetChildren(child1());
    }

    @Test
    public final void testSetChildrenThree() {
        this.createAndSetChildren(child1(), differentChild(), text("third"));
    }

    private void createAndSetChildren(final Expression... children) {
        final N expression = this.createExpression();

        final List<Expression> list = Lists.of(children);
        final N different = expression.setChildren(list).cast();
        this.checkChildren(different, list);
    }

    @Test
    public final void testSetDifferentChildren() {
        final N expression = this.createExpression();
        final List<Expression> children = expression.children();

        final List<Expression> differentChildren = Lists.of(differentChild());
        final N different = expression.setChildren(differentChildren).cast();
        assertNotSame(expression, different);

        assertEquals(expression.name(), different.name(), "name");

        this.checkChildren(different, differentChildren);
        this.checkChildren(expression, children);

        assertSame(children, expression.children(), "original children");
        assertNotSame(differentChildren, different.children(), "updated children");
    }

    @Test
    public final void testSetDifferentChildrenListCopied() {
        final N expression = this.createExpression();

        final List<Expression> differentChildren = Lists.array();
        differentChildren.add(differentChild());

        final N different = expression.setChildren(differentChildren).cast();
        assertNotSame(expression, different);

        this.checkChildren(different, differentChildren);
        this.checkChildren(expression, this.children());
    }

    @Test
    public final void testEqualsDifferentChildren() {
        assertNotEquals(this.createExpression(), this.createExpression(differentChild()));
    }

    @Override
    final N createExpression() {
        return this.createExpression(this.children());
    }

    final N createExpression(final Expression... children) {
        return this.createExpression(Lists.of(children));
    }

    abstract N createExpression(final List<Expression> children);

    @Override
    final List<Expression> children() {
        return Lists.of(this.child1(), this.child2(), this.child3());
    }

    final Expression child1() {
        return text("child-111");
    }

    final Expression child2() {
        return text("child-222");
    }

    final Expression child3() {
        return text("child-333");
    }

    final Expression differentChild() {
        return text("different-child123");
    }
}
