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

import walkingkooka.Cast;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.function.ExpressionFunctionContextTesting;

public final class BasicNodeSelectorContextExpressionFunctionContextTest implements ExpressionFunctionContextTesting<BasicNodeSelectorContextExpressionFunctionContext> {

    @Override
    public BasicNodeSelectorContextExpressionFunctionContext createContext() {
        return BasicNodeSelectorContextExpressionFunctionContext.with(null, null, null);
    }

    @Override
    public Class<BasicNodeSelectorContextExpressionFunctionContext> type() {
        return Cast.to(BasicNodeSelectorContextExpressionFunctionContext.class);
    }
}
