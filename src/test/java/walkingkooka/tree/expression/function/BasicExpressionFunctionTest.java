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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicExpressionFunctionTest implements ExpressionFunctionTesting<BasicExpressionFunction<String, FakeExpressionEvaluationContext>, String, FakeExpressionEvaluationContext> {

    private final static Optional<ExpressionFunctionName> NAME = Optional.of(
        ExpressionFunctionName.with("test123")
    );

    private final static boolean PURE = true;

    private final static ExpressionFunctionParameter<String> PARAMETER = ExpressionFunctionParameterName.with("param-0").variable(String.class);
    private final static IntFunction<List<ExpressionFunctionParameter<?>>> PARAMETERS = (n) -> Lists.of(
        PARAMETER
    );
    private final static Class<String> RETURN = String.class;
    private final static BiFunction<List<Object>, FakeExpressionEvaluationContext, String> BIF = (p, c) ->
        PARAMETER.getVariable(p, 0)
            .toString();

    @Test
    public void testWithNullNameFails() {
        this.withFails(
            null,
            PURE,
            PARAMETERS,
            RETURN,
            BIF
        );
    }

    @Test
    public void testWithNullParametersFails() {
        this.withFails(
            NAME,
            PURE,
            null,
            RETURN,
            BIF
        );
    }

    @Test
    public void testWithNullReturnTypeFails() {
        this.withFails(
            NAME,
            PURE,
            PARAMETERS,
            null,
            BIF
        );
    }

    @Test
    public void testWithNullBiFFails() {
        this.withFails(
            NAME,
            PURE,
            PARAMETERS,
            RETURN,
            null
        );
    }

    private void withFails(final Optional<ExpressionFunctionName> name,
                           final boolean pure,
                           final IntFunction<List<ExpressionFunctionParameter<?>>> parameters,
                           final Class<String> returnType,
                           final BiFunction<List<Object>, FakeExpressionEvaluationContext, String> biFunction) {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionFunction.with(
                name,
                pure,
                parameters,
                returnType,
                biFunction
            )
        );
    }

    @Test
    public void testSetNameSame() {
        final BasicExpressionFunction<String, FakeExpressionEvaluationContext> function = this.createBiFunction();
        assertSame(function, function.setName(NAME));
    }

    @Test
    public void testSetNameDifferent() {
        final BasicExpressionFunction<String, FakeExpressionEvaluationContext> function = this.createBiFunction();
        final Optional<ExpressionFunctionName> different = Optional.of(
            ExpressionFunctionName.with("different")
        );
        final ExpressionFunction<String, FakeExpressionEvaluationContext> differentFunction = function.setName(different);

        assertNotSame(function, differentFunction);
        assertSame(different, differentFunction.name());
        this.checkEquals(NAME, function.name());
    }

    @Test
    public void testApply() {
        this.applyAndCheck(
            Lists.of(1, 2.0),
            "[1, 2.0]"
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            NAME.get()
                .toString()
        );
    }

    @Test
    public void testToStringAnonymous() {
        this.toStringAndCheck(
            this.createBiFunction()
                .setName(Optional.empty()),
            "<anonymous>"
        );
    }

    @Override
    public BasicExpressionFunction<String, FakeExpressionEvaluationContext> createBiFunction() {
        return BasicExpressionFunction.with(
            NAME,
            PURE,
            PARAMETERS,
            RETURN,
            BIF
        );
    }

    @Override
    public FakeExpressionEvaluationContext createContext() {
        return new FakeExpressionEvaluationContext();
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }

    @Override
    public Class<BasicExpressionFunction<String, FakeExpressionEvaluationContext>> type() {
        return Cast.to(BasicExpressionFunction.class);
    }
}
