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

import org.junit.jupiter.api.Test;
import walkingkooka.naming.NameTesting2;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;

public final class FunctionExpressionNameTest implements ClassTesting2<FunctionExpressionName>,
        NameTesting2<FunctionExpressionName, FunctionExpressionName> {

    @Test
    public void testErrorDotType() {
        this.createNameAndCheck("Error.Type");
    }

    @Test
    public void testNotFoundText() {
        this.checkEquals(
                "Function not found: \"abc123\"",
                FunctionExpressionName.with("abc123").notFoundText()
        );
    }

    @Override
    public FunctionExpressionName createName(final String name) {
        return FunctionExpressionName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return "abc2";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "a1";
    }

    @Override
    public int minLength() {
        return 1;
    }

    @Override
    public int maxLength() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String possibleValidChars(final int position) {
        return 0 == position ?
                ASCII_LETTERS :
                ASCII_LETTERS_DIGITS + "-._";
    }

    @Override
    public String possibleInvalidChars(final int position) {
        return NameTesting2.subtract(ASCII, this.possibleValidChars(position));
    }

    @Override
    public Class<FunctionExpressionName> type() {
        return FunctionExpressionName.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
