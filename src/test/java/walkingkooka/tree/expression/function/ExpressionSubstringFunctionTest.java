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
import walkingkooka.tree.select.NodeSelector;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionSubstringFunctionTest extends ExpressionFunctionTestCase<ExpressionSubstringFunction, String> {

    @Test
    public void testZeroParametersFails() {
        assertThrows(IllegalArgumentException.class, () -> this.apply2(this));
    }

    @Test
    public void testOneParametersFails() {
        assertThrows(IllegalArgumentException.class, () -> this.apply2( "a1"));
    }

    @Test
    public void testTwoParametersFails() {
        assertThrows(IllegalArgumentException.class, () -> this.apply2( "a1", "b2"));
    }

    @Test
    public void testFourParametersFails() {
        assertThrows(IllegalArgumentException.class, () -> this.apply2( "a1", 2, 3, 4));
    }

    @Test
    public void testSubstringOutOfRange() {
        assertThrows(StringIndexOutOfBoundsException.class, () -> this.apply2( "abcdef", -2, 2));
    }

    @Test
    public void testSubstringOutOfRange2() {
        assertThrows(StringIndexOutOfBoundsException.class, () -> this.apply2( "abcdef", 2, -1));
    }

    @Test
    public void testSubstringOutOfRange3() {
        assertThrows(StringIndexOutOfBoundsException.class, () -> this.apply2( "abcdef", 1, 99));
    }

    @Test
    public void testSubstring() {
        this.applyAndCheck2(parameters( "abcdef", 3, 0), "");
    }

    @Test
    public void testSubstring2() {
        this.applyAndCheck2(parameters( "abcdef", 4, 1), "d");
    }

    @Test
    public void testSubstring3() {
        this.applyAndCheck2(parameters( "abcdef", 3, 2), "cd");
    }

    @Test
    public void testSubstringLengthMissing() {
        this.applyAndCheck2(parameters( "abcdef", 2), "bcdef");
    }

    @Test
    public void testSubstringLengthMissing2() {
        this.applyAndCheck2(parameters( "abc", 3), "c");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "substring");
    }

    @Override
    public ExpressionSubstringFunction createBiFunction() {
        return ExpressionSubstringFunction.with(NodeSelector.INDEX_BIAS);
    }

    @Override
    public Class<ExpressionSubstringFunction> type() {
        return ExpressionSubstringFunction.class;
    }
}
