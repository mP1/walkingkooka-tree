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

package walkingkooka.tree.expression.function;

import org.junit.jupiter.api.Test;
import walkingkooka.convert.HasConvertError;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionParameterCastTest implements ClassTesting2<ExpressionFunctionParameterCast> {

    @Test
    public void testCastFails() {
        final ClassCastException thrown = assertThrows(
            ClassCastException.class,
            () -> ExpressionFunctionParameterCast.cast(
                "Hello",
                this.parameter(Void.class)
            )
        );

        this.checkEquals(
            "Parameter \"Parameter\": Invalid type java.lang.String expected java.lang.Void",
            thrown.getMessage()
        );
    }

    @Test
    public void testCastWithHasConvertErrorFails() {
        final String message = "Cannot convert 'Hello' to 'Void' blah blah blah";

        final ClassCastException thrown = assertThrows(
            ClassCastException.class,
            () -> ExpressionFunctionParameterCast.cast(
                new HasConvertError() {
                    @Override
                    public Optional<String> convertErrorMessage() {
                        return Optional.of(message);
                    }
                },
                this.parameter(Void.class)
            )
        );

        this.checkEquals(
            message,
            thrown.getMessage()
        );
    }

    @Test
    public void testCastStringToString() {
        final String value = "Hello World";
        this.castAndCheck(
            value,
            String.class,
            value
        );
    }

    @Test
    public void testCastStringToCharSequence() {
        final String value = "Hello World";
        this.castAndCheck(
            value,
            CharSequence.class,
            value
        );
    }

    private <T> void castAndCheck(final Object value,
                                  final Class<T> parameterType,
                                  final T expected) {
        this.castAndCheck(
            value,
            this.parameter(parameterType),
            expected
        );
    }

    private <T> ExpressionFunctionParameter<T> parameter(final Class<T> parameterType) {
        return ExpressionFunctionParameter.with(
            ExpressionFunctionParameterName.with("Parameter"),
            parameterType,
            ExpressionFunctionParameterCardinality.REQUIRED,
            ExpressionFunctionParameter.withoutDefaultValue(), // no defaultValue
            ExpressionFunctionParameter.NO_KINDS
        );
    }

    private <T> void castAndCheck(final Object value,
                                  final ExpressionFunctionParameter<T> parameter,
                                  final T expected) {
        this.checkEquals(
            expected,
            ExpressionFunctionParameterCast.cast(
                value,
                parameter
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionFunctionParameterCast> type() {
        return ExpressionFunctionParameterCast.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
