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

import walkingkooka.Cast;
import walkingkooka.collect.list.ListTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.tree.expression.function.FakeExpressionFunction;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class ExpressionEvaluationContextPrepareParametersListTestCase<T extends ExpressionEvaluationContextPrepareParametersList> implements ClassTesting<T>,
        ListTesting {

    ExpressionEvaluationContextPrepareParametersListTestCase() {
        super();
    }

    final static ExpressionFunctionParameter<Integer> VARIABLE = ExpressionFunctionParameterName.with("integer")
            .variable(Integer.class);

    final static ExpressionEvaluationContext CONTEXT = new FakeExpressionEvaluationContext() {

        @Override
        public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                      final Object value) {
            return Cast.to(value);
        }
    };

    final static ExpressionEvaluationContext CONTEXT_PARSE_INT = new FakeExpressionEvaluationContext() {

        @Override
        public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                      final Object value) {
            return Cast.to(Integer.parseInt((String) value));
        }

        @Override
        public Object handleException(final RuntimeException exception) {
            return "@@@" + exception.getMessage();
        }
    };

    final static ExpressionReference REFERENCE = new ExpressionReference() {
        @Override
        public String toString() {
            return "*reference*";
        }
    };

    final ExpressionFunction<Void, ExpressionEvaluationContext> function(final ExpressionFunctionKind... kinds) {
        return this.function(
                Lists.of(
                        ExpressionFunctionParameterName.with("parameters")
                                .variable(Object.class)
                ),
                kinds
        );
    }

    final ExpressionFunction<Void, ExpressionEvaluationContext> function(final List<ExpressionFunctionParameter<?>> parameters,
                                                                         final ExpressionFunctionKind... kinds) {
        return new FakeExpressionFunction<>() {

            @Override
            public List<ExpressionFunctionParameter<?>> parameters() {
                return parameters;
            }

            @Override
            public Set<ExpressionFunctionKind> kinds() {
                return Sets.of(kinds);
            }

            @Override
            public String toString() {
                return "parameters: " + parameters + " kinds: " + Arrays.toString(kinds);
            }
        };
    }

    // ClassTesting....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
