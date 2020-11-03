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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContextTesting;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.ExpressionNumberConverterContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.select.parser.NodeSelectorAttributeName;

import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicNodeSelectorExpressionEvaluationContextTest implements ExpressionEvaluationContextTesting<BasicNodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object>> {

    private final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.DEFAULT;

    @BeforeEach
    public void beforeEachTest() {
        TestNode.clear();
    }

    @Test
    public void testWithNullNodeFails() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorExpressionEvaluationContext.with(null, (r) -> ExpressionEvaluationContexts.fake()));
    }

    @Test
    public void testWithNullContextFactoryFails() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorExpressionEvaluationContext.with(TestNode.with("test-node-123"), null));
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
    public void testReference() {
        final String attribute = "attribute123";
        final String value = "value123";
        final TestNode node = TestNode.with("node123")
                .setAttributes(Maps.of(Names.string(attribute), value));

        assertEquals(Expression.string(value),
                this.createContext(node)
                        .referenceOrFail(NodeSelectorAttributeName.with(attribute)));
    }

    @Override
    public BasicNodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object> createContext() {
        return this.createContext(TestNode.with("test-node-123"));
    }

    private BasicNodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object> createContext(final TestNode node) {
        return BasicNodeSelectorExpressionEvaluationContext.with(node,
                (r) -> ExpressionEvaluationContexts.basic(
                        EXPRESSION_NUMBER_KIND,
                        this.functions(),
                        r,
                        this.converter(),
                        this.converterContext()
                ));
    }

    private BiFunction<FunctionExpressionName, List<Object>, Object> functions() {
        return (n, p) -> {
            Objects.requireNonNull(n, "name");
            Objects.requireNonNull(p, "parameters");
            throw new IllegalArgumentException("Unknown function " + n);
        };
    }

    private Converter<ExpressionNumberConverterContext> converter() {
        return Converters.simple();
    }
    private ExpressionNumberConverterContext converterContext() {
        return ExpressionNumberConverterContexts.basic(ConverterContexts.basic(DateTimeContexts.fake(), this.decimalNumberContext()), EXPRESSION_NUMBER_KIND);
    }

    @Override
    public Class<BasicNodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(BasicNodeSelectorExpressionEvaluationContext.class);
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
