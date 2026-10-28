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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.collect.list.ImmutableListTesting;
import walkingkooka.reflect.PackagePrivateClassTesting;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BinaryExpressionChildrenListTest implements ImmutableListTesting<BinaryExpressionChildrenList, Expression>,
    HashCodeEqualsDefinedTesting2<BinaryExpressionChildrenList>,
    PackagePrivateClassTesting<BinaryExpressionChildrenList> {

    private final static Expression LEFT = Expression.value("left");

    private final static Expression RIGHT = Expression.value("right");

    private final static Expression DIFFERENT = Expression.value("different");

    @Test
    public void testWithNullLeftFails() {
        assertThrows(
            NullPointerException.class,
            () -> BinaryExpressionChildrenList.with(
                null,
                RIGHT
            )
        );
    }

    @Test
    public void testWithNullRightFails() {
        assertThrows(
            NullPointerException.class,
            () -> BinaryExpressionChildrenList.with(
                LEFT,
                null
            )
        );
    }

    @Test
    public void testWith() {
        final BinaryExpressionChildrenList list = this.createList();

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
        final BinaryExpressionChildrenList list = this.createList();

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
            BinaryExpressionChildrenList.with(
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
            BinaryExpressionChildrenList.with(
                LEFT,
                DIFFERENT
            )
        );
    }

    private void setLeftAndRightAndCheck(final BinaryExpressionChildrenList list,
                                         final Expression left,
                                         final Expression right,
                                         final BinaryExpressionChildrenList expected) {
        final BinaryExpressionChildrenList actual = list.setLeftAndRight(
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
    public BinaryExpressionChildrenList createList() {
        return BinaryExpressionChildrenList.with(
            LEFT,
            RIGHT
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferentLeft() {
        this.checkNotEquals(
            BinaryExpressionChildrenList.with(
                DIFFERENT,
                RIGHT
            )
        );
    }

    @Test
    public void testEqualsDifferentRight() {
        this.checkNotEquals(
            BinaryExpressionChildrenList.with(
                LEFT,
                DIFFERENT
            )
        );
    }

    @Override
    public BinaryExpressionChildrenList createObject() {
        return this.createList();
    }

    // class............................................................................................................

    @Override
    public Class<BinaryExpressionChildrenList> type() {
        return BinaryExpressionChildrenList.class;
    }
}
