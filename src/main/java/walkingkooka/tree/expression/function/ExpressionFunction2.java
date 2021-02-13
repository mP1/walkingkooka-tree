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

import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.select.NodeSelectorException;

import java.util.List;

/**
 * Base class for many {@link ExpressionFunction} within this package.
 */
abstract class ExpressionFunction2<T, C extends ExpressionFunctionContext> implements ExpressionFunction<T, C> {

    /**
     * Package private to limit sub classing.
     */
    ExpressionFunction2() {
        super();
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return true;
    }

    /**
     * Always resolve references to values.
     */
    @Override
    public final boolean resolveReferences() {
        return true;
    }

    /**
     * Checks and complains if the parameter count doesnt match the expected count.
     */
    final void checkParameterCount(final List<Object> parameters, final int expectedCount) {
        final int count = parameters.size();
        if (expectedCount != count) {
            throw new IllegalArgumentException("Expected " + expectedCount + " but got " + count + "=" + parameters);
        }
    }

    /**
     * Converts a value into a boolean.
     */
    final boolean booleanValue(final Object value, final C context) {
        return context.convertOrFail(value, Boolean.class);
    }

    /**
     * Converts a value into a {@link Comparable} with type parameters.
     */
    final Comparable comparable(final Object value, final C context) {
        return context.convertOrFail(value, Comparable.class);
    }

    /**
     * Type safe {@link Boolean} parameter getter.
     */
    final Boolean booleanValue(final List<?> parameters, final int i, final C context) {
        return this.booleanValue(this.parameter(parameters, i), context);
    }

    /**
     * Type safe {@link Comparable} parameter getter.
     */
    final Comparable comparable(final List<?> parameters, final int i, final C context) {
        return this.comparable(this.parameter(parameters, i), context);
    }

    /**
     * Retrieves the parameter at the index or throws a nice exception message.
     */
    final <TT> TT parameter(final List<?> parameters, final int i, final Class<TT> type, final C context) {
        return context.convertOrFail(this.parameter(parameters, i), type);
    }

    /**
     * Retrieves the parameter at the index or throws a nice exception message.
     */
    final Object parameter(final List<?> parameters, final int i) {
        final int count = parameters.size();
        if (i < 0 || i >= count) {
            throw new NodeSelectorException("Parameter " + i + " missing from " + parameters);
        }
        return parameters.get(i);
    }

    @Override
    public abstract String toString();
}
