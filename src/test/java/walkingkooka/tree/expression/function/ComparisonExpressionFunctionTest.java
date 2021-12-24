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
import walkingkooka.Cast;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ComparisonExpressionFunctionTest extends ExpressionFunctionTestCase<ComparisonExpressionFunction<ExpressionFunctionContext>, Boolean> {

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

    @Test
    public void testResolveReferencesFalse() {
        this.resolveReferenceAndCheck(false);
    }

    // EQ........................................................................................

    @Test
    public void testEqualsLess() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.equals(),
                "a",
                "b",
                false
        );
    }

    @Test
    public void testEqualsEqual() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.equals(),
                "a",
                "a",
                true
        );
    }

    @Test
    public void testEqualsMore() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.equals(),
                "z",
                "a",
                false
        );
    }

    // NE........................................................................................

    @Test
    public void testNotEqualsLess() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.notEquals(),
                "a",
                "b",
                true
        );
    }

    @Test
    public void testNotEqualsNeual() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.notEquals(),
                "a",
                "a",
                false
        );
    }

    @Test
    public void testNotEqualsMore() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.notEquals(),
                "z",
                "a",
                true
        );
    }

    // GT........................................................................................

    @Test
    public void testGreaterThanLess() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.greaterThan(),
                "a",
                "b",
                false
        );
    }

    @Test
    public void testGreaterThanEqual() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.greaterThan(),
                "a",
                "a",
                false
        );
    }

    @Test
    public void testGreaterThanMore() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.greaterThan(),
                "z",
                "a",
                true
        );
    }

    // GTE........................................................................................

    @Test
    public void testGreaterThanEqualsLess() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.greaterThanEqual(),
                "a",
                "b",
                false
        );
    }

    @Test
    public void testGreaterThanEqualsEqual() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.greaterThanEqual(),
                "a",
                "a",
                true
        );
    }

    @Test
    public void testGreaterThanEqualsMore() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.greaterThanEqual(),
                "z",
                "a",
                true
        );
    }

    // LT........................................................................................

    @Test
    public void testLessThanLess() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.lessThan(),
                "a",
                "b",
                true
        );
    }

    @Test
    public void testLessThanEqual() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.lessThan(),
                "a",
                "a",
                false
        );
    }

    @Test
    public void testLessThanMore() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.lessThan(),
                "z",
                "a",
                false
        );
    }

    // LTE........................................................................................

    @Test
    public void testLessThanEqualsLess() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.lessThanEqual(),
                "a",
                "b",
                true
        );
    }

    @Test
    public void testLessThanEqualsEqual() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.lessThanEqual(),
                "a",
                "a",
                true
        );
    }

    @Test
    public void testLessThanEqualsMore() {
        this.applyAndCheck3(
                ComparisonExpressionFunction.lessThanEqual(),
                "z",
                "a",
                false
        );
    }

    // helper........................................................................................

    void applyAndCheck3(final ComparisonExpressionFunction<ExpressionFunctionContext> function,
                        final Object first,
                        final Object second,
                        final Boolean result) {
        this.applyAndCheck2(
                function,
                parameters(first, second),
                result
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "greater-than");
    }

    @Override
    public ComparisonExpressionFunction<ExpressionFunctionContext> createBiFunction() {
        return ComparisonExpressionFunction.greaterThan();
    }

    @Override
    public Class<ComparisonExpressionFunction<ExpressionFunctionContext>> type() {
        return Cast.to(ComparisonExpressionFunction.class);
    }
}
