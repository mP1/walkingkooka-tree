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

package walkingkooka.tree.select;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.ExpressionNumberConverterContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicNodeSelectorContextTest implements ClassTesting2<BasicNodeSelectorContext<TestNode, StringName, StringName, Object>>,
        NodeSelectorContextTesting<BasicNodeSelectorContext<TestNode, StringName, StringName, Object>,
                TestNode,
                StringName,
                StringName,
                Object> {

    private final static ExpressionNumberKind KIND = ExpressionNumberKind.DEFAULT;

    @Test
    public void testWithNullFinisher() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorContext.with(null,
                this.predicate(),
                this.mapper(),
                this.expressionEvaluationContext(),
                this.nodeType()));
    }

    @Test
    public void testWithNullFilter() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorContext.with(this.finisher(),
                null,
                this.mapper(),
                this.expressionEvaluationContext(),
                this.nodeType()));
    }

    @Test
    public void testWithNullSelectedFails() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorContext.with(this.finisher(),
                this.predicate(),
                null,
                this.expressionEvaluationContext(),
                this.nodeType()));
    }

    @Test
    public void testWithNullExpressionEvaluationContextFails() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorContext.with(this.finisher(),
                this.predicate(),
                this.mapper(),
                null,
                this.nodeType()));
    }

    @Test
    public void testWithNullNodeTypeFails() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorContext.with(this.finisher(),
                this.predicate(),
                this.mapper(),
                this.expressionEvaluationContext(),
                null));
    }

    @Test
    public void testEvaluate() {
        final BasicNodeSelectorContext<TestNode, StringName, StringName, Object> context = this.createContext();
        final int number = 123;

        this.checkEquals(
                KIND.create(number),
                context.evaluate(
                        Expression.value(KIND.create(number)
                        )
                )
        );
    }

    @Test
    public void testEvaluateAddition() {
        final BasicNodeSelectorContext<TestNode, StringName, StringName, Object> context = this.createContext();
        final int left = 123;
        final int right = 456;

        this.checkEquals(
                KIND.create(left + right),
                context.evaluate(
                        Expression.add(
                                Expression.value(KIND.create(left)),
                                Expression.value(KIND.create(right))
                        )
                )
        );
    }

    @Test
    public void testToString() {
        final BooleanSupplier finisher = this.finisher();
        final Predicate<TestNode> filter = this.predicate();
        final Function<TestNode, TestNode> mapper = this.mapper();
        final Function<NodeSelectorContext<TestNode, StringName, StringName, Object>, ExpressionEvaluationContext> expressionEvaluationContext = this.expressionEvaluationContext();

        this.toStringAndCheck(BasicNodeSelectorContext.with(finisher,
                filter,
                mapper,
                expressionEvaluationContext,
                this.nodeType()),
                finisher + " " + filter + " " + mapper + " " + expressionEvaluationContext);
    }

    @Override
    public String typeNameSuffix() {
        return NodeSelectorContext.class.getSimpleName();
    }

    @Override
    public BasicNodeSelectorContext<TestNode, StringName, StringName, Object> createContext() {
        return BasicNodeSelectorContext.with(this.finisher(),
                this.predicate(),
                this.mapper(),
                this.expressionEvaluationContext(),
                this.nodeType());
    }

    private BooleanSupplier finisher() {
        return () -> false;
    }

    private Predicate<TestNode> predicate() {
        return Predicates.always();
    }

    private Function<TestNode, TestNode> mapper() {
        return Function.identity();
    }

    private Function<NodeSelectorContext<TestNode, StringName, StringName, Object>, ExpressionEvaluationContext> expressionEvaluationContext() {
        return new Function<>() {
            @Override
            public ExpressionEvaluationContext apply(final NodeSelectorContext<TestNode, StringName, StringName, Object> context) {
                return ExpressionEvaluationContexts.basic(
                        KIND,
                        this.functions(),
                        this.exceptionHandler(),
                        this.references(),
                        ExpressionEvaluationContexts.referenceNotFound(),
                        CaseSensitivity.SENSITIVE,
                        this.converterContext()
                );
            }

            private Function<RuntimeException, Object> exceptionHandler() {
                return (r) -> {
                    throw r;
                };
            }

            private Function<FunctionExpressionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions() {
                return (n) -> {
                    throw new UnsupportedOperationException();
                };
            }

            private Function<ExpressionReference, Optional<Object>> references() {
                return (r) -> Optional.empty();
            }

            private ExpressionNumberConverterContext converterContext() {
                return ExpressionNumberConverterContexts.basic(ExpressionNumber.toConverter(Converters.fake()),
                        ConverterContexts.basic(Converters.fake(),
                                DateTimeContexts.fake(),
                                DecimalNumberContexts.fake()), KIND);
            }

            public String toString() {
                return "factory123";
            }
        };
    }

    private Class<TestNode> nodeType() {
        return TestNode.class;
    }

    @Override
    public Class<BasicNodeSelectorContext<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(BasicNodeSelectorContext.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
