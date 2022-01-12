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

public enum ExpressionFunctionParameterCardinality {

    REQUIRED("", 1, 1),

    OPTIONAL("?", 0, 1),

    VARIABLE("*", 0, Integer.MAX_VALUE); // only legal for last parameter

    ExpressionFunctionParameterCardinality(final String parameterToString,
                                           final int min,
                                           final int max) {
        this.parameterToString = parameterToString;
        this.min = min;
        this.max = max;
    }

    void get(final ExpressionFunctionParameter<?> parameter) {
        if (this != OPTIONAL) {
            this.fail(parameter);
        }
    }

    void getOrFail(final ExpressionFunctionParameter<?> parameter) {
        if (this != REQUIRED) {
            this.fail(parameter);
        }
    }

    final void fail(final ExpressionFunctionParameter<?> parameter) {
        throw new RuntimeException(parameter + " is not " + this.name().toLowerCase());
    }

    final int min;

    final int max;

    /**
     * The last component appended to {@link ExpressionFunctionParameter#toString()}
     */
    final String parameterToString;
}
