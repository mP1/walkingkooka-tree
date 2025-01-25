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
    public void testParseDescendantSlashSlashFails() {
        this.parseThrows2(
            descendantOrSelfSlashSlash(),
            "Invalid character '/' at (1,1) expected ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)})"
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashFails() {
        this.parseThrows2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            "Invalid character '/' at (6,1) expected (([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | Fail)"
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashSlashFails() {
        this.parseThrows2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            slash(),
            "Invalid character '/' at (6,1) expected (([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | Fail)"
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashNodeName() {
        this.parseAndCheck2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            nodeName2()
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashWildcard() {
        this.parseAndCheck2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            wildcard()
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashSelfDot() {
        this.parseAndCheck2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            selfDot()
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashParentDotDot() {
        this.parseAndCheck2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            parentDotDot()
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashAncestorNodeName() {
        this.parseAndCheck2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            ancestor(),
            nodeName2()
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashChildNodeName() {
        this.parseAndCheck2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            child(),
            nodeName2()
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashDescendantNodeName() {
        this.parseAndCheck2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            descendant(),
            nodeName2()
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashFirstChildNodeName() {
        this.parseAndCheck2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            firstChild(),
            nodeName2()
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashFollowingNodeName() {
        this.parseAndCheck2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            following(),
            nodeName2()
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashFollowingSiblingNodeName() {
        this.parseAndCheck2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            followingSibling(),
            nodeName2()
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashLastChildNodeName() {
        this.parseAndCheck2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            lastChild(),
            nodeName2()
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashParentOfNodeName() {
        this.parseAndCheck2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            parent(),
            nodeName2()
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashPrecedingNodeName() {
        this.parseAndCheck2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            preceding(),
            nodeName2()
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashPrecedingSiblingNodeName() {
        this.parseAndCheck2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            precedingSibling(),
            nodeName2()
        );
    }

    @Test
    public void testParseNodeNameDescendantSlashSlashSelfNodeName() {
        this.parseAndCheck2(
            nodeName(),
            descendantOrSelfSlashSlash(),
            self(),
            nodeName2()
        );
    }

    // parentDot ...............................................................................................

    @Test
    public void testParseParentDotDot() {
        this.parseAndCheck2(parentDotDot());
    }

    // selfDot ...............................................................................................

    @Test
    public void testParseSelfDot() {
        this.parseAndCheck2(selfDot());
    }

    // selfDot ...............................................................................................

    @Test
    public void testParseWildcard() {
        this.parseAndCheck2(wildcard());
    }

    // absolute node................................................................................................

    @Test
    public void testParseAbsoluteNodeName() {
        this.parseAndCheck2(absolute(), nodeName());
    }

    // absolute axis node .....................................................................................

    @Test
    public void testParseAbsoluteAncestorNodeNameMissingFails() {
        this.parseThrows2(
            absolute(),
            ancestor(),
            "Invalid character '/' at (1,1) expected ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)})"
        );
    }

    @Test
    public void testParseAbsoluteAncestorNodeName() {
        this.parseAndCheck2(
            absolute(),
            ancestor(),
            nodeName()
        );
    }

    @Test
    public void testParseAbsoluteChildNodeName() {
        this.parseAndCheck2(
            absolute(),
            child(),
            nodeName()
        );
    }

    @Test
    public void testParseAbsoluteDescendantNodeName() {
        this.parseAndCheck2(
            absolute(),
            descendant(),
            nodeName()
        );
    }

    @Test
    public void testParseAbsoluteFirstChildNodeName() {
        this.parseAndCheck2(
            absolute(),
            firstChild(),
            nodeName()
        );
    }

    @Test
    public void testParseAbsoluteFollowingNodeName() {
        this.parseAndCheck2(
            absolute(),
            following(),
            nodeName()
        );
    }

    @Test
    public void testParseAbsoluteFollowingSiblingNodeName() {
        this.parseAndCheck2(
            absolute(),
            followingSibling(),
            nodeName()
        );
    }

    @Test
    public void testParseAbsoluteLastChildNodeName() {
        this.parseAndCheck2(
            absolute(),
            lastChild(),
            nodeName()
        );
    }

    @Test
    public void testParseAbsolutePrecedingNodeName() {
        this.parseAndCheck2(
            absolute(),
            preceding(),
            nodeName()
        );
    }

    @Test
    public void testParseAbsolutePrecedingSiblingNodeName() {
        this.parseAndCheck2(
            absolute(),
            precedingSibling(),
            nodeName()
        );
    }

    @Test
    public void testParseAbsoluteParentNodeName() {
        this.parseAndCheck2(
            absolute(),
            parent(),
            nodeName()
        );
    }

    @Test
    public void testParseAbsoluteSelfNodeName() {
        this.parseAndCheck2(
            absolute(),
            self(),
            nodeName()
        );
    }

    // absolute nodeName predicate child index ..........................................................................

    @Test
    public void testParseAbsoluteNodeNameBracketOpenIndexBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenIndexBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenIndexBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenIndexBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenIndexBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(expressionNumber()),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenWhitespaceIndexWhitespaceBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenWhitespaceIndexWhitespaceBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenWhitespaceIndexWhitespaceBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenWhitespaceIndexWhitespaceBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenWhitespaceIndexWhitespaceBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                whitespace(),
                expressionNumber(),
                whitespace()
            ),
            bracketClose()
        );
    }

    // absolute nodeName predicate expressionNumber.....................................................................

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberBracketClose();
    }


    private void parseAbsoluteNodeNameBracketOpenNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                expressionNumber()
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenDecimalNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenDecimalNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenDecimalNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenDecimalNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenDecimalNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                expressionNumber(12.5)
            ),
            bracketClose()
        );
    }


    @Test
    public void testParseAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                whitespace(),
                expressionNumber(),
                whitespace()
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNegativeNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNegativeNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNegativeNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNegativeNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNegativeNumberBracketClose() {
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
    public void testParseAbsoluteNodeNameBracketOpenQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                quotedText()
            ),
            bracketClose());
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenWhitespaceQuotedTextWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                whitespace(),
                quotedText(),
                whitespace()
            ),
            bracketClose());
    }

    // absolute nodeName predicate attribute.....................................................................

    @Test
    public void testParseAbsoluteNodeNameBracketOpenAttributeBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                attribute(atSign(), attributeName())
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenWhitespaceAttributeWhitespaceBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                whitespace(),
                attribute(atSign(), attributeName()),
                whitespace()
            ),
            bracketClose()
        );
    }

    // absolute nodeName predicate namedFunction.....................................................................

    // /nodeName [
    @Test
    public void testParseAbsoluteNodeNameBracketOpenFunctionNameParenOpenParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                function(
                    functionName(),
                    parenthesisOpen(),
                    parenthesisClose()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenFunctionNameParenOpenWhitespaceParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
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
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenWhitespaceFunctionNameParenOpenWhitespaceParenCloseWhitespaceBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(whitespace(),
                function(
                    functionName(),
                    parenthesisOpen(),
                    whitespace(),
                    parenthesisClose()
                ),
                whitespace()
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberParenCloseBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberParenCloseBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberParenCloseBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberParenCloseBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberParenCloseBracketClose() {
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
    public void testParseAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberParenCloseBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberParenCloseBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberParenCloseBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberParenCloseBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                function(
                    functionName(),
                    parenthesisOpen(),
                    expressionNumber(),
                    comma(),
                    expressionNumber2(),
                    parenthesisClose()
                )
            ),
            bracketClose());
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberNumberParenCloseBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberNumberParenCloseBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberNumberParenCloseBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberNumberParenCloseBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                function(
                    functionName(),
                    parenthesisOpen(),
                    expressionNumber(),
                    comma(),
                    expressionNumber2(),
                    comma(),
                    expressionNumber(3),
                    parenthesisClose()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenFunctionNameParenOpenQuotedTextParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
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
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenFunctionNameParenOpen_functionNameParenOpenQuotedTextParenClose_ParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                function(
                    functionName(),
                    parenthesisOpen(),
                    function(
                        functionName("f2"),
                        parenthesisOpen(),
                        quotedText(),
                        parenthesisClose()
                    ),
                    parenthesisClose()
                )
            ),
            bracketClose()
        );
    }

    // absolute nodeName predicate EQ.....................................................................

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberEqualsNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberEqualsNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberEqualsNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberEqualsNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNumberEqualsNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                equalsParserToken(
                    expressionNumber(),
                    equalsSymbol(),
                    expressionNumber2()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNegativeNumberEqualsNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNegativeNumberEqualsNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNegativeNumberEqualsNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNegativeNumberEqualsNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNegativeNumberEqualsNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
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
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberEqualsNegativeNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberEqualsNegativeNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberEqualsNegativeNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberEqualsNegativeNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNumberEqualsNegativeNumberBracketClose() {
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
    public void testParseAbsoluteNodeNameBracketOpenNegativeNumberEqualsNegativeNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNegativeNumberEqualsNegativeNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNegativeNumberEqualsNegativeNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNegativeNumberEqualsNegativeNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNegativeNumberEqualsNegativeNumberBracketClose() {
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
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenQuotedTextEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                equalsParserToken(
                    quotedText(),
                    equalsSymbol(),
                    quotedText2()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenFunctionEqualsNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenFunctionEqualsNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenFunctionEqualsNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenFunctionEqualsNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenFunctionEqualsNumberBracketClose() {
        this.parseAndCheck2(absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                equalsParserToken(
                    function(
                        functionName(),
                        parenthesisOpen(),
                        parenthesisClose()
                    ),
                    equalsSymbol(),
                    expressionNumber()
                )
            ),
            bracketClose());
    }


    @Test
    public void testParseAbsoluteNodeNameBracketOpenNegativeFunctionEqualsNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNegativeFunctionEqualsNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNegativeFunctionEqualsNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNegativeFunctionEqualsNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNegativeFunctionEqualsNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                equalsParserToken(
                    negative(
                        minusSymbol(),
                        function(
                            functionName(),
                            parenthesisOpen(),
                            parenthesisClose()
                        )
                    ),
                    equalsSymbol(),
                    expressionNumber()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenFunctionEqualsFunctionBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                equalsParserToken(
                    function(
                        functionName(),
                        parenthesisOpen(),
                        parenthesisClose()
                    ),
                    equalsSymbol(),
                    function(
                        functionName(),
                        parenthesisOpen(),
                        quotedText(),
                        parenthesisClose()
                    )
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenAttributeNameEqualsAttributeNameBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                equalsParserToken(
                    attribute(atSign(), attributeName()),
                    equalsSymbol(),
                    attribute(atSign(), attributeName2())
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenAttributeNameLessThanNumberBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenAttributeNameLessThanNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenAttributeNameLessThanNumberBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenAttributeNameLessThanNumberBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenAttributeNameLessThanNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                lessThan(
                    attribute(atSign(), attributeName()),
                    lessThanSymbol(),
                    expressionNumber()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenAttributeNameLessThanMinusNumberBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenAttributeNameLessThanMinusNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenAttributeNameLessThanMinusNumberBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenAttributeNameLessThanMinusNumberBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenAttributeNameLessThanMinusNumberBracketClose() {
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
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenAttributeNameEqualsNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenAttributeNameEqualsNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenAttributeNameEqualsNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenAttributeNameEqualsNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenAttributeNameEqualsNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                equalsParserToken(
                    attribute(atSign(), attributeName()),
                    equalsSymbol(),
                    expressionNumber()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenAttributeNameEqualsMinusNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenAttributeNameEqualsMinusNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenAttributeNameEqualsMinusNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenAttributeNameEqualsMinusNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenAttributeNameEqualsMinusNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                equalsParserToken(
                    attribute(atSign(), attributeName()),
                    equalsSymbol(),
                    negative(
                        minusSymbol(),
                        expressionNumber()
                    )
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenAttributeNameEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                equalsParserToken(
                    attribute(atSign(), attributeName()),
                    equalsSymbol(),
                    quotedText())),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberEqualsNegativeAttributeBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberEqualsNegativeAttributeBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberEqualsNegativeAttributeBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberEqualsNegativeAttributeBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNumberEqualsNegativeAttributeBracketClose() {
        this.parseAndCheck2(
            absolute(),
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
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberEqualsQuotedTextBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberEqualsQuotedTextBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberEqualsQuotedTextBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberEqualsQuotedTextBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNumberEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                equalsParserToken(
                    expressionNumber(),
                    equalsSymbol(),
                    quotedText()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceEqualsWhitespaceNumberWhitespaceBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceEqualsWhitespaceNumberWhitespaceBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceEqualsWhitespaceNumberWhitespaceBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceEqualsWhitespaceNumberWhitespaceBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceEqualsWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(whitespace(),
                equalsParserToken(
                    expressionNumber(),
                    whitespace(),
                    equalsSymbol(),
                    whitespace(),
                    expressionNumber2()
                ),
                whitespace()),
            bracketClose()
        );
    }

    // absolute nodeName predicate GT.....................................................................

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberGreaterThanNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberGreaterThanNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberGreaterThanNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberGreaterThanNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNumberGreaterThanNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                greaterThan(
                    expressionNumber(),
                    greaterThanSymbol(),
                    expressionNumber2()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberGreaterThanMinusNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberGreaterThanMinusNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberGreaterThanMinusNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberGreaterThanMinusNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNumberGreaterThanMinusNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
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
            bracketClose()
        );
    }

    // absolute nodeName predicate GTE.....................................................................

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberGreaterThanEqualNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberGreaterThanEqualNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberGreaterThanEqualNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberGreaterThanEqualNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNumberGreaterThanEqualNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                greaterThanEquals(
                    expressionNumber(),
                    greaterThanEqualsSymbol(),
                    expressionNumber2()
                )
            ),
            bracketClose()
        );
    }

    // absolute nodeName predicate LT.....................................................................

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberLessThanNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberLessThanNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberLessThanNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberLessThanNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNumberLessThanNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                lessThan(
                    expressionNumber(),
                    lessThanSymbol(),
                    expressionNumber2()
                )
            ),
            bracketClose()
        );
    }

    // absolute nodeName predicate LTE.....................................................................

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberLessThanEqualNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberLessThanEqualNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberLessThanEqualNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberLessThanEqualNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNumberLessThanEqualNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                lessThanEquals(
                    expressionNumber(),
                    lessThanEqualsSymbol(),
                    expressionNumber2()
                )
            ),
            bracketClose()
        );
    }

    // absolute nodeName predicate NE.....................................................................

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberNotEqualNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberNotEqualNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberNotEqualNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberNotEqualNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNumberNotEqualNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                notEquals(
                    expressionNumber(),
                    notEqualsSymbol(),
                    expressionNumber2()
                )
            ),
            bracketClose()
        );
    }

    // absolute nodeName predicate ADD.....................................................................

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberAdditionNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberAdditionNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberAdditionNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberAdditionNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNumberAdditionNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                addition(
                    expressionNumber(),
                    plusSymbol(),
                    expressionNumber2()
                )
            ),
            bracketClose()
        );
    }

    // absolute nodeName predicate DIVIDE.....................................................................

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberDivisionNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberDivisionNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberDivisionNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberDivisionNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNumberDivisionNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                division(
                    expressionNumber(),
                    divideSymbol(),
                    expressionNumber2()
                )
            ),
            bracketClose()
        );
    }

    // absolute nodeName predicate MODULO.....................................................................

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberModuloNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberModuloNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberModuloNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberModuloNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNumberModuloNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                modulo(
                    expressionNumber(),
                    moduloSymbol(),
                    expressionNumber2()
                )
            ),
            bracketClose()
        );
    }

    // absolute nodeName predicate MULTIPLY.....................................................................

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberMultiplyNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberMultiplyNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberMultiplyNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberMultiplyNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNumberMultiplyNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                multiplication(
                    expressionNumber(),
                    multiplySymbol(),
                    expressionNumber2()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberMultiplyNumberAddNumberBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberMultiplyNumberAddNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberMultiplyNumberAddNumberBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberMultiplyNumberAddNumberBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNumberMultiplyNumberAddNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
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
            bracketClose()
        );
    }

    // absolute nodeName predicate GROUP.....................................................................

    @Test
    public void testParseAbsoluteNodeNameBracketOpenParenOpenNumberParenCloseBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenParenOpenNumberParenCloseBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenParenOpenNumberParenCloseBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenParenOpenNumberParenCloseBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenParenOpenNumberParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                group(
                    parenthesisOpen(),
                    expressionNumber(),
                    parenthesisClose()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenParenOpenNegativeNumberParenCloseBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenParenOpenNegativeNumberParenCloseBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenParenOpenNegativeNumberParenCloseBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenParenOpenNegativeNumberParenCloseBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenParenOpenNegativeNumberParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
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
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenParenOpenQuotedTextParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                group(
                    parenthesisOpen(),
                    quotedText(),
                    parenthesisClose()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenParenOpenFunctionParenCloseBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenParenOpenFunctionParenCloseBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenParenOpenFunctionParenCloseBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenParenOpenFunctionParenCloseBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenParenOpenFunctionParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                group(
                    parenthesisOpen(),
                    function(
                        functionName(),
                        parenthesisOpen(),
                        expressionNumber(),
                        parenthesisClose()
                    ),
                    parenthesisClose()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenParenOpenNumberGreaterThanNumberParenCloseBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenParenOpenNumberGreaterThanNumberParenCloseBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenParenOpenNumberGreaterThanNumberParenCloseBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenParenOpenNumberGreaterThanNumberParenCloseBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenParenOpenNumberGreaterThanNumberParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
            nodeName(),
            bracketOpen(),
            predicate(
                group(
                    parenthesisOpen(),
                    greaterThan(
                        expressionNumber(),
                        greaterThanSymbol(),
                        expressionNumber2()
                    ),
                    parenthesisClose()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenParenOpenParenOpenNumberGreaterThanNumberParenCloseParenCloseBracketCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenParenOpenParenOpenNumberGreaterThanNumberParenCloseParenCloseBracketClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenParenOpenParenOpenNumberGreaterThanNumberParenCloseParenCloseBracketCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenParenOpenParenOpenNumberGreaterThanNumberParenCloseParenCloseBracketClose();
    }

    private void parseAbsoluteNodeNameBracketOpenParenOpenParenOpenNumberGreaterThanNumberParenCloseParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
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
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenParenNumberParensCloseGreaterThanNumberParenCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenParenNumberParensCloseGreaterThanNumberParenClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenParenNumberParensCloseGreaterThanNumberParenCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenParenNumberParensCloseGreaterThanNumberParenClose();
    }

    private void parseAbsoluteNodeNameBracketOpenParenNumberParensCloseGreaterThanNumberParenClose() {
        this.parseAndCheck2(
            absolute(),
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
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberBracketGreaterThanParenOpenNumberParensCloseParenCloseBigDecimal() {
        this.parseAbsoluteNodeNameBracketOpenNumberBracketGreaterThanParenOpenNumberParensCloseParenClose();
    }

    @Test
    public void testParseAbsoluteNodeNameBracketOpenNumberBracketGreaterThanParenOpenNumberParensCloseParenCloseDouble() {
        this.parseAbsoluteNodeNameBracketOpenNumberBracketGreaterThanParenOpenNumberParensCloseParenClose();
    }

    private void parseAbsoluteNodeNameBracketOpenNumberBracketGreaterThanParenOpenNumberParensCloseParenClose() {
        this.parseAndCheck2(
            absolute(),
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
            bracketClose()
        );
    }

    // absolute wildcard................................................................................................

    @Test
    public void testParseAbsoluteWildcard() {
        this.parseAndCheck2(
            absolute(),
            wildcard()
        );
    }

    // absolute axis wildcard....................................................................................

    @Test
    public void testParseAbsoluteAncestorWildcardFails() {
        this.parseThrows2(
            absolute(),
            ancestor(),
            "Invalid character '/' at (1,1) expected ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)})"
        );
    }

    @Test
    public void testParseAbsoluteAncestorWildcard() {
        this.parseAndCheck2(
            absolute(),
            ancestor(),
            wildcard()
        );
    }

    @Test
    public void testParseAbsoluteAncestorOrSelfWildcardFails() {
        this.parseThrows2(
            absolute(),
            ancestorOrSelf(),
            "Invalid character '/' at (1,1) expected ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)})"
        );
    }

    @Test
    public void testParseAbsoluteAncestorOrSelfWildcard() {
        this.parseAndCheck2(
            absolute(),
            ancestorOrSelf(),
            wildcard()
        );
    }

    @Test
    public void testParseAbsoluteChildWildcard() {
        this.parseAndCheck2(
            absolute(),
            child(),
            wildcard()
        );
    }

    @Test
    public void testParseAbsoluteDescendantWildcard() {
        this.parseAndCheck2(
            absolute(),
            descendant(),
            wildcard()
        );
    }

    @Test
    public void testParseAbsoluteDescendantOrSelfWildcard() {
        this.parseAndCheck2(
            absolute(),
            descendantOrSelf(),
            wildcard()
        );
    }

    @Test
    public void testParseAbsoluteFirstChildWildcard() {
        this.parseAndCheck2(
            absolute(),
            firstChild(),
            wildcard()
        );
    }

    @Test
    public void testParseAbsoluteFollowingWildcard() {
        this.parseAndCheck2(
            absolute(),
            following(),
            wildcard()
        );
    }

    @Test
    public void testParseAbsoluteFollowingSiblingWildcard() {
        this.parseAndCheck2(
            absolute(),
            followingSibling(),
            wildcard()
        );
    }

    @Test
    public void testParseAbsoluteLastChildWildcard() {
        this.parseAndCheck2(
            absolute(),
            lastChild(),
            wildcard()
        );
    }

    @Test
    public void testParseAbsolutePrecedingWildcard() {
        this.parseAndCheck2(
            absolute(),
            preceding(),
            wildcard()
        );
    }

    @Test
    public void testParseAbsolutePrecedingSiblingWildcard() {
        this.parseAndCheck2(
            absolute(),
            precedingSibling(),
            wildcard()
        );
    }

    @Test
    public void testParseAbsoluteParentWildcard() {
        this.parseAndCheck2(
            absolute(),
            parent(),
            wildcard()
        );
    }

    @Test
    public void testParseAbsoluteSelfWildcard() {
        this.parseAndCheck2(
            absolute(),
            self(),
            wildcard()
        );
    }

    // absolute wildcard predicate ...................................................................................

    @Test
    public void testParseIndexMissingNumberFails() {
        this.parseThrows2(
            absolute(),
            wildcard(),
            bracketOpen(),
            "Invalid character '[' at (3,1) expected (([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | Fail)"
        );
    }

    @Test
    public void testParseIndexMissingNumberFails2() {
        this.parseThrows2(
            absolute(),
            wildcard(),
            bracketOpen(),
            bracketClose(),
            "Invalid character '[' at (3,1) expected (([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | Fail)"
        );
    }

    @Test
    public void testParseIndexMissingBracketCloseFailsDouble() {
        this.parseThrows2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(expressionNumber()),
            "Invalid character '[' at (3,1) expected (([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | Fail)"
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenIndexBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenIndexBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenIndexBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenIndexBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenIndexBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(expressionNumber()),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenWhitespaceIndexWhitespaceBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenWhitespaceIndexWhitespaceBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenWhitespaceIndexWhitespaceBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenWhitespaceIndexWhitespaceBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenWhitespaceIndexWhitespaceBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                whitespace(),
                expressionNumber(),
                whitespace()
            ),
            bracketClose()
        );
    }

    // absolute wildcard predicate namedFunction.....................................................................

    // /wildcard [
    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionNameParenOpenParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                functionWithoutArguments()
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionNameParenOpenWhitespaceParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                function(
                    functionName(),
                    parenthesisOpen(),
                    whitespace(),
                    parenthesisClose()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenWhitespaceFunctionNameParenOpenWhitespaceParenCloseWhitespaceBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(whitespace(),
                function(
                    functionName(),
                    parenthesisOpen(),
                    whitespace(),
                    parenthesisClose()
                ),
                whitespace()),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberParenCloseBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberParenCloseBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberParenCloseBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberParenCloseBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                function(
                    functionName(),
                    parenthesisOpen(),
                    expressionNumber(),
                    parenthesisClose()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberParenCloseBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberParenCloseBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberParenCloseBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberParenCloseBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                function(
                    functionName(),
                    parenthesisOpen(),
                    expressionNumber(),
                    comma(),
                    expressionNumber2(),
                    parenthesisClose()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberCommaNumberParenCloseBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberCommaNumberParenCloseBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberCommaNumberParenCloseBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberCommaNumberParenCloseBracketClose();
    }


    private void parseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberCommaNumberParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                function(
                    functionName(),
                    parenthesisOpen(),
                    expressionNumber(),
                    comma(),
                    expressionNumber2(),
                    comma(),
                    expressionNumber3(),
                    parenthesisClose()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberWhitespaceCommaNumberParenCloseBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberWhitespaceCommaNumberParenCloseBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberWhitespaceCommaNumberParenCloseBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberWhitespaceCommaNumberParenCloseBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberWhitespaceCommaNumberParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                function(
                    functionName(), parenthesisOpen(), expressionNumber(), whitespace(), comma(), expressionNumber2(), parenthesisClose()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaWhitespaceNumberParenCloseBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaWhitespaceNumberParenCloseBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaWhitespaceNumberParenCloseBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaWhitespaceNumberParenCloseBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaWhitespaceNumberParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                function(
                    functionName(), parenthesisOpen(), expressionNumber(), comma(), whitespace(), expressionNumber2(), parenthesisClose()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionNameParenOpenQuotedTextParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                function(
                    functionName(), parenthesisOpen(), quotedText(), parenthesisClose()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionNameParenOpen_functionNameParenOpenQuotedTextParenClose_ParenCloseBracketClose() {
        this.parseAndCheck2(
            absolute(),
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
            bracketClose()
        );
    }

    // and ....................................................................................................

    @Test
    public void testParseAndMissingRightFailsDouble() {
        this.parseThrows2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                expressionNumber(),
                andSymbol()
            ),
            "Invalid character '[' at (3,1) expected (([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | Fail)"
        );
    }

    @Test
    public void testParseAndMissingRightFails2Double() {
        this.parseThrows2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                expressionNumber(),
                andSymbol()
            ),
            bracketClose(),
            "Invalid character '[' at (3,1) expected (([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | Fail)"
        );
    }

    @Test
    public void testParseAndMissingBracketCloseFailsDouble() {
        this.parseThrows2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                expressionNumber(),
                andSymbol()
            ),
            "Invalid character '[' at (3,1) expected (([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | Fail)"
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenNumberAndNumberBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenNumberAndNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenNumberAndNumberBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenNumberAndNumberBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenNumberAndNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                and(
                    expressionNumber(),
                    andSymbol(),
                    expressionNumber2()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenQuotedTextAndQuotedTextBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenQuotedTextAndQuotedTextBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenQuotedTextAndQuotedTextBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenQuotedTextAndQuotedTextBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenQuotedTextAndQuotedTextBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                and(
                    expressionNumber(),
                    andSymbol(),
                    expressionNumber2()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionAndWhitespaceFunctionBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenFunctionAndWhitespaceFunctionBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionAndWhitespaceFunctionBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenFunctionAndWhitespaceFunctionBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenFunctionAndWhitespaceFunctionBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                and(
                    functionWithoutArguments(),
                    andSymbol(),
                    whitespace(),
                    functionWithArguments()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceAndWhitespaceFunctionWhitespaceBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceAndWhitespaceFunctionWhitespaceBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceAndWhitespaceFunctionWhitespaceBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceAndWhitespaceFunctionWhitespaceBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceAndWhitespaceFunctionWhitespaceBracketClose() {
        this.parseAndCheck2(
            absolute(),
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
            bracketClose()
        );
    }

    // or ....................................................................................................

    @Test
    public void testParseOrMissingRightFailsDouble() {
        this.parseThrows2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                expressionNumber(),
                orSymbol()
            ),
            "Invalid character '[' at (3,1) expected (([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | Fail)"
        );
    }

    @Test
    public void testParseOrMissingRightFails2Double() {
        this.parseThrows2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                expressionNumber(),
                orSymbol()
            ),
            bracketClose(),
            "Invalid character '[' at (3,1) expected (([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | Fail)"
        );
    }

    @Test
    public void testParseOrMissingBracketCloseFailsBigDecimal() {
        this.parseThrows2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                expressionNumber(),
                orSymbol(),
                expressionNumber()
            ),
            "Invalid character '[' at (3,1) expected (([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)}) | Fail)"
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenNumberOrNumberBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenNumberOrNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenNumberOrNumberBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenNumberOrNumberBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenNumberOrNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                or(
                    expressionNumber(),
                    orSymbol(),
                    expressionNumber2()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenQuotedTextOrQuotedTextBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenQuotedTextOrQuotedTextBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenQuotedTextOrQuotedTextBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenQuotedTextOrQuotedTextBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenQuotedTextOrQuotedTextBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                or(
                    expressionNumber(),
                    orSymbol(),
                    expressionNumber2()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionOrWhitespaceFunctionBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenFunctionOrWhitespaceFunctionBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionOrWhitespaceFunctionBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenFunctionOrWhitespaceFunctionBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenFunctionOrWhitespaceFunctionBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                or(
                    functionWithoutArguments(),
                    orSymbol(),
                    whitespace(),
                    functionWithArguments()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionOrNumberOrQuotedTextBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenFunctionOrNumberOrQuotedTextBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionOrNumberOrQuotedTextBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenFunctionOrNumberOrQuotedTextBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenFunctionOrNumberOrQuotedTextBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                or(
                    or(
                        functionWithoutArguments(),
                        orSymbol(),
                        whitespace(),
                        expressionNumber()
                    ),
                    orSymbol(),
                    quotedText()
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceOrWhitespaceFunctionWhitespaceBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceOrWhitespaceFunctionWhitespaceBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceOrWhitespaceFunctionWhitespaceBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceOrWhitespaceFunctionWhitespaceBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceOrWhitespaceFunctionWhitespaceBracketClose() {
        this.parseAndCheck2(
            absolute(),
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
            bracketClose()
        );
    }

    // and or...............................................................................................

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                or(
                    and(
                        functionWithoutArguments(),
                        andSymbol(),
                        functionWithArguments()
                    ),
                    orSymbol(),
                    functionWithoutArguments()
                )
            ),
            bracketClose()
        );
    }

    // and or and...............................................................................................

    @Test
    public void testParseAbsoluteWildcardBracketOpenNumberAndNumberOrNumberAndNumberBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenNumberAndNumberOrNumberAndNumberBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenNumberAndNumberOrNumberAndNumberBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenNumberAndNumberOrNumberAndNumberBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenNumberAndNumberOrNumberAndNumberBracketClose() {
        this.parseAndCheck2(
            absolute(),
            wildcard(),
            bracketOpen(),
            predicate(
                or(
                    and(
                        expressionNumber(1),
                        andSymbol(),
                        expressionNumber(2)
                    ),
                    orSymbol(),
                    and(
                        expressionNumber(3),
                        andSymbol(),
                        expressionNumber(4)
                    )
                )
            ),
            bracketClose()
        );
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionAndFunctionBracketCloseBigDecimal() {
        this.parseAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionAndFunctionBracketClose();
    }

    @Test
    public void testParseAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionAndFunctionBracketCloseDouble() {
        this.parseAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionAndFunctionBracketClose();
    }

    private void parseAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionAndFunctionBracketClose() {
        this.parseAndCheck2(
            absolute(),
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
            bracketClose()
        );
    }

    // relative axis node .....................................................................................

    @Test
    public void testParseRelativeAncestorNodeNameMissingFails() {
        this.parseThrows2(
            ancestor(),
            "Invalid character 'a' at (1,1) expected ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)})"
        );
    }

    @Test
    public void testParseRelativeAncestorNodeName() {
        this.parseAndCheck2(ancestor(), nodeName());
    }

    @Test
    public void testParseRelativeAncestorOrSelfNodeNameMissingFails() {
        this.parseThrows2(
            ancestorOrSelf(),
            "Invalid character 'a' at (1,1) expected ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)})"
        );
    }

    @Test
    public void testParseRelativeAncestorOrSelfNodeName() {
        this.parseAndCheck2(
            ancestorOrSelf(),
            nodeName()
        );
    }

    @Test
    public void testParseRelativeChildNodeName() {
        this.parseAndCheck2(
            child(),
            nodeName()
        );
    }

    @Test
    public void testParseRelativeDescendantNodeNameMissingFails() {
        this.parseThrows2(
            descendant(),
            "Invalid character 'd' at (1,1) expected ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)})"
        );
    }

    @Test
    public void testParseRelativeDescendantNodeName() {
        this.parseAndCheck2(
            descendant(),
            nodeName()
        );
    }

    @Test
    public void testParseRelativeDescendantOrSelfNodeNameMissingFails() {
        this.parseThrows2(
            descendantOrSelf(),
            "Invalid character 'd' at (1,1) expected ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)})"
        );
    }

    @Test
    public void testParseRelativeDescendantOrSelfNodeName() {
        this.parseAndCheck2(
            descendantOrSelf(),
            nodeName()
        );
    }

    @Test
    public void testParseRelativeFirstChildNodeName() {
        this.parseAndCheck2(
            firstChild(),
            nodeName()
        );
    }

    @Test
    public void testParseRelativeFollowingNodeName() {
        this.parseAndCheck2(
            following(),
            nodeName()
        );
    }

    @Test
    public void testParseRelativeFollowingSiblingNodeName() {
        this.parseAndCheck2(
            followingSibling(),
            nodeName()
        );
    }

    @Test
    public void testParseRelativeLastChildNodeName() {
        this.parseAndCheck2(
            lastChild(),
            nodeName()
        );
    }

    @Test
    public void testParseRelativePrecedingNodeName() {
        this.parseAndCheck2(
            preceding(),
            nodeName()
        );
    }

    @Test
    public void testParseRelativePrecedingSiblingNodeName() {
        this.parseAndCheck2(
            precedingSibling(),
            nodeName()
        );
    }

    @Test
    public void testParseRelativeParentNodeName() {
        this.parseAndCheck2(
            parent(),
            nodeName()
        );
    }

    @Test
    public void testParseRelativeSelfNodeName() {
        this.parseAndCheck2(
            self(),
            nodeName()
        );
    }

    // relative axis wildcard....................................................................................

    @Test
    public void testParseRelativeAncestorWildcardFails() {
        this.parseThrows2(
            ancestor(),
            "Invalid character 'a' at (1,1) expected ([(ABSOLUTE_AXIS_NODE_PREDICATE | DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)], {(DESCENDANTORSELFSLASHSLASH_AXIS_NODE_PREDICATE | SLASH_PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE | PARENTDOTDOT_SELFDOT_SLASH_AXIS_NODE_PREDICATE)})"
        );
    }

    @Test
    public void testParseRelativeAncestorWildcard() {
        this.parseAndCheck2(
            ancestor(),
            wildcard()
        );
    }

    @Test
    public void testParseRelativeChildWildcard() {
        this.parseAndCheck2(
            child(),
            wildcard()
        );
    }

    @Test
    public void testParseRelativeDescendantWildcard() {
        this.parseAndCheck2(
            descendant(),
            wildcard()
        );
    }

    @Test
    public void testParseRelativeFirstChildWildcard() {
        this.parseAndCheck2(
            firstChild(),
            wildcard()
        );
    }

    @Test
    public void testParseRelativeFollowingWildcard() {
        this.parseAndCheck2(
            following(),
            wildcard()
        );
    }

    @Test
    public void testParseRelativeFollowingSiblingWildcard() {
        this.parseAndCheck2(
            followingSibling(),
            wildcard()
        );
    }

    @Test
    public void testParseRelativeLastChildWildcard() {
        this.parseAndCheck2(
            lastChild(),
            wildcard()
        );
    }

    @Test
    public void testParseRelativePrecedingWildcard() {
        this.parseAndCheck2(
            preceding(),
            wildcard()
        );
    }

    @Test
    public void testParseRelativePrecedingSiblingWildcard() {
        this.parseAndCheck2(
            precedingSibling(),
            wildcard()
        );
    }

    @Test
    public void testParseRelativeParentWildcard() {
        this.parseAndCheck2(
            parent(),
            wildcard()
        );
    }

    @Test
    public void testParseRelativeSelfWildcard() {
        this.parseAndCheck2(
            self(),
            wildcard()
        );
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

    private void parseThrows2(final NodeSelectorParserToken token,
                              final String expected) {
        this.parseThrows2(
            Lists.of(token),
            expected
        );
    }

    private void parseThrows2(final NodeSelectorParserToken token,
                              final NodeSelectorParserToken token2,
                              final String expected) {
        this.parseThrows2(
            Lists.of(
                token,
                token2
            ),
            expected
        );
    }

    private void parseThrows2(final NodeSelectorParserToken token,
                              final NodeSelectorParserToken token2,
                              final NodeSelectorParserToken token3,
                              final String expected) {
        this.parseThrows2(
            Lists.of(
                token,
                token2,
                token3
            ),
            expected
        );
    }

    private void parseThrows2(final NodeSelectorParserToken token,
                              final NodeSelectorParserToken token2,
                              final NodeSelectorParserToken token3,
                              final NodeSelectorParserToken token4,
                              final String expected) {
        this.parseThrows2(
            Lists.of(
                token,
                token2,
                token3,
                token4
            ),
            expected
        );
    }

    private void parseThrows2(final NodeSelectorParserToken token,
                              final NodeSelectorParserToken token2,
                              final NodeSelectorParserToken token3,
                              final NodeSelectorParserToken token4,
                              final NodeSelectorParserToken token5,
                              final String expected) {
        this.parseThrows2(
            Lists.of(
                token,
                token2,
                token3,
                token4,
                token5
            ),
            expected
        );
    }

    private void parseThrows2(final List<NodeSelectorParserToken> tokens,
                              final String expected) {
        this.parseThrows(
            this.createParser()
                .orFailIfCursorNotEmpty(ParserReporters.basic()),
            ParserToken.text(tokens),
            expected
        );
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
