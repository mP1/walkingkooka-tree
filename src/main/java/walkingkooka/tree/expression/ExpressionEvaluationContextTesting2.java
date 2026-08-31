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

package walkingkooka.tree.expression;

import org.junit.jupiter.api.Test;
import walkingkooka.convert.ConverterLikeTesting2;
import walkingkooka.environment.EnvironmentContextTesting2;
import walkingkooka.locale.LocaleContextTesting2;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctions;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixing testing interface for {@link ExpressionEvaluationContext}
 */
public interface ExpressionEvaluationContextTesting2<C extends ExpressionEvaluationContext> extends ExpressionEvaluationContextTesting,
    CanEvaluateExpressionTesting2<C>,
    CanEvaluateStringTesting2<C>,
    ConverterLikeTesting2<C>,
    EnvironmentContextTesting2<C>,
    ExpressionNumberContextTesting2<C>,
    ExpressionPurityContextTesting2<C>,
    LocaleContextTesting2<C> {

    // enterScope.......................................................................................................

    @Test
    default void testEnterScopeWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .enterScope(null)
        );
    }

    @Test
    default void testEnterScopeGivesDifferentInstance() {
        final C context = this.createContext();

        assertNotSame(
            context,
            context.enterScope(
                (n) -> {
                    throw new UnsupportedOperationException();
                }
            )
        );
    }

    // evaluateExpression...............................................................................................

    @Test
    default void testEvaluateExpressionUnknownFunctionNameFails() {
        assertThrows(
            UnknownExpressionFunctionException.class,
            () -> this.createContext()
                .evaluateExpression(
                    Expression.call(
                        Expression.namedFunction(
                            ExpressionFunctionName.with("unknown-function-123")
                        ),
                        Expression.NO_CHILDREN
                    )
                )
        );
    }

    // expressionFunction...............................................................................................

    @Test
    default void testExpressionFunctionWithNullFunctionNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .expressionFunction(null)
        );
    }

    @Test
    default void testEvaluateFunctionWithNullFunctionNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .evaluateFunction(
                    null,
                    ExpressionEvaluationContext.NO_PARAMETERS
                )
        );
    }

    @Test
    default void testEvaluateFunctionWithNullParametersFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .evaluateFunction(
                    ExpressionFunctions.fake(),
                    null
                )
        );
    }

    default <T> void evaluateFunctionAndCheck(final ExpressionFunction<T, C> function,
                                              final List<Object> parameters,
                                              final T expected) {

        this.evaluateFunctionAndCheck(
            this.createContext(),
            function,
            parameters,
            expected
        );
    }

    // reference........................................................................................................

    @Test
    default void testReferenceWithNullReferenceFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .reference(null)
        );
    }

    // EnvironmentContext...............................................................................................

    @Test
    @Override
    default void testSetLocaleWithNullFails() {
        LocaleContextTesting2.super.testSetLocaleWithNullFails();
    }

    // CanEvaluateExpression............................................................................................

    @Override
    default C createCanEvaluateExpression() {
        return this.createContext();
    }

    // CanEvaluateString................................................................................................

    @Override
    default C createCanEvaluateString() {
        return this.createContext();
    }

    // ExpressionEvaluationContext......................................................................................

    @Override
    default C createConverterLike() {
        return this.createContext();
    }

    // class............................................................................................................

    @Override
    default String typeNameSuffix() {
        return ExpressionEvaluationContext.class.getSimpleName();
    }
}
