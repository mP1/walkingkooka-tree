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

package walkingkooka.tree.select;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class AllNodeSelectorContext2Test extends NodeSelectorContext2TestCase<AllNodeSelectorContext2<TestNode, StringName, StringName, Object>,
        TestNode, StringName, StringName, Object> {

    @Test
    public void testAll() {
        final AllNodeSelectorContext2<TestNode, StringName, StringName, Object> context = this.createContext();
        assertSame(context, context.all());
    }

    @Test
    public void testExpression() {
        final AllNodeSelectorContext2<TestNode, StringName, StringName, Object> context = this.createContext();
        this.checkType(context.expression(), ExpressionNodeSelectorNodeSelectorContext2.class);
    }

    @Test
    public void testExpressionCreateIfNecessary() {
        final AllNodeSelectorContext2<TestNode, StringName, StringName, Object> context = this.createContext();
        this.checkType(context.expressionCreateIfNecessary(), ExpressionNodeSelectorNodeSelectorContext2.class);
    }

    @Test
    public void testToString() {
        final String toString = "Context123";
        this.toStringAndCheck(AllNodeSelectorContext2.with(this.contextWithToString(toString)), toString);
    }

    @Override
    public AllNodeSelectorContext2<TestNode, StringName, StringName, Object> createContext() {
        return AllNodeSelectorContext2.with(null);
    }

    @Override
    public Class<AllNodeSelectorContext2<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(AllNodeSelectorContext2.class);
    }
}
