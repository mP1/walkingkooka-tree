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

package walkingkooka.tree.expression.function;

import walkingkooka.Cast;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.util.FunctionTesting;

import java.util.Optional;

public final class ExpressionFunctionParameterDefaultValueTest implements FunctionTesting<ExpressionFunctionParameterDefaultValue<Integer>, ExpressionEvaluationContext, Optional<Integer>> {

    @Override
    public ExpressionFunctionParameterDefaultValue<Integer> createFunction() {
        return ExpressionFunctionParameterDefaultValue.with(
            Optional.of(123)
        );
    }

    // class............................................................................................................

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<ExpressionFunctionParameterDefaultValue<Integer>> type() {
        return Cast.to(ExpressionFunctionParameterDefaultValue.class);
    }
}
