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
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentContexts;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.convert.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.convert.ExpressionNumberConverterContexts;
import walkingkooka.tree.expression.convert.ExpressionNumberConverters;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Locale;
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

    private final static BooleanSupplier FINISHER = () -> false;

    private final static Predicate<TestNode> PREDICATE = Predicates.always();

    private final static Function<TestNode, TestNode> MAPPER = Function.identity();

    private final static ExpressionNumberConverterContext CONVERTER_CONTEXT = ExpressionNumberConverterContexts.basic(
        ExpressionNumberConverters.toNumberOrExpressionNumber(
            Converters.fake()
        ),
        ConverterContexts.basic(
            (l) -> {
                throw new UnsupportedOperationException();
            }, // canDateTimeSymbolsForLocale
            (l) -> {
                throw new UnsupportedOperationException();
            }, // canDecimalNumberSymbolsForLocale
            false, // canNumbersHaveGroupSeparator
            Converters.JAVA_EPOCH_OFFSET, // dateOffset
            Indentation.SPACES2,
            LineEnding.NL,
            ',', // valueSeparator
            Converters.fake(),
            DateTimeContexts.fake(),
            DecimalNumberContexts.fake()
        ),
        KIND
    );

    private final static Function<NodeSelectorContext<TestNode, StringName, StringName, Object>, ExpressionEvaluationContext> EXPRESSION_EVALUATION_CONTEXT_FACTORY = new Function<>() {
        @Override
        public ExpressionEvaluationContext apply(final NodeSelectorContext<TestNode, StringName, StringName, Object> context) {
            return ExpressionEvaluationContexts.basic(
                KIND,
                (e, c) -> {
                    throw new UnsupportedOperationException();
                },
                this.functions(),
                this.exceptionHandler(),
                this.references(),
                ExpressionEvaluationContexts.referenceNotFound(),
                CaseSensitivity.SENSITIVE,
                CONVERTER_CONTEXT,
                EnvironmentContexts.map(
                    EnvironmentContexts.empty(
                        Currency.getInstance("AUD"),
                        Indentation.SPACES2,
                        LineEnding.NL,
                        Locale.ENGLISH,
                        LocalDateTime::now,
                        EnvironmentContext.ANONYMOUS
                    )
                ),
                LocaleContexts.fake()
            );
        }

        private Function<RuntimeException, Object> exceptionHandler() {
            return (r) -> {
                throw r;
            };
        }

        private Function<ExpressionFunctionName, ExpressionFunction<?, ExpressionEvaluationContext>> functions() {
            return (n) -> {
                throw new UnsupportedOperationException();
            };
        }

        private Function<ExpressionReference, Optional<Optional<Object>>> references() {
            return (r) -> Optional.empty();
        }

        @Override
        public String toString() {
            return "factory123";
        }
    };

    private final static Class<TestNode> NODE_TYPE = TestNode.class;

    @Test
    public void testWithNullFinisher() {
        assertThrows(
            NullPointerException.class,
            () -> BasicNodeSelectorContext.with(null,
                PREDICATE,
                MAPPER,
                EXPRESSION_EVALUATION_CONTEXT_FACTORY,
                NODE_TYPE
            )
        );
    }

    @Test
    public void testWithNullFilter() {
        assertThrows(
            NullPointerException.class,
            () -> BasicNodeSelectorContext.with(
                FINISHER,
                null,
                MAPPER,
                EXPRESSION_EVALUATION_CONTEXT_FACTORY,
                NODE_TYPE
            )
        );
    }

    @Test
    public void testWithNullSelectedFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicNodeSelectorContext.with(
                FINISHER,
                PREDICATE,
                null,
                EXPRESSION_EVALUATION_CONTEXT_FACTORY,
                NODE_TYPE
            )
        );
    }

    @Test
    public void testWithNullExpressionEvaluationContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicNodeSelectorContext.with(
                FINISHER,
                PREDICATE,
                MAPPER,
                null,
                NODE_TYPE
            )
        );
    }

    @Test
    public void testWithNullNodeTypeFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicNodeSelectorContext.with(
                FINISHER,
                PREDICATE,
                MAPPER,
                EXPRESSION_EVALUATION_CONTEXT_FACTORY,
                null
            )
        );
    }

    @Test
    public void testEvaluate() {
        final BasicNodeSelectorContext<TestNode, StringName, StringName, Object> context = this.createContext();
        final int number = 123;

        this.checkEquals(
            KIND.create(number),
            context.evaluate(
                Expression.value(
                    KIND.create(number)
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
                    Expression.value(
                        KIND.create(left)
                    ),
                    Expression.value(
                        KIND.create(right)
                    )
                )
            )
        );
    }

    @Test
    public void testToString() {
        final BooleanSupplier finisher = FINISHER;
        final Predicate<TestNode> filter = PREDICATE;
        final Function<TestNode, TestNode> mapper = MAPPER;
        final Function<NodeSelectorContext<TestNode, StringName, StringName, Object>, ExpressionEvaluationContext> context = EXPRESSION_EVALUATION_CONTEXT_FACTORY;

        this.toStringAndCheck(BasicNodeSelectorContext.with(
                finisher,
                filter,
                mapper,
                context,
                NODE_TYPE),
            finisher + " " + filter + " " + mapper + " " + context
        );
    }

    @Override
    public BasicNodeSelectorContext<TestNode, StringName, StringName, Object> createContext() {
        return BasicNodeSelectorContext.with(
            FINISHER,
            PREDICATE,
            MAPPER,
            EXPRESSION_EVALUATION_CONTEXT_FACTORY,
            NODE_TYPE
        );
    }

    // class............................................................................................................

    @Override
    public Class<BasicNodeSelectorContext<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(BasicNodeSelectorContext.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
