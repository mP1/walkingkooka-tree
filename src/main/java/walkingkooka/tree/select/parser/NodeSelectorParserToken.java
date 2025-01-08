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

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.text.Whitespace;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.Objects;

/**
 * Represents a token within the xpath query grammar.
 */
public abstract class NodeSelectorParserToken implements ParserToken {

    /**
     * {@see NodeSelectorAbsoluteParserToken}
     */
    public static NodeSelectorAbsoluteParserToken absolute(final String value, final String text) {
        return NodeSelectorAbsoluteParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorAdditionParserToken}
     */
    public static NodeSelectorAdditionParserToken addition(final List<ParserToken> value, final String text) {
        return NodeSelectorAdditionParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorAncestorParserToken}
     */
    public static NodeSelectorAncestorParserToken ancestor(final String value, final String text) {
        return NodeSelectorAncestorParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorAncestorOrSelfParserToken}
     */
    public static NodeSelectorAncestorOrSelfParserToken ancestorOrSelf(final String value, final String text) {
        return NodeSelectorAncestorOrSelfParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorAndParserToken}
     */
    public static NodeSelectorAndParserToken and(final List<ParserToken> value, final String text) {
        return NodeSelectorAndParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorAndSymbolParserToken}
     */
    public static NodeSelectorAndSymbolParserToken andSymbol(final String value, final String text) {
        return NodeSelectorAndSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorAtSignSymbolParserToken}
     */
    public static NodeSelectorAtSignSymbolParserToken atSignSymbol(final String value, final String text) {
        return NodeSelectorAtSignSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorAttributeParserToken}
     */
    public static NodeSelectorAttributeParserToken attribute(final List<ParserToken> value, final String text) {
        return NodeSelectorAttributeParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorAttributeNameParserToken}
     */
    public static NodeSelectorAttributeNameParserToken attributeName(final NodeSelectorAttributeName value, final String text) {
        return NodeSelectorAttributeNameParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorBracketOpenSymbolParserToken}
     */
    public static NodeSelectorBracketOpenSymbolParserToken bracketOpenSymbol(final String value, final String text) {
        return NodeSelectorBracketOpenSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorBracketCloseSymbolParserToken}
     */
    public static NodeSelectorBracketCloseSymbolParserToken bracketCloseSymbol(final String value, final String text) {
        return NodeSelectorBracketCloseSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorChildParserToken}
     */
    public static NodeSelectorChildParserToken child(final String value, final String text) {
        return NodeSelectorChildParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorDescendantParserToken}
     */
    public static NodeSelectorDescendantParserToken descendant(final String value, final String text) {
        return NodeSelectorDescendantParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorDescendantOrSelfParserToken}
     */
    public static NodeSelectorDescendantOrSelfParserToken descendantOrSelf(final String value, final String text) {
        return NodeSelectorDescendantOrSelfParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorDivideSymbolParserToken}
     */
    public static NodeSelectorDivideSymbolParserToken divideSymbol(final String value, final String text) {
        return NodeSelectorDivideSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorDivisionParserToken}
     */
    public static NodeSelectorDivisionParserToken division(final List<ParserToken> value, final String text) {
        return NodeSelectorDivisionParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorEqualsParserToken}
     */
    public static NodeSelectorEqualsParserToken equalsParserToken(final List<ParserToken> value, final String text) {
        return NodeSelectorEqualsParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorEqualsSymbolParserToken}
     */
    public static NodeSelectorEqualsSymbolParserToken equalsSymbol(final String value, final String text) {
        return NodeSelectorEqualsSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorExpressionParserToken}
     */
    public static NodeSelectorExpressionParserToken expression(final List<ParserToken> value, final String text) {
        return NodeSelectorExpressionParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorExpressionNumberParserToken}
     */
    public static NodeSelectorExpressionNumberParserToken expressionNumber(final ExpressionNumber value, final String text) {
        return NodeSelectorExpressionNumberParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorFirstChildParserToken}
     */
    public static NodeSelectorFirstChildParserToken firstChild(final String value, final String text) {
        return NodeSelectorFirstChildParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorFollowingParserToken}
     */
    public static NodeSelectorFollowingParserToken following(final String value, final String text) {
        return NodeSelectorFollowingParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorFollowingSiblingParserToken}
     */
    public static NodeSelectorFollowingSiblingParserToken followingSibling(final String value, final String text) {
        return NodeSelectorFollowingSiblingParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorFunctionParserToken}
     */
    public static NodeSelectorFunctionParserToken function(final List<ParserToken> value, final String text) {
        return NodeSelectorFunctionParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorFunctionNameParserToken}
     */
    public static NodeSelectorFunctionNameParserToken functionName(final NodeSelectorFunctionName value, final String text) {
        return NodeSelectorFunctionNameParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorGreaterThanParserToken}
     */
    public static NodeSelectorGreaterThanParserToken greaterThan(final List<ParserToken> value, final String text) {
        return NodeSelectorGreaterThanParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorGreaterThanSymbolParserToken}
     */
    public static NodeSelectorGreaterThanSymbolParserToken greaterThanSymbol(final String value, final String text) {
        return NodeSelectorGreaterThanSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorGreaterThanEqualsParserToken}
     */
    public static NodeSelectorGreaterThanEqualsParserToken greaterThanEquals(final List<ParserToken> value, final String text) {
        return NodeSelectorGreaterThanEqualsParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorGreaterThanEqualsSymbolParserToken}
     */
    public static NodeSelectorGreaterThanEqualsSymbolParserToken greaterThanEqualsSymbol(final String value, final String text) {
        return NodeSelectorGreaterThanEqualsSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorGroupParserToken}
     */
    public static NodeSelectorGroupParserToken group(final List<ParserToken> value, final String text) {
        return NodeSelectorGroupParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorLastChildParserToken}
     */
    public static NodeSelectorLastChildParserToken lastChild(final String value, final String text) {
        return NodeSelectorLastChildParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorLessThanParserToken}
     */
    public static NodeSelectorLessThanParserToken lessThan(final List<ParserToken> value, final String text) {
        return NodeSelectorLessThanParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorLessThanSymbolParserToken}
     */
    public static NodeSelectorLessThanSymbolParserToken lessThanSymbol(final String value, final String text) {
        return NodeSelectorLessThanSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorLessThanEqualsParserToken}
     */
    public static NodeSelectorLessThanEqualsParserToken lessThanEquals(final List<ParserToken> value, final String text) {
        return NodeSelectorLessThanEqualsParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorLessThanEqualsSymbolParserToken}
     */
    public static NodeSelectorLessThanEqualsSymbolParserToken lessThanEqualsSymbol(final String value, final String text) {
        return NodeSelectorLessThanEqualsSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorMinusSymbolParserToken}
     */
    public static NodeSelectorMinusSymbolParserToken minusSymbol(final String value, final String text) {
        return NodeSelectorMinusSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorModuloParserToken}
     */
    public static NodeSelectorModuloParserToken modulo(final List<ParserToken> value, final String text) {
        return NodeSelectorModuloParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorModuloSymbolParserToken}
     */
    public static NodeSelectorModuloSymbolParserToken moduloSymbol(final String value, final String text) {
        return NodeSelectorModuloSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorMultiplicationParserToken}
     */
    public static NodeSelectorMultiplicationParserToken multiplication(final List<ParserToken> value, final String text) {
        return NodeSelectorMultiplicationParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorMultiplySymbolParserToken}
     */
    public static NodeSelectorMultiplySymbolParserToken multiplySymbol(final String value, final String text) {
        return NodeSelectorMultiplySymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorNegativeParserToken}
     */
    public static NodeSelectorNegativeParserToken negative(final List<ParserToken> value, final String text) {
        return NodeSelectorNegativeParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorNodeNameParserToken}
     */
    public static NodeSelectorNodeNameParserToken nodeName(final NodeSelectorNodeName value, final String text) {
        return NodeSelectorNodeNameParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorNotEqualsParserToken}
     */
    public static NodeSelectorNotEqualsParserToken notEquals(final List<ParserToken> value, final String text) {
        return NodeSelectorNotEqualsParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorNotEqualsSymbolParserToken}
     */
    public static NodeSelectorNotEqualsSymbolParserToken notEqualsSymbol(final String value, final String text) {
        return NodeSelectorNotEqualsSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorOrParserToken}
     */
    public static NodeSelectorOrParserToken or(final List<ParserToken> value, final String text) {
        return NodeSelectorOrParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorOrSymbolParserToken}
     */
    public static NodeSelectorOrSymbolParserToken orSymbol(final String value, final String text) {
        return NodeSelectorOrSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorParameterSeparatorSymbolParserToken}
     */
    public static NodeSelectorParameterSeparatorSymbolParserToken parameterSeparatorSymbol(final String value, final String text) {
        return NodeSelectorParameterSeparatorSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorParenthesisOpenSymbolParserToken}
     */
    public static NodeSelectorParenthesisOpenSymbolParserToken parenthesisOpenSymbol(final String value, final String text) {
        return NodeSelectorParenthesisOpenSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorParenthesisCloseSymbolParserToken}
     */
    public static NodeSelectorParenthesisCloseSymbolParserToken parenthesisCloseSymbol(final String value, final String text) {
        return NodeSelectorParenthesisCloseSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorParentOfParserToken}
     */
    public static NodeSelectorParentOfParserToken parentOf(final String value, final String text) {
        return NodeSelectorParentOfParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorPlusSymbolParserToken}
     */
    public static NodeSelectorPlusSymbolParserToken plusSymbol(final String value, final String text) {
        return NodeSelectorPlusSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorPrecedingParserToken}
     */
    public static NodeSelectorPrecedingParserToken preceding(final String value, final String text) {
        return NodeSelectorPrecedingParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorPrecedingSiblingParserToken}
     */
    public static NodeSelectorPrecedingSiblingParserToken precedingSibling(final String value, final String text) {
        return NodeSelectorPrecedingSiblingParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorPredicateParserToken}
     */
    public static NodeSelectorPredicateParserToken predicate(final List<ParserToken> value, final String text) {
        return NodeSelectorPredicateParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorQuotedTextParserToken}
     */
    public static NodeSelectorQuotedTextParserToken quotedText(final String value, final String text) {
        return NodeSelectorQuotedTextParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorSelfParserToken}
     */
    public static NodeSelectorSelfParserToken self(final String value, final String text) {
        return NodeSelectorSelfParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorSlashSeparatorSymbolParserToken}
     */
    public static NodeSelectorSlashSeparatorSymbolParserToken slashSeparatorSymbol(final String value, final String text) {
        return NodeSelectorSlashSeparatorSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorSubtractionParserToken}
     */
    public static NodeSelectorSubtractionParserToken subtraction(final List<ParserToken> value, final String text) {
        return NodeSelectorSubtractionParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorWhitespaceParserToken}
     */
    public static NodeSelectorWhitespaceParserToken whitespace(final String value, final String text) {
        return NodeSelectorWhitespaceParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorWildcardParserToken}
     */
    public static NodeSelectorWildcardParserToken wildcard(final String value, final String text) {
        return NodeSelectorWildcardParserToken.with(value, text);
    }

