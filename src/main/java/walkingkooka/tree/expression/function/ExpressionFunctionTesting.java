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
import walkingkooka.logging.HasLoggerPathTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.util.BiFunctionTesting;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mixing interface that provides methods to test a {@link ExpressionFunction}
 */
public interface ExpressionFunctionTesting extends BiFunctionTesting,
    HasLoggerPathTesting {

    // apply.............................................................................................................

    default <RR, CC extends ExpressionEvaluationContext> void applyAndCheck2(final ExpressionFunction<RR, CC> function,
                                                                             final List<Object> parameters,
                                                                             final CC context,
                                                                             final RR result) {
        for (final ExpressionFunctionParameter<?> parameter : function.parameters(parameters.size())) {
            for (final ExpressionFunctionParameterKind kind : parameter.kinds()) {
                switch (kind) {
                    case FLATTEN:
                        this.checkEquals(
                            Lists.empty(),
                            parameters.stream()
                                .filter(List.class::isInstance)
                                .collect(Collectors.toList()
                                ),
                            () -> "Should not include parameter(s) of type " + List.class.getName()
                        );
                        break;
                    case RESOLVE_REFERENCES:
                        this.checkEquals(
                            Lists.empty(),
                            parameters.stream()
                                .filter(ExpressionReference.class::isInstance)
                                .collect(Collectors.toList()
                                ),
                            () -> "Should not include parameter(s) of type " + ExpressionReference.class.getName()
                        );
                        break;
                    default:
                        break;
                }
            }
        }

        this.checkEquals(
            result,
            function.apply(
                parameters,
                context
            ),
            () -> "Wrong result for " +
                function +
                " for params: " +
                parameters.stream()
                    .map(CharSequences::quoteIfChars)
                    .collect(Collectors.joining(", "))
        );
    }
}
