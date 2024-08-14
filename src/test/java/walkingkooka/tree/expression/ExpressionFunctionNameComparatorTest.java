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

import org.junit.jupiter.api.Test;
import walkingkooka.compare.ComparatorTesting2;
import walkingkooka.text.CaseSensitivity;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionNameComparatorTest implements ComparatorTesting2<ExpressionFunctionNameComparator, ExpressionFunctionName> {

    @Test
    public void testWithNullCaseSensitivityFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctionNameComparator.with(null)
        );
    }

    @Test
    public void testWithSensitiveCached() {
        assertSame(
                ExpressionFunctionNameComparator.with(CaseSensitivity.SENSITIVE),
                ExpressionFunctionNameComparator.with(CaseSensitivity.SENSITIVE)
        );
    }

    @Test
    public void testWithInsensitiveCached() {
        assertSame(
                ExpressionFunctionNameComparator.with(CaseSensitivity.INSENSITIVE),
                ExpressionFunctionNameComparator.with(CaseSensitivity.INSENSITIVE)
        );
    }

    @Test
    public void testCompareEqualCaseSensitive() {
        this.compareAndCheckEquals(
                ExpressionFunctionNameComparator.with(CaseSensitivity.SENSITIVE),
                ExpressionFunctionName.with("hello"),
                ExpressionFunctionName.with("hello")
        );
    }

    @Test
    public void testCompareEqualCaseInsensitive() {
        this.compareAndCheckEquals(
                ExpressionFunctionNameComparator.with(CaseSensitivity.INSENSITIVE),
                ExpressionFunctionName.with("HELLO"),
                ExpressionFunctionName.with("hello")
        );
    }

    @Test
    public void testCompareLessCaseSensitive() {
        this.compareAndCheckLess(
                ExpressionFunctionNameComparator.with(CaseSensitivity.SENSITIVE),
                ExpressionFunctionName.with("hello"),
                ExpressionFunctionName.with("xyz")
        );
    }

    @Test
    public void testCompareLessCaseInsensitive() {
        this.compareAndCheckLess(
                ExpressionFunctionNameComparator.with(CaseSensitivity.INSENSITIVE),
                ExpressionFunctionName.with("hello"),
                ExpressionFunctionName.with("XYZ")
        );
    }

    @Test
    public void testToStringSensitive() {
        this.toStringAndCheck(
                ExpressionFunctionNameComparator.with(CaseSensitivity.SENSITIVE),
                "ExpressionFunctionName CASE SENSITIVE"
        );
    }

    @Test
    public void testToStringInsensitive() {
        this.toStringAndCheck(
                ExpressionFunctionNameComparator.with(CaseSensitivity.INSENSITIVE),
                "ExpressionFunctionName CASE INSENSITIVE"
        );
    }

    @Override
    public ExpressionFunctionNameComparator createComparator() {
        return ExpressionFunctionNameComparator.with(CaseSensitivity.INSENSITIVE);
    }

    @Override
    public Class<ExpressionFunctionNameComparator> type() {
        return ExpressionFunctionNameComparator.class;
    }
}
