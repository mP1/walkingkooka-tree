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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.convert.Converter;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionEvaluationContextTesting;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.math.MathContext;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicNodeSelectorContextExpressionEvaluationContextTest extends BasicNodeSelectorContextTestCase<BasicNodeSelectorContextExpressionEvaluationContext>
        implements ExpressionEvaluationContextTesting<BasicNodeSelectorContextExpressionEvaluationContext> {

    @Test
    public void testFunction() {
        assertEquals(FUNCTION_RESULT, this.createContext().function(FunctionExpressionName.with(FUNCTION_NAME), PARAMETERS));
    }

    @Test
    public void testFunctionUnknownFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createContext().function(FunctionExpressionName.with("unknownFunction123"), ExpressionEvaluationContext.NO_PARAMETERS);
        });
    }

    @Test
    public void testConvert() {
        assertEquals(Either.left(123), this.createContext().convert("123", Integer.class));
    }

    @Override
    public BasicNodeSelectorContextExpressionEvaluationContext createContext(final Function<FunctionExpressionName, Optional<ExpressionFunction<?>>>functions,
                                                                             final Converter<ExpressionNumberConverterContext> converter,
                                                                             final ExpressionNumberConverterContext context) {
        return BasicNodeSelectorContextExpressionEvaluationContext.with(BasicNodeSelectorContextExpressionFunctionContext.with(functions, converter, context));
    }

    @Override
    public Class<BasicNodeSelectorContextExpressionEvaluationContext> type() {
        return Cast.to(BasicNodeSelectorContextExpressionEvaluationContext.class);
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
        return this.decimalNumberContext().mathContext();
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
}
