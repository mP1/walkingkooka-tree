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
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionFunctionName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionCustomNameTest extends ExpressionFunctionTestCase<ExpressionFunctionCustomName<String, ExpressionEvaluationContext>,
        ExpressionEvaluationContext,
        String> {

    private final static Optional<ExpressionFunctionName> NAME = Optional.of(
            ExpressionFunctionName.with("Custom")
    );

    @Test
    public void testWithNullFunctionFails() {
        assertThrows(NullPointerException.class, () -> ExpressionFunctionCustomName.with(null, NAME));
    }

    @Test
    public void testWithNullNameFails() {
        assertThrows(NullPointerException.class, () -> ExpressionFunctionCustomName.with(wrapped(), null));
    }

    @Test
    public void testApply() {
        final List<Object> parameters = Lists.of(this);
        final ExpressionEvaluationContext context = this.createContext();
        this.applyAndCheck(this.createBiFunction(), parameters, context,
                this.wrapped().apply(parameters, context));
    }

    @Test
    public void testSetNameSame() {
        final ExpressionFunctionCustomName<String, ExpressionEvaluationContext> function = this.createBiFunction();
        assertSame(function, function.setName(NAME));
    }

    @Test
    public void testSetNameDifferent() {
        final ExpressionFunctionCustomName<String, ExpressionEvaluationContext> function = this.createBiFunction();
        final Optional<ExpressionFunctionName> different = Optional.of(
                ExpressionFunctionName.with("different")
        );
        final ExpressionFunction<String, ExpressionEvaluationContext> differentFunction = function.setName(different);

        assertNotSame(function, differentFunction);
        assertSame(different, differentFunction.name());
        this.checkEquals(NAME, function.name());
    }

    @Override
    public ExpressionFunctionCustomName<String, ExpressionEvaluationContext> createBiFunction() {
        return Cast.to(ExpressionFunctionCustomName.with(wrapped(), NAME));
    }

    private ExpressionFunction<String, ExpressionEvaluationContext> wrapped() {
        return ExpressionFunctions.typeName();
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }

    @Override
    public ExpressionEvaluationContext createContext() {
        return ExpressionEvaluationContexts.fake();
    }

    @Override
    public Class<ExpressionFunctionCustomName<String, ExpressionEvaluationContext>> type() {
        return Cast.to(ExpressionFunctionCustomName.class);
    }
}
