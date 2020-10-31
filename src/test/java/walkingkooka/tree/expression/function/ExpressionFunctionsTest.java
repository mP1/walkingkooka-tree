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
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.lang.reflect.Method;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ExpressionFunctionsTest implements PublicStaticHelperTesting<ExpressionFunctions> {

    @Test
    public void testVisit() {
        final Set<FunctionExpressionName> names = Sets.sorted();
        ExpressionFunctions.visit((e) -> names.add(e.name()));

        final Set<String> methods = Arrays.stream(ExpressionFunctions.class.getDeclaredMethods())
                .filter(m -> m.getReturnType() == ExpressionFunction.class && false == m.getName().equals("fake"))
                .map(Method::getName)
                .collect(Collectors.toCollection(Sets::sorted));
        assertEquals(methods.size(),
                names.size(),
                () -> methods.toString());
        assertEquals(true, names.contains(ExpressionFunctions.booleanFunction().name()));
        assertEquals(true, names.contains(ExpressionFunctions.trueFunction().name()));
    }

    @Test
    public void testPublicStaticMethodsWithoutMathContextParameter() {
        this.publicStaticMethodParametersTypeCheck(MathContext.class);
    }

    @Override
    public Class<ExpressionFunctions> type() {
        return ExpressionFunctions.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
