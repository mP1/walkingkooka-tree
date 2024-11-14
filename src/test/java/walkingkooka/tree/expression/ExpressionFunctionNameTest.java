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
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.NameTesting2;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.ParserContexts;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionNameTest implements ClassTesting2<ExpressionFunctionName>,
        NameTesting2<ExpressionFunctionName, ExpressionFunctionName> {

    // DEFAULT_CASE_SENSITIVITY.........................................................................................

    @Test public void testDefaultCaseSensitivity() {
        assertSame(CaseSensitivity.SENSITIVE, ExpressionFunctionName.DEFAULT_CASE_SENSITIVITY);
    }

    // name.............................................................................................................
    @Test
    public void testErrorDotType() {
        this.createNameAndCheck("Error.Type");
    }

    @Test
    public void testNotFoundText() {
        this.checkEquals(
                "Function not found: \"abc123\"",
                ExpressionFunctionName.with("abc123").notFoundText()
        );
    }

    @Test
    public void testComparatorCaseSensitive() {
        final ExpressionFunctionName name1 = ExpressionFunctionName.with("abc");
        final ExpressionFunctionName name2 = ExpressionFunctionName.with("def");
        final ExpressionFunctionName name3 = ExpressionFunctionName.with("XYZ");

        final List<ExpressionFunctionName> sorted = Lists.array();
        sorted.add(name1);
        sorted.add(name2);
        sorted.add(name3);
        sorted.sort(ExpressionFunctionName.comparator(CaseSensitivity.SENSITIVE));

        this.checkEquals(
                Lists.of(
                        name3, name1, name2
                ),
                sorted
        );
    }

    @Test
    public void testComparatorCaseInsensitive() {
        final ExpressionFunctionName name1 = ExpressionFunctionName.with("abc");
        final ExpressionFunctionName name2 = ExpressionFunctionName.with("def");
        final ExpressionFunctionName name3 = ExpressionFunctionName.with("XYZ");

        final List<ExpressionFunctionName> sorted = Lists.array();
        sorted.add(name1);
        sorted.add(name2);
        sorted.add(name3);
        sorted.sort(ExpressionFunctionName.comparator(CaseSensitivity.INSENSITIVE));

        this.checkEquals(
                Lists.of(
                        name1, name2, name3
                ),
                sorted
        );
    }

    @Test
    public void testEqualsDifferentCaseInsensitive() {
        final ExpressionFunctionName name1 = ExpressionFunctionName.with("abc")
                .setCaseSensitivity(CaseSensitivity.SENSITIVE);
        final ExpressionFunctionName name2 = name1.setCaseSensitivity(CaseSensitivity.INSENSITIVE);

        this.checkNotEquals(
                name1,
                name2
        );

        this.checkEquals(
                name1,
                name2.setCaseSensitivity(name1.caseSensitivity())
        );
    }

    @Override
    public ExpressionFunctionName createName(final String name) {
        return ExpressionFunctionName.with(name);
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

    // setCaseSensitivity...............................................................................................

    @Test
    public void testSetCaseSensitivityWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctionName.with("Hello")
                        .setCaseSensitivity(null)
        );
    }

    @Test
    public void testSetCaseSensitivityWithSame() {
        final ExpressionFunctionName name = ExpressionFunctionName.with("Hello");

        assertSame(
                name,
                name.setCaseSensitivity(name.caseSensitivity())
        );
    }

    @Test
    public void testSetCaseSensitivityWithDifferent() {
        final ExpressionFunctionName name = ExpressionFunctionName.with("Hello");

        final CaseSensitivity different = CaseSensitivity.INSENSITIVE;
        final ExpressionFunctionName differentName = name.setCaseSensitivity(different);

        assertNotSame(
                name,
                differentName
        );

        this.caseSensitivityAndCheck(
                differentName,
                different
        );

        {
            final CaseSensitivity different2 = CaseSensitivity.SENSITIVE;
            this.caseSensitivityAndCheck(
                    differentName.setCaseSensitivity(different2),
                    different2
            );
        }
    }

    private void caseSensitivityAndCheck(final ExpressionFunctionName name,
                                         final CaseSensitivity expected) {
        this.checkEquals(
                expected,
                name.caseSensitivity()
        );
    }

    // parser...........................................................................................................

    @Test
    public void testParseFails() {
        final String text = " fails!";
        this.parseAndCheck(
                text,
                Optional.empty(),
                text
        );
    }

    @Test
    public void testParseSuccess() {
        final String name = "Function123";
        final String after = " after";

        this.parseAndCheck(
                name + after,
                Optional.of(
                        ExpressionFunctionName.with(name)
                ),
                after
        );
    }

    private void parseAndCheck(final String text,
                               final Optional<ExpressionFunctionName> expected,
                               final String left) {
        final TextCursor cursor = TextCursors.charSequence(text);

        this.checkEquals(
                expected,
                ExpressionFunctionName.PARSER.apply(
                        cursor,
                        ParserContexts.fake()
                )
        );

        final TextCursorSavePoint save = cursor.save();
        cursor.end();

        this.checkEquals(
                left,
                save.textBetween().toString(),
                "cursor remaining text after parsing"
        );
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionFunctionName> type() {
        return ExpressionFunctionName.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
