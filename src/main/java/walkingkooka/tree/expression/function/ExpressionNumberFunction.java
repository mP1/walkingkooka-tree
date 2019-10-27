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

package walkingkooka.tree.expression.function;

import walkingkooka.tree.expression.ExpressionNodeName;

import java.util.List;

/**
 * A function that converts the given value into a {@link Number}.
 */
final class ExpressionNumberFunction extends ExpressionFunction2<Number> {
    /**
     * Singleton
     */
    static final ExpressionNumberFunction INSTANCE = new ExpressionNumberFunction();

    /**
     * Private ctor
     */
    private ExpressionNumberFunction() {
        super();
    }

    @Override
    public Number apply(final List<Object> parameters,
                        final ExpressionFunctionContext context) {
        this.checkParameterCount(parameters, 1);

        return this.number(parameters, 0, context);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    private final static ExpressionNodeName NAME = ExpressionNodeName.with("number");

    @Override
    public String toString() {
        return this.name().toString();
    }
}
