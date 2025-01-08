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

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ParentFixedExpressionTestCase<N extends ParentFixedExpression> extends ParentExpressionTestCase<N> {

    @Test
    public void testAppendChild() {
        assertThrows(UnsupportedOperationException.class, super::testAppendChild);
    }

    @Test
    public void testAppendChild2() {
        assertThrows(UnsupportedOperationException.class, super::testAppendChild2);
    }

    @Test
    public void testRemoveChildFirst() {
        assertThrows(UnsupportedOperationException.class, super::testRemoveChildFirst);
    }

    @Test
    public void testRemoveChildLast() {
        assertThrows(UnsupportedOperationException.class, super::testRemoveChildLast);
    }

    @Test
    public void testParentWithoutChild() {
        assertThrows(UnsupportedOperationException.class, super::testParentWithoutChild);
    }

    // ExpressionPurity.................................................................................................

    @Test
    public void testIsPure() {
        this.isPureAndCheck(
            this.createExpression(),
            this.context(),
            true
        );
    }

    // toString.........................................................................................................

    @Test
    public final void testToString() {
        this.toStringAndCheck(
            this.createExpression(),
            this.expectedToString()
        );
    }

    abstract String expectedToString();
}
