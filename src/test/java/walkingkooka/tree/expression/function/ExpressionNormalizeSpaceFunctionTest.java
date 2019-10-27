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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNormalizeSpaceFunctionTest extends ExpressionFunctionTestCase<ExpressionNormalizeSpaceFunction, String> {

    @Test
    public void testZeroParametersFails() {
        assertThrows(IllegalArgumentException.class, this::apply2);
    }

    @Test
    public void testTwoParametersFails() {
        assertThrows(IllegalArgumentException.class, () -> this.apply2( "a1", "b2"));
    }

    @Test
    public void testUnnecessary() {
        this.applyAndCheck3("a", "a");
    }

    @Test
    public void testUnnecessary2() {
        this.applyAndCheck3("abc", "abc");
    }

    @Test
    public void testUnnecessary3() {
        this.applyAndCheck3("a b c", "a b c");
    }

    @Test
    public void testLeadingWhitespace() {
        this.applyAndCheck3("  a 1", "a 1");
    }

    @Test
    public void testLeadingWhitespace2() {
        this.applyAndCheck3("\n\r  a 1", "a 1");
    }

    @Test
    public void testTrailingWhitespace() {
        this.applyAndCheck3("a 1  ", "a 1");
    }

    @Test
    public void testTrailingWhitespace2() {
        this.applyAndCheck3("a 1 \n\r ", "a 1");
    }

    @Test
    public void testWithoutLeadingOrTrailingWhitespace() {
        this.applyAndCheck3("a 1", "a 1");
    }

    @Test
    public void testWithLeadingOrTrailingWhitespace() {
        this.applyAndCheck3(" a 1  ", "a 1");
    }

    @Test
    public void testWithLeadingOrTrailingWhitespace2() {
        this.applyAndCheck3(" a    1  ", "a 1");
    }

    @Test
    public void testIgnoresNonSpaceWhitespace() {
        this.applyAndCheck3("a\nb", "a b");
    }

    @Test
    public void testIgnoresNonSpaceWhitespace2() {
        this.applyAndCheck3("a\rb", "a b");
    }

    @Test
    public void testIgnoresNonSpaceWhitespace3() {
        this.applyAndCheck3("a\r\n  b", "a b");
    }

    @Test
    public void testNumber() {
        this.applyAndCheck3(123, "123");
    }

    private void applyAndCheck3(final Object value, final String expected) {
        this.applyAndCheck2(parameters(value), expected);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "normalize-space");
    }

    @Override
    public ExpressionNormalizeSpaceFunction createBiFunction() {
        return ExpressionNormalizeSpaceFunction.INSTANCE;
    }

    @Override
    public Class<ExpressionNormalizeSpaceFunction> type() {
        return ExpressionNormalizeSpaceFunction.class;
    }
}
