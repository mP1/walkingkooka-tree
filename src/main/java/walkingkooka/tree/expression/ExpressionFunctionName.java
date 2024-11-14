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
import walkingkooka.InvalidTextLengthException;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.Parsers;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * The name of an {@link NamedFunctionExpression}. Note initially {@link ExpressionFunctionName#caseSensitivity()} is
 * {@link CaseSensitivity#SENSITIVE}, and can be changed by calling {@link ExpressionFunctionName#setCaseSensitivity(CaseSensitivity)}.
 */
public final class ExpressionFunctionName implements Name,
        Comparable<ExpressionFunctionName> {

    /**
     * When initially created {@link ExpressionFunctionName#caseSensitivity} is {@link CaseSensitivity#SENSITIVE}.
     */
    public final static CaseSensitivity DEFAULT_CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;

    /**
     * Factory that creates a new {@link ExpressionFunctionName} after verifying the given characters are acceptable.
     */
    public static ExpressionFunctionName with(final String name) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(
                name,
                "name",
                INITIAL,
                PART
        );
        return new ExpressionFunctionName(
                name,
                DEFAULT_CASE_SENSITIVITY
        );
    }

    private final static CharPredicate INITIAL = CharPredicates.letter();
    private final static CharPredicate PART = CharPredicates.letterOrDigit()
            .or(CharPredicates.any("-._"));

    static ExpressionFunctionName fromClass(final Class<? extends Expression> klass) {
        return new ExpressionFunctionName(
                CharSequences.subSequence(
                        klass.getSimpleName(),
                        0,
                        -EXPRESSION_STRING_LENGTH
                ).toString(),
                DEFAULT_CASE_SENSITIVITY
        );
    }

    private final static int EXPRESSION_STRING_LENGTH = Expression.class.getSimpleName().length();

    /**
     * The minimum length of function names. Difficult to find minimum length using this constant for now.
     */
    public final static int MIN_LENGTH = 1;

    /**
     * The maximum length of function names. Guessing this limit is the same as for named ranges (aka labels).
     */
    // https://support.microsoft.com/en-au/office/names-in-formulas-fc2935f9-115d-4bef-a370-3aa8bb4c91f1#:~:text=Name%20length%20A%20name%20can,and%20lowercase%20characters%20in%20names.
    public final static int MAX_LENGTH = 255;

    // @VisibleForTesting
    private ExpressionFunctionName(final String name,
                                   final CaseSensitivity caseSensitivity) {
        final int length = name.length();
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            throw new InvalidTextLengthException(
                    "Function name",
                    name,
                    MIN_LENGTH,
                    MAX_LENGTH
            );
        }
        this.name = name;
        this.caseSensitivity = caseSensitivity;
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
        return this.caseSensitivity.hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof ExpressionFunctionName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final ExpressionFunctionName other) {
        return this.caseSensitivity.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return this.name;
    }

    // Comparable ...................................................................................................

    @Override
    public int compareTo(final ExpressionFunctionName other) {
        return this.caseSensitivity.comparator().compare(this.name, other.name);
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return this.caseSensitivity;
    }

    /**
     * Would be setter that returns a {@link ExpressionFunctionName} with the given {@link CaseSensitivity}.
     */
    public ExpressionFunctionName setCaseSensitivity(final CaseSensitivity caseSensitivity) {
        Objects.requireNonNull(caseSensitivity, "caseSensitivity");

        return this.caseSensitivity.equals(caseSensitivity) ?
                this :
                new ExpressionFunctionName(
                        this.name,
                        caseSensitivity
                );
    }

    private final CaseSensitivity caseSensitivity;

    // Comparator.......................................................................................................

    /**
     * {@see ExpressionFunctionNameComparator}
     */
    public static Comparator<ExpressionFunctionName> comparator(final CaseSensitivity caseSensitivity) {
        return ExpressionFunctionNameComparator.with(caseSensitivity);
    }

    private final static Parser<ParserContext> PARSER2 = Parsers.stringInitialAndPartCharPredicate(
            INITIAL,
            PART,
            MIN_LENGTH,
            MAX_LENGTH
    );

    /**
     * A parser function that returns a {@link ExpressionFunctionName} if the text under the cursor could be parsed.
     */
    public final static BiFunction<TextCursor, ParserContext, Optional<ExpressionFunctionName>> PARSER = (t, c) -> PARSER2.parse(t, c)
            .map(token -> with(token.text()));
}
