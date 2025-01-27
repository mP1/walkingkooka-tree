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
     * {@see AbsoluteNodeSelectorParserToken}
     */
    public static AbsoluteNodeSelectorParserToken absolute(final String value, final String text) {
        return AbsoluteNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see AdditionNodeSelectorParserToken}
     */
    public static AdditionNodeSelectorParserToken addition(final List<ParserToken> value, final String text) {
        return AdditionNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see AncestorNodeSelectorParserToken}
     */
    public static AncestorNodeSelectorParserToken ancestor(final String value, final String text) {
        return AncestorNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see AncestorOrSelfNodeSelectorParserToken}
     */
    public static AncestorOrSelfNodeSelectorParserToken ancestorOrSelf(final String value, final String text) {
        return AncestorOrSelfNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see AndNodeSelectorParserToken}
     */
    public static AndNodeSelectorParserToken and(final List<ParserToken> value, final String text) {
        return AndNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see AndSymbolNodeSelectorParserToken}
     */
    public static AndSymbolNodeSelectorParserToken andSymbol(final String value, final String text) {
        return AndSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see AtSignSymbolNodeSelectorParserToken}
     */
    public static AtSignSymbolNodeSelectorParserToken atSignSymbol(final String value, final String text) {
        return AtSignSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see AttributeNodeSelectorParserToken}
     */
    public static AttributeNodeSelectorParserToken attribute(final List<ParserToken> value, final String text) {
        return AttributeNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see AttributeNameNodeSelectorParserToken}
     */
    public static AttributeNameNodeSelectorParserToken attributeName(final NodeSelectorAttributeName value, final String text) {
        return AttributeNameNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see BracketOpenSymbolNodeSelectorParserToken}
     */
    public static BracketOpenSymbolNodeSelectorParserToken bracketOpenSymbol(final String value, final String text) {
        return BracketOpenSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see BracketCloseSymbolNodeSelectorParserToken}
     */
    public static BracketCloseSymbolNodeSelectorParserToken bracketCloseSymbol(final String value, final String text) {
        return BracketCloseSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see ChildNodeSelectorParserToken}
     */
    public static ChildNodeSelectorParserToken child(final String value, final String text) {
        return ChildNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see DescendantNodeSelectorParserToken}
     */
    public static DescendantNodeSelectorParserToken descendant(final String value, final String text) {
        return DescendantNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see DescendantOrSelfNodeSelectorParserToken}
     */
    public static DescendantOrSelfNodeSelectorParserToken descendantOrSelf(final String value, final String text) {
        return DescendantOrSelfNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see DivideSymbolNodeSelectorParserToken}
     */
    public static DivideSymbolNodeSelectorParserToken divideSymbol(final String value, final String text) {
        return DivideSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see DivisionNodeSelectorParserToken}
     */
    public static DivisionNodeSelectorParserToken division(final List<ParserToken> value, final String text) {
        return DivisionNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see EqualsNodeSelectorParserToken}
     */
    public static EqualsNodeSelectorParserToken equalsNodeSelectorParserToken(final List<ParserToken> value, final String text) {
        return EqualsNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see EqualsSymbolNodeSelectorParserToken}
     */
    public static EqualsSymbolNodeSelectorParserToken equalsSymbol(final String value, final String text) {
        return EqualsSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see ExpressionNodeSelectorParserToken}
     */
    public static ExpressionNodeSelectorParserToken expression(final List<ParserToken> value, final String text) {
        return ExpressionNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see ExpressionNumberNodeSelectorParserToken}
     */
    public static ExpressionNumberNodeSelectorParserToken expressionNumber(final ExpressionNumber value, final String text) {
        return ExpressionNumberNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see FirstChildNodeSelectorParserToken}
     */
    public static FirstChildNodeSelectorParserToken firstChild(final String value, final String text) {
        return FirstChildNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see FollowingNodeSelectorParserToken}
     */
    public static FollowingNodeSelectorParserToken following(final String value, final String text) {
        return FollowingNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see FollowingSiblingNodeSelectorParserToken}
     */
    public static FollowingSiblingNodeSelectorParserToken followingSibling(final String value, final String text) {
        return FollowingSiblingNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see FunctionNodeSelectorParserToken}
     */
    public static FunctionNodeSelectorParserToken function(final List<ParserToken> value, final String text) {
        return FunctionNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see FunctionNameNodeSelectorParserToken}
     */
    public static FunctionNameNodeSelectorParserToken functionName(final NodeSelectorFunctionName value, final String text) {
        return FunctionNameNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see GreaterThanNodeSelectorParserToken}
     */
    public static GreaterThanNodeSelectorParserToken greaterThan(final List<ParserToken> value, final String text) {
        return GreaterThanNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see GreaterThanSymbolNodeSelectorParserToken}
     */
    public static GreaterThanSymbolNodeSelectorParserToken greaterThanSymbol(final String value, final String text) {
        return GreaterThanSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see GreaterThanEqualsNodeSelectorParserToken}
     */
    public static GreaterThanEqualsNodeSelectorParserToken greaterThanEquals(final List<ParserToken> value, final String text) {
        return GreaterThanEqualsNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see GreaterThanEqualsSymbolNodeSelectorParserToken}
     */
    public static GreaterThanEqualsSymbolNodeSelectorParserToken greaterThanEqualsSymbol(final String value, final String text) {
        return GreaterThanEqualsSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see GroupNodeSelectorParserToken}
     */
    public static GroupNodeSelectorParserToken group(final List<ParserToken> value, final String text) {
        return GroupNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see LastChildNodeSelectorParserToken}
     */
    public static LastChildNodeSelectorParserToken lastChild(final String value, final String text) {
        return LastChildNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see LessThanNodeSelectorParserToken}
     */
    public static LessThanNodeSelectorParserToken lessThan(final List<ParserToken> value, final String text) {
        return LessThanNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see LessThanSymbolNodeSelectorParserToken}
     */
    public static LessThanSymbolNodeSelectorParserToken lessThanSymbol(final String value, final String text) {
        return LessThanSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see LessThanEqualsNodeSelectorParserToken}
     */
    public static LessThanEqualsNodeSelectorParserToken lessThanEquals(final List<ParserToken> value, final String text) {
        return LessThanEqualsNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see LessThanEqualsSymbolNodeSelectorParserToken}
     */
    public static LessThanEqualsSymbolNodeSelectorParserToken lessThanEqualsSymbol(final String value, final String text) {
        return LessThanEqualsSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see MinusSymbolNodeSelectorParserToken}
     */
    public static MinusSymbolNodeSelectorParserToken minusSymbol(final String value, final String text) {
        return MinusSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see ModuloNodeSelectorParserToken}
     */
    public static ModuloNodeSelectorParserToken modulo(final List<ParserToken> value, final String text) {
        return ModuloNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see ModuloSymbolNodeSelectorParserToken}
     */
    public static ModuloSymbolNodeSelectorParserToken moduloSymbol(final String value, final String text) {
        return ModuloSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see MultiplicationNodeSelectorParserToken}
     */
    public static MultiplicationNodeSelectorParserToken multiplication(final List<ParserToken> value, final String text) {
        return MultiplicationNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see MultiplySymbolNodeSelectorParserToken}
     */
    public static MultiplySymbolNodeSelectorParserToken multiplySymbol(final String value, final String text) {
        return MultiplySymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see NegativeNodeSelectorParserToken}
     */
    public static NegativeNodeSelectorParserToken negative(final List<ParserToken> value, final String text) {
        return NegativeNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see NodeNameNodeSelectorParserToken}
     */
    public static NodeNameNodeSelectorParserToken nodeName(final NodeSelectorNodeName value, final String text) {
        return NodeNameNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see NotEqualsNodeSelectorParserToken}
     */
    public static NotEqualsNodeSelectorParserToken notEquals(final List<ParserToken> value, final String text) {
        return NotEqualsNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see NotEqualsSymbolNodeSelectorParserToken}
     */
    public static NotEqualsSymbolNodeSelectorParserToken notEqualsSymbol(final String value, final String text) {
        return NotEqualsSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see OrNodeSelectorParserToken}
     */
    public static OrNodeSelectorParserToken or(final List<ParserToken> value, final String text) {
        return OrNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see OrSymbolNodeSelectorParserToken}
     */
    public static OrSymbolNodeSelectorParserToken orSymbol(final String value, final String text) {
        return OrSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see ParameterSeparatorSymbolNodeSelectorParserToken}
     */
    public static ParameterSeparatorSymbolNodeSelectorParserToken parameterSeparatorSymbol(final String value, final String text) {
        return ParameterSeparatorSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see ParenthesisOpenSymbolNodeSelectorParserToken}
     */
    public static ParenthesisOpenSymbolNodeSelectorParserToken parenthesisOpenSymbol(final String value, final String text) {
        return ParenthesisOpenSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see ParenthesisCloseSymbolNodeSelectorParserToken}
     */
    public static ParenthesisCloseSymbolNodeSelectorParserToken parenthesisCloseSymbol(final String value, final String text) {
        return ParenthesisCloseSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see ParentOfNodeSelectorParserToken}
     */
    public static ParentOfNodeSelectorParserToken parentOf(final String value, final String text) {
        return ParentOfNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see PlusSymbolNodeSelectorParserToken}
     */
    public static PlusSymbolNodeSelectorParserToken plusSymbol(final String value, final String text) {
        return PlusSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see PrecedingNodeSelectorParserToken}
     */
    public static PrecedingNodeSelectorParserToken preceding(final String value, final String text) {
        return PrecedingNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see PrecedingSiblingNodeSelectorParserToken}
     */
    public static PrecedingSiblingNodeSelectorParserToken precedingSibling(final String value, final String text) {
        return PrecedingSiblingNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see PredicateNodeSelectorParserToken}
     */
    public static PredicateNodeSelectorParserToken predicate(final List<ParserToken> value, final String text) {
        return PredicateNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see QuotedTextNodeSelectorParserToken}
     */
    public static QuotedTextNodeSelectorParserToken quotedText(final String value, final String text) {
        return QuotedTextNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see SelfNodeSelectorParserToken}
     */
    public static SelfNodeSelectorParserToken self(final String value, final String text) {
        return SelfNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see SlashSeparatorSymbolNodeSelectorParserToken}
     */
    public static SlashSeparatorSymbolNodeSelectorParserToken slashSeparatorSymbol(final String value, final String text) {
        return SlashSeparatorSymbolNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see SubtractionNodeSelectorParserToken}
     */
    public static SubtractionNodeSelectorParserToken subtraction(final List<ParserToken> value, final String text) {
        return SubtractionNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see WhitespaceNodeSelectorParserToken}
     */
    public static WhitespaceNodeSelectorParserToken whitespace(final String value, final String text) {
        return WhitespaceNodeSelectorParserToken.with(value, text);
    }

    /**
     * {@see WildcardNodeSelectorParserToken}
     */
    public static WildcardNodeSelectorParserToken wildcard(final String value, final String text) {
        return WildcardNodeSelectorParserToken.with(value, text);
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
        return this instanceof LeafNodeSelectorParserToken;
    }

    @Override
    public final boolean isParent() {
        return this instanceof ParentNodeSelectorParserToken;
    }

    // isXXX............................................................................................................

    /**
     * Only {@link AbsoluteNodeSelectorParserToken} return true
     */
    public final boolean isAbsolute() {
        return this instanceof AbsoluteNodeSelectorParserToken;
    }

    /**
     * Only {@link AdditionNodeSelectorParserToken} return true
     */
    public final boolean isAddition() {
        return this instanceof AdditionNodeSelectorParserToken;
    }

    /**
     * Only {@link AncestorNodeSelectorParserToken} return true
     */
    public final boolean isAncestor() {
        return this instanceof AncestorNodeSelectorParserToken;
    }

    /**
     * Only {@link AncestorOrSelfNodeSelectorParserToken} return true
     */
    public final boolean isAncestorOrSelf() {
        return this instanceof AncestorOrSelfNodeSelectorParserToken;
    }

    /**
     * Only {@link AndNodeSelectorParserToken} return true
     */
    public final boolean isAnd() {
        return this instanceof AndNodeSelectorParserToken;
    }

    /**
     * Only {@link AndSymbolNodeSelectorParserToken} return true
     */
    public final boolean isAndSymbol() {
        return this instanceof AndSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link AtSignSymbolNodeSelectorParserToken} return true
     */
    public final boolean isAtSignSymbol() {
        return this instanceof AtSignSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link AttributeNodeSelectorParserToken} return true
     */
    public final boolean isAttribute() {
        return this instanceof AttributeNodeSelectorParserToken;
    }

    /**
     * Only {@link AttributeNameNodeSelectorParserToken} return true
     */
    public final boolean isAttributeName() {
        return this instanceof AttributeNameNodeSelectorParserToken;
    }

    /**
     * Only {@link BracketOpenSymbolNodeSelectorParserToken} return true
     */
    public final boolean isBracketOpenSymbol() {
        return this instanceof BracketOpenSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link BracketCloseSymbolNodeSelectorParserToken} return true
     */
    public final boolean isBracketCloseSymbol() {
        return this instanceof BracketCloseSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link ChildNodeSelectorParserToken} return true
     */
    public final boolean isChild() {
        return this instanceof ChildNodeSelectorParserToken;
    }

    /**
     * Only {@link DescendantNodeSelectorParserToken} return true
     */
    public final boolean isDescendant() {
        return this instanceof DescendantNodeSelectorParserToken;
    }

    /**
     * Only {@link DescendantOrSelfNodeSelectorParserToken} return true
     */
    public final boolean isDescendantOrSelf() {
        return this instanceof DescendantOrSelfNodeSelectorParserToken;
    }

    /**
     * Only {@link DivideSymbolNodeSelectorParserToken} return true
     */
    public final boolean isDivideSymbol() {
        return this instanceof DivideSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link DivisionNodeSelectorParserToken} return true
     */
    public final boolean isDivision() {
        return this instanceof DivisionNodeSelectorParserToken;
    }

    /**
     * Only {@link EqualsNodeSelectorParserToken} return true
     */
    public final boolean isEquals() {
        return this instanceof EqualsNodeSelectorParserToken;
    }

    /**
     * Only {@link EqualsSymbolNodeSelectorParserToken} return true
     */
    public final boolean isEqualsSymbol() {
        return this instanceof EqualsSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link ExpressionNodeSelectorParserToken} return true
     */
    public final boolean isExpression() {
        return this instanceof ExpressionNodeSelectorParserToken;
    }

    /**
     * Only {@link ExpressionNumberNodeSelectorParserToken} return true
     */
    public final boolean isExpressionNumber() {
        return this instanceof ExpressionNumberNodeSelectorParserToken;
    }

    /**
     * Only {@link FirstChildNodeSelectorParserToken} return true
     */
    public final boolean isFirstChild() {
        return this instanceof FirstChildNodeSelectorParserToken;
    }

    /**
     * Only {@link FollowingNodeSelectorParserToken} return true
     */
    public final boolean isFollowing() {
        return this instanceof FollowingNodeSelectorParserToken;
    }

    /**
     * Only {@link FollowingSiblingNodeSelectorParserToken} return true
     */
    public final boolean isFollowingSibling() {
        return this instanceof FollowingSiblingNodeSelectorParserToken;
    }

    /**
     * Only {@link FunctionNodeSelectorParserToken} return true
     */
    public final boolean isFunction() {
        return this instanceof FunctionNodeSelectorParserToken;
    }

    /**
     * Only {@link FunctionNameNodeSelectorParserToken} return true
     */
    public final boolean isFunctionName() {
        return this instanceof FunctionNameNodeSelectorParserToken;
    }

    /**
     * Only {@link GreaterThanNodeSelectorParserToken} return true
     */
    public final boolean isGreaterThan() {
        return this instanceof GreaterThanNodeSelectorParserToken;
    }

    /**
     * Only {@link GreaterThanSymbolNodeSelectorParserToken} return true
     */
    public final boolean isGreaterThanSymbol() {
        return this instanceof GreaterThanSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link GreaterThanEqualsNodeSelectorParserToken} return true
     */
    public final boolean isGreaterThanEquals() {
        return this instanceof GreaterThanEqualsNodeSelectorParserToken;
    }

    /**
     * Only {@link GreaterThanEqualsSymbolNodeSelectorParserToken} return true
     */
    public final boolean isGreaterThanEqualsSymbol() {
        return this instanceof GreaterThanEqualsSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link GroupNodeSelectorParserToken} return true
     */
    public final boolean isGroup() {
        return this instanceof GroupNodeSelectorParserToken;
    }

    /**
     * Only {@link LastChildNodeSelectorParserToken} return true
     */
    public final boolean isLastChild() {
        return this instanceof LastChildNodeSelectorParserToken;
    }

    /**
     * Only {@link LessThanNodeSelectorParserToken} return true
     */
    public final boolean isLessThan() {
        return this instanceof LessThanNodeSelectorParserToken;
    }

    /**
     * Only {@link LessThanSymbolNodeSelectorParserToken} return true
     */
    public final boolean isLessThanSymbol() {
        return this instanceof LessThanSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link LessThanEqualsNodeSelectorParserToken} return true
     */
    public final boolean isLessThanEquals() {
        return this instanceof LessThanEqualsNodeSelectorParserToken;
    }

    /**
     * Only {@link LessThanEqualsSymbolNodeSelectorParserToken} return true
     */
    public final boolean isLessThanEqualsSymbol() {
        return this instanceof LessThanEqualsSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link MinusSymbolNodeSelectorParserToken} return true
     */
    public final boolean isMinusSymbol() {
        return this instanceof MinusSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link ModuloNodeSelectorParserToken} return true
     */
    public final boolean isModulo() {
        return this instanceof ModuloNodeSelectorParserToken;
    }

    /**
     * Only {@link ModuloSymbolNodeSelectorParserToken} return true
     */
    public final boolean isModuloSymbol() {
        return this instanceof ModuloSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link MultiplicationNodeSelectorParserToken} return true
     */
    public final boolean isMultiplication() {
        return this instanceof MultiplicationNodeSelectorParserToken;
    }

    /**
     * Only {@link MultiplySymbolNodeSelectorParserToken} return true
     */
    public final boolean isMultiplySymbol() {
        return this instanceof MultiplySymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link NegativeNodeSelectorParserToken} return true
     */
    public final boolean isNegative() {
        return this instanceof NegativeNodeSelectorParserToken;
    }

    /**
     * Only {@link NodeNameNodeSelectorParserToken} return true
     */
    public final boolean isNodeName() {
        return this instanceof NodeNameNodeSelectorParserToken;
    }

    /**
     * Only {@link NotEqualsNodeSelectorParserToken} return true
     */
    public final boolean isNotEquals() {
        return this instanceof NotEqualsNodeSelectorParserToken;
    }

    /**
     * Only {@link NotEqualsSymbolNodeSelectorParserToken} return true
     */
    public final boolean isNotEqualsSymbol() {
        return this instanceof NotEqualsSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link OrNodeSelectorParserToken} return true
     */
    public final boolean isOr() {
        return this instanceof OrNodeSelectorParserToken;
    }

    /**
     * Only {@link OrSymbolNodeSelectorParserToken} return true
     */
    public final boolean isOrSymbol() {
        return this instanceof OrSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link ParameterSeparatorSymbolNodeSelectorParserToken} return true
     */
    public final boolean isParameterSeparatorSymbol() {
        return this instanceof ParameterSeparatorSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link ParenthesisOpenSymbolNodeSelectorParserToken} return true
     */
    public final boolean isParenthesisOpenSymbol() {
        return this instanceof ParenthesisOpenSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link ParenthesisCloseSymbolNodeSelectorParserToken} return true
     */
    public final boolean isParenthesisCloseSymbol() {
        return this instanceof ParenthesisCloseSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link ParentOfNodeSelectorParserToken} return true
     */
    public final boolean isParentOf() {
        return this instanceof ParentOfNodeSelectorParserToken;
    }

    /**
     * Only {@link PlusSymbolNodeSelectorParserToken} return true
     */
    public final boolean isPlusSymbol() {
        return this instanceof PlusSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link PrecedingNodeSelectorParserToken} return true
     */
    public final boolean isPreceding() {
        return this instanceof PrecedingNodeSelectorParserToken;
    }

    /**
     * Only {@link PrecedingSiblingNodeSelectorParserToken} return true
     */
    public final boolean isPrecedingSibling() {
        return this instanceof PrecedingSiblingNodeSelectorParserToken;
    }

    /**
     * Only {@link PredicateNodeSelectorParserToken} return true
     */
    public final boolean isPredicate() {
        return this instanceof PredicateNodeSelectorParserToken;
    }

    /**
     * Only {@link QuotedTextNodeSelectorParserToken} return true
     */
    public final boolean isQuotedText() {
        return this instanceof QuotedTextNodeSelectorParserToken;
    }

    /**
     * Only {@link SelfNodeSelectorParserToken} return true
     */
    public final boolean isSelf() {
        return this instanceof SelfNodeSelectorParserToken;
    }

    /**
     * Only {@link SlashSeparatorSymbolNodeSelectorParserToken} return true
     */
    public final boolean isSlashSeparatorSymbol() {
        return this instanceof SlashSeparatorSymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link SubtractionNodeSelectorParserToken} return true
     */
    public final boolean isSubtraction() {
        return this instanceof SubtractionNodeSelectorParserToken;
    }

    /**
     * Only {@link SymbolNodeSelectorParserToken} return true
     */
    public final boolean isSymbol() {
        return this instanceof SymbolNodeSelectorParserToken;
    }

    /**
     * Only {@link WhitespaceNodeSelectorParserToken} return true
     */
    public final boolean isWhitespace() {
        return this instanceof WhitespaceNodeSelectorParserToken;
    }

    /**
     * Only {@link WildcardNodeSelectorParserToken} return true
     */
    public final boolean isWildcard() {
        return this instanceof WildcardNodeSelectorParserToken;
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
     * Factory that creates the {@link BinaryNodeSelectorParserToken} sub class using the provided tokens and text.
     */
    abstract BinaryNodeSelectorParserToken<?> binaryOperand(final List<ParserToken> tokens, final String text);

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
    public final boolean equals(final Object other) {
        return this == other ||
            null != other && this.getClass() == other.getClass() && this.equals0((NodeSelectorParserToken) other);
    }

    private boolean equals0(final NodeSelectorParserToken other) {
        return this.text.equals(other.text) &&
            this.value().equals(other.value());
    }

    @Override
    public final String toString() {
        return this.text();
    }
}
