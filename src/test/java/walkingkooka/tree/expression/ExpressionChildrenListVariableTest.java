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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionChildrenListVariableTest extends ExpressionChildrenListTestCase<ExpressionChildrenListVariable> {

    private final static Expression EXPRESSION1 = Expression.value("111");

    private final static Expression EXPRESSION2 = Expression.value("222");

    private final static Expression EXPRESSION3 = Expression.value("333");

    private final static Expression EXPRESSION4 = Expression.value("444");

    private final static Expression EXPRESSION5 = Expression.value("555");

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
            EXPRESSION1
        );
    }

    @Test
    public void testGetWithOne() {
        this.getAndCheck(
            this.createList(),
            1,
            EXPRESSION2
        );
    }

    @Test
    public void testGetWithTwo() {
        this.getAndCheck(
            this.createList(),
            1,
            EXPRESSION2
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
                EXPRESSION1,
                EXPRESSION3
            ),
            new ExpressionChildrenListVariable(
                Lists.of(
                    EXPRESSION1,
                    EXPRESSION3
                )
            )
        );
    }

    // subList..........................................................................................................

    private final static List<Expression> ELEMENTS = Lists.of(
        EXPRESSION1,
        EXPRESSION2,
        EXPRESSION3,
        EXPRESSION4,
        EXPRESSION5
    );

    @Test
    public void testSubList() {
        this.subListAndCheck(
            new ExpressionChildrenListVariable(ELEMENTS),
            0,
            2,
            new ExpressionChildrenListVariable(
                Lists.of(
                    EXPRESSION1,
                    EXPRESSION2
                )
            )
        );
    }

    @Test
    public void testSubList2() {
        this.subListAndCheck(
            new ExpressionChildrenListVariable(ELEMENTS),
            2,
            4,
            new ExpressionChildrenListVariable(
                Lists.of(
                    EXPRESSION3,
                    EXPRESSION4
                )
            )
        );
    }

    @Test
    public void testSubListSubList() {
        this.subListAndCheck(
            new ExpressionChildrenListVariable(ELEMENTS)
                .subList(
                    1,
                    4
                ), // 2, 3, 4
            1,
            3,
            new ExpressionChildrenListVariable(
                Lists.of(
                    EXPRESSION3,
                    EXPRESSION4
                )
            )
        );
    }

    @Override
    public ExpressionChildrenListVariable createList() {
        return new ExpressionChildrenListVariable(
            Lists.of(
                EXPRESSION1,
                EXPRESSION2
            )
        );
    }

    @Test
    public void testSetParent() {
        final ListExpression listExpression = Expression.list(
            Lists.of(
                EXPRESSION1,
                EXPRESSION2,
                EXPRESSION3
            )
        );

        this.checkEquals(
            ExpressionChildrenListVariable.EMPTY.setElements(
                Lists.of(
                    EXPRESSION1.setParent(
                        Optional.of(listExpression),
                        0
                    ),
                    EXPRESSION2.setParent(
                        Optional.of(listExpression),
                        1
                    ),
                    EXPRESSION3.setParent(
                        Optional.of(listExpression),
                        2
                    )
                )
            ),
            ExpressionChildrenListVariable.EMPTY.setElements(
                Lists.of(
                    EXPRESSION1,
                    EXPRESSION2,
                    EXPRESSION3
                )
            ).setParent(listExpression)
        );
    }

    @Test
    public void testExpressionChildrenListUnary() {
        this.toExpressionChildrenListUnaryAndCheck(
            ExpressionChildrenListVariable.EMPTY.setElements(
                Lists.of(EXPRESSION1)
            )
        );
    }

    @Test
    public void testExpressionChildrenListUnaryNonOneFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createList()
                .toExpressionChildrenListUnary()
        );
    }

    @Test
    public void testExpressionChildrenListBinary() {
        this.toExpressionChildrenListBinaryAndCheck(
            this.createList()
        );
    }

    @Test
    public void testExpressionChildrenListVariable() {
        final ExpressionChildrenListVariable list = this.createList();

        assertSame(
            list,
            list.toExpressionChildrenListVariable()
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(
            new ExpressionChildrenListVariable(
                Lists.of(
                    EXPRESSION3,
                    EXPRESSION2
                )
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionChildrenListVariable> type() {
        return ExpressionChildrenListVariable.class;
    }
}
