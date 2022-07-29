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

import walkingkooka.Cast;
import walkingkooka.Context;
import walkingkooka.convert.ConverterContext;
import walkingkooka.tree.expression.ExpressionNumberContext;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;
import java.util.Optional;

/**
 * A function that returns {@link Object#getClass()#getName()} of the first parameter.
 */
final class TypeNameExpressionFunction<C extends Context & ConverterContext & ExpressionNumberContext> implements ExpressionFunction<String, C> {

    /**
     * Instance getter.
     */
    static <C extends Context & ConverterContext & ExpressionNumberContext> TypeNameExpressionFunction<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private static final TypeNameExpressionFunction<?> INSTANCE = new TypeNameExpressionFunction<>();

    /**
     * Private ctor
     */
    private TypeNameExpressionFunction() {
        super();
    }

    @Override
    public Optional<FunctionExpressionName> name() {
        return NAME;
    }

    private final static Optional<FunctionExpressionName> NAME = Optional.of(
            FunctionExpressionName.with("typeName")
    );

    @Override
    public String apply(final List<Object> parameters,
                        final C context) {
        this.checkParameterCount(parameters);

        return PARAMETER.getOrFail(parameters, 0)
                .getClass()
                .getName();
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<Object> PARAMETER = ExpressionFunctionParameterName.VALUE
            .required(Object.class)
            .setKinds(ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(PARAMETER);

    @Override
    public Class<String> returnType() {
        return String.class;
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return true;
    }

    @Override
    public String toString() {
        return this.name()
                .get()
                .toString();
    }
}
