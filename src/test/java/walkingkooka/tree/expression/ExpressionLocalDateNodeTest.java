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
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.visit.Visiting;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionLocalDateNodeTest extends ExpressionLeafNodeTestCase<ExpressionLocalDateNode, LocalDate> {

    private final static int VALUE = 123;
    private final String DATE_STRING = "2000-01-02";
    private final String DIFFERENT_DATE_STRING = "1999-12-31";

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final ExpressionLocalDateNode node = this.createExpressionNode();

        new FakeExpressionNodeVisitor() {
            @Override
            protected Visiting startVisit(final ExpressionNode n) {
                assertSame(node, n);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionNode n) {
                assertSame(node, n);
                b.append("2");
            }

            @Override
            protected void visit(final ExpressionLocalDateNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        assertEquals("132", b.toString());
    }

    // Evaluation ...................................................................................................

    @Test
    public void testToBooleanFalse() {
        this.evaluateAndCheckBoolean(this.createExpressionNode(0), false);
    }

    @Test
    public void testToBooleanTrue() {
        this.evaluateAndCheckBoolean(this.createExpressionNode(1), true);
    }

    @Test
    public void testToBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(), VALUE);
    }

    @Test
    public void testToBigInteger() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(), VALUE);
    }

    @Test
    public void testToDouble() {
        this.evaluateAndCheckDouble(this.createExpressionNode(), VALUE);
    }

    @Test
    public void testToLocalDate() {
        this.evaluateAndCheckLocalDate(this.createExpressionNode(), this.value());
    }

    @Test
    public void testToLocalDateTime() {
        this.evaluateAndCheckLocalDateTime(this.createExpressionNode(),
                Converters.localDateLocalDateTime()
                        .convertOrFail(this.value(), LocalDateTime.class, ConverterContexts.fake()));
    }

    @Test
    public void testToLocalTime() {
        assertThrows(ExpressionEvaluationException.class, () -> this.createExpressionNode().toLocalTime(context()));
    }

    @Test
    public void testToLong() {
        this.evaluateAndCheckLong(this.createExpressionNode(), VALUE);
    }

    @Test
    public void testToText() {
        this.evaluateAndCheckText(this.createExpressionNode(DATE_STRING), DATE_STRING);
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createExpressionNode(DATE_STRING), DATE_STRING);
    }

    @Test
    public void testToString2() {
        this.toStringAndCheck(this.createExpressionNode(LocalDate.parse(DIFFERENT_DATE_STRING)), DIFFERENT_DATE_STRING);
    }

    private ExpressionLocalDateNode createExpressionNode(final String value) {
        return this.createExpressionNode(LocalDate.parse(value));
    }

    private ExpressionLocalDateNode createExpressionNode(final long value) {
        return this.localDate(value);
    }

    @Override
    ExpressionLocalDateNode createExpressionNode(final LocalDate value) {
        return ExpressionLocalDateNode.with(value);
    }

    @Override
    LocalDate value() {
        return LocalDate.ofEpochDay(VALUE);
    }

    @Override
    LocalDate differentValue() {
        return LocalDate.parse(DIFFERENT_DATE_STRING);
    }

    @Override
    Class<ExpressionLocalDateNode> expressionNodeType() {
        return ExpressionLocalDateNode.class;
    }
}
