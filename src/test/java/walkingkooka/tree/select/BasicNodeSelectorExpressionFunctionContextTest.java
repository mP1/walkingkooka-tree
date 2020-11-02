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

package walkingkooka.tree.select;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunctionContext;
import walkingkooka.tree.expression.function.ExpressionFunctionContextTesting;
import walkingkooka.tree.expression.function.ExpressionFunctionContexts;
import walkingkooka.tree.expression.function.FakeExpressionFunctionContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicNodeSelectorExpressionFunctionContextTest implements ExpressionFunctionContextTesting<BasicNodeSelectorExpressionFunctionContext<TestNode, StringName, StringName, Object>>,
        ToStringTesting<BasicNodeSelectorExpressionFunctionContext<TestNode, StringName, StringName, Object>> {

    private final static TestNode NODE = TestNode.with("testNode");
    private final static ExpressionFunctionContext CONTEXT = ExpressionFunctionContexts.fake();

    @Test
    public void testWithNullNodeFails() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorExpressionFunctionContext.with(null, CONTEXT));
    }

    @Test
    public void testWithNullContextFails() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorExpressionFunctionContext.with(NODE, null));
    }

    @Test
    public void testFunction() {
        final FunctionExpressionName name = FunctionExpressionName.with("custom");
        final List<Object> parameters = Lists.of(1, 2, 3);
        final Object result = "123";

        assertEquals(result,
                BasicNodeSelectorExpressionFunctionContext.with(NODE,
                        new FakeExpressionFunctionContext() {
                            @Override
                            public Object function(final FunctionExpressionName n,
                                                   final List<Object> p) {
                                assertEquals(name, n, "name");
                                assertEquals(parameters, p, "parameters");
                                return result;
                            }
                        }).function(name, parameters));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createContext(), NODE + " " + CONTEXT);
    }

    @Override
    public BasicNodeSelectorExpressionFunctionContext<TestNode, StringName, StringName, Object> createContext() {
        return BasicNodeSelectorExpressionFunctionContext.with(NODE, CONTEXT);
    }

    @Override
    public Class<BasicNodeSelectorExpressionFunctionContext<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(BasicNodeSelectorExpressionFunctionContext.class);
    }
}