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

import walkingkooka.collect.list.ListTesting;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.Map;
import java.util.Optional;

public abstract class ExpressionEvaluationContextPrepareParametersListTestCase<T extends ExpressionEvaluationContextPrepareParametersList> implements ClassTesting<T>,
        ListTesting {

    final static ExpressionFunctionParameter<Integer> REQUIRED = ExpressionFunctionParameterName.with("required-integer")
            .required(Integer.class);

    final static ExpressionFunctionParameter<Long> OPTIONAL = ExpressionFunctionParameterName.with("optional-long")
            .optional(Long.class);

    final static ExpressionFunctionParameter<Double> VARIABLE = ExpressionFunctionParameterName.with("variable-double")
            .variable(Double.class);

    final static ExpressionFunctionParameter<?> FLATTEN = ExpressionFunctionParameterName.with("flatten")
            .variable(Object.class)
            .setKinds(
                    Sets.of(
                            ExpressionFunctionParameterKind.FLATTEN)
            );

    final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.DOUBLE;

    final static ExpressionNumber EXPRESSION_NUMBER = EXPRESSION_NUMBER_KIND.create(999);

    final static Expression EXPRESSION = Expression.value(EXPRESSION_NUMBER);

    final static ExpressionReference REFERENCE = new FakeExpressionReference() {
        @Override
        public String toString() {
            return "*reference*";
        }
    };

    ExpressionEvaluationContextPrepareParametersListTestCase() {
        super();
    }

    final ExpressionEvaluationContext createContextWithReference(final ExpressionReference reference,
                                                                 final Object value) {
        return new FakeExpressionEvaluationContext() {

            @Override
            public Optional<Object> reference(final ExpressionReference r) {
                return Optional.ofNullable(
                        reference.equals(r) ?
                                value :
                                null
                );
            }

            @Override
            public Object handleException(final RuntimeException exception) {
                return "@@@" + exception.getMessage();
            }

            @Override
            public String toString() {
                return REFERENCE + "->" + value;
            }
        };
    }

    final ExpressionEvaluationContext createContextWhichConverts(final Object from,
                                                                 final Object to) {
        return this.createContextWhichConverts(
                Maps.of(from, to)
        );
    }

    final ExpressionEvaluationContext createContextWhichConverts(final Map<Object, Object> conversions) {
        return new FakeExpressionEvaluationContext() {

            @Override
            public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                          final Object value) {
                return this.convertOrFail(value, parameter.type());
            }

            @Override
            public <T> T convertOrFail(final Object value,
                                       final Class<T> target) {
                final Object converted = conversions.get(value);
                if (null == converted) {
                    throw new UnsupportedOperationException("Unexpected convert from " + value + " to " + target.getName());
                }
                return target.cast(converted);
            }

            @Override
            public String toString() {
                return conversions.toString();
            }
        };
    }

    final ExpressionEvaluationContext createContextWhichConvertFails() {
        return new FakeExpressionEvaluationContext() {

            @Override
            public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                          final Object value) {
                return this.convertOrFail(value, parameter.type());
            }

            @Override
            public <T> T convertOrFail(final Object value,
                                       final Class<T> target) {
                throw new UnsupportedOperationException("Unable to convert " + value + " to " + target.getSimpleName());
            }

            @Override
            public Object handleException(final RuntimeException exception) {
                return "@@@" + exception.getMessage();
            }
        };
    }

    // ClassTesting....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