    static List<ParserToken> copyAndCheckTokens(final List<ParserToken> tokens) {
        return Lists.immutable(
            Objects.requireNonNull(tokens, "tokens")
        );
    }

    static String checkTextNullOrEmpty(final String text) {
        return CharSequences.failIfNullOrEmpty(text, "text");
    }

    static String checkTextNullOrWhitespace(final String text) {
        return Whitespace.failIfNullOrEmptyOrWhitespace(text, "text");
    }

    /**
     * Package private ctor to limit sub classing.
     */
    NodeSelectorParserToken(final String text) {
        this.text = text;
    }

    @Override
    public final String text() {
        return this.text;
    }

    private final String text;

    abstract Object value();

    @Override
    public final boolean isLeaf() {
        return this instanceof NodeSelectorLeafParserToken;
    }

    @Override
    public final boolean isParent() {
        return this instanceof NodeSelectorParentParserToken;
    }

    // isXXX............................................................................................................

    /**
     * Only {@link NodeSelectorAbsoluteParserToken} return true
     */
    public final boolean isAbsolute() {
        return this instanceof NodeSelectorAbsoluteParserToken;
    }

    /**
     * Only {@link NodeSelectorAdditionParserToken} return true
     */
    public final boolean isAddition() {
        return this instanceof NodeSelectorAdditionParserToken;
    }

