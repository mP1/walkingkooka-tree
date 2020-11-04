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
import walkingkooka.Either;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.math.MathContext;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionEvaluationContextExpressionFunctionContextTest implements ExpressionFunctionContextTesting<ExpressionEvaluationContextExpressionFunctionContext>,
        ToStringTesting<ExpressionEvaluationContextExpressionFunctionContext> {

    @Test
    public void testWithNullContextFails() {
        assertThrows(NullPointerException.class, () -> ExpressionEvaluationContextExpressionFunctionContext.with(null));
    }

    private final static ExpressionNumberKind KIND = ExpressionNumberKind.BIG_DECIMAL;

    @Test
    public void testExpressionNumberKind() {
        assertSame(KIND, this.createContext().expressionNumberKind());
    }

    private final static Locale LOCALE = Locale.forLanguageTag("EN-AU");

    @Test
    public void testLocale() {
        assertSame(LOCALE, this.createContext().locale());
    }

    private final static MathContext MATH_CONTEXT = MathContext.DECIMAL64;

    @Test
    public void testMathContext() {
        assertSame(MATH_CONTEXT, this.createContext().mathContext());
    }

    private final static String CONVERT_FROM = "value to be converted 123!";
    private final static Class<Integer> CONVERT_TYPE = Integer.class;
    private final static Integer CONVERT_TO = 1234;

    @Test
    public void testConvert() {
        this.convertAndCheck(CONVERT_FROM, CONVERT_TYPE, CONVERT_TO);
    }

    @Test
    public void testFunction() {
        final FunctionExpressionName name = FunctionExpressionName.with("function-123");
        final List<Object> parameters = Lists.of(1, true, 23.5);
        assertEquals("@" + name + parameters, this.createContext().function(name, parameters));
    }

    @Test
    public void testToString() {
        final String toString = "1234";
        this.toStringAndCheck(ExpressionEvaluationContextExpressionFunctionContext.with(new FakeExpressionEvaluationContext() {
            @Override
            public String toString() {
                return toString;
            }
        }), toString);
    }

    @Override
    public ExpressionEvaluationContextExpressionFunctionContext createContext() {
        return ExpressionEvaluationContextExpressionFunctionContext.with(new FakeExpressionEvaluationContext() {
            @Override
            public ExpressionNumberKind expressionNumberKind() {
                return KIND;
            }

            @Override
            public Object function(final FunctionExpressionName name,
                                   final List<Object> parameters) {
                Objects.requireNonNull(name, "name");
                Objects.requireNonNull(parameters, "parameters");

                return "@" + name + parameters;
            }

            @Override
            public Locale locale() {
                return LOCALE;
            }

            @Override
            public MathContext mathContext() {
                return MATH_CONTEXT;
            }

            @Override
            public boolean canConvert(final Object value,
                                      final Class<?> type) {
                return CONVERT_FROM == value && CONVERT_TYPE == type;
            }

            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> type) {
                return this.canConvert(value, type) ?
                        Either.left(type.cast(CONVERT_TO)) :
                        this.failConversion(value, type);
            }
        });
    }

    @Override
    public Class<ExpressionEvaluationContextExpressionFunctionContext> type() {
        return ExpressionEvaluationContextExpressionFunctionContext.class;
    }
}
