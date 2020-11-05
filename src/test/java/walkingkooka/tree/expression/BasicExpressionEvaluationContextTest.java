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
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionContext;
import walkingkooka.tree.expression.function.FakeExpressionFunction;
import walkingkooka.tree.expression.function.UnknownFunctionException;

import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicExpressionEvaluationContextTest implements ClassTesting2<BasicExpressionEvaluationContext>,
        ExpressionEvaluationContextTesting<BasicExpressionEvaluationContext>,
        ToStringTesting<BasicExpressionEvaluationContext> {

    private final static ExpressionNumberKind KIND = ExpressionNumberKind.DEFAULT;
    private final static ExpressionReference REFERENCE = new ExpressionReference() {
    };

    @Test
    public void testWithNullExpressionNumberKindFails() {
        assertThrows(NullPointerException.class, () -> BasicExpressionEvaluationContext.with(null,
                this.functions(),
                this.references(),
                this.converterContext()));
    }

    @Test
    public void testWithNullFunctionsFails() {
        assertThrows(NullPointerException.class, () -> BasicExpressionEvaluationContext.with(KIND,
                null,
                this.references(),
                this.converterContext()));
    }

    @Test
    public void testWithNullReferencesFails() {
        assertThrows(NullPointerException.class, () -> BasicExpressionEvaluationContext.with(KIND,
                this.functions(),
                null,
                this.converterContext()));
    }

    @Test
    public void testWithNullConverterContextFails() {
        assertThrows(NullPointerException.class, () -> BasicExpressionEvaluationContext.with(KIND,
                this.functions(),
                this.references(),
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
    public void testEvaluate() {
        assertEquals(this.functionValue(),
                this.createContext()
                        .evaluate(this.functionName(), this.parameters()));
    }

    @Test
    public void testReferences() {
        assertEquals(Optional.of(this.expression()), this.createContext().reference(REFERENCE));
    }

    @Test
    public void testConvert() {
        this.convertAndCheck(123.0, Long.class, 123L);
    }

    @Test
    public void testToString() {
        final Function<FunctionExpressionName, ExpressionFunction<?, ExpressionFunctionContext>> functions = this.functions();
        final Function<ExpressionReference, Optional<Expression>> references = this.references();
        final ConverterContext converterContext = this.converterContext();

        this.toStringAndCheck(BasicExpressionEvaluationContext.with(KIND,
                functions,
                references,
                converterContext)
                .toString(), functions + " " + references + " " + converterContext);
    }

    @Override
    public BasicExpressionEvaluationContext createContext() {
        return BasicExpressionEvaluationContext.with(KIND,
                this.functions(),
                this.references(),
                this.converterContext());
    }

    private Function<FunctionExpressionName, ExpressionFunction<?, ExpressionFunctionContext>> functions() {
        return (functionName) -> {
            Objects.requireNonNull(functionName, "functionName");

            if (false == this.functionName().equals(functionName)) {
                throw new UnknownFunctionException(functionName);
            }

            return new FakeExpressionFunction<>() {
                @Override
                public Object apply(final List<Object> parameters,
                                    final ExpressionFunctionContext context) {
                    Objects.requireNonNull(parameters, "parameters");
                    Objects.requireNonNull(context, "context");
                    return BasicExpressionEvaluationContextTest.this.functionValue();
                }
            };
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

    private ConverterContext converterContext() {
        return ConverterContexts.basic(Converters.numberNumber(),
                DateTimeContexts.fake(),
                DecimalNumberContexts.american(MathContext.DECIMAL32));
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<BasicExpressionEvaluationContext> type() {
        return Cast.to(BasicExpressionEvaluationContext.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
