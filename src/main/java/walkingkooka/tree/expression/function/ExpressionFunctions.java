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

import walkingkooka.reflect.PublicStaticHelper;

/**
 * Collection of static factory methods for numerous {@link ExpressionFunction}.
 */
public final class ExpressionFunctions implements PublicStaticHelper {

    /**
     * {@see BooleanExpressionFunction}
     */
    public static ExpressionFunction<Boolean> booleanFunction() {
        return BooleanExpressionFunction.INSTANCE;
    }

    /**
     * {@see ChooseExpressionFunction}
     */
    public static ExpressionFunction<Object> choose() {
        return ChooseExpressionFunction.INSTANCE;
    }

    /**
     * {@see FakeExpressionFunction}
     */
    public static <T> ExpressionFunction<T> fake() {
        return new FakeExpressionFunction();
    }

    /**
     * {@see FalseExpressionFunction}
     */
    public static ExpressionFunction<Boolean> falseFunction() {
        return FalseExpressionFunction.INSTANCE;
    }

    /**
     * {@see FunctionExpressionNameExpressionFunction}
     */
    public static ExpressionFunction<String> nodeName() {
        return FunctionExpressionNameExpressionFunction.INSTANCE;
    }

    /**
     * {@see NotExpressionFunction}
     */
    public static ExpressionFunction<Boolean> not() {
        return NotExpressionFunction.INSTANCE;
    }

    /**
     * {@see TrueExpressionFunction}
     */
    public static ExpressionFunction<Boolean> trueFunction() {
        return TrueExpressionFunction.INSTANCE;
    }

    /**
     * {@see TypeNameExpressionFunction}
     */
    public static ExpressionFunction<String> typeName() {
        return TypeNameExpressionFunction.INSTANCE;
    }

    /**
     * Stops creation
     */
    private ExpressionFunctions() {
        throw new UnsupportedOperationException();
    }
}
