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

public final class FunctionExpressionNameComparatorTest implements ComparatorTesting2<FunctionExpressionNameComparator, FunctionExpressionName> {

    @Test
    public void testWithNullCaseSensitivityFails() {
        assertThrows(
                NullPointerException.class,
                () -> FunctionExpressionNameComparator.with(null)
        );
    }

    @Test
    public void testWithSensitiveCached() {
        assertSame(
                FunctionExpressionNameComparator.with(CaseSensitivity.SENSITIVE),
                FunctionExpressionNameComparator.with(CaseSensitivity.SENSITIVE)
        );
    }

    @Test
    public void testWithInsensitiveCached() {
        assertSame(
                FunctionExpressionNameComparator.with(CaseSensitivity.INSENSITIVE),
                FunctionExpressionNameComparator.with(CaseSensitivity.INSENSITIVE)
        );
    }

    @Test
    public void testCompareEqualCaseSensitive() {
        this.compareAndCheckEquals(
                FunctionExpressionNameComparator.with(CaseSensitivity.SENSITIVE),
                FunctionExpressionName.with("hello"),
                FunctionExpressionName.with("hello")
        );
    }

    @Test
    public void testCompareEqualCaseInsensitive() {
        this.compareAndCheckEquals(
                FunctionExpressionNameComparator.with(CaseSensitivity.INSENSITIVE),
                FunctionExpressionName.with("HELLO"),
                FunctionExpressionName.with("hello")
        );
    }

    @Test
    public void testCompareLessCaseSensitive() {
        this.compareAndCheckLess(
                FunctionExpressionNameComparator.with(CaseSensitivity.SENSITIVE),
                FunctionExpressionName.with("hello"),
                FunctionExpressionName.with("xyz")
        );
    }

    @Test
    public void testCompareLessCaseInsensitive() {
        this.compareAndCheckLess(
                FunctionExpressionNameComparator.with(CaseSensitivity.INSENSITIVE),
                FunctionExpressionName.with("hello"),
                FunctionExpressionName.with("XYZ")
        );
    }

    @Test
    public void testToStringSensitive() {
        this.toStringAndCheck(
                FunctionExpressionNameComparator.with(CaseSensitivity.SENSITIVE),
                "FunctionExpressionName CASE SENSITIVE"
        );
    }

    @Test
    public void testToStringInsensitive() {
        this.toStringAndCheck(
                FunctionExpressionNameComparator.with(CaseSensitivity.INSENSITIVE),
                "FunctionExpressionName CASE INSENSITIVE"
        );
    }

    @Override
    public FunctionExpressionNameComparator createComparator() {
        return FunctionExpressionNameComparator.with(CaseSensitivity.INSENSITIVE);
    }

    @Override
    public Class<FunctionExpressionNameComparator> type() {
        return FunctionExpressionNameComparator.class;
    }
}
