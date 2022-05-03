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
import walkingkooka.collect.set.Sets;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionContext;
import walkingkooka.tree.expression.function.ExpressionFunctionContexts;
import walkingkooka.tree.expression.function.ExpressionFunctionKind;
import walkingkooka.tree.expression.function.FakeExpressionFunction;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicExpressionEvaluationContextTest implements ClassTesting2<BasicExpressionEvaluationContext>,
        ExpressionEvaluationContextTesting<BasicExpressionEvaluationContext>,
        ToStringTesting<BasicExpressionEvaluationContext> {

    private final static ExpressionReference REFERENCE = new ExpressionReference() {
    };

    private final static Object REFERENCE_VALUE = "expression node 123";

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;

    @Test
    public void testWithNullFunctionContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> BasicExpressionEvaluationContext.with(
                        null
                )
        );
    }

    // evaluate function................................................................................................

    @Test
    public void testEvaluate() {
        this.evaluateAndCheck(
                this.functionName(),
                this.parameters(),
                this.functionValue()
        );
    }

    // evaluate expressions.............................................................................................

    @Test
    public void testEvaluateTrue() {
        this.evaluateAndCheck2(true);
    }

    @Test
    public void testEvaluateFalse() {
        this.evaluateAndCheck2(false);
    }

    private void evaluateAndCheck2(final boolean value) {
        this.evaluateAndCheck(
                Expression.value(value),
                value
        );
    }

    @Test
    public void testEvaluateString() {
        final String value = "abc123";
        this.evaluateAndCheck(
                Expression.value(value),
                value
        );
    }

    // string equals....................................................................................................

    @Test
    public void testEvaluateStringEqualsCaseSensitive() {
        this.evaluateStringEqualsAndCheck(
                "abc",
                "abc",
                CaseSensitivity.SENSITIVE,
                true
        );
    }

    @Test
    public void testEvaluateStringEqualsCaseSensitiveCaseDifferent() {
        this.evaluateStringEqualsAndCheck(
                "abc",
                "ABC",
                CaseSensitivity.SENSITIVE,
                false
        );
    }

    @Test
    public void testEvaluateStringEqualsCaseInsensitiveCaseDifferent() {
        this.evaluateStringEqualsAndCheck(
                "abc",
                "ABC",
                CaseSensitivity.INSENSITIVE,
                true
        );
    }

    @Test
    public void testEvaluateStringEqualsCaseSensitiveDifferent() {
        this.evaluateStringEqualsAndCheck(
                "abc",
                "different",
                CaseSensitivity.SENSITIVE,
                false
        );
    }

    private void evaluateStringEqualsAndCheck(final String left,
                                              final String right,
                                              final CaseSensitivity caseSensitivity,
                                              final boolean expected) {
        this.evaluateAndCheck(
                this.createContext(true, caseSensitivity),
                Expression.equalsExpression(
                        Expression.value(left),
                        Expression.value(right)
                ),
                expected
        );
    }

    // string notEquals................................................................................................

    @Test
    public void testEvaluateStringNotEqualsCaseSensitive() {
        this.evaluateStringNotEqualsAndCheck(
                "abc",
                "abc",
                CaseSensitivity.SENSITIVE,
                false
        );
    }

    @Test
    public void testEvaluateStringNotEqualsCaseSensitiveCaseDifferent() {
        this.evaluateStringNotEqualsAndCheck(
                "abc",
                "ABC",
                CaseSensitivity.SENSITIVE,
                true
        );
    }

    @Test
    public void testEvaluateStringNotEqualsCaseInsensitiveCaseDifferent() {
        this.evaluateStringNotEqualsAndCheck(
                "abc",
                "ABC",
                CaseSensitivity.INSENSITIVE,
                false
        );
    }

    @Test
    public void testEvaluateStringNotEqualsCaseSensitiveDifferent() {
        this.evaluateStringNotEqualsAndCheck(
                "abc",
                "different",
                CaseSensitivity.SENSITIVE,
                true
        );
    }

    private void evaluateStringNotEqualsAndCheck(final String left,
                                                 final String right,
                                                 final CaseSensitivity stringNotEqualsCaseSensitivity,
                                                 final boolean expected) {
        this.evaluateAndCheck(
                this.createContext(true, stringNotEqualsCaseSensitivity),
                Expression.notEquals(
                        Expression.value(left),
                        Expression.value(right)
                ),
                expected
        );
    }

    // isPure..........................................................................................................

    @Test
    public void testIsPureTrue() {
        this.isPureAndCheck2(true);
    }

    @Test
    public void testIsPureFalse() {
        this.isPureAndCheck2(false);
    }

    private void isPureAndCheck2(final boolean pure) {
        this.isPureAndCheck(
                this.createContext(pure, CASE_SENSITIVITY),
                this.functionName(),
                pure
        );
    }

    @Test
    public void testReferences() {
        this.checkEquals(
                Optional.of(REFERENCE_VALUE),
                this.createContext()
                        .reference(REFERENCE)
        );
    }

    @Test
    public void testConvert() {
        this.convertAndCheck(123.0, Long.class, 123L);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final ExpressionFunctionContext functionContext = this.functionContext(
                true,
                CaseSensitivity.SENSITIVE
        );

        this.toStringAndCheck(
                BasicExpressionEvaluationContext.with(
                        functionContext
                ),
                functionContext.toString()
        );
    }

    @Override
    public BasicExpressionEvaluationContext createContext() {
        return this.createContext(true, CASE_SENSITIVITY);
    }

    private BasicExpressionEvaluationContext createContext(final boolean pure,
                                                           final CaseSensitivity caseSensitivity) {
        return BasicExpressionEvaluationContext.with(
                this.functionContext(
                        pure,
                        caseSensitivity
                )
        );
    }

    private ExpressionFunctionContext functionContext(final boolean isPure,
                                                      final CaseSensitivity caseSensitivity) {
        return ExpressionFunctionContexts.basic(
                ExpressionNumberKind.DEFAULT,
                this.functions(isPure),
                this.references(),
                ExpressionFunctionContexts.referenceNotFound(),
                caseSensitivity,
                this.converterContext()
        );
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

                    return BasicExpressionEvaluationContextTest.this.functionValue();
                }

                @Override
                public boolean isPure(final ExpressionPurityContext context) {
                    return pure;
                }

                @Override
                public Set<ExpressionFunctionKind> kinds() {
                    return Sets.empty();
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
            return Optional.of(REFERENCE_VALUE);
        });
    }

    private ConverterContext converterContext() {
        return ConverterContexts.basic(
                Converters.collection(
                        Lists.of(
                                Converters.numberNumber(),
                                Converters.simple()
                        )
                ),
                DateTimeContexts.fake(),
                DecimalNumberContexts.american(MathContext.DECIMAL32)
        );
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
