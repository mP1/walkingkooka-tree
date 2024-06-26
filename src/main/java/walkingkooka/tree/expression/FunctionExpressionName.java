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

package walkingkooka.tree.expression;

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

import java.util.Comparator;

/**
 * The name of an {@link NamedFunctionExpression}.
 */
public final class FunctionExpressionName implements Name,
        Comparable<FunctionExpressionName> {

    /**
     * Factory that creates a new {@link FunctionExpressionName} after verifying the given characters are acceptable.
     */
    public static FunctionExpressionName with(final String name) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(
                name,
                "name",
                INITIAL,
                PART
        );
        return new FunctionExpressionName(name);
    }

    private final static CharPredicate INITIAL = CharPredicates.letter();
    private final static CharPredicate PART = CharPredicates.letterOrDigit()
            .or(CharPredicates.any("-._"));

    static FunctionExpressionName fromClass(final Class<? extends Expression> klass) {
        return new FunctionExpressionName(
                CharSequences.subSequence(
                        klass.getSimpleName(),
                        0,
                        -EXPRESSION_STRING_LENGTH
                ).toString()
        );
    }

    private final static int EXPRESSION_STRING_LENGTH = Expression.class.getSimpleName().length();

    // @VisibleForTesting
    private FunctionExpressionName(final String name) {
        this.name = name;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    /**
     * Useful to report that a function was not found within an expression.
     */
    public String notFoundText() {
        return "Function not found: " + CharSequences.quoteAndEscape(this.name);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return CASE_SENSITIVITY.hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof FunctionExpressionName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final FunctionExpressionName other) {
        return CASE_SENSITIVITY.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return this.name;
    }

    // Comparable ...................................................................................................

    @Override
    public int compareTo(final FunctionExpressionName other) {
        return CASE_SENSITIVITY.comparator().compare(this.name, other.name);
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;

    // Comparator.......................................................................................................

    /**
     * {@see FunctionExpressionNameComparator}
     */
    public static Comparator<FunctionExpressionName> comparator(final CaseSensitivity caseSensitivity) {
        return FunctionExpressionNameComparator.with(caseSensitivity);
    }
}
