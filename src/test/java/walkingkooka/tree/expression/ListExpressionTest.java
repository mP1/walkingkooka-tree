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
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.visit.Visiting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ListExpressionTest extends VariableExpressionTestCase<ListExpression> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<Expression> visited = Lists.array();

        final ListExpression function = this.createExpression();
        final Expression text1 = function.children().get(0);
        final Expression text2 = function.children().get(1);
        final Expression text3 = function.children().get(2);

        new FakeExpressionVisitor() {
            @Override
            protected Visiting startVisit(final Expression n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Expression n) {
                b.append("2");
                visited.add(n);
            }

            @Override
            protected Visiting startVisit(final ListExpression t) {
                assertSame(function, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ListExpression t) {
                assertSame(function, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final StringExpression t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(function);
        assertEquals("1315215215242", b.toString());
        assertEquals(Lists.of(function, function,
                text1, text1, text1,
                text2, text2, text2,
                text3, text3, text3,
                function, function),
                visited,
                "visited");
    }

    // Evaluation ...................................................................................................

    @Test
    public void testEmptyToBooleanFalse() {
        this.evaluateAndCheckBoolean(ListExpression.with(Lists.empty()), this.context(), false);
    }

    @Test
    public void testNotEmptyToBooleanTrue() {
        this.evaluateAndCheckBoolean(this.createExpression(), this.context(), true);
    }

    @Test
    public void testToBigDecimal() {
        final BigDecimal value = BigDecimal.valueOf(123);
        this.evaluateAndCheckBigDecimal(this.createExpression(), context(value), value);
    }

    @Test
    public void testToBigInteger() {
        final BigInteger value = BigInteger.valueOf(123);
        this.evaluateAndCheckBigInteger(this.createExpression(), context(value), value);
    }

    @Test
    public void testToDouble() {
        final Double value = 123.5;
        this.evaluateAndCheckDouble(this.createExpression(), context(value), value);
    }

    @Test
    public void testToLong() {
        final Long value = 123L;
        this.evaluateAndCheckLong(this.createExpression(), context(value), value);
    }

    @Test
    public void testToNumber() {
        final BigDecimal value = BigDecimal.valueOf(123);
        this.evaluateAndCheckNumberBigDecimal(this.createExpression(), context(value), value.doubleValue());
    }

    @Test
    public void testToText() {
        this.evaluateAndCheckText(this.createExpression(), this.context(), "[child-111, child-222, child-333]");
    }

    static ExpressionEvaluationContext context(final Object convertedValue) {
        return new FakeExpressionEvaluationContext() {

            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> target) {
                return Either.left(target.cast(convertedValue));
            }
        };
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        assertEquals("[\"child-111\",\"child-222\",\"child-333\"]", this.createExpression().toString());
    }

    @Override
    ListExpression createExpression(final List<Expression> children) {
        return ListExpression.with(children);
    }

    @Override
    Class<ListExpression> expressionType() {
        return ListExpression.class;
    }
}