    /**
     * Only {@link NodeSelectorAncestorParserToken} return true
     */
    public final boolean isAncestor() {
        return this instanceof NodeSelectorAncestorParserToken;
    }

    /**
     * Only {@link NodeSelectorAncestorOrSelfParserToken} return true
     */
    public final boolean isAncestorOrSelf() {
        return this instanceof NodeSelectorAncestorOrSelfParserToken;
    }

    /**
     * Only {@link NodeSelectorAndParserToken} return true
     */
    public final boolean isAnd() {
        return this instanceof NodeSelectorAndParserToken;
    }

    /**
     * Only {@link NodeSelectorAndSymbolParserToken} return true
     */
    public final boolean isAndSymbol() {
        return this instanceof NodeSelectorAndSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorAtSignSymbolParserToken} return true
     */
    public final boolean isAtSignSymbol() {
        return this instanceof NodeSelectorAtSignSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorAttributeParserToken} return true
     */
    public final boolean isAttribute() {
        return this instanceof NodeSelectorAttributeParserToken;
    }

    /**
     * Only {@link NodeSelectorAttributeNameParserToken} return true
     */
    public final boolean isAttributeName() {
        return this instanceof NodeSelectorAttributeNameParserToken;
    }

    /**
     * Only {@link NodeSelectorBracketOpenSymbolParserToken} return true
     */
    public final boolean isBracketOpenSymbol() {
        return this instanceof NodeSelectorBracketOpenSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorBracketCloseSymbolParserToken} return true
     */
    public final boolean isBracketCloseSymbol() {
        return this instanceof NodeSelectorBracketCloseSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorChildParserToken} return true
     */
    public final boolean isChild() {
        return this instanceof NodeSelectorChildParserToken;
    }

