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

import walkingkooka.NeverError;
import walkingkooka.text.CaseSensitivity;

import java.util.Comparator;
import java.util.Objects;

final class ExpressionFunctionNameComparator implements Comparator<ExpressionFunctionName> {

    static ExpressionFunctionNameComparator with(final CaseSensitivity caseSensitivity) {
        Objects.requireNonNull(caseSensitivity, "stringEqualsCaseSensitivity");

        final ExpressionFunctionNameComparator comparator;

        switch (caseSensitivity) {
            case SENSITIVE:
                comparator = CASE_SENSITIVE;
                break;
            case INSENSITIVE:
                comparator = CASE_INSENSITIVE;
                break;
            default:
                comparator = NeverError.unhandledEnum(
                    caseSensitivity,
                    CaseSensitivity.values()
                );
        }

        return comparator;
    }

    private final static ExpressionFunctionNameComparator CASE_SENSITIVE = new ExpressionFunctionNameComparator(CaseSensitivity.SENSITIVE);

    private final static ExpressionFunctionNameComparator CASE_INSENSITIVE = new ExpressionFunctionNameComparator(CaseSensitivity.INSENSITIVE);

    private ExpressionFunctionNameComparator(final CaseSensitivity caseSensitivity) {
        this.caseSensitivity = caseSensitivity;
    }

    @Override
    public int compare(final ExpressionFunctionName left,
                       final ExpressionFunctionName right) {
        final CaseSensitivity caseSensitivity = this.caseSensitivity;
        return caseSensitivity.comparator()
            .compare(
                left.value(),
                right.value()
            );
    }

    private final CaseSensitivity caseSensitivity;

    @Override
    public String toString() {
        return ExpressionFunctionName.class.getSimpleName() + " CASE " + this.caseSensitivity;
    }
}
