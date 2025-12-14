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

package walkingkooka.tree.expression;

import org.junit.jupiter.api.Test;
import walkingkooka.convert.CanConvertTesting;
import walkingkooka.environment.EnvironmentContextTesting2;
import walkingkooka.locale.LocaleContextTesting2;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctions;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixing testing interface for {@link ExpressionEvaluationContext}
 */
public interface ExpressionEvaluationContextTesting<C extends ExpressionEvaluationContext> extends
    CanConvertTesting<C>,
    EnvironmentContextTesting2<C>,
    ExpressionNumberContextTesting<C>,
    ExpressionPurityContextTesting<C>,
    LocaleContextTesting2<C>,
    TreePrintableTesting {

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

    // evaluate.........................................................................................................

    @Test
    default void testEvaluateWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .evaluate(null)
        );
    }

    default void evaluateAndCheck(final C context,
                                  final String text,
                                  final Object expected) {
        this.checkEquals(
            expected,
            context.evaluate(text),
            text
        );
    }

    // evaluateExpression...............................................................................................

    @Test
    default void testEvaluateExpressionWithNullExpressionFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext().evaluateExpression(null)
        );
    }

    @Test
    default void testEvaluateExpressionUnknownFunctionNameFails() {
        assertThrows(
            UnknownExpressionFunctionException.class,
            () -> this.createContext()
                .evaluateExpression(
                    Expression.call(
                        Expression.namedFunction(
                            ExpressionFunctionName.with("unknown-namedFunction-123")
                        ),
                        Expression.NO_CHILDREN
                    )
                )
        );
    }

    default void evaluateExpressionAndCheck(final Expression expression,
                                            final Object value) {
        this.evaluateExpressionAndCheck(this.createContext(), expression, value);
    }

    default void evaluateExpressionAndCheck(final C context,
                                            final Expression expression,
                                            final Object value) {
        this.checkEquals(value,
            context.evaluateExpression(expression),
            () -> "evaluate " + expression + " " + context);
    }

    default void toValueAndCheck(final Expression node,
                                 final ExpressionEvaluationContext context,
                                 final Object value) {
        this.checkEquals(
            value,
            node.toValue(context),
            () -> "Expression.toValue failed, node=" + node + " context=" + context
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

    default <T> void evaluateFunctionAndCheck(final C context,
                                              final ExpressionFunction<T, C> function,
                                              final List<Object> parameters,
                                              final T expected) {
        this.checkEquals(
            expected,
            context.evaluateFunction(function, parameters),
            () -> "evaluate " + function + " " + parameters
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

    default void referenceAndCheck(final ExpressionEvaluationContext context,
                                   final ExpressionReference reference) {
        this.referenceAndCheck2(
            context,
            reference,
            Optional.empty()
        );
    }

    default void referenceAndCheck(final ExpressionEvaluationContext context,
                                   final ExpressionReference reference,
                                   final Object expected) {
        this.referenceAndCheck(
            context,
            reference,
            Optional.of(expected)
        );
    }

    default void referenceAndCheck(final ExpressionEvaluationContext context,
                                   final ExpressionReference reference,
                                   final Optional<Object> expected) {
        this.referenceAndCheck2(
            context,
            reference,
            Optional.of(expected)
        );
    }

    default void referenceAndCheck2(final ExpressionEvaluationContext context,
                                    final ExpressionReference reference,
                                    final Optional<Optional<Object>> expected) {
        this.checkEquals(
            expected,
            context.reference(reference),
            () -> "reference " + reference
        );
    }

    default void referenceFails(final ExpressionEvaluationContext context,
                                final ExpressionReference reference,
                                final RuntimeException expected) {
        final RuntimeException thrown = assertThrows(
            expected.getClass(),
            () -> context.reference(reference)
        );

        this.checkEquals(
            expected.getMessage(),
            thrown.getMessage(),
            () -> "reference " + reference
        );
    }

    // EnvironmentContext...............................................................................................

    @Test
    @Override
    default void testSetLocaleWithNullFails() {
        LocaleContextTesting2.super.testSetLocaleWithNullFails();
    }

    // ExpressionEvaluationContext......................................................................................

    @Override
    default C createCanConvert() {
        return this.createContext();
    }

    // class............................................................................................................

    @Override
    default String typeNameSuffix() {
        return ExpressionEvaluationContext.class.getSimpleName();
    }
}