    /**
     * Only {@link NodeSelectorDescendantParserToken} return true
     */
    public final boolean isDescendant() {
        return this instanceof NodeSelectorDescendantParserToken;
    }

    /**
     * Only {@link NodeSelectorDescendantOrSelfParserToken} return true
     */
    public final boolean isDescendantOrSelf() {
        return this instanceof NodeSelectorDescendantOrSelfParserToken;
    }

    /**
     * Only {@link NodeSelectorDivideSymbolParserToken} return true
     */
    public final boolean isDivideSymbol() {
        return this instanceof NodeSelectorDivideSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorDivisionParserToken} return true
     */
    public final boolean isDivision() {
        return this instanceof NodeSelectorDivisionParserToken;
    }

    /**
     * Only {@link NodeSelectorEqualsParserToken} return true
     */
    public final boolean isEquals() {
        return this instanceof NodeSelectorEqualsParserToken;
    }

    /**
     * Only {@link NodeSelectorEqualsSymbolParserToken} return true
     */
    public final boolean isEqualsSymbol() {
        return this instanceof NodeSelectorEqualsSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorExpressionParserToken} return true
     */
    public final boolean isExpression() {
        return this instanceof NodeSelectorExpressionParserToken;
    }

    /**
     * Only {@link NodeSelectorExpressionNumberParserToken} return true
     */
    public final boolean isExpressionNumber() {
        return this instanceof NodeSelectorExpressionNumberParserToken;
    }

    /**
     * Only {@link NodeSelectorFirstChildParserToken} return true
     */
    public final boolean isFirstChild() {
        return this instanceof NodeSelectorFirstChildParserToken;
    }

    /**
     * Only {@link NodeSelectorFollowingParserToken} return true
     */
    public final boolean isFollowing() {
        return this instanceof NodeSelectorFollowingParserToken;
    }

    /**
     * Only {@link NodeSelectorFollowingSiblingParserToken} return true
     */
    public final boolean isFollowingSibling() {
        return this instanceof NodeSelectorFollowingSiblingParserToken;
    }

    /**
     * Only {@link NodeSelectorFunctionParserToken} return true
     */
    public final boolean isFunction() {
        return this instanceof NodeSelectorFunctionParserToken;
    }

    /**
     * Only {@link NodeSelectorFunctionNameParserToken} return true
     */
    public final boolean isFunctionName() {
        return this instanceof NodeSelectorFunctionNameParserToken;
    }

    /**
     * Only {@link NodeSelectorGreaterThanParserToken} return true
     */
    public final boolean isGreaterThan() {
        return this instanceof NodeSelectorGreaterThanParserToken;
    }

