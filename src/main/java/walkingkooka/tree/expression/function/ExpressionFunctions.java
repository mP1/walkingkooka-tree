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

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.currency.CurrencyCode;
import walkingkooka.locale.LocaleLanguageTag;
import walkingkooka.naming.Name;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberFunction;
import walkingkooka.tree.select.NodeSelectorExpressionEvaluationContext;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * Collection of static factory methods for numerous {@link ExpressionFunction}.
 */
public final class ExpressionFunctions implements PublicStaticHelper {

    /**
     * Returns a {@link ExpressionFunction} that may or may not be case-sensitive when performing {@link ExpressionFunction} name lookups.
     * If the {@link Set} includes an anonymous {@link ExpressionFunction} (where {@link ExpressionFunction#name() returns @link Optional#empty()}.
     */
    public static <C extends ExpressionEvaluationContext> Function<ExpressionFunctionName, Optional<ExpressionFunction<?, C>>> lookup(final Set<ExpressionFunction<?, C>> functions,
                                                                                                                                      final CaseSensitivity caseSensitivity) {
        Objects.requireNonNull(caseSensitivity, "caseSensitivity");

        final Map<String, ExpressionFunction<?, C>> nameToFunctions = Maps.sorted(caseSensitivity.comparator());

        for (final ExpressionFunction<?, C> function : functions) {
            final String name = function.name()
                .orElseThrow(() -> new IllegalArgumentException("Anonymous functions are not supported"))
                .value();
            if (null != nameToFunctions.put(
                name,
                function
            )) {
                throw new IllegalArgumentException("Duplicate function " + CharSequences.quote(name));
            }
        }

        return (name) -> {
            Objects.requireNonNull(name, "name");

            return Optional.ofNullable(
                nameToFunctions.get(name.value())
            );
        };
    }

    /**
     * Visit all {@link ExpressionFunction functions}. Note this does not include the {@link #fake() fake {@link ExpressionFunction}}
     */
    public static void visit(final Consumer<ExpressionFunction<?, ?>> consumer) {
        Lists.of(
            currencyCode(),
            localeLanguageTag(),
            node(),
            nodeName(),
            nullFunction(),
            typeName()
        ).forEach(consumer);
    }

    /**
     * {@see TreeExpressionFunctionBasic}
     */
    public static <T, C extends ExpressionEvaluationContext> ExpressionFunction<T, C> basic(final Optional<ExpressionFunctionName> name,
                                                                                            final boolean pure,
                                                                                            final IntFunction<List<ExpressionFunctionParameter<?>>> parameters,
                                                                                            final Class<T> returnType,
                                                                                            final BiFunction<List<Object>, C, T> biFunction) {
        return TreeExpressionFunctionBasic.with(
            name,
            pure,
            parameters,
            returnType,
            biFunction
        );
    }

    /**
     * {@see TreeExpressionFunctionCurrencyCode}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<CurrencyCode, C> currencyCode() {
        return TreeExpressionFunctionCurrencyCode.instance();
    }

    /**
     * {@see TreeExpressionFunctionEval}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<Object, C> eval(final Expression expression) {
        return TreeExpressionFunctionEval.with(expression);
    }

    /**
     * {@see ExpressionNumberFunctionExpressionFunction}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<ExpressionNumber, C> expressionNumberFunction(final Optional<ExpressionFunctionName> name,
                                                                                                                           final ExpressionNumberFunction function) {
        return ExpressionNumberFunctionExpressionFunction.with(
            name,
            function
        );
    }

    /**
     * {@see FakeExpressionFunction}
     */
    public static <T, C extends ExpressionEvaluationContext> ExpressionFunction<T, C> fake() {
        return new FakeExpressionFunction<>();
    }

    /**
     * {@see TreeExpressionFunctionLambda}
     */
    public static <T, C extends ExpressionEvaluationContext> ExpressionFunction<T, C> lambda(final List<ExpressionFunctionParameter<?>> parameters,
                                                                                             final Class<T> returnType,
                                                                                             final Expression expression) {
        return TreeExpressionFunctionLambda.with(
            parameters,
            returnType,
            expression
        );
    }

    /**
     * {@see TreeExpressionFunctionLocaleLanguageTag}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<LocaleLanguageTag, C> localeLanguageTag() {
        return TreeExpressionFunctionLocaleLanguageTag.instance();
    }

    /**
     * {@see TreeExpressionFunctionNode}
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE,
        C extends NodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE>> ExpressionFunction<N, C> node() {
        return TreeExpressionFunctionNode.instance();
    }

    /**
     * {@see TreeExpressionFunctionNodeName}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<String, C> nodeName() {
        return TreeExpressionFunctionNodeName.instance();
    }

    /**
     * {@see TreeExpressionFunctionNull}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<Object, C> nullFunction() {
        return TreeExpressionFunctionNull.instance();
    }

    /**
     * {@see TreeExpressionFunctionTypeName}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<String, C> typeName() {
        return TreeExpressionFunctionTypeName.instance();
    }

    /**
     * Stops creation
     */
    private ExpressionFunctions() {
        throw new UnsupportedOperationException();
    }
}
