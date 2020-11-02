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
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicExpressionEvaluationContextTest implements ClassTesting2<BasicExpressionEvaluationContext>,
        ExpressionEvaluationContextTesting<BasicExpressionEvaluationContext> {

    private final static ExpressionNumberKind KIND = ExpressionNumberKind.DEFAULT;
    private final static ExpressionReference REFERENCE = new ExpressionReference() {
    };

    @Test
    public void testWithNullExpressionNumberKindFails() {
        assertThrows(NullPointerException.class, () -> BasicExpressionEvaluationContext.with(null,
                this.functions(),
                this.references(),
                this.converter(),
                this.converterContext()));
    }

    @Test
    public void testWithNullFunctionsFails() {
        assertThrows(NullPointerException.class, () -> BasicExpressionEvaluationContext.with(KIND,
                null,
                this.references(),
                this.converter(),
                this.converterContext()));
    }

    @Test
    public void testWithNullReferencesFails() {
        assertThrows(NullPointerException.class, () -> BasicExpressionEvaluationContext.with(KIND,
                this.functions(),
                null,
                this.converter(),
                this.converterContext()));
    }

    @Test
    public void testWithNullConverterFails() {
        assertThrows(NullPointerException.class, () -> BasicExpressionEvaluationContext.with(KIND,
                this.functions(),
                this.references(),
                null,
                this.converterContext()));
    }

    @Test
    public void testWithNullConverterContextFails() {
        assertThrows(NullPointerException.class, () -> BasicExpressionEvaluationContext.with(KIND,
                this.functions(),
                this.references(),
                this.converter(),
                null));
    }

    @Test
    public void testEvaluateTrue() {
        this.evaluateAndCheck2(true);
    }

    @Test
    public void testEvaluateFalse() {
        this.evaluateAndCheck2(false);
    }

    private void evaluateAndCheck2(final boolean value) {
        this.evaluateAndCheck(Expression.booleanExpression(value), value);
    }

    @Test
    public void testEvaluateString() {
        final String value = "abc123";
        this.evaluateAndCheck(Expression.string(value), value);
    }

    @Test
    public void testFunction() {
        assertEquals(this.functionValue(), this.createContext().function(this.functionName(), this.parameters()));
    }

    @Test
    public void testReferences() {
        assertEquals(Optional.of(this.expression()), this.createContext().reference(REFERENCE));
    }

    @Test
    public void testConvert() {
        assertEquals(Either.left(123L), this.createContext().convert(123.0, Long.class));
    }

    @Override
    public BasicExpressionEvaluationContext createContext() {
        return BasicExpressionEvaluationContext.with(KIND,
                this.functions(),
                this.references(),
                this.converter(),
                this.converterContext());
    }

    private BiFunction<FunctionExpressionName, List<Object>, Object> functions() {
        return (functionName, parameters) -> {
            Objects.requireNonNull(functionName, "functionName");
            Objects.requireNonNull(parameters, "parameters");

            if (false == this.functionName().equals(functionName)) {
                throw new IllegalArgumentException("Unknown function: " + functionName);
            }

            assertEquals(this.parameters(), parameters, "parameters");
            return this.functionValue();
        };
    }

    private FunctionExpressionName functionName() {
        return FunctionExpressionName.with("sum");
    }

    private List<Object> parameters() {
        return Lists.of("parameter-1", 2);
    }

    private Object functionValue() {
        return "function-value-234";
    }

    private Function<ExpressionReference, Optional<Expression>> references() {
        return (r -> {
            Objects.requireNonNull(r, "references");
            assertEquals(REFERENCE, r, "reference");
            return Optional.of(this.expression());
        });
    }

    private Expression expression() {
        return Expression.string("expression node 123");
    }

    private Converter<ConverterContext> converter() {
        return Converters.numberNumber();
    }

    private ConverterContext converterContext() {
        return ConverterContexts.basic(DateTimeContexts.fake(), this.decimalNumberContext());
    }

    private DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.american(this.mathContext());
    }

    @Override
    public String currencySymbol() {
        return this.decimalNumberContext().currencySymbol();
    }

    @Override
    public char decimalSeparator() {
        return this.decimalNumberContext().decimalSeparator();
    }

    @Override
    public String exponentSymbol() {
        return this.decimalNumberContext().exponentSymbol();
    }

    @Override
    public char groupingSeparator() {
        return this.decimalNumberContext().groupingSeparator();
    }

    @Override
    public MathContext mathContext() {
        return MathContext.DECIMAL32;
    }

    @Override
    public char negativeSign() {
        return this.decimalNumberContext().negativeSign();
    }

    @Override
    public char percentageSymbol() {
        return this.decimalNumberContext().percentageSymbol();
    }

    @Override
    public char positiveSign() {
        return this.decimalNumberContext().positiveSign();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<BasicExpressionEvaluationContext> type() {
        return BasicExpressionEvaluationContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