    /**
     * Only {@link NodeSelectorGreaterThanSymbolParserToken} return true
     */
    public final boolean isGreaterThanSymbol() {
        return this instanceof NodeSelectorGreaterThanSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorGreaterThanEqualsParserToken} return true
     */
    public final boolean isGreaterThanEquals() {
        return this instanceof NodeSelectorGreaterThanEqualsParserToken;
    }

    /**
     * Only {@link NodeSelectorGreaterThanEqualsSymbolParserToken} return true
     */
    public final boolean isGreaterThanEqualsSymbol() {
        return this instanceof NodeSelectorGreaterThanEqualsSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorGroupParserToken} return true
     */
    public final boolean isGroup() {
        return this instanceof NodeSelectorGroupParserToken;
    }

    /**
     * Only {@link NodeSelectorLastChildParserToken} return true
     */
    public final boolean isLastChild() {
        return this instanceof NodeSelectorLastChildParserToken;
    }

    /**
     * Only {@link NodeSelectorLessThanParserToken} return true
     */
    public final boolean isLessThan() {
        return this instanceof NodeSelectorLessThanParserToken;
    }

    /**
     * Only {@link NodeSelectorLessThanSymbolParserToken} return true
     */
    public final boolean isLessThanSymbol() {
        return this instanceof NodeSelectorLessThanSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorLessThanEqualsParserToken} return true
     */
    public final boolean isLessThanEquals() {
        return this instanceof NodeSelectorLessThanEqualsParserToken;
    }

    /**
     * Only {@link NodeSelectorLessThanEqualsSymbolParserToken} return true
     */
    public final boolean isLessThanEqualsSymbol() {
        return this instanceof NodeSelectorLessThanEqualsSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorMinusSymbolParserToken} return true
     */
    public final boolean isMinusSymbol() {
        return this instanceof NodeSelectorMinusSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorModuloParserToken} return true
     */
    public final boolean isModulo() {
        return this instanceof NodeSelectorModuloParserToken;
    }

    /**
     * Only {@link NodeSelectorModuloSymbolParserToken} return true
     */
    public final boolean isModuloSymbol() {
        return this instanceof NodeSelectorModuloSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorMultiplicationParserToken} return true
     */
    public final boolean isMultiplication() {
        return this instanceof NodeSelectorMultiplicationParserToken;
    }

    /**
     * Only {@link NodeSelectorMultiplySymbolParserToken} return true
     */
    public final boolean isMultiplySymbol() {
        return this instanceof NodeSelectorMultiplySymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorNegativeParserToken} return true
     */
    public final boolean isNegative() {
        return this instanceof NodeSelectorNegativeParserToken;
    }

    /**
     * Only {@link NodeSelectorNodeNameParserToken} return true
     */
    public final boolean isNodeName() {
        return this instanceof NodeSelectorNodeNameParserToken;
    }

    /**
     * Only {@link NodeSelectorNotEqualsParserToken} return true
     */
    public final boolean isNotEquals() {
        return this instanceof NodeSelectorNotEqualsParserToken;
    }

    /**
     * Only {@link NodeSelectorNotEqualsSymbolParserToken} return true
     */
    public final boolean isNotEqualsSymbol() {
        return this instanceof NodeSelectorNotEqualsSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorOrParserToken} return true
     */
    public final boolean isOr() {
        return this instanceof NodeSelectorOrParserToken;
    }

    /**
     * Only {@link NodeSelectorOrSymbolParserToken} return true
     */
    public final boolean isOrSymbol() {
        return this instanceof NodeSelectorOrSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorParameterSeparatorSymbolParserToken} return true
     */
    public final boolean isParameterSeparatorSymbol() {
        return this instanceof NodeSelectorParameterSeparatorSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorParenthesisOpenSymbolParserToken} return true
     */
    public final boolean isParenthesisOpenSymbol() {
        return this instanceof NodeSelectorParenthesisOpenSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorParenthesisCloseSymbolParserToken} return true
     */
    public final boolean isParenthesisCloseSymbol() {
        return this instanceof NodeSelectorParenthesisCloseSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorParentOfParserToken} return true
     */
    public final boolean isParentOf() {
        return this instanceof NodeSelectorParentOfParserToken;
    }

