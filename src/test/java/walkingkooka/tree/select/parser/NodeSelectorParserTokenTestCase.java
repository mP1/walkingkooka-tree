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
package walkingkooka.tree.select.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.IsMethodTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticFactoryTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenTesting;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.select.NodeSelector;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class NodeSelectorParserTokenTestCase<T extends NodeSelectorParserToken> implements ClassTesting2<T>,
        IsMethodTesting<T>,
        ParserTokenTesting<T> {

    final ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.DEFAULT;

    NodeSelectorParserTokenTestCase() {
        super();
    }

    @Test
    public final void testPublicStaticFactoryMethod() {
        PublicStaticFactoryTesting.checkFactoryMethods(NodeSelectorParserToken.class,
                NodeSelector.class.getSimpleName(),
                ParserToken.class.getSimpleName(),
                this.type());
    }

    @Test
    public final void testEmptyTextFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createToken(""));
    }

    @Test
    public final void testAccept2() {
        new NodeSelectorParserTokenVisitor() {
        }.accept(this.createToken());
    }

    // token factories...............................................................................

    final NodeSelectorParserToken absolute() {
        return NodeSelectorParserToken.absolute("/", "/");
    }

    final NodeSelectorAdditionParserToken addition(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.addition(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken ancestor() {
        return NodeSelectorParserToken.ancestor("ancestor::", "ancestor::");
    }

    final NodeSelectorParserToken ancestorOrSelf() {
        return NodeSelectorParserToken.ancestorOrSelf("ancestor-or-self::", "ancestor-or-self::");
    }

    final NodeSelectorParserToken and(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.and(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken andSymbol() {
        return NodeSelectorParserToken.andSymbol("and", "and");
    }

    final NodeSelectorParserToken atSign() {
        return NodeSelectorParserToken.atSignSymbol("@", "@");
    }

    final NodeSelectorAttributeParserToken attribute(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.attribute(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorAttributeNameParserToken attributeName() {
        return attributeName("attribute1");
    }

    final NodeSelectorAttributeNameParserToken attributeName2() {
        return attributeName("attribute2");
    }

    final NodeSelectorAttributeNameParserToken attributeName(final String name) {
        return NodeSelectorParserToken.attributeName(NodeSelectorAttributeName.with(name), name);
    }

    final NodeSelectorParserToken bracketOpen() {
        return NodeSelectorParserToken.bracketOpenSymbol("[", "[");
    }

    final NodeSelectorParserToken bracketClose() {
        return NodeSelectorParserToken.bracketCloseSymbol("]", "]");
    }

    final NodeSelectorParserToken child() {
        return NodeSelectorParserToken.child("child::", "child::");
    }

    final NodeSelectorParserToken comma() {
        return NodeSelectorParserToken.parameterSeparatorSymbol(",", ",");
    }

    final NodeSelectorParserToken descendant() {
        return NodeSelectorParserToken.descendant("descendant::", "descendant::");
    }

    final NodeSelectorParserToken descendantOrSelf() {
        return NodeSelectorParserToken.descendantOrSelf("descendant-or-self::", "descendant-or-self::");
    }

    final NodeSelectorParserToken descendantOrSelfSlashSlash() {
        return NodeSelectorParserToken.descendantOrSelf("//", "//");
    }

    final NodeSelectorParserToken divideSymbol() {
        return NodeSelectorParserToken.divideSymbol("div", "div");
    }

    final NodeSelectorParserToken division(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.division(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorEqualsParserToken equalsParserToken(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.equalsParserToken(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken equalsSymbol() {
        return NodeSelectorParserToken.equalsSymbol("=", "=");
    }

    final NodeSelectorExpressionParserToken expression(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.expression(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken firstChild() {
        return NodeSelectorParserToken.firstChild("first-child::", "first-child::");
    }

    final NodeSelectorParserToken following() {
        return NodeSelectorParserToken.following("following::", "following::");
    }

    final NodeSelectorParserToken followingSibling() {
        return NodeSelectorParserToken.followingSibling("following-sibling::", "following-sibling::");
    }

    final NodeSelectorFunctionParserToken function(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.function(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorFunctionParserToken functionWithoutArguments() {
        return function(functionName(), parenthesisOpen(), parenthesisClose());
    }

    final NodeSelectorFunctionParserToken functionWithArguments() {
        return function(functionName(), parenthesisOpen(), number(), comma(), quotedText(), parenthesisClose());
    }

    final NodeSelectorFunctionNameParserToken functionName() {
        return functionName("contains");
    }

    final NodeSelectorFunctionNameParserToken functionName(final String text) {
        return NodeSelectorParserToken.functionName(NodeSelectorFunctionName.with(text), text);
    }

    final NodeSelectorGreaterThanParserToken greaterThan(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.greaterThan(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken greaterThanSymbol() {
        return NodeSelectorParserToken.greaterThanSymbol(">", ">");
    }

    final NodeSelectorGreaterThanEqualsParserToken greaterThanEquals(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.greaterThanEquals(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken greaterThanEqualsSymbol() {
        return NodeSelectorParserToken.greaterThanEqualsSymbol(">=", ">=");
    }

    final NodeSelectorGroupParserToken group(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.group(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorLessThanParserToken lessThan(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.lessThan(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken lessThanSymbol() {
        return NodeSelectorParserToken.lessThanSymbol("<", "<");
    }

    final NodeSelectorLessThanEqualsParserToken lessThanEquals(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.lessThanEquals(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken lessThanEqualsSymbol() {
        return NodeSelectorParserToken.lessThanEqualsSymbol("<=", "<=");
    }

    final NodeSelectorParserToken lastChild() {
        return NodeSelectorParserToken.lastChild("last-child::", "last-child::");
    }

    final NodeSelectorParserToken minusSymbol() {
        return NodeSelectorParserToken.minusSymbol("-", "-");
    }

    final NodeSelectorParserToken modulo(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.modulo(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken moduloSymbol() {
        return NodeSelectorParserToken.moduloSymbol("mod", "mod");
    }

    final NodeSelectorMultiplicationParserToken multiplication(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.multiplication(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken multiplySymbol() {
        return NodeSelectorParserToken.multiplySymbol("*", "*");
    }

    final NodeSelectorNegativeParserToken negative(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.negative(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken nodeName() {
        return nodeName("Node1");
    }

    final NodeSelectorParserToken nodeName2() {
        return nodeName("Node22");
    }

    final NodeSelectorParserToken nodeName(final String name) {
        return NodeSelectorParserToken.nodeName(NodeSelectorNodeName.with(name), name);
    }

    final NodeSelectorNotEqualsParserToken notEquals(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.notEquals(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken notEqualsSymbol() {
        return NodeSelectorParserToken.notEqualsSymbol("!=", "!=");
    }

    final NodeSelectorParserToken number() {
        return number(1);
    }

    final NodeSelectorParserToken number2() {
        return number(23);
    }

    final NodeSelectorParserToken number(final double value) {
        return NodeSelectorParserToken.expressionNumber(EXPRESSION_NUMBER_KIND.create(value), String.valueOf(value));
    }

    final NodeSelectorOrParserToken or(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.or(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken orSymbol() {
        return NodeSelectorParserToken.orSymbol("or", "or");
    }

    final NodeSelectorParserToken parenthesisOpen() {
        return NodeSelectorParserToken.parenthesisOpenSymbol("(", "(");
    }

    final NodeSelectorParserToken parenthesisClose() {
        return NodeSelectorParserToken.parenthesisCloseSymbol(")", ")");
    }

    final NodeSelectorParserToken parent() {
        return NodeSelectorParserToken.parentOf("parent::", "parent::");
    }

    final NodeSelectorParserToken parentDotDot() {
        return NodeSelectorParserToken.parentOf("..", "..");
    }

    final NodeSelectorParserToken plusSymbol() {
        return NodeSelectorParserToken.plusSymbol("+", "+");
    }

    final NodeSelectorParserToken preceding() {
        return NodeSelectorParserToken.preceding("preceding::", "preceding::");
    }

    final NodeSelectorParserToken precedingSibling() {
        return NodeSelectorParserToken.precedingSibling("preceding-sibling::", "preceding-sibling::");
    }

    final NodeSelectorPredicateParserToken predicate(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.predicate(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken quotedText() {
        return quotedText("xyz");
    }

    final NodeSelectorParserToken quotedText2() {
        return quotedText("qrst");
    }

    final NodeSelectorParserToken quotedText(final String value) {
        return NodeSelectorParserToken.quotedText(value, CharSequences.quoteAndEscape(value).toString());
    }

    final NodeSelectorParserToken self() {
        return NodeSelectorParserToken.self("self::", "self::");
    }

    final NodeSelectorParserToken selfDot() {
        return NodeSelectorParserToken.self(".", ".");
    }

    final NodeSelectorParserToken slash() {
        return NodeSelectorParserToken.slashSeparatorSymbol("/", "/");
    }

    final NodeSelectorSubtractionParserToken subtraction(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.subtraction(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken whitespace() {
        return NodeSelectorParserToken.whitespace("  ", "  ");
    }

    final NodeSelectorParserToken wildcard() {
        return NodeSelectorParserToken.wildcard("*", "*");
    }

    private static String text(final NodeSelectorParserToken... tokens) {
        return ParserToken.text(Lists.of(tokens));
    }

    // IsMethodTesting.................................................................................................

    @Override
    public final T createIsMethodObject() {
        return this.createToken();
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return NodeSelector.class.getSimpleName();
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return ParserToken.class.getSimpleName();
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) ->
                m.equals("isLeaf") ||
                        m.equals("isNoise") ||
                        m.equals("isParent") ||
                        m.equals("isSymbol") ||
                        m.equals("isEmpty");
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
