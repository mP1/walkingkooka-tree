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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;

import java.math.BigDecimal;

public final class TypeNameExpressionFunctionTest implements ClassTesting2<TypeNameExpressionFunction<ExpressionEvaluationContext>>,
        ExpressionFunctionTesting<TypeNameExpressionFunction<ExpressionEvaluationContext>, String, ExpressionEvaluationContext> {

    @Test
    public void testBigDecimalParameter() {
        this.applyAndCheck2(parameters(BigDecimal.valueOf(123.5)),
                BigDecimal.class.getName());
    }

    @Test
    public void testThisTestParameter() {
        this.applyAndCheck2(parameters(this),
                this.getClass().getName());
    }

    @Override
    public ExpressionEvaluationContext createContext() {
        return ExpressionEvaluationContexts.fake();
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "typeName");
    }

    @Override
    public TypeNameExpressionFunction<ExpressionEvaluationContext> createBiFunction() {
        return TypeNameExpressionFunction.instance();
    }

    @Override
    public Class<TypeNameExpressionFunction<ExpressionEvaluationContext>> type() {
        return Cast.to(TypeNameExpressionFunction.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
