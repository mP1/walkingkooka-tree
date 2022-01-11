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

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * The name of an {@link ExpressionFunctionParameter}. A name is case sensitive and must start with a letter, followed
 * by letters/numbers or dashes.
 */
public final class ExpressionFunctionParameterName implements Name,
        Comparable<ExpressionFunctionParameterName> {

    private final static CharPredicate INITIAL = CharPredicates.letter();
    private final static CharPredicate PART = CharPredicates.letterOrDigit().or(CharPredicates.any("-"));

    private final static Map<String, ExpressionFunctionParameterName> CONSTANTS = new TreeMap<>();

    public final static ExpressionFunctionParameterName BOOLEAN = register("boolean");

    public final static ExpressionFunctionParameterName CHARACTER = register("character");

    public final static ExpressionFunctionParameterName DATE = register("date");

    public final static ExpressionFunctionParameterName DATETIME = register("date-time");

    public final static ExpressionFunctionParameterName NUMBER = register("number");

    public final static ExpressionFunctionParameterName REFERENCE = register("reference");

    public final static ExpressionFunctionParameterName STRING = register("string");

    public final static ExpressionFunctionParameterName TEXT = register("text");

    public final static ExpressionFunctionParameterName TIME = register("time");

    public final static ExpressionFunctionParameterName VALUE = register("value");

    /**
     * Used to create and then register constants.
     */
    private static ExpressionFunctionParameterName register(final String name) {
        final ExpressionFunctionParameterName created = create(name);
        CONSTANTS.put(name, created);
        return created;
    }

    /**
     * Factory that returns a {@link ExpressionFunctionParameterName} with the given {@link String name}.
     */
    public static ExpressionFunctionParameterName with(final String name) {
        Objects.requireNonNull(
                name,
                "name"
        );

        final ExpressionFunctionParameterName constant = CONSTANTS.get(name);
        return null != constant ?
                constant :
                create(name);
    }

    private static ExpressionFunctionParameterName create(final String name) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(
                name,
                "name",
                INITIAL,
                PART
        );
        return new ExpressionFunctionParameterName(name);
    }

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
     * Factory that creates a REQUIRED {@link ExpressionFunctionParameter} with this name and the given {@link Class type}.
     */
    public <T> ExpressionFunctionParameter<T> required(final Class<T> type) {
        return ExpressionFunctionParameter.with(
                this,
                type,
                ExpressionFunctionParameterCardinality.REQUIRED
        );
    }

    /**
     * Factory that creates a OPTIONAL {@link ExpressionFunctionParameter} with this name and the given {@link Class type}.
     */
    public <T> ExpressionFunctionParameter<T> optional(final Class<T> type) {
        return ExpressionFunctionParameter.with(
                this,
                type,
                ExpressionFunctionParameterCardinality.OPTIONAL
        );
    }

    /**
     * Factory that creates a VARIABLE {@link ExpressionFunctionParameter} with this name and the given {@link Class type}.
     */
    public <T> ExpressionFunctionParameter<T> variable(final Class<T> type) {
        return ExpressionFunctionParameter.with(
                this,
                type,
                ExpressionFunctionParameterCardinality.VARIABLE
        );
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
