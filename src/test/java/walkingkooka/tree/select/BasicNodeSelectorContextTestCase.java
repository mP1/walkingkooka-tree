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

package walkingkooka.tree.select;

import walkingkooka.Context;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.ExpressionNumberConverterContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionContext;
import walkingkooka.tree.expression.function.FakeExpressionFunction;

import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class BasicNodeSelectorContextTestCase<C extends Context> implements ClassTesting<C> {

    final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.DEFAULT;

    BasicNodeSelectorContextTestCase() {
        super();
    }

    final static String FUNCTION_NAME = "hello";
    final static List<Object> PARAMETERS = Lists.of(1, 2, 3, 4, 5);
    final static String FUNCTION_RESULT = "12345";

    private Function<FunctionExpressionName, Optional<ExpressionFunction<?>>> functions() {
        return (f) -> {
            return FUNCTION_NAME.equals(f.value()) ?
                    Optional.of(this.hello()) :
                    Optional.empty();
        };
    }

    private ExpressionFunction<String> hello() {
        return new FakeExpressionFunction<>() {
            @Override
            public String apply(final List<Object> parameters, final ExpressionFunctionContext context) {
                assertEquals(PARAMETERS, parameters);
                return FUNCTION_RESULT;
            }
        };
    }

    private Converter<ExpressionNumberConverterContext> converter() {
        return Converters.stringNumber((c) -> (DecimalFormat) DecimalFormat.getInstance());
    }

    private ExpressionNumberConverterContext expressionNumberConverterContext() {
        return ExpressionNumberConverterContexts.basic(ConverterContexts.basic(DateTimeContexts.fake(), this.decimalNumberContext()), EXPRESSION_NUMBER_KIND);
    }

    public final C createContext() {
        return this.createContext(this.functions(), this.converter(), this.expressionNumberConverterContext());
    }

    abstract C createContext(final Function<FunctionExpressionName, Optional<ExpressionFunction<?>>> functions,
                             final Converter<ExpressionNumberConverterContext> converter,
                             final ExpressionNumberConverterContext context);

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    final DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.american(MathContext.DECIMAL32);
    }
}
