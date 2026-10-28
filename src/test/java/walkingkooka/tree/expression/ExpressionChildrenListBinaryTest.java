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

public final class ExpressionChildrenListBinaryTest extends ExpressionChildrenListTestCase<ExpressionChildrenListBinary> {

    private final static Expression LEFT = Expression.value("left");

    private final static Expression RIGHT = Expression.value("right");

    private final static Expression DIFFERENT = Expression.value("different");

    @Test
    public void testWithNullLeftFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionChildrenListBinary.with(
                null,
                RIGHT
            )
        );
    }

    @Test
    public void testWithNullRightFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionChildrenListBinary.with(
                LEFT,
                null
            )
        );
    }

    @Test
    public void testWith() {
        final ExpressionChildrenListBinary list = this.createList();

        this.checkEquals(
            LEFT,
            list.left,
            "left"
        );
        this.checkEquals(
            RIGHT,
            list.right,
            "right"
        );
    }

    @Test
    public void testGetWithZero() {
        this.getAndCheck(
            this.createList(),
            0,
            LEFT
        );
    }

    @Test
    public void testGetWithOne() {
        this.getAndCheck(
            this.createList(),
            1,
            RIGHT
        );
    }

    @Test
    public void testGetWithTwoFails() {
        this.getFails(
            this.createList(),
            2
        );
    }

    @Test
    public void testSetLeftAndRightWithSame() {
        final ExpressionChildrenListBinary list = this.createList();

        this.setLeftAndRightAndCheck(
            list,
            LEFT,
            RIGHT,
            list
        );
    }

    @Test
    public void testSetLeftAndRightWithDifferentLeft() {
        this.setLeftAndRightAndCheck(
            this.createList(),
            DIFFERENT,
            RIGHT,
            ExpressionChildrenListBinary.with(
                DIFFERENT,
                RIGHT
            )
        );
    }

    @Test
    public void testSetLeftAndRightWithDifferentRight() {
        this.setLeftAndRightAndCheck(
            this.createList(),
            LEFT,
            DIFFERENT,
            ExpressionChildrenListBinary.with(
                LEFT,
                DIFFERENT
            )
        );
    }

    private void setLeftAndRightAndCheck(final ExpressionChildrenListBinary list,
                                         final Expression left,
                                         final Expression right,
                                         final ExpressionChildrenListBinary expected) {
        final ExpressionChildrenListBinary actual = list.setLeftAndRight(
            left,
            right
        );

        if (expected.equals(list)) {
            assertSame(
                list,
                actual
            );
        } else {
            this.checkEquals(
                expected,
                actual,
                () -> "setLeftAndRight " + left + " " + right
            );
        }
    }

    @Override
    public ExpressionChildrenListBinary createList() {
        return ExpressionChildrenListBinary.with(
            LEFT,
            RIGHT
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferentLeft() {
        this.checkNotEquals(
            ExpressionChildrenListBinary.with(
                DIFFERENT,
                RIGHT
            )
        );
    }

    @Test
    public void testEqualsDifferentRight() {
        this.checkNotEquals(
            ExpressionChildrenListBinary.with(
                LEFT,
                DIFFERENT
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionChildrenListBinary> type() {
        return ExpressionChildrenListBinary.class;
    }
}
