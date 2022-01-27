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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.ParserTesting2;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.visit.Visiting;

import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class NodeSelectorParsersTest implements ParserTesting2<Parser<NodeSelectorParserContext>,
        NodeSelectorParserContext> {

    private ExpressionNumberKind expressionNumberKind;

    /**
     * The name of the test (test method) is important and is used to select a {@link ExpressionNumberKind}.
     */
    @BeforeEach
    public void setupExpressionNumberKind(final TestInfo testInfo) {
        final String testName = testInfo.getDisplayName();
        this.expressionNumberKind = testName.endsWith("BigDecimal()") ?
                ExpressionNumberKind.BIG_DECIMAL :
                testName.endsWith("Double()") ?
                        ExpressionNumberKind.DOUBLE :
                        ExpressionNumberKind.DEFAULT;
    }

    // descendant ...........................................................................................

    @Test
    public void testDescendantSlashSlashFails() {
        this.parseThrows2(descendantOrSelfSlashSlash());
    }

    @Test
    public void testNodeNameDescendantSlashSlashFails() {
        this.parseThrows2(nodeName(), descendantOrSelfSlashSlash());
    }

    @Test
    public void testNodeNameDescendantSlashSlashSlashFails() {
        this.parseThrows2(nodeName(), descendantOrSelfSlashSlash(), slash());
    }

    @Test
    public void testNodeNameDescendantSlashSlashNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashWildcard() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), wildcard());
    }

    @Test
    public void testNodeNameDescendantSlashSlashSelfDot() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), selfDot());
    }

    @Test
    public void testNodeNameDescendantSlashSlashParentDotDot() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), parentDotDot());
    }

    @Test
    public void testNodeNameDescendantSlashSlashAncestorNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), ancestor(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashChildNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), child(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashDescendantNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), descendant(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashFirstChildNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), firstChild(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashFollowingNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), following(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashFollowingSiblingNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), followingSibling(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashLastChildNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), lastChild(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashParentOfNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), parent(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashPrecedingNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), preceding(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashPrecedingSiblingNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), precedingSibling(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashSelfNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), self(), nodeName2());
    }

    // parentDot ...............................................................................................

    @Test
    public void testParentDotDot() {
        this.parseAndCheck2(parentDotDot());
    }

    // selfDot ...............................................................................................

    @Test
    public void testSelfDot() {
        this.parseAndCheck2(selfDot());
    }

    // selfDot ...............................................................................................

    @Test
    public void testWildcard() {
        this.parseAndCheck2(wildcard());
    }

    // absolute node................................................................................................

    @Test
    public void testAbsoluteNodeName() {
        this.parseAndCheck2(absolute(), nodeName());
    }

    // absolute axis node .....................................................................................

    @Test
    public void testAbsoluteAncestorNodeNameMissingFails() {
        this.parseThrows2(absolute(), ancestor());
    }

    @Test
    public void testAbsoluteAncestorNodeName() {
        this.parseAndCheck2(absolute(), ancestor(), nodeName());
    }

    @Test
    public void testAbsoluteChildNodeName() {
        this.parseAndCheck2(absolute(), child(), nodeName());
    }

    @Test
    public void testAbsoluteDescendantNodeName() {
        this.parseAndCheck2(absolute(), descendant(), nodeName());
    }

    @Test
    public void testAbsoluteFirstChildNodeName() {
        this.parseAndCheck2(absolute(), firstChild(), nodeName());
    }

    @Test
    public void testAbsoluteFollowingNodeName() {
        this.parseAndCheck2(absolute(), following(), nodeName());
    }

    @Test
    public void testAbsoluteFollowingSiblingNodeName() {
        this.parseAndCheck2(absolute(), followingSibling(), nodeName());
    }

    @Test
    public void testAbsoluteLastChildNodeName() {
        this.parseAndCheck2(absolute(), lastChild(), nodeName());
    }

    @Test
    public void testAbsolutePrecedingNodeName() {
        this.parseAndCheck2(absolute(), preceding(), nodeName());
    }

    @Test
    public void testAbsolutePrecedingSiblingNodeName() {
        this.parseAndCheck2(absolute(), precedingSibling(), nodeName());
    }

    @Test
    public void testAbsoluteParentNodeName() {
        this.parseAndCheck2(absolute(), parent(), nodeName());
    }

    @Test
    public void testAbsoluteSelfNodeName() {
        this.parseAndCheck2(absolute(), self(), nodeName());
    }

    // absolute nodeName predicate child index ..........................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenIndexBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenIndexBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenIndexBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenIndexBracketClose();
    }

    private void absoluteNodeNameBracketOpenIndexBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(expressionNumber()),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceIndexWhitespaceBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenWhitespaceIndexWhitespaceBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceIndexWhitespaceBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenWhitespaceIndexWhitespaceBracketClose();
    }

    private void absoluteNodeNameBracketOpenWhitespaceIndexWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(whitespace(), expressionNumber(), whitespace()),
                bracketClose());
    }

    // absolute nodeName predicate expressionNumber.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberBracketClose();
    }


    private void absoluteNodeNameBracketOpenNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        expressionNumber()),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenDecimalNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenDecimalNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenDecimalNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenDecimalNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenDecimalNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(expressionNumber(12.5)
                ),
                bracketClose());
    }


    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenWhitespaceNumberWhitespaceBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenWhitespaceNumberWhitespaceBracketClose();
    }

    private void absoluteNodeNameBracketOpenWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        whitespace(), expressionNumber(), whitespace()),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNegativeNumberBracketCloseBigDecimal() {
        this.testAbsoluteNodeNameBracketOpenNegativeNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNegativeNumberBracketCloseDouble() {
        this.testAbsoluteNodeNameBracketOpenNegativeNumberBracketClose();
    }

    private void testAbsoluteNodeNameBracketOpenNegativeNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        negative(
                                minusSymbol(),
                                expressionNumber()
                        )
                ),
                bracketClose());
    }

    // absolute nodeName predicate quoted text.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        quotedText()
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceQuotedTextWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        whitespace(), quotedText(), whitespace()
                ),
                bracketClose());
    }

    // absolute nodeName predicate attribute.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        attribute(atSign(), attributeName())
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceAttributeWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        whitespace(),
                        attribute(atSign(), attributeName()),
                        whitespace()
                ),
                bracketClose());
    }

    // absolute nodeName predicate function.....................................................................

    // /nodeName [
    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(),
                                parenthesisOpen(),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenWhitespaceParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(),
                                parenthesisOpen(),
                                whitespace(),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceFunctionNameParenOpenWhitespaceParenCloseWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(whitespace(),
                        function(
                                functionName(),
                                parenthesisOpen(),
                                whitespace(),
                                parenthesisClose()
                        ),
                        whitespace()),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberParenCloseBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenFunctionNameParenOpenNumberParenCloseBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberParenCloseBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenFunctionNameParenOpenNumberParenCloseBracketClose();
    }

    private void absoluteNodeNameBracketOpenFunctionNameParenOpenNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(),
                                parenthesisOpen(),
                                expressionNumber(),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberParenCloseBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberParenCloseBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberParenCloseBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberParenCloseBracketClose();
    }

    private void absoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(),
                                parenthesisOpen(),
                                expressionNumber(), comma(), expressionNumber2(),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberNumberParenCloseBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberNumberParenCloseBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberNumberParenCloseBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberNumberParenCloseBracketClose();
    }

    private void absoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(),
                                parenthesisOpen(),
                                expressionNumber(), comma(), expressionNumber2(), comma(), expressionNumber(3),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenQuotedTextParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(),
                                parenthesisOpen(),
                                quotedText(),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpen_functionNameParenOpenQuotedTextParenClose_ParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        function(functionName(), parenthesisOpen(),
                                function(functionName("f2"), parenthesisOpen(), quotedText(), parenthesisClose()),
                                parenthesisClose())),
                bracketClose());
    }

    // absolute nodeName predicate EQ.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberEqualsNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberEqualsNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberEqualsNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberEqualsNumberBracketClose();
    }

    @Test
    private void absoluteNodeNameBracketOpenNumberEqualsNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                expressionNumber(),
                                equalsSymbol(),
                                expressionNumber2()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNegativeNumberEqualsNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNegativeNumberEqualsNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNegativeNumberEqualsNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNegativeNumberEqualsNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenNegativeNumberEqualsNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                negative(
                                        minusSymbol(),
                                        expressionNumber()
                                ),
                                equalsSymbol(),
                                expressionNumber2()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberEqualsNegativeNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberEqualsNegativeNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberEqualsNegativeNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberEqualsNegativeNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenNumberEqualsNegativeNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                expressionNumber(),
                                equalsSymbol(),
                                negative(
                                        minusSymbol(),
                                        expressionNumber2()
                                )
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNegativeNumberEqualsNegativeNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNegativeNumberEqualsNegativeNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNegativeNumberEqualsNegativeNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNegativeNumberEqualsNegativeNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenNegativeNumberEqualsNegativeNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                negative(
                                        minusSymbol(),
                                        expressionNumber()
                                ),
                                equalsSymbol(),
                                negative(
                                        minusSymbol(),
                                        expressionNumber2()
                                )
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenQuotedTextEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(quotedText(), equalsSymbol(), quotedText2())),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionEqualsNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenFunctionEqualsNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionEqualsNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenFunctionEqualsNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenFunctionEqualsNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                function(functionName(), parenthesisOpen(), parenthesisClose()),
                                equalsSymbol(),
                                expressionNumber()
                        )
                ),
                bracketClose());
    }


    @Test
    public void testAbsoluteNodeNameBracketOpenNegativeFunctionEqualsNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNegativeFunctionEqualsNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNegativeFunctionEqualsNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNegativeFunctionEqualsNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenNegativeFunctionEqualsNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                negative(
                                        minusSymbol(),
                                        function(functionName(), parenthesisOpen(), parenthesisClose())
                                ),
                                equalsSymbol(),
                                expressionNumber()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionEqualsFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                function(functionName(), parenthesisOpen(), parenthesisClose()),
                                equalsSymbol(),
                                function(functionName(), parenthesisOpen(), quotedText(), parenthesisClose())
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameEqualsAttributeNameBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                attribute(atSign(), attributeName()),
                                equalsSymbol(),
                                attribute(atSign(), attributeName2())
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenAttributeNameLessThanNumberBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenAttributeNameLessThanNumberBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenAttributeNameLessThanNumberBracketCloseDouble() {
        this.absoluteWildcardBracketOpenAttributeNameLessThanNumberBracketClose();
    }

    private void absoluteWildcardBracketOpenAttributeNameLessThanNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        lessThan(
                                attribute(atSign(), attributeName()),
                                lessThanSymbol(),
                                expressionNumber())),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenAttributeNameLessThanMinusNumberBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenAttributeNameLessThanMinusNumberBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenAttributeNameLessThanMinusNumberBracketCloseDouble() {
        this.absoluteWildcardBracketOpenAttributeNameLessThanMinusNumberBracketClose();
    }

    private void absoluteWildcardBracketOpenAttributeNameLessThanMinusNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        lessThan(
                                attribute(atSign(), attributeName()),
                                lessThanSymbol(),
                                negative(
                                        minusSymbol(),
                                        expressionNumber()
                                )
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameEqualsNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenAttributeNameEqualsNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameEqualsNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenAttributeNameEqualsNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenAttributeNameEqualsNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                attribute(atSign(), attributeName()),
                                equalsSymbol(),
                                expressionNumber())),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameEqualsMinusNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenAttributeNameEqualsMinusNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameEqualsMinusNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenAttributeNameEqualsMinusNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenAttributeNameEqualsMinusNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                attribute(atSign(), attributeName()),
                                equalsSymbol(),
                                negative(
                                        minusSymbol(),
                                        expressionNumber()
                                ))),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                attribute(atSign(), attributeName()),
                                equalsSymbol(),
                                quotedText())),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberEqualsNegativeAttributeBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberEqualsNegativeAttributeBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberEqualsNegativeAttributeBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberEqualsNegativeAttributeBracketClose();
    }

    private void absoluteNodeNameBracketOpenNumberEqualsNegativeAttributeBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                expressionNumber(),
                                equalsSymbol(),
                                negative(
                                        minusSymbol(),
                                        attribute(
                                                atSign(),
                                                attributeName()
                                        )
                                )
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberEqualsQuotedTextBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberEqualsQuotedTextBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberEqualsQuotedTextBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberEqualsQuotedTextBracketClose();
    }

    private void absoluteNodeNameBracketOpenNumberEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(expressionNumber(), equalsSymbol(), quotedText())),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceEqualsWhitespaceNumberWhitespaceBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenWhitespaceNumberWhitespaceEqualsWhitespaceNumberWhitespaceBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceEqualsWhitespaceNumberWhitespaceBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenWhitespaceNumberWhitespaceEqualsWhitespaceNumberWhitespaceBracketClose();
    }

    private void absoluteNodeNameBracketOpenWhitespaceNumberWhitespaceEqualsWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(whitespace(),
                        equalsParserToken(expressionNumber(), whitespace(), equalsSymbol(), whitespace(), expressionNumber2()),
                        whitespace()),
                bracketClose());
    }

    // absolute nodeName predicate GT.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberGreaterThanNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberGreaterThanNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberGreaterThanNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberGreaterThanNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenNumberGreaterThanNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        greaterThan(expressionNumber(), greaterThanSymbol(), expressionNumber2())
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberGreaterThanMinusNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberGreaterThanMinusNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberGreaterThanMinusNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberGreaterThanMinusNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenNumberGreaterThanMinusNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        greaterThan(
                                expressionNumber(),
                                greaterThanSymbol(),
                                negative(
                                        minusSymbol(),
                                        expressionNumber2()
                                ))
                ),
                bracketClose());
    }

    // absolute nodeName predicate GTE.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberGreaterThanEqualNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberGreaterThanEqualNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberGreaterThanEqualNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberGreaterThanEqualNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenNumberGreaterThanEqualNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        greaterThanEquals(expressionNumber(), greaterThanEqualsSymbol(), expressionNumber2())
                ),
                bracketClose());
    }

    // absolute nodeName predicate LT.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberLessThanNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberLessThanNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberLessThanNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberLessThanNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenNumberLessThanNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        lessThan(expressionNumber(), lessThanSymbol(), expressionNumber2())
                ),
                bracketClose());
    }

    // absolute nodeName predicate LTE.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberLessThanEqualNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberLessThanEqualNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberLessThanEqualNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberLessThanEqualNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenNumberLessThanEqualNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        lessThanEquals(expressionNumber(), lessThanEqualsSymbol(), expressionNumber2())
                ),
                bracketClose());
    }

    // absolute nodeName predicate NE.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberNotEqualNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberNotEqualNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberNotEqualNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberNotEqualNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenNumberNotEqualNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        notEquals(expressionNumber(), notEqualsSymbol(), expressionNumber2())
                ),
                bracketClose());
    }

    // absolute nodeName predicate ADD.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberAdditionNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberAdditionNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberAdditionNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberAdditionNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenNumberAdditionNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        addition(expressionNumber(), plusSymbol(), expressionNumber2())
                ),
                bracketClose());
    }

    // absolute nodeName predicate DIVIDE.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberDivisionNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberDivisionNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberDivisionNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberDivisionNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenNumberDivisionNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        division(expressionNumber(), divideSymbol(), expressionNumber2())
                ),
                bracketClose());
    }

    // absolute nodeName predicate MODULO.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberModuloNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberModuloNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberModuloNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberModuloNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenNumberModuloNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        modulo(expressionNumber(), moduloSymbol(), expressionNumber2())),
                bracketClose());
    }

    // absolute nodeName predicate MULTIPLY.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberMultiplyNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberMultiplyNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberMultiplyNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberMultiplyNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenNumberMultiplyNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        multiplication(expressionNumber(), multiplySymbol(), expressionNumber2())
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberMultiplyNumberAddNumberBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberMultiplyNumberAddNumberBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberMultiplyNumberAddNumberBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberMultiplyNumberAddNumberBracketClose();
    }

    private void absoluteNodeNameBracketOpenNumberMultiplyNumberAddNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        addition(
                                multiplication(
                                        expressionNumber(),
                                        multiplySymbol(),
                                        expressionNumber2()),
                                plusSymbol(),
                                expressionNumber3()
                        )
                ),
                bracketClose());
    }

    // absolute nodeName predicate GROUP.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenNumberParenCloseBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenParenOpenNumberParenCloseBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenNumberParenCloseBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenParenOpenNumberParenCloseBracketClose();
    }

    private void absoluteNodeNameBracketOpenParenOpenNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        group(
                                parenthesisOpen(), expressionNumber(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenNegativeNumberParenCloseBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenParenOpenNegativeNumberParenCloseBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenNegativeNumberParenCloseBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenParenOpenNegativeNumberParenCloseBracketClose();
    }

    private void absoluteNodeNameBracketOpenParenOpenNegativeNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        group(
                                parenthesisOpen(),
                                negative(
                                        minusSymbol(),
                                        expressionNumber()
                                ),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenQuotedTextParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        group(
                                parenthesisOpen(), quotedText(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenFunctionParenCloseBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenParenOpenFunctionParenCloseBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenFunctionParenCloseBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenParenOpenFunctionParenCloseBracketClose();
    }

    private void absoluteNodeNameBracketOpenParenOpenFunctionParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        group(
                                parenthesisOpen(),
                                function(
                                        functionName(), parenthesisOpen(), expressionNumber(), parenthesisClose()
                                ),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenNumberGreaterThanNumberParenCloseBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenParenOpenNumberGreaterThanNumberParenCloseBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenNumberGreaterThanNumberParenCloseBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenParenOpenNumberGreaterThanNumberParenCloseBracketClose();
    }

    private void absoluteNodeNameBracketOpenParenOpenNumberGreaterThanNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        group(
                                parenthesisOpen(),
                                greaterThan(expressionNumber(), greaterThanSymbol(), expressionNumber2()),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenParenOpenNumberGreaterThanNumberParenCloseParenCloseBracketCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenParenOpenParenOpenNumberGreaterThanNumberParenCloseParenCloseBracketClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenParenOpenNumberGreaterThanNumberParenCloseParenCloseBracketCloseDouble() {
        this.absoluteNodeNameBracketOpenParenOpenParenOpenNumberGreaterThanNumberParenCloseParenCloseBracketClose();
    }

    private void absoluteNodeNameBracketOpenParenOpenParenOpenNumberGreaterThanNumberParenCloseParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        group(
                                parenthesisOpen(),
                                group(
                                        parenthesisOpen(),
                                        greaterThan(
                                                expressionNumber(),
                                                greaterThanSymbol(),
                                                expressionNumber2()),
                                        parenthesisClose()
                                ),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenNumberParensCloseGreaterThanNumberParenCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenParenNumberParensCloseGreaterThanNumberParenClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenNumberParensCloseGreaterThanNumberParenCloseDouble() {
        this.absoluteNodeNameBracketOpenParenNumberParensCloseGreaterThanNumberParenClose();
    }

    private void absoluteNodeNameBracketOpenParenNumberParensCloseGreaterThanNumberParenClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        greaterThan(
                                group(
                                        parenthesisOpen(),
                                        expressionNumber(),
                                        parenthesisClose()
                                ),
                                greaterThanSymbol(),
                                expressionNumber2()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberBracketGreaterThanParenOpenNumberParensCloseParenCloseBigDecimal() {
        this.absoluteNodeNameBracketOpenNumberBracketGreaterThanParenOpenNumberParensCloseParenClose();
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberBracketGreaterThanParenOpenNumberParensCloseParenCloseDouble() {
        this.absoluteNodeNameBracketOpenNumberBracketGreaterThanParenOpenNumberParensCloseParenClose();
    }

    private void absoluteNodeNameBracketOpenNumberBracketGreaterThanParenOpenNumberParensCloseParenClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        greaterThan(
                                expressionNumber(),
                                greaterThanSymbol(),
                                group(
                                        parenthesisOpen(),
                                        expressionNumber2(),
                                        parenthesisClose()
                                )
                        )
                ),
                bracketClose());
    }

    // absolute wildcard................................................................................................

    @Test
    public void testAbsoluteWildcard() {
        this.parseAndCheck2(absolute(), wildcard());
    }

    // absolute axis wildcard....................................................................................

    @Test
    public void testAbsoluteAncestorWildcardFails() {
        this.parseThrows2(absolute(), ancestor());
    }

    @Test
    public void testAbsoluteAncestorWildcard() {
        this.parseAndCheck2(absolute(), ancestor(), wildcard());
    }

    @Test
    public void testAbsoluteAncestorOrSelfWildcardFails() {
        this.parseThrows2(absolute(), ancestorOrSelf());
    }

    @Test
    public void testAbsoluteAncestorOrSelfWildcard() {
        this.parseAndCheck2(absolute(), ancestorOrSelf(), wildcard());
    }

    @Test
    public void testAbsoluteChildWildcard() {
        this.parseAndCheck2(absolute(), child(), wildcard());
    }

    @Test
    public void testAbsoluteDescendantWildcard() {
        this.parseAndCheck2(absolute(), descendant(), wildcard());
    }

    @Test
    public void testAbsoluteDescendantOrSelfWildcard() {
        this.parseAndCheck2(absolute(), descendantOrSelf(), wildcard());
    }

    @Test
    public void testAbsoluteFirstChildWildcard() {
        this.parseAndCheck2(absolute(), firstChild(), wildcard());
    }

    @Test
    public void testAbsoluteFollowingWildcard() {
        this.parseAndCheck2(absolute(), following(), wildcard());
    }

    @Test
    public void testAbsoluteFollowingSiblingWildcard() {
        this.parseAndCheck2(absolute(), followingSibling(), wildcard());
    }

    @Test
    public void testAbsoluteLastChildWildcard() {
        this.parseAndCheck2(absolute(), lastChild(), wildcard());
    }

    @Test
    public void testAbsolutePrecedingWildcard() {
        this.parseAndCheck2(absolute(), preceding(), wildcard());
    }

    @Test
    public void testAbsolutePrecedingSiblingWildcard() {
        this.parseAndCheck2(absolute(), precedingSibling(), wildcard());
    }

    @Test
    public void testAbsoluteParentWildcard() {
        this.parseAndCheck2(absolute(), parent(), wildcard());
    }

    @Test
    public void testAbsoluteSelfWildcard() {
        this.parseAndCheck2(absolute(), self(), wildcard());
    }

    // absolute wildcard predicate ...................................................................................

    @Test
    public void testIndexMissingNumberFails() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen());
    }

    @Test
    public void testIndexMissingNumberFails2() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen(),
                bracketClose());
    }

    @Test
    public void testIndexMissingBracketCloseFailsDouble() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(expressionNumber()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenIndexBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenIndexBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenIndexBracketCloseDouble() {
        this.absoluteWildcardBracketOpenIndexBracketClose();
    }

    private void absoluteWildcardBracketOpenIndexBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(expressionNumber()),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenWhitespaceIndexWhitespaceBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenWhitespaceIndexWhitespaceBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenWhitespaceIndexWhitespaceBracketCloseDouble() {
        this.absoluteWildcardBracketOpenWhitespaceIndexWhitespaceBracketClose();
    }

    private void absoluteWildcardBracketOpenWhitespaceIndexWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(whitespace(), expressionNumber(), whitespace()),
                bracketClose());
    }

    // absolute wildcard predicate function.....................................................................

    // /wildcard [
    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        functionWithoutArguments()
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenWhitespaceParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(), parenthesisOpen(), whitespace(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenWhitespaceFunctionNameParenOpenWhitespaceParenCloseWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(whitespace(),
                        function(
                                functionName(), parenthesisOpen(), whitespace(), parenthesisClose()
                        ),
                        whitespace()),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberParenCloseBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenFunctionNameParenOpenNumberParenCloseBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberParenCloseBracketCloseDouble() {
        this.absoluteWildcardBracketOpenFunctionNameParenOpenNumberParenCloseBracketClose();
    }

    private void absoluteWildcardBracketOpenFunctionNameParenOpenNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(), parenthesisOpen(), expressionNumber(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberParenCloseBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberParenCloseBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberParenCloseBracketCloseDouble() {
        this.absoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberParenCloseBracketClose();
    }

    private void absoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(), parenthesisOpen(), expressionNumber(), comma(), expressionNumber2(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberCommaNumberParenCloseBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberCommaNumberParenCloseBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberCommaNumberParenCloseBracketCloseDouble() {
        this.absoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberCommaNumberParenCloseBracketClose();
    }


    private void absoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberCommaNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(), parenthesisOpen(), expressionNumber(), comma(), expressionNumber2(), comma(), expressionNumber3(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberWhitespaceCommaNumberParenCloseBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenFunctionNameParenOpenNumberWhitespaceCommaNumberParenCloseBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberWhitespaceCommaNumberParenCloseBracketCloseDouble() {
        this.absoluteWildcardBracketOpenFunctionNameParenOpenNumberWhitespaceCommaNumberParenCloseBracketClose();
    }

    private void absoluteWildcardBracketOpenFunctionNameParenOpenNumberWhitespaceCommaNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(), parenthesisOpen(), expressionNumber(), whitespace(), comma(), expressionNumber2(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaWhitespaceNumberParenCloseBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaWhitespaceNumberParenCloseBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaWhitespaceNumberParenCloseBracketCloseDouble() {
        this.absoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaWhitespaceNumberParenCloseBracketClose();
    }

    private void absoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaWhitespaceNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(), parenthesisOpen(), expressionNumber(), comma(), whitespace(), expressionNumber2(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenQuotedTextParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(), parenthesisOpen(), quotedText(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpen_functionNameParenOpenQuotedTextParenClose_ParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(), parenthesisOpen(),
                                function(
                                        functionName("f2"), parenthesisOpen(), quotedText(), parenthesisClose()
                                ),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    // and ....................................................................................................

    @Test
    public void testAndMissingRightFailsDouble() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        expressionNumber(),
                        andSymbol()));
    }

    @Test
    public void testAndMissingRightFails2Double() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        expressionNumber(),
                        andSymbol()),
                bracketClose());
    }

    @Test
    public void testAndMissingBracketCloseFailsDouble() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        expressionNumber(),
                        andSymbol()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenNumberAndNumberBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenNumberAndNumberBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenNumberAndNumberBracketCloseDouble() {
        this.absoluteWildcardBracketOpenNumberAndNumberBracketClose();
    }

    private void absoluteWildcardBracketOpenNumberAndNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        and(
                                expressionNumber(),
                                andSymbol(),
                                expressionNumber2())),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenQuotedTextAndQuotedTextBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenQuotedTextAndQuotedTextBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenQuotedTextAndQuotedTextBracketCloseDouble() {
        this.absoluteWildcardBracketOpenQuotedTextAndQuotedTextBracketClose();
    }

    private void absoluteWildcardBracketOpenQuotedTextAndQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        and(
                                expressionNumber(),
                                andSymbol(),
                                expressionNumber2())),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionAndWhitespaceFunctionBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenFunctionAndWhitespaceFunctionBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionAndWhitespaceFunctionBracketCloseDouble() {
        this.absoluteWildcardBracketOpenFunctionAndWhitespaceFunctionBracketClose();
    }

    private void absoluteWildcardBracketOpenFunctionAndWhitespaceFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        and(
                                functionWithoutArguments(),
                                andSymbol(),
                                whitespace(),
                                functionWithArguments())),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceAndWhitespaceFunctionWhitespaceBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenWhitespaceFunctionWhitespaceAndWhitespaceFunctionWhitespaceBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceAndWhitespaceFunctionWhitespaceBracketCloseDouble() {
        this.absoluteWildcardBracketOpenWhitespaceFunctionWhitespaceAndWhitespaceFunctionWhitespaceBracketClose();
    }

    private void absoluteWildcardBracketOpenWhitespaceFunctionWhitespaceAndWhitespaceFunctionWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        whitespace(),
                        and(
                                functionWithoutArguments(),
                                whitespace(),
                                andSymbol(),
                                whitespace(),
                                functionWithArguments()
                        ),
                        whitespace()
                ),
                bracketClose());
    }

    // or ....................................................................................................

    @Test
    public void testOrMissingRightFailsDouble() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(expressionNumber(),
                        orSymbol()));
    }

    @Test
    public void testOrMissingRightFails2Double() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(expressionNumber(),
                        orSymbol()
                ),
                bracketClose());
    }

    @Test
    public void testOrMissingBracketCloseFailsBigDecimal() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(expressionNumber(),
                        orSymbol(),
                        expressionNumber()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenNumberOrNumberBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenNumberOrNumberBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenNumberOrNumberBracketCloseDouble() {
        this.absoluteWildcardBracketOpenNumberOrNumberBracketClose();
    }

    private void absoluteWildcardBracketOpenNumberOrNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        or(
                                expressionNumber(),
                                orSymbol(),
                                expressionNumber2())),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenQuotedTextOrQuotedTextBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenQuotedTextOrQuotedTextBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenQuotedTextOrQuotedTextBracketCloseDouble() {
        this.absoluteWildcardBracketOpenQuotedTextOrQuotedTextBracketClose();
    }

    private void absoluteWildcardBracketOpenQuotedTextOrQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        or(
                                expressionNumber(),
                                orSymbol(),
                                expressionNumber2())
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionOrWhitespaceFunctionBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenFunctionOrWhitespaceFunctionBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionOrWhitespaceFunctionBracketCloseDouble() {
        this.absoluteWildcardBracketOpenFunctionOrWhitespaceFunctionBracketClose();
    }

    private void absoluteWildcardBracketOpenFunctionOrWhitespaceFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        or(
                                functionWithoutArguments(),
                                orSymbol(),
                                whitespace(),
                                functionWithArguments())),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionOrNumberOrQuotedTextBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenFunctionOrNumberOrQuotedTextBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionOrNumberOrQuotedTextBracketCloseDouble() {
        this.absoluteWildcardBracketOpenFunctionOrNumberOrQuotedTextBracketClose();
    }

    private void absoluteWildcardBracketOpenFunctionOrNumberOrQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        or(
                                or(
                                        functionWithoutArguments(),
                                        orSymbol(),
                                        whitespace(),
                                        expressionNumber()),
                                orSymbol(),
                                quotedText())
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceOrWhitespaceFunctionWhitespaceBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenWhitespaceFunctionWhitespaceOrWhitespaceFunctionWhitespaceBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceOrWhitespaceFunctionWhitespaceBracketCloseDouble() {
        this.absoluteWildcardBracketOpenWhitespaceFunctionWhitespaceOrWhitespaceFunctionWhitespaceBracketClose();
    }

    private void absoluteWildcardBracketOpenWhitespaceFunctionWhitespaceOrWhitespaceFunctionWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        whitespace(),
                        or(
                                functionWithoutArguments(),
                                whitespace(),
                                orSymbol(),
                                whitespace(),
                                functionWithArguments()
                        ),
                        whitespace()
                ),
                bracketClose());
    }

    // and or...............................................................................................

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenFunctionAndFunctionOrFunctionBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionBracketCloseDouble() {
        this.absoluteWildcardBracketOpenFunctionAndFunctionOrFunctionBracketClose();
    }

    private void absoluteWildcardBracketOpenFunctionAndFunctionOrFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        or(
                                and(functionWithoutArguments(),
                                        andSymbol(),
                                        functionWithArguments()),
                                orSymbol(),
                                functionWithoutArguments())),
                bracketClose());
    }

    // and or and...............................................................................................

    @Test
    public void testAbsoluteWildcardBracketOpenNumberAndNumberOrNumberAndNumberBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenNumberAndNumberOrNumberAndNumberBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenNumberAndNumberOrNumberAndNumberBracketCloseDouble() {
        this.absoluteWildcardBracketOpenNumberAndNumberOrNumberAndNumberBracketClose();
    }

    private void absoluteWildcardBracketOpenNumberAndNumberOrNumberAndNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        or(
                                and(
                                        expressionNumber(1),
                                        andSymbol(),
                                        expressionNumber(2)),
                                orSymbol(),
                                and(
                                        expressionNumber(3),
                                        andSymbol(),
                                        expressionNumber(4))
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionAndFunctionBracketCloseBigDecimal() {
        this.absoluteWildcardBracketOpenFunctionAndFunctionOrFunctionAndFunctionBracketClose();
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionAndFunctionBracketCloseDouble() {
        this.absoluteWildcardBracketOpenFunctionAndFunctionOrFunctionAndFunctionBracketClose();
    }

    private void absoluteWildcardBracketOpenFunctionAndFunctionOrFunctionAndFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        or(
                                and(
                                        functionWithoutArguments(),
                                        andSymbol(),
                                        functionWithArguments()),
                                orSymbol(),
                                and(
                                        functionWithoutArguments(),
                                        andSymbol(),
                                        functionWithoutArguments()
                                )
                        )
                ),
                bracketClose());
    }

    // relative axis node .....................................................................................

    @Test
    public void testRelativeAncestorNodeNameMissingFails() {
        this.parseThrows2(ancestor());
    }

    @Test
    public void testRelativeAncestorNodeName() {
        this.parseAndCheck2(ancestor(), nodeName());
    }

    @Test
    public void testRelativeAncestorOrSelfNodeNameMissingFails() {
        this.parseThrows2(ancestorOrSelf());
    }

    @Test
    public void testRelativeAncestorOrSelfNodeName() {
        this.parseAndCheck2(ancestorOrSelf(), nodeName());
    }

    @Test
    public void testRelativeChildNodeName() {
        this.parseAndCheck2(child(), nodeName());
    }

    @Test
    public void testRelativeDescendantNodeNameMissingFails() {
        this.parseThrows2(descendant());
    }

    @Test
    public void testRelativeDescendantNodeName() {
        this.parseAndCheck2(descendant(), nodeName());
    }

    @Test
    public void testRelativeDescendantOrSelfNodeNameMissingFails() {
        this.parseThrows2(descendantOrSelf());
    }

    @Test
    public void testRelativeDescendantOrSelfNodeName() {
        this.parseAndCheck2(descendantOrSelf(), nodeName());
    }

    @Test
    public void testRelativeFirstChildNodeName() {
        this.parseAndCheck2(firstChild(), nodeName());
    }

    @Test
    public void testRelativeFollowingNodeName() {
        this.parseAndCheck2(following(), nodeName());
    }

    @Test
    public void testRelativeFollowingSiblingNodeName() {
        this.parseAndCheck2(followingSibling(), nodeName());
    }

    @Test
    public void testRelativeLastChildNodeName() {
        this.parseAndCheck2(lastChild(), nodeName());
    }

    @Test
    public void testRelativePrecedingNodeName() {
        this.parseAndCheck2(preceding(), nodeName());
    }

    @Test
    public void testRelativePrecedingSiblingNodeName() {
        this.parseAndCheck2(precedingSibling(), nodeName());
    }

    @Test
    public void testRelativeParentNodeName() {
        this.parseAndCheck2(parent(), nodeName());
    }

    @Test
    public void testRelativeSelfNodeName() {
        this.parseAndCheck2(self(), nodeName());
    }

    // relative axis wildcard....................................................................................

    @Test
    public void testRelativeAncestorWildcardFails() {
        this.parseThrows2(ancestor());
    }

    @Test
    public void testRelativeAncestorWildcard() {
        this.parseAndCheck2(ancestor(), wildcard());
    }

    @Test
    public void testRelativeChildWildcard() {
        this.parseAndCheck2(child(), wildcard());
    }

    @Test
    public void testRelativeDescendantWildcard() {
        this.parseAndCheck2(descendant(), wildcard());
    }

    @Test
    public void testRelativeFirstChildWildcard() {
        this.parseAndCheck2(firstChild(), wildcard());
    }

    @Test
    public void testRelativeFollowingWildcard() {
        this.parseAndCheck2(following(), wildcard());
    }

    @Test
    public void testRelativeFollowingSiblingWildcard() {
        this.parseAndCheck2(followingSibling(), wildcard());
    }

    @Test
    public void testRelativeLastChildWildcard() {
        this.parseAndCheck2(lastChild(), wildcard());
    }

    @Test
    public void testRelativePrecedingWildcard() {
        this.parseAndCheck2(preceding(), wildcard());
    }

    @Test
    public void testRelativePrecedingSiblingWildcard() {
        this.parseAndCheck2(precedingSibling(), wildcard());
    }

    @Test
    public void testRelativeParentWildcard() {
        this.parseAndCheck2(parent(), wildcard());
    }

    @Test
    public void testRelativeSelfWildcard() {
        this.parseAndCheck2(self(), wildcard());
    }

    // helpers....................................................................................................

    @Override
    public Parser<NodeSelectorParserContext> createParser() {
        return NodeSelectorParsers.expression()
                .orReport(ParserReporters.basic())
                .cast();
    }

    @Override
    public NodeSelectorParserContext createContext() {
        return NodeSelectorParserContexts.basic(
                this.expressionNumberKind,
                MathContext.DECIMAL32
        );
    }

    // helpers................................................................................................

    private void parseAndCheck2(final NodeSelectorParserToken... tokens) {
        final List<ParserToken> list = Lists.of(tokens);
        final String text = ParserToken.text(list);

        this.checkEquals(text, text, "text should be all upper case");

        final Parser<NodeSelectorParserContext> parser = this.createParser();
        this.parseAndCheck3(parser,
                text,
                NodeSelectorParserToken.expression(list, text),
                text);

        final List<ParserToken> lower = Arrays.stream(tokens)
                .map(NodeSelectorParsersTestNodeSelectorParserTokenVisitor::toUpper)
                .collect(Collectors.toList());
        final String textUpper = ParserToken.text(lower);

        this.parseAndCheck3(parser,
                textUpper,
                NodeSelectorParserToken.expression(lower, textUpper),
                textUpper);
    }

    private TextCursor parseAndCheck3(final Parser<NodeSelectorParserContext> parser,
                                      final String cursorText,
                                      final ParserToken token,
                                      final String text) {
        final TextCursor after = this.parseAndCheck(parser,
                cursorText,
                token,
                text,
                "");

        // if the expression has a NodeSelectorPredicateParserToken convert it to text and try parse back
        final TestNodeSelectorParserTokenVisitor visitor = new TestNodeSelectorParserTokenVisitor();
        visitor.accept(token);
        final NodeSelectorPredicateParserToken predicate = visitor.predicate;

        if (null != predicate) {
            final String predicateText = predicate.text();

            this.parseAndCheck(NodeSelectorParsers.predicate(),
                    predicateText,
                    predicate,
                    predicateText,
                    "");
        }
        return after;
    }

    static class TestNodeSelectorParserTokenVisitor extends NodeSelectorParserTokenVisitor {
        @Override
        protected Visiting startVisit(final NodeSelectorPredicateParserToken token) {
            this.predicate = token;
            return Visiting.SKIP;
        }

        NodeSelectorPredicateParserToken predicate;
    }

    private void parseThrows2(final NodeSelectorParserToken... tokens) {
        this.parseThrows(this.createParser().orFailIfCursorNotEmpty(ParserReporters.basic()),
                ParserToken.text(Lists.of(tokens)));
    }

    // token factories...............................................................................

    NodeSelectorParserToken absolute() {
        return NodeSelectorParserToken.absolute("/", "/");
    }

    NodeSelectorParserToken addition(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.addition(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken ancestor() {
        return NodeSelectorParserToken.ancestor("ancestor::", "ancestor::");
    }

    NodeSelectorParserToken ancestorOrSelf() {
        return NodeSelectorParserToken.ancestorOrSelf("ancestor-or-self::", "ancestor-or-self::");
    }

    NodeSelectorParserToken and(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.and(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken andSymbol() {
        return NodeSelectorParserToken.andSymbol("and", "and");
    }

    NodeSelectorParserToken atSign() {
        return NodeSelectorParserToken.atSignSymbol("@", "@");
    }

    NodeSelectorParserToken attribute(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.attribute(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken attributeName() {
        return attributeName("attribute1");
    }

    NodeSelectorParserToken attributeName2() {
        return attributeName("attribute2");
    }

    NodeSelectorParserToken attributeName(final String name) {
        return NodeSelectorParserToken.attributeName(NodeSelectorAttributeName.with(name), name);
    }

    NodeSelectorParserToken bracketOpen() {
        return NodeSelectorParserToken.bracketOpenSymbol("[", "[");
    }

    NodeSelectorParserToken bracketClose() {
        return NodeSelectorParserToken.bracketCloseSymbol("]", "]");
    }

    NodeSelectorParserToken child() {
        return NodeSelectorParserToken.child("child::", "child::");
    }

    NodeSelectorParserToken comma() {
        return NodeSelectorParserToken.parameterSeparatorSymbol(",", ",");
    }

    NodeSelectorParserToken descendant() {
        return NodeSelectorParserToken.descendant("descendant::", "descendant::");
    }

    NodeSelectorParserToken descendantOrSelf() {
        return NodeSelectorParserToken.descendantOrSelf("descendant-or-self::", "descendant-or-self::");
    }

    NodeSelectorParserToken descendantOrSelfSlashSlash() {
        return NodeSelectorParserToken.descendantOrSelf("//", "//");
    }

    NodeSelectorParserToken divideSymbol() {
        return NodeSelectorParserToken.divideSymbol("div", "div");
    }

    NodeSelectorParserToken division(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.division(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken equalsParserToken(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.equalsParserToken(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken equalsSymbol() {
        return NodeSelectorParserToken.equalsSymbol("=", "=");
    }

    NodeSelectorParserToken expressionNumber() {
        return expressionNumber(1.5);
    }

    NodeSelectorParserToken expressionNumber2() {
        return expressionNumber(23.5);
    }

    NodeSelectorParserToken expressionNumber3() {
        return expressionNumber(345.5);
    }

    NodeSelectorParserToken expressionNumber(final double value) {
        return NodeSelectorParserToken.expressionNumber(this.expressionNumberKind.create(value), String.valueOf(value));
    }

    NodeSelectorParserToken firstChild() {
        return NodeSelectorParserToken.firstChild("first-child::", "first-child::");
    }

    NodeSelectorParserToken following() {
        return NodeSelectorParserToken.following("following::", "following::");
    }

    NodeSelectorParserToken followingSibling() {
        return NodeSelectorParserToken.followingSibling("following-sibling::", "following-sibling::");
    }

    NodeSelectorParserToken function(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.function(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken functionWithoutArguments() {
        return function(functionName(), parenthesisOpen(), parenthesisClose());
    }

    NodeSelectorParserToken functionWithArguments() {
        return function(functionName(), parenthesisOpen(), expressionNumber(), comma(), quotedText(), parenthesisClose());
    }

    NodeSelectorParserToken functionName() {
        return functionName("contains");
    }

    NodeSelectorParserToken functionName(final String text) {
        return NodeSelectorParserToken.functionName(NodeSelectorFunctionName.with(text), text);
    }

    NodeSelectorParserToken greaterThan(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.greaterThan(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken greaterThanSymbol() {
        return NodeSelectorParserToken.greaterThanSymbol(">", ">");
    }

    NodeSelectorParserToken greaterThanEquals(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.greaterThanEquals(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken greaterThanEqualsSymbol() {
        return NodeSelectorParserToken.greaterThanEqualsSymbol(">=", ">=");
    }

    NodeSelectorParserToken group(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.group(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken lessThan(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.lessThan(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken lessThanSymbol() {
        return NodeSelectorParserToken.lessThanSymbol("<", "<");
    }

    NodeSelectorParserToken lessThanEquals(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.lessThanEquals(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken lessThanEqualsSymbol() {
        return NodeSelectorParserToken.lessThanEqualsSymbol("<=", "<=");
    }

    NodeSelectorParserToken lastChild() {
        return NodeSelectorParserToken.lastChild("last-child::", "last-child::");
    }

    NodeSelectorParserToken minusSymbol() {
        return NodeSelectorParserToken.minusSymbol("-", "-");
    }

    NodeSelectorParserToken modulo(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.modulo(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken moduloSymbol() {
        return NodeSelectorParserToken.moduloSymbol("mod", "mod");
    }

    NodeSelectorParserToken multiplication(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.multiplication(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken multiplySymbol() {
        return NodeSelectorParserToken.multiplySymbol("*", "*");
    }

    NodeSelectorParserToken negative(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.negative(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken nodeName() {
        return nodeName("Node1");
    }

    NodeSelectorParserToken nodeName2() {
        return nodeName("Node22");
    }

    NodeSelectorParserToken nodeName(final String name) {
        return NodeSelectorParserToken.nodeName(NodeSelectorNodeName.with(name), name);
    }

    NodeSelectorParserToken notEquals(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.notEquals(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken notEqualsSymbol() {
        return NodeSelectorParserToken.notEqualsSymbol("!=", "!=");
    }

    NodeSelectorOrParserToken or(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.or(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken orSymbol() {
        return NodeSelectorParserToken.orSymbol("or", "or");
    }

    NodeSelectorParserToken parenthesisOpen() {
        return NodeSelectorParserToken.parenthesisOpenSymbol("(", "(");
    }

    NodeSelectorParserToken parenthesisClose() {
        return NodeSelectorParserToken.parenthesisCloseSymbol(")", ")");
    }

    NodeSelectorParserToken parent() {
        return NodeSelectorParserToken.parentOf("parent::", "parent::");
    }

    NodeSelectorParserToken parentDotDot() {
        return NodeSelectorParserToken.parentOf("..", "..");
    }

    NodeSelectorParserToken plusSymbol() {
        return NodeSelectorParserToken.plusSymbol("+", "+");
    }

    NodeSelectorParserToken preceding() {
        return NodeSelectorParserToken.preceding("preceding::", "preceding::");
    }

    NodeSelectorParserToken precedingSibling() {
        return NodeSelectorParserToken.precedingSibling("preceding-sibling::", "preceding-sibling::");
    }

    NodeSelectorParserToken predicate(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.predicate(Lists.of(tokens), text(tokens));
    }

    NodeSelectorParserToken quotedText() {
        return quotedText("xyz");
    }

    NodeSelectorParserToken quotedText2() {
        return quotedText("qrst");
    }

    NodeSelectorParserToken quotedText(final String value) {
        return NodeSelectorParserToken.quotedText(value, CharSequences.quoteAndEscape(value).toString());
    }

    NodeSelectorParserToken self() {
        return NodeSelectorParserToken.self("self::", "self::");
    }

    NodeSelectorParserToken selfDot() {
        return NodeSelectorParserToken.self(".", ".");
    }

    NodeSelectorParserToken slash() {
        return NodeSelectorParserToken.slashSeparatorSymbol("/", "/");
    }

    NodeSelectorParserToken whitespace() {
        return NodeSelectorParserToken.whitespace("  ", "  ");
    }

    NodeSelectorParserToken wildcard() {
        return NodeSelectorParserToken.wildcard("*", "*");
    }

    private static String text(final NodeSelectorParserToken... tokens) {
        return ParserToken.text(Lists.of(tokens));
    }

}
