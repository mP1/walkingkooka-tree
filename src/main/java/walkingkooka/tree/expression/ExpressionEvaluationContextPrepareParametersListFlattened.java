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
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A partially lazy list, note values are evaluated and references resolved if necessary but the convert to the actual
 * parameter type is performed when the parameter is gotten.
 */
final class ExpressionEvaluationContextPrepareParametersListFlattened extends ExpressionEvaluationContextPrepareParametersList {

    static ExpressionEvaluationContextPrepareParametersListFlattened withFlattened(final List<ExpressionFunctionParameter<?>> parameters,
                                                                                   final List<Object> values,
                                                                                   final int preparedValuesCount,
                                                                                   final ExpressionFunctionParameter last,
                                                                                   final ExpressionEvaluationContext context) {
        return new ExpressionEvaluationContextPrepareParametersListFlattened(
                parameters,
                values,
                preparedValuesCount,
                last,
                context
        );
    }

    /**
     * Private ctor
     */
    private ExpressionEvaluationContextPrepareParametersListFlattened(final List<ExpressionFunctionParameter<?>> parameters,
                                                                      final List<Object> values,
                                                                      final int preparedValuesCount,
                                                                      final ExpressionFunctionParameter<?> last,
                                                                      final ExpressionEvaluationContext context) {
        super(
                parameters,
                values,
                preparedValuesCount,
                context
        );

        this.last = last;
    }

    @Override
    public Object get(final int index) {
        final int last = this.preparedValues.length;

        return index < last ?
                this.getPrepareIfNecessary(index) :
                this.getFlattenIfNecessary(index, index - last);
    }

    /**
     * Lazily flatten the values belonging to the last parameter, and convert on demand the individual element being fetched.
     */
    private Object getFlattenIfNecessary(final int index,
                                         final int filteredIndex) {
        Object preparedValue;

        final Object[] converted = this.convertedFlattenValues;
        if (null == converted) {
            this.flatten();
            final ExpressionFunctionParameter<?> parameter = this.last;

            preparedValue = prepareAndConvert(
                    parameter,
                    flattenValues.get(filteredIndex)
            );

            this.convertedFlattenValues[filteredIndex] = preparedValue;
        } else {
            preparedValue = converted[filteredIndex];

            if (MISSING == preparedValue) {
                final ExpressionFunctionParameter<?> parameter = this.last;

                preparedValue = prepareAndConvert(
                        parameter,
                        flattenValues.get(filteredIndex)
                );

                this.convertedFlattenValues[filteredIndex] = preparedValue;
            }
        }
        return preparedValue;
    }

    // @VisibleForTesting
    List<Object> flattenIfNecessary() {
        if (null == this.flattenValues) {
            this.flatten();
        }

        return this.flattenValues;
    }

    private void flatten() {
        final List<Object> flattenValues = Lists.array();

        this.flatten0(
                this.values.subList(
                        this.preparedValues.length,
                        this.values.size()
                ).iterator(),
                flattenValues
        );

        this.convertedFlattenValues = preparedValues(flattenValues.size());
        this.flattenValues = flattenValues;
    }

    private void flatten0(final Iterator<Object> values,
                          final Collection<Object> flattenValues) {
        while (values.hasNext()) {
            final Object value = prepareValue(
                    this.last,
                    values.next()
            );
            if (value instanceof Collection) {
                final Collection<Object> collection = Cast.to(value);
                this.flatten0(
                        collection.iterator(),
                        flattenValues
                );
                continue;
            } else {
                flattenValues.add(value);
            }
        }
    }

    /**
     * The last parameter being flattened.
     */
    private final ExpressionFunctionParameter<?> last;

    private List<Object> flattenValues;

    /**
     * A cache of the converted values from {@link #flattenValues}.
     */
    private Object[] convertedFlattenValues;

    @Override
    public int size() {
        return this.preparedValues.length + this.flattenIfNecessary().size();
    }
}
