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
import walkingkooka.collect.list.Lists;

public final class ExpressionChildrenListVariableTest extends ExpressionChildrenListTestCase<ExpressionChildrenListVariable> {

    private final static Expression FIRST = Expression.value("1st");

    private final static Expression SECOND = Expression.value("2nd");

    private final static Expression DIFFERENT = Expression.value("different");

    // EMPTY............................................................................................................

    @Test
    public void testEmpty() {
        this.checkEquals(
            Lists.empty(),
            ExpressionChildrenListVariable.EMPTY
        );
    }

    // get..............................................................................................................

    @Test
    public void testGetWithZero() {
        this.getAndCheck(
            this.createList(),
            0,
            FIRST
        );
    }

    @Test
    public void testGetWithOne() {
        this.getAndCheck(
            this.createList(),
            1,
            SECOND
        );
    }

    @Test
    public void testGetWithTwo() {
        this.getAndCheck(
            this.createList(),
            1,
            SECOND
        );
    }

    @Test
    public void testGetWithMoreFails() {
        this.getFails(
            this.createList(),
            3
        );
    }

    // setElements......................................................................................................

    @Test
    public void testSetElementsWithDifferent() {
        final ExpressionChildrenListVariable list = this.createList();

        this.setElementsAndCheck(
            list,
            Lists.of(
                FIRST,
                DIFFERENT
            ),
            new ExpressionChildrenListVariable(
                FIRST,
                DIFFERENT
            )
        );
    }

    @Override
    public ExpressionChildrenListVariable createList() {
        return new ExpressionChildrenListVariable(
            FIRST,
            SECOND
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(
            new ExpressionChildrenListVariable(
                DIFFERENT,
                SECOND
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionChildrenListVariable> type() {
        return ExpressionChildrenListVariable.class;
    }
}
