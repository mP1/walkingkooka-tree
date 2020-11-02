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

import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContextTesting;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.ExpressionNumberConverterContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class BasicNodeSelectorExpressionEvaluationContextTest implements ExpressionEvaluationContextTesting<BasicNodeSelectorExpressionEvaluationContext> {

    private final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.DEFAULT;

    @Override
    public BasicNodeSelectorExpressionEvaluationContext createContext() {
        return BasicNodeSelectorExpressionEvaluationContext.with(null, ExpressionEvaluationContexts.basic(
                EXPRESSION_NUMBER_KIND,
                this.functions(),
                this.references(),
                this.converter(),
                this.converterContext()

        ));
    }

    final BiFunction<FunctionExpressionName, List<Object>, Object> functions() {
        return (n, p) -> {
            Objects.requireNonNull(n, "name");
            Objects.requireNonNull(p, "parameters");
            throw new IllegalArgumentException("Unknown function " + n);
        };
    }

    private Function<ExpressionReference, Optional<Expression>> references() {
        return r -> {
            throw new UnsupportedOperationException();
        };
    }

    private Converter<ExpressionNumberConverterContext> converter() {
        return Converters.simple();
    }
    private ExpressionNumberConverterContext converterContext() {
        return ExpressionNumberConverterContexts.basic(ConverterContexts.basic(DateTimeContexts.fake(), this.decimalNumberContext()), EXPRESSION_NUMBER_KIND);
    }

    @Override
    public Class<BasicNodeSelectorExpressionEvaluationContext> type() {
        return BasicNodeSelectorExpressionEvaluationContext.class;
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
    
    private DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.american(MathContext.DECIMAL32);
    }
}
