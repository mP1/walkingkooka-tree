/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionChildrenListUnaryTest extends ExpressionChildrenListTestCase<ExpressionChildrenListUnary> {

    private final static Expression CHILD = Expression.value("child");

    private final static Expression DIFFERENT = Expression.value("different");

    @Test
    public void testWithNullChildFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionChildrenListUnary.with(
                null
            )
        );
    }

    @Test
    public void testWith() {
        final ExpressionChildrenListUnary list = this.createList();

        this.checkEquals(
            CHILD,
            list.child,
            "child"
        );
    }

    @Test
    public void testGetWithZero() {
        this.getAndCheck(
            this.createList(),
            0,
            CHILD
        );
    }

    @Test
    public void testGetWithOneFails() {
        this.getFails(
            this.createList(),
            1
        );
    }

    @Test
    public void testSetChildWithSame() {
        final ExpressionChildrenListUnary list = this.createList();

        this.setChildAndCheck(
            list,
            CHILD,
            list
        );
    }

    @Test
    public void testSetChildWithDifferentChild() {
        this.setChildAndCheck(
            this.createList(),
            DIFFERENT,
            ExpressionChildrenListUnary.with(
                DIFFERENT
            )
        );
    }

    private void setChildAndCheck(final ExpressionChildrenListUnary list,
                                  final Expression child,
                                  final ExpressionChildrenListUnary expected) {
        final ExpressionChildrenListUnary actual = list.setChild(child);

        if (expected.equals(list)) {
            assertSame(
                list,
                actual
            );
        } else {
            this.checkEquals(
                expected,
                actual,
                () -> "setChild " + child
            );
        }
    }

    @Override
    public ExpressionChildrenListUnary createList() {
        return ExpressionChildrenListUnary.with(CHILD);
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferentChild() {
        this.checkNotEquals(
            ExpressionChildrenListUnary.with(DIFFERENT)
        );
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionChildrenListUnary> type() {
        return ExpressionChildrenListUnary.class;
    }
}
