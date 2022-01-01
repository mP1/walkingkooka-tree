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
import walkingkooka.collect.set.Sets;
import walkingkooka.naming.NameTesting2;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.ConstantsTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionParameterNameTest implements ClassTesting2<ExpressionFunctionParameterName>,
        ConstantsTesting<ExpressionFunctionParameterName>,
        NameTesting2<ExpressionFunctionParameterName, ExpressionFunctionParameterName> {

    @Test
    public void testWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctionParameterName.with(null)
        );
    }

    @Test
    public void testWithBooleanConstantSingleton() {
        assertSame(
                ExpressionFunctionParameterName.BOOLEAN,
                ExpressionFunctionParameterName.with(
                        ExpressionFunctionParameterName.BOOLEAN.value()
                )
        );
    }

    @Test
    public void testSampleA() {
        this.createNameAndCheck("a");
    }

    @Test
    public void testSampleA1() {
        this.createNameAndCheck("a1");
    }

    @Test
    public void testSampleXyz() {
        this.createNameAndCheck("XYZ");
    }

    @Test
    public void testSampleUrlDashParameterDashName() {
        this.createNameAndCheck("url-parameter-name");
    }

    @Test
    public void testSetType() {
        final ExpressionFunctionParameterName name = this.createObject();

        final Class<String> type = String.class;
        final ExpressionFunctionParameter<String> parameter = name.setType(type);
        assertSame(name, parameter.name(), "name");
        assertSame(type, parameter.type(), "type");
    }

    @Override
    public ExpressionFunctionParameterName createName(final String name) {
        return ExpressionFunctionParameterName.with(name);
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
                ASCII_LETTERS_DIGITS + "-";
    }

    @Override
    public String possibleInvalidChars(final int position) {
        return NameTesting2.subtract(ASCII, this.possibleValidChars(position));
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<ExpressionFunctionParameterName> type() {
        return ExpressionFunctionParameterName.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // ConstantsTesting................................................................................................

    @Override
    public Set<ExpressionFunctionParameterName> intentionalDuplicateConstants() {
        return Sets.empty();
    }
}
