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
import walkingkooka.compare.ComparisonRelation;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;

/**
 * A function that compares two parameters of the same value. Before comparing the second value is coverted to the same
 * type as the first.
 */
final class ComparisonExpressionFunction<C extends ExpressionFunctionContext> implements ExpressionFunction<Boolean, C> {

    static <C extends ExpressionFunctionContext> ComparisonExpressionFunction<C> equals() {
        return Cast.to(EQ);
    }

    private final static ComparisonExpressionFunction<ExpressionFunctionContext> EQ = new ComparisonExpressionFunction<ExpressionFunctionContext>("equals", ComparisonRelation.EQ);

    static <C extends ExpressionFunctionContext> ComparisonExpressionFunction<C> notEquals() {
        return Cast.to(NE);
    }

    private final static ComparisonExpressionFunction<ExpressionFunctionContext> NE = new ComparisonExpressionFunction<ExpressionFunctionContext>("not-equals", ComparisonRelation.NE);

    static <C extends ExpressionFunctionContext> ComparisonExpressionFunction<C> greaterThan() {
        return Cast.to(GT);
    }

    private final static ComparisonExpressionFunction<ExpressionFunctionContext> GT = new ComparisonExpressionFunction<ExpressionFunctionContext>("greater-than", ComparisonRelation.GT);

    static <C extends ExpressionFunctionContext> ComparisonExpressionFunction<C> greaterThanEqual() {
        return Cast.to(GTE);
    }

    private final static ComparisonExpressionFunction<ExpressionFunctionContext> GTE = new ComparisonExpressionFunction<ExpressionFunctionContext>("greater-than-equal", ComparisonRelation.GTE);

    static <C extends ExpressionFunctionContext> ComparisonExpressionFunction<C> lessThan() {
        return Cast.to(LT);
    }

    private final static ComparisonExpressionFunction<ExpressionFunctionContext> LT = new ComparisonExpressionFunction<ExpressionFunctionContext>("less-than", ComparisonRelation.LT);

    static <C extends ExpressionFunctionContext> ComparisonExpressionFunction<C> lessThanEqual() {
        return Cast.to(LTE);
    }

    private final static ComparisonExpressionFunction<ExpressionFunctionContext> LTE = new ComparisonExpressionFunction<ExpressionFunctionContext>("less-than-equal", ComparisonRelation.LTE);

    /**
     * Private ctor
     */
    private ComparisonExpressionFunction(final String name, final ComparisonRelation relation) {
        super();
        this.name = FunctionExpressionName.with(name);
        this.relation = relation;
    }

    @Override
    public Boolean apply(final List<Object> parameters,
                         final C context) {
        this.checkOnlyRequiredParameters(parameters);

        final Comparable<?> first = FIRST.getOrFail(parameters, 0);
        final Comparable<?> second = SECOND.getOrFail(parameters, 1);

        return this.relation.predicate(
                Cast.to(second)).test(Cast.to(first)
        );
    }

    private final ComparisonRelation relation;

    @Override
    public FunctionExpressionName name() {
        return name;
    }

    private final FunctionExpressionName name;

    @Override
    public List<ExpressionFunctionParameter<?>> parameters() {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<Comparable> FIRST = ExpressionFunctionParameterName.with("first")
            .setType(Comparable.class);

    private final static ExpressionFunctionParameter<Comparable> SECOND = ExpressionFunctionParameterName.with("second")
            .setType(Comparable.class);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(FIRST, SECOND);

    @Override
    public boolean lsLastParameterVariable() {
        return false;
    }

    @Override
    public boolean resolveReferences() {
        return false;
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return true;
    }

    @Override
    public String toString() {
        return this.name().toString();
    }
}
