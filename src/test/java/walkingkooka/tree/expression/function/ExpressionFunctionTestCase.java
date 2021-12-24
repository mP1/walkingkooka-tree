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
import walkingkooka.Either;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.ExpressionNumberConverterContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.ExpressionPurityTesting;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.List;

public abstract class ExpressionFunctionTestCase<F extends ExpressionFunction<T, ExpressionFunctionContext>, T> implements ExpressionFunctionTesting<F, T, ExpressionFunctionContext>,
        ExpressionPurityTesting,
        ClassTesting2<F> {

    final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.DEFAULT;

    ExpressionFunctionTestCase() {
        super();
    }

    @Test
    public final void testIsPureTrue() {
        this.isPureAndCheck(
                this.createBiFunction(),
                new ExpressionPurityContext() {
                    @Override
                    public boolean isPure(final FunctionExpressionName name) {
                        throw new UnsupportedOperationException();
                    }
                },
                true
        );
    }


    final void apply2(final Object... parameters) {
        this.createBiFunction().apply(parameters(parameters), this.createContext());
    }

    final void applyAndCheck2(final List<Object> parameters,
                                final T result) {
        this.applyAndCheck2(this.createBiFunction(), parameters, result);
    }

    final void applyAndCheck2(final ExpressionFunction<T, ExpressionFunctionContext> function,
                              final List<Object> parameters,
                              final T result) {
        this.applyAndCheck2(function, parameters, this.createContext(), result);
    }

    @Override
    public ExpressionFunctionContext createContext() {
        return new FakeExpressionFunctionContext() {
            @Override
            public ExpressionNumberKind expressionNumberKind() {
                return EXPRESSION_NUMBER_KIND;
            }

            @Override
            public <TT> Either<TT, String> convert(final Object value, final Class<TT> target) {
                if (target.isInstance(value)) {
                    return Either.left(target.cast(value));
                }
                if (target == String.class) {
                    return Either.left(Cast.to(value.toString()));
                }

                final ExpressionNumberConverterContext context = ExpressionNumberConverterContexts.basic(Converters.fake(),
                        ConverterContexts.fake(),
                        this.expressionNumberKind());

                if (value instanceof String && Number.class.isAssignableFrom(target)) {
                    final Double doubleValue = Double.parseDouble((String) value);
                    return ExpressionNumber.toConverter(ExpressionNumber.fromConverter(Converters.numberNumber()))
                            .convert(doubleValue, target, context);
                }
                if (target == Boolean.class) {
                    return Either.left(Cast.to(Boolean.parseBoolean(value.toString())));
                }
                return ExpressionNumber.toConverter(ExpressionNumber.fromConverter(Converters.numberNumber()))
                        .convert(value, target, context);
            }

        };
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
