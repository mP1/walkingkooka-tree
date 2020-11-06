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

import walkingkooka.ToStringTesting;
import walkingkooka.util.BiFunctionTesting;

import java.util.List;

public abstract class ParametersMapperExpressionFunctionBiFunctionTestCase2<F extends ParametersMapperExpressionFunctionBiFunction<FakeExpressionFunctionContext>>
        implements BiFunctionTesting<F, List<Object>, FakeExpressionFunctionContext, List<Object>>,
        ToStringTesting<F> {

    ParametersMapperExpressionFunctionBiFunctionTestCase2() {
        super();
    }

    final void applyAndCheck2(final List<Object> parameters) {
        this.applyAndCheck2(parameters, parameters);
    }

    final void applyAndCheck2(final List<Object> parameters,
                              final List<Object> expected) {
        this.applyAndCheck(parameters, this.createContext(), expected);
    }

    abstract FakeExpressionFunctionContext createContext();
}
