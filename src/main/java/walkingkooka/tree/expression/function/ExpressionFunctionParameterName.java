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

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

/**
 * The name of an {@link ExpressionFunctionParameter}. A name is case sensitive and must start with a letter, followed
 * by letters/numbers or dashes.
 */
public final class ExpressionFunctionParameterName implements Name,
        Comparable<ExpressionFunctionParameterName> {

    public static ExpressionFunctionParameterName with(final String name) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(name,
                "name",
                INITIAL,
                PART);
        return new ExpressionFunctionParameterName(name);
    }

    private final static CharPredicate INITIAL = CharPredicates.letter();
    private final static CharPredicate PART = CharPredicates.letterOrDigit().or(CharPredicates.any("-"));

    // @VisibleForTesting
    private ExpressionFunctionParameterName(final String name) {
        this.name = name;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    /**
     * Factory that creates a {@link ExpressionFunctionParameter} with this name and the given {@link Class type}.
     */
    public <T> ExpressionFunctionParameter<T> setType(final Class<T> type) {
        return ExpressionFunctionParameter.with(this, type);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return CASE_SENSITIVITY.hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof ExpressionFunctionParameterName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final ExpressionFunctionParameterName other) {
        return CASE_SENSITIVITY.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return this.name;
    }

    // Comparable ...................................................................................................

    @Override
    public int compareTo(final ExpressionFunctionParameterName other) {
        return CASE_SENSITIVITY.comparator().compare(this.name, other.name);
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;
}
