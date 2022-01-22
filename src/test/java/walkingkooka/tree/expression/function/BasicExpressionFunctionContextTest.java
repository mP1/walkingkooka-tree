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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicExpressionFunctionContextTest implements ClassTesting2<BasicExpressionFunctionContext>,
        ExpressionFunctionContextTesting<BasicExpressionFunctionContext>,
        ToStringTesting<BasicExpressionFunctionContext> {

    private final static ExpressionNumberKind KIND = ExpressionNumberKind.DEFAULT;

    private final static ExpressionReference REFERENCE = new ExpressionReference() {
    };

    private final static Object REFERENCE_VALUE = "*123*";

    @Test
    public void testWithNullExpressionNumberKindFails() {
        assertThrows(
                NullPointerException.class,
                () -> BasicExpressionFunctionContext.with(
                        null,
                        this.functions(),
                        this.references(),
                        this.converterContext()
                )
        );
    }

    @Test
    public void testWithNullFunctionsFails() {
        assertThrows(
                NullPointerException.class,
                () -> BasicExpressionFunctionContext.with(
                        KIND,
                        null,
                        this.references(),
                        this.converterContext()
                )
        );
    }

    @Test
    public void testWithNullReferencesFails() {
        assertThrows(
                NullPointerException.class,
                () -> BasicExpressionFunctionContext.with(
                        KIND,
                        this.functions(),
                        null,
                        this.converterContext()
                )
        );
    }

    @Test
    public void testWithNullConverterContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> BasicExpressionFunctionContext.with(
                        KIND,
                        this.functions(),
                        this.references(),
                        null
                )
        );
    }

    @Test
    public void testEvaluate() {
        this.checkEquals(this.functionValue(),
                this.createContext()
                        .evaluate(this.functionName(), this.parameters()));
    }

    @Test
    public void testReference() {
        this.checkEquals(
                Optional.of(
                        REFERENCE_VALUE
                ),
                this.createContext().reference(REFERENCE)
        );
    }

    @Test
    public void testConvert() {
        this.convertAndCheck(123.0, Long.class, 123L);
    }

    @Test
    public void testToString() {
        final Function<FunctionExpressionName, ExpressionFunction<?, ExpressionFunctionContext>> functions = this.functions();
        final Function<ExpressionReference, Optional<Object>> references = this.references();
        final ConverterContext converterContext = this.converterContext();

        this.toStringAndCheck(
                BasicExpressionFunctionContext.with(
                        KIND,
                        functions,
                        references,
                        converterContext
                ),
                functions + " " + references + " " + converterContext
        );
    }

    @Override
    public BasicExpressionFunctionContext createContext() {
        return this.createContext(true);
    }

    private BasicExpressionFunctionContext createContext(final boolean pure) {
        return BasicExpressionFunctionContext.with(
                KIND,
                this.functions(pure),
                this.references(),
                this.converterContext()
        );
    }

    private Function<FunctionExpressionName, ExpressionFunction<?, ExpressionFunctionContext>> functions() {
        return this.functions(true);
    }

    private Function<FunctionExpressionName, ExpressionFunction<?, ExpressionFunctionContext>> functions(final boolean pure) {
        return (functionName) -> {
            Objects.requireNonNull(functionName, "functionName");

            if (false == this.functionName().equals(functionName)) {
                throw new UnknownExpressionFunctionException(functionName);
            }

            return new FakeExpressionFunction<>() {
                @Override
                public Object apply(final List<Object> parameters,
                                    final ExpressionFunctionContext context) {
                    Objects.requireNonNull(parameters, "parameters");
                    Objects.requireNonNull(context, "context");

                    return BasicExpressionFunctionContextTest.this.functionValue();
                }

                @Override
                public boolean isPure(final ExpressionPurityContext context) {
                    return pure;
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

    private Function<ExpressionReference, Optional<Object>> references() {
        return (r -> {
            Objects.requireNonNull(r, "references");
            this.checkEquals(REFERENCE, r, "reference");
            return Optional.of(
                    REFERENCE_VALUE
            );
        });
    }

    private ConverterContext converterContext() {
        return ConverterContexts.basic(Converters.numberNumber(),
                DateTimeContexts.fake(),
                DecimalNumberContexts.american(MathContext.DECIMAL32));
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<BasicExpressionFunctionContext> type() {
        return Cast.to(BasicExpressionFunctionContext.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
