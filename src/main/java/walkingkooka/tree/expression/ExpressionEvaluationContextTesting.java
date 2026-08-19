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

import walkingkooka.convert.ConverterLikeTesting;
import walkingkooka.environment.EnvironmentContextTesting;
import walkingkooka.locale.LocaleContextTesting;
import walkingkooka.reflect.ThrowableTesting;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixing testing interface for {@link ExpressionEvaluationContext}
 */
public interface ExpressionEvaluationContextTesting extends
    CanEvaluateExpressionTesting,
    ConverterLikeTesting,
    EnvironmentContextTesting,
    ExpressionNumberContextTesting,
    ExpressionPurityContextTesting,
    LocaleContextTesting,
    ThrowableTesting,
    TreePrintableTesting {

    // evaluateExpression...............................................................................................

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

    default <C extends ExpressionEvaluationContext, T> void evaluateFunctionAndCheck(final C context,
                                                                                     final ExpressionFunction<T, C> function,
                                                                                     final List<Object> parameters,
                                                                                     final T expected) {
        this.checkEquals(
            expected,
            context.evaluateFunction(
                function,
                parameters
            ),
            () -> "evaluate " + function + " " + parameters
        );
    }

    // reference........................................................................................................

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

        this.getMessageAndCheck(
            thrown,
            expected.getMessage()
        );
    }
}