    /**
     * Only {@link NodeSelectorPlusSymbolParserToken} return true
     */
    public final boolean isPlusSymbol() {
        return this instanceof NodeSelectorPlusSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorPrecedingParserToken} return true
     */
    public final boolean isPreceding() {
        return this instanceof NodeSelectorPrecedingParserToken;
    }

    /**
     * Only {@link NodeSelectorPrecedingSiblingParserToken} return true
     */
    public final boolean isPrecedingSibling() {
        return this instanceof NodeSelectorPrecedingSiblingParserToken;
    }

    /**
     * Only {@link NodeSelectorPredicateParserToken} return true
     */
    public final boolean isPredicate() {
        return this instanceof NodeSelectorPredicateParserToken;
    }

    /**
     * Only {@link NodeSelectorQuotedTextParserToken} return true
     */
    public final boolean isQuotedText() {
        return this instanceof NodeSelectorQuotedTextParserToken;
    }

    /**
     * Only {@link NodeSelectorSelfParserToken} return true
     */
    public final boolean isSelf() {
        return this instanceof NodeSelectorSelfParserToken;
    }

    /**
     * Only {@link NodeSelectorSlashSeparatorSymbolParserToken} return true
     */
    public final boolean isSlashSeparatorSymbol() {
        return this instanceof NodeSelectorSlashSeparatorSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorSubtractionParserToken} return true
     */
    public final boolean isSubtraction() {
        return this instanceof NodeSelectorSubtractionParserToken;
    }

    /**
     * Only {@link NodeSelectorSymbolParserToken} return true
     */
    public final boolean isSymbol() {
        return this instanceof NodeSelectorSymbolParserToken;
    }

    /**
     * Only {@link NodeSelectorWhitespaceParserToken} return true
     */
    public final boolean isWhitespace() {
        return this instanceof NodeSelectorWhitespaceParserToken;
    }

    /**
     * Only {@link NodeSelectorWildcardParserToken} return true
     */
    public final boolean isWildcard() {
        return this instanceof NodeSelectorWildcardParserToken;
    }

    /**
     * The priority of this token, tokens with a value of zero are left in their original position.
     */
    abstract int operatorPriority();

    final static int IGNORED = 0;

    final static int LOWEST_PRIORITY = IGNORED + 1;

    final static int OR_PRIORITY = LOWEST_PRIORITY + 1;
    final static int AND_PRIORITY = OR_PRIORITY + 1;

    final static int EQUALS_NOT_EQUALS_PRIORITY = AND_PRIORITY + 1;
    final static int LESS_GREATER_PRIORITY = EQUALS_NOT_EQUALS_PRIORITY + 1;

    final static int ADDITION_SUBTRACTION_PRIORITY = LESS_GREATER_PRIORITY + 1;
    final static int MULTIPLY_DIVISION_PRIORITY = ADDITION_SUBTRACTION_PRIORITY + 1;
    final static int MOD_PRIORITY = MULTIPLY_DIVISION_PRIORITY + 1;
    final static int HIGHEST_PRIORITY = MOD_PRIORITY;

    /**
     * Factory that creates the {@link NodeSelectorBinaryParserToken} sub class using the provided tokens and text.
     */
    abstract NodeSelectorBinaryParserToken<?> binaryOperand(final List<ParserToken> tokens, final String text);

    // Visitor ......................................................................................................

    /**
     * Begins the visiting process.
     */
    @Override
    public final void accept(final ParserTokenVisitor visitor) {
        if (visitor instanceof NodeSelectorParserTokenVisitor) {
            final NodeSelectorParserTokenVisitor visitor2 = Cast.to(visitor);

            if (Visiting.CONTINUE == visitor2.startVisit(this)) {
                this.accept(visitor2);
            }
            visitor2.endVisit(this);
        }
    }

    abstract void accept(final NodeSelectorParserTokenVisitor visitor);

    // Object ...........................................................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(this.text, this.value());
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public final boolean equals(final Object other) {
        return this == other ||
            this.canBeEqual(other) &&
                this.equals0((NodeSelectorParserToken) other);
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final NodeSelectorParserToken other) {
        return this.text.equals(other.text) &&
            this.value().equals(other.value());
    }

    @Override
    public final String toString() {
        return this.text();
    }
}
