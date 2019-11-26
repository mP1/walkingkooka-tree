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

import org.junit.jupiter.api.Test;
import walkingkooka.compare.ComparisonRelation;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ComparisonExpressionFunctionTest extends ExpressionFunctionTestCase<ComparisonExpressionFunction, Boolean> {

    @Test
    public void testZeroParametersFails() {
        assertThrows(IllegalArgumentException.class, this::apply2);
    }

    @Test
    public void testOneParametersFails() {
        assertThrows(IllegalArgumentException.class, () -> this.apply2("a1"));
    }

    @Test
    public void testThreeParametersFails() {
        assertThrows(IllegalArgumentException.class, () -> this.apply2("a1", 2, 3));
    }

    // EQ........................................................................................

    @Test
    public void testEqualsLess() {
        this.applyAndCheck3(ComparisonRelation.EQ, "a", "b", false);
    }

    @Test
    public void testEqualsEqual() {
        this.applyAndCheck3(ComparisonRelation.EQ, "a", "a", true);
    }

    @Test
    public void testEqualsMore() {
        this.applyAndCheck3(ComparisonRelation.EQ, "z", "a", false);
    }

    // NE........................................................................................

    @Test
    public void testNotEqualsLess() {
        this.applyAndCheck3(ComparisonRelation.NE, "a", "b", true);
    }

    @Test
    public void testNotEqualsNeual() {
        this.applyAndCheck3(ComparisonRelation.NE, "a", "a", false);
    }

    @Test
    public void testNotEqualsMore() {
        this.applyAndCheck3(ComparisonRelation.NE, "z", "a", true);
    }

    // GT........................................................................................

    @Test
    public void testGreaterThanLess() {
        this.applyAndCheck3(ComparisonRelation.GT, "a", "b", false);
    }

    @Test
    public void testGreaterThanEqual() {
        this.applyAndCheck3(ComparisonRelation.GT, "a", "a", false);
    }

    @Test
    public void testGreaterThanMore() {
        this.applyAndCheck3(ComparisonRelation.GT, "z", "a", true);
    }

    // GTE........................................................................................

    @Test
    public void testGreaterThanEqualsLess() {
        this.applyAndCheck3(ComparisonRelation.GTE, "a", "b", false);
    }

    @Test
    public void testGreaterThanEqualsEqual() {
        this.applyAndCheck3(ComparisonRelation.GTE, "a", "a", true);
    }

    @Test
    public void testGreaterThanEqualsMore() {
        this.applyAndCheck3(ComparisonRelation.GTE, "z", "a", true);
    }

    // LT........................................................................................

    @Test
    public void testLessThanLess() {
        this.applyAndCheck3(ComparisonRelation.LT, "a", "b", true);
    }

    @Test
    public void testLessThanEqual() {
        this.applyAndCheck3(ComparisonRelation.LT, "a", "a", false);
    }

    @Test
    public void testLessThanMore() {
        this.applyAndCheck3(ComparisonRelation.LT, "z", "a", false);
    }

    // LTE........................................................................................

    @Test
    public void testLessThanEqualsLess() {
        this.applyAndCheck3(ComparisonRelation.LTE, "a", "b", true);
    }

    @Test
    public void testLessThanEqualsEqual() {
        this.applyAndCheck3(ComparisonRelation.LTE, "a", "a", true);
    }

    @Test
    public void testLessThanEqualsMore() {
        this.applyAndCheck3(ComparisonRelation.LTE, "z", "a", false);
    }

    // conversion.............................................................................

    @Test
    public void testSecondParameterConverted() {
        this.applyAndCheck3(ComparisonRelation.LTE, 123, "456", true);
    }


    // helper........................................................................................

    final void applyAndCheck3(final ComparisonRelation relation,
                              final Object first,
                              final Object second,
                              final Boolean result) {
        this.applyAndCheck2(ComparisonExpressionFunction.with(relation), parameters(first, second), result);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), ComparisonRelation.EQ.toString());
    }

    @Override
    public ComparisonExpressionFunction createBiFunction() {
        return ComparisonExpressionFunction.with(ComparisonRelation.EQ);
    }

    @Override
    public Class<ComparisonExpressionFunction> type() {
        return ComparisonExpressionFunction.class;
    }
}
