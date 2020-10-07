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
import walkingkooka.collect.list.Lists;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.predicate.Predicates;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.ParserException;
import walkingkooka.text.cursor.parser.ParserReporterException;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionVisitorTesting;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.select.parser.NodeSelectorParserContexts;
import walkingkooka.tree.select.parser.NodeSelectorParsers;
import walkingkooka.tree.select.parser.NodeSelectorPredicateParserToken;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNodeSelectorToStringExpressionVisitorTest implements ExpressionVisitorTesting<ExpressionNodeSelectorToStringExpressionVisitor> {

    @Test
    public void testNumber() {
        this.parseAndStringExpressionCheck("123");
    }

    @Test
    public void testNumber2() {
        this.parseAndStringExpressionCheck("123.75");
    }

    @Test
    public void testNegativeNumber() {
        this.parseAndStringExpressionCheck("-123");
    }

    @Test
    public void testBigDecimal() {
        this.toStringAndCheck(Expression.bigDecimal(BigDecimal.valueOf(123.75)), "123.75");
    }

    @Test
    public void testBigInteger() {
        this.toStringAndCheck(Expression.bigInteger(BigInteger.valueOf(99)), "99");
    }

    @Test
    public void testDouble() {
        this.toStringAndCheck(Expression.doubleExpression(123.75), "123.75");
    }

    @Test
    public void testLong() {
        this.toStringAndCheck(Expression.longExpression(99L), "99");
    }

    @Test
    public void testLocalDate() {
        this.toStringAndCheck(Expression.localDate(LocalDate.of(2000, 12, 31)),
                "localDate(\"2000-12-31\")");
    }

    @Test
    public void testLocalDateTime() {
        this.toStringAndCheck(Expression.localDateTime(LocalDateTime.of(2000, 12, 31, 12, 58, 59, 999)),
                "localDateTime(\"2000-12-31T12:58:59.000000999\")");
    }

    @Test
    public void testLocalTime() {
        this.toStringAndCheck(Expression.localTime(LocalTime.of(12, 58, 59, 999)),
                "localTime(\"12:58:59.000000999\")");
    }

    @Test
    public void testString() {
        this.parseAndStringExpressionCheck("\"abc\"");
    }

    @Test
    public void testString2() {
        this.toStringAndCheck(Expression.string("abc"), "\"abc\"");
    }

    @Test
    public void testStringRequiresEscaping() {
        this.toStringAndCheck(Expression.string("ab\tc"), "\"ab\\tc\"");
    }

    @Test
    public void testStringRequiresEscaping2() {
        this.parseAndStringExpressionCheck("\"a\\tbc\"");
    }

    @Test
    public void testBooleanTrue() {
        this.toStringAndCheck(Expression.booleanExpression(true), "true()");
    }

    @Test
    public void testBooleanFalse() {
        this.toStringAndCheck(Expression.booleanExpression(false), "false()");
    }

    @Test
    public void testAdd() {
        this.parseAndStringExpressionCheck("1+2");
    }

    @Test
    public void testSubtract() {
        this.parseAndStringExpressionCheck("1+2");
    }

    @Test
    public void testMultiply() {
        this.parseAndStringExpressionCheck("1*2");
    }

    @Test
    public void testDivide() {
        this.parseAndStringExpressionCheck("1 div 2");
    }

    @Test
    public void testMod() {
        this.parseAndStringExpressionCheck("1 mod 2");
    }

    @Test
    public void testEquals() {
        this.parseAndStringExpressionCheck("1=2");
    }

    @Test
    public void testGreaterThanEquals() {
        this.parseAndStringExpressionCheck("1>=2");
    }

    @Test
    public void testGreaterThan() {
        this.parseAndStringExpressionCheck("1>2");
    }

    @Test
    public void testLessThanEquals() {
        this.parseAndStringExpressionCheck("1<=2");
    }

    @Test
    public void testLessThan() {
        this.parseAndStringExpressionCheck("1<2");
    }

    @Test
    public void testNotEquals() {
        this.parseAndStringExpressionCheck("1!=2");
    }

    @Test
    public void testAnd() {
        this.parseAndStringExpressionCheck("1 and 2");
    }

    @Test
    public void testOr() {
        this.parseAndStringExpressionCheck("1 or 2");
    }

    @Test
    public void testNot() {
        this.parseAndStringExpressionCheck("not(1)");
    }

    @Test
    public void testNot2() {
        this.toStringAndCheck(Expression.not(Expression.longExpression(1)), "not(1)");
    }

    @Test
    public void testList() {
        assertThrows(ParserReporterException.class, () -> this.parseOrFail("[1,\"abc\", 3]"));
    }

    @Test
    public void testFunction() {
        this.parseAndStringExpressionCheck("fx()");
    }

    @Test
    public void testFunctionWithArguments() {
        this.parseAndStringExpressionCheck("fx(1,2)");
    }

    @Test
    public void testFunctionWithArguments2() {
        this.toStringAndCheck(Expression.function(
                FunctionExpressionName.with("fx"),
                Lists.of(
                        Expression.longExpression(1),
                        Expression.bigDecimal(BigDecimal.valueOf(2.5))
                )),
                "fx(1,2.5)");
    }

    @Test
    public void testFunctionStartsWithStringLiteral() {
        this.parseAndStringExpressionCheck("string-length(\"abc\")");
    }

    @Test
    public void testNestedFunctions() {
        this.parseAndStringExpressionCheck("sum(\"abc\",random(2,skip(3)))");
    }

    @Test
    public void testAttribute() {
        this.parseAndStringExpressionCheck("@id");
    }

    @Test
    public void testAttribute2() {
        this.parseAndStringExpressionCheck("@id=\"z\"");
    }

    private void parseAndStringExpressionCheck(final String expression) {
        final NodeSelectorPredicateParserToken parsed = parseOrFail(expression);

        final Expression expressionObject = parsed.toExpression(Predicates.always());

        assertEquals(expression,
                ExpressionNodeSelectorToStringExpressionVisitor.toString(expressionObject),
                () -> "Input expression: " + CharSequences.quoteAndEscape(expression) + "\n" + parsed + "\n" + expressionObject);
    }

    private NodeSelectorPredicateParserToken parseOrFail(final String expression) {
        return NodeSelectorParsers.predicate()
                .orReport(ParserReporters.basic())
                .orFailIfCursorNotEmpty(ParserReporters.basic())
                .parse(TextCursors.charSequence(expression), NodeSelectorParserContexts.basic(DecimalNumberContexts.american(MathContext.DECIMAL32)))
                .orElseThrow(() -> new ParserException("Failed to parse " + CharSequences.quoteAndEscape(expression)))
                .cast(NodeSelectorPredicateParserToken.class);
    }

    private void toStringAndCheck(final Expression node,
                                  final String expression) {
        assertEquals(expression,
                ExpressionNodeSelectorToStringExpressionVisitor.toString(node),
                () -> "Input expression: " + CharSequences.quoteAndEscape(expression) + "\n" + node);
    }

    @Override
    public ExpressionNodeSelectorToStringExpressionVisitor createVisitor() {
        return new ExpressionNodeSelectorToStringExpressionVisitor();
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return ExpressionNodeSelector.class.getSimpleName() + "ToString";
    }

    @Override
    public Class<ExpressionNodeSelectorToStringExpressionVisitor> type() {
        return ExpressionNodeSelectorToStringExpressionVisitor.class;
    }
}
