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
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.ExpressionNumberConverterContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;
import walkingkooka.tree.select.parser.NodeSelectorAttributeName;

import java.math.MathContext;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicNodeSelectorExpressionEvaluationContextTest implements NodeSelectorExpressionEvaluationContextTesting<BasicNodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object>, TestNode, StringName, StringName, Object> {

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
    public void testConvert() {
        this.convertAndCheck(123, Float.class, 123f);
    }

    @Test
    public void testEvaluateExpressionTrue() {
        this.evaluateExpressionAndCheck2(true);
    }

    @Test
    public void testEvaluateExpressionFalse() {
        this.evaluateExpressionAndCheck2(false);
    }

    private void evaluateExpressionAndCheck2(final boolean value) {
        this.evaluateExpressionAndCheck(
            Expression.value(value),
            value
        );
    }

    @Test
    public void testEvaluateExpressionString() {
        final String value = "abc123";
        this.evaluateExpressionAndCheck(
            Expression.value(value),
            value
        );
    }

    @Override
    public void testEvaluateFunctionWithNullParametersFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testReference() {
        final String attribute = "attribute123";
        final String value = "value123";
        final TestNode node = TestNode.with("node123")
            .setAttributes(
                Maps.of(
                    Names.string(attribute),
                    value
                )
            );

        this.referenceAndCheck(
            this.createContext(node),
            NodeSelectorAttributeName.with(attribute),
            value
        );
    }

    @Override
    public void testEnterScopeGivesDifferentInstance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BasicNodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object> createContext() {
        return this.createContext(TestNode.with("test-node-123"));
    }

    private BasicNodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object> createContext(final TestNode node) {
        return BasicNodeSelectorExpressionEvaluationContext.with(
            node,
            (r) -> ExpressionEvaluationContexts.basic(
                EXPRESSION_NUMBER_KIND,
                this.functions(),
                this.exceptionHandler(),
                this.references(),
                ExpressionEvaluationContexts.referenceNotFound(),
                CaseSensitivity.SENSITIVE,
                this.converterContext()
            ));
    }

    private Function<RuntimeException, Object> exceptionHandler() {
        return (r) -> {
            throw r;
        };
    }

    private Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions() {
        return (n) -> {
            Objects.requireNonNull(n, "name");
            throw new UnknownExpressionFunctionException(n);
        };
    }

    private Function<ExpressionReference, Optional<Optional<Object>>> references() {
        return (r -> {
            throw new UnsupportedOperationException();
        });
    }

    private ExpressionNumberConverterContext converterContext() {
        return ExpressionNumberConverterContexts.basic(
            Converters.numberToNumber(),
            ConverterContexts.basic(
                Converters.JAVA_EPOCH_OFFSET, // dateOffset
                Converters.fake(),
                DateTimeContexts.fake(),
                decimalNumberContext()
            ),
            EXPRESSION_NUMBER_KIND
        );
    }

    // FunctionContextTesting..........................................................................................

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
    public char groupSeparator() {
        return this.decimalNumberContext().groupSeparator();
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
    public char percentSymbol() {
        return this.decimalNumberContext().percentSymbol();
    }

    @Override
    public char positiveSign() {
        return this.decimalNumberContext().positiveSign();
    }

    private DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.american(MathContext.DECIMAL32);
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<BasicNodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(BasicNodeSelectorExpressionEvaluationContext.class);
    }
}
